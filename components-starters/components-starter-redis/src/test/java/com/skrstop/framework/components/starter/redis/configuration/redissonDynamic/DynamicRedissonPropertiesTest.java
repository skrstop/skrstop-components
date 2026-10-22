package com.skrstop.framework.components.starter.redis.configuration.redissonDynamic;

import org.junit.Assert;
import org.junit.Test;

import org.springframework.core.Ordered;

import java.util.LinkedHashMap;
import java.util.Map;

public class DynamicRedissonPropertiesTest {

    @Test
    public void props() {
        DynamicRedissonProperties p = new DynamicRedissonProperties();
        Assert.assertFalse(p.isEnabled());
        Assert.assertEquals("default", p.getPrimary());
        Assert.assertFalse(p.isExceptionWhileNotFound());
        Assert.assertNotNull(p.getAop());
        Assert.assertNotNull(p.getClient());
        Assert.assertNotNull(p.getDataSources());
        Assert.assertTrue(p.getAop().isEnabled());
        Assert.assertEquals(Ordered.HIGHEST_PRECEDENCE + 1, (int) p.getAop().getOrder());
        Assert.assertTrue(p.getAop().isAllowedPublicOnly());
        Assert.assertTrue(p.getClient().isEnabled());
        Assert.assertEquals(Ordered.HIGHEST_PRECEDENCE, (int) p.getClient().getOrder());

        DynamicRedissonProperties.Config config = new DynamicRedissonProperties.Config();
        Assert.assertNull(config.getConfigFile());
        config.setConfigFile("f").setConfigYamlStr("y");
        Assert.assertEquals("f", config.getConfigFile());
        Assert.assertEquals("y", config.getConfigYamlStr());
        new DynamicRedissonProperties.Config("f", "y");
        DynamicRedissonProperties.Config.builder().build();

        DynamicRedissonProperties.Aop aop = new DynamicRedissonProperties.Aop();
        aop.setEnabled(false).setOrder(2).setAllowedPublicOnly(false);
        Assert.assertFalse(aop.isEnabled());
        Assert.assertEquals(2, (int) aop.getOrder());
        Assert.assertFalse(aop.isAllowedPublicOnly());
        new DynamicRedissonProperties.Aop(false, 2, false);

        DynamicRedissonProperties.Client client = new DynamicRedissonProperties.Client();
        client.setEnabled(false).setOrder(3);
        Assert.assertFalse(client.isEnabled());
        Assert.assertEquals(3, (int) client.getOrder());
        new DynamicRedissonProperties.Client(false, 3);

        Map<String, DynamicRedissonProperties.Config> dataSources = new LinkedHashMap<>();
        dataSources.put("d", new DynamicRedissonProperties.Config());
        DynamicRedissonProperties full = new DynamicRedissonProperties();
        full.setEnabled(true);
        full.setPrimary("p");
        full.setExceptionWhileNotFound(true);
        full.setAop(aop);
        full.setClient(client);
        full.setDataSources(dataSources);
        Assert.assertTrue(full.isEnabled());
        Assert.assertEquals("p", full.getPrimary());
        Assert.assertTrue(full.isExceptionWhileNotFound());
        Assert.assertEquals(1, full.getDataSources().size());
    }
}
