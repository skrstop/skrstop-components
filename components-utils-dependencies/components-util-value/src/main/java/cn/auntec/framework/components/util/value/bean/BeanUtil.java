package cn.auntec.framework.components.util.value.bean;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BeanUtil class
 *
 * @author 蒋时华
 * @date 2019/6/19
 */
public class BeanUtil extends cn.hutool.core.bean.BeanUtil {

    /**
     * 创建对应的Class对象并复制Bean对象属性
     *
     * @param <T>    对象类型
     * @param source 源Bean对象
     * @param tClass 目标Class
     * @return 目标对象
     */
    public static <T> T copyProperties(Object source, Class<T> tClass, CopyOptions copyOptions) {
        T target = ReflectUtil.newInstance(tClass);
        copyProperties(source, target, copyOptions);
        return target;
    }

    /**
     * 对象拷贝方法
     *
     * @param source
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T, R> List<T> copyProperties(List<R> source, Class<T> clazz) {
        if (source == null || source.isEmpty()) {
            return new ArrayList<T>();
        }
        return source.stream().map(item -> BeanUtil.copyProperties(item, clazz)).collect(Collectors.toList());
    }

    /**
     * 对象拷贝方法
     *
     * @param source
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T, R> List<T> copyProperties(List<R> source, Class<T> clazz, CopyOptions copyOptions) {
        if (source == null || source.isEmpty()) {
            return new ArrayList<T>();
        }
        return source.stream().map(item -> BeanUtil.copyProperties(item, clazz, copyOptions)).collect(Collectors.toList());
    }

    /**
     * 判断对象是否是基础数据类型
     *
     * @param object
     * @return
     */
    public static boolean isBaseType(Object object) {
        return object != null && (ClassUtil.isBasicType(object.getClass()) || object instanceof String);
    }

}
