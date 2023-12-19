package com.skrstop.framework.components.starter.web.exception.core.interceptor;

import com.skrstop.framework.components.starter.web.entity.InterceptorResult;

/**
 * web 异常处理拦截器
 *
 * @author 蒋时华
 * @date 2020-05-08 12:56:46
 */
public interface ErrorHandlerInterceptor {

    /**
     * 是否可执行该拦截器
     *
     * @param e
     * @return
     */
    boolean support(Error e);

    /**
     * 指定顺序
     *
     * @return
     */
    int order();

    /**
     * 执行
     *
     * @param e
     * @return
     */
    InterceptorResult execute(Error e);

}
