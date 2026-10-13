package com.skrstop.framework.components.starter.redis.configuration.common;

import com.skrstop.framework.components.starter.redis.constant.ValueProcessorConst;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.lang.reflect.Field;

import static org.mockito.Mockito.mock;

public class CustomRedisAutoConfigurationTest {

    private GlobalRedisProperties props(byte processor, boolean safeMode, boolean autoType) throws Exception {
        GlobalRedisProperties p = props(processor);
        p.setFastjsonSafeMode(safeMode);
        p.setFastjsonAutoType(autoType);
        return p;
    }

    @Test
    public void fastJsonSafeModeOff() throws Exception {
        RedisConnectionFactory factory = mock(RedisConnectionFactory.class);
        Assert.assertNotNull(new CustomRedisAutoConfiguration().redisService(factory, props(ValueProcessorConst.FAST_JSON, false, true)));
        Assert.assertNotNull(new CustomRedisAutoConfiguration().redisService(factory, props(ValueProcessorConst.FAST_JSON, false, false)));
    }

    private GlobalRedisProperties props(byte processor) throws Exception {
        GlobalRedisProperties p = new GlobalRedisProperties();
        if (p.getValueProcessor() != processor) {
            Field f = GlobalRedisProperties.class.getDeclaredField("valueProcessor");
            f.setAccessible(true);
            f.set(p, Byte.valueOf(processor));
        }
        return p;
    }

    @Test
    public void binary() throws Exception {
        RedisConnectionFactory factory = mock(RedisConnectionFactory.class);
        Assert.assertNotNull(new CustomRedisAutoConfiguration().redisService(factory, props(ValueProcessorConst.BINARY)));
    }

    @Test
    public void string() throws Exception {
        RedisConnectionFactory factory = mock(RedisConnectionFactory.class);
        Assert.assertNotNull(new CustomRedisAutoConfiguration().redisService(factory, props(ValueProcessorConst.STRING)));
    }

    @Test
    public void fastJson() throws Exception {
        RedisConnectionFactory factory = mock(RedisConnectionFactory.class);
        Assert.assertNotNull(new CustomRedisAutoConfiguration().redisService(factory, props(ValueProcessorConst.FAST_JSON)));
    }

    @Test
    public void defaultCase() throws Exception {
        RedisConnectionFactory factory = mock(RedisConnectionFactory.class);
        Assert.assertNotNull(new CustomRedisAutoConfiguration().redisService(factory, props((byte) 9)));
    }

    @Test
    public void dsSelector() {
        Assert.assertNotNull(new CustomRedisAutoConfiguration().dsSelector(mock(org.springframework.beans.factory.BeanFactory.class)));
    }

    @Test
    public void dynamicService() {
        Assert.assertNotNull(new com.skrstop.framework.components.starter.redis.configuration.commonDynamic.DynamicRedisAutoConfiguration()
                .dynamicRedisService(new com.skrstop.framework.components.starter.redis.service.impl.RedisServiceImpl(
                        mock(org.springframework.data.redis.core.RedisTemplate.class), new com.skrstop.framework.components.starter.redis.filter.BinaryValueFilter())));
    }
}
