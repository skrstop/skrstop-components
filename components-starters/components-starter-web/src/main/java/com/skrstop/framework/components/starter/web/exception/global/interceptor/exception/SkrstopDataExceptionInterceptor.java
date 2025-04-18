package com.skrstop.framework.components.starter.web.exception.global.interceptor.exception;

import com.skrstop.framework.components.core.common.response.Result;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.exception.core.BusinessThrowable;
import com.skrstop.framework.components.core.exception.core.SkrstopDataThrowable;
import com.skrstop.framework.components.core.exception.core.data.ThrowableData;
import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:10:43
 */
@Slf4j
@NoArgsConstructor
public class SkrstopDataExceptionInterceptor implements ExceptionHandlerInterceptor {
    @Override
    public boolean support(Exception e) {
        return e instanceof SkrstopDataThrowable;
    }

    @Override
    public int order() {
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

    @Override
    public InterceptorResult execute(Exception e, HttpServletResponse httpServletResponse, ServerHttpResponse serverHttpResponse) {
        SkrstopDataThrowable serviceByDataException = (SkrstopDataThrowable) e;
        ThrowableData<?> throwableData = serviceByDataException.getThrowableData();
        if (e instanceof BusinessThrowable) {
            return InterceptorResult.builder()
                    .next(false)
                    .result(Result.Builder.result(serviceByDataException.getIResult(), throwableData.getData()))
                    .build();
        }
        return InterceptorResult.builder()
                .next(false)
                .result(Result.Builder.result(CommonResultCode.FAIL, throwableData.getData()))
                .build();
    }
}
