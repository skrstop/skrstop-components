package com.zoe.framework.components.starter.redis.filter;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Set;

/**
 * @author 蒋时华
 * @date 2020-10-06 18:16:45
 */
public interface ValueFilter {

    /**
     * 过滤器处理
     *
     * @param obj
     * @param cls
     * @param <T>
     * @return
     */
    <T> T filter(Object obj, final Class<T> cls);

    /**
     * 过滤器处理
     *
     * @param obj
     * @param cls
     * @param <T>
     * @return
     */
    <T> Set<T> filterSet(Set obj, final Class<T> cls);

    <T> Set<ZSetOperations.TypedTuple<T>> filterSetScore(Set<ZSetOperations.TypedTuple<T>> obj, final Class<T> cls);

    /**
     * 过滤器处理
     *
     * @param obj
     * @param cls
     * @param <T>
     * @return
     */
    <T> List<T> filterList(List obj, final Class<T> cls);

}
