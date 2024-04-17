package com.skrstop.framework.components.starter.feign.protostuff.configuration.interceptor;

import com.skrstop.framework.components.starter.common.util.AnnoFindUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Controller;

public class FeignClientMethodInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        boolean isController = AnnoFindUtil.has(invocation.getMethod(), Controller.class);
        if (isController) {
            return invocation.proceed();
        }
        DynamicFeignClientMethodContextHolder.push(true);
        try {
            return invocation.proceed();
        } finally {
            DynamicFeignClientMethodContextHolder.poll();
        }
    }

}