package com.zoe.framework.components.starter.redis.configuration.redissonDynamic.aop;

import com.zoe.framework.components.starter.common.util.AnnoFindUtil;
import com.zoe.framework.components.starter.redis.annotation.DSRedisson;
import com.zoe.framework.components.starter.redis.configuration.redissonDynamic.DynamicRedissonContextHolder;
import com.zoe.framework.components.starter.redis.configuration.redissonDynamic.DynamicRedissonProperties;
import com.zoe.framework.components.starter.redis.selector.DsSelector;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class DynamicAnnotationInterceptor implements MethodInterceptor {

    private static final String DYNAMIC_PREFIX = "#";

    private final DsSelector dsSelector;

    private DynamicRedissonProperties dynamicRedissonProperties;

    public DynamicAnnotationInterceptor(DynamicRedissonProperties dynamicRedissonProperties, DsSelector dsSelector) {
        this.dsSelector = dsSelector;
        this.dynamicRedissonProperties = dynamicRedissonProperties;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String dsKey = this.determineConnectionFactoryKey(invocation);
        DynamicRedissonContextHolder.push(dsKey);
        try {
            return invocation.proceed();
        } finally {
            DynamicRedissonContextHolder.poll();
        }
    }

    private String determineConnectionFactoryKey(MethodInvocation invocation) {
        DSRedisson dsRedisson = AnnoFindUtil.find(invocation.getMethod(), invocation.getThis(), DSRedisson.class, this.dynamicRedissonProperties.getAop().isAllowedPublicOnly());
        if (ObjectUtil.isNull(dsRedisson)) {
            return null;
        }
        String key = dsRedisson.value();
        return key.startsWith(DYNAMIC_PREFIX) ? dsSelector.determineDataSource(invocation, key) : key;
    }
}