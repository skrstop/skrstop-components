package cn.auntec.framework.components.starter.web.response.core;

import cn.auntec.framework.components.core.common.response.core.IResult;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:48:03
 */
public interface ResponseHandlerInterceptor {

    IResult execute(Object returnValue, boolean transResultResponse);

}
