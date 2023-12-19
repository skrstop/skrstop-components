package com.skrstop.framework.components.util.value.data;


import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @description: 对象验证工具类
 * @author: 蒋时华
 * @date: 2018/10/16
 */
@UtilityClass
public class ObjectUtil extends cn.hutool.core.util.ObjectUtil {

    private static final int INITIAL_HASH = 7;
    private static final int MULTIPLIER = 31;

    private static final String EMPTY_STRING = "";
    private static final String NULL_STRING = "null";
    private static final String ARRAY_START = "{";
    private static final String ARRAY_END = "}";
    private static final String EMPTY_ARRAY = ARRAY_START + ARRAY_END;
    private static final String ARRAY_ELEMENT_SEPARATOR = ", ";

    /**
     * 比较两个对象是否相等。<br>
     * 相同的条件有两个，满足其一即可：<br>
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 是否相等
     */
    public static boolean equals(Object obj1, Object obj2) {
        return Objects.equals(obj1, obj2);
    }

    /**
     * Return a String representation of the specified Object.
     * <p>Builds a String representation of the contents in case of an array.
     * Returns a {@code "null"} String if {@code obj} is {@code null}.
     *
     * @param obj the object to build a String representation for
     * @return a String representation of {@code obj}
     */
    public static String nullSafeToString(Object obj) {
        if (obj == null) {
            return NULL_STRING;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof Object[]) {
            return nullSafeToString((Object[]) obj);
        }
        if (obj instanceof boolean[]) {
            return nullSafeToString((boolean[]) obj);
        }
        if (obj instanceof byte[]) {
            return nullSafeToString((byte[]) obj);
        }
        if (obj instanceof char[]) {
            return nullSafeToString((char[]) obj);
        }
        if (obj instanceof double[]) {
            return nullSafeToString((double[]) obj);
        }
        if (obj instanceof float[]) {
            return nullSafeToString((float[]) obj);
        }
        if (obj instanceof int[]) {
            return nullSafeToString((int[]) obj);
        }
        if (obj instanceof long[]) {
            return nullSafeToString((long[]) obj);
        }
        if (obj instanceof short[]) {
            return nullSafeToString((short[]) obj);
        }
        String str = obj.toString();
        return (str != null ? str : EMPTY_STRING);
    }

    /**
     * 对象是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof Optional) {
            return !((Optional) obj).isPresent();
        } else if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        } else {
            return obj instanceof Map && ((Map) obj).isEmpty();
        }
    }

    /**
     * 对象是否不为空
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 字符串批量判空
     *
     * @param strs
     * @return
     */
    public static boolean isEmptyBatch(String... strs) {
        for (String str : strs) {
            if (StrUtil.isBlank(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否是基本类型或者包装类
     *
     * @param object
     * @return
     */
    public static boolean isBaseType(Object object) {
        if (object != null && (ClassUtil.isBasicType(object.getClass()) || object instanceof String)) {
            return true;
        }
        return false;
    }

    /**
     * 是否不是基本类型或者包装类
     *
     * @param object
     * @return
     */
    public static boolean isNotBaseType(Object object) {
        return !isBaseType(object);
    }

    public static boolean nullSafeEquals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        } else if (o1 != null && o2 != null) {
            if (o1.equals(o2)) {
                return true;
            } else {
                return (o1.getClass().isArray() && o2.getClass().isArray()) && arrayEquals(o1, o2);
            }
        } else {
            return false;
        }
    }

    /**
     * 将对象转换为对象数据
     *
     * @param source
     * @return
     */
    public static Object[] toObjectArray(Object source) {
        if (source instanceof Object[]) {
            return (Object[]) ((Object[]) source);
        } else if (source == null) {
            return new Object[0];
        } else if (!source.getClass().isArray()) {
            throw new IllegalArgumentException("Source is not an array: " + source);
        } else {
            int length = Array.getLength(source);
            if (length == 0) {
                return new Object[0];
            } else {
                Class<?> wrapperType = Array.get(source, 0).getClass();
                Object[] newArray = (Object[]) ((Object[]) Array.newInstance(wrapperType, length));

                for (int i = 0; i < length; ++i) {
                    newArray[i] = Array.get(source, i);
                }

                return newArray;
            }
        }
    }

    /**
     * 比较对象数组
     *
     * @param o1
     * @param o2
     * @return
     */
    private static boolean arrayEquals(Object o1, Object o2) {
        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            return Arrays.equals((Object[]) ((Object[]) o1), (Object[]) ((Object[]) o2));
        } else if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
            return Arrays.equals((boolean[]) ((boolean[]) o1), (boolean[]) ((boolean[]) o2));
        } else if (o1 instanceof byte[] && o2 instanceof byte[]) {
            return Arrays.equals((byte[]) ((byte[]) o1), (byte[]) ((byte[]) o2));
        } else if (o1 instanceof char[] && o2 instanceof char[]) {
            return Arrays.equals((char[]) ((char[]) o1), (char[]) ((char[]) o2));
        } else if (o1 instanceof double[] && o2 instanceof double[]) {
            return Arrays.equals((double[]) ((double[]) o1), (double[]) ((double[]) o2));
        } else if (o1 instanceof float[] && o2 instanceof float[]) {
            return Arrays.equals((float[]) ((float[]) o1), (float[]) ((float[]) o2));
        } else if (o1 instanceof int[] && o2 instanceof int[]) {
            return Arrays.equals((int[]) ((int[]) o1), (int[]) ((int[]) o2));
        } else if (o1 instanceof long[] && o2 instanceof long[]) {
            return Arrays.equals((long[]) ((long[]) o1), (long[]) ((long[]) o2));
        } else {
            return (o1 instanceof short[] && o2 instanceof short[]) && Arrays.equals((short[]) ((short[]) o1), (short[]) ((short[]) o2));
        }
    }

    public static <T> T[] of(T... values) {
        return values;
    }

    /**
     * Create a new empty array from the specified component type
     *
     * @param componentType the specified component type
     * @param <T>           the specified component type
     * @return new empty array
     */
    public static <T> T[] emptyArray(Class<T> componentType) {
        return (T[]) Array.newInstance(componentType, 0);
    }

}
