package com.skrstop.framework.components.starter.feign.protostuff.configuration.interceptor;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class DynamicFeignClientMethodContextHolderTest {

    @After
    public void tearDown() {
        DynamicFeignClientMethodContextHolder.clear();
    }

    @Test
    public void stack() {
        Assert.assertFalse(DynamicFeignClientMethodContextHolder.peek());
        DynamicFeignClientMethodContextHolder.push(false);
        Assert.assertFalse(DynamicFeignClientMethodContextHolder.peek());
        DynamicFeignClientMethodContextHolder.push(true);
        Assert.assertTrue(DynamicFeignClientMethodContextHolder.peek());
        DynamicFeignClientMethodContextHolder.poll();
        Assert.assertFalse(DynamicFeignClientMethodContextHolder.peek());
        DynamicFeignClientMethodContextHolder.poll();
        Assert.assertFalse(DynamicFeignClientMethodContextHolder.peek());
        DynamicFeignClientMethodContextHolder.clear();
        Assert.assertFalse(DynamicFeignClientMethodContextHolder.peek());
    }
}
