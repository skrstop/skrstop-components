package com.skrstop.framework.components.starter.database.configuration.dynamic;

import com.skrstop.framework.components.starter.database.configuration.dynamic.common.DynamicDatasourceAopProperties;
import org.junit.Assert;
import org.junit.Test;

public class DynamicDatasourceAopPropertiesTest {

    @Test
    public void defaultsAndBuilder() {
        DynamicDatasourceAopProperties p = new DynamicDatasourceAopProperties();
        Assert.assertTrue(p.getEnabled());
        Assert.assertEquals(org.springframework.core.Ordered.HIGHEST_PRECEDENCE, (int) p.getOrder());
        Assert.assertTrue(p.getAllowedPublicOnly());

        DynamicDatasourceAopProperties built = DynamicDatasourceAopProperties.builder()
                .enabled(false).order(5).allowedPublicOnly(false).build();
        Assert.assertFalse(built.getEnabled());
        Assert.assertEquals(5, (int) built.getOrder());
        Assert.assertFalse(built.getAllowedPublicOnly());
        Assert.assertSame(built, built.setEnabled(true));
        built.setOrder(7);
        built.setAllowedPublicOnly(true);
        Assert.assertEquals(7, (int) built.getOrder());
        Assert.assertTrue(built.getAllowedPublicOnly());
    }
}
