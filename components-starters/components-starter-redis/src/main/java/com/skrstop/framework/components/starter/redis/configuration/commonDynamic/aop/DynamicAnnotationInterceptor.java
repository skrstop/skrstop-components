package com.skrstop.framework.components.starter.redis.configuration.commonDynamic.aop;

import com.skrstop.framework.components.starter.common.dsSelector.DsSelector;
import com.skrstop.framework.components.starter.common.util.AnnoFindUtil;
import com.skrstop.framework.components.starter.redis.annotation.DSRedis;
import com.skrstop.framework.components.starter.redis.configuration.commonDynamic.DynamicConnectFactoryContextHolder;
import com.skrstop.framework.components.starter.redis.configuration.commonDynamic.DynamicRedisProperties;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class DynamicAnnotationInterceptor implements MethodInterceptor {

    private static final String DYNAMIC_PREFIX = "#";

    private final DsSelector dsSelector;

    private DynamicRedisProperties dynamicRedisProperties;

    public DynamicAnnotationInterceptor(DynamicRedisProperties dynamicRedisProperties, DsSelector dsSelector) {
        this.dsSelector = dsSelector;
        this.dynamicRedisProperties = dynamicRedisProperties;
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
        DSRedis dsRedis = AnnoFindUtil.find(invocation.getMethod(), invocation.getThis(), DSRedis.class, this.dynamicRedisProperties.getAop().isAllowedPublicOnly());
        if (ObjectUtil.isNull(dsRedis)) {
            return null;
        }
        String key = dsRedis.value();
        return key.startsWith(DYNAMIC_PREFIX) ? dsSelector.determineDataSource(invocation, key) : key;
    }
}