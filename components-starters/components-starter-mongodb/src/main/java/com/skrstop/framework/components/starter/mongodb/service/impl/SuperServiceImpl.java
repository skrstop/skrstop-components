package com.skrstop.framework.components.starter.mongodb.service.impl;

import cn.hutool.core.util.ReflectUtil;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.skrstop.framework.components.core.common.response.page.ListSimplePageData;
import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.skrstop.framework.components.starter.id.service.IdService;
import com.skrstop.framework.components.starter.mongodb.configuration.GlobalMongodbProperties;
import com.skrstop.framework.components.starter.mongodb.constant.MongodbConst;
import com.skrstop.framework.components.starter.mongodb.entity.*;
import com.skrstop.framework.components.starter.mongodb.entity.expand.CreatorExpand;
import com.skrstop.framework.components.starter.mongodb.entity.expand.UpdaterExpand;
import com.skrstop.framework.components.starter.mongodb.entity.version.*;
import com.skrstop.framework.components.starter.mongodb.service.SuperService;
import com.skrstop.framework.components.starter.mongodb.wrapper.PageQuery;
import com.skrstop.framework.components.util.value.data.ArrayUtil;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import com.skrstop.framework.components.util.value.format.TextFormatUtil;
import dev.morphia.*;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import dev.morphia.query.updates.UpdateOperator;
import dev.morphia.query.updates.UpdateOperators;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

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
public abstract class SuperServiceImpl<T extends AbstractBaseEntity, KEY extends Serializable> implements SuperService<T, KEY> {

    public static String UPDATE_BY = "updateBy";
    public static String UPDATE_TIME = "updateTime";
    public static String CREATE_BY = "createBy";
    public static String CREATE_TIME = "createTime";
    public static final String UPDATER = "updater";
    public static final String CREATOR = "creator";
    public static final String VERSION = "version";
    public static final String DELETED = "deleted";
    public static final int ASC = 1;
    public static final int DESC = -1;

    @Autowired
    protected Datastore datastore;
    @Autowired(required = false)
    protected IdService idService;
    @Autowired
    private GlobalMongodbProperties globalMongodbProperties;

    protected Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    //    protected Class<T> entityIdClass = (Class<T>) ((ParameterizedType) entityClass.getGenericSuperclass()).getActualTypeArguments()[0];
    protected Class<T> entityIdClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    protected String idColumnName = ReflectUtil.newInstance(entityClass).getIdName();

    @PostConstruct
    private void initPropertiesFormat() {
        switch (globalMongodbProperties.getPropertyNaming()) {
            case MongodbConst.NAME_RULE_IDENTITY:
            case MongodbConst.NAME_RULE_CAMEL_CASE:
                idColumnName = ReflectUtil.newInstance(entityClass).getIdName();
                break;
            case MongodbConst.NAME_RULE_SNAKE_CASE:
                UPDATE_BY = StrUtil.toUnderlineCase(UPDATE_BY);
                UPDATE_TIME = StrUtil.toUnderlineCase(UPDATE_TIME);
                CREATE_BY = StrUtil.toUnderlineCase(CREATE_BY);
                CREATE_TIME = StrUtil.toUnderlineCase(CREATE_TIME);
                idColumnName = StrUtil.toUnderlineCase(ReflectUtil.newInstance(entityClass).getIdName());
                break;
            case MongodbConst.NAME_RULE_LOWER_CASE:
                UPDATE_BY = UPDATE_BY.toLowerCase();
                UPDATE_TIME = UPDATE_TIME.toLowerCase();
                CREATE_BY = CREATE_BY.toLowerCase();
                CREATE_TIME = CREATE_TIME.toLowerCase();
                idColumnName = ReflectUtil.newInstance(entityClass).getIdName().toLowerCase();
                break;
            case MongodbConst.NAME_RULE_KEBAB_CASE:
                UPDATE_BY = StrUtil.toSymbolCase(UPDATE_BY, '-');
                UPDATE_TIME = StrUtil.toSymbolCase(UPDATE_TIME, '-');
                CREATE_BY = StrUtil.toSymbolCase(CREATE_BY, '-');
                CREATE_TIME = StrUtil.toSymbolCase(CREATE_TIME, '-');
                idColumnName = StrUtil.toSymbolCase(ReflectUtil.newInstance(entityClass).getIdName(), '-');
                break;
            default:
                break;
        }
    }

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

    private void setId(T entity) {
        if (!this.isAutoSetId()) {
            return;
        }
        if (ObjectUtil.isNotNull(idService)
                && Long.class.isAssignableFrom(entityIdClass)
                && ObjectUtil.isNull(entity.getId())) {
            entity.setId(idService.getId());
        } else if (String.class.isAssignableFrom(entityIdClass)
                && StrUtil.isBlank((String) entity.getId())) {
            entity.setId(idService.getObjectId());
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
    public T findById(KEY id) {
        return this.find(Arrays.asList(Filters.eq(idColumnName, id)))
                .iterator().tryNext();
    }

    @Override
    public List<T> findByIds(List<KEY> ids) {
        return this.find(Arrays.asList(Filters.in(idColumnName, ids)))
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
        if (AbstractVersionBaseEntity.class.isAssignableFrom(this.entityClass)) {
            // 有版本校验
            updatesArray = new UpdateOperator[updates.size() + 1];
            updatesArray[0] = UpdateOperators.inc(VERSION);
            for (int i = 0; i < updates.size(); i++) {
                updatesArray[i + 1] = updates.get(i);
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
        if (AbstractVersionBaseEntity.class.isAssignableFrom(this.entityClass)) {
            // 有版本校验
            filterArray = new Filter[filters.size() + 1];
            filterArray[0] = Filters.eq(VERSION, currentVersion);
            for (int i = 0; i < filters.size(); i++) {
                filterArray[i + 1] = filters.get(i);
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
    public DeleteResult removeById(KEY id) {
        return this.find(Arrays.asList(Filters.eq(idColumnName, id)))
                .delete();
    }

    @Override
    public DeleteResult removeByIds(Collection<KEY> ids) {
        DeleteOptions deleteOptions = new DeleteOptions();
        deleteOptions.multi(true);
        return this.find(Arrays.asList(Filters.in(idColumnName, ids)))
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
    public UpdateResult removeLogicByIds(Collection<KEY> ids) {
        Query<T> query = this.find(Arrays.asList(Filters.in(idColumnName, ids)));
        return this.update(query, this.setRemoveUpdateInfo(false));
    }

    @Override
    public UpdateResult removeLogicById(KEY id) {
        Query<T> query = this.find(Arrays.asList(Filters.eq(idColumnName, id)));
        return this.update(query, this.setRemoveUpdateInfo(false));
    }

    @Override
    public UpdateResult removeLogicByFilter(List<Filter> filters) {
        Query<T> query = this.find(filters);
        return this.update(query, this.setRemoveUpdateInfo(false));
    }

    @Override
    public UpdateResult undoRemoveLogicByIds(Collection<KEY> ids) {
        Query<T> query = this.find(Arrays.asList(Filters.in(idColumnName, ids)));
        return this.update(query, this.setRemoveUpdateInfo(true));
    }

    @Override
    public UpdateResult undoRemoveLogicById(KEY id) {
        Query<T> query = this.find(Arrays.asList(Filters.eq(idColumnName, id)));
        return this.update(query, this.setRemoveUpdateInfo(true));
    }

    @Override
    public UpdateResult undoRemoveLogicByFilter(List<Filter> filters) {
        Query<T> query = this.find(filters);
        return this.update(query, this.setRemoveUpdateInfo(true));
    }

    private List<UpdateOperator> setRemoveUpdateInfo(boolean undo) {
        if (AbstractDeletedBaseEntity.class.isAssignableFrom(entityClass)
                || AbstractTimeDeletedBaseEntity.class.isAssignableFrom(entityClass)
                || AbstractOperatorTimeDeletedBaseEntity.class.isAssignableFrom(entityClass)
                || AbstractTimeDeletedVersionBaseEntity.class.isAssignableFrom(entityClass)
                || AbstractCreatorTimeDeletedVersionBaseEntity.class.isAssignableFrom(entityClass)
                || AbstractCreateTimeDeletedVersionBaseEntity.class.isAssignableFrom(entityClass)
                || AbstractOperatorTimeDeletedVersionBaseEntity.class.isAssignableFrom(entityClass)
                || AbstractDeletedVersionBaseEntity.class.isAssignableFrom(entityClass)) {
            List<UpdateOperator> updates = this.setUpdateTimeUpdateInfo(CollectionUtil.newArrayList());
            updates.add(UpdateOperators.set(DELETED, !undo));
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
        if (AbstractTimeBaseEntity.class.isAssignableFrom(entityClass)
                || AbstractTimeVersionBaseEntity.class.isAssignableFrom(entityClass)) {
            final Set<String> collect = updates.stream().map(UpdateOperator::field).collect(Collectors.toSet());
            if (this.onlySetUpdateInfoWhenNull()
                    && !collect.contains(UPDATE_TIME)) {
                updates.add(UpdateOperators.set(UPDATE_TIME, LocalDateTime.now()));
            } else if (!this.onlySetUpdateInfoWhenNull()) {
                updates.add(UpdateOperators.set(UPDATE_TIME, LocalDateTime.now()));
            }
            if (this.onlySetUpdateInfoWhenNull()
                    && !collect.contains(UPDATE_BY)) {
                updates.add(UpdateOperators.set(UPDATE_BY, this.getOptionUserId()));
            } else if (!this.onlySetUpdateInfoWhenNull()) {
                updates.add(UpdateOperators.set(UPDATE_BY, this.getOptionUserId()));
            }

            if (AbstractOperatorTimeVersionBaseEntity.class.isAssignableFrom(entityClass)) {
                if (this.onlySetUpdateInfoWhenNull()
                        && !collect.contains(UPDATER)) {
                    updates.add(UpdateOperators.set(UPDATER, this.getOperator()));
                } else if (!this.onlySetUpdateInfoWhenNull()) {
                    updates.add(UpdateOperators.set(UPDATER, this.getOperator()));
                }
            }
        }
        return updates;
    }

}
