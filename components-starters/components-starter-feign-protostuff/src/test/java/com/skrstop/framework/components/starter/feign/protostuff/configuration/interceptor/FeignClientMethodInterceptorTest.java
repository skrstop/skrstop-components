package com.skrstop.framework.components.starter.feign.protostuff.configuration.interceptor;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.Collections;

public class FeignClientMethodInterceptorTest {

    @Controller
    static class ControllerService {
        public String controllerMethod() {
            return "controller";
        }
    }

    static class PlainService {
        public String plain() {
            return "plain";
        }
    }

    @After
    public void tearDown() {
        DynamicFeignClientMethodContextHolder.clear();
    }

    private ReflectiveMethodInvocation invocation(Class<?> clazz, String methodName) throws Exception {
        Method method = clazz.getMethod(methodName);
        Object target = clazz.getDeclaredConstructor().newInstance();
        return new ReflectiveMethodInvocation(target, target, method, new Object[0], null,
                Collections.emptyList()) {
            @Override
            public Object proceed() {
                // 记录拦截器 push 后、poll 前的状态
                return DynamicFeignClientMethodContextHolder.peek() ? "pushed" : "ok";
            }
        };
    }

    @Test
    public void controllerMethodSkipsPush() throws Throwable {
        FeignClientMethodInterceptor interceptor = new FeignClientMethodInterceptor();
        Assert.assertEquals("ok", interceptor.invoke(invocation(ControllerService.class, "controllerMethod")));
        Assert.assertFalse(DynamicFeignClientMethodContextHolder.peek());
    }

    @Test
    public void plainMethodPushesAndPolls() throws Throwable {
        FeignClientMethodInterceptor interceptor = new FeignClientMethodInterceptor();
        Assert.assertEquals("pushed", interceptor.invoke(invocation(PlainService.class, "plain")));
        // finally poll 后清空
        Assert.assertFalse(DynamicFeignClientMethodContextHolder.peek());
    }
}
