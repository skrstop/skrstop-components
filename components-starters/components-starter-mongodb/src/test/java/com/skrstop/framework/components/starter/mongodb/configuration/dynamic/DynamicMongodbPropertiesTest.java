package com.skrstop.framework.components.starter.mongodb.configuration.dynamic;

import com.skrstop.framework.components.starter.mongodb.constant.GlobalConfigConst;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;

import java.util.LinkedHashMap;
import java.util.Map;

public class DynamicMongodbPropertiesTest {

    @Test
    public void defaultsAndSetters() {
        DynamicMongodbProperties p = new DynamicMongodbProperties();
        Assert.assertFalse(p.getEnabled());
        Assert.assertEquals("master", p.getPrimary());
        Assert.assertNotNull(p.getDataSources());
        Assert.assertNotNull(p.getAop());
        Assert.assertTrue(p.getAop().getEnabled());
        Assert.assertEquals(Integer.valueOf(Integer.MIN_VALUE), p.getAop().getOrder());
        Assert.assertTrue(p.getAop().getAllowedPublicOnly());

        Map<String, MongoProperties> dataSources = new LinkedHashMap<>();
        dataSources.put(GlobalConfigConst.DS_MASTER, new MongoProperties());
        p.setEnabled(true);
        p.setPrimary(GlobalConfigConst.DS_SLAVE);
        p.setDataSources(dataSources);
        p.setAop(new DynamicMongoAopProperties().setEnabled(false).setOrder(1).setAllowedPublicOnly(false));
        Assert.assertTrue(p.getEnabled());
        Assert.assertEquals(GlobalConfigConst.DS_SLAVE, p.getPrimary());
        Assert.assertSame(dataSources, p.getDataSources());
        Assert.assertFalse(p.getAop().getEnabled());
        Assert.assertEquals(Integer.valueOf(1), p.getAop().getOrder());
        Assert.assertFalse(p.getAop().getAllowedPublicOnly());
    }

    @Test
    public void aopPropertiesBuilder() {
        DynamicMongoAopProperties aop = DynamicMongoAopProperties.builder()
                .enabled(false)
                .order(9)
                .allowedPublicOnly(false)
                .build();
        Assert.assertFalse(aop.getEnabled());
        Assert.assertEquals(Integer.valueOf(9), aop.getOrder());
        Assert.assertFalse(aop.getAllowedPublicOnly());
        Assert.assertNotNull(new DynamicMongoAopProperties(false, 9, false));
        Assert.assertNotNull(new DynamicMongoAopProperties());
        aop.setOrder(2);
        aop.setEnabled(true);
        aop.setAllowedPublicOnly(true);
        Assert.assertEquals(Integer.valueOf(2), aop.getOrder());
        Assert.assertTrue(aop.getEnabled());
        Assert.assertTrue(aop.getAllowedPublicOnly());
    }
}
