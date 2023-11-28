package com.jphoebe.framework.components.starter.web.response.interceptor;

import com.jphoebe.framework.components.core.common.response.Result;
import com.jphoebe.framework.components.core.common.response.core.IResult;
import com.jphoebe.framework.components.starter.web.response.core.ResponseHandlerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:50:54
 */
@Configuration
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class DefaultResponseInterceptor implements ResponseHandlerInterceptor {
    @Override
    public IResult execute(Object returnValue, boolean transResultResponse) {
        return Result.Builder.success(returnValue);
    }
}
