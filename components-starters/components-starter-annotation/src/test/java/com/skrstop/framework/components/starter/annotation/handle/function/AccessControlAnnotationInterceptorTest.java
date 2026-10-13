package com.skrstop.framework.components.starter.annotation.handle.function;

import com.skrstop.framework.components.starter.annotation.anno.function.AccessControl;
import com.skrstop.framework.components.starter.annotation.configuration.AnnotationProperties;
import com.skrstop.framework.components.starter.annotation.exception.aspect.ControllerDeprecatedException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

import java.lang.reflect.Method;
import java.util.Collections;

public class AccessControlAnnotationInterceptorTest {

    public static class Methods {
        public String plain() {
            return "plain";
        }

        @AccessControl(alias = "")
        public String blankAlias() {
            return "x";
        }

        @AccessControl(alias = "locked")
        public String notReleased() {
            return "x";
        }

        @AccessControl(alias = "released")
        public String released() {
            return "x";
        }
    }

    private Object invoke(AnnotationProperties properties, String methodName) throws Throwable {
        Method method = Methods.class.getMethod(methodName);
        AccessControlAnnotationInterceptor interceptor = new AccessControlAnnotationInterceptor(properties);
        return interceptor.invoke(new ReflectiveMethodInvocation(
                new Methods(), new Methods(), method, new Object[0], null, Collections.emptyList()) {
            @Override
            public Object proceed() {
                return "proceeded";
            }
        });
    }

    @Test
    public void testPlainProceeds() throws Throwable {
        Assert.assertEquals("proceeded", invoke(new AnnotationProperties(), "plain"));
    }

    @Test
    public void testBlankAliasThrows() throws Throwable {
        try {
            invoke(new AnnotationProperties(), "blankAlias");
            Assert.fail();
        } catch (ControllerDeprecatedException ignored) {
        }
    }

    @Test
    public void testNotReleasedThrows() throws Throwable {
        try {
            invoke(new AnnotationProperties(), "notReleased");
            Assert.fail();
        } catch (ControllerDeprecatedException ignored) {
        }
    }

    @Test
    public void testReleasedProceeds() throws Throwable {
        AnnotationProperties properties = new AnnotationProperties();
        properties.getAccessControl().setReleaseAlias(Collections.singletonList("released"));
        Assert.assertEquals("proceeded", invoke(properties, "released"));
    }
}
