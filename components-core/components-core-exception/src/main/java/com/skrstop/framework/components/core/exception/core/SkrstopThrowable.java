package com.skrstop.framework.components.core.exception.core;

import com.skrstop.framework.components.core.common.response.core.IResult;

import java.io.Serializable;

/**
 * throwable interface
 *
 * @author 蒋时华
 * @date 2019/6/3
 */
public interface SkrstopThrowable extends Serializable {

    /**
     * 获取异常信息
     *
     * @return String
     */
    default String getMessage() {
        return this.getIResult().getMessage();
    }

    /**
     * 获取异常码
     *
     * @return Integer
     */
    default Object getCode() {
        return this.getIResult().getCode();
    }

    /**
     * 获取异常栈信息
     *
     * @return String
     */
    default String getException() {
        return null;
    }

    /**
     * get responseCode
     *
     * @return {@link IResult}
     */
    IResult getIResult();

}
