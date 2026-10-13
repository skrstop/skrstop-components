package com.skrstop.framework.components.starter.database.configuration;

import com.skrstop.framework.components.starter.database.constant.GlobalConfigConst;
import org.junit.Assert;
import org.junit.Test;

public class GlobalDatabasePropertiesTest {

    @Test
    public void defaultsAndSetters() {
        GlobalDatabaseProperties p = new GlobalDatabaseProperties();
        Assert.assertFalse(p.isSqlHealthyCheck());
        Assert.assertFalse(p.isUsePingMethod());
        Assert.assertEquals("", p.getMapperClassLocation());
        Assert.assertEquals(GlobalConfigConst.DEFAULT_MAPPER_XML_LOCATION, p.getMapperXmlLocation());
        Assert.assertTrue(p.isMapUnderscoreToCamelCase());
        Assert.assertTrue(p.isLazyLoadingEnabled());

        p.setSqlHealthyCheck(true);
        p.setUsePingMethod(true);
        p.setMapperClassLocation("com.x");
        p.setMapperXmlLocation("classpath:x/*.xml");
        p.setMapUnderscoreToCamelCase(false);
        p.setLazyLoadingEnabled(false);
        Assert.assertTrue(p.isSqlHealthyCheck());
        Assert.assertTrue(p.isUsePingMethod());
        Assert.assertEquals("com.x", p.getMapperClassLocation());
        Assert.assertEquals("classpath:x/*.xml", p.getMapperXmlLocation());
        Assert.assertFalse(p.isMapUnderscoreToCamelCase());
        Assert.assertFalse(p.isLazyLoadingEnabled());
    }
}
