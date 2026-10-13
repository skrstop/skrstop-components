package com.skrstop.framework.components.starter.web.utils;

import com.skrstop.framework.components.starter.web.response.DisableGlobalResponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ClassUtilTest {

    @DisableGlobalResponse
    static class Dummy {
        @DisableGlobalResponse
        public void method() {
        }

        public void plain() {
        }

        public Dummy() {
        }

        public Dummy(int a) {
        }
    }

    @Test
    public void testMethodParameter() throws Exception {
        Constructor<Dummy> ctor = Dummy.class.getConstructor(int.class);
        MethodParameter mp = ClassUtil.getMethodParameter(ctor, 0);
        Assert.assertNotNull(mp);
        Assert.assertEquals("a", mp.getParameterName());

        Method m = Dummy.class.getMethod("method");
        MethodParameter mp2 = ClassUtil.getMethodParameter(m, -1);
        Assert.assertNotNull(mp2);
    }

    @Test
    public void testGetAnnotationOnMethod() throws Exception {
        Method m = Dummy.class.getMethod("method");
        Assert.assertNotNull(ClassUtil.getAnnotation(m, DisableGlobalResponse.class));

        Method plain = Dummy.class.getMethod("plain");
        Assert.assertNotNull(ClassUtil.getAnnotation(plain, DisableGlobalResponse.class));

        Method hashCode = Object.class.getMethod("hashCode");
        Assert.assertNull(ClassUtil.getAnnotation(hashCode, ResponseBody.class));
    }

    @Test
    public void testGetAnnotationOnHandlerMethod() throws Exception {
        Object bean = new Dummy();
        Method m = Dummy.class.getMethod("method");
        HandlerMethod hm = new HandlerMethod(bean, m);
        Assert.assertNotNull(ClassUtil.getAnnotation(hm, DisableGlobalResponse.class));

        Method plain = Dummy.class.getMethod("plain");
        HandlerMethod hm2 = new HandlerMethod(bean, plain);
        Assert.assertNotNull(ClassUtil.getAnnotation(hm2, DisableGlobalResponse.class));

        Method objectM = Object.class.getMethod("hashCode");
        HandlerMethod hm3 = new HandlerMethod(new Object(), objectM);
        Assert.assertNull(ClassUtil.getAnnotation(hm3, ResponseBody.class));
    }

    @Test
    public void testClassUtilInstantiable() {
        Assert.assertNotNull(new ClassUtil());
    }
}
