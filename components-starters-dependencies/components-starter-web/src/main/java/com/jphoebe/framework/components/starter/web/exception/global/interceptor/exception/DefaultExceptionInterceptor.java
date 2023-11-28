package com.jphoebe.framework.components.starter.web.exception.global.interceptor.exception;

import com.jphoebe.framework.components.core.common.response.Result;
import com.jphoebe.framework.components.core.common.response.common.CommonResultCode;
import com.jphoebe.framework.components.core.common.response.core.IResult;
import com.jphoebe.framework.components.core.exception.util.ThrowableStackTraceUtil;
import com.jphoebe.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:11:34
 */
@Slf4j
@NoArgsConstructor
public class DefaultExceptionInterceptor implements ExceptionHandlerInterceptor {
    @Override
    public IResult execute(Exception e) {
        log.error(ThrowableStackTraceUtil.getStackTraceStr(e));
        return Result.Builder.result(CommonResultCode.FAIL);
    }
}
