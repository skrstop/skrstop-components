package com.skrstop.framework.components.starter.redis.configuration.common;

import com.skrstop.framework.components.starter.redis.constant.ValueProcessorConst;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static org.mockito.Mockito.mock;

public class FastJsonRedisTemplateTest {

    private GlobalRedisProperties props(boolean pretty, boolean safeMode, boolean autoType) {
        GlobalRedisProperties p = new GlobalRedisProperties();
        p.setValueProcessor(ValueProcessorConst.FAST_JSON);
        p.setFastjsonPrettyFormatJson(pretty);
        p.setFastjsonSafeMode(safeMode);
        p.setFastjsonAutoType(autoType);
        return p;
    }

    @Test
    public void serializers() {
        RedisConnectionFactory factory = mock(org.springframework.data.redis.connection.RedisConnectionFactory.class);
        FastJsonRedisTemplate template = new FastJsonRedisTemplate(factory, props(false, true, false));
        Assert.assertNotNull(template.getValueSerializer());
        RedisSerializer serializer = (RedisSerializer) template.getDefaultSerializer();
        Assert.assertEquals(0, serializer.serialize(null).length);
        Assert.assertNull(serializer.deserialize(null));
        Assert.assertNull(serializer.deserialize(new byte[0]));
        byte[] bytes = serializer.serialize("hello");
        Assert.assertNotNull(bytes);
        Assert.assertEquals("hello", serializer.deserialize(bytes));
        // 对象
        byte[] obj = serializer.serialize(new Date(0));
        Assert.assertNotNull(obj);
        Object back = serializer.deserialize(obj);
        Assert.assertNotNull(back);
        // 时间类型
        byte[] ldt = serializer.serialize(LocalDateTime.of(2020, 1, 1, 0, 0));
        Assert.assertNotNull(serializer.deserialize(ldt));
        byte[] ld = serializer.serialize(LocalDate.of(2020, 1, 1));
        Assert.assertNotNull(serializer.deserialize(ld));
        byte[] lt = serializer.serialize(LocalTime.of(1, 0));
        Assert.assertNotNull(serializer.deserialize(lt));
    }

    public static class Bad {
        public String getX() {
            throw new IllegalStateException("boom");
        }
    }

    @Test(expected = org.springframework.data.redis.serializer.SerializationException.class)
    public void serializeFailure() {
        RedisConnectionFactory factory = mock(org.springframework.data.redis.connection.RedisConnectionFactory.class);
        FastJsonRedisTemplate template = new FastJsonRedisTemplate(factory, props(false, true, false));
        RedisSerializer serializer = (RedisSerializer) template.getDefaultSerializer();
        serializer.serialize(new Bad());
    }

    @Test(expected = org.springframework.data.redis.serializer.SerializationException.class)
    public void deserializeFailure() {
        RedisConnectionFactory factory = mock(org.springframework.data.redis.connection.RedisConnectionFactory.class);
        FastJsonRedisTemplate template = new FastJsonRedisTemplate(factory, props(false, true, false));
        RedisSerializer serializer = (RedisSerializer) template.getDefaultSerializer();
        serializer.deserialize("{".getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    @Test
    public void prettyAndAutoType() {
        RedisConnectionFactory factory = mock(org.springframework.data.redis.connection.RedisConnectionFactory.class);
        FastJsonRedisTemplate pretty = new FastJsonRedisTemplate(factory, props(true, true, false));
        RedisSerializer serializer = (RedisSerializer) pretty.getDefaultSerializer();
        byte[] bytes = serializer.serialize("x");
        Assert.assertNotNull(serializer.deserialize(bytes));

        FastJsonRedisTemplate autoType = new FastJsonRedisTemplate(factory, props(false, false, true));
        // safeMode=false 且 autoType=false 组合
        FastJsonRedisTemplate noAuto = new FastJsonRedisTemplate(factory, props(false, false, false));
        RedisSerializer s3 = (RedisSerializer) noAuto.getDefaultSerializer();
        byte[] b3 = s3.serialize("z");
        Assert.assertNotNull(s3.deserialize(b3));
        RedisSerializer s2 = (RedisSerializer) autoType.getDefaultSerializer();
        byte[] b2 = s2.serialize("y");
        Assert.assertNotNull(s2.deserialize(b2));
    }
}
