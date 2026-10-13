package com.skrstop.framework.components.starter.redis.configuration.commonDynamic;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class DynamicConnectFactoryContextHolderTest {

    @After
    public void tearDown() {
        DynamicConnectFactoryContextHolder.clear();
    }

    @Test
    public void stack() {
        Assert.assertNull(DynamicConnectFactoryContextHolder.peek());
        Assert.assertEquals("", DynamicConnectFactoryContextHolder.push(null));
        Assert.assertEquals("", DynamicConnectFactoryContextHolder.peek());
        DynamicConnectFactoryContextHolder.poll();
        Assert.assertNull(DynamicConnectFactoryContextHolder.peek());
        Assert.assertEquals("a", DynamicConnectFactoryContextHolder.push("a"));
        Assert.assertEquals("b", DynamicConnectFactoryContextHolder.push("b"));
        Assert.assertEquals("b", DynamicConnectFactoryContextHolder.peek());
        DynamicConnectFactoryContextHolder.poll();
        Assert.assertEquals("a", DynamicConnectFactoryContextHolder.peek());
        DynamicConnectFactoryContextHolder.clear();
        Assert.assertNull(DynamicConnectFactoryContextHolder.peek());
    }
}
