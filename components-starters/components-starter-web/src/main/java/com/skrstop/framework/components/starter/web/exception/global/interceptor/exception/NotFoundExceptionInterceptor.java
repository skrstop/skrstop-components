package com.skrstop.framework.components.starter.web.exception.global.interceptor.exception;

import com.skrstop.framework.components.core.common.response.DefaultResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

/**
 * 参数异常handle
 *
 * @author 蒋时华
 * @date 2020-05-08 13:03:01
 */
@Slf4j
@NoArgsConstructor
public class NotFoundExceptionInterceptor implements ExceptionHandlerInterceptor {

    @Override
    public boolean support(Exception e) {
        return e instanceof ResponseStatusException;
    }

    @Override
    public int order() {
        return Ordered.LOWEST_PRECEDENCE - 7;
    }

    @Override
    public InterceptorResult execute(Exception e) {
        HttpStatusCode status = ((ResponseStatusException) e).getStatusCode();
        if (HttpStatus.NOT_FOUND.value() == status.value()) {
            // 404
            return InterceptorResult.builder()
                    .next(false)
                    .result(DefaultResult.Builder.result(CommonResultCode.NOT_FOUND))
                    .build();
        }
        return InterceptorResult.builder()
                .next(true)
                .result(null)
                .build();
    }

}
