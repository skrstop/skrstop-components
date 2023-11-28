
package com.jphoebe.framework.components.starter.database.service.impl;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.jphoebe.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.jphoebe.framework.components.starter.database.constant.DatabaseConst;
import com.jphoebe.framework.components.starter.database.entity.*;
import com.jphoebe.framework.components.starter.database.entity.expand.CreatorExpand;
import com.jphoebe.framework.components.starter.database.entity.expand.UpdaterExpand;
import com.jphoebe.framework.components.starter.database.entity.version.*;
import com.jphoebe.framework.components.starter.database.mapper.SuperMapper;
import com.jphoebe.framework.components.starter.database.service.SuperService;
import com.jphoebe.framework.components.starter.database.utils.SuperParamsUtil;
import com.jphoebe.framework.components.util.constant.StringPoolConst;
import com.jphoebe.framework.components.util.value.data.ObjectUtil;
import com.jphoebe.framework.components.util.value.data.StrUtil;
import lombok.Getter;
import org.apache.ibatis.binding.MapperMethod;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 业务封装基础类
 *
 * @param <M> mapper
 * @param <T> model
 * @author 蒋时华
 */
public abstract class SuperServiceImpl<M extends SuperMapper<T>, T extends AbstractBaseEntity> extends ServiceImpl<M, T> implements SuperService<T> {

    private static final String UPDATE_BY = "update_by";
    private static final String UPDATE_TIME = "update_time";
    private static final String UPDATER = "updater";
    private static final String CREATE_BY = "create_by";
    private static final String CREATE_TIME = "create_time";
    private static final String CREATOR = "creator";
    private static final String VERSION = "version";
    private static final String DELETED = "deleted";

    @Getter
    private String idColumnName = StrUtil.toUnderlineCase(ReflectUtil.newInstance(entityClass).getIdName());

    private void setCreateInfo(T entity) {
        if (!this.isAutoSetCreateExtraInfo()) {
            return;
        }
        if (entity instanceof AbstractCreateTimeBaseEntity) {
            AbstractCreateTimeBaseEntity abstractCreateTimeBaseEntity = (AbstractCreateTimeBaseEntity) entity;
            if (this.onlySetCreateInfoWhenNull()
                    && ObjectUtil.isNull(abstractCreateTimeBaseEntity.getCreateTime())) {
                abstractCreateTimeBaseEntity.setCreateTime(LocalDateTime.now());
            } else if (!this.onlySetCreateInfoWhenNull()) {
                abstractCreateTimeBaseEntity.setCreateTime(LocalDateTime.now());
            }
            if (this.onlySetCreateInfoWhenNull()
                    && ObjectUtil.isNull(abstractCreateTimeBaseEntity.getCreateBy())) {
                abstractCreateTimeBaseEntity.setCreateBy(this.getOptionUserId());
            } else if (!this.onlySetCreateInfoWhenNull()) {
                abstractCreateTimeBaseEntity.setCreateBy(this.getOptionUserId());
            }
        } else if (entity instanceof AbstractTimeVersionBaseEntity) {
            AbstractTimeVersionBaseEntity abstractTimeVersionBaseEntity = (AbstractTimeVersionBaseEntity) entity;
            if (this.onlySetCreateInfoWhenNull()
                    && ObjectUtil.isNull(abstractTimeVersionBaseEntity.getCreateTime())) {
                abstractTimeVersionBaseEntity.setCreateTime(LocalDateTime.now());
            } else if (!this.onlySetCreateInfoWhenNull()) {
                abstractTimeVersionBaseEntity.setCreateTime(LocalDateTime.now());
            }
            if (this.onlySetCreateInfoWhenNull()
                    && ObjectUtil.isNull(abstractTimeVersionBaseEntity.getCreateBy())) {
                abstractTimeVersionBaseEntity.setCreateBy(this.getOptionUserId());
            } else if (!this.onlySetCreateInfoWhenNull()) {
                abstractTimeVersionBaseEntity.setCreateBy(this.getOptionUserId());
            }
        } else if (entity instanceof AbstractCreateTimeVersionBaseEntity) {
            AbstractCreateTimeVersionBaseEntity abstractCreateTimeVersionBaseEntity = (AbstractCreateTimeVersionBaseEntity) entity;
            if (this.onlySetCreateInfoWhenNull()
                    && ObjectUtil.isNull(abstractCreateTimeVersionBaseEntity.getCreateTime())) {
                abstractCreateTimeVersionBaseEntity.setCreateTime(LocalDateTime.now());
            } else if (!this.onlySetCreateInfoWhenNull()) {
                abstractCreateTimeVersionBaseEntity.setCreateTime(LocalDateTime.now());
            }
            if (this.onlySetCreateInfoWhenNull()
                    && ObjectUtil.isNull(abstractCreateTimeVersionBaseEntity.getCreateBy())) {
                abstractCreateTimeVersionBaseEntity.setCreateBy(this.getOptionUserId());
            } else if (!this.onlySetCreateInfoWhenNull()) {
                abstractCreateTimeVersionBaseEntity.setCreateBy(this.getOptionUserId());
            }
        }
        if (entity instanceof CreatorExpand) {
            CreatorExpand creatorExpand = (CreatorExpand) entity;
            if (this.onlySetCreateInfoWhenNull()
                    && ObjectUtil.isNull(creatorExpand.getCreator())) {
                creatorExpand.setCreator(this.getOperator());
            } else if (!this.onlySetCreateInfoWhenNull()) {
                creatorExpand.setCreator(this.getOperator());
            }
        }
        this.setUpdateInfo(entity);
    }

    private void setUpdateInfo(T entity) {
        if (!this.isAutoSetUpdateExtraInfo()) {
            return;
        }
        if (entity instanceof AbstractTimeBaseEntity) {
            AbstractTimeBaseEntity abstractTimeBaseEntity = (AbstractTimeBaseEntity) entity;
            if (this.onlySetUpdateInfoWhenNull()
                    && ObjectUtil.isNull(abstractTimeBaseEntity.getUpdateTime())) {
                abstractTimeBaseEntity.setUpdateTime(LocalDateTime.now());
            } else if (!this.onlySetCreateInfoWhenNull()) {
                abstractTimeBaseEntity.setUpdateTime(LocalDateTime.now());
            }
            if (this.onlySetUpdateInfoWhenNull()
                    && ObjectUtil.isNull(abstractTimeBaseEntity.getUpdateBy())) {
                abstractTimeBaseEntity.setUpdateBy(this.getOptionUserId());
            } else if (!this.onlySetUpdateInfoWhenNull()) {
                abstractTimeBaseEntity.setUpdateBy(this.getOptionUserId());
            }
        } else if (entity instanceof AbstractTimeVersionBaseEntity) {
            AbstractTimeVersionBaseEntity abstractTimeVersionBaseEntity = (AbstractTimeVersionBaseEntity) entity;
            if (this.onlySetUpdateInfoWhenNull()
                    && ObjectUtil.isNull(abstractTimeVersionBaseEntity.getUpdateTime())) {
                abstractTimeVersionBaseEntity.setUpdateTime(LocalDateTime.now());
            } else if (!this.onlySetUpdateInfoWhenNull()) {
                abstractTimeVersionBaseEntity.setUpdateTime(LocalDateTime.now());
            }
            if (this.onlySetUpdateInfoWhenNull()
                    && ObjectUtil.isNull(abstractTimeVersionBaseEntity.getUpdateBy())) {
                abstractTimeVersionBaseEntity.setUpdateBy(this.getOptionUserId());
            } else if (!this.onlySetUpdateInfoWhenNull()) {
                abstractTimeVersionBaseEntity.setUpdateBy(this.getOptionUserId());
            }
        }
        if (entity instanceof UpdaterExpand) {
            UpdaterExpand updaterExpand = (UpdaterExpand) entity;
            if (this.onlySetUpdateInfoWhenNull()
                    && ObjectUtil.isNull(updaterExpand.getUpdater())) {
                updaterExpand.setUpdater(this.getOperator());
            } else if (!this.onlySetUpdateInfoWhenNull()) {
                updaterExpand.setUpdater(this.getOperator());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean save(@NotNull T entity) {
        this.setCreateInfo(entity);
        return super.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean saveBatch(@NotEmpty Collection<T> entityList) {
        entityList.forEach(item -> this.setCreateInfo(item));
        String sqlStatement = getSqlStatement(SqlMethod.INSERT_ONE);
        return executeBatch(entityList, entityList.size(), (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean update(Wrapper<T> updateWrapper) {
        return super.update(this.setUpdateTimeUpdateInfo(updateWrapper));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        this.setUpdateInfo(entity);
        return super.update(entity, updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean updateById(@NotNull T entity) {
        this.setUpdateInfo(entity);
        return super.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean updateBatchById(@NotEmpty Collection<T> entityList) {
        entityList.forEach(entity -> this.updateById(entity));
        String sqlStatement = getSqlStatement(SqlMethod.UPDATE_BY_ID);
        return executeBatch(entityList, entityList.size(), (sqlSession, entity) -> {
            MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
            param.put(Constants.ENTITY, entity);
            sqlSession.update(sqlStatement, param);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean saveOrUpdate(T entity) {
        if (null != entity) {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
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
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean saveOrUpdateBatch(Collection<T> entityList) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
        return SqlHelper.saveOrUpdateBatch(this.entityClass, this.mapperClass, this.log, entityList, entityList.size()
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
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean removeById(Serializable id) {
        String tableName = StrUtil.toUnderlineCase(entityClass.getSimpleName());
        int result = this.getBaseMapper().removePhysicalById(id, tableName, idColumnName);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean removeByMap(Map<String, Object> columnMap) {
        String tableName = StrUtil.toUnderlineCase(entityClass.getSimpleName());
        int result = this.getBaseMapper().removePhysicalByMap(columnMap, tableName);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean remove(Wrapper<T> queryWrapper) {
        String tableName = StrUtil.toUnderlineCase(entityClass.getSimpleName());
        int result = this.getBaseMapper().removePhysicalByCustom(queryWrapper.getSqlSegment(), queryWrapper, tableName);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean removeByIds(Collection<?> ids) {
        String tableName = StrUtil.toUnderlineCase(entityClass.getSimpleName());
        int result = this.getBaseMapper().removePhysicalByIds(ids, tableName, idColumnName);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean removeLogicByIds(Collection<?> ids) {
        UpdateWrapper<T> updateWrapper = (UpdateWrapper<T>) this.setRemoveUpdateInfo(Wrappers.<T>update(), false);
        updateWrapper.in(idColumnName, ids);
        return super.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean removeLogicById(Serializable id) {
        UpdateWrapper<T> updateWrapper = (UpdateWrapper<T>) this.setRemoveUpdateInfo(Wrappers.<T>update(), false);
        updateWrapper.eq(idColumnName, id);
        return super.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean removeLogic(Wrapper<T> updateWrapper) {
        Wrapper<T> newUpdateWrapper = this.setRemoveUpdateInfo(updateWrapper, false);
        return super.update(newUpdateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean removeLogicByMap(Map<String, Object> columnMap) {
        UpdateWrapper<T> updateWrapper = (UpdateWrapper<T>) this.setRemoveUpdateInfo(Wrappers.<T>update(), false);
        updateWrapper.allEq(columnMap);
        return super.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean undoRemoveLogicByIds(Collection<?> ids) {
        UpdateWrapper<T> updateWrapper = (UpdateWrapper<T>) this.setRemoveUpdateInfo(Wrappers.<T>update(), true);
        updateWrapper.in(idColumnName, ids);
        return super.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean undoRemoveLogicById(Serializable id) {
        UpdateWrapper<T> updateWrapper = (UpdateWrapper<T>) this.setRemoveUpdateInfo(Wrappers.<T>update(), true);
        updateWrapper.eq(idColumnName, id);
        return super.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean undoRemoveLogic(Wrapper<T> updateWrapper) {
        Wrapper<T> newUpdateWrapper = this.setRemoveUpdateInfo(updateWrapper, true);
        return super.update(newUpdateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DatabaseConst.TRANSACTION_NAME)
    public boolean undoRemoveLogicByMap(Map<String, Object> columnMap) {
        UpdateWrapper<T> updateWrapper = (UpdateWrapper<T>) this.setRemoveUpdateInfo(Wrappers.<T>update(), true);
        updateWrapper.allEq(columnMap);
        return super.update(updateWrapper);
    }

    private Wrapper<T> setRemoveUpdateInfo(Wrapper<T> updateWrapper, boolean undo) {
        if (ObjectUtil.isNull(updateWrapper)) {
            return updateWrapper;
        }
        if (AbstractDeletedBaseEntity.class.isAssignableFrom(entityClass)
                || AbstractTimeDeletedBaseEntity.class.isAssignableFrom(entityClass)
                || AbstractOperatorTimeDeletedBaseEntity.class.isAssignableFrom(entityClass)
                || AbstractTimeDeletedVersionBaseEntity.class.isAssignableFrom(entityClass)
                || AbstractCreatorTimeDeletedVersionBaseEntity.class.isAssignableFrom(entityClass)
                || AbstractCreateTimeDeletedVersionBaseEntity.class.isAssignableFrom(entityClass)
                || AbstractOperatorTimeDeletedVersionBaseEntity.class.isAssignableFrom(entityClass)
                || AbstractDeletedVersionBaseEntity.class.isAssignableFrom(entityClass)) {
            if (updateWrapper instanceof UpdateWrapper) {
                UpdateWrapper<T> update = (UpdateWrapper<T>) updateWrapper;
                update.set(DELETED, !undo);
            } else if (updateWrapper instanceof LambdaUpdateWrapper) {
                LambdaUpdateWrapper<T> lambdaUpdateWrapper = (LambdaUpdateWrapper<T>) updateWrapper;
                Map<String, String> paramMap = SuperParamsUtil.getParamMap(lambdaUpdateWrapper.getSqlSet());
                if (ObjectUtil.isNotNull(paramMap.get(DELETED))) {
                    Map<String, Object> paramNameValuePairs = lambdaUpdateWrapper.getParamNameValuePairs();
                    paramNameValuePairs.put(paramMap.get(DELETED), !undo ? 1 : 0);
                } else {
                    lambdaUpdateWrapper.setSql(DELETED + StringPoolConst.EQUALS + (!undo ? 1 : 0));
                }
            }
        } else {
            throw new NotSupportedException("不支持逻辑删除操作，请确认Entity是否有错");
        }
        this.setUpdateTimeUpdateInfo(updateWrapper);
        return updateWrapper;

    }

    private Wrapper<T> setUpdateTimeUpdateInfo(@NotNull Wrapper<T> wrapper) {
        if (ObjectUtil.isNull(wrapper)) {
            return wrapper;
        }
        if (!this.isAutoSetUpdateExtraInfo()) {
            return wrapper;
        }
        if (AbstractTimeBaseEntity.class.isAssignableFrom(entityClass)
                || AbstractTimeVersionBaseEntity.class.isAssignableFrom(entityClass)) {
            if (wrapper instanceof UpdateWrapper) {
                UpdateWrapper<T> updateWrapper = (UpdateWrapper<T>) wrapper;
                Map<String, Object> paramNameValueMap = ((UpdateWrapper<T>) wrapper).getParamNameValuePairs();
                Map<String, String> paramMap = SuperParamsUtil.getParamMap(updateWrapper.getSqlSet());
                if (this.onlySetUpdateInfoWhenNull()
                        && (!paramMap.containsKey(UPDATE_TIME) || ObjectUtil.isNull(paramNameValueMap.get(paramMap.get(UPDATE_TIME))))) {
                    updateWrapper.set(UPDATE_TIME, LocalDateTime.now());
                }
                if (this.onlySetUpdateInfoWhenNull()
                        && (!paramMap.containsKey(UPDATE_BY) || ObjectUtil.isNull(paramNameValueMap.get(paramMap.get(UPDATE_BY))))) {
                    updateWrapper.set(UPDATE_BY, this.getOptionUserId());
                }
                // 无论如何都覆盖则直接修改
                if (!this.onlySetUpdateInfoWhenNull()) {
                    updateWrapper.set(UPDATE_TIME, LocalDateTime.now());
                    updateWrapper.set(UPDATE_BY, this.getOptionUserId());
                }

                if (AbstractOperatorTimeVersionBaseEntity.class.isAssignableFrom(entityClass) ||
                        AbstractOperatorTimeBaseEntity.class.isAssignableFrom(entityClass) ||
                        UpdaterExpand.class.isAssignableFrom(entityClass)) {
                    if (this.onlySetUpdateInfoWhenNull()
                            && (!paramMap.containsKey(UPDATER) || ObjectUtil.isNull(paramNameValueMap.get(paramMap.get(UPDATER))))) {
                        updateWrapper.set(UPDATER, this.getOperator());
                    }
                    // 无论如何都覆盖则直接修改
                    if (!this.onlySetUpdateInfoWhenNull()) {
                        updateWrapper.set(UPDATER, this.getOperator());
                    }
                }
                return updateWrapper;
            } else if (wrapper instanceof LambdaUpdateWrapper) {
                LambdaUpdateWrapper<T> lambdaUpdateWrapper = (LambdaUpdateWrapper<T>) wrapper;
                // origin
                Map<String, Object> paramNameValueMap = ((LambdaUpdateWrapper<T>) wrapper).getParamNameValuePairs();
                Map<String, String> paramMap = SuperParamsUtil.getParamMap(lambdaUpdateWrapper.getSqlSet());
                // update_time
                if (this.onlySetUpdateInfoWhenNull()
                        && (!paramMap.containsKey(UPDATE_TIME) || ObjectUtil.isNull(paramNameValueMap.get(paramMap.get(UPDATE_TIME))))) {
                    if (ObjectUtil.isNotNull(paramMap.get(UPDATE_TIME))) {
                        paramNameValueMap.put(paramMap.get(UPDATE_TIME), LocalDateTime.now());
                    } else {
                        lambdaUpdateWrapper.setSql(UPDATE_TIME + StringPoolConst.EQUALS + "now()");
                    }
                }
                // update_by
                if (this.onlySetUpdateInfoWhenNull()
                        && (!paramMap.containsKey(UPDATE_BY) || ObjectUtil.isNull(paramNameValueMap.get(paramMap.get(UPDATE_BY))))) {
                    if (ObjectUtil.isNotNull(paramMap.get(UPDATE_BY))) {
                        paramNameValueMap.put(paramMap.get(UPDATE_BY), this.getOptionUserId());
                    } else {
                        lambdaUpdateWrapper.setSql(UPDATE_BY + StringPoolConst.EQUALS + this.getOptionUserId());
                    }
                }
                // 无论如何都覆盖则直接修改
                if (!this.onlySetUpdateInfoWhenNull()) {
                    if (ObjectUtil.isNotNull(paramMap.get(UPDATE_TIME))) {
                        paramNameValueMap.put(paramMap.get(UPDATE_TIME), LocalDateTime.now());
                    } else {
                        lambdaUpdateWrapper.setSql(UPDATE_TIME + StringPoolConst.EQUALS + "now()");
                    }
                    if (ObjectUtil.isNotNull(paramMap.get(UPDATE_BY))) {
                        paramNameValueMap.put(paramMap.get(UPDATE_BY), this.getOptionUserId());
                    } else {
                        lambdaUpdateWrapper.setSql(UPDATE_BY + StringPoolConst.EQUALS + this.getOptionUserId());
                    }
                }
                // updater
                if (AbstractOperatorTimeVersionBaseEntity.class.isAssignableFrom(entityClass) ||
                        AbstractOperatorTimeBaseEntity.class.isAssignableFrom(entityClass) ||
                        UpdaterExpand.class.isAssignableFrom(entityClass)) {
                    if (this.onlySetUpdateInfoWhenNull()
                            && (!paramMap.containsKey(UPDATER) || ObjectUtil.isNull(paramNameValueMap.get(paramMap.get(UPDATER))))) {
                        if (ObjectUtil.isNotNull(paramMap.get(UPDATER))) {
                            paramNameValueMap.put(paramMap.get(UPDATER), this.getOperator());
                        } else {
                            lambdaUpdateWrapper.setSql(UPDATER + StringPoolConst.EQUALS + StringPoolConst.QUOTE + this.getOperator() + StringPoolConst.QUOTE);
                        }
                    }
                    if (!this.onlySetUpdateInfoWhenNull()) {
                        if (ObjectUtil.isNotNull(paramMap.get(UPDATER))) {
                            paramNameValueMap.put(paramMap.get(UPDATER), this.getOperator());
                        } else {
                            lambdaUpdateWrapper.setSql(UPDATER + StringPoolConst.EQUALS + StringPoolConst.QUOTE + this.getOperator() + StringPoolConst.QUOTE);
                        }
                    }
                }
                return lambdaUpdateWrapper;
            }
        }
        return wrapper;
    }

}
