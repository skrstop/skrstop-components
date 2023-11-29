package com.zoe.framework.components.starter.web.exception.global.interceptor.exception;

import com.zoe.framework.components.core.common.response.Result;
import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.core.exception.core.ServiceDataZoeThrowable;
import com.zoe.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:10:43
 */
@Slf4j
@NoArgsConstructor
public class ServiceDataExceptionInterceptor implements ExceptionHandlerInterceptor {
    @Override
    public IResult execute(Exception e) {
        if (e instanceof ServiceDataZoeThrowable) {
            ServiceDataZoeThrowable serviceByDataException = (ServiceDataZoeThrowable) e;
            Object data = serviceByDataException.getData();
            return Result.Builder.result(serviceByDataException.getIResult(), data);
        }
        return null;
    }
}
