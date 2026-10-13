package com.skrstop.framework.components.util.value.data;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author 蒋时华
 */
public class BigIntegerBitUtilTest {

    @Test
    public void testSumAndTestBit() {
        BigInteger sum = BigIntegerBitUtil.sumInteger(Arrays.asList(1, 3, 5));
        Assert.assertTrue(BigIntegerBitUtil.testBit(sum, 1));
        Assert.assertTrue(BigIntegerBitUtil.testBit(sum, 3));
        Assert.assertTrue(BigIntegerBitUtil.testBit(sum, 5));
        Assert.assertFalse(BigIntegerBitUtil.testBit(sum, 2));

        BigInteger sumByte = BigIntegerBitUtil.sumByte(Arrays.asList((byte) 2, (byte) 4));
        Assert.assertTrue(BigIntegerBitUtil.testBit(sumByte, (byte) 2));
        Assert.assertTrue(BigIntegerBitUtil.testBit(sumByte, (byte) 4));
        Assert.assertFalse(BigIntegerBitUtil.testBit(sumByte, (byte) 3));

        Assert.assertTrue(BigIntegerBitUtil.testBit(sum.toString(), (Integer) 1));
        // Byte 重载
        Assert.assertTrue(BigIntegerBitUtil.testBit(sum.toString(), (byte) 1));
        Assert.assertFalse(BigIntegerBitUtil.testBit(sum.toString(), (byte) 2));
        Assert.assertFalse(BigIntegerBitUtil.testBit(sum.toString(), (Integer) 2));
        Assert.assertTrue(BigIntegerBitUtil.testBit(sum, "1"));
        Assert.assertFalse(BigIntegerBitUtil.testBit(sum, "2"));
        Assert.assertTrue(BigIntegerBitUtil.testBit(sum.toString(), "1"));
        Assert.assertFalse(BigIntegerBitUtil.testBit(sum.toString(), "2"));

        // 空列表 → 0
        Assert.assertEquals(BigInteger.ZERO, BigIntegerBitUtil.sumInteger(Collections.emptyList()));
        Assert.assertEquals(BigInteger.ZERO, BigIntegerBitUtil.sumByte(Collections.emptyList()));
    }

    @Test
    public void testEmptySum() {
        Assert.assertFalse(BigIntegerBitUtil.testBit("", 1));
        Assert.assertFalse(BigIntegerBitUtil.testBit((String) null, 1));
        Assert.assertFalse(BigIntegerBitUtil.testBit("", (byte) 1));
        Assert.assertFalse(BigIntegerBitUtil.testBit((String) null, (byte) 1));
        Assert.assertFalse(BigIntegerBitUtil.testBit("", "1"));
        Assert.assertFalse(BigIntegerBitUtil.testBit((String) null, "1"));
    }

}
