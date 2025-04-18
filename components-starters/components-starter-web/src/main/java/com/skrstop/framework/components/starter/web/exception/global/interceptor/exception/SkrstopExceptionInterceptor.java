package com.skrstop.framework.components.starter.web.exception.global.interceptor.exception;

import com.skrstop.framework.components.core.common.response.Result;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.exception.SkrstopBusinessException;
import com.skrstop.framework.components.core.exception.core.SkrstopThrowable;
import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:11:34
 */
@NoArgsConstructor
@Slf4j
public class SkrstopExceptionInterceptor implements ExceptionHandlerInterceptor {
    @Override
    public boolean support(Exception e) {
        return e instanceof SkrstopThrowable;
    }

    @Override
    public int order() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

    @Override
    public InterceptorResult execute(Exception e, HttpServletResponse httpServletResponse, ServerHttpResponse serverHttpResponse) {
        SkrstopThrowable skrstopRuntimeException = (SkrstopThrowable) e;
        if (e instanceof SkrstopBusinessException) {
            return InterceptorResult.builder()
                    .next(false)
                    .result(Result.Builder.result(skrstopRuntimeException.getIResult()))
                    .build();
        }
        return InterceptorResult.builder()
                .next(false)
                .result(Result.Builder.result(CommonResultCode.FAIL))
                .build();
    }
}
