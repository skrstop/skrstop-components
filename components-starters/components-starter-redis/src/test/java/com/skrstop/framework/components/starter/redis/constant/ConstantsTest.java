package com.skrstop.framework.components.starter.redis.constant;

import org.junit.Assert;
import org.junit.Test;

public class ConstantsTest {

    @Test
    public void consts() {
        Assert.assertEquals("skrstop.redis.config", GlobalConfigConst.REDIS_PREFIX);
        Assert.assertEquals("skrstop.redis.dynamic", GlobalConfigConst.REDIS_DYNAMIC);
        Assert.assertEquals("skrstop.redis.redisson", GlobalConfigConst.REDISSON_PREFIX);
        Assert.assertEquals("skrstop.redis.redisson.dynamic", GlobalConfigConst.REDISSON_DYNAMIC_PREFIX);
        Assert.assertEquals(Byte.valueOf((byte) 1), ValueProcessorConst.VALUES.get(0));
        Assert.assertEquals(Byte.valueOf((byte) 2), ValueProcessorConst.VALUES.get(1));
        Assert.assertEquals(Byte.valueOf((byte) 3), ValueProcessorConst.VALUES.get(2));
        Assert.assertEquals(3, ValueProcessorConst.VALUES.size());
    }
}
