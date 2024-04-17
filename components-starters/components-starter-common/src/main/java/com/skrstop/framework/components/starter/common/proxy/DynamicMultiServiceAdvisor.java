package com.skrstop.framework.components.starter.common.proxy;

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

import java.util.Collection;

public class DynamicMultiServiceAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

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
    private final Collection<Class<?>> service;

    /**
     * 构造方法
     *
     * @param advice  切面
     * @param service service
     */
    public DynamicMultiServiceAdvisor(@NonNull MethodInterceptor advice,
                                      @NonNull Collection<Class<?>> service) {
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

        private final Collection<Class<?>> serviceType;

        public ServiceMethodPoint(Collection<Class<?>> serviceType) {
            this.serviceType = serviceType;
        }

        @Override
        public ClassFilter getClassFilter() {
            return clazz -> {
                for (Class<?> aClass : serviceType) {
                    if (aClass.isAssignableFrom(clazz)) {
                        return true;
                    }
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