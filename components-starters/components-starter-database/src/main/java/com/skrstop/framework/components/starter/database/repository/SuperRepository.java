package com.skrstop.framework.components.starter.database.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.github.yulichang.base.MPJBaseService;
import com.skrstop.framework.components.util.constant.StringPoolConst;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * 基础业务接口
 *
 * @param <T>
 * @author 蒋时华
 */
public interface SuperRepository<T> extends MPJBaseService<T> {

    /**
     * 获取操作人id
     *
     * @return
     */
    default Object getOptionUserId() {
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
     * 逻辑删除
     *
     * @param ids id集合(逗号分隔)
     */
    boolean removeLogicByIds(Collection<?> ids);

    /**
     * 逻辑删除
     *
     * @param id
     */
    boolean removeLogicById(Serializable id);

    /**
     * 逻辑删除
     *
     * @param columnMap
     * @return
     */
    boolean removeLogicByMap(Map<String, Object> columnMap);

    /**
     * 逻辑删除
     *
     * @param updateWrapper
     * @return
     */
    boolean removeLogic(Wrapper<T> updateWrapper);

    /**
     * 撤销逻辑删除
     *
     * @param ids id集合(逗号分隔)
     */
    boolean undoRemoveLogicByIds(Collection<?> ids);

    /**
     * 撤销逻辑删除
     *
     * @param id
     */
    boolean undoRemoveLogicById(Serializable id);

    /**
     * 撤销逻辑删除
     *
     * @param columnMap
     */
    boolean undoRemoveLogicByMap(Map<String, Object> columnMap);

    /**
     * 撤销逻辑删除
     *
     * @param updateWrapper
     * @return
     */
    boolean undoRemoveLogic(Wrapper<T> updateWrapper);

}
