package com.skrstop.framework.components.starter.redis.configuration.commonDynamic.builder;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionDetails;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RedisConnectionConfigurationTest {

    @SuppressWarnings("unchecked")
    public static class TestConfiguration extends RedisConnectionConfiguration {
        TestConfiguration(RedisProperties properties, RedisConnectionDetails connectionDetails,
                          ObjectProvider<RedisStandaloneConfiguration> standalone,
                          ObjectProvider<RedisSentinelConfiguration> sentinel,
                          ObjectProvider<RedisClusterConfiguration> cluster,
                          ObjectProvider<SslBundles> sslBundles) {
            super(properties, connectionDetails, standalone, sentinel, cluster, sslBundles);
        }
    }

    @SuppressWarnings("unchecked")
    private TestConfiguration config(RedisProperties properties, RedisConnectionDetails details,
                                     boolean withStandalone, boolean withSentinel, boolean withCluster) {
        ObjectProvider<RedisStandaloneConfiguration> standalone = mock(ObjectProvider.class);
        ObjectProvider<RedisSentinelConfiguration> sentinel = mock(ObjectProvider.class);
        ObjectProvider<RedisClusterConfiguration> cluster = mock(ObjectProvider.class);
        ObjectProvider<SslBundles> sslBundles = mock(ObjectProvider.class);
        when(standalone.getIfAvailable()).thenReturn(withStandalone ? new RedisStandaloneConfiguration() : null);
        when(sentinel.getIfAvailable()).thenReturn(withSentinel ? new RedisSentinelConfiguration() : null);
        when(cluster.getIfAvailable()).thenReturn(withCluster ? new RedisClusterConfiguration() : null);
        when(sslBundles.getIfAvailable()).thenReturn(null);
        return new TestConfiguration(properties, details, standalone, sentinel, cluster, sslBundles);
    }

    public static class EmptyDetailsHelper implements RedisConnectionDetails {
        @Override
        public Standalone getStandalone() {
            return Standalone.of("h", 1, 0);
        }
    }

    private static class PasswordDetails implements RedisConnectionDetails {
        @Override
        public Standalone getStandalone() {
            return Standalone.of("h", 1, 0);
        }

        @Override
        public String getUsername() {
            return "u";
        }

        @Override
        public String getPassword() {
            return "pw";
        }
    }

    private static class EmptyDetails implements RedisConnectionDetails {
        @Override
        public Standalone getStandalone() {
            return Standalone.of("h", 1, 0);
        }
    }

    @Test
    public void standaloneConfig() {
        RedisProperties properties = new RedisProperties();
        TestConfiguration c = config(properties, new EmptyDetails(), true, false, false);
        Assert.assertNotNull(c.getStandaloneConfig());
        // 无提供者 -> connectionDetails
        TestConfiguration c2 = config(properties, new EmptyDetails(), false, false, false);
        RedisStandaloneConfiguration sc = c2.getStandaloneConfig();
        Assert.assertEquals("h", sc.getHostName());
        // ssl/pool flags
        Assert.assertFalse(c2.isSslEnabled());
        properties.getSsl().setEnabled(true);
        Assert.assertTrue(c2.isSslEnabled());
        Assert.assertNull(c2.getSslBundles());
        properties.getJedis().getPool().setEnabled(true);
        Assert.assertTrue(c2.isPoolEnabled(properties.getJedis().getPool()));
        properties.getLettuce().getPool().setEnabled(false);
        Assert.assertFalse(c2.isPoolEnabled(properties.getLettuce().getPool()));
        Assert.assertSame(properties, c2.getProperties());
        Assert.assertSame(c2.getConnectionDetails(), c2.getConnectionDetails());
    }

    @Test
    public void sentinelConfig() {
        RedisProperties properties = new RedisProperties();
        TestConfiguration c = config(properties, new EmptyDetails(), false, true, false);
        Assert.assertNotNull(c.getSentinelConfig());
        // connectionDetails sentinel
        RedisConnectionDetails.Sentinel sentinelDetails = new RedisConnectionDetails.Sentinel() {
            @Override
            public String getMaster() {
                return "master";
            }

            @Override
            public java.util.List<RedisConnectionDetails.Node> getNodes() {
                return Arrays.asList(new RedisConnectionDetails.Node("h1", 1));
            }

            @Override
            public String getUsername() {
                return "su";
            }

            @Override
            public String getPassword() {
                return "sp";
            }

            @Override
            public int getDatabase() {
                return 0;
            }
        };
        RedisConnectionDetails details = new EmptyDetails() {
            @Override
            public Sentinel getSentinel() {
                return sentinelDetails;
            }
        };
        TestConfiguration c2 = config(properties, new PasswordDetails() {
            @Override
            public Sentinel getSentinel() {
                return sentinelDetails;
            }
        }, false, false, false);
        RedisSentinelConfiguration sc = c2.getSentinelConfig();
        Assert.assertEquals("master", sc.getMaster().getName());
        Assert.assertEquals(1, sc.getSentinels().size());
        Assert.assertEquals("su", sc.getSentinelUsername());
        Assert.assertNotNull(sc.getSentinelPassword());
        Assert.assertNotNull(sc);
        // password 为 null
        RedisConnectionDetails details2 = new EmptyDetails() {
            @Override
            public Sentinel getSentinel() {
                return new RedisConnectionDetails.Sentinel() {
                    @Override
                    public String getMaster() {
                        return "master";
                    }

                    @Override
                    public java.util.List<Node> getNodes() {
                        return java.util.Collections.emptyList();
                    }

                    @Override
                    public int getDatabase() {
                        return 0;
                    }

                    @Override
                    public String getUsername() {
                        return null;
                    }

                    @Override
                    public String getPassword() {
                        return null;
                    }
                };
            }
        };
        RedisSentinelConfiguration sc2 = config(properties, details2, false, false, false).getSentinelConfig();
        Assert.assertNotNull(sc2);
        // 无 sentinel
        Assert.assertNull(config(properties, new EmptyDetails(), false, false, false).getSentinelConfig());
    }

    @Test
    public void clusterConfig() {
        RedisProperties properties = new RedisProperties();
        TestConfiguration c = config(properties, new EmptyDetails(), false, false, true);
        Assert.assertNotNull(c.getClusterConfiguration());
        // connectionDetails cluster
        RedisConnectionDetails.Cluster clusterDetails = () -> Arrays.asList(new RedisConnectionDetails.Node("h1", 1));
        RedisConnectionDetails details = new EmptyDetails() {
            @Override
            public Cluster getCluster() {
                return clusterDetails;
            }
        };
        RedisProperties.Cluster clusterProps = new RedisProperties.Cluster();
        clusterProps.setMaxRedirects(3);
        properties.setCluster(clusterProps);
        RedisClusterConfiguration cc = config(properties, new PasswordDetails() {
            @Override
            public Cluster getCluster() {
                return clusterDetails;
            }
        }, false, false, false).getClusterConfiguration();
        Assert.assertEquals(3L, (long) cc.getMaxRedirects());
        Assert.assertEquals("h1:1", cc.getClusterNodes().iterator().next().getHost() + ":" + cc.getClusterNodes().iterator().next().getPort());
        Assert.assertNotNull(cc);
        // clusterProperties 为 null
        RedisClusterConfiguration cc2 = config(new RedisProperties(), new PasswordDetails() {
            @Override
            public Cluster getCluster() {
                return clusterDetails;
            }
        }, false, false, false).getClusterConfiguration();
        Assert.assertNotNull(cc2);
        // clusterProperties 非空但 maxRedirects 为 null
        RedisProperties properties3 = new RedisProperties();
        properties3.setCluster(new RedisProperties.Cluster());
        RedisClusterConfiguration cc3 = config(properties3, new PasswordDetails() {
            @Override
            public Cluster getCluster() {
                return clusterDetails;
            }
        }, false, false, false).getClusterConfiguration();
        Assert.assertNotNull(cc3);
        // cluster 密码为 null
        RedisClusterConfiguration cc4 = config(properties, new EmptyDetails() {
            @Override
            public Cluster getCluster() {
                return clusterDetails;
            }
        }, false, false, false).getClusterConfiguration();
        Assert.assertNotNull(cc4);
        // 无 cluster
        Assert.assertNull(config(new RedisProperties(), new EmptyDetails(), false, false, false).getClusterConfiguration());
    }

    @Test
    public void parseUrl() {
        // redis scheme
        RedisConnectionConfiguration.ConnectionInfo info = RedisConnectionConfiguration.parseUrl("redis://127.0.0.1:6379");
        Assert.assertFalse(info.isUseSsl());
        Assert.assertNull(info.getUsername());
        Assert.assertNull(info.getPassword());
        Assert.assertEquals("127.0.0.1", info.getUri().getHost());
        // rediss + user:pass
        RedisConnectionConfiguration.ConnectionInfo info2 = RedisConnectionConfiguration.parseUrl("rediss://user:pass@127.0.0.1:6379");
        Assert.assertTrue(info2.isUseSsl());
        Assert.assertEquals("user", info2.getUsername());
        Assert.assertEquals("pass", info2.getPassword());
        // 仅密码
        RedisConnectionConfiguration.ConnectionInfo info3 = RedisConnectionConfiguration.parseUrl("redis://pass@127.0.0.1:6379");
        Assert.assertNull(info3.getUsername());
        Assert.assertEquals("pass", info3.getPassword());
        // 错误 scheme
        try {
            RedisConnectionConfiguration.parseUrl("http://127.0.0.1:6379");
            Assert.fail();
        } catch (RedisUrlSyntaxException e) {
            Assert.assertNotNull(e.getMessage());
        }
        // 非法 URI
        try {
            RedisConnectionConfiguration.parseUrl("redis://a b c");
            Assert.fail();
        } catch (RedisUrlSyntaxException e) {
            Assert.assertNotNull(e.getCause());
        }
    }
}
