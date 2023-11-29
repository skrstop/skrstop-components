package com.zoe.framework.components.starter.web.exception.global.interceptor.exception;

import com.zoe.framework.components.core.common.response.Result;
import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.core.exception.ZoeRuntimeException;
import com.zoe.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:11:34
 */
@NoArgsConstructor
@Slf4j
public class ZoeRuntimeExceptionInterceptor implements ExceptionHandlerInterceptor {
    @Override
    public IResult execute(Exception e) {
        if (e instanceof ZoeRuntimeException) {
            ZoeRuntimeException zoeRuntimeException = (ZoeRuntimeException) e;
            return Result.Builder.result(zoeRuntimeException.getIResult());
        }
        return null;
    }
}
