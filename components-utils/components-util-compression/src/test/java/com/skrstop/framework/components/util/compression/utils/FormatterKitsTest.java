package com.skrstop.framework.components.util.compression.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class FormatterKitsTest {

    @Test
    public void testFormatDouble() {
        Assert.assertEquals("", FormatterKits.formatDouble(null));
        Assert.assertEquals("1.5", FormatterKits.formatDouble(1.5));
        Assert.assertEquals("2", FormatterKits.formatDouble(2.0));
    }

    @Test
    public void testFormatPercent() {
        Assert.assertEquals("", FormatterKits.formatPercent(null));
        Assert.assertEquals("50%", FormatterKits.formatPercent(0.5));
    }

    @Test
    public void testFormatSizeInfo() {
        Assert.assertTrue(FormatterKits.formatSizeInfo(10).endsWith("Byte(s)"));
        Assert.assertTrue(FormatterKits.formatSizeInfo(2048).endsWith("KB(s)"));
        Assert.assertTrue(FormatterKits.formatSizeInfo(1024L * 1024 * 3).endsWith("MB(s)"));
        Assert.assertTrue(FormatterKits.formatSizeInfo(1024L * 1024 * 1024 * 2).endsWith("GB(s)"));
    }

    @Test
    public void testFormatTimeInfo() {
        Assert.assertTrue(FormatterKits.formatTimeInfo(500).contains("millisecond(s)"));
        Assert.assertTrue(FormatterKits.formatTimeInfo(1500).contains("second(s)"));
        Assert.assertTrue(FormatterKits.formatTimeInfo(65000).contains("minute(s)"));
        Assert.assertTrue(FormatterKits.formatTimeInfo(3665000).contains("hour(s)"));
        Assert.assertTrue(FormatterKits.formatTimeInfo(90000000L).contains("day(s)"));
        Assert.assertTrue(FormatterKits.formatTimeInfo(3L * 30 * 24 * 3600 * 1000).contains("month(s)"));
        Assert.assertTrue(FormatterKits.formatTimeInfo(400L * 30 * 24 * 3600 * 1000).contains("year(s)"));
    }

}
