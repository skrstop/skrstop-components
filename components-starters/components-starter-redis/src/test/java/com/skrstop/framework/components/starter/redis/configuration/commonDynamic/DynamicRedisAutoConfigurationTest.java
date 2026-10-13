package com.skrstop.framework.components.starter.redis.configuration.commonDynamic;

import com.skrstop.framework.components.starter.common.dsSelector.DsSelector;
import com.skrstop.framework.components.starter.redis.service.RedisService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.aop.Advisor;

import static org.mockito.Mockito.mock;

public class DynamicRedisAutoConfigurationTest {

    @Test
    @SuppressWarnings("unchecked")
    public void beans() {
        DynamicRedisAutoConfiguration configuration = new DynamicRedisAutoConfiguration();
        DsSelector dsSelector = mock(DsSelector.class);
        DynamicRedisProperties properties = new DynamicRedisProperties();
        Advisor aop = configuration.dynamicRedisDsRedisAnnotationAdvisor(dsSelector, properties);
        Assert.assertNotNull(aop);
        Advisor service = configuration.dynamicRedisServiceAdvisor(dsSelector, properties);
        Assert.assertNotNull(service);
        RedisService redisService = mock(RedisService.class);
        Assert.assertNotNull(configuration.dynamicRedisService(redisService));
    }
}
