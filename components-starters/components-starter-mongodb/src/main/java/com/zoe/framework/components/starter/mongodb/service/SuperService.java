package com.zoe.framework.components.starter.mongodb.service;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.zoe.framework.components.core.common.response.page.CommonPageData;
import com.zoe.framework.components.starter.mongodb.wrapper.PageQuery;
import com.zoe.framework.components.util.constant.StringPoolConst;
import dev.morphia.InsertManyOptions;
import dev.morphia.InsertOneOptions;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.updates.UpdateOperator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 基础业务接口
 *
 * @param <T>
 * @author 蒋时华
 */
public interface SuperService<T, KEY extends Serializable> {

    /**
     * 获取操作人id
     *
     * @return
     */
    default long getOptionUserId() {
        return 0L;
    }

    /**
     * 获取操作人名字
     *
     * @return
     */
    default String getOperator() {
        return StringPoolConst.EMPTY;
    }

    /**
     * 是否自动设置 创建时间、创建人
     *
     * @return
     */
    default boolean isAutoSetCreateExtraInfo() {
        return true;
    }

    /**
     * 是否自动设置 创建时间、创建人
     *
     * @return
     */
    default boolean isAutoSetUpdateExtraInfo() {
        return true;
    }

    /**
     * true: 当 创建信息 不为空是自动通过方法填充
     * false: 不管有没有设值，都会通过方法填充
     *
     * @return
     */
    default boolean onlySetCreateInfoWhenNull() {
        return true;
    }

    /**
     * true: 当 修改信息 不为空是自动通过方法填充
     * false: 不管有没有设值，都会通过方法填充
     *
     * @return
     */
    default boolean onlySetUpdateInfoWhenNull() {
        return true;
    }

    /**
     * 当id字段为空的时候，自动设置id
     *
     * @return
     */
    default boolean isAutoSetId() {
        return true;
    }

    /**
     * 查询
     *
     * @param filters
     * @return
     */
    Query<T> find(List<Filter> filters);

    /**
     * 查询单条记录
     *
     * @param filters
     * @param findOptions
     * @return
     */
    T find(List<Filter> filters, FindOptions findOptions);

    /**
     * 查询多条记录
     *
     * @param filters
     * @param findOptions
     * @return
     */
    List<T> list(List<Filter> filters, FindOptions findOptions);

    /**
     * 分页查询
     *
     * @param pageQuery
     * @param filters
     * @return
     */
    CommonPageData<T> findPage(PageQuery pageQuery, List<Filter> filters);

    /**
     * 分页查询
     *
     * @param pageQuery
     * @param filters
     * @return
     */
    CommonPageData<T> findPage(PageQuery pageQuery, List<Filter> filters, FindOptions findOptions);

    /**
     * id查询
     *
     * @param id
     * @return
     */
    T findById(KEY id);

    /**
     * ids查询
     *
     * @param ids
     * @return
     */
    List<T> findByIds(List<KEY> ids);

    /**
     * 保存
     *
     * @param entity
     * @return
     */
    T save(@NotNull T entity);

    /**
     * 保存
     *
     * @param entity
     * @param insertOneOptions
     * @return
     */
    T save(@NotNull T entity, InsertOneOptions insertOneOptions);

    /**
     * 批量保存
     *
     * @param entityList
     * @return
     */
    List<T> saveBatch(@NotEmpty List<T> entityList);

    /**
     * 批量保存
     *
     * @param entityList
     * @param insertManyOptions
     * @return
     */
    List<T> saveBatch(@NotEmpty List<T> entityList, InsertManyOptions insertManyOptions);

    /**
     * 更新
     *
     * @param query
     * @param updates
     * @return
     */
    UpdateResult update(Query<T> query, List<UpdateOperator> updates);

    /**
     * 版本比较更新
     *
     * @param filters
     * @param updates
     * @return
     */
    UpdateResult updateWithVersion(Long currentVersion, List<Filter> filters, List<UpdateOperator> updates);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    DeleteResult removeById(KEY id);

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    DeleteResult removeByIds(Collection<KEY> ids);

    /**
     * 删除
     *
     * @param filters
     * @return
     */
    DeleteResult removeByFilter(List<Filter> filters);

    /**
     * 逻辑删除
     *
     * @param ids
     * @return
     */
    UpdateResult removeLogicByIds(Collection<KEY> ids);

    /**
     * 逻辑删除
     *
     * @param id
     * @return
     */
    UpdateResult removeLogicById(KEY id);

    /**
     * 逻辑删除
     *
     * @param filters
     * @return
     */
    UpdateResult removeLogicByFilter(List<Filter> filters);

    /**
     * 删除回滚
     *
     * @param ids
     * @return
     */
    UpdateResult undoRemoveLogicByIds(Collection<KEY> ids);

    /**
     * 删除回滚
     *
     * @param id
     * @return
     */
    UpdateResult undoRemoveLogicById(KEY id);

    /**
     * 删除回滚
     *
     * @param filters
     * @return
     */
    UpdateResult undoRemoveLogicByFilter(List<Filter> filters);

}
