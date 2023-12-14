package com.zoe.framework.components.starter.common.proxy;

import lombok.NonNull;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class DynamicServiceAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    /**
     * the advice
     */
    private final Advice advice;
    /**
     * the pointcut
     */
    private final Pointcut pointcut;

    /**
     * the service
     */
    private final Class<?> service;

    /**
     * 构造方法
     *
     * @param advice  切面
     * @param service service
     */
    public DynamicServiceAdvisor(@NonNull MethodInterceptor advice,
                                 @NonNull Class<?> service) {
        this.advice = advice;
        this.service = service;
        this.pointcut = buildPointcut();
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    private Pointcut buildPointcut() {
        Pointcut pc = new ServiceMethodPoint(this.service);
        return new ComposablePointcut(pc);
    }

    private static class ServiceMethodPoint implements Pointcut {

        private final Class<?> serviceType;

        public ServiceMethodPoint(Class<?> serviceType) {
            this.serviceType = serviceType;
        }

        @Override
        public ClassFilter getClassFilter() {
            return clazz -> {
                if (serviceType.isAssignableFrom(clazz)) {
                    return true;
                }
                return false;
            };
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return MethodMatcher.TRUE;
        }
    }
}