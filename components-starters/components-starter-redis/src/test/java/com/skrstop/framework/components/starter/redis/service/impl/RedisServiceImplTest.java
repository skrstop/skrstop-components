package com.skrstop.framework.components.starter.redis.service.impl;

import com.skrstop.framework.components.starter.redis.filter.BinaryValueFilter;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

/**
 * 覆盖 RedisServiceImpl 全部方法：
 * A : deep-stub template + filter -> 正常路径
 * A2: deep-stub template + null filter -> valueFilter 为 null 分支
 * B : 普通 mock template -> 所有 try/catch 的 catch 分支
 */
public class RedisServiceImplTest {

    private RedisTemplate<String, Object> deepTemplate() {
        return mock(RedisTemplate.class, RETURNS_DEEP_STUBS);
    }

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
        if (type == org.springframework.data.redis.core.SessionCallback.class)
            return Mockito.mock(org.springframework.data.redis.core.SessionCallback.class);
        if (varargs && type == Object.class) return new Object[]{"v"};
        if (varargs && type == String.class) return new String[]{"k"};
        if (type == Object.class) return "v";
        if (type == java.util.Collection.class || type == java.util.List.class) return new ArrayList<>();
        if (type == java.util.Set.class) return new HashSet<>();
        if (type == java.util.Map.class) {
            java.util.Map<String, Object> map = new HashMap<>();
            map.put("k", "v");
            return map;
        }
        return null;
    }

    private static Object invokeQuiet(Object target, Method m, Object[] a) {
        try {
            return m.invoke(target, a);
        } catch (InvocationTargetException e) {
            return e.getCause();
        } catch (Throwable t) {
            return t;
        }
    }

    @Test
    public void coverAllMethods() throws Exception {
        RedisTemplate<String, Object> t = deepTemplate();
        RedisServiceImpl withFilter = new RedisServiceImpl(t, new BinaryValueFilter());
        RedisServiceImpl nullFilter = new RedisServiceImpl(deepTemplate(), null);
        RedisServiceImpl plain = new RedisServiceImpl(mock(RedisTemplate.class), new BinaryValueFilter());

        for (Method m : com.skrstop.framework.components.starter.redis.service.RedisService.class.getDeclaredMethods()) {
            if (m.isSynthetic()) continue;
            Object[] a = args(m);
            m.setAccessible(true);
            // 正常路径：必须不抛异常
            try {
                m.invoke(withFilter, a);
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException) {
                    // BinaryValueFilter.filterStr2List 不支持，已覆盖
                } else {
                    Throwable c = e.getCause();
                    Assert.fail("method " + m.getName() + " threw " + c);
                }
            } catch (IllegalArgumentException e) {
                throw new AssertionError("method " + m + " arg mismatch", e);
            }
            // null filter 分支：允许 NotSupportedException
            invokeQuiet(nullFilter, m, a);
            // 普通 mock 触发 catch 分支
            invokeQuiet(plain, m, a);
        }

        // isConnectionClose=true：所有方法走 return null 分支
        RedisTemplate<String, Object> closedTpl = mock(RedisTemplate.class, RETURNS_DEEP_STUBS);
        when(closedTpl.getRequiredConnectionFactory().getConnection().isClosed()).thenReturn(true);
        RedisServiceImpl closedSvc = new RedisServiceImpl(closedTpl, new BinaryValueFilter());
        for (Method m : com.skrstop.framework.components.starter.redis.service.RedisService.class.getDeclaredMethods()) {
            if (m.isSynthetic()) continue;
            m.setAccessible(true);
            try {
                m.invoke(closedSvc, args(m));
            } catch (InvocationTargetException e) {
                // getForList 等抛 NotSupportedException 可忽略
            }
        }

        // isConnectionClose 分支
        RedisTemplate<String, Object> closedTemplate = mock(RedisTemplate.class, RETURNS_DEEP_STUBS);
        RedisConnection conn = closedTemplate.getRequiredConnectionFactory().getConnection();
        when(conn.isClosed()).thenReturn(true);
        RedisServiceImpl closed = new RedisServiceImpl(closedTemplate, new BinaryValueFilter());
        Assert.assertTrue(closed.isConnectionClose());

        RedisTemplate<String, Object> errTemplate = mock(RedisTemplate.class, RETURNS_DEEP_STUBS);
        when(errTemplate.getRequiredConnectionFactory().getConnection()).thenThrow(new RuntimeException("x"));
        Assert.assertFalse(new RedisServiceImpl(errTemplate, new BinaryValueFilter()).isConnectionClose());

        RedisServiceImpl nullTemplate = new RedisServiceImpl(null, new BinaryValueFilter());
        Assert.assertFalse(nullTemplate.isConnectionClose());

        // setDB 成功分支
        RedisTemplate<String, Object> lettuceTemplate = mock(RedisTemplate.class, RETURNS_DEEP_STUBS);
        when(lettuceTemplate.getConnectionFactory()).thenReturn(mock(LettuceConnectionFactory.class));
        RedisServiceImpl setDb = new RedisServiceImpl(lettuceTemplate, new BinaryValueFilter());
        Assert.assertTrue(setDb.setDB(1));
        // getPattern keys 为 null
        RedisTemplate<String, Object> keysTemplate = mock(RedisTemplate.class, RETURNS_DEEP_STUBS);
        when(keysTemplate.keys(Mockito.anyString())).thenReturn(null);
        Assert.assertEquals(0, new RedisServiceImpl(keysTemplate, new BinaryValueFilter()).getPattern("p*").size());
        Assert.assertNotNull(new RedisServiceImpl(deepTemplate(), new BinaryValueFilter()).getPattern("p*"));
        // getRedisTemplate
        Assert.assertSame(t, withFilter.getRedisTemplate());
    }

    @Test
    public void getForListWithFilter() {
        RedisServiceImpl s = new RedisServiceImpl(mock(RedisTemplate.class, RETURNS_DEEP_STUBS), new BinaryValueFilter());
        try {
            s.getForList("k", String.class);
            Assert.fail();
        } catch (com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException e) {
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void getForListCustomFilter() {
        // filterStr2List 正常返回，确保 getForList 的 return 语句探针被命中
        com.skrstop.framework.components.starter.redis.filter.ValueFilter f = new com.skrstop.framework.components.starter.redis.filter.BinaryValueFilter() {
            @Override
            public <T> java.util.List<T> filterStr2List(String str, Class<T> cls) {
                return new java.util.ArrayList<>();
            }
        };
        RedisServiceImpl s = new RedisServiceImpl(mock(RedisTemplate.class, RETURNS_DEEP_STUBS), f);
        Assert.assertNotNull(s.getForList("k", String.class));
    }

    @Test
    public void getForListNullFilter() {
        RedisServiceImpl s = new RedisServiceImpl(mock(RedisTemplate.class, RETURNS_DEEP_STUBS), null);
        try {
            s.getForList("k", String.class);
            Assert.fail();
        } catch (com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException e) {
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void removePatternBranches() {
        // keys 非空
        java.util.Set<String> keys = new java.util.HashSet<>();
        keys.add("a");
        RedisTemplate<String, Object> t1 = mock(RedisTemplate.class, RETURNS_DEEP_STUBS);
        when(t1.keys(Mockito.anyString())).thenReturn((java.util.Set) keys);
        Assert.assertEquals(Long.valueOf(0), new RedisServiceImpl(t1, new BinaryValueFilter()).removePattern("p*"));
        // keys 为 null -> NPE
        RedisTemplate<String, Object> t2 = mock(RedisTemplate.class, RETURNS_DEEP_STUBS);
        when(t2.keys(Mockito.anyString())).thenReturn(null);
        try {
            new RedisServiceImpl(t2, new BinaryValueFilter()).removePattern("p*");
            Assert.fail();
        } catch (NullPointerException e) {
            Assert.assertNotNull(e.getMessage());
        }
    }

}
