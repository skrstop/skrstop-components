package com.skrstop.framework.components.util.extra;

import com.skrstop.framework.components.util.extra.calculation.WeightSumUtil;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2023-11-30 13:40:20
 */
public class WeightSumUtilTest {

    @Test
    public void test1() {
        List<Integer> integers = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 17, 18, 19);
        String s = WeightSumUtil.sumWeight(integers);
        System.out.println(s);
        System.out.println(WeightSumUtil.testWeight(s, 1));
        System.out.println(WeightSumUtil.testWeight(s, 19));
    }

    @Test
    public void testSumWeightListEmpty() {
        Assert.assertEquals("0", WeightSumUtil.sumWeight(Collections.emptyList()));
    }

    @Test
    public void testSumWeightArray() {
        Assert.assertEquals("0", WeightSumUtil.sumWeight(new String[0]));
        String sum = WeightSumUtil.sumWeight(new String[]{"1", "3"});
        // 2^1 + 2^3 = 10
        Assert.assertEquals("10", sum);
    }

    @Test
    public void testWeightVariants() {
        BigInteger sum = new BigInteger("10");
        Assert.assertTrue(WeightSumUtil.testWeight(sum, 1));
        Assert.assertFalse(WeightSumUtil.testWeight(sum, 2));
        // String sum 变体
        Assert.assertTrue(WeightSumUtil.testWeight("10", 1));
        Assert.assertFalse(WeightSumUtil.testWeight("", 1));
        Assert.assertFalse(WeightSumUtil.testWeight((String) null, 1));
        // (String, String) 变体
        Assert.assertTrue(WeightSumUtil.testWeight("10", "3"));
        Assert.assertFalse(WeightSumUtil.testWeight("", "3"));
        // (BigInteger, String) 变体
        Assert.assertTrue(WeightSumUtil.testWeight(sum, "3"));
        Assert.assertFalse(WeightSumUtil.testWeight(sum, "2"));
    }

}
