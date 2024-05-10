package com.skrstop.framework.components.starter.annotation.handle.function;

import cn.hutool.core.util.ReflectUtil;
import com.skrstop.framework.components.starter.annotation.anno.function.AccessLimit;
import com.skrstop.framework.components.starter.annotation.configuration.AnnotationProperties;
import com.skrstop.framework.components.starter.annotation.exception.aspect.AccessLimitException;
import com.skrstop.framework.components.starter.annotation.handle.function.accessLimit.AccessLimitRule;
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
public class AccessLimitAnnotationInterceptor implements MethodInterceptor {

    private final AnnotationProperties annotationProperties;

    private final Map<Object, AccessLimitRule> ruleMap;

    public AccessLimitAnnotationInterceptor(AnnotationProperties annotationProperties) {
        this.annotationProperties = annotationProperties;
        ruleMap = new ConcurrentHashMap<>();
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        AccessLimit accessLimit = AnnoFindUtil.find(invocation.getMethod(), invocation.getThis(), AccessLimit.class, this.annotationProperties.isAllowedPublicOnly());
        if (ObjectUtil.isNull(accessLimit)) {
            return invocation.proceed();
        }
        AccessLimitRule rule = null;
        if (StrUtil.isNotBlank(accessLimit.beanName())) {
            // beanName
            rule = ruleMap.get(accessLimit.beanName());
            if (ObjectUtil.isNull(rule)) {
                try {
                    Object bean = SpringUtil.getBean(accessLimit.beanName());
                    if (!(bean instanceof AccessLimitRule)) {
                        throw new RuntimeException("beanName: " + accessLimit.beanName() + "未实现限流规则接口AccessLimitRule");
                    }
                    rule = (AccessLimitRule) bean;
                    if (ObjectUtil.isNull(rule)) {
                        throw new RuntimeException("未找到限流规则, beanName: " + accessLimit.beanName());
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException("未找到限流规则, beanName: " + accessLimit.beanName());
                }
                ruleMap.put(accessLimit.beanName(), rule);
            }
        } else if (ObjectUtil.isNotNull(accessLimit.beanClass())
                && accessLimit.beanClass() != void.class
                && accessLimit.beanClass() != Void.class) {
            // beanClass
            rule = ruleMap.get(accessLimit.beanClass());
            if (ObjectUtil.isNull(rule)) {
                if (!AccessLimitRule.class.isAssignableFrom(accessLimit.beanClass())) {
                    throw new RuntimeException("beanClass: " + accessLimit.beanClass().getName() + "未实现限流规则接口AccessLimitRule");
                }
                try {
                    rule = (AccessLimitRule) SpringUtil.getBean(accessLimit.beanClass());
                    if (ObjectUtil.isNull(rule)) {
                        throw new RuntimeException("未找到限流规则, beanClass: " + accessLimit.beanClass().getName());
                    }
                } catch (NoUniqueBeanDefinitionException e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException("找到多个限流规则, beanClass: " + accessLimit.beanClass().getName());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException("未找到限流规则, beanClass: " + accessLimit.beanClass().getName());
                }
                ruleMap.put(accessLimit.beanClass(), rule);
            }
        } else if (ObjectUtil.isNotNull(accessLimit.defaultRule())) {
            // defaultRule
            rule = ruleMap.get(accessLimit.defaultRule());
            if (ObjectUtil.isNull(rule)) {
                rule = ReflectUtil.newInstance(accessLimit.defaultRule());
                ruleMap.put(accessLimit.defaultRule(), rule);
            }
        }
        if (ObjectUtil.isNull(rule)) {
            throw new RuntimeException("未设置任何限流规则");
        }
        if (rule.canContinue(invocation, accessLimit.limit())) {
            //执行方法
            return invocation.proceed();
        }
        //拒绝请求
        if (StrUtil.isNotBlank(accessLimit.message())) {
            throw new AccessLimitException(accessLimit.message());
        }
        throw new AccessLimitException();
    }

}