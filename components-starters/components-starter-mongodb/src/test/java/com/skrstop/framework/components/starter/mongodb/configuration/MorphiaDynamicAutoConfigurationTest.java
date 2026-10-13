package com.skrstop.framework.components.starter.mongodb.configuration;

import com.mongodb.client.MongoClient;
import com.skrstop.framework.components.starter.common.dsSelector.DsSelector;
import com.skrstop.framework.components.starter.common.dsSelector.DsSpelExpressionSelector;
import com.skrstop.framework.components.starter.mongodb.configuration.dynamic.DynamicDatastore;
import com.skrstop.framework.components.starter.mongodb.configuration.dynamic.DynamicMongoClient;
import com.skrstop.framework.components.starter.mongodb.configuration.dynamic.DynamicMongodbProperties;
import com.skrstop.framework.components.starter.mongodb.configuration.dynamic.MorphiaDynamicAutoConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

public class MorphiaDynamicAutoConfigurationTest {

    @SuppressWarnings("unchecked")
    private static ObjectProvider<org.springframework.boot.ssl.SslBundles> emptyProvider() {
        ObjectProvider<org.springframework.boot.ssl.SslBundles> provider = Mockito.mock(ObjectProvider.class);
        Mockito.when(provider.getIfAvailable()).thenReturn(null);
        return provider;
    }

    @Test
    public void dsSelectorBean() {
        MorphiaDynamicAutoConfiguration configuration = new MorphiaDynamicAutoConfiguration();
        DsSelector selector = configuration.dsSelector(Mockito.mock(BeanFactory.class));
        Assert.assertTrue(selector instanceof DsSpelExpressionSelector);
    }

    @Test
    public void advisorBean() {
        MorphiaDynamicAutoConfiguration configuration = new MorphiaDynamicAutoConfiguration();
        DsSelector selector = configuration.dsSelector(Mockito.mock(BeanFactory.class));
        DynamicMongodbProperties properties = new DynamicMongodbProperties();
        Advisor advisor = configuration.dynamicRedisDsRedisAnnotationAdvisor(selector, properties);
        Assert.assertNotNull(advisor);
        Assert.assertEquals(properties.getAop().getOrder().intValue(), ((org.springframework.core.Ordered) advisor).getOrder());
    }

    @Test
    public void mongoAndDatastoreBeans() {
        MorphiaDynamicAutoConfiguration configuration = new MorphiaDynamicAutoConfiguration();
        DynamicMongodbProperties properties = new DynamicMongodbProperties();
        Environment environment = new MockEnvironment();
        MongoClient mongo = configuration.mongo(properties, environment, emptyProvider());
        Assert.assertTrue(mongo instanceof DynamicMongoClient);
        // 空数据源 -> datastore map 为空
        dev.morphia.Datastore datastore = configuration.datastore(mongo, properties, new GlobalMongodbProperties());
        Assert.assertTrue(datastore instanceof DynamicDatastore);
    }
}
