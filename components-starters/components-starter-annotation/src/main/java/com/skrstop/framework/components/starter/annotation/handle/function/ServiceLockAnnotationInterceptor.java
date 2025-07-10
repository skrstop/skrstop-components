package com.skrstop.framework.components.starter.annotation.handle.function;

import cn.hutool.core.util.ReflectUtil;
import com.skrstop.framework.components.starter.annotation.anno.function.ServiceLock;
import com.skrstop.framework.components.starter.annotation.configuration.AnnotationProperties;
import com.skrstop.framework.components.starter.annotation.handle.function.accessLimit.AccessLimitRule;
import com.skrstop.framework.components.starter.annotation.handle.function.serviceLock.ServiceLockRule;
import com.skrstop.framework.components.starter.common.util.AnnoFindUtil;
import com.skrstop.framework.components.starter.spring.support.bean.SpringUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ServiceLockAnnotationInterceptor implements MethodInterceptor {

    private final AnnotationProperties annotationProperties;
    private final Map<Object, ServiceLockRule> ruleMap;

    public ServiceLockAnnotationInterceptor(AnnotationProperties annotationProperties) {
        this.annotationProperties = annotationProperties;
        ruleMap = new ConcurrentHashMap<>();
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        ServiceLock serviceLock = AnnoFindUtil.find(invocation.getMethod(), invocation.getThis(), ServiceLock.class, this.annotationProperties.isAllowedPublicOnly());
        if (ObjectUtil.isNull(serviceLock)) {
            return invocation.proceed();
        }
        ServiceLockRule rule = null;
        if (StrUtil.isNotBlank(serviceLock.beanName())) {
            // beanName
            rule = ruleMap.get(serviceLock.beanName());
            if (ObjectUtil.isNull(rule)) {
                try {
                    Object bean = SpringUtil.getBean(serviceLock.beanName());
                    if (!(bean instanceof AccessLimitRule)) {
                        throw new RuntimeException("beanName: " + serviceLock.beanName() + "未实现锁规则接口AccessLimitRule");
                    }
                    rule = (ServiceLockRule) bean;
                    if (ObjectUtil.isNull(rule)) {
                        throw new RuntimeException("未找到锁规则, beanName: " + serviceLock.beanName());
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException("未找到锁规则, beanName: " + serviceLock.beanName());
                }
                ruleMap.put(serviceLock.beanName(), rule);
            }
        } else if (ObjectUtil.isNotNull(serviceLock.beanClass())
                && serviceLock.beanClass() != void.class
                && serviceLock.beanClass() != Void.class) {
            // beanClass
            rule = ruleMap.get(serviceLock.beanClass());
            if (ObjectUtil.isNull(rule)) {
                if (!AccessLimitRule.class.isAssignableFrom(serviceLock.beanClass())) {
                    throw new RuntimeException("beanClass: " + serviceLock.beanClass().getName() + "未实现锁规则接口AccessLimitRule");
                }
                try {
                    rule = (ServiceLockRule) SpringUtil.getBean(serviceLock.beanClass());
                    if (ObjectUtil.isNull(rule)) {
                        throw new RuntimeException("未找到锁规则, beanClass: " + serviceLock.beanClass().getName());
                    }
                } catch (NoUniqueBeanDefinitionException e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException("找到多个锁规则, beanClass: " + serviceLock.beanClass().getName());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException("未找到锁规则, beanClass: " + serviceLock.beanClass().getName());
                }
                ruleMap.put(serviceLock.beanClass(), rule);
            }
        } else if (ObjectUtil.isNotNull(serviceLock.defaultRule())) {
            // defaultRule
            rule = ruleMap.get(serviceLock.defaultRule());
            if (ObjectUtil.isNull(rule)) {
                rule = ReflectUtil.newInstance(serviceLock.defaultRule());
                ruleMap.put(serviceLock.defaultRule(), rule);
            }
        }
        if (ObjectUtil.isNull(rule)) {
            throw new RuntimeException("未设置任何锁规则");
        }
        return rule.handle(invocation, serviceLock);
    }

}