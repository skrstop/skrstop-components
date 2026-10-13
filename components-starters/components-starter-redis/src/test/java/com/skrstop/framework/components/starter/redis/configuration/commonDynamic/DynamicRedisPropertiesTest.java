package com.skrstop.framework.components.starter.redis.configuration.commonDynamic;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.core.Ordered;

import java.util.LinkedHashMap;
import java.util.Map;

public class DynamicRedisPropertiesTest {

    @Test
    public void props() {
        DynamicRedisProperties p = new DynamicRedisProperties();
        Assert.assertFalse(p.isEnabled());
        Assert.assertEquals("default", p.getPrimary());
        Assert.assertFalse(p.isExceptionWhileNotFound());
        Assert.assertNotNull(p.getAop());
        Assert.assertNotNull(p.getService());
        Assert.assertNotNull(p.getDataSources());
        Assert.assertTrue(p.getAop().isEnabled());
        Assert.assertEquals(Ordered.HIGHEST_PRECEDENCE + 1, (int) p.getAop().getOrder());
        Assert.assertTrue(p.getAop().isAllowedPublicOnly());
        Assert.assertTrue(p.getService().isEnabled());
        Assert.assertEquals(Ordered.HIGHEST_PRECEDENCE, (int) p.getService().getOrder());

        DynamicRedisProperties.Aop aop = new DynamicRedisProperties.Aop();
        aop.setEnabled(false).setOrder(2).setAllowedPublicOnly(false);
        Assert.assertFalse(aop.isEnabled());
        Assert.assertEquals(2, (int) aop.getOrder());
        Assert.assertFalse(aop.isAllowedPublicOnly());
        new DynamicRedisProperties.Aop(false, 2, false);

        DynamicRedisProperties.Service service = new DynamicRedisProperties.Service();
        service.setEnabled(false).setOrder(3);
        Assert.assertFalse(service.isEnabled());
        Assert.assertEquals(3, (int) service.getOrder());
        new DynamicRedisProperties.Service(false, 3);

        Map<String, RedisProperties> dataSources = new LinkedHashMap<>();
        dataSources.put("d", new RedisProperties());
        DynamicRedisProperties full = new DynamicRedisProperties(true, "p", true, aop, service, dataSources);
        Assert.assertTrue(full.isEnabled());
        Assert.assertEquals("p", full.getPrimary());
        Assert.assertTrue(full.isExceptionWhileNotFound());
        Assert.assertEquals(1, full.getDataSources().size());
    }
}
