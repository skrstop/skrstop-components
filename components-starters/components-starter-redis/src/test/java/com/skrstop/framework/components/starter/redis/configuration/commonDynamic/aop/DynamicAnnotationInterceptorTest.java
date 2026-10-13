package com.skrstop.framework.components.starter.redis.configuration.commonDynamic.aop;

import com.skrstop.framework.components.starter.common.dsSelector.DsSelector;
import com.skrstop.framework.components.starter.redis.annotation.DSRedis;
import com.skrstop.framework.components.starter.redis.configuration.commonDynamic.DynamicConnectFactoryContextHolder;
import com.skrstop.framework.components.starter.redis.configuration.commonDynamic.DynamicRedisProperties;
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
        @DSRedis("ds1")
        public void annotated() {
        }

        @DSRedis("#spel")
        public void spel() {
        }

        public void plain() {
        }
    }

    @After
    public void tearDown() {
        DynamicConnectFactoryContextHolder.clear();
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

    private DynamicAnnotationInterceptor interceptor(DsSelector selector, boolean allowedPublicOnly) {
        DynamicRedisProperties properties = new DynamicRedisProperties();
        properties.getAop().setAllowedPublicOnly(allowedPublicOnly);
        return new DynamicAnnotationInterceptor(properties, selector);
    }

    @Test
    public void branches() throws Throwable {
        DsSelector selector = mock(DsSelector.class);
        when(selector.matches(any(String.class))).thenReturn(true);
        when(selector.doDetermineDataSource(any(MethodInvocation.class), eq("#spel"))).thenReturn("sel");
        // allowedPublicOnly = true
        DynamicAnnotationInterceptor interceptor = interceptor(selector, true);
        Assert.assertEquals("ok", interceptor.invoke(invocation("plain")));
        DynamicConnectFactoryContextHolder.clear();
        Assert.assertEquals("ok", interceptor.invoke(invocation("annotated")));
        DynamicConnectFactoryContextHolder.clear();
        Assert.assertEquals("ok", interceptor.invoke(invocation("spel")));
        // allowedPublicOnly = false
        DynamicAnnotationInterceptor interceptor2 = interceptor(selector, false);
        Assert.assertEquals("ok", interceptor2.invoke(invocation("plain")));
        Assert.assertEquals("ok", interceptor2.invoke(invocation("annotated")));
        Assert.assertEquals("ok", interceptor2.invoke(invocation("spel")));
        DynamicConnectFactoryContextHolder.clear();
    }
}
