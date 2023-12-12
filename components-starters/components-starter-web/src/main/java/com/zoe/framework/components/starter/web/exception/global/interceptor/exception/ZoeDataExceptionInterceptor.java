package com.zoe.framework.components.starter.web.exception.global.interceptor.exception;

import com.zoe.framework.components.core.common.response.Result;
import com.zoe.framework.components.core.exception.core.ZoeDataThrowable;
import com.zoe.framework.components.core.exception.core.data.ThrowableData;
import com.zoe.framework.components.starter.web.entity.InterceptorResult;
import com.zoe.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:10:43
 */
@Slf4j
@NoArgsConstructor
public class ZoeDataExceptionInterceptor implements ExceptionHandlerInterceptor {
    @Override
    public boolean support(Exception e) {
        return e instanceof ZoeDataThrowable;
    }

    @Override
    public int order() {
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

    @Override
    public InterceptorResult execute(Exception e) {
        ZoeDataThrowable serviceByDataException = (ZoeDataThrowable) e;
        ThrowableData<?> throwableData = serviceByDataException.getThrowableData();
        return InterceptorResult.builder()
                .next(false)
                .result(Result.Builder.result(serviceByDataException.getIResult(), throwableData.getData()))
                .build();
    }
}
