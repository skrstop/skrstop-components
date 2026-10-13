package com.skrstop.framework.components.starter.redis.configuration.commonDynamic.builder;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.ClientResourcesBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LettuceConnectionConfigurationTest {

    @SuppressWarnings("unchecked")
    private LettuceConnectionConfiguration config(RedisProperties properties, boolean sentinel, boolean cluster) {
        ObjectProvider<RedisStandaloneConfiguration> standalone = mock(ObjectProvider.class);
        ObjectProvider<RedisSentinelConfiguration> sentinelProvider = mock(ObjectProvider.class);
        ObjectProvider<RedisClusterConfiguration> clusterProvider = mock(ObjectProvider.class);
        ObjectProvider<SslBundles> sslBundles = mock(ObjectProvider.class);
        when(standalone.getIfAvailable()).thenReturn(null);
        when(sentinelProvider.getIfAvailable()).thenReturn(sentinel
                ? new org.springframework.data.redis.connection.RedisSentinelConfiguration("m", Collections.singleton("h:1")) : null);
        when(clusterProvider.getIfAvailable()).thenReturn(cluster
                ? new org.springframework.data.redis.connection.RedisClusterConfiguration(Collections.singleton("h:1")) : null);
        when(sslBundles.getIfAvailable()).thenReturn(null);
        return new LettuceConnectionConfiguration(properties, new RedisConnectionConfigurationTest.EmptyDetailsHelper(),
                standalone, sentinelProvider, clusterProvider, sslBundles);
    }

    @SuppressWarnings("unchecked")
    private ObjectProvider<ClientResourcesBuilderCustomizer> resourceCustomizers() {
        ObjectProvider<ClientResourcesBuilderCustomizer> provider = mock(ObjectProvider.class);
        when(provider.orderedStream()).thenReturn(java.util.stream.Stream.empty());
        return provider;
    }

    @SuppressWarnings("unchecked")
    private ObjectProvider<LettuceClientConfigurationBuilderCustomizer> builderCustomizers() {
        ObjectProvider<LettuceClientConfigurationBuilderCustomizer> provider = mock(ObjectProvider.class);
        when(provider.orderedStream()).thenReturn(java.util.stream.Stream.empty());
        return provider;
    }

    @SuppressWarnings("unchecked")
    private ObjectProvider<ClientResourcesBuilderCustomizer> resourceCustomizersWithOne() {
        ClientResourcesBuilderCustomizer customizer = builder -> {
        };
        ObjectProvider<ClientResourcesBuilderCustomizer> provider = mock(ObjectProvider.class);
        when(provider.orderedStream()).thenReturn(java.util.stream.Stream.of(customizer));
        return provider;
    }

    @SuppressWarnings("unchecked")
    private ObjectProvider<LettuceClientConfigurationBuilderCustomizer> builderCustomizersWithOne() {
        LettuceClientConfigurationBuilderCustomizer customizer = builder -> {
        };
        ObjectProvider<LettuceClientConfigurationBuilderCustomizer> provider = mock(ObjectProvider.class);
        when(provider.orderedStream()).thenReturn(java.util.stream.Stream.of(customizer));
        return provider;
    }

    @SuppressWarnings("unchecked")
    private LettuceConnectionConfiguration config(RedisProperties properties, boolean sentinel, boolean cluster, SslBundles sslBundles) {
        ObjectProvider<RedisStandaloneConfiguration> standalone = mock(ObjectProvider.class);
        ObjectProvider<RedisSentinelConfiguration> sentinelProvider = mock(ObjectProvider.class);
        ObjectProvider<RedisClusterConfiguration> clusterProvider = mock(ObjectProvider.class);
        ObjectProvider<SslBundles> sslBundlesProvider = mock(ObjectProvider.class);
        when(standalone.getIfAvailable()).thenReturn(null);
        when(sentinelProvider.getIfAvailable()).thenReturn(sentinel
                ? new org.springframework.data.redis.connection.RedisSentinelConfiguration("m", Collections.singleton("h:1")) : null);
        when(clusterProvider.getIfAvailable()).thenReturn(cluster
                ? new org.springframework.data.redis.connection.RedisClusterConfiguration(Collections.singleton("h:1")) : null);
        when(sslBundlesProvider.getIfAvailable()).thenReturn(sslBundles);
        return new LettuceConnectionConfiguration(properties, new RedisConnectionConfigurationTest.EmptyDetailsHelper(),
                standalone, sentinelProvider, clusterProvider, sslBundlesProvider);
    }

    @Test
    public void buildPoolDisabled() {
        RedisProperties properties = new RedisProperties();
        properties.getLettuce().getPool().setEnabled(false);
        LettuceConnectionFactory factory = config(properties, false, false).build(resourceCustomizers(), builderCustomizers());
        Assert.assertNotNull(factory);
    }

    @Test
    public void buildEvictionOnly() {
        RedisProperties properties = new RedisProperties();
        properties.getLettuce().getPool().setEnabled(true);
        properties.getLettuce().getPool().setMaxActive(1);
        properties.getLettuce().getPool().setMaxIdle(1);
        properties.getLettuce().getPool().setMinIdle(1);
        properties.getLettuce().getPool().setTimeBetweenEvictionRuns(Duration.ofSeconds(1));
        properties.getLettuce().getPool().setMaxWait(null);
        LettuceConnectionFactory factory = config(properties, false, false).build(resourceCustomizers(), builderCustomizers());
        Assert.assertNotNull(factory);
    }

    @Test
    public void buildMaxWaitOnly() {
        RedisProperties properties = new RedisProperties();
        properties.getLettuce().getPool().setEnabled(true);
        properties.getLettuce().getPool().setMaxActive(1);
        properties.getLettuce().getPool().setMaxIdle(1);
        properties.getLettuce().getPool().setMinIdle(1);
        properties.getLettuce().getPool().setMaxWait(Duration.ofSeconds(1));
        LettuceConnectionFactory factory = config(properties, false, false).build(resourceCustomizers(), builderCustomizers());
        Assert.assertNotNull(factory);
    }

    @Test
    public void buildUrlNotSsl() {
        RedisProperties properties = new RedisProperties();
        properties.setUrl("redis://127.0.0.1:6379");
        LettuceConnectionFactory factory = config(properties, false, false).build(resourceCustomizers(), builderCustomizers());
        Assert.assertNotNull(factory);
    }

    @Test
    public void buildShutdownTimeoutNull() {
        RedisProperties properties = new RedisProperties();
        properties.getLettuce().setShutdownTimeout(null);
        LettuceConnectionFactory factory = config(properties, false, false).build(resourceCustomizers(), builderCustomizers());
        Assert.assertNotNull(factory);
    }

    @Test
    public void buildSslBundleNoOptions() {
        RedisProperties properties = new RedisProperties();
        properties.getSsl().setEnabled(true);
        properties.getSsl().setBundle("b");
        SslBundles sslBundles = mock(SslBundles.class);
        org.springframework.boot.ssl.SslBundle bundle = mock(org.springframework.boot.ssl.SslBundle.class);
        when(sslBundles.getBundle("b")).thenReturn(bundle);
        org.springframework.boot.ssl.SslManagerBundle managers = mock(org.springframework.boot.ssl.SslManagerBundle.class);
        when(bundle.getManagers()).thenReturn(managers);
        when(managers.getKeyManagerFactory()).thenReturn(mock(javax.net.ssl.KeyManagerFactory.class));
        when(managers.getTrustManagerFactory()).thenReturn(mock(javax.net.ssl.TrustManagerFactory.class));
        org.springframework.boot.ssl.SslOptions sslOptions = mock(org.springframework.boot.ssl.SslOptions.class);
        when(bundle.getOptions()).thenReturn(sslOptions);
        LettuceConnectionFactory factory = config(properties, false, false, sslBundles)
                .build(resourceCustomizers(), builderCustomizers());
        Assert.assertNotNull(factory);
    }

    @Test
    public void buildClusterRefreshDefaults() {
        RedisProperties properties = new RedisProperties();
        properties.setCluster(new RedisProperties.Cluster());
        LettuceConnectionFactory factory = config(properties, false, true).build(resourceCustomizers(), builderCustomizers());
        Assert.assertNotNull(factory);
    }

    @Test
    public void buildSslBundleAndCustomizers() {
        RedisProperties properties = new RedisProperties();
        properties.getSsl().setEnabled(true);
        properties.getSsl().setBundle("b");
        SslBundles sslBundles = mock(SslBundles.class);
        org.springframework.boot.ssl.SslBundle bundle = mock(org.springframework.boot.ssl.SslBundle.class);
        when(sslBundles.getBundle("b")).thenReturn(bundle);
        org.springframework.boot.ssl.SslManagerBundle managers = mock(org.springframework.boot.ssl.SslManagerBundle.class);
        when(bundle.getManagers()).thenReturn(managers);
        when(managers.getKeyManagerFactory()).thenReturn(mock(javax.net.ssl.KeyManagerFactory.class));
        when(managers.getTrustManagerFactory()).thenReturn(mock(javax.net.ssl.TrustManagerFactory.class));
        org.springframework.boot.ssl.SslOptions sslOptions = mock(org.springframework.boot.ssl.SslOptions.class);
        when(bundle.getOptions()).thenReturn(sslOptions);
        when(sslOptions.getCiphers()).thenReturn(new String[]{"TLS"});
        when(sslOptions.getEnabledProtocols()).thenReturn(new String[]{"TLSv1.2"});
        LettuceConnectionFactory factory = config(properties, false, false, sslBundles)
                .build(resourceCustomizersWithOne(), builderCustomizersWithOne());
        Assert.assertNotNull(factory);
    }

    @Test
    public void buildStandalone() {
        RedisProperties properties = new RedisProperties();
        properties.setTimeout(Duration.ofSeconds(1));
        properties.setConnectTimeout(Duration.ofSeconds(1));
        properties.setClientName("client");
        properties.getLettuce().setShutdownTimeout(Duration.ZERO);
        LettuceConnectionFactory factory = config(properties, false, false).build(resourceCustomizers(), builderCustomizers());
        Assert.assertNotNull(factory);
    }

    @Test
    public void buildPoolSslShutdownTimeout() {
        RedisProperties properties = new RedisProperties();
        properties.getLettuce().getPool().setEnabled(true);
        properties.getLettuce().getPool().setMaxActive(1);
        properties.getLettuce().getPool().setMaxIdle(1);
        properties.getLettuce().getPool().setMinIdle(1);
        properties.getLettuce().getPool().setTimeBetweenEvictionRuns(Duration.ofSeconds(1));
        properties.getLettuce().getPool().setMaxWait(Duration.ofSeconds(1));
        properties.getSsl().setEnabled(true);
        properties.getLettuce().setShutdownTimeout(Duration.ofSeconds(2));
        LettuceConnectionFactory factory = config(properties, false, false).build(resourceCustomizers(), builderCustomizers());
        Assert.assertNotNull(factory);
    }

    @Test
    public void buildClusterRefresh() {
        RedisProperties properties = new RedisProperties();
        properties.setCluster(new RedisProperties.Cluster());
        RedisProperties.Lettuce.Cluster.Refresh refresh = properties.getLettuce().getCluster().getRefresh();
        refresh.setPeriod(Duration.ofSeconds(1));
        refresh.setAdaptive(true);
        LettuceConnectionFactory factory = config(properties, false, true).build(resourceCustomizers(), builderCustomizers());
        Assert.assertNotNull(factory);
    }

    @Test
    public void buildSentinelAndUrlSsl() {
        RedisProperties properties = new RedisProperties();
        properties.setUrl("rediss://127.0.0.1:6379");
        LettuceConnectionFactory factory = config(properties, true, false).build(resourceCustomizers(), builderCustomizers());
        Assert.assertNotNull(factory);
    }
}
