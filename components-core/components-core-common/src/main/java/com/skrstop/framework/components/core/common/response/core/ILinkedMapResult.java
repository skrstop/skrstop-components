package com.skrstop.framework.components.core.common.response.core;

import java.util.LinkedHashMap;

/**
 * @author 蒋时华
 * @date 2020-04-30 16:25:41
 */
public interface ILinkedMapResult<T, R> extends IDataResult<LinkedHashMap<T, R>> {

    /**
     * get response data
     *
     * @return T
     */
    @Override
    LinkedHashMap<T, R> getData();

    /**
     * set response data
     *
     * @param data
     */
    @Override
    void setData(LinkedHashMap<T, R> data);

}
