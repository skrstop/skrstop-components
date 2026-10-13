package com.skrstop.framework.components.starter.database.constant;

import org.junit.Assert;
import org.junit.Test;

public class ConstTest {

    @Test
    public void constants() {
        Assert.assertEquals("DefaultDatabaseTransactionManager", DatabaseConst.TRANSACTION_NAME_DATABASE);
        Assert.assertEquals("skrstop.database.config", GlobalConfigConst.DATABASE_PREFIX);
        Assert.assertEquals("skrstop.database.dynamic", GlobalConfigConst.DATABASE_DYNAMIC);
        Assert.assertEquals("skrstop.database.config.use-ping-method", GlobalConfigConst.DATABASE_CONFIG_CHECK_DRUID_USE_PING_METHOD);
        Assert.assertEquals("skrstop.database.config.mapper-class-location", GlobalConfigConst.DATABASE_CONFIG_CLASS_CLASS_LOCATION);
        Assert.assertEquals("classpath*:mapper/*.xml", GlobalConfigConst.DEFAULT_MAPPER_XML_LOCATION);
        Assert.assertEquals("default", GlobalConfigConst.DS_DEFAULT);
        Assert.assertEquals("master", GlobalConfigConst.DS_MASTER);
        Assert.assertEquals("slave", GlobalConfigConst.DS_SLAVE);
    }
}
