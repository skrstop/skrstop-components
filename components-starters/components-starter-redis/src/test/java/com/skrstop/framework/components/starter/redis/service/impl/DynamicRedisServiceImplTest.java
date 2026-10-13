package com.skrstop.framework.components.starter.redis.service.impl;

import com.skrstop.framework.components.starter.redis.service.RedisService;
import org.junit.Test;
import org.springframework.data.redis.core.SessionCallback;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;

/**
 * DynamicRedisServiceImpl 纯委托 RedisService，逐方法调用即覆盖。
 */
public class DynamicRedisServiceImplTest {

    private static Object[] args(Method m) {
        Class<?>[] types = m.getParameterTypes();
        Object[] result = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            boolean lastVarargs = m.isVarArgs() && i == types.length - 1;
            if (lastVarargs) {
                Object single = defaultArg(types[i].getComponentType(), false);
                Object array = java.lang.reflect.Array.newInstance(types[i].getComponentType(), 1);
                java.lang.reflect.Array.set(array, 0, single);
                result[i] = array;
            } else {
                result[i] = defaultArg(types[i], false);
            }
        }
        return result;
    }

    private static Object defaultArg(Class<?> type, boolean varargs) {
        if (type == String.class) return "k";
        if (type == long.class) return 1L;
        if (type == Long.class) return 1L;
        if (type == int.class) return 1;
        if (type == Integer.class) return 1;
        if (type == double.class) return 1.0;
        if (type == Double.class) return 1.0;
        if (type == boolean.class) return false;
        if (type == Boolean.class) return Boolean.FALSE;
        if (type == Class.class) return String.class;
        if (type == Duration.class) return Duration.ZERO;
        if (type == TimeUnit.class) return TimeUnit.SECONDS;
        if (type == org.springframework.data.domain.Range.class)
            return org.springframework.data.domain.Range.closed("a", "b");
        if (type == org.springframework.data.redis.connection.Limit.class)
            return org.springframework.data.redis.connection.Limit.unlimited();
        if (type == org.springframework.data.redis.connection.zset.Aggregate.class)
            return org.springframework.data.redis.connection.zset.Aggregate.SUM;
        if (type == org.springframework.data.redis.connection.zset.Weights.class)
            return org.springframework.data.redis.connection.zset.Weights.fromSetCount(1);
        if (type == SessionCallback.class) return mock(SessionCallback.class);
        if (varargs && type == Object.class) return new Object[]{"v"};
        if (varargs && type == String.class) return new String[]{"k"};
        if (type == Object.class) return "v";
        if (type == java.util.Collection.class || type == java.util.List.class) return new ArrayList<>();
        if (type == java.util.Set.class) return new HashSet<>();
        if (type == java.util.Map.class) return new HashMap<>();
        return null;
    }

    @Test
    public void coverAllMethods() throws Exception {
        DynamicRedisServiceImpl service = new DynamicRedisServiceImpl(mock(RedisService.class));
        for (Method m : com.skrstop.framework.components.starter.redis.service.DynamicRedisService.class.getDeclaredMethods()) {
            if (m.isSynthetic()) continue;
            m.setAccessible(true);
            try {
                m.invoke(service, args(m));
            } catch (InvocationTargetException e) {
                throw new AssertionError("method " + m.getName() + " threw " + e.getCause(), e.getCause());
            } catch (IllegalArgumentException e) {
                throw new AssertionError("method " + m + " arg mismatch", e);
            }
        }
    }

}
