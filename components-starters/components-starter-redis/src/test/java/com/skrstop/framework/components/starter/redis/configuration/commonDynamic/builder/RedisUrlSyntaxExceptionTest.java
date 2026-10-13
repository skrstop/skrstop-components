package com.skrstop.framework.components.starter.redis.configuration.commonDynamic.builder;

import org.junit.Assert;
import org.junit.Test;

public class RedisUrlSyntaxExceptionTest {

    @Test
    public void message() {
        RedisUrlSyntaxException ex = new RedisUrlSyntaxException("u", new RuntimeException("c"));
        Assert.assertEquals("Invalid Redis URL 'u'", ex.getMessage());
        Assert.assertEquals("u", ex.getUrl());
        Assert.assertNotNull(ex.getCause());
        RedisUrlSyntaxException ex2 = new RedisUrlSyntaxException("u2");
        Assert.assertEquals("Invalid Redis URL 'u2'", ex2.getMessage());
        Assert.assertEquals("u2", ex2.getUrl());
        Assert.assertNull(ex2.getCause());
    }
}
