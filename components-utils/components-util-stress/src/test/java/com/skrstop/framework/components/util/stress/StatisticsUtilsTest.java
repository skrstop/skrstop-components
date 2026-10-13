package com.skrstop.framework.components.util.stress;

import com.skrstop.framework.components.util.stress.util.StatisticsUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author 蒋时华
 */
public class StatisticsUtilsTest {

    @Test
    public void testConstructor() {
        new StatisticsUtils();
    }

    @Test
    public void testGetTotal() {
        Assert.assertEquals(6L, StatisticsUtils.getTotal(Arrays.asList(1L, 2L, 3L)));
        Assert.assertEquals(0L, StatisticsUtils.getTotal(Collections.emptyList()));
    }

    @Test
    public void testGetAverage() {
        Assert.assertEquals(2.0F, StatisticsUtils.getAverage(Arrays.asList(1L, 2L, 3L)), 0.001F);
        Assert.assertEquals(2.0F, StatisticsUtils.getAverage(6L, 3), 0.001F);
    }

    @Test
    public void testGetTps() {
        Assert.assertEquals(2000.0F, StatisticsUtils.getTps(500.0F, 1000), 0.001F);
    }

    @Test
    public void testToMsAndSecond() {
        Assert.assertEquals(1.0F, StatisticsUtils.toMs(1000000L), 0.001F);
        Assert.assertEquals(1.0F, StatisticsUtils.toSecond(1000000000L), 0.001F);
        Assert.assertEquals(1.0F, StatisticsUtils.toMs(1000000.0F), 0.001F);
        Assert.assertEquals(1.0F, StatisticsUtils.toSecond(1000000000.0F), 0.001F);
    }

}
