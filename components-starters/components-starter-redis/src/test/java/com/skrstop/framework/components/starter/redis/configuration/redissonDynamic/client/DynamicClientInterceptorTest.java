package com.skrstop.framework.components.starter.redis.configuration.redissonDynamic.client;

import com.skrstop.framework.components.starter.common.dsSelector.DsSelector;
import com.skrstop.framework.components.starter.redis.configuration.redissonDynamic.DynamicRedissonContextHolder;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DynamicClientInterceptorTest {

    static class Service {
        public void m() {
        }
    }

    @After
    public void tearDown() {
        DynamicRedissonContextHolder.clear();
    }

    private MethodInvocation invocation(Object[] args) throws Exception {
        Method method = Service.class.getMethod("m");
        return new org.springframework.aop.framework.ReflectiveMethodInvocation(
                new Service(), new Service(), method, args, null, java.util.Collections.emptyList()) {
            @Override
            public Object proceed() {
                return "ok";
            }
        };
    }

    @Test
    public void branches() throws Throwable {
        DsSelector selector = mock(DsSelector.class);
        when(selector.matches(any(String.class))).thenReturn(true);
        when(selector.doDetermineDataSource(any(MethodInvocation.class), eq("#spel"))).thenReturn("sel");
        DynamicClientInterceptor interceptor = new DynamicClientInterceptor(selector);

        // args == null（ReflectiveMethodInvocation 会把 null 转为空数组，这里用 mock 保证 null）
        MethodInvocation nullArgsInvocation = mock(MethodInvocation.class);
        when(nullArgsInvocation.getArguments()).thenReturn(null);
        Assert.assertNull(interceptor.invoke(nullArgsInvocation));
        DynamicRedissonContextHolder.clear();
        Assert.assertEquals("ok", interceptor.invoke(invocation(new Object[0])));
        DynamicRedissonContextHolder.clear();
        Assert.assertEquals("ok", interceptor.invoke(invocation(new Object[]{"ds1"})));
        DynamicRedissonContextHolder.clear();
        Assert.assertEquals("ok", interceptor.invoke(invocation(new Object[]{"#spel"})));
    }
}
