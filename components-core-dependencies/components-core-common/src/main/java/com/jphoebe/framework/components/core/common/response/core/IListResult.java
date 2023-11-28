package com.jphoebe.framework.components.core.common.response.core;

import java.util.List;

/**
 * @author 蒋时华
 * @date 2020-04-30 16:25:41
 */
public interface IListResult<T> extends IDataResult<List<T>> {

    /**
     * get response data
     *
     * @return T
     */
    @Override
    List<T> getData();

    /**
     * set response data
     *
     * @param data
     */
    @Override
    void setData(List<T> data);

}
