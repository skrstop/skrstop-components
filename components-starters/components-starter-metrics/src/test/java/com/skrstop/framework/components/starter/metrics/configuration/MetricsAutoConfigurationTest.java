package com.skrstop.framework.components.starter.metrics.configuration;

import com.skrstop.framework.components.starter.metrics.constant.GlobalConfigConst;
import org.junit.Assert;
import org.junit.Test;

/**
 * MetricsAutoConfiguration 单元测试
 */
public class MetricsAutoConfigurationTest {

    @Test
    public void testBeans() {
        MetricsAutoConfiguration configuration = new MetricsAutoConfiguration();
        Assert.assertNotNull(configuration.processMemoryMetrics());
        Assert.assertNotNull(configuration.processThreadMetrics());
    }

    @Test
    public void testProperties() {
        MetricsProperties properties = new MetricsProperties();
        Assert.assertTrue(properties.isEnabled());
        properties.setEnabled(false);
        Assert.assertFalse(properties.isEnabled());
        Assert.assertEquals("skrstop.metrics", GlobalConfigConst.METRICS_PREFIX);
    }
}
