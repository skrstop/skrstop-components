package com.skrstop.framework.components.starter.objectStorage.configuration.dynamic.aop;

import com.skrstop.framework.components.starter.common.dsSelector.DsSelector;
import com.skrstop.framework.components.starter.objectStorage.annotation.DSObjectStorage;
import com.skrstop.framework.components.starter.objectStorage.configuration.dynamic.DynamicDatasourceContextHolder;
import com.skrstop.framework.components.starter.objectStorage.configuration.dynamic.DynamicObjectStorageProperties;
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

public class DynamicAnnotationInterceptorTest {

    static class Service {
        @DSObjectStorage("ds1")
        public void annotated() {
        }

        @DSObjectStorage("#spel")
        public void spel() {
        }

        public void plain() {
        }
    }

    @After
    public void tearDown() {
        DynamicDatasourceContextHolder.clear();
    }

    private MethodInvocation invocation(String methodName) throws Exception {
        Method method = Service.class.getMethod(methodName);
        return new ReflectiveMethodInvocation(
                new Service(), new Service(), method, new Object[0], null, Collections.emptyList()) {
            @Override
            public Object proceed() {
                return "ok";
            }
        };
    }

    private DynamicAnnotationInterceptor interceptor(DsSelector selector, boolean allowedPublicOnly) {
        DynamicObjectStorageProperties properties = new DynamicObjectStorageProperties();
        properties.getAop().setAllowedPublicOnly(allowedPublicOnly);
        return new DynamicAnnotationInterceptor(properties, selector);
    }

    @Test
    public void branches() throws Throwable {
        DsSelector selector = mock(DsSelector.class);
        when(selector.determineDataSource(any(MethodInvocation.class), eq("#spel"))).thenReturn("sel");
        // allowedPublicOnly = true
        DynamicAnnotationInterceptor interceptor = interceptor(selector, true);
        Assert.assertEquals("ok", interceptor.invoke(invocation("plain")));
        DynamicDatasourceContextHolder.clear();
        Assert.assertEquals("ok", interceptor.invoke(invocation("annotated")));
        DynamicDatasourceContextHolder.clear();
        Assert.assertEquals("ok", interceptor.invoke(invocation("spel")));
        // allowedPublicOnly = false, AnnoFindUtil 缓存按 (method, target) 区分
        DynamicAnnotationInterceptor interceptor2 = interceptor(selector, false);
        Assert.assertEquals("ok", interceptor2.invoke(invocation("plain")));
        DynamicDatasourceContextHolder.clear();
        Assert.assertEquals("ok", interceptor2.invoke(invocation("annotated")));
        DynamicDatasourceContextHolder.clear();
        Assert.assertEquals("ok", interceptor2.invoke(invocation("spel")));
    }
}
