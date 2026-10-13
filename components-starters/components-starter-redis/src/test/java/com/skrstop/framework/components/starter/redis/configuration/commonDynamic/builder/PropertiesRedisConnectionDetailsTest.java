package com.skrstop.framework.components.starter.redis.configuration.commonDynamic.builder;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import java.util.Arrays;

public class PropertiesRedisConnectionDetailsTest {

    @Test
    public void plain() {
        RedisProperties properties = new RedisProperties();
        properties.setHost("h");
        properties.setPort(2);
        properties.setDatabase(3);
        properties.setUsername("u");
        properties.setPassword("p");
        PropertiesRedisConnectionDetails details = new PropertiesRedisConnectionDetails(properties);
        Assert.assertEquals("u", details.getUsername());
        Assert.assertEquals("p", details.getPassword());
        Assert.assertEquals("h", details.getStandalone().getHost());
        Assert.assertEquals(2, details.getStandalone().getPort());
        Assert.assertEquals(3, details.getStandalone().getDatabase());
        Assert.assertNull(details.getSentinel());
        Assert.assertNull(details.getCluster());
    }

    @Test
    public void withUrl() {
        RedisProperties properties = new RedisProperties();
        properties.setUrl("rediss://user:pass@host:6379/3");
        properties.setDatabase(3);
        properties.setUsername("u2");
        properties.setPassword("p2");
        PropertiesRedisConnectionDetails details = new PropertiesRedisConnectionDetails(properties);
        Assert.assertEquals("user", details.getUsername());
        Assert.assertEquals("pass", details.getPassword());
        Assert.assertEquals("host", details.getStandalone().getHost());
        Assert.assertEquals(6379, details.getStandalone().getPort());
        Assert.assertEquals(3, details.getStandalone().getDatabase());
    }

    @Test
    public void sentinelAndCluster() {
        RedisProperties properties = new RedisProperties();
        RedisProperties.Sentinel sentinel = new RedisProperties.Sentinel();
        sentinel.setMaster("m");
        sentinel.setNodes(Arrays.asList("h1:1", "h2:2"));
        sentinel.setUsername("su");
        sentinel.setPassword("sp");
        properties.setSentinel(sentinel);
        properties.setDatabase(4);
        PropertiesRedisConnectionDetails details = new PropertiesRedisConnectionDetails(properties);
        Assert.assertNotNull(details.getSentinel());
        Assert.assertEquals("m", details.getSentinel().getMaster());
        Assert.assertEquals(2, details.getSentinel().getNodes().size());
        Assert.assertEquals("h1", details.getSentinel().getNodes().get(0).host());
        Assert.assertEquals(1, details.getSentinel().getNodes().get(0).port());
        Assert.assertEquals(4, details.getSentinel().getDatabase());
        Assert.assertEquals("su", details.getSentinel().getUsername());
        Assert.assertEquals("sp", details.getSentinel().getPassword());

        RedisProperties.Cluster cluster = new RedisProperties.Cluster();
        cluster.setNodes(Arrays.asList("c1:1", "c2:2"));
        properties.setCluster(cluster);
        Assert.assertNotNull(details.getCluster());
        Assert.assertEquals("c1", details.getCluster().getNodes().get(0).host());
        Assert.assertEquals(1, details.getCluster().getNodes().get(0).port());
    }
}
