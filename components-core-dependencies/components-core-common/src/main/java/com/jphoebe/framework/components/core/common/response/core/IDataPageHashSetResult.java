package com.jphoebe.framework.components.core.common.response.core;

import java.util.HashSet;

/**
 * response code interface
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
public interface IDataPageHashSetResult<T> extends IDataPageResult<HashSet<T>> {

    /**
     * get response data
     *
     * @return T
     */
    @Override
    HashSet<T> getData();

    /**
     * set response data
     *
     * @param data
     */
    @Override
    void setData(HashSet<T> data);

}
