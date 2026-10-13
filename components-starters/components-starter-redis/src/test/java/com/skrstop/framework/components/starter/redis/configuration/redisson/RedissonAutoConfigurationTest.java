package com.skrstop.framework.components.starter.redis.configuration.redisson;

import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RedissonAutoConfigurationTest {

    private static final String YAML = "singleServerConfig:\n  address: redis://127.0.0.1:6379\n";
    private static final String JSON = "{\"singleServerConfig\":{\"address\":\"redis://127.0.0.1:6379\"}}";

    @Test
    public void configFile() throws Exception {
        GlobalRedissonProperties p = new GlobalRedissonProperties();
        p.setConfigFile("classpath:redisson.yaml");
        ApplicationContext ctx = mock(ApplicationContext.class);
        when(ctx.getResource(anyString())).thenReturn(new ByteArrayResource(YAML.getBytes(StandardCharsets.UTF_8)));
        try (MockedStatic<Redisson> redisson = Mockito.mockStatic(Redisson.class)) {
            redisson.when(() -> Redisson.create(any(Config.class))).thenReturn(mock(RedissonClient.class));
            Assert.assertNotNull(new RedissonAutoConfiguration().redissonClient(p, ctx));
        }
    }

    @Test
    public void yamlStr() throws Exception {
        GlobalRedissonProperties p = new GlobalRedissonProperties();
        p.setConfigYamlStr(YAML);
        try (MockedStatic<Redisson> redisson = Mockito.mockStatic(Redisson.class)) {
            redisson.when(() -> Redisson.create(any(Config.class))).thenReturn(mock(RedissonClient.class));
            Assert.assertNotNull(new RedissonAutoConfiguration().redissonClient(p, mock(ApplicationContext.class)));
        }
    }

    @Test
    public void jsonStr() throws Exception {
        GlobalRedissonProperties p = new GlobalRedissonProperties();
        p.setConfigJsonStr(JSON);
        try (MockedStatic<Redisson> redisson = Mockito.mockStatic(Redisson.class)) {
            redisson.when(() -> Redisson.create(any(Config.class))).thenReturn(mock(RedissonClient.class));
            Assert.assertNotNull(new RedissonAutoConfiguration().redissonClient(p, mock(ApplicationContext.class)));
        }
    }

    @Test
    public void noConfig() throws Exception {
        try {
            new RedissonAutoConfiguration().redissonClient(new GlobalRedissonProperties(), mock(ApplicationContext.class));
            Assert.fail();
        } catch (SkrstopRuntimeException e) {
            Assert.assertNotNull(e.getMessage());
        }
    }
}
