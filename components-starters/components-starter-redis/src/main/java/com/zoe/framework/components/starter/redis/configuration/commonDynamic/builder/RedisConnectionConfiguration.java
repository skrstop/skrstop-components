package com.zoe.framework.components.starter.redis.configuration.commonDynamic.builder;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionDetails;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.data.redis.connection.*;
import org.springframework.util.ClassUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2023-12-07 17:37:48
 */
public abstract class RedisConnectionConfiguration {

    private static final boolean COMMONS_POOL2_AVAILABLE = ClassUtils.isPresent("org.apache.commons.pool2.ObjectPool",
            RedisConnectionConfiguration.class.getClassLoader());

    private final RedisProperties properties;

    private final RedisStandaloneConfiguration standaloneConfiguration;

    private final RedisSentinelConfiguration sentinelConfiguration;

    private final RedisClusterConfiguration clusterConfiguration;

    private final RedisConnectionDetails connectionDetails;

    private final SslBundles sslBundles;

    protected RedisConnectionConfiguration(RedisProperties properties, RedisConnectionDetails connectionDetails,
                                           ObjectProvider<RedisStandaloneConfiguration> standaloneConfigurationProvider,
                                           ObjectProvider<RedisSentinelConfiguration> sentinelConfigurationProvider,
                                           ObjectProvider<RedisClusterConfiguration> clusterConfigurationProvider,
                                           ObjectProvider<SslBundles> sslBundles) {
        this.properties = properties;
        this.standaloneConfiguration = standaloneConfigurationProvider.getIfAvailable();
        this.sentinelConfiguration = sentinelConfigurationProvider.getIfAvailable();
        this.clusterConfiguration = clusterConfigurationProvider.getIfAvailable();
        this.connectionDetails = connectionDetails;
        this.sslBundles = sslBundles.getIfAvailable();
    }

    protected final RedisStandaloneConfiguration getStandaloneConfig() {
        if (this.standaloneConfiguration != null) {
            return this.standaloneConfiguration;
        }
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(this.connectionDetails.getStandalone().getHost());
        config.setPort(this.connectionDetails.getStandalone().getPort());
        config.setUsername(this.connectionDetails.getUsername());
        config.setPassword(RedisPassword.of(this.connectionDetails.getPassword()));
        config.setDatabase(this.connectionDetails.getStandalone().getDatabase());
        return config;
    }

    protected final RedisSentinelConfiguration getSentinelConfig() {
        if (this.sentinelConfiguration != null) {
            return this.sentinelConfiguration;
        }
        if (this.connectionDetails.getSentinel() != null) {
            RedisSentinelConfiguration config = new RedisSentinelConfiguration();
            config.master(this.connectionDetails.getSentinel().getMaster());
            config.setSentinels(createSentinels(this.connectionDetails.getSentinel()));
            config.setUsername(this.connectionDetails.getUsername());
            String password = this.connectionDetails.getPassword();
            if (password != null) {
                config.setPassword(RedisPassword.of(password));
            }
            config.setSentinelUsername(this.connectionDetails.getSentinel().getUsername());
            String sentinelPassword = this.connectionDetails.getSentinel().getPassword();
            if (sentinelPassword != null) {
                config.setSentinelPassword(RedisPassword.of(sentinelPassword));
            }
            config.setDatabase(this.connectionDetails.getSentinel().getDatabase());
            return config;
        }
        return null;
    }

    /**
     * Create a {@link RedisClusterConfiguration} if necessary.
     *
     * @return {@literal null} if no cluster settings are set.
     */
    protected final RedisClusterConfiguration getClusterConfiguration() {
        if (this.clusterConfiguration != null) {
            return this.clusterConfiguration;
        }
        RedisProperties.Cluster clusterProperties = this.properties.getCluster();
        if (this.connectionDetails.getCluster() != null) {
            RedisClusterConfiguration config = new RedisClusterConfiguration(
                    getNodes(this.connectionDetails.getCluster()));
            if (clusterProperties != null && clusterProperties.getMaxRedirects() != null) {
                config.setMaxRedirects(clusterProperties.getMaxRedirects());
            }
            config.setUsername(this.connectionDetails.getUsername());
            String password = this.connectionDetails.getPassword();
            if (password != null) {
                config.setPassword(RedisPassword.of(password));
            }
            return config;
        }
        return null;
    }

    private List<String> getNodes(RedisConnectionDetails.Cluster cluster) {
        return cluster.getNodes().stream().map((node) -> "%s:%d".formatted(node.host(), node.port())).toList();
    }

    protected final RedisProperties getProperties() {
        return this.properties;
    }

    protected SslBundles getSslBundles() {
        return this.sslBundles;
    }

    protected boolean isSslEnabled() {
        return getProperties().getSsl().isEnabled();
    }

    protected boolean isPoolEnabled(RedisProperties.Pool pool) {
        Boolean enabled = pool.getEnabled();
        return (enabled != null) ? enabled : COMMONS_POOL2_AVAILABLE;
    }

    private List<RedisNode> createSentinels(RedisConnectionDetails.Sentinel sentinel) {
        List<RedisNode> nodes = new ArrayList<>();
        for (RedisConnectionDetails.Node node : sentinel.getNodes()) {
            nodes.add(new RedisNode(node.host(), node.port()));
        }
        return nodes;
    }

    protected final boolean urlUsesSsl() {
        return parseUrl(this.properties.getUrl()).isUseSsl();
    }

    protected final RedisConnectionDetails getConnectionDetails() {
        return this.connectionDetails;
    }

    static RedisConnectionConfiguration.ConnectionInfo parseUrl(String url) {
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            if (!"redis".equals(scheme) && !"rediss".equals(scheme)) {
                throw new RedisUrlSyntaxException(url);
            }
            boolean useSsl = ("rediss".equals(scheme));
            String username = null;
            String password = null;
            if (uri.getUserInfo() != null) {
                String candidate = uri.getUserInfo();
                int index = candidate.indexOf(':');
                if (index >= 0) {
                    username = candidate.substring(0, index);
                    password = candidate.substring(index + 1);
                } else {
                    password = candidate;
                }
            }
            return new RedisConnectionConfiguration.ConnectionInfo(uri, useSsl, username, password);
        } catch (URISyntaxException ex) {
            throw new RedisUrlSyntaxException(url, ex);
        }
    }

    static class ConnectionInfo {

        private final URI uri;

        private final boolean useSsl;

        private final String username;

        private final String password;

        ConnectionInfo(URI uri, boolean useSsl, String username, String password) {
            this.uri = uri;
            this.useSsl = useSsl;
            this.username = username;
            this.password = password;
        }

        URI getUri() {
            return this.uri;
        }

        boolean isUseSsl() {
            return this.useSsl;
        }

        String getUsername() {
            return this.username;
        }

        String getPassword() {
            return this.password;
        }

    }

}
