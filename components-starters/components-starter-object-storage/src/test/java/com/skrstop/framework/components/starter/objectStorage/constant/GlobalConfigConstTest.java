package com.skrstop.framework.components.starter.objectStorage.constant;

import org.junit.Assert;
import org.junit.Test;

public class GlobalConfigConstTest {

    @Test
    public void constants() {
        Assert.assertEquals("skrstop.object-storage", GlobalConfigConst.OBJECT_STORAGE_PREFIX);
        Assert.assertEquals("skrstop.object-storage.dynamic", GlobalConfigConst.DYNAMIC_OBJECT_STORAGE_PREFIX);
    }
}
