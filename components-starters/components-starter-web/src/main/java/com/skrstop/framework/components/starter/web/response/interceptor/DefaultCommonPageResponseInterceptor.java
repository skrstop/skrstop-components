package com.skrstop.framework.components.starter.web.response.interceptor;

import com.skrstop.framework.components.core.common.response.page.PageData;
import com.skrstop.framework.components.core.common.util.DynamicResult;
import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import com.skrstop.framework.components.starter.web.response.core.ResponseHandlerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:50:54
 */
@Slf4j
public class DefaultCommonPageResponseInterceptor implements ResponseHandlerInterceptor {
    @Override
    public boolean support(Object returnValue) {
        return returnValue instanceof PageData;
    }

    @Override
    public int order() {
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

    @Override
    public InterceptorResult execute(Object returnValue) {
        return InterceptorResult.builder()
                .next(false)
                .result(DynamicResult.buildPage((PageData<?>) returnValue))
                .build();
    }
}
