package com.skrstop.framework.components.starter.redis.configuration.commonDynamic.builder;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.JedisClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionDetails;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.time.Duration;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JedisConnectionConfigurationTest {

    @SuppressWarnings("unchecked")
    private JedisConnectionConfiguration config(RedisProperties properties) {
        ObjectProvider<RedisStandaloneConfiguration> standalone = mock(ObjectProvider.class);
        ObjectProvider<RedisSentinelConfiguration> sentinel = mock(ObjectProvider.class);
        ObjectProvider<RedisClusterConfiguration> cluster = mock(ObjectProvider.class);
        ObjectProvider<SslBundles> sslBundles = mock(ObjectProvider.class);
        when(standalone.getIfAvailable()).thenReturn(null);
        when(sentinel.getIfAvailable()).thenReturn(null);
        when(cluster.getIfAvailable()).thenReturn(null);
        when(sslBundles.getIfAvailable()).thenReturn(null);
        return new JedisConnectionConfiguration(properties, new EmptyDetails(), standalone, sentinel, cluster, sslBundles);
    }

    private static class EmptyDetails implements RedisConnectionDetails {
        @Override
        public Standalone getStandalone() {
            return Standalone.of("h", 1, 0);
        }
    }

    @SuppressWarnings("unchecked")
    private ObjectProvider<JedisClientConfigurationBuilderCustomizer> customizers() {
        ObjectProvider<JedisClientConfigurationBuilderCustomizer> provider = mock(ObjectProvider.class);
        when(provider.orderedStream()).thenReturn(java.util.stream.Stream.empty());
        return provider;
    }

    @SuppressWarnings("unchecked")
    private ObjectProvider<JedisClientConfigurationBuilderCustomizer> customizersWithOne() {
        JedisClientConfigurationBuilderCustomizer customizer = builder -> {
        };
        ObjectProvider<JedisClientConfigurationBuilderCustomizer> provider = mock(ObjectProvider.class);
        when(provider.orderedStream()).thenReturn(java.util.stream.Stream.of(customizer));
        return provider;
    }

    @Test
    public void buildStandalone() {
        RedisProperties properties = new RedisProperties();
        properties.setClientName("client");
        properties.setTimeout(Duration.ofSeconds(1));
        properties.setConnectTimeout(Duration.ofSeconds(1));
        JedisConnectionFactory factory = config(properties).build(customizers());
        Assert.assertNotNull(factory);
    }

    @Test
    public void buildPoolAndSslAndUrl() {
        RedisProperties properties = new RedisProperties();
        properties.getJedis().getPool().setEnabled(true);
        properties.getJedis().getPool().setMaxActive(1);
        properties.getJedis().getPool().setMaxIdle(1);
        properties.getJedis().getPool().setMinIdle(1);
        properties.getJedis().getPool().setTimeBetweenEvictionRuns(Duration.ofSeconds(1));
        properties.getJedis().getPool().setMaxWait(Duration.ofSeconds(1));
        properties.getSsl().setEnabled(true);
        properties.setUrl("redis://127.0.0.1:6379");
        JedisConnectionFactory factory = config(properties).build(customizers());
        Assert.assertNotNull(factory);
    }

    @Test
    public void buildUrlSsl() {
        RedisProperties properties = new RedisProperties();
        properties.setUrl("rediss://127.0.0.1:6379");
        JedisConnectionFactory factory = config(properties).build(customizers());
        Assert.assertNotNull(factory);
    }

    @Test
    public void buildPoolDisabledAndEvictionOnly() {
        RedisProperties properties = new RedisProperties();
        properties.getJedis().getPool().setEnabled(false);
        JedisConnectionFactory factory = config(properties).build(customizers());
        Assert.assertNotNull(factory);
        // 仅 timeBetweenEvictionRuns，maxWait 为 null
        RedisProperties properties2 = new RedisProperties();
        properties2.getJedis().getPool().setEnabled(true);
        properties2.getJedis().getPool().setMaxActive(1);
        properties2.getJedis().getPool().setMaxIdle(1);
        properties2.getJedis().getPool().setMinIdle(1);
        properties2.getJedis().getPool().setTimeBetweenEvictionRuns(Duration.ofSeconds(1));
        properties2.getJedis().getPool().setMaxWait(null);
        JedisConnectionFactory factory2 = config(properties2).build(customizers());
        Assert.assertNotNull(factory2);
        // 仅 maxWait，timeBetweenEvictionRuns 为 null
        RedisProperties properties3 = new RedisProperties();
        properties3.getJedis().getPool().setEnabled(true);
        properties3.getJedis().getPool().setMaxActive(1);
        properties3.getJedis().getPool().setMaxIdle(1);
        properties3.getJedis().getPool().setMinIdle(1);
        properties3.getJedis().getPool().setMaxWait(Duration.ofSeconds(1));
        JedisConnectionFactory factory3 = config(properties3).build(customizers());
        Assert.assertNotNull(factory3);
    }

    @Test
    public void buildWithSslBundleAndCustomizer() {
        RedisProperties properties = new RedisProperties();
        properties.getSsl().setEnabled(true);
        properties.getSsl().setBundle("b");
        SslBundles sslBundles = mock(SslBundles.class);
        org.springframework.boot.ssl.SslBundle bundle = mock(org.springframework.boot.ssl.SslBundle.class);
        when(sslBundles.getBundle("b")).thenReturn(bundle);
        javax.net.ssl.SSLContext sslContext = mock(javax.net.ssl.SSLContext.class);
        when(bundle.createSslContext()).thenReturn(sslContext);
        when(sslContext.getSocketFactory()).thenReturn(mock(javax.net.ssl.SSLSocketFactory.class));
        org.springframework.boot.ssl.SslOptions sslOptions = mock(org.springframework.boot.ssl.SslOptions.class);
        when(bundle.getOptions()).thenReturn(sslOptions);
        when(sslOptions.getCiphers()).thenReturn(new String[]{"TLS"});
        when(sslOptions.getEnabledProtocols()).thenReturn(new String[]{"TLSv1.2"});
        ObjectProvider<RedisStandaloneConfiguration> standalone = mock(ObjectProvider.class);
        ObjectProvider<RedisSentinelConfiguration> sentinel = mock(ObjectProvider.class);
        ObjectProvider<RedisClusterConfiguration> cluster = mock(ObjectProvider.class);
        ObjectProvider<SslBundles> sslBundlesProvider = mock(ObjectProvider.class);
        when(standalone.getIfAvailable()).thenReturn(null);
        when(sentinel.getIfAvailable()).thenReturn(null);
        when(cluster.getIfAvailable()).thenReturn(null);
        when(sslBundlesProvider.getIfAvailable()).thenReturn(sslBundles);
        JedisConnectionFactory factory = new JedisConnectionConfiguration(properties, new EmptyDetails(),
                standalone, sentinel, cluster, sslBundlesProvider).build(customizersWithOne());
        Assert.assertNotNull(factory);
    }

    @Test
    public void buildClusterAndSentinel() {
        RedisProperties properties = new RedisProperties();
        // sentinel 路径
        org.springframework.data.redis.connection.RedisSentinelConfiguration sentinelConfiguration =
                new org.springframework.data.redis.connection.RedisSentinelConfiguration("m", Collections.singleton("h:1"));
        ObjectProvider<RedisStandaloneConfiguration> standalone = mock(ObjectProvider.class);
        ObjectProvider<RedisSentinelConfiguration> sentinel = mock(ObjectProvider.class);
        ObjectProvider<RedisClusterConfiguration> cluster = mock(ObjectProvider.class);
        ObjectProvider<SslBundles> sslBundles = mock(ObjectProvider.class);
        when(standalone.getIfAvailable()).thenReturn(null);
        when(sentinel.getIfAvailable()).thenReturn(sentinelConfiguration);
        when(cluster.getIfAvailable()).thenReturn(null);
        when(sslBundles.getIfAvailable()).thenReturn(null);
        JedisConnectionFactory factory = new JedisConnectionConfiguration(properties, new EmptyDetails(),
                standalone, sentinel, cluster, sslBundles).build(customizers());
        Assert.assertNotNull(factory);
        // cluster 路径
        when(sentinel.getIfAvailable()).thenReturn(null);
        when(cluster.getIfAvailable()).thenReturn(new org.springframework.data.redis.connection.RedisClusterConfiguration(Collections.singleton("h:1")));
        JedisConnectionFactory factory2 = new JedisConnectionConfiguration(properties, new EmptyDetails(),
                standalone, sentinel, cluster, sslBundles).build(customizers());
        Assert.assertNotNull(factory2);
    }
}
