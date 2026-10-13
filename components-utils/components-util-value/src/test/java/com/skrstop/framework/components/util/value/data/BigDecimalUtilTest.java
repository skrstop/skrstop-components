package com.skrstop.framework.components.util.value.data;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author 蒋时华
 */
public class BigDecimalUtilTest {

    @Test
    public void testScaleHalfUp() {
        Assert.assertEquals("1.24", BigDecimalUtil.scaleHalfUp("1.235"));
        Assert.assertEquals("1.24", BigDecimalUtil.scaleHalfUp(new BigDecimal("1.235")));
        Assert.assertEquals("1.24", BigDecimalUtil.scaleHalfUp(2, new BigDecimal("1.235")));
        // DecimalFormat 固定两位输出
        Assert.assertEquals("1.20", BigDecimalUtil.scaleHalfUp(1, new BigDecimal("1.24")));
    }

    @Test
    public void testScaleFloorCeiling() {
        Assert.assertEquals("1.23", BigDecimalUtil.scaleFloor(2, new BigDecimal("1.235")));
        Assert.assertEquals("1.24", BigDecimalUtil.scaleCeiling(2, new BigDecimal("1.235")));
        Assert.assertEquals("1.24", BigDecimalUtil.scaleCeiling(2, new BigDecimal("1.234")));
    }

    @Test
    public void testMultiplyDivide() {
        Assert.assertEquals(Float.valueOf(6f), BigDecimalUtil.multiply("2", "3"));
        Assert.assertEquals(Float.valueOf(2f), BigDecimalUtil.divide("6", "3"));
        Assert.assertEquals("6.00", BigDecimalUtil.multiplyScaleHalfUp("2", "3"));
        Assert.assertEquals("2.00", BigDecimalUtil.divideScaleHalfUp("6", "3"));
        Assert.assertEquals(Integer.valueOf(600), BigDecimalUtil.toInteger("6", "100"));
    }

    @Test
    public void testRoundingModes() {
        // HALF_UP 与 floor/ceiling 的组合行为
        BigDecimal v = new BigDecimal("2.345");
        Assert.assertEquals("2.35", BigDecimalUtil.scaleHalfUp(2, v));
        Assert.assertEquals("2.34", BigDecimalUtil.scaleFloor(2, v));
        Assert.assertEquals("2.35", BigDecimalUtil.scaleCeiling(2, v));
        Assert.assertEquals(
                "2.35", BigDecimalUtil.scaleHalfUp(2, v.setScale(3, RoundingMode.HALF_UP)));
    }

}
