package com.zoe.framework.components.starter.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
@Slf4j
public class AnnoFindUtil {

    /**
     * 缓存方法对应的数据源
     */
    private static final Map<MethodClassKey, Map<Class<Annotation>, Object>> ANNO_CACHE = new ConcurrentHashMap<>();

    private static final Object EMPTY = new Object();

    /**
     * 从缓存获取数据
     *
     * @param method       方法
     * @param targetObject 目标对象
     * @return key
     */
    public static Annotation find(Method method, Object targetObject, Class<Annotation> annotation, boolean allowedPublicOnly) {
        if (method.getDeclaringClass() == Object.class) {
            return null;
        }
        MethodClassKey cacheKey = new MethodClassKey(method, targetObject.getClass());
        Map<Class<Annotation>, Object> classAnnotationMap = ANNO_CACHE.computeIfAbsent(cacheKey, k -> new ConcurrentHashMap<>());
        // 读取缓存数据
        Object annoCache = classAnnotationMap.get(annotation);
        if (annoCache != null) {
            if (EMPTY.equals(annoCache)) {
                return null;
            }
            return (Annotation) annoCache;
        }
        Annotation anno = getAnno(method, targetObject, annotation, allowedPublicOnly);
        classAnnotationMap.put(annotation, anno == null ? EMPTY : anno);
        return anno;
    }

    /**
     * 从缓存获取数据
     *
     * @param method       方法
     * @param targetObject 目标对象
     * @return key
     */
    public static Annotation find(Method method, Object targetObject, Class<Annotation> annotation) {
        return find(method, targetObject, annotation, false);
    }

    /**
     * 查找注解的顺序
     * 1. 当前方法
     * 2. 桥接方法
     * 3. 当前类开始一直找到Object
     *
     * @param method       方法
     * @param targetObject 目标对象
     * @return ds
     */
    private static Annotation getAnno(Method method, Object targetObject, Class<? extends Annotation> annotation, boolean allowedPublicOnly) {
        if (allowedPublicOnly && !Modifier.isPublic(method.getModifiers())) {
            return null;
        }
        // 从当前方法接口中获取
        Annotation findAnno = AnnotatedElementUtils.findMergedAnnotation(method, annotation);
        if (findAnno != null) {
            return findAnno;
        }
        Class<?> targetClass = targetObject.getClass();
        Class<?> userClass = ClassUtils.getUserClass(targetClass);
        // JDK代理时,  获取实现类的方法声明.  method: 接口的方法, specificMethod: 实现类方法
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        // 从实现类的方法找
        findAnno = AnnotatedElementUtils.findMergedAnnotation(specificMethod, annotation);
        if (findAnno != null) {
            return findAnno;
        }
        // 从当前方法声明的类查找
        findAnno = AnnotatedElementUtils.findMergedAnnotation(userClass, annotation);
        if (findAnno != null && ClassUtils.isUserLevelMethod(method)) {
            return findAnno;
        }
        // since 3.4.1 从接口查找，只取第一个找到的
        for (Class<?> interfaceClazz : ClassUtils.getAllInterfacesForClassAsSet(userClass)) {
            findAnno = AnnotatedElementUtils.findMergedAnnotation(interfaceClazz, annotation);
            if (findAnno != null) {
                return findAnno;
            }
        }
        // 如果存在桥接方法
        if (specificMethod != method) {
            // 从桥接方法查找
            findAnno = AnnotatedElementUtils.findMergedAnnotation(method, annotation);
            if (findAnno != null) {
                return findAnno;
            }
            // 从桥接方法声明的类查找
            findAnno = AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), annotation);
            if (findAnno != null && ClassUtils.isUserLevelMethod(method)) {
                return findAnno;
            }
        }
        return getDefaultAnno(targetObject, annotation);
    }

    /**
     * 默认的获取数据源名称方式
     *
     * @param targetObject 目标对象
     * @return ds
     */
    private static Annotation getDefaultAnno(Object targetObject, Class<? extends Annotation> annotation) {
        Class<?> targetClass = targetObject.getClass();
        // 如果不是代理类, 从当前类开始, 不断的找父类的声明
        if (!Proxy.isProxyClass(targetClass)) {
            Class<?> currentClass = targetClass;
            while (currentClass != Object.class) {
                Annotation findAnno = AnnotatedElementUtils.findMergedAnnotation(currentClass, annotation);
                if (findAnno != null) {
                    return findAnno;
                }
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

}