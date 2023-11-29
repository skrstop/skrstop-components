package com.zoe.framework.components.core.common.response.core;

import java.util.HashSet;

/**
 * @author 蒋时华
 * @date 2020-04-30 16:25:41
 */
public interface IHashSetResult<T> extends IDataResult<HashSet<T>> {

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
