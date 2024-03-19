package com.skrstop.framework.components.starter.objectStorage.configuration.dynamic.service;

import com.skrstop.framework.components.starter.objectStorage.configuration.dynamic.DynamicDatasourceContextHolder;
import com.skrstop.framework.components.starter.objectStorage.selector.DsSelector;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class DynamicServiceInterceptor implements MethodInterceptor {

    private static final String DYNAMIC_PREFIX = "#";

    private final DsSelector dsSelector;

    public DynamicServiceInterceptor(DsSelector dsSelector) {
        this.dsSelector = dsSelector;
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
        // 获取方法的第一个参数
        Object[] arguments = invocation.getArguments();
        if (ObjectUtil.isNull(arguments) || arguments.length <= 0) {
            return null;
        }
        String key = arguments[0].toString();
        return key.startsWith(DYNAMIC_PREFIX) ? dsSelector.determineDataSource(invocation, key) : key;
    }
}