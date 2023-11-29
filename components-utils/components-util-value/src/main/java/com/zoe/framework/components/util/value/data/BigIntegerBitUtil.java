package com.zoe.framework.components.util.value.data;

import com.zoe.framework.components.util.constant.StringPoolConst;

import java.math.BigInteger;
import java.util.List;

/**
 * @author 蒋时华
 * 权限计算帮助类
 */
public class BigIntegerBitUtil {

    /**
     * 利用BigInteger对权限进行2的权的和计算
     *
     * @param numbers int型权限编码数组
     * @return 2的权的和
     */
    public static BigInteger sumInteger(List<Integer> numbers) {
        BigInteger num = new BigInteger(StringPoolConst.ZERO);
        for (Integer number : numbers) {
            num = num.setBit(number);
        }
        return num;
    }

    /**
     * 利用BigInteger对权限进行2的权的和计算
     *
     * @param numbers String型权限编码数组
     * @return 2的权的和
     */
    public static BigInteger sumByte(List<Byte> numbers) {
        BigInteger num = new BigInteger(StringPoolConst.ZERO);
        for (Byte number : numbers) {
            num = num.setBit(number);
        }
        return num;
    }

    /**
     * 测试是否具有指定编码的权限
     *
     * @param sum
     * @param targetNumber
     * @return
     */
    public static boolean testBit(BigInteger sum, Integer targetNumber) {
        return sum.testBit(targetNumber);
    }

    /**
     * 测试是否具有指定编码的权限
     *
     * @param sum
     * @param targetNumber
     * @return
     */
    public static boolean testBit(BigInteger sum, Byte targetNumber) {
        return sum.testBit(targetNumber);
    }

    /**
     * 测试是否具有指定编码的权限
     *
     * @param sum
     * @param targetNumber
     * @return
     */
    public static boolean testBit(BigInteger sum, String targetNumber) {
        return sum.testBit(Integer.parseInt(targetNumber));
    }

    /**
     * 测试是否具有指定编码的权限
     *
     * @param sum
     * @param targetNumber
     * @return
     */
    public static boolean testBit(String sum, Integer targetNumber) {
        if (StrUtil.isEmpty(sum)) {
            return false;
        }
        return testBit(new BigInteger(sum), targetNumber);
    }

    /**
     * 测试是否具有指定编码的权限
     *
     * @param sum
     * @param targetNumber
     * @return
     */
    public static boolean testBit(String sum, Byte targetNumber) {
        if (StrUtil.isEmpty(sum)) {
            return false;
        }
        return testBit(new BigInteger(sum), targetNumber);
    }

    /**
     * 测试是否具有指定编码的权限
     *
     * @param sum
     * @param targetNumber
     * @return
     */
    public static boolean testBit(String sum, String targetNumber) {
        if (StrUtil.isEmpty(sum)) {
            return false;
        }
        return testBit(new BigInteger(sum), targetNumber);
    }
}
