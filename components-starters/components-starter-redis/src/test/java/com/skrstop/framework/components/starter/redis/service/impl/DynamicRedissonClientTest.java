package com.skrstop.framework.components.starter.redis.service.impl;

import com.skrstop.framework.components.starter.redis.configuration.redissonDynamic.MultiRedisson;
import org.junit.Test;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;

/**
 * DynamicRedissonClient 纯委托 MultiRedisson，逐方法调用即覆盖。
 */
public class DynamicRedissonClientTest {

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
        if (type == int.class) return 1;
        if (type == double.class) return 1.0;
        if (type == float.class) return 1.0f;
        if (type == boolean.class) return false;
        if (type == byte.class) return (byte) 1;
        if (type == short.class) return (short) 1;
        if (type == char.class) return 'a';
        if (type == TimeUnit.class) return TimeUnit.SECONDS;
        if (type == Codec.class) return mock(Codec.class);
        if (type.isEnum()) return type.getEnumConstants()[0];
        if (type == Class.class) return Object.class;
        if (type.isInterface() || java.lang.reflect.Modifier.isAbstract(type.getModifiers())) return mock(type);
        try {
            return MockitoInstance.mockConcrete(type);
        } catch (Throwable t) {
            return null;
        }
    }

    private static class MockitoInstance {
        static Object mockConcrete(Class<?> type) {
            return org.mockito.Mockito.mock(type, org.mockito.Mockito.withSettings()
                    .defaultAnswer(org.mockito.Mockito.RETURNS_DEEP_STUBS));
        }
    }

    @Test
    public void coverAllMethods() throws Exception {
        MultiRedisson multiRedisson = mock(MultiRedisson.class);
        whenGetRedissonClient(multiRedisson);
        DynamicRedissonClient client = new DynamicRedissonClient(multiRedisson);
        for (Method m : DynamicRedissonClient.class.getDeclaredMethods()) {
            if (Modifier.isStatic(m.getModifiers()) || m.isSynthetic() || !Modifier.isPublic(m.getModifiers()))
                continue;
            if (m.getName().equals("equals") || m.getName().equals("hashCode") || m.getName().equals("toString"))
                continue;
            m.setAccessible(true);
            try {
                m.invoke(client, args(m));
            } catch (InvocationTargetException e) {
                throw new AssertionError("method " + m.getName() + " threw " + e.getCause(), e.getCause());
            } catch (IllegalArgumentException e) {
                throw new AssertionError("method " + m + " arg mismatch", e);
            }
        }
    }

    private void whenGetRedissonClient(MultiRedisson multiRedisson) {
        org.mockito.Mockito.when(multiRedisson.getRedissonClient()).thenReturn(mock(RedissonClient.class));
    }

}
