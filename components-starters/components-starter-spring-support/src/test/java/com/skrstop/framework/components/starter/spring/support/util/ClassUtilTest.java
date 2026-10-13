package com.skrstop.framework.components.starter.spring.support.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class ClassUtilTest {

    private static class Parent<T> {
    }

    private static class Child extends Parent<String> {
    }

    @Test
    public void testResolveGenericType() {
        Assert.assertEquals(String.class, (Object) ClassUtil.resolveGenericType(Child.class));
    }

}