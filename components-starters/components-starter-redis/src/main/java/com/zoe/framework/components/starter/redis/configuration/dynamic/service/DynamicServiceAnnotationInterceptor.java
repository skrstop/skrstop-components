package com.zoe.framework.components.starter.redis.configuration.dynamic.service;

import com.zoe.framework.components.starter.redis.configuration.dynamic.DynamicConnectFactoryContextHolder;
import com.zoe.framework.components.starter.redis.configuration.dynamic.selector.DsSelector;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class DynamicServiceAnnotationInterceptor implements MethodInterceptor {

    private static final String DYNAMIC_PREFIX = "#";

    private final DsSelector dsSelector;

    public DynamicServiceAnnotationInterceptor(DsSelector dsSelector) {
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
        // 获取方法的第一个参数
        Object[] arguments = invocation.getArguments();
        if (ObjectUtil.isNull(arguments) || arguments.length <= 0) {
            return null;
        }
        String key = arguments[0].toString();
        return key.startsWith(DYNAMIC_PREFIX) ? dsSelector.determineConnectionFactory(invocation, key) : key;
    }
}