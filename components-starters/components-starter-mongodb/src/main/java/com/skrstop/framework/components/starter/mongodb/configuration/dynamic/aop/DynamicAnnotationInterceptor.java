package com.skrstop.framework.components.starter.mongodb.configuration.dynamic.aop;

import com.skrstop.framework.components.starter.common.dsSelector.DsSelector;
import com.skrstop.framework.components.starter.common.util.AnnoFindUtil;
import com.skrstop.framework.components.starter.mongodb.annotation.DSMongo;
import com.skrstop.framework.components.starter.mongodb.configuration.dynamic.DynamicMongoAopProperties;
import com.skrstop.framework.components.starter.mongodb.configuration.dynamic.DynamicMongoContextHolder;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class DynamicAnnotationInterceptor implements MethodInterceptor {

    private static final String DYNAMIC_PREFIX = "#";

    private final DsSelector dsSelector;

    private DynamicMongoAopProperties dynamicMongoAopProperties;

    public DynamicAnnotationInterceptor(DynamicMongoAopProperties dynamicMongoAopProperties, DsSelector dsSelector) {
        this.dsSelector = dsSelector;
        this.dynamicMongoAopProperties = dynamicMongoAopProperties;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String dsKey = this.determineConnectionFactoryKey(invocation);
        DynamicMongoContextHolder.push(dsKey);
        try {
            return invocation.proceed();
        } finally {
            DynamicMongoContextHolder.poll();
        }
    }

    private String determineConnectionFactoryKey(MethodInvocation invocation) {
        DSMongo dsMongo = AnnoFindUtil.find(invocation.getMethod(), invocation.getThis(), DSMongo.class, this.dynamicMongoAopProperties.getAllowedPublicOnly());
        if (ObjectUtil.isNull(dsMongo)) {
            return null;
        }
        String key = dsMongo.value();
        return key.startsWith(DYNAMIC_PREFIX) ? dsSelector.determineDataSource(invocation, key) : key;
    }
}