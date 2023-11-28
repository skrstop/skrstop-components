package cn.auntec.framework.components.util.value.data;

import java.util.*;

/**
 * CollectionUtil class
 *
 * @author 蒋时华
 * @date 2019/6/19
 */
public class CollectionUtil extends cn.hutool.core.collection.CollectionUtil {

    /**
     * 两个list是否相等
     *
     * @param col1
     * @param col2
     * @return
     */
    public static boolean equals(Collection<?> col1, Collection<?> col2) {
        if (null == col1 && null == col2) {
            return true;
        }
        if (null != col1 && null != col2) {
            if (CollectionUtil.containsAll(col1, col2) && CollectionUtil.containsAll(col2, col1)) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 是否存在某个属性
     *
     * @param iterator
     * @param element
     * @return
     */
    public static boolean contains(Iterator<?> iterator, Object element) {
        if (iterator != null) {
            while (iterator.hasNext()) {
                Object candidate = iterator.next();
                if (ObjectUtil.nullSafeEquals(candidate, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否存在某个属性
     *
     * @param enumeration
     * @param element
     * @return
     */
    public static boolean contains(Enumeration<?> enumeration, Object element) {
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                Object candidate = enumeration.nextElement();
                if (ObjectUtil.nullSafeEquals(candidate, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 转换为String集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<String> toStrList(String str) {
        return Arrays.asList(ArrayUtil.toStrArray(str));
    }

    /**
     * 转换为String集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static List<String> toStrList(String split, String str) {
        return Arrays.asList(ArrayUtil.toStrArray(split, str));
    }

    /**
     * 转换为Integer集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<Integer> toIntList(String str) {
        return Arrays.asList(ArrayUtil.toIntArray(str));
    }

    /**
     * 转换为Integer集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static List<Integer> toIntList(String split, String str) {
        return Arrays.asList(ArrayUtil.toIntArray(split, str));
    }

    /**
     * 转换为Long集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<Long> toLongList(String str) {
        return Arrays.asList(ArrayUtil.toLongArray(str));
    }

    /**
     * 转换为Long集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static List<Long> toLongList(String split, String str) {
        return Arrays.asList(ArrayUtil.toLongArray(split, str));
    }

    /**
     * 转换为double集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<Double> toDoubleList(String str) {
        return Arrays.asList(ArrayUtil.toDoubleArray(str));
    }

    /**
     * 转换为Long集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static List<Double> toDoubleList(String split, String str) {
        return Arrays.asList(ArrayUtil.toDoubleArray(split, str));
    }

    /**
     * 转换为float集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<Float> toFloatList(String str) {
        return Arrays.asList(ArrayUtil.toFloatArray(str));
    }

    /**
     * 转换为Long集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static List<Float> toFloatList(String split, String str) {
        return Arrays.asList(ArrayUtil.toFloatArray(split, str));
    }

    /**
     * 转换为Short集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<Short> toShortList(String str) {
        return Arrays.asList(ArrayUtil.toShortArray(str));
    }

    /**
     * 转换为Short集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static List<Short> toShortList(String split, String str) {
        return Arrays.asList(ArrayUtil.toShortArray(split, str));
    }

    /**
     * 转换为Bool集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<Boolean> toBoolList(String str) {
        return Arrays.asList(ArrayUtil.toBoolArray(str));
    }

    /**
     * 转换为Bool集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static List<Boolean> toBoolList(String split, String str) {
        return Arrays.asList(ArrayUtil.toBoolArray(split, str));
    }

    /**
     * 列表和并
     *
     * @param target
     * @param sources
     * @param <T>
     * @return
     */
    public static <T> Collection<T> addAll(Collection<T> target, Collection<T>... sources) {
        if (ObjectUtil.isNull(target)) {
            target = CollectionUtil.newArrayList();
        }
        if (ArrayUtil.isEmpty(sources)) {
            return target;
        }
        for (Collection<T> source : sources) {
            if (CollectionUtil.isNotEmpty(source)) {
                target.addAll(source);
            }
        }
        return target;
    }
}
