package com.skrstop.framework.components.core.common.response.core;

import java.util.LinkedHashSet;

/**
 * @author 蒋时华
 * @date 2020-04-30 16:25:41
 */
public interface ILinkedSetResult<T> extends IDataResult<LinkedHashSet<T>> {

    /**
     * get response data
     *
     * @return T
     */
    @Override
    LinkedHashSet<T> getData();

    /**
     * set response data
     *
     * @param data
     */
    @Override
    void setData(LinkedHashSet<T> data);

}
