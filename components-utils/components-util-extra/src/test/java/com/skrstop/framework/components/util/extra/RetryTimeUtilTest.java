package com.skrstop.framework.components.util.extra;

import com.skrstop.framework.components.util.extra.calculation.RetryTimeUtil;
import org.junit.Assert;
import org.junit.Test;

public class RetryTimeUtilTest {

    @Test
    public void testIfRetry() {
        // offset = 100s；2^3 * 30 = 240 → 不重试
        Assert.assertFalse(RetryTimeUtil.ifRetry(100000L, 0L, 3, 30));
        // offset = 300s >= 2^3 * 30 = 240 → 重试
        Assert.assertTrue(RetryTimeUtil.ifRetry(300000L, 0L, 3, 30));
        // 边界相等 → 重试
        Assert.assertTrue(RetryTimeUtil.ifRetry(240000L, 0L, 3, 30));
    }

    @Test
    public void testGetNextRetryTime() {
        // 2^3 * 30 = 240
        Assert.assertEquals(Long.valueOf(240L), RetryTimeUtil.getNextRetryTime(0L, 3, 30));
        Assert.assertEquals(Long.valueOf(30L), RetryTimeUtil.getNextRetryTime(0L, 0, 30));
    }
}
