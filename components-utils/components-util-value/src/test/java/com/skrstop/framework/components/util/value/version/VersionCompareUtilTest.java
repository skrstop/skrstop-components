package com.skrstop.framework.components.util.value.version;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class VersionCompareUtilTest {

    @Test
    public void testCompare() {
        Assert.assertEquals(VersionCompareUtil.GTR, VersionCompareUtil.compareVersion("1.0.1", "1.0.0"));
        Assert.assertEquals(VersionCompareUtil.LSS, VersionCompareUtil.compareVersion("1.0.0", "1.0.1"));
        Assert.assertEquals(VersionCompareUtil.EQU, VersionCompareUtil.compareVersion("1.0.0", "1.0.0"));
        // 段数不同
        Assert.assertEquals(VersionCompareUtil.GTR, VersionCompareUtil.compareVersion("1.0.0.1", "1.0.0"));
        Assert.assertEquals(VersionCompareUtil.LSS, VersionCompareUtil.compareVersion("1.0.0", "1.0.0.1"));
        // 末尾 0 视为相等
        Assert.assertEquals(VersionCompareUtil.EQU, VersionCompareUtil.compareVersion("1.0.0.0", "1.0"));
        Assert.assertEquals(VersionCompareUtil.EQU, VersionCompareUtil.compareVersion("1.0", "1.0.0.0"));
    }

    @Test
    public void testCompareWithResult() {
        Assert.assertTrue(VersionCompareUtil.compareVersion("1.0.1", "1.0.0", VersionCompareUtil.GTR));
        Assert.assertFalse(VersionCompareUtil.compareVersion("1.0.1", "1.0.0", VersionCompareUtil.LSS));
    }

}
