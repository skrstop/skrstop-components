package com.skrstop.framework.components.starter.annotation.handle.function;

import com.skrstop.framework.components.starter.annotation.anno.function.IntranetLimit;
import com.skrstop.framework.components.starter.annotation.configuration.AnnotationProperties;
import com.skrstop.framework.components.starter.annotation.exception.aspect.IntranetLimitException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Collections;

public class IntranetLimitAnnotationInterceptorTest {

    public static class Methods {
        public String plain() {
            return "plain";
        }

        @IntranetLimit
        public String limited() {
            return "x";
        }
    }

    private final IntranetLimitAnnotationInterceptor interceptor =
            new IntranetLimitAnnotationInterceptor(new AnnotationProperties());

    private Object invoke(String methodName) throws Throwable {
        Method method = Methods.class.getMethod(methodName);
        return interceptor.invoke(new ReflectiveMethodInvocation(
                new Methods(), new Methods(), method, new Object[0], null, Collections.emptyList()) {
            @Override
            public Object proceed() {
                return "proceeded";
            }
        });
    }

    @After
    public void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testPlainProceeds() throws Throwable {
        Assert.assertEquals("proceeded", invoke("plain"));
    }

    @Test
    public void testNoRequestAttributes() throws Throwable {
        RequestContextHolder.resetRequestAttributes();
        Assert.assertEquals("proceeded", invoke("limited"));
    }

    @Test
    public void testNullRequest() throws Throwable {
        RequestAttributes ra = org.mockito.Mockito.mock(ServletRequestAttributes.class);
        RequestContextHolder.setRequestAttributes(ra);
        Assert.assertEquals("proceeded", invoke("limited"));
    }

    @Test
    public void testInnerIpProceeds() throws Throwable {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Assert.assertEquals("proceeded", invoke("limited"));
    }

    @Test
    public void testOuterIpThrows() throws Throwable {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("8.8.8.8");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        try {
            invoke("limited");
            Assert.fail();
        } catch (IntranetLimitException ignored) {
        }
    }
}
