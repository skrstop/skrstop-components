package com.skrstop.framework.components.util.value.data;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class NumberUtilTest {

    @Test
    public void testToByte() {
        Assert.assertEquals(Byte.valueOf((byte) 1), NumberUtil.toByte((Object) 1));
        Assert.assertEquals(Byte.valueOf((byte) 1), NumberUtil.toByte("1"));
        Assert.assertNull(NumberUtil.toByte((Object) null));
        Assert.assertNull(NumberUtil.toByte("abc"));
        Assert.assertEquals(Byte.valueOf((byte) 5), NumberUtil.toByte(null, (byte) 5));
        Assert.assertEquals(Byte.valueOf((byte) 5), NumberUtil.toByte("abc", (byte) 5));
        Assert.assertEquals(Byte.valueOf((byte) 2), NumberUtil.toByte("2", (byte) 5));
    }

    @Test
    public void testToInt() {
        Assert.assertEquals(Integer.valueOf(1), NumberUtil.toInt((Object) 1));
        Assert.assertEquals(Integer.valueOf(1), NumberUtil.toInt("1"));
        Assert.assertNull(NumberUtil.toInt((Object) null));
        Assert.assertNull(NumberUtil.toInt("abc"));
        Assert.assertEquals(Integer.valueOf(5), NumberUtil.toInt((Object) null, 5));
        Assert.assertEquals(Integer.valueOf(5), NumberUtil.toInt("abc", 5));
        Assert.assertEquals(Integer.valueOf(2), NumberUtil.toInt("2", 5));
    }

    @Test
    public void testToLong() {
        Assert.assertEquals(Long.valueOf(1L), NumberUtil.toLong((Object) 1));
        Assert.assertEquals(Long.valueOf(1L), NumberUtil.toLong("1"));
        Assert.assertNull(NumberUtil.toLong((Object) null));
        Assert.assertNull(NumberUtil.toLong("abc"));
        Assert.assertEquals(Long.valueOf(5L), NumberUtil.toLong((Object) null, 5L));
        Assert.assertEquals(Long.valueOf(5L), NumberUtil.toLong("abc", 5L));
        Assert.assertEquals(Long.valueOf(2L), NumberUtil.toLong("2", 5L));
    }

    @Test
    public void testToDouble() {
        Assert.assertEquals(Double.valueOf(1.5), (Object) NumberUtil.toDouble((Object) 1.5));
        Assert.assertEquals(Double.valueOf(1.5), (Object) NumberUtil.toDouble("1.5"));
        Assert.assertNull(NumberUtil.toDouble((Object) null));
        try {
            NumberUtil.toDouble("abc");
            Assert.fail();
        } catch (NumberFormatException ignored) {
        }
        Assert.assertEquals(Double.valueOf(5.0), (Object) NumberUtil.toDouble((Object) null, 5.0));
        Assert.assertEquals(Double.valueOf(2.0), (Object) NumberUtil.toDouble("2", 5.0));
        try {
            NumberUtil.toDouble("abc", 5.0);
            Assert.fail();
        } catch (NumberFormatException ignored) {
        }
    }

    @Test
    public void testToFloat() {
        Assert.assertEquals(Float.valueOf(1.5f), NumberUtil.toFloat((Object) 1.5f));
        Assert.assertEquals(Float.valueOf(1.5f), NumberUtil.toFloat("1.5"));
        Assert.assertNull(NumberUtil.toFloat((Object) null));
        try {
            NumberUtil.toDouble("abc");
            Assert.fail();
        } catch (NumberFormatException ignored) {
        }
        Assert.assertEquals(Float.valueOf(5.0f), (Object) NumberUtil.toFloat((Object) null, 5.0f));
        Assert.assertEquals(Float.valueOf(2.0f), (Object) NumberUtil.toFloat("2", 5.0f));
        try {
            NumberUtil.toFloat("abc", 5.0f);
            Assert.fail();
        } catch (NumberFormatException ignored) {
        }
    }

    @Test
    public void testToShort() {
        Assert.assertEquals(Short.valueOf((short) 1), NumberUtil.toShort((Object) 1));
        Assert.assertEquals(Short.valueOf((short) 1), NumberUtil.toShort("1"));
        Assert.assertNull(NumberUtil.toShort((Object) null));
        try {
            NumberUtil.toDouble("abc");
            Assert.fail();
        } catch (NumberFormatException ignored) {
        }
        Assert.assertEquals(Short.valueOf((short) 5), (Object) NumberUtil.toShort((Object) null, (short) 5));
        Assert.assertEquals(Short.valueOf((short) 2), (Object) NumberUtil.toShort("2", (short) 5));
        try {
            NumberUtil.toShort("abc", (short) 5);
            Assert.fail();
        } catch (NumberFormatException ignored) {
        }
    }

    @Test
    public void testTo62String() {
        Assert.assertEquals("0", NumberUtil.to62String(0));
        Assert.assertEquals("1", NumberUtil.to62String(1));
        Assert.assertEquals("10", NumberUtil.to62String(62));
        Assert.assertEquals("Z", NumberUtil.to62String(61));
        Assert.assertEquals("w7e", NumberUtil.to62String(123456L));
        // 往返
        long v = 987654321987L;
        String s = NumberUtil.to62String(v);
        long back = 0;
        for (char c : s.toCharArray()) {
            int d = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c);
            back = back * 62 + d;
        }
        Assert.assertEquals(v, back);
    }

}
