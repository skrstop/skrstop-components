package com.zoe.framework.components.starter.redis.configuration.dynamic.aop;

import com.zoe.framework.components.starter.redis.configuration.dynamic.DynamicConnectFactoryContextHolder;
import com.zoe.framework.components.starter.redis.configuration.dynamic.annotation.DSRedis;
import com.zoe.framework.components.starter.redis.configuration.dynamic.selector.DsSelector;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class DynamicAopSourceAnnotationInterceptor implements MethodInterceptor {

    private static final String DYNAMIC_PREFIX = "#";

    private final DsSelector dsSelector;

    private final MethodAopResolver methodAopResolver;

    public DynamicAopSourceAnnotationInterceptor(Boolean allowedPublicOnly, DsSelector dsSelector) {
        this.methodAopResolver = new MethodAopResolver(allowedPublicOnly);
        this.dsSelector = dsSelector;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String dsKey = this.determineConnectionFactoryKey(invocation);
        DynamicConnectFactoryContextHolder.push(dsKey);
        try {
            return invocation.proceed();
        } finally {
            DynamicConnectFactoryContextHolder.poll();
        }
    }

    private String determineConnectionFactoryKey(MethodInvocation invocation) {
        String key = this.methodAopResolver.findKey(invocation.getMethod(), invocation.getThis(), DSRedis.class);
        return key.startsWith(DYNAMIC_PREFIX) ? dsSelector.determineConnectionFactory(invocation, key) : key;
    }
}