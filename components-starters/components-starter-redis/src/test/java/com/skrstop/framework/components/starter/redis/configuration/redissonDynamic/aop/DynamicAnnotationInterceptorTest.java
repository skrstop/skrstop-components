package com.skrstop.framework.components.starter.redis.configuration.redissonDynamic.aop;

import com.skrstop.framework.components.starter.common.dsSelector.DsSelector;
import com.skrstop.framework.components.starter.redis.annotation.DSRedisson;
import com.skrstop.framework.components.starter.redis.configuration.redissonDynamic.DynamicRedissonContextHolder;
import com.skrstop.framework.components.starter.redis.configuration.redissonDynamic.DynamicRedissonProperties;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DynamicAnnotationInterceptorTest {

    static class Service {
        @DSRedisson("ds1")
        public void annotated() {
        }

        @DSRedisson("#spel")
        public void spel() {
        }

        public void plain() {
        }
    }

    @After
    public void tearDown() {
        DynamicRedissonContextHolder.clear();
    }

    private MethodInvocation invocation(String methodName) throws Exception {
        Method method = Service.class.getMethod(methodName);
        return new org.springframework.aop.framework.ReflectiveMethodInvocation(
                new Service(), new Service(), method, new Object[0], null, java.util.Collections.emptyList()) {
            @Override
            public Object proceed() {
                return "ok";
            }
        };
    }

    private com.skrstop.framework.components.starter.redis.configuration.redissonDynamic.aop.DynamicAnnotationInterceptor
    interceptor(DsSelector selector, boolean allowedPublicOnly) {
        DynamicRedissonProperties properties = new DynamicRedissonProperties();
        properties.getAop().setAllowedPublicOnly(allowedPublicOnly);
        return new com.skrstop.framework.components.starter.redis.configuration.redissonDynamic.aop.DynamicAnnotationInterceptor(properties, selector);
    }

    @Test
    public void branches() throws Throwable {
        DsSelector selector = mock(DsSelector.class);
        when(selector.matches(any(String.class))).thenReturn(true);
        when(selector.doDetermineDataSource(any(MethodInvocation.class), eq("#spel"))).thenReturn("sel");
        com.skrstop.framework.components.starter.redis.configuration.redissonDynamic.aop.DynamicAnnotationInterceptor interceptor = interceptor(selector, true);
        Assert.assertEquals("ok", interceptor.invoke(invocation("plain")));
        DynamicRedissonContextHolder.clear();
        Assert.assertEquals("ok", interceptor.invoke(invocation("annotated")));
        DynamicRedissonContextHolder.clear();
        Assert.assertEquals("ok", interceptor.invoke(invocation("spel")));
        com.skrstop.framework.components.starter.redis.configuration.redissonDynamic.aop.DynamicAnnotationInterceptor interceptor2 = interceptor(selector, false);
        Assert.assertEquals("ok", interceptor2.invoke(invocation("plain")));
        Assert.assertEquals("ok", interceptor2.invoke(invocation("annotated")));
        Assert.assertEquals("ok", interceptor2.invoke(invocation("spel")));
        DynamicRedissonContextHolder.clear();
    }
}
