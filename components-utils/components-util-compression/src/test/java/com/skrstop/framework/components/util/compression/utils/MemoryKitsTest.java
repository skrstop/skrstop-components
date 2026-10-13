package com.skrstop.framework.components.util.compression.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class MemoryKitsTest {

    @Test
    public void testMemoryInfo() {
        Assert.assertTrue(MemoryKits.getMaxMemory() > 0);
        Assert.assertTrue(MemoryKits.getTotalMemory() > 0);
        Assert.assertTrue(MemoryKits.getFreeMemory() >= 0);
        Assert.assertTrue(MemoryKits.getUsedMemory() >= 0);
    }

}
