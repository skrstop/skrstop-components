package com.skrstop.framework.components.starter.mongodb.repository.impl;

import cn.hutool.core.lang.Pair;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.skrstop.framework.components.core.common.response.page.ListSimplePageData;
import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.skrstop.framework.components.starter.mongodb.annotation.property.*;
import com.skrstop.framework.components.starter.mongodb.configuration.GlobalMongodbProperties;
import com.skrstop.framework.components.starter.mongodb.configuration.generator.IdentifierGenerator;
import com.skrstop.framework.components.starter.mongodb.entity.AbstractBaseEntity;
import com.skrstop.framework.components.starter.mongodb.repository.SuperRepository;
import com.skrstop.framework.components.starter.mongodb.utils.EntityPropertiesUtil;
import com.skrstop.framework.components.starter.mongodb.wrapper.PageQuery;
import com.skrstop.framework.components.util.value.data.ArrayUtil;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.format.TextFormatUtil;
import dev.morphia.*;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import dev.morphia.query.updates.UpdateOperator;
import dev.morphia.query.updates.UpdateOperators;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务封装基础类
 *
 * @param <T> model
 * @author 蒋时华
 */
@SuppressWarnings("all")
public abstract class SuperRepositoryImpl<T extends AbstractBaseEntity, KEY extends Serializable> implements SuperRepository<T> {

    public static final int ASC = 1;
    public static final int DESC = -1;

    private Map<Class<?>, Map<String, Pair<String, Class<?>>>> propertyFieldCache = null;
    @Autowired
    protected Datastore datastore;
    @Autowired
    protected IdentifierGenerator identifierGenerator;
    @Autowired
    private GlobalMongodbProperties globalMongodbProperties;

    protected Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    //    protected Class<T> entityIdClass = (Class<T>) ((ParameterizedType) entityClass.getGenericSuperclass()).getActualTypeArguments()[0];
//    protected Class<T> entityIdClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];

    @PostConstruct
    private void initPropertiesFormat() {
        this.propertyFieldCache = EntityPropertiesUtil.tableProperties(globalMongodbProperties.getPropertyNaming(), entityClass);
        Map<String, Pair<String, Class<?>>> columnIds = this.propertyFieldCache.get(PropertyId.class);
        if (ObjectUtil.isEmpty(columnIds)) {
            throw new NotSupportedException("实体类" + entityClass.getName() + "必须且只能有一个主键字段");
        }
    }

    @Override
    public Query<T> find(List<Filter> filters) {
        Filter[] filterArray = new Filter[filters.size()];
        for (int i = 0; i < filters.size(); i++) {
            filterArray[i] = filters.get(i);
        }
        return this.datastore.find(entityClass)
                .filter(filterArray);
    }

    @Override
    public T find(List<Filter> filters, FindOptions findOptions) {
        final Query<T> ts = this.find(filters);
        final long count = ts.count();
        if (count > 1) {
            throw new RuntimeException(TextFormatUtil.formatString("找到了多条记录，数量：{}", count));
        }
        return ts
                .iterator(ObjectUtil.isNull(findOptions) ? new FindOptions() : findOptions)
                .tryNext();
    }

    @Override
    public List<T> list(List<Filter> filters, FindOptions findOptions) {
        return this.find(filters)
                .iterator(ObjectUtil.isNull(findOptions) ? new FindOptions() : findOptions)
                .toList();
    }

    @Override
    public ListSimplePageData<T> findPage(PageQuery pageQuery, List<Filter> filters) {
        return this.findPage(pageQuery, filters, null);
    }

    @Override
    public ListSimplePageData<T> findPage(PageQuery pageQuery, List<Filter> filters, FindOptions findOptions) {
        final Query<T> query = this.find(filters);
        // 获取总条数
        final long count = query.count();
        if (ObjectUtil.isNull(findOptions)) {
            findOptions = new FindOptions();
        }
        List<Sort> sorts = new ArrayList<>();
        // 排序：正排序
        for (String asc : pageQuery.getAscs()) {
            sorts.add(Sort.ascending(asc));
        }
        // 排序：倒排序
        for (String desc : pageQuery.getDescs()) {
            sorts.add(Sort.descending(desc));
        }
        // 分页数据
        findOptions.skip((pageQuery.getPageNumber() - 1) * pageQuery.getPageSize())
                .limit(pageQuery.getPageSize())
                .sort(ArrayUtil.toArray(sorts, Sort.class));
        final List<T> list = query.iterator(findOptions).toList();

        // 组装数据
        ListSimplePageData<T> result = new ListSimplePageData();
        result.setPageSize(pageQuery.getPageSize());
        result.setPageNumber(pageQuery.getPageNumber());
        result.setTotal(count);
        result.setRows(list);
        return result;
    }

    @Override
    public T findById(Serializable id) {
        String columnNameId = EntityPropertiesUtil.getColumnPropertyNameId(propertyFieldCache);
        return this.find(Arrays.asList(Filters.eq(columnNameId, id)))
                .iterator().tryNext();
    }

    @Override
    public List<T> findByIds(List<Serializable> ids) {
        String columnNameId = EntityPropertiesUtil.getColumnPropertyNameId(propertyFieldCache);
        return this.find(Arrays.asList(Filters.in(columnNameId, ids)))
                .iterator().toList();
    }

    @Override
    public T save(@NotNull T entity) {
        return this.save(entity, new InsertOneOptions());
    }

    @Override
    public T save(@NotNull T entity, InsertOneOptions insertOneOptions) {
        if (insertOneOptions == null) {
            insertOneOptions = new InsertOneOptions();
        }
        this.setId(entity);
        this.setCreateInfo(entity);
        return this.datastore.save(entity, insertOneOptions);
    }

    @Override
    public List<T> saveBatch(@NotEmpty List<T> entityList) {
        return this.saveBatch(entityList, new InsertManyOptions());
    }

    @Override
    public List<T> saveBatch(@NotEmpty List<T> entityList, InsertManyOptions insertManyOptions) {
        if (insertManyOptions == null) {
            insertManyOptions = new InsertManyOptions();
        }
        entityList.forEach(item -> {
            this.setId(item);
            this.setCreateInfo(item);
        });
        return this.datastore.save(entityList, insertManyOptions);
    }

    @Override
    public UpdateResult update(Query<T> query, List<UpdateOperator> updates) {
        if (CollectionUtil.isEmpty(updates)) {
            return null;
        }
        updates.addAll(this.setUpdateTimeUpdateInfo(updates));
        UpdateOperator[] updatesArray;
        Set<String> columnNameVersion = EntityPropertiesUtil.getColumnPropertyNames(propertyFieldCache, PropertyVersion.class);
        if (CollectionUtil.isNotEmpty(columnNameVersion)) {
            // 有版本校验
            updatesArray = new UpdateOperator[updates.size() + columnNameVersion.size()];
            int index = 0;
            for (String columnName : columnNameVersion) {
                updatesArray[index] = UpdateOperators.inc(columnName);
                index++;
            }
            for (int i = 0; i < updates.size(); i++) {
                updatesArray[i + columnNameVersion.size()] = updates.get(i);
            }
        } else {
            updatesArray = new UpdateOperator[updates.size()];
            for (int i = 0; i < updates.size(); i++) {
                updatesArray[i] = updates.get(i);
            }
        }
        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.multi(true);
        return query.update(updateOptions, updatesArray);
    }

    @Override
    public UpdateResult updateWithVersion(Long currentVersion, List<Filter> filters, List<UpdateOperator> updates) {
        Filter[] filterArray;
        Set<String> columnNameVersion = EntityPropertiesUtil.getColumnPropertyNames(propertyFieldCache, PropertyVersion.class);
        if (CollectionUtil.isNotEmpty(columnNameVersion)) {
            // 有版本校验
            filterArray = new Filter[filters.size() + columnNameVersion.size()];
            int index = 0;
            for (String columnName : columnNameVersion) {
                filterArray[index] = Filters.eq(columnName, currentVersion);
                index++;
            }
            for (int i = 0; i < filters.size(); i++) {
                filterArray[i + columnNameVersion.size()] = filters.get(i);
            }
        } else {
            filterArray = new Filter[filters.size()];
            for (int i = 0; i < filters.size(); i++) {
                filterArray[i] = filters.get(i);
            }
        }
        Query<T> query = this.datastore.find(entityClass).filter(filterArray);
        return this.update(query, updates);
    }

    @Override
    public DeleteResult removeById(Serializable id) {
        String columnNameId = EntityPropertiesUtil.getColumnPropertyNameId(propertyFieldCache);
        return this.find(Arrays.asList(Filters.eq(columnNameId, id)))
                .delete();
    }

    @Override
    public DeleteResult removeByIds(Collection<Serializable> ids) {
        String columnNameId = EntityPropertiesUtil.getColumnPropertyNameId(propertyFieldCache);
        DeleteOptions deleteOptions = new DeleteOptions();
        deleteOptions.multi(true);
        return this.find(Arrays.asList(Filters.in(columnNameId, ids)))
                .delete(deleteOptions);
    }

    @Override
    public DeleteResult removeByFilter(List<Filter> filters) {
        DeleteOptions deleteOptions = new DeleteOptions();
        deleteOptions.multi(true);
        return this.find(filters)
                .delete(deleteOptions);
    }

    @Override
    public UpdateResult removeLogicByIds(Collection<Serializable> ids) {
        String columnNameId = EntityPropertiesUtil.getColumnPropertyNameId(propertyFieldCache);
        Query<T> query = this.find(Arrays.asList(Filters.in(columnNameId, ids)));
        return this.update(query, this.setRemoveUpdateInfo(false));
    }

    @Override
    public UpdateResult removeLogicById(Serializable id) {
        String columnNameId = EntityPropertiesUtil.getColumnPropertyNameId(propertyFieldCache);
        Query<T> query = this.find(Arrays.asList(Filters.eq(columnNameId, id)));
        return this.update(query, this.setRemoveUpdateInfo(false));
    }

    @Override
    public UpdateResult removeLogicByFilter(List<Filter> filters) {
        Query<T> query = this.find(filters);
        return this.update(query, this.setRemoveUpdateInfo(false));
    }

    @Override
    public UpdateResult undoRemoveLogicByIds(Collection<Serializable> ids) {
        String columnNameId = EntityPropertiesUtil.getColumnPropertyNameId(propertyFieldCache);
        Query<T> query = this.find(Arrays.asList(Filters.in(columnNameId, ids)));
        return this.update(query, this.setRemoveUpdateInfo(true));
    }

    @Override
    public UpdateResult undoRemoveLogicById(Serializable id) {
        String columnNameId = EntityPropertiesUtil.getColumnPropertyNameId(propertyFieldCache);
        Query<T> query = this.find(Arrays.asList(Filters.eq(columnNameId, id)));
        return this.update(query, this.setRemoveUpdateInfo(true));
    }

    @Override
    public UpdateResult undoRemoveLogicByFilter(List<Filter> filters) {
        Query<T> query = this.find(filters);
        return this.update(query, this.setRemoveUpdateInfo(true));
    }

    private void setId(T entity) {
        if (!this.isAutoSetId()) {
            return;
        }
        String columnNameId = EntityPropertiesUtil.getColumnPropertyNameId(propertyFieldCache);
        Object idValue = EntityPropertiesUtil.getFieldValue(entity, columnNameId);
        if (ObjectUtil.isNotNull(idValue)) {
            return;
        }
        Set<String> columnNames = EntityPropertiesUtil.getColumnPropertyNames(propertyFieldCache, PropertyId.class);
        Class<?> columnTypeId = EntityPropertiesUtil.getColumnTypeId(propertyFieldCache);
        if (Number.class.isAssignableFrom(columnTypeId)) {
            EntityPropertiesUtil.setFieldValue(this, entity, columnNames, identifierGenerator.nextId());
        } else if (String.class.isAssignableFrom(columnTypeId)) {
            EntityPropertiesUtil.setFieldValue(this, entity, columnNames, identifierGenerator.nextUUID());
        }
    }

    private void setCreateInfo(T entity) {
        if (!this.isAutoSetCreateExtraInfo()) {
            return;
        }
        Set<String> createByPropertyNames = EntityPropertiesUtil.getColumnPropertyNames(propertyFieldCache, PropertyCreateBy.class);
        EntityPropertiesUtil.setFieldValue(this, entity, createByPropertyNames, this.getOptionUserId());

        Set<String> createTimePropertyNames = EntityPropertiesUtil.getColumnPropertyNames(propertyFieldCache, PropertyCreateTime.class);
        EntityPropertiesUtil.setFieldValue(this, entity, createTimePropertyNames, LocalDateTime.now());

        Set<String> creatorPropertyNames = EntityPropertiesUtil.getColumnPropertyNames(propertyFieldCache, PropertyCreator.class);
        EntityPropertiesUtil.setFieldValue(this, entity, creatorPropertyNames, this.getOperator());

        this.setUpdateInfo(entity);
    }

    private void setUpdateInfo(T entity) {
        if (!this.isAutoSetUpdateExtraInfo()) {
            return;
        }
        Set<String> updateByPropertyNames = EntityPropertiesUtil.getColumnPropertyNames(propertyFieldCache, PropertyUpdateBy.class);
        EntityPropertiesUtil.setFieldValue(this, entity, updateByPropertyNames, this.getOptionUserId());

        Set<String> UpdateTimePropertyNames = EntityPropertiesUtil.getColumnPropertyNames(propertyFieldCache, PropertyUpdateTime.class);
        EntityPropertiesUtil.setFieldValue(this, entity, UpdateTimePropertyNames, LocalDateTime.now());

        Set<String> UpdaterPropertyNames = EntityPropertiesUtil.getColumnPropertyNames(propertyFieldCache, PropertyUpdater.class);
        EntityPropertiesUtil.setFieldValue(this, entity, UpdaterPropertyNames, this.getOperator());
    }

    private List<UpdateOperator> setRemoveUpdateInfo(boolean undo) {
        Set<String> deletedPropertyNames = EntityPropertiesUtil.getColumnPropertyNames(propertyFieldCache, PropertyDeleted.class);
        if (CollectionUtil.isNotEmpty(deletedPropertyNames)) {
            List<UpdateOperator> updates = this.setUpdateTimeUpdateInfo(CollectionUtil.newArrayList());
            for (String deletedPropertyName : deletedPropertyNames) {
                updates.add(UpdateOperators.set(deletedPropertyName, !undo));
            }
            return updates;
        } else {
            throw new NotSupportedException("不支持逻辑删除操作，请确认Entity是否有错");
        }
    }

    private List<UpdateOperator> setUpdateTimeUpdateInfo(List<UpdateOperator> updates) {
        if (ObjectUtil.isNull(updates)) {
            updates = new ArrayList<>();
        }
        if (!this.isAutoSetUpdateExtraInfo()) {
            return updates;
        }
        Set<String> updateByPropertyNames = EntityPropertiesUtil.getColumnPropertyNames(propertyFieldCache, PropertyUpdateBy.class);
        Set<String> updateTimePropertyNames = EntityPropertiesUtil.getColumnPropertyNames(propertyFieldCache, PropertyUpdateTime.class);
        Set<String> updatorPropertyNames = EntityPropertiesUtil.getColumnPropertyNames(propertyFieldCache, PropertyUpdater.class);
        if (CollectionUtil.isEmpty(updateByPropertyNames)
                && CollectionUtil.isEmpty(updateTimePropertyNames)
                && CollectionUtil.isEmpty(updatorPropertyNames)) {
            return updates;
        }
        final Set<String> fieldCollect = updates.stream().map(UpdateOperator::field).collect(Collectors.toSet());
        EntityPropertiesUtil.setFieldValue(this, updates, fieldCollect, updateByPropertyNames, this.getOptionUserId());
        EntityPropertiesUtil.setFieldValue(this, updates, fieldCollect, updateTimePropertyNames, LocalDateTime.now());
        EntityPropertiesUtil.setFieldValue(this, updates, fieldCollect, updatorPropertyNames, this.getOperator());
        return updates;
    }

}
