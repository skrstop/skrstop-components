package com.skrstop.framework.components.starter.feign.protostuff.constant;

import org.junit.Assert;
import org.junit.Test;

public class GlobalConfigConstTest {

    @Test
    public void constants() {
        Assert.assertEquals("skrstop.feign.config", GlobalConfigConst.FEIGN_PREFIX);
        Assert.assertEquals("skrstop.feign.config.scan-package", GlobalConfigConst.FEIGN_CONFIG_SCAN_PACKAGE);
    }
}
