package com.skrstop.framework.components.starter.redis.configuration.redissonDynamic;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MultiRedissonTest {

    private static final String YAML = "singleServerConfig:\n  address: redis://127.0.0.1:6379\n";

    private DynamicRedissonProperties props() {
        DynamicRedissonProperties p = new DynamicRedissonProperties();
        p.setPrimary("default");
        Map<String, DynamicRedissonProperties.Config> ds = new LinkedHashMap<>();
        DynamicRedissonProperties.Config c = new DynamicRedissonProperties.Config();
        c.setConfigYamlStr(YAML);
        ds.put("default", c);
        p.setDataSources(ds);
        return p;
    }

    @Test
    public void constructorValidation() {
        try {
            new MultiRedisson(new DynamicRedissonProperties(), mock(ApplicationContext.class));
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("多数据源"));
        }
        DynamicRedissonProperties noPrimary = new DynamicRedissonProperties();
        noPrimary.setPrimary(" ");
        Map<String, DynamicRedissonProperties.Config> dsMap = new LinkedHashMap<>();
        dsMap.put("default", config(YAML));
        noPrimary.setDataSources(dsMap);
        try {
            new MultiRedisson(noPrimary, mock(ApplicationContext.class));
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("默认数据源"));
        }
        DynamicRedissonProperties missing = new DynamicRedissonProperties();
        missing.setPrimary("default");
        Map<String, DynamicRedissonProperties.Config> dsMap2 = new LinkedHashMap<>();
        dsMap2.put("other", config(YAML));
        missing.setDataSources(dsMap2);
        try {
            new MultiRedisson(missing, mock(ApplicationContext.class));
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("默认数据源未配置"));
        }
    }

    private MultiRedisson client(Map<String, DynamicRedissonProperties.Config> ds) throws Exception {
        DynamicRedissonProperties p = new DynamicRedissonProperties();
        p.setPrimary("default");
        p.setDataSources(ds);
        try (MockedStatic<Redisson> redisson = Mockito.mockStatic(Redisson.class)) {
            redisson.when(() -> Redisson.create(any(Config.class))).thenReturn(mock(RedissonClient.class));
            MultiRedisson multi = new MultiRedisson(p, mock(ApplicationContext.class));
            multi.afterPropertiesSet();
            return multi;
        }
    }

    @Test
    public void getRedissonClient() throws Exception {
        Map<String, DynamicRedissonProperties.Config> ds = new LinkedHashMap<>();
        ds.put("default", config(YAML));
        ds.put("other", config(YAML));
        MultiRedisson multi = client(ds);

        // blank -> primary
        Assert.assertNotNull(multi.getRedissonClient());
        // 未找到 & 不抛异常 -> default
        DynamicRedissonContextHolder.push("missing");
        Assert.assertNotNull(multi.getRedissonClient());
        // 未找到 & 抛异常
        DynamicRedissonProperties p2 = new DynamicRedissonProperties();
        p2.setPrimary("default");
        p2.setExceptionWhileNotFound(true);
        p2.setDataSources(ds);
        MultiRedisson multi2 = new MultiRedisson(p2, mock(ApplicationContext.class));
        java.lang.reflect.Field f = MultiRedisson.class.getDeclaredField("multiRedissonClient");
        f.setAccessible(true);
        Map<String, RedissonClient> map = (Map) f.get(multi2);
        map.put("default", mock(RedissonClient.class));
        try {
            multi2.getRedissonClient();
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("missing"));
        }
        // 找到指定数据源
        map.put("other", mock(RedissonClient.class));
        DynamicRedissonContextHolder.push("other");
        Assert.assertNotNull(multi2.getRedissonClient());
        DynamicRedissonContextHolder.clear();
    }

    private DynamicRedissonProperties.Config config(String yaml) {
        DynamicRedissonProperties.Config c = new DynamicRedissonProperties.Config();
        c.setConfigYamlStr(yaml);
        return c;
    }

    @Test
    public void afterPropertiesSetFromFile() throws Exception {
        DynamicRedissonProperties p = new DynamicRedissonProperties();
        p.setPrimary("default");
        Map<String, DynamicRedissonProperties.Config> ds = new LinkedHashMap<>();
        DynamicRedissonProperties.Config c = new DynamicRedissonProperties.Config();
        c.setConfigFile("classpath:redisson.yaml");
        ds.put("default", c);
        p.setDataSources(ds);
        ApplicationContext ctx = mock(ApplicationContext.class);
        when(ctx.getResource(anyString())).thenReturn(new ByteArrayResource(YAML.getBytes(StandardCharsets.UTF_8)));
        try (MockedStatic<Redisson> redisson = Mockito.mockStatic(Redisson.class)) {
            redisson.when(() -> Redisson.create(any(Config.class))).thenReturn(mock(RedissonClient.class));
            MultiRedisson multi = new MultiRedisson(p, ctx);
            multi.afterPropertiesSet();
        }
        // 未配置参数
        DynamicRedissonProperties.Config empty = new DynamicRedissonProperties.Config();
        ds.put("default", empty);
        try (MockedStatic<Redisson> redisson = Mockito.mockStatic(Redisson.class)) {
            MultiRedisson multi = new MultiRedisson(p, ctx);
            try {
                multi.afterPropertiesSet();
                Assert.fail();
            } catch (Exception e) {
                Assert.assertTrue(e.getMessage().contains("default"));
            }
        }
    }

    @Test
    @org.junit.After
    public void clearHolder() {
        DynamicRedissonContextHolder.clear();
    }

    @Test
    public void destroyBranches() throws Exception {
        Map<String, DynamicRedissonProperties.Config> ds = new LinkedHashMap<>();
        ds.put("default", config(YAML));
        MultiRedisson multi = client(ds);
        java.lang.reflect.Field f = MultiRedisson.class.getDeclaredField("multiRedissonClient");
        f.setAccessible(true);
        Map<String, RedissonClient> map = (Map) f.get(multi);
        RedissonClient normal = mock(RedissonClient.class);
        when(normal.isShutdown()).thenReturn(false);
        when(normal.isShuttingDown()).thenReturn(false);
        RedissonClient shut = mock(RedissonClient.class);
        when(shut.isShutdown()).thenReturn(true);
        RedissonClient shutting = mock(RedissonClient.class);
        when(shutting.isShuttingDown()).thenReturn(true);
        RedissonClient boom = mock(RedissonClient.class);
        when(boom.isShutdown()).thenReturn(false);
        when(boom.isShuttingDown()).thenReturn(false);
        Mockito.doThrow(new RuntimeException("x")).when(boom).shutdown();
        map.put("n", normal);
        map.put("s", shut);
        map.put("g", shutting);
        map.put("b", boom);
        multi.destroy();
        Mockito.verify(normal).shutdown();
        Mockito.verify(boom).shutdown();
    }
}
