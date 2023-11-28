package com.jphoebe.framework.components.core.common.response.core;

/**
 * @author 蒋时华
 * @date 2020-04-30 16:25:41
 */
public interface IDataResult<T> extends IResult {

    /**
     * get response data
     *
     * @return T
     */
    T getData();

    /**
     * set response data
     *
     * @param data
     */
    void setData(T data);

}
