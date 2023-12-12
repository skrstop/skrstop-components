package com.zoe.framework.components.starter.web.response.core;

import com.zoe.framework.components.starter.web.entity.InterceptorResult;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:48:03
 */
public interface ResponseHandlerInterceptor {

    /**
     * 是否可执行该拦截器
     *
     * @param returnValue
     * @return
     */
    boolean support(Object returnValue);

    /**
     * 指定顺序
     *
     * @return
     */
    int order();

    /**
     * 执行
     *
     * @param returnValue
     * @return
     */
    InterceptorResult execute(Object returnValue);

}
