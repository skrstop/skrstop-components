package cn.auntec.framework.components.util.extra.calculation;

import cn.hutool.core.util.StrUtil;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * @author 蒋时华
 * 权值计算帮助类
 */
public class WeightSumUtil {

    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 17, 18, 19);
        String s = WeightSumUtil.sumWeight(integers);
        System.out.println(s);
        System.out.println(WeightSumUtil.testWeight(s, 1));
        System.out.println(WeightSumUtil.testWeight(s, 19));
    }

    /**
     * 利用BigInteger对权限进行2的权的和计算
     *
     * @param data int型权限编码数组
     * @return 2的权的和
     */
    public static String sumWeight(List<Integer> data) {
        BigInteger num = new BigInteger("0");
        for (int i = 0; i < data.size(); i++) {
            num = num.setBit(data.get(i));
        }
        return num.toString();
    }

    /**
     * 利用BigInteger对权限进行2的权的和计算
     *
     * @param data String型权限编码数组
     * @return 2的权的和
     */
    public static String sumWeight(String[] data) {
        BigInteger num = new BigInteger("0");
        for (int i = 0; i < data.length; i++) {
            num = num.setBit(Integer.parseInt(data[i]));
        }
        return num.toString();
    }

    /**
     * 测试是否具有指定编码的权限
     *
     * @param sum
     * @param target
     * @return
     */
    public static boolean testWeight(BigInteger sum, int target) {
        return sum.testBit(target);
    }

    /**
     * 测试是否具有指定编码的权限
     *
     * @param sum
     * @param target
     * @return
     */
    public static boolean testWeight(String sum, int target) {
        if (StrUtil.isEmpty(sum)) {
            return false;
        }
        return testWeight(new BigInteger(sum), target);
    }

    /**
     * 测试是否具有指定编码的权限
     *
     * @param sum
     * @param target
     * @return
     */
    public static boolean testWeight(String sum, String target) {
        if (StrUtil.isEmpty(sum)) {
            return false;
        }
        return testWeight(new BigInteger(sum), target);
    }

    /**
     * 测试是否具有指定编码的权限
     *
     * @param sum
     * @param target
     * @return
     */
    public static boolean testWeight(BigInteger sum, String target) {
        return testWeight(sum, Integer.parseInt(target));
    }
}
