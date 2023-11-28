package cn.auntec.framework.components.starter.web.exception.core.interceptor;

import cn.auntec.framework.components.core.common.response.core.IResult;

/**
 * web 异常处理拦截器
 *
 * @author 蒋时华
 * @date 2020-05-08 12:56:46
 */
public interface ExceptionHandlerInterceptor {

    /**
     * exception 异常处理
     *
     * @param e
     * @return
     */
    IResult execute(Exception e);

}
