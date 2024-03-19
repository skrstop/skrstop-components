package com.skrstop.framework.components.starter.objectStorage.configuration.dynamic.aop;

import com.skrstop.framework.components.starter.common.dsSelector.DsSelector;
import com.skrstop.framework.components.starter.common.util.AnnoFindUtil;
import com.skrstop.framework.components.starter.objectStorage.annotation.DSObjectStorage;
import com.skrstop.framework.components.starter.objectStorage.configuration.dynamic.DynamicDatasourceContextHolder;
import com.skrstop.framework.components.starter.objectStorage.configuration.dynamic.DynamicObjectStorageProperties;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class DynamicAnnotationInterceptor implements MethodInterceptor {

    private static final String DYNAMIC_PREFIX = "#";

    private final DsSelector dsSelector;

    private DynamicObjectStorageProperties dynamicObjectStorageProperties;

    public DynamicAnnotationInterceptor(DynamicObjectStorageProperties dynamicObjectStorageProperties, DsSelector dsSelector) {
        this.dsSelector = dsSelector;
        this.dynamicObjectStorageProperties = dynamicObjectStorageProperties;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String dsKey = this.determineObjectStorageKey(invocation);
        DynamicDatasourceContextHolder.push(dsKey);
        try {
            return invocation.proceed();
        } finally {
            DynamicDatasourceContextHolder.poll();
        }
    }

    private String determineObjectStorageKey(MethodInvocation invocation) {
        DSObjectStorage dsRedis = AnnoFindUtil.find(invocation.getMethod(), invocation.getThis(), DSObjectStorage.class, this.dynamicObjectStorageProperties.getAop().isAllowedPublicOnly());
        if (ObjectUtil.isNull(dsRedis)) {
            return null;
        }
        String key = dsRedis.value();
        return key.startsWith(DYNAMIC_PREFIX) ? dsSelector.determineDataSource(invocation, key) : key;
    }
}