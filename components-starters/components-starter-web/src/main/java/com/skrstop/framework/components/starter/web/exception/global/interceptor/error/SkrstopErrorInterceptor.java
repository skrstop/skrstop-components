package com.skrstop.framework.components.starter.web.exception.global.interceptor.error;

import com.skrstop.framework.components.core.common.response.Result;
import com.skrstop.framework.components.core.exception.core.SkrstopThrowable;
import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ErrorHandlerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:33:02
 */
@Slf4j
public class SkrstopErrorInterceptor implements ErrorHandlerInterceptor {
    @Override
    public boolean support(Error e) {
        return e instanceof SkrstopThrowable;
    }

    @Override
    public int order() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

    @Override
    public InterceptorResult execute(Error e) {
        SkrstopThrowable skrstopError = (SkrstopThrowable) e;
        return InterceptorResult.builder()
                .next(false)
                .result(Result.Builder.result(skrstopError.getIResult()))
                .build();
    }

}
