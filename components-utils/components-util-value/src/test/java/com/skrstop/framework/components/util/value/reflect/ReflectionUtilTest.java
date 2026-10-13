package com.skrstop.framework.components.util.value.reflect;

import org.junit.Test;

import java.util.Set;

/**
 * @author 蒋时华
 */
public class ReflectionUtilTest {

    @Test
    public void testGetSubTypesOfByPackage() {
        Set<Class<? extends Exception>> subs = ReflectionUtil.getSubTypesOf("java.lang", Exception.class);
        org.junit.Assert.assertNotNull(subs);
    }

    @Test
    public void testGetSubTypesOf() {
        Set<Class<? extends Exception>> subs = ReflectionUtil.getSubTypesOf(Exception.class);
        org.junit.Assert.assertNotNull(subs);
    }

} 
