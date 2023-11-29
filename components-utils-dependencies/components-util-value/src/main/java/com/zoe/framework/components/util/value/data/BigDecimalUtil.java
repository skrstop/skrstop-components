package com.zoe.framework.components.util.value.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 高精度数据工具类
 *
 * @author 蒋时华
 * @date 2018/11/29
 */
public class BigDecimalUtil {

    /**
     * 四舍五入，不够补0
     *
     * @param value
     * @return
     */
    public static String scaleHalfUp(String value) {
        DecimalFormat format = new DecimalFormat("0.00");
        double doubleValue = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
        return format.format(doubleValue);
    }

    /**
     * 四舍五入，不够补0
     *
     * @param value
     * @return
     */
    public static String scaleHalfUp(BigDecimal value) {
        DecimalFormat format = new DecimalFormat("0.00");
        double doubleValue = value.setScale(2, RoundingMode.HALF_UP).doubleValue();
        return format.format(doubleValue);
    }

    /**
     * 四舍五入，保留scale位小数
     *
     * @param scale
     * @param value
     * @return
     */
    public static String scaleHalfUp(Integer scale, BigDecimal value) {
        DecimalFormat format = new DecimalFormat("0.00");
        double doubleValue = value.setScale(scale, RoundingMode.HALF_UP).doubleValue();
        return format.format(doubleValue);
    }

    /**
     * 去位，保留scale位小数
     *
     * @param scale
     * @param value
     * @return
     */
    public static String scaleFloor(Integer scale, BigDecimal value) {
        DecimalFormat format = new DecimalFormat("0.00");
        double doubleValue = value.setScale(scale, RoundingMode.FLOOR).doubleValue();
        return format.format(doubleValue);
    }

    /**
     * 进位，保留scale为小数
     *
     * @param scale
     * @param value
     * @return
     */
    public static String scaleCeiling(Integer scale, BigDecimal value) {
        DecimalFormat format = new DecimalFormat("0.00");
        double doubleValue = value.setScale(scale, RoundingMode.CEILING).doubleValue();
        return format.format(doubleValue);
    }

    /**
     * 乘法
     *
     * @param value1
     * @param value2
     * @return
     */
    public static Float multiply(String value1, String value2) {
        BigDecimal multiply = new BigDecimal(value1).multiply(new BigDecimal(value2));
        return multiply.floatValue();
    }

    public static Float divide(String value1, String value2) {
        BigDecimal multiply = new BigDecimal(value1).divide(new BigDecimal(value2), 10, RoundingMode.HALF_UP);
        return multiply.floatValue();
    }

    /**
     * 相乘后，四舍五入，保留两位
     *
     * @param value1
     * @param value2
     * @return
     */
    public static String multiplyScaleHalfUp(String value1, String value2) {
        Float multiply = BigDecimalUtil.multiply(value1, value2);
        return BigDecimalUtil.scaleHalfUp(new BigDecimal(multiply));
    }

    /**
     * 相除后，四舍五入，保留两位
     *
     * @param value1
     * @param value2
     * @return
     */
    public static String divideScaleHalfUp(String value1, String value2) {
        Float multiply = BigDecimalUtil.divide(value1, value2);
        return BigDecimalUtil.scaleHalfUp(new BigDecimal(multiply));
    }

    /**
     * 相除后，向下整数
     */
    public static Integer toInteger(String value, String multiplyValue) {
        return new BigDecimal(value).multiply(new BigDecimal(multiplyValue)).intValue();
    }


}


