package com.zoe.framework.components.util.value.data;


/**
 * NumberUtil class
 *
 * @author 蒋时华
 * @date 2019/6/19
 */
public class NumberUtil extends cn.hutool.core.util.NumberUtil {

    /**
     * <p>Convert a <code>String</code> to an <code>byte</code>
     *
     * @param obj the string to convert, may be null
     * @return the int represented by the string, or <code>zero</code> if
     * conversion fails
     */
    public static Byte toByte(final Object obj) {
        return toByte(obj, null);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>byte</code>
     *
     * @param object       the string to convert, may be null
     * @param defaultValue the default value
     * @return the int represented by the string, or the default if conversion fails
     */
    public static Byte toByte(final Object object, final Byte defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        try {
            return Byte.parseByte(object.toString().trim());
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * <p>Convert a <code>String</code> to an <code>int</code>
     *
     * @param obj the string to convert, may be null
     * @return the int represented by the string, or <code>zero</code> if
     * conversion fails
     */
    public static Integer toInt(final Object obj) {
        return toInt(obj, null);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>int</code>
     *
     * @param object       the string to convert, may be null
     * @param defaultValue the default value
     * @return the int represented by the string, or the default if conversion fails
     */
    public static Integer toInt(final Object object, final Integer defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(object.toString().trim());
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * <p>Convert a <code>String</code> to a <code>long</code>
     *
     * @param obj the string to convert, may be null
     * @return the long represented by the string, or <code>0</code> if
     * conversion fails
     */
    public static Long toLong(final Object obj) {
        return toLong(obj, null);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>long</code>
     *
     * @param obj          the string to convert, may be null
     * @param defaultValue the default value
     * @return the long represented by the string, or the default if conversion fails
     */
    public static Long toLong(final Object obj, final Long defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(obj.toString().trim());
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Double</code>
     *
     * @param obj value
     * @return double value
     */
    public static Double toDouble(Object obj) {
        return toDouble(obj, null);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Double</code>
     *
     * @param value        value
     * @param defaultValue 默认值
     * @return double value
     */
    public static Double toDouble(Object value, Double defaultValue) {
        if (value != null) {
            return Double.valueOf(value.toString().trim());
        }
        return defaultValue;
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Double</code>
     *
     * @param value value
     * @return double value
     */
    public static Float toFloat(Object value) {
        return toFloat(value, null);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Double</code>
     *
     * @param value        value
     * @param defaultValue 默认值
     * @return double value
     */
    public static Float toFloat(Object value, Float defaultValue) {
        if (value != null) {
            return Float.valueOf(value.toString().trim());
        }
        return defaultValue;
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Short</code>
     *
     * @param value value
     * @return double value
     */
    public static Short toShort(Object value) {
        return toShort(value, null);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Short</code>
     *
     * @param value        value
     * @param defaultValue 默认值
     * @return double value
     */
    public static Short toShort(Object value, Short defaultValue) {
        if (value != null) {
            return Short.valueOf(value.toString().trim());
        }
        return defaultValue;
    }

    /**
     * All possible chars for representing a number as a String
     */
    private final static char[] DIGITS = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z'
    };

    /**
     * 将 long 转短字符串 为 62 进制
     *
     * @param i 数字
     * @return 短字符串
     */
    public static String to62String(long i) {
        int radix = DIGITS.length;
        char[] buf = new char[65];
        int charPos = 64;
        i = -i;
        while (i <= -radix) {
            buf[charPos--] = DIGITS[(int) (-(i % radix))];
            i = i / radix;
        }
        buf[charPos] = DIGITS[(int) (-i)];

        return new String(buf, charPos, (65 - charPos));
    }


}
