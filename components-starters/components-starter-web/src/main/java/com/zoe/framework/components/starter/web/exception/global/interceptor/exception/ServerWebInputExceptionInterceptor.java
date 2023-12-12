package com.zoe.framework.components.starter.web.exception.global.interceptor.exception;

import com.zoe.framework.components.core.common.response.DefaultResult;
import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.core.common.util.EnumCodeUtil;
import com.zoe.framework.components.starter.web.entity.InterceptorResult;
import com.zoe.framework.components.starter.web.exception.code.WebStarterExceptionCode;
import com.zoe.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebInputException;

/**
 * @author 蒋时华
 * @date 2021-01-11 21:18:29
 */
@Slf4j
public class ServerWebInputExceptionInterceptor implements ExceptionHandlerInterceptor {
    @Override
    public boolean support(Exception e) {
        return e instanceof ServerWebInputException;
    }

    @Override
    public int order() {
        return Ordered.LOWEST_PRECEDENCE - 7;
    }

    @Override
    public InterceptorResult execute(Exception e) {
        Throwable cause = e.getCause();
        if (ObjectUtil.isNull(cause)) {
            return null;
        }
        if (cause instanceof TypeMismatchException) {
            IResult iResult = EnumCodeUtil.transferEnumCode(WebStarterExceptionCode.MATCH_PARAMETER);
            String message = iResult.getMessage();
            message = new StringBuffer(message)
                    .append(": ")
                    .append(((TypeMismatchException) cause).getValue())
                    .toString();
            iResult.setMessage(message);
            return InterceptorResult.builder()
                    .next(false)
                    .result(DefaultResult.Builder.result(iResult))
                    .build();
        }
        return InterceptorResult.builder()
                .next(true)
                .result(null)
                .build();
    }
}
