package com.skrstop.framework.components.starter.web.exception.global.interceptor.exception;

import com.skrstop.framework.components.core.common.response.Result;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import com.skrstop.framework.components.util.constant.HttpStatusConst;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:11:34
 */
@Slf4j
@NoArgsConstructor
public class DefaultExceptionInterceptor implements ExceptionHandlerInterceptor {
    @Override
    public boolean support(Exception e) {
        return true;
    }

    @Override
    public int order() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public InterceptorResult execute(Exception e) {
        return InterceptorResult.builder()
                .next(false)
                .result(Result.Builder.result(CommonResultCode.FAIL))
                .responseStatus(HttpStatusConst.HTTP_INTERNAL_ERROR)
                .build();
    }
}
