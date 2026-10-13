package com.skrstop.framework.components.starter.redis.configuration.redissonDynamic;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class DynamicRedissonContextHolderTest {

    @After
    public void tearDown() {
        DynamicRedissonContextHolder.clear();
    }

    @Test
    public void stack() {
        Assert.assertNull(DynamicRedissonContextHolder.peek());
        Assert.assertEquals("", DynamicRedissonContextHolder.push(null));
        Assert.assertEquals("", DynamicRedissonContextHolder.peek());
        DynamicRedissonContextHolder.poll();
        Assert.assertNull(DynamicRedissonContextHolder.peek());
        Assert.assertEquals("a", DynamicRedissonContextHolder.push("a"));
        Assert.assertEquals("b", DynamicRedissonContextHolder.push("b"));
        Assert.assertEquals("b", DynamicRedissonContextHolder.peek());
        DynamicRedissonContextHolder.poll();
        Assert.assertEquals("a", DynamicRedissonContextHolder.peek());
        DynamicRedissonContextHolder.clear();
        Assert.assertNull(DynamicRedissonContextHolder.peek());
    }
}
