
package com.skrstop.framework.components.starter.database.repository.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.skrstop.framework.components.starter.database.annotation.property.*;
import com.skrstop.framework.components.starter.database.configuration.GlobalDatabaseProperties;
import com.skrstop.framework.components.starter.database.constant.DatabaseConst;
import com.skrstop.framework.components.starter.database.entity.AbstractBaseEntity;
import com.skrstop.framework.components.starter.database.mapper.SuperMapper;
import com.skrstop.framework.components.starter.database.repository.SuperRepository;
import com.skrstop.framework.components.starter.database.utils.EntityPropertiesUtil;
import com.skrstop.framework.components.starter.database.utils.SuperParamsUtil;
import com.skrstop.framework.components.util.constant.DateFormatConst;
import com.skrstop.framework.components.util.constant.StringPoolConst;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.binding.MapperMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 业务封装基础类
 *
 * @param <M> mapper
 * @param <T> model
 * @author 蒋时华
 */
@SuppressWarnings("all")
public abstract class SuperRepositoryImpl<M extends SuperMapper<T>, T extends AbstractBaseEntity> extends MPJBaseServiceImpl<M, T> implements SuperRepository<T> {

    private Map<Class<?>, Map<String, Class<?>>> propertyFieldCache = null;
    private TableInfo tableInfo = null;

    @Autowired
    private GlobalDatabaseProperties globalDatabaseProperties;

    @PostConstruct
    private void init() {
        this.propertyFieldCache = EntityPropertiesUtil.tableProperties(super.getEntityClass());
        this.tableInfo = TableInfoHelper.getTableInfo(super.getEntityClass());
        Map<String, Class<?>> columnIds = propertyFieldCache.get(PropertyId.class);
        if (ObjectUtil.isEmpty(columnIds)) {
            throw new NotSupportedException("实体类" + super.getEntityClass().getName() + "必须且只能有一个主键字段");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean save(@NotNull T entity) {
        this.setCreateInfo(entity);
        return super.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean saveBatch(@NotEmpty Collection<T> entityList) {
        entityList.forEach(this::setCreateInfo);
        String sqlStatement = getSqlStatement(SqlMethod.INSERT_ONE);
        return executeBatch(entityList, entityList.size(), (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean update(Wrapper<T> updateWrapper) {
        return super.update(this.setUpdateTimeUpdateInfo(updateWrapper));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        this.setUpdateInfo(entity);
        return super.update(entity, updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean updateById(@NotNull T entity) {
        this.setUpdateInfo(entity);
        return super.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean updateBatchById(@NotEmpty Collection<T> entityList) {
        entityList.forEach(this::updateById);
        String sqlStatement = getSqlStatement(SqlMethod.UPDATE_BY_ID);
        return executeBatch(entityList, entityList.size(), (sqlSession, entity) -> {
            MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
            param.put(Constants.ENTITY, entity);
            sqlSession.update(sqlStatement, param);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean saveOrUpdate(T entity) {
        if (null != entity) {
            Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
            String keyProperty = tableInfo.getKeyProperty();
            Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
            Object idVal = tableInfo.getPropertyValue(entity, tableInfo.getKeyProperty());
            if (StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal))) {
                this.save(entity);
            } else {
                this.updateById(entity);
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean saveOrUpdateBatch(Collection<T> entityList) {
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
        return SqlHelper.saveOrUpdateBatch(super.getSqlSessionFactory(), super.getEntityClass(), this.log, entityList, entityList.size()
                , (sqlSession, entity) -> {
                    Object idVal = tableInfo.getPropertyValue(entity, keyProperty);
                    boolean save = StringUtils.checkValNull(idVal)
                            || CollectionUtils.isEmpty(sqlSession.selectList(getSqlStatement(SqlMethod.SELECT_BY_ID), entity));
                    if (save) {
                        this.setCreateInfo(entity);
                    }
                    return save;
                }, (sqlSession, entity) -> {
                    this.setUpdateInfo(entity);
                    MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                    param.put(Constants.ENTITY, entity);
                    sqlSession.update(getSqlStatement(SqlMethod.UPDATE_BY_ID), param);
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean removeById(Serializable id) {
        String tableName = StrUtil.toUnderlineCase(super.getEntityClass().getSimpleName());
        String columnNameId = EntityPropertiesUtil.getColumnNameId(propertyFieldCache);
        int result = this.getBaseMapper().removePhysicalById(id, tableName, columnNameId);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean removeByMap(Map<String, Object> columnMap) {
        String tableName = StrUtil.toUnderlineCase(super.getEntityClass().getSimpleName());
        int result = this.getBaseMapper().removePhysicalByMap(columnMap, tableName);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean remove(Wrapper<T> queryWrapper) {
        String tableName = StrUtil.toUnderlineCase(super.getEntityClass().getSimpleName());
        int result = this.getBaseMapper().removePhysicalByCustom(queryWrapper.getSqlSegment(), queryWrapper, tableName);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean removeByIds(Collection<?> ids) {
        String tableName = StrUtil.toUnderlineCase(super.getEntityClass().getSimpleName());
        String columnNameId = EntityPropertiesUtil.getColumnNameId(propertyFieldCache);
        int result = this.getBaseMapper().removePhysicalByIds(ids, tableName, columnNameId);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean removeLogicByIds(Collection<?> ids) {
        UpdateWrapper<T> updateWrapper = (UpdateWrapper<T>) this.setRemoveUpdateInfo(Wrappers.<T>update(), false);
        String columnNameId = EntityPropertiesUtil.getColumnNameId(propertyFieldCache);
        updateWrapper.in(columnNameId, ids);
        return super.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean removeLogicById(Serializable id) {
        UpdateWrapper<T> updateWrapper = (UpdateWrapper<T>) this.setRemoveUpdateInfo(Wrappers.<T>update(), false);
        String columnNameId = EntityPropertiesUtil.getColumnNameId(propertyFieldCache);
        updateWrapper.eq(columnNameId, id);
        return super.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean removeLogic(Wrapper<T> updateWrapper) {
        Wrapper<T> newUpdateWrapper = this.setRemoveUpdateInfo(updateWrapper, false);
        return super.update(newUpdateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean removeLogicByMap(Map<String, Object> columnMap) {
        UpdateWrapper<T> updateWrapper = (UpdateWrapper<T>) this.setRemoveUpdateInfo(Wrappers.<T>update(), false);
        updateWrapper.allEq(columnMap);
        return super.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean undoRemoveLogicByIds(Collection<?> ids) {
        UpdateWrapper<T> updateWrapper = (UpdateWrapper<T>) this.setRemoveUpdateInfo(Wrappers.<T>update(), true);
        String columnNameId = EntityPropertiesUtil.getColumnNameId(propertyFieldCache);
        updateWrapper.in(columnNameId, ids);
        return super.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean undoRemoveLogicById(Serializable id) {
        UpdateWrapper<T> updateWrapper = (UpdateWrapper<T>) this.setRemoveUpdateInfo(Wrappers.<T>update(), true);
        String columnNameId = EntityPropertiesUtil.getColumnNameId(propertyFieldCache);
        updateWrapper.eq(columnNameId, id);
        return super.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean undoRemoveLogic(Wrapper<T> updateWrapper) {
        Wrapper<T> newUpdateWrapper = this.setRemoveUpdateInfo(updateWrapper, true);
        return super.update(newUpdateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME_DATABASE)
    public boolean undoRemoveLogicByMap(Map<String, Object> columnMap) {
        UpdateWrapper<T> updateWrapper = (UpdateWrapper<T>) this.setRemoveUpdateInfo(Wrappers.<T>update(), true);
        updateWrapper.allEq(columnMap);
        return super.update(updateWrapper);
    }

    private Wrapper<T> setRemoveUpdateInfo(Wrapper<T> updateWrapper, boolean undo) {
        if (ObjectUtil.isNull(updateWrapper)) {
            return updateWrapper;
        }
        if (!propertyFieldCache.containsKey(PropertyDeleted.class)) {
            throw new NotSupportedException("不支持逻辑删除操作，请确认Entity是否有错");
        }
        if (updateWrapper instanceof UpdateWrapper) {
            UpdateWrapper<T> update = (UpdateWrapper<T>) updateWrapper;
            EntityPropertiesUtil.getColumnNames(propertyFieldCache, PropertyDeleted.class)
                    .forEach(property -> update.set(property, !undo));
        } else if (updateWrapper instanceof LambdaUpdateWrapper) {
            LambdaUpdateWrapper<T> lambdaUpdateWrapper = (LambdaUpdateWrapper<T>) updateWrapper;
            Map<String, String> paramMap = SuperParamsUtil.getParamMap(lambdaUpdateWrapper.getSqlSet());
            EntityPropertiesUtil.getColumnNames(propertyFieldCache, PropertyDeleted.class)
                    .forEach(property -> {
                        if (ObjectUtil.isNotNull(paramMap.get(property))) {
                            Map<String, Object> paramNameValuePairs = lambdaUpdateWrapper.getParamNameValuePairs();
                            paramNameValuePairs.put(paramMap.get(property), !undo ? 1 : 0);
                        } else {
                            lambdaUpdateWrapper.setSql(property + StringPoolConst.EQUALS + (!undo ? 1 : 0));
                        }
                    });
        }
        this.setUpdateTimeUpdateInfo(updateWrapper);
        return updateWrapper;
    }

    private void setCreateInfo(T entity) {
        if (!this.isAutoSetCreateExtraInfo()) {
            return;
        }
        Set<String> createByPropertyNames = EntityPropertiesUtil.getColumnNames(propertyFieldCache, PropertyCreateBy.class);
        EntityPropertiesUtil.setFieldValue(this, tableInfo, entity, createByPropertyNames, this.getOptionUserId());

        Set<String> createTimePropertyNames = EntityPropertiesUtil.getColumnNames(propertyFieldCache, PropertyCreateTime.class);
        EntityPropertiesUtil.setFieldValue(this, tableInfo, entity, createTimePropertyNames, LocalDateTime.now());

        Set<String> creatorPropertyNames = EntityPropertiesUtil.getColumnNames(propertyFieldCache, PropertyCreator.class);
        EntityPropertiesUtil.setFieldValue(this, tableInfo, entity, creatorPropertyNames, this.getOperator());

        this.setUpdateInfo(entity);
    }

    private void setUpdateInfo(T entity) {
        if (!this.isAutoSetUpdateExtraInfo()) {
            return;
        }
        Set<String> updateByPropertyNames = EntityPropertiesUtil.getColumnNames(propertyFieldCache, PropertyUpdateBy.class);
        EntityPropertiesUtil.setFieldValue(this, tableInfo, entity, updateByPropertyNames, this.getOptionUserId());

        Set<String> UpdateTimePropertyNames = EntityPropertiesUtil.getColumnNames(propertyFieldCache, PropertyUpdateTime.class);
        EntityPropertiesUtil.setFieldValue(this, tableInfo, entity, UpdateTimePropertyNames, LocalDateTime.now());

        Set<String> UpdaterPropertyNames = EntityPropertiesUtil.getColumnNames(propertyFieldCache, PropertyUpdater.class);
        EntityPropertiesUtil.setFieldValue(this, tableInfo, entity, UpdaterPropertyNames, this.getOperator());
    }

    private Wrapper<T> setUpdateTimeUpdateInfo(@NotNull Wrapper<T> wrapper) {
        if (ObjectUtil.isNull(wrapper)) {
            return wrapper;
        }
        if (!this.isAutoSetUpdateExtraInfo()) {
            return wrapper;
        }
        Set<String> updateByPropertyNames = EntityPropertiesUtil.getColumnNames(propertyFieldCache, PropertyUpdateBy.class);
        Set<String> updateTimePropertyNames = EntityPropertiesUtil.getColumnNames(propertyFieldCache, PropertyUpdateTime.class);
        Set<String> updatorPropertyNames = EntityPropertiesUtil.getColumnNames(propertyFieldCache, PropertyUpdater.class);
        if (CollectionUtil.isEmpty(updateByPropertyNames)
                && CollectionUtil.isEmpty(updateTimePropertyNames)
                && CollectionUtil.isEmpty(updatorPropertyNames)) {
            return wrapper;
        }
        if (wrapper instanceof UpdateWrapper) {
            UpdateWrapper<T> updateWrapper = (UpdateWrapper<T>) wrapper;
            Map<String, Object> paramNameValueMap = ((UpdateWrapper<T>) wrapper).getParamNameValuePairs();
            Map<String, String> paramMap = SuperParamsUtil.getParamMap(updateWrapper.getSqlSet());
            EntityPropertiesUtil.setFieldValue(this, updateWrapper, paramNameValueMap, paramMap, updateByPropertyNames, this.getOptionUserId());
            EntityPropertiesUtil.setFieldValue(this, updateWrapper, paramNameValueMap, paramMap, updateTimePropertyNames, LocalDateTime.now());
            EntityPropertiesUtil.setFieldValue(this, updateWrapper, paramNameValueMap, paramMap, updatorPropertyNames, this.getOperator());
            return updateWrapper;
        } else if (wrapper instanceof LambdaUpdateWrapper) {
            LambdaUpdateWrapper<T> lambdaUpdateWrapper = (LambdaUpdateWrapper<T>) wrapper;
            // origin
            Map<String, Object> paramNameValueMap = ((LambdaUpdateWrapper<T>) wrapper).getParamNameValuePairs();
            Map<String, String> paramMap = SuperParamsUtil.getParamMap(lambdaUpdateWrapper.getSqlSet());
            EntityPropertiesUtil.setFieldValue(this, lambdaUpdateWrapper, paramNameValueMap, paramMap, updateByPropertyNames, this.getOptionUserId());
            EntityPropertiesUtil.setFieldValue(this, lambdaUpdateWrapper, paramNameValueMap, paramMap, updateTimePropertyNames, LocalDateTimeUtil.format(LocalDateTime.now(), DateFormatConst.NORM_DATETIME_NS_PATTERN));
            EntityPropertiesUtil.setFieldValue(this, lambdaUpdateWrapper, paramNameValueMap, paramMap, updatorPropertyNames, this.getOperator());
            return lambdaUpdateWrapper;
        }
        return wrapper;
    }

}
