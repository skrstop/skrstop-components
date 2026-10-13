package com.skrstop.framework.components.starter.mongodb.configuration.dynamic.aop;

import com.skrstop.framework.components.starter.common.dsSelector.DsSelector;
import com.skrstop.framework.components.starter.mongodb.annotation.DSMongo;
import com.skrstop.framework.components.starter.mongodb.configuration.dynamic.DynamicMongoAopProperties;
import com.skrstop.framework.components.starter.mongodb.configuration.dynamic.DynamicMongoContextHolder;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

import java.lang.reflect.Method;
import java.util.Collections;

public class DynamicAnnotationInterceptorTest {

    static class Service {
        @DSMongo("slave")
        public String fixed() {
            return "fixed";
        }

        @DSMongo("#key")
        public String dynamic() {
            return "dynamic";
        }

        public String plain() {
            return "plain";
        }
    }

    private final Service service = new Service();

    @Before
    @After
    public void clean() {
        DynamicMongoContextHolder.clear();
    }

    private MethodInvocation invocationOf(String methodName) throws Exception {
        Method method = Service.class.getMethod(methodName);
        // 6 参构造：target, proxy, method, args, parameterTypes(null), interceptorsAndDynamicMethods
        return new ReflectiveMethodInvocation(service, service, method, new Object[0], null, Collections.emptyList()) {
            @Override
            public Object proceed() {
                try {
                    return method.invoke(getThis(), getArguments());
                } catch (java.lang.reflect.InvocationTargetException e) {
                    throw new RuntimeException(e.getCause());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Test
    public void fixedKey() throws Throwable {
        DynamicAnnotationInterceptor interceptor =
                new DynamicAnnotationInterceptor(new DynamicMongoAopProperties(), Mockito.mock(DsSelector.class));
        Assert.assertEquals("fixed", interceptor.invoke(invocationOf("fixed")));
        // 执行后上下文已清理
        Assert.assertNull(DynamicMongoContextHolder.peek());
    }

    @Test
    public void noAnnotation() throws Throwable {
        DynamicAnnotationInterceptor interceptor =
                new DynamicAnnotationInterceptor(new DynamicMongoAopProperties(), Mockito.mock(DsSelector.class));
        Assert.assertEquals("plain", interceptor.invoke(invocationOf("plain")));
        Assert.assertNull(DynamicMongoContextHolder.peek());
    }

    @Test
    public void dynamicKey() throws Throwable {
        DsSelector selector = Mockito.mock(DsSelector.class);
        Mockito.when(selector.determineDataSource(Mockito.any(), Mockito.eq("#key"))).thenReturn("spel_ds");
        DynamicAnnotationInterceptor interceptor =
                new DynamicAnnotationInterceptor(new DynamicMongoAopProperties(), selector);
        Assert.assertEquals("dynamic", interceptor.invoke(invocationOf("dynamic")));
        Assert.assertNull(DynamicMongoContextHolder.peek());
        Mockito.verify(selector).determineDataSource(Mockito.any(), Mockito.eq("#key"));
    }
}
