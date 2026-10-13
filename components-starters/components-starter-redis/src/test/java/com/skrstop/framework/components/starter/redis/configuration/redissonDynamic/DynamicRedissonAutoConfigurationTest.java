package com.skrstop.framework.components.starter.redis.configuration.redissonDynamic;

import com.skrstop.framework.components.starter.common.dsSelector.DsSelector;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.aop.Advisor;
import org.springframework.context.ApplicationContext;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

public class DynamicRedissonAutoConfigurationTest {

    @Test
    public void beans() {
        DynamicRedissonAutoConfiguration configuration = new DynamicRedissonAutoConfiguration();
        DsSelector dsSelector = mock(DsSelector.class);
        DynamicRedissonProperties properties = new DynamicRedissonProperties();
        Map<String, DynamicRedissonProperties.Config> ds = new LinkedHashMap<>();
        ds.put("default", new DynamicRedissonProperties.Config());
        properties.setDataSources(ds);

        Assert.assertNotNull(configuration.multiRedisson(properties, mock(ApplicationContext.class)));
        Advisor aop = configuration.dynamicRedissonDsRedissonAnnotationAdvisor(dsSelector, properties);
        Assert.assertNotNull(aop);
        Advisor client = configuration.dynamicRedissonClientAdvisor(dsSelector, properties);
        Assert.assertNotNull(client);
        MultiRedisson multiRedisson = configuration.multiRedisson(properties, mock(ApplicationContext.class));
        Assert.assertNotNull(configuration.dynamicAopRedissonClient(multiRedisson));
        Assert.assertNotNull(configuration.dynamicRedissonClient(multiRedisson));
    }
}
