package com.zoe.framework.components.starter.redis.configuration.redissonDynamic.client;

import com.zoe.framework.components.starter.redis.configuration.redissonDynamic.DynamicRedissonContextHolder;
import com.zoe.framework.components.starter.redis.selector.DsSelector;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class DynamicClientInterceptor implements MethodInterceptor {

    private static final String DYNAMIC_PREFIX = "#";

    private final DsSelector dsSelector;

    public DynamicClientInterceptor(DsSelector dsSelector) {
        this.dsSelector = dsSelector;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String dsKey = this.determineClientKey(invocation);
        DynamicRedissonContextHolder.push(dsKey);
        try {
            return invocation.proceed();
        } finally {
            DynamicRedissonContextHolder.poll();
        }
    }

    private String determineClientKey(MethodInvocation invocation) {
        // 获取方法的第一个参数
        Object[] arguments = invocation.getArguments();
        if (ObjectUtil.isNull(arguments) || arguments.length <= 0) {
            return null;
        }
        String key = arguments[0].toString();
        return key.startsWith(DYNAMIC_PREFIX) ? dsSelector.determineDataSource(invocation, key) : key;
    }
}