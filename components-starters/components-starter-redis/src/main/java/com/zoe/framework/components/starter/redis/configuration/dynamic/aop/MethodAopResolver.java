package com.zoe.framework.components.starter.redis.configuration.dynamic.aop;

import com.zoe.framework.components.starter.redis.configuration.dynamic.annotation.DSRedis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MethodAopResolver {

    /**
     * 缓存方法对应的数据源
     */
    private final Map<Object, String> dsCache = new ConcurrentHashMap<>();
    private final boolean allowedPublicOnly;

    /**
     * 加入扩展, 给外部一个修改aop条件的机会
     *
     * @param allowedPublicOnly 只允许公共的方法, 默认为true
     */
    public MethodAopResolver(boolean allowedPublicOnly) {
        this.allowedPublicOnly = allowedPublicOnly;
    }

    /**
     * 从缓存获取数据
     *
     * @param method       方法
     * @param targetObject 目标对象
     * @return key
     */
    public String findKey(Method method, Object targetObject, Class<? extends Annotation> annotation) {
        if (method.getDeclaringClass() == Object.class) {
            return "";
        }
        Object cacheKey = new MethodClassKey(method, targetObject.getClass());
        String ds = this.dsCache.get(cacheKey);
        if (ds == null) {
            AnnoAttribute<String> dsOperation = this.getAnnoAttr(method, targetObject, annotation);
            if (dsOperation == null) {
                ds = "";
            } else {
                ds = dsOperation.getAttr();
            }
            this.dsCache.put(cacheKey, ds);
        }
        return ds;
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
    private <T> AnnoAttribute<T> getAnnoAttr(Method method, Object targetObject, Class<? extends Annotation> annotation) {
        if (allowedPublicOnly && !Modifier.isPublic(method.getModifiers())) {
            return null;
        }
        // 从当前方法接口中获取
        AnnoAttribute<T> annoAttr = findAnnoAttribute(method, annotation);
        if (annoAttr != null) {
            return annoAttr;
        }
        Class<?> targetClass = targetObject.getClass();
        Class<?> userClass = ClassUtils.getUserClass(targetClass);
        // JDK代理时,  获取实现类的方法声明.  method: 接口的方法, specificMethod: 实现类方法
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);

        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        // 从实现类的方法找
        annoAttr = findAnnoAttribute(specificMethod, annotation);
        if (annoAttr != null) {
            return annoAttr;
        }
        // 从当前方法声明的类查找
        annoAttr = findAnnoAttribute(userClass, annotation);
        if (annoAttr != null && ClassUtils.isUserLevelMethod(method)) {
            return annoAttr;
        }
        // since 3.4.1 从接口查找，只取第一个找到的
        for (Class<?> interfaceClazz : ClassUtils.getAllInterfacesForClassAsSet(userClass)) {
            annoAttr = findAnnoAttribute(interfaceClazz, annotation);
            if (annoAttr != null) {
                return annoAttr;
            }
        }
        // 如果存在桥接方法
        if (specificMethod != method) {
            // 从桥接方法查找
            annoAttr = findAnnoAttribute(method, annotation);
            if (annoAttr != null) {
                return annoAttr;
            }
            // 从桥接方法声明的类查找
            annoAttr = findAnnoAttribute(method.getDeclaringClass(), annotation);
            if (annoAttr != null && ClassUtils.isUserLevelMethod(method)) {
                return annoAttr;
            }
        }
        return getDefaultAnnoAttr(targetObject, annotation);
    }

    /**
     * 默认的获取数据源名称方式
     *
     * @param targetObject 目标对象
     * @return ds
     */
    private <T> AnnoAttribute<T> getDefaultAnnoAttr(Object targetObject, Class<? extends Annotation> annotation) {
        Class<?> targetClass = targetObject.getClass();
        // 如果不是代理类, 从当前类开始, 不断的找父类的声明
        if (!Proxy.isProxyClass(targetClass)) {
            Class<?> currentClass = targetClass;
            while (currentClass != Object.class) {
                AnnoAttribute<T> datasourceAttr = findAnnoAttribute(currentClass, annotation);
                if (datasourceAttr != null) {
                    return datasourceAttr;
                }
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> AnnoAttribute<T> findAnnoAttribute(AnnotatedElement ae, Class<? extends Annotation> annotation) {
        if (annotation.isAssignableFrom(DSRedis.class)) {
            //AnnotatedElementUtils.findMergedAnnotation()会委托给findMergedAnnotationAttributes()
            DSRedis ds = AnnotatedElementUtils.findMergedAnnotation(ae, DSRedis.class);
            if (ds != null) {
                return new AnnoAttribute(ds.value());
            }
        }
        return null;
    }
}