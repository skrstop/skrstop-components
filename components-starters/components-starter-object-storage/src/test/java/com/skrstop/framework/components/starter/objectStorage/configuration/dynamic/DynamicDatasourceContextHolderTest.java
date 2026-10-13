package com.skrstop.framework.components.starter.objectStorage.configuration.dynamic;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class DynamicDatasourceContextHolderTest {

    @After
    public void tearDown() {
        DynamicDatasourceContextHolder.clear();
    }

    @Test
    public void stack() {
        Assert.assertNull(DynamicDatasourceContextHolder.peek());
        Assert.assertEquals("", DynamicDatasourceContextHolder.push(null));
        Assert.assertEquals("", DynamicDatasourceContextHolder.peek());
        DynamicDatasourceContextHolder.poll();
        Assert.assertNull(DynamicDatasourceContextHolder.peek());
        Assert.assertEquals("a", DynamicDatasourceContextHolder.push("a"));
        Assert.assertEquals("b", DynamicDatasourceContextHolder.push("b"));
        Assert.assertEquals("b", DynamicDatasourceContextHolder.peek());
        DynamicDatasourceContextHolder.poll();
        Assert.assertEquals("a", DynamicDatasourceContextHolder.peek());
        DynamicDatasourceContextHolder.clear();
        Assert.assertNull(DynamicDatasourceContextHolder.peek());
    }
}
