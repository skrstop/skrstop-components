package com.skrstop.framework.components.starter.redis.configuration.redisson;

import org.junit.Assert;
import org.junit.Test;

public class GlobalRedissonPropertiesTest {

    @Test
    public void props() {
        GlobalRedissonProperties p = new GlobalRedissonProperties();
        Assert.assertFalse(p.isEnabled());
        p.setEnabled(true);
        p.setConfigFile("f");
        p.setConfigYamlStr("y");
        p.setConfigJsonStr("j");
        Assert.assertTrue(p.isEnabled());
        Assert.assertEquals("f", p.getConfigFile());
        Assert.assertEquals("y", p.getConfigYamlStr());
        Assert.assertEquals("j", p.getConfigJsonStr());
    }
}
