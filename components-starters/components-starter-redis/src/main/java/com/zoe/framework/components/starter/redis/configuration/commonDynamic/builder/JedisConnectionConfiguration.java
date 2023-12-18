package com.zoe.framework.components.starter.redis.configuration.commonDynamic.builder;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.JedisClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionDetails;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.ssl.SslOptions;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import javax.net.ssl.SSLParameters;

/**
 * @author 蒋时华
 * @date 2023-12-07 17:47:29
 */
public class JedisConnectionConfiguration extends RedisConnectionConfiguration {

    public JedisConnectionConfiguration(RedisProperties properties,
                                        RedisConnectionDetails connectionDetails,
                                        ObjectProvider<RedisStandaloneConfiguration> standaloneConfigurationProvider,
                                        ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration,
                                        ObjectProvider<RedisClusterConfiguration> clusterConfiguration,
                                        ObjectProvider<SslBundles> sslBundles) {
        super(properties, connectionDetails, standaloneConfigurationProvider, sentinelConfiguration,
                clusterConfiguration, sslBundles);
    }

    public JedisConnectionFactory build(ObjectProvider<JedisClientConfigurationBuilderCustomizer> builderCustomizers) {
        return createJedisConnectionFactory(builderCustomizers);
    }

    private JedisConnectionFactory createJedisConnectionFactory(
            ObjectProvider<JedisClientConfigurationBuilderCustomizer> builderCustomizers) {
        JedisClientConfiguration clientConfiguration = getJedisClientConfiguration(builderCustomizers);
        if (getSentinelConfig() != null) {
            return new JedisConnectionFactory(getSentinelConfig(), clientConfiguration);
        }
        if (getClusterConfiguration() != null) {
            return new JedisConnectionFactory(getClusterConfiguration(), clientConfiguration);
        }
        return new JedisConnectionFactory(getStandaloneConfig(), clientConfiguration);
    }

    private JedisClientConfiguration getJedisClientConfiguration(
            ObjectProvider<JedisClientConfigurationBuilderCustomizer> builderCustomizers) {
        JedisClientConfiguration.JedisClientConfigurationBuilder builder = applyProperties(JedisClientConfiguration.builder());
        if (isSslEnabled()) {
            applySsl(builder);
        }
        RedisProperties.Pool pool = getProperties().getJedis().getPool();
        if (isPoolEnabled(pool)) {
            applyPooling(pool, builder);
        }
        if (StringUtils.hasText(getProperties().getUrl())) {
            customizeConfigurationFromUrl(builder);
        }
        builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
        return builder.build();
    }

    private JedisClientConfiguration.JedisClientConfigurationBuilder applyProperties(JedisClientConfiguration.JedisClientConfigurationBuilder builder) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        map.from(getProperties().getTimeout()).to(builder::readTimeout);
        map.from(getProperties().getConnectTimeout()).to(builder::connectTimeout);
        map.from(getProperties().getClientName()).whenHasText().to(builder::clientName);
        return builder;
    }

    private void applySsl(JedisClientConfiguration.JedisClientConfigurationBuilder builder) {
        JedisClientConfiguration.JedisSslClientConfigurationBuilder sslBuilder = builder.useSsl();
        if (getProperties().getSsl().getBundle() != null) {
            SslBundle sslBundle = getSslBundles().getBundle(getProperties().getSsl().getBundle());
            sslBuilder.sslSocketFactory(sslBundle.createSslContext().getSocketFactory());
            SslOptions sslOptions = sslBundle.getOptions();
            SSLParameters sslParameters = new SSLParameters();
            PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
            map.from(sslOptions.getCiphers()).to(sslParameters::setCipherSuites);
            map.from(sslOptions.getEnabledProtocols()).to(sslParameters::setProtocols);
            sslBuilder.sslParameters(sslParameters);
        }
    }

    private void applyPooling(RedisProperties.Pool pool,
                              JedisClientConfiguration.JedisClientConfigurationBuilder builder) {
        builder.usePooling().poolConfig(jedisPoolConfig(pool));
    }

    private JedisPoolConfig jedisPoolConfig(RedisProperties.Pool pool) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(pool.getMaxActive());
        config.setMaxIdle(pool.getMaxIdle());
        config.setMinIdle(pool.getMinIdle());
        if (pool.getTimeBetweenEvictionRuns() != null) {
            config.setTimeBetweenEvictionRuns(pool.getTimeBetweenEvictionRuns());
        }
        if (pool.getMaxWait() != null) {
            config.setMaxWait(pool.getMaxWait());
        }
        return config;
    }

    private void customizeConfigurationFromUrl(JedisClientConfiguration.JedisClientConfigurationBuilder builder) {
        if (urlUsesSsl()) {
            builder.useSsl();
        }
    }

}
