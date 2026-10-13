package com.skrstop.framework.components.starter.objectStorage.configuration.dynamic.service;

import com.skrstop.framework.components.starter.common.dsSelector.DsSelector;
import com.skrstop.framework.components.starter.objectStorage.configuration.dynamic.DynamicDatasourceContextHolder;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

import java.lang.reflect.Method;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DynamicServiceInterceptorTest {

    static class Service {
        public String doIt(String dsKey) {
            return dsKey;
        }
    }

    @After
    public void tearDown() {
        DynamicDatasourceContextHolder.clear();
    }

    private MethodInvocation invocation(Object... args) throws Exception {
        Method method = Service.class.getMethod("doIt", String.class);
        return new ReflectiveMethodInvocation(
                new Service(), new Service(), method, args, null, Collections.emptyList()) {
            @Override
            public Object proceed() {
                // 记录拦截器 push 后、poll 前的数据源
                return DynamicDatasourceContextHolder.peek() != null
                        ? DynamicDatasourceContextHolder.peek() : "ok";
            }
        };
    }

    @Test
    public void branches() throws Throwable {
        DsSelector selector = mock(DsSelector.class);
        when(selector.determineDataSource(any(MethodInvocation.class), eq("#spel"))).thenReturn("sel");
        DynamicServiceInterceptor interceptor = new DynamicServiceInterceptor(selector);
        // 正常 key
        Assert.assertEquals("ds1", interceptor.invoke(invocation("ds1")));
        DynamicDatasourceContextHolder.clear();
        // spel key
        Assert.assertEquals("sel", interceptor.invoke(invocation("#spel")));
        DynamicDatasourceContextHolder.clear();
        // 空参数数组：ObjectUtil.isNull(arguments)=false, arguments.length <= 0=true, push(null) 后 peek 为 ""
        Assert.assertEquals("", interceptor.invoke(invocation(new Object[0])));
        DynamicDatasourceContextHolder.clear();
        // null arguments: ObjectUtil.isNull(arguments)=true 短路
        MethodInvocation nullArgs = mock(MethodInvocation.class);
        when(nullArgs.getArguments()).thenReturn(null);
        when(nullArgs.proceed()).thenReturn("ok");
        Assert.assertEquals("ok", interceptor.invoke(nullArgs));
        DynamicDatasourceContextHolder.clear();
    }
}
