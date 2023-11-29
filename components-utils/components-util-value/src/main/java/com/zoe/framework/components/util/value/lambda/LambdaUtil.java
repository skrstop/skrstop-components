package com.zoe.framework.components.util.value.lambda;

import cn.hutool.core.lang.func.Func0;
import cn.hutool.core.lang.func.Func1;
import com.zoe.framework.components.util.value.data.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

/**
 * BeanUtil class
 *
 * @author 蒋时华
 * @date 2019/12/14
 */
@Slf4j
public class LambdaUtil extends cn.hutool.core.lang.func.LambdaUtil {

    /**
     * 转换方法引用为属性名
     *
     * @param fn
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> String convertToFieldName(Func1<T, ?> fn) {
        return getFieldName(fn);
    }

    /**
     * 转换方法引用为属性名(下划线)
     *
     * @param fn
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> String convertToFieldNameByUnderline(Func1<T, ?> fn) {
        String fieldName = getFieldName(fn);
        return StrUtil.humpToUnderline(fieldName);
    }

    /**
     * 转换方法引用为属性名(驼峰)
     *
     * @param fn
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> String convertToFieldNameByHump(Func1<T, ?> fn) {
        String fieldName = getFieldName(fn);
        return StrUtil.underlineToHump(fieldName);
    }

    /**
     * 转换方法引用为属性名
     *
     * @param fn
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> String convertToFieldName(Func0<T> fn) {
        return getFieldName(fn);
    }

    /**
     * 转换方法引用为属性名(下划线)
     *
     * @param fn
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> String convertToFieldNameByUnderline(Func0<T> fn) {
        String fieldName = getFieldName(fn);
        return StrUtil.humpToUnderline(fieldName);
    }

    /**
     * 转换方法引用为属性名(驼峰)
     *
     * @param fn
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> String convertToFieldNameByHump(Func0<T> fn) {
        String fieldName = getFieldName(fn);
        return StrUtil.underlineToHump(fieldName);
    }

}
