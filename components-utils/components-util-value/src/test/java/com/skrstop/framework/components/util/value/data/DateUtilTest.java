package com.skrstop.framework.components.util.value.data;

import cn.hutool.core.date.DateUnit;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 蒋时华
 */
public class DateUtilTest {

    private static final String FMT = "yyyy-MM-dd HH:mm:ss";

    @Test
    public void testNow() throws Exception {
        Timestamp ts = DateUtil.nowTimestamp();
        Thread.sleep(2);
        Assert.assertTrue(ts.getTime() <= System.currentTimeMillis());
        String now = DateUtil.now(FMT);
        Assert.assertEquals(19, now.length());
    }

    @Test
    public void testFormat() throws Exception {
        Timestamp ts = Timestamp.valueOf("2020-01-02 03:04:05");
        Assert.assertEquals("2020-01-02 03:04:05", DateUtil.format(ts, FMT));
        Assert.assertEquals("2020-01-02 03:04:05", DateUtil.format(ts.getTime(), FMT));
        Date date = DateUtil.format("2020-01-02 03:04:05", FMT);
        Assert.assertEquals(1577905445000L, date.getTime());
        Assert.assertNull(DateUtil.format((String) "", FMT));
        Assert.assertNull(DateUtil.format((String) null, FMT));
        LocalDateTime ldt = DateUtil.formatLocalDateTime("2020-01-02 03:04:05", FMT);
        Assert.assertEquals(2020, ldt.getYear());
    }

    @Test
    public void testCompareDate() {
        Assert.assertEquals(1, DateUtil.compareDate("2021-01-01 00:00:00", "2020-01-01 00:00:00"));
        Assert.assertEquals(-1, DateUtil.compareDate("2020-01-01 00:00:00", "2021-01-01 00:00:00"));
        Assert.assertEquals(0, DateUtil.compareDate("2020-01-01 00:00:00", "2020-01-01 00:00:00"));
        // 非法输入 → 0
        Assert.assertEquals(0, DateUtil.compareDate("bad", "2020-01-01 00:00:00"));
        // 自定义格式
        Assert.assertEquals(
                1, DateUtil.compareDate("20210102", "20210101", "yyyyMMdd"));
        // Date / LocalDateTime
        Date d1 = new Date(1000);
        Date d2 = new Date(2000);
        Assert.assertEquals(-1, DateUtil.compareDate(d1, d2));
        Assert.assertEquals(1, DateUtil.compareDate(d2, d1));
        Assert.assertEquals(0, DateUtil.compareDate(d1, new Date(1000)));
        LocalDateTime l1 = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime l2 = LocalDateTime.of(2021, 1, 1, 0, 0);
        Assert.assertEquals(-1, DateUtil.compareDate(l1, l2));
        Assert.assertEquals(1, DateUtil.compareDate(l2, l1));
        Assert.assertEquals(0, DateUtil.compareDate(l1, LocalDateTime.of(2020, 1, 1, 0, 0)));
    }

    @Test
    public void testBetween() {
        LocalDateTime begin = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, 1, 2, 1, 0);
        Assert.assertEquals(25, DateUtil.between(begin, end, DateUnit.HOUR));
        Assert.assertEquals(25, DateUtil.between(end, begin, DateUnit.HOUR));
        Assert.assertEquals(-25, DateUtil.between(end, begin, DateUnit.HOUR, false));
        Assert.assertEquals(1, DateUtil.between(begin, end, DateUnit.DAY));
    }

    @Test
    public void testPlusAndConvert() {
        Date base = new Date(0);
        Assert.assertNull(DateUtil.plusMillis(null, 10));
        Assert.assertNull(DateUtil.plusSeconds(null, 10));
        Assert.assertEquals(5000L, DateUtil.plusMillis(base, 5000).getTime());
        Assert.assertEquals(60000L, DateUtil.plusSeconds(base, 60).getTime());

        LocalDateTime ldt = LocalDateTime.of(2020, 1, 1, 0, 0);
        Long second = DateUtil.getTimeSecond(ldt);
        Long milli = DateUtil.getTimeMilli(ldt);
        Assert.assertEquals(milli.longValue(), second.longValue() * 1000);
        Assert.assertNull(DateUtil.getTimeSecond(null));
        Assert.assertNull(DateUtil.getTimeMilli(null));
        Assert.assertNull(DateUtil.toDate(null));
        Assert.assertEquals(milli.longValue(), DateUtil.toDate(ldt).getTime());
    }

    @Test
    public void testIsValidDefaultFormat() {
        Assert.assertTrue(DateUtil.isValidDefaultFormat("2020-01-02 03:04:05", FMT));
        Assert.assertFalse(DateUtil.isValidDefaultFormat("bad", FMT));
    }

}
