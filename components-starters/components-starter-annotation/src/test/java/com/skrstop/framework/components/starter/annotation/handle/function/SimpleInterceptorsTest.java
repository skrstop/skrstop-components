package com.skrstop.framework.components.starter.annotation.handle.function;

import com.skrstop.framework.components.starter.annotation.anno.function.AccessControl;
import com.skrstop.framework.components.starter.annotation.anno.function.ExecuteTimeLog;
import com.skrstop.framework.components.starter.annotation.anno.function.IntranetLimit;
import com.skrstop.framework.components.starter.annotation.configuration.AnnotationProperties;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @author 蒋时华
 */
public class SimpleInterceptorsTest {

    public static class Service {
        @ExecuteTimeLog("tag")
        public String annotated() {
            return "ok";
        }

        @ExecuteTimeLog
        public String defaultAnnotated() {
            return "ok";
        }

        public String plain() {
            return "ok";
        }

        @IntranetLimit
        public String intranet() {
            return "ok";
        }

        @AccessControl(alias = "released")
        public String released() {
            return "ok";
        }

        @AccessControl(alias = "blocked")
        public String blocked() {
            return "ok";
        }

        @AccessControl
        public String blockedDefault() {
            return "ok";
        }
    }

    private static MethodInvocation invocation(Service service, String methodName) throws Exception {
        Method method = Service.class.getMethod(methodName);
        Object target = service;
        org.springframework.aop.framework.ProxyFactory proxyFactory = new org.springframework.aop.framework.ProxyFactory();
        proxyFactory.setTarget(target);
        proxyFactory.setInterfaces(Service.class.getDeclaredMethod(methodName).getDeclaringClass().isInterface()
                ? new Class[0] : new Class[0]);
        return new org.springframework.aop.framework.ReflectiveMethodInvocation(
                target, target, method, new Object[0], null, java.util.Collections.emptyList()) {
            @Override
            public Object proceed() {
                return "proceeded";
            }
        };
    }

    private static MethodInvocation simple(Service service, String methodName) throws Exception {
        Method method = Service.class.getMethod(methodName);
        return new org.springframework.aop.framework.ReflectiveMethodInvocation(
                service, service, method, new Object[0], null, java.util.Collections.emptyList()) {
            @Override
            public Object proceed() {
                return "proceeded";
            }
        };
    }

    @Test
    public void testExecuteTimeLogInterceptor() throws Throwable {
        AnnotationProperties properties = new AnnotationProperties();
        ExecuteTimeAnnotationInterceptor interceptor = new ExecuteTimeAnnotationInterceptor(properties);
        // 有注解 info 级别
        Assert.assertEquals("proceeded", interceptor.invoke(simple(new Service(), "annotated")));
        // 无 value
        Assert.assertEquals("proceeded", interceptor.invoke(simple(new Service(), "defaultAnnotated")));
        // 无注解 → 直接 proceed
        Assert.assertEquals("proceeded", interceptor.invoke(simple(new Service(), "plain")));
        // debug 级别
        properties.getExecuteTimeLog().setLogLevelInfo(false);
        Assert.assertEquals("proceeded", interceptor.invoke(simple(new Service(), "annotated")));
    }

    @Test
    public void testAccessControlInterceptor() throws Throwable {
        AnnotationProperties properties = new AnnotationProperties();
        properties.getAccessControl().setReleaseAlias(java.util.Collections.singletonList("released"));
        AccessControlAnnotationInterceptor interceptor = new AccessControlAnnotationInterceptor(properties);
        // 放行
        Assert.assertEquals("proceeded", interceptor.invoke(simple(new Service(), "released")));
        // 未放行（alias 在黑名单）
        try {
            interceptor.invoke(simple(new Service(), "blocked"));
            Assert.fail();
        } catch (
                com.skrstop.framework.components.starter.annotation.exception.aspect.ControllerDeprecatedException ignored) {
        }
        // 无 alias → 拦截
        try {
            interceptor.invoke(simple(new Service(), "blockedDefault"));
            Assert.fail();
        } catch (
                com.skrstop.framework.components.starter.annotation.exception.aspect.ControllerDeprecatedException ignored) {
        }
    }

    @Test
    public void testIntranetLimitInterceptor() throws Throwable {
        AnnotationProperties properties = new AnnotationProperties();
        IntranetLimitAnnotationInterceptor interceptor = new IntranetLimitAnnotationInterceptor(properties);
        // 无注解 → 直接 proceed
        Assert.assertEquals("proceeded", interceptor.invoke(simple(new Service(), "plain")));
        // 有注解但无请求上下文 → proceed
        Assert.assertEquals("proceeded", interceptor.invoke(simple(new Service(), "intranet")));
    }

}