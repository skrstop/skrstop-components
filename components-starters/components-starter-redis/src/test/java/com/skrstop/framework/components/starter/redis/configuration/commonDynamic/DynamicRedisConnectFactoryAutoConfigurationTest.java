package com.skrstop.framework.components.starter.redis.configuration.commonDynamic;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.ObjectProvider;

import static org.mockito.Mockito.mock;

public class DynamicRedisConnectFactoryAutoConfigurationTest {

    @Test
    @SuppressWarnings("unchecked")
    public void bean() {
        DynamicRedisConnectFactoryAutoConfiguration configuration = new DynamicRedisConnectFactoryAutoConfiguration();
        ObjectProvider<org.springframework.data.redis.connection.RedisStandaloneConfiguration> standalone = mock(ObjectProvider.class);
        ObjectProvider<org.springframework.data.redis.connection.RedisSentinelConfiguration> sentinel = mock(ObjectProvider.class);
        ObjectProvider<org.springframework.data.redis.connection.RedisClusterConfiguration> cluster = mock(ObjectProvider.class);
        ObjectProvider<org.springframework.boot.autoconfigure.data.redis.JedisClientConfigurationBuilderCustomizer> jedis = mock(ObjectProvider.class);
        ObjectProvider<org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer> lettuce = mock(ObjectProvider.class);
        ObjectProvider<org.springframework.boot.autoconfigure.data.redis.ClientResourcesBuilderCustomizer> resources = mock(ObjectProvider.class);
        ObjectProvider<org.springframework.boot.ssl.SslBundles> sslBundles = mock(ObjectProvider.class);
        DynamicRedisProperties properties = new DynamicRedisProperties();
        java.util.Map<String, org.springframework.boot.autoconfigure.data.redis.RedisProperties> ds = new java.util.LinkedHashMap<>();
        ds.put("default", new org.springframework.boot.autoconfigure.data.redis.RedisProperties());
        properties.setDataSources(ds);
        Assert.assertNotNull(configuration.dynamicRedisConnectionFactory(properties,
                standalone, sentinel, cluster, jedis, lettuce, resources, sslBundles));
    }
}
