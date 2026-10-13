package com.skrstop.framework.components.starter.redis.configuration.commonDynamic;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class DynamicRedisConnectionFactoryTest {

    @After
    public void tearDown() {
        DynamicConnectFactoryContextHolder.clear();
    }

    @SuppressWarnings("unchecked")
    private ObjectProvider provider() {
        ObjectProvider p = mock(ObjectProvider.class);
        when(p.getIfAvailable()).thenReturn(null);
        return p;
    }

    @SuppressWarnings("unchecked")
    private DynamicRedisConnectionFactory factory(RedisProperties redisProperties, boolean exceptionWhileNotFound) throws Exception {
        DynamicRedisProperties properties = new DynamicRedisProperties();
        properties.setPrimary("default");
        properties.setExceptionWhileNotFound(exceptionWhileNotFound);
        Map<String, RedisProperties> ds = new LinkedHashMap<>();
        ds.put("default", redisProperties);
        ds.put("other", redisProperties);
        properties.setDataSources(ds);
        DynamicRedisConnectionFactory factory = new DynamicRedisConnectionFactory(properties,
                provider(), provider(), provider(), provider(), provider(), provider(), provider());
        factory.afterPropertiesSet();
        return factory;
    }

    @Test
    public void afterPropertiesSet() throws Exception {
        RedisProperties redisProperties = new RedisProperties();
        DynamicRedisConnectionFactory factory = factory(redisProperties, false);
        factory.destroy();
    }

    @Test
    public void constructorValidation() {
        try {
            new DynamicRedisConnectionFactory(new DynamicRedisProperties(), provider(), provider(), provider(), provider(), provider(), provider(), provider());
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("多数据源"));
        }
        DynamicRedisProperties p = new DynamicRedisProperties();
        p.setPrimary(" ");
        Map<String, RedisProperties> dsMap = new LinkedHashMap<>();
        dsMap.put("default", new RedisProperties());
        p.setDataSources(dsMap);
        try {
            new DynamicRedisConnectionFactory(p, provider(), provider(), provider(), provider(), provider(), provider(), provider());
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("默认数据源"));
        }
    }

    @Test
    public void connections() throws Exception {
        RedisProperties redisProperties = new RedisProperties();
        DynamicRedisConnectionFactory factory = factory(redisProperties, false);
        // 注入 mock 工厂，全部走委托
        Field f = DynamicRedisConnectionFactory.class.getDeclaredField("multiRedisConnectionFactory");
        f.setAccessible(true);
        Map<String, RedisConnectionFactory> map = (Map) f.get(factory);
        org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory lettuce =
                mock(org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory.class);
        map.put("default", lettuce);
        when(lettuce.getConnection()).thenReturn(null);
        when(lettuce.getClusterConnection()).thenReturn(null);
        when(lettuce.getSentinelConnection()).thenReturn(null);
        when(lettuce.getConvertPipelineAndTxResults()).thenReturn(true);
        when(lettuce.translateExceptionIfPossible(any(RuntimeException.class))).thenReturn(null);
        when(lettuce.getReactiveConnection()).thenReturn(null);
        when(lettuce.getReactiveClusterConnection()).thenReturn(null);
        Assert.assertNull(factory.getConnection());
        Assert.assertNull(factory.getClusterConnection());
        Assert.assertNull(factory.getSentinelConnection());
        Assert.assertTrue(factory.getConvertPipelineAndTxResults());
        Assert.assertNull(factory.translateExceptionIfPossible(new RuntimeException("x")));
        Assert.assertNull(factory.getReactiveConnection());
        Assert.assertNull(factory.getReactiveClusterConnection());
    }

    @Test
    public void notFoundBranches() throws Exception {
        RedisProperties redisProperties = new RedisProperties();
        DynamicRedisConnectionFactory factory = factory(redisProperties, true);
        DynamicConnectFactoryContextHolder.push("missing");
        try {
            factory.getConnection();
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("missing") || e.getMessage().contains("未找到"));
        }
        // 不抛异常 -> 默认数据源
        DynamicRedisConnectionFactory factory2 = factory(redisProperties, false);
        DynamicConnectFactoryContextHolder.push("missing");
        // 默认工厂为 lettuce mock
        Field f = DynamicRedisConnectionFactory.class.getDeclaredField("multiRedisConnectionFactory");
        f.setAccessible(true);
        Map<String, RedisConnectionFactory> map = (Map) f.get(factory2);
        RedisConnectionFactory lf = mock(RedisConnectionFactory.class);
        when(lf.getConnection()).thenReturn(null);
        map.put("default", lf);
        Assert.assertNull(factory2.getConnection());
        DynamicConnectFactoryContextHolder.clear();
    }

    @Test
    public void reactiveNotSupported() throws Exception {
        RedisProperties redisProperties = new RedisProperties();
        DynamicRedisConnectionFactory factory = factory(redisProperties, false);
        // 注入非 reactive 的 mock factory
        Field f = DynamicRedisConnectionFactory.class.getDeclaredField("multiRedisConnectionFactory");
        f.setAccessible(true);
        Map<String, RedisConnectionFactory> map = (Map) f.get(factory);
        RedisConnectionFactory plain = mock(RedisConnectionFactory.class);
        map.put("other", plain);
        DynamicConnectFactoryContextHolder.push("other");
        try {
            factory.getReactiveConnection();
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("ReactiveRedisConnection"));
        }
        try {
            factory.getReactiveClusterConnection();
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("ReactiveRedisClusterConnection"));
        }
        // 委托调用
        when(plain.getConnection()).thenReturn(null);
        when(plain.getConvertPipelineAndTxResults()).thenReturn(true);
        Assert.assertTrue(factory.getConvertPipelineAndTxResults());
        Assert.assertNull(factory.getConnection());
        Assert.assertNull(factory.translateExceptionIfPossible(new RuntimeException("y")));
        // destroy: null / 非 DisposableBean / DisposableBean 抛异常
        map.put("boom", new BoomFactory());
        factory.destroy();
        DynamicConnectFactoryContextHolder.clear();
    }

    private static class BoomFactory implements RedisConnectionFactory, org.springframework.beans.factory.DisposableBean {
        @Override
        public RedisConnection getConnection() {
            return null;
        }

        @Override
        public org.springframework.data.redis.connection.RedisClusterConnection getClusterConnection() {
            return null;
        }

        @Override
        public boolean getConvertPipelineAndTxResults() {
            return false;
        }

        @Override
        public org.springframework.data.redis.connection.RedisSentinelConnection getSentinelConnection() {
            return null;
        }

        @Override
        public DataAccessException translateExceptionIfPossible(RuntimeException e) {
            return null;
        }

        @Override
        public void destroy() {
            throw new RuntimeException("boom");
        }
    }

    @Test
    public void jedisClientType() throws Exception {
        RedisProperties redisProperties = new RedisProperties();
        redisProperties.setClientType(RedisProperties.ClientType.JEDIS);
        DynamicRedisConnectionFactory factory = factory(redisProperties, false);
        Field f = DynamicRedisConnectionFactory.class.getDeclaredField("multiRedisConnectionFactory");
        f.setAccessible(true);
        Map<String, RedisConnectionFactory> map = (Map) f.get(factory);
        Assert.assertTrue(map.get("default") instanceof JedisConnectionFactory);
        factory.destroy();
    }

    @Test
    public void autoDetectJedisOnly() throws Exception {
        RedisProperties redisProperties = new RedisProperties();
        DynamicRedisProperties properties = new DynamicRedisProperties();
        properties.setPrimary("default");
        Map<String, RedisProperties> ds = new LinkedHashMap<>();
        ds.put("default", redisProperties);
        properties.setDataSources(ds);
        DynamicRedisConnectionFactory factory = new DynamicRedisConnectionFactory(properties,
                provider(), provider(), provider(), provider(), provider(), provider(), provider());
        try (org.mockito.MockedStatic<org.springframework.util.ClassUtils> cu = org.mockito.Mockito.mockStatic(org.springframework.util.ClassUtils.class)) {
            cu.when(() -> org.springframework.util.ClassUtils.isPresent(anyString(), any(ClassLoader.class))).thenReturn(false);
            cu.when(() -> org.springframework.util.ClassUtils.isPresent(org.mockito.ArgumentMatchers.eq("redis.clients.jedis.Jedis"), any(ClassLoader.class))).thenReturn(true);
            factory.afterPropertiesSet();
        }
        Field f = DynamicRedisConnectionFactory.class.getDeclaredField("multiRedisConnectionFactory");
        f.setAccessible(true);
        Map<String, RedisConnectionFactory> map = (Map) f.get(factory);
        Assert.assertTrue(map.get("default") instanceof JedisConnectionFactory);
        factory.destroy();
    }

    @Test
    public void autoDetectNone() throws Exception {
        RedisProperties redisProperties = new RedisProperties();
        DynamicRedisProperties properties = new DynamicRedisProperties();
        properties.setPrimary("default");
        Map<String, RedisProperties> ds = new LinkedHashMap<>();
        ds.put("default", redisProperties);
        properties.setDataSources(ds);
        DynamicRedisConnectionFactory factory = new DynamicRedisConnectionFactory(properties,
                provider(), provider(), provider(), provider(), provider(), provider(), provider());
        try (org.mockito.MockedStatic<org.springframework.util.ClassUtils> cu = org.mockito.Mockito.mockStatic(org.springframework.util.ClassUtils.class)) {
            cu.when(() -> org.springframework.util.ClassUtils.isPresent(anyString(), any(ClassLoader.class))).thenReturn(false);
            try {
                factory.afterPropertiesSet();
                Assert.fail();
            } catch (RuntimeException e) {
                Assert.assertTrue(e.getMessage().contains("lettuce"));
            }
        }
    }

    @Test
    public void autoDetectClientType() throws Exception {
        RedisProperties redisProperties = new RedisProperties();
        // 未指定 client-type，自动检测到 lettuce
        DynamicRedisConnectionFactory factory = factory(redisProperties, false);
        Field f = DynamicRedisConnectionFactory.class.getDeclaredField("multiRedisConnectionFactory");
        f.setAccessible(true);
        Map<String, RedisConnectionFactory> map = (Map) f.get(factory);
        Assert.assertTrue(map.get("default") instanceof org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory);
        factory.destroy();
    }
}
