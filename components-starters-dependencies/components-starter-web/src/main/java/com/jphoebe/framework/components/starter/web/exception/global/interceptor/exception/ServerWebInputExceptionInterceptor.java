package com.jphoebe.framework.components.starter.web.exception.global.interceptor.exception;

import com.jphoebe.framework.components.core.common.response.DefaultResult;
import com.jphoebe.framework.components.core.common.response.core.IResult;
import com.jphoebe.framework.components.core.common.util.EnumCodeUtil;
import com.jphoebe.framework.components.core.exception.util.ThrowableStackTraceUtil;
import com.jphoebe.framework.components.starter.web.exception.code.WebStarterExceptionCode;
import com.jphoebe.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import com.jphoebe.framework.components.util.value.data.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.server.ServerWebInputException;

/**
 * @author 蒋时华
 * @date 2021-01-11 21:18:29
 */
@Slf4j
public class ServerWebInputExceptionInterceptor implements ExceptionHandlerInterceptor {
    @Override
    public IResult execute(Exception e) {
        if (e instanceof ServerWebInputException) {
            log.error(ThrowableStackTraceUtil.getStackTraceStr(e));
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
                return DefaultResult.Builder.result(iResult);
            }
        }
        return null;
    }
}
