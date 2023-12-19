package com.skrstop.framework.components.util.extra;

import com.skrstop.framework.components.util.extra.calculation.WeightSumUtil;
import org.junit.Test;

import java.util.Arrays;
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

}
