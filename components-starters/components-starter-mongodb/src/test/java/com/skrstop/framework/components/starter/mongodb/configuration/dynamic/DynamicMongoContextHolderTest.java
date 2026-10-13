package com.skrstop.framework.components.starter.mongodb.configuration.dynamic;

import org.junit.Assert;
import org.junit.Test;

public class DynamicMongoContextHolderTest {

    @Test
    public void pushPeekPoll() {
        DynamicMongoContextHolder.clear();
        Assert.assertNull(DynamicMongoContextHolder.peek());
        Assert.assertEquals("master", DynamicMongoContextHolder.push("master"));
        Assert.assertEquals("master", DynamicMongoContextHolder.peek());
        // 嵌套
        Assert.assertEquals("slave", DynamicMongoContextHolder.push("slave"));
        Assert.assertEquals("slave", DynamicMongoContextHolder.peek());
        DynamicMongoContextHolder.poll();
        Assert.assertEquals("master", DynamicMongoContextHolder.peek());
        DynamicMongoContextHolder.poll();
        Assert.assertNull(DynamicMongoContextHolder.peek());
        // push null -> 空串
        Assert.assertEquals("", DynamicMongoContextHolder.push(null));
        DynamicMongoContextHolder.poll();
        DynamicMongoContextHolder.clear();
        Assert.assertNull(DynamicMongoContextHolder.peek());
    }
}
