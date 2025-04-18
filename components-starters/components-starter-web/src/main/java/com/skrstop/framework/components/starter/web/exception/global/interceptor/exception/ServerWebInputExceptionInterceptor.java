package com.skrstop.framework.components.starter.web.exception.global.interceptor.exception;

import com.skrstop.framework.components.core.common.response.DefaultResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.util.EnumCodeUtil;
import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import com.skrstop.framework.components.starter.web.exception.code.WebStarterExceptionCode;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import com.skrstop.framework.components.util.constant.HttpStatusConst;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
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
    public InterceptorResult execute(Exception e, HttpServletResponse httpServletResponse, ServerHttpResponse serverHttpResponse) {
        Throwable cause = e.getCause();
        if (ObjectUtil.isNull(cause)) {
            return null;
        }
        if (cause instanceof TypeMismatchException) {
            if (ObjectUtil.isNotNull(httpServletResponse)) {
                httpServletResponse.setStatus(HttpStatusConst.HTTP_BAD_REQUEST);
            }
            if (ObjectUtil.isNotNull(serverHttpResponse)) {
                serverHttpResponse.setRawStatusCode(HttpStatusConst.HTTP_BAD_REQUEST);
            }
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
