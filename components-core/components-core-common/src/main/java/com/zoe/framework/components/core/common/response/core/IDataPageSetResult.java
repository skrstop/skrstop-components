package com.zoe.framework.components.core.common.response.core;

import java.util.Set;

/**
 * response code interface
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
public interface IDataPageSetResult<T> extends IDataPageResult<Set<T>> {

    /**
     * get response data
     *
     * @return T
     */
    @Override
    Set<T> getData();

    /**
     * set response data
     *
     * @param data
     */
    @Override
    void setData(Set<T> data);

}
