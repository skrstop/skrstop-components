package com.zoe.framework.components.starter.web.response.interceptor;

import com.zoe.framework.components.core.common.response.core.IPageResult;
import com.zoe.framework.components.core.common.util.DynamicResult;
import com.zoe.framework.components.starter.web.entity.InterceptorResult;
import com.zoe.framework.components.starter.web.response.core.ResponseHandlerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:50:54
 */
@Slf4j
public class DefaultPageResultResponseInterceptor implements ResponseHandlerInterceptor {
    @Override
    public boolean support(Object returnValue) {
        return returnValue instanceof IPageResult;
    }

    @Override
    public int order() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

    @Override
    public InterceptorResult execute(Object returnValue) {
        return InterceptorResult.builder()
                .next(false)
                .result(DynamicResult.buildPage((IPageResult) returnValue))
                .build();
    }
}
