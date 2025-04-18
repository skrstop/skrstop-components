package com.skrstop.framework.components.starter.web.exception.core.interceptor;

import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import org.springframework.http.server.reactive.ServerHttpResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * web 异常处理拦截器
 *
 * @author 蒋时华
 * @date 2020-05-08 12:56:46
 */
public interface ExceptionHandlerInterceptor {

    /**
     * 是否可执行该拦截器
     *
     * @param e
     * @return
     */
    boolean support(Exception e);

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
     * @param response
     * @param serverHttpResponse
     * @return
     */
    InterceptorResult execute(Exception e, HttpServletResponse response, ServerHttpResponse serverHttpResponse);

}
