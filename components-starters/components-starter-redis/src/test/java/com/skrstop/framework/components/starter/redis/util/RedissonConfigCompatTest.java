package com.skrstop.framework.components.starter.redis.util;

import org.junit.Assert;
import org.junit.Test;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

public class RedissonConfigCompatTest {

    @Test
    public void json() {
        String json = "{\"singleServerConfig\":{\"address\":\"redis://127.0.0.1:6379\"}}";
        Config config = RedissonConfigCompat.fromJson(json);
        Assert.assertNotNull(config);
        Assert.assertTrue(config.isSingleConfig());
    }

    @Test
    public void yaml() {
        String yaml = "singleServerConfig:\n  address: redis://127.0.0.1:6379\n";
        Config config = RedissonConfigCompat.fromYaml(yaml);
        Assert.assertNotNull(config);
        Assert.assertTrue(config.isSingleConfig());
    }

    @Test
    public void nestedCodecClass() {
        String yaml = "codec:\n  class: org.redisson.client.codec.StringCodec\nsingleServerConfig:\n  address: redis://127.0.0.1:6379\n";
        Config config = RedissonConfigCompat.fromYaml(yaml);
        Assert.assertTrue(config.getCodec() instanceof StringCodec);
        String json = "{\"codec\":{\"class\":\"org.redisson.client.codec.StringCodec\"},\"singleServerConfig\":{\"address\":\"redis://127.0.0.1:6379\"}}";
        Assert.assertTrue(RedissonConfigCompat.fromJson(json).getCodec() instanceof StringCodec);
        // codec 非 Map
        String yaml2 = "singleServerConfig:\n  address: redis://127.0.0.1:6379\n";
        Assert.assertNotNull(RedissonConfigCompat.fromYaml(yaml2));
    }

    @Test
    public void codecMapWithoutStringClass() {
        // codec 是 Map 但 class 不是 String：codec 留在 map 中，Config.fromYAML 无法映射，抛出异常
        try {
            RedissonConfigCompat.fromYaml("codec:\n  class: 123\nsingleServerConfig:\n  address: redis://127.0.0.1:6379\n");
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void badCodecClass() {
        try {
            RedissonConfigCompat.fromJson("{\"codec\":{\"class\":\"no.such.Codec\"},\"singleServerConfig\":{\"address\":\"redis://127.0.0.1:6379\"}}");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("no.such.Codec"));
        }
    }
}
