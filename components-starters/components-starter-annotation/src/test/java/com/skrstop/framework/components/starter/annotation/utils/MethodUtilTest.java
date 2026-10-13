package com.skrstop.framework.components.starter.annotation.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MethodUtilTest {

    public static class Target {
        public String foo() {
            return "foo";
        }
    }

    private static MethodSignature signature(String name, Class<?>[] parameterTypes) {
        return (MethodSignature) Proxy.newProxyInstance(
                MethodSignature.class.getClassLoader(),
                new Class[]{MethodSignature.class},
                (proxy, method, args) -> {
                    if ("getName".equals(method.getName())) {
                        return name;
                    }
                    if ("getParameterTypes".equals(method.getName())) {
                        return parameterTypes;
                    }
                    return null;
                });
    }

    private static ProceedingJoinPoint joinPoint(Signature sig, Object target) {
        return (ProceedingJoinPoint) Proxy.newProxyInstance(
                ProceedingJoinPoint.class.getClassLoader(),
                new Class[]{ProceedingJoinPoint.class},
                (proxy, method, args) -> {
                    if ("getSignature".equals(method.getName())) {
                        return sig;
                    }
                    if ("getTarget".equals(method.getName())) {
                        return target;
                    }
                    return null;
                });
    }

    @Test
    public void testGetMethod() throws NoSuchMethodException {
        ProceedingJoinPoint jp = joinPoint(signature("foo", new Class[0]), new Target());
        Method method = MethodUtil.getMethod(jp);
        Assert.assertEquals("foo", method.getName());
    }

    @Test
    public void testGetMethodMissing() {
        ProceedingJoinPoint jp = joinPoint(signature("missing", new Class[0]), new Target());
        try {
            MethodUtil.getMethod(jp);
            Assert.fail();
        } catch (NoSuchMethodException ignored) {
        }
    }
}
