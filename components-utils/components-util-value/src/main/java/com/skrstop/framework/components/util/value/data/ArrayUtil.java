package com.skrstop.framework.components.util.value.data;

import cn.hutool.core.util.BooleanUtil;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Collection;

/**
 * ArrayUtil class
 *
 * @author 蒋时华
 * @date 2019/6/19
 */
@UtilityClass
public class ArrayUtil extends cn.hutool.core.util.ArrayUtil {

    /**
     * 转换为Integer数组<br>
     * 默认分隔符","
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static Integer[] toIntArray(String str) {
        return toIntArray(",", str);
    }

    /**
     * 转换为Integer数组<br>
     *
     * @param split 分割符
     * @param str   被转换的值
     * @return 结果
     */
    public static Integer[] toIntArray(String split, String str) {
        if (StrUtil.isEmpty(str)) {
            return new Integer[]{};
        }
        String[] arr = str.split(split);
        final Integer[] ints = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ints[i] = NumberUtil.toInt(arr[i], 0);
        }
        return ints;
    }

    /**
     * list 转 数组
     *
     * @param collection
     * @return
     */
    public static Integer[] toIntArray(Collection<Integer> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        return collection.toArray(new Integer[collection.size()]);
    }

    /**
     * 转换为String数组<br>
     * 默认分隔符","
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static String[] toStrArray(String str) {
        return toStrArray(",", str);
    }

    /**
     * 转换为String数组<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static String[] toStrArray(String split, String str) {
        if (StrUtil.isBlank(str)) {
            return new String[]{};
        }
        return str.split(split);
    }

    /**
     * list 转 数组
     *
     * @param collection
     * @return
     */
    public static String[] toStrArray(Collection<String> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }

    /**
     * 转换为Long数组<br>
     * 默认分隔符","
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static Long[] toLongArray(String str) {
        return toLongArray(",", str);
    }

    /**
     * 转换为Long数组<br>
     * 默认分隔符","
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static Long[] toLongArray(String split, String str) {
        if (StrUtil.isEmpty(str)) {
            return new Long[]{};
        }
        String[] arr = str.split(split);
        final Long[] ints = new Long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ints[i] = NumberUtil.toLong(arr[i]);
        }
        return ints;
    }

    /**
     * list 转 数组
     *
     * @param collection
     * @return
     */
    public static Long[] toLongArray(Collection<Long> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        return collection.toArray(new Long[collection.size()]);
    }

    /**
     * 转换为Double数组<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static Double[] toDoubleArray(String str) {
        return toDoubleArray(",", str);
    }

    /**
     * 转换为Double数组<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static Double[] toDoubleArray(String split, String str) {
        if (StrUtil.isEmpty(str)) {
            return new Double[]{};
        }
        String[] arr = str.split(split);
        final Double[] ints = new Double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ints[i] = NumberUtil.toDouble(arr[i]);
        }
        return ints;
    }

    /**
     * list 转 数组
     *
     * @param collection
     * @return
     */
    public static Double[] toDoubleArray(Collection<Double> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        return collection.toArray(new Double[collection.size()]);
    }

    /**
     * 转换为Float数组<br>
     * 默认分隔符","
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static Float[] toFloatArray(String str) {
        return toFloatArray(",", str);
    }

    /**
     * 转换为Float数组<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static Float[] toFloatArray(String split, String str) {
        if (StrUtil.isEmpty(str)) {
            return new Float[]{};
        }
        String[] arr = str.split(split);
        final Float[] ints = new Float[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ints[i] = NumberUtil.toFloat(arr[i]);
        }
        return ints;
    }

    /**
     * list 转 数组
     *
     * @param collection
     * @return
     */
    public static Float[] toFloatArray(Collection<Float> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        return collection.toArray(new Float[collection.size()]);
    }

    /**
     * 转换为Boolean数组<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static Boolean[] toBoolArray(String str) {
        return toBoolArray(",", str);
    }

    /**
     * 转换为Boolean数组<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static Boolean[] toBoolArray(String split, String str) {
        if (StrUtil.isEmpty(str)) {
            return new Boolean[]{};
        }
        String[] arr = str.split(split);
        final Boolean[] ints = new Boolean[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ints[i] = BooleanUtil.toBoolean(arr[i]);
        }
        return ints;
    }

    /**
     * list 转 数组
     *
     * @param collection
     * @return
     */
    public static Boolean[] toBoolArray(Collection<Boolean> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        return collection.toArray(new Boolean[collection.size()]);
    }

    /**
     * 转换为Boolean数组<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static Short[] toShortArray(String str) {
        return toShortArray(",", str);
    }

    /**
     * 转换为Boolean数组<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static Short[] toShortArray(String split, String str) {
        if (StrUtil.isEmpty(str)) {
            return new Short[]{};
        }
        String[] arr = str.split(split);
        final Short[] ints = new Short[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ints[i] = NumberUtil.toShort(arr[i]);
        }
        return ints;
    }

    /**
     * list 转 数组
     *
     * @param collection
     * @return
     */
    public static Short[] toShortArray(Collection<Short> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        return collection.toArray(new Short[collection.size()]);
    }

    /**
     * 数据中是否存在某个属性
     *
     * @param array   目标数组
     * @param element 查找对象
     * @return 结果
     */
    public static <T> boolean contains(T[] array, final T element) {
        if (array == null) {
            return false;
        }
        return Arrays.stream(array).anyMatch(x -> ObjectUtil.nullSafeEquals(x, element));
    }

}
