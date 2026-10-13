package com.skrstop.framework.components.starter.database.configuration.dynamic;

import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.enums.SeataMode;
import com.baomidou.dynamic.datasource.strategy.LoadBalanceDynamicDataSourceStrategy;
import com.skrstop.framework.components.starter.database.configuration.dynamic.common.DynamicDataSourceProperties;
import com.skrstop.framework.components.starter.database.configuration.dynamic.common.DynamicDatasourceAopProperties;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class DynamicDataSourcePropertiesTest {

    @Test
    public void defaultsAndSetters() {
        DynamicDataSourceProperties p = new DynamicDataSourceProperties();
        Assert.assertFalse(p.getEnabled());
        Assert.assertEquals("master", p.getPrimary());
        Assert.assertFalse(p.getStrict());
        Assert.assertFalse(p.getP6spy());
        Assert.assertFalse(p.getSeata());
        Assert.assertFalse(p.getLazy());
        Assert.assertFalse(p.getGraceDestroy());
        Assert.assertEquals(SeataMode.AT, p.getSeataMode());
        Assert.assertNotNull(p.getPublicKey());
        Assert.assertNotNull(p.getDataSources());
        Assert.assertEquals(LoadBalanceDynamicDataSourceStrategy.class, p.getStrategy());
        Assert.assertNotNull(p.getDruid());
        Assert.assertNotNull(p.getHikari());
        Assert.assertNotNull(p.getBeecp());
        Assert.assertNotNull(p.getDbcp2());
        Assert.assertNotNull(p.getAtomikos());
        Assert.assertNotNull(p.getAop());
        Assert.assertEquals((int) OrderedValues.HIGHEST, (int) p.getAop().getOrder());

        Map<String, DataSourceProperty> dataSources = new LinkedHashMap<>();
        dataSources.put("master", new DataSourceProperty());
        p.setEnabled(true).setPrimary("db1").setStrict(true).setP6spy(true).setSeata(true)
                .setLazy(true).setGraceDestroy(true).setSeataMode(SeataMode.XA)
                .setPublicKey("pk").setDataSources(dataSources)
                .setStrategy(LoadBalanceDynamicDataSourceStrategy.class)
                .setAop(new DynamicDatasourceAopProperties());
        Assert.assertTrue(p.getEnabled());
        Assert.assertEquals("db1", p.getPrimary());
        Assert.assertTrue(p.getStrict());
        Assert.assertTrue(p.getP6spy());
        Assert.assertTrue(p.getSeata());
        Assert.assertTrue(p.getLazy());
        Assert.assertTrue(p.getGraceDestroy());
        Assert.assertEquals(SeataMode.XA, p.getSeataMode());
        Assert.assertEquals("pk", p.getPublicKey());
        Assert.assertSame(dataSources, p.getDataSources());
        Assert.assertNotNull(p.getAop());
    }

    interface OrderedValues {
        int HIGHEST = org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
    }
}
