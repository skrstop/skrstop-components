package com.jphoebe.framework.components.starter.web.exception.core.interceptor;

import com.jphoebe.framework.components.core.common.response.core.IResult;

/**
 * web 异常处理拦截器
 *
 * @author 蒋时华
 * @date 2020-05-08 12:56:46
 */
public interface ErrorHandlerInterceptor {

    /**
     * error 异常处理
     *
     * @param e
     * @return
     */
    IResult execute(Error e);

}
