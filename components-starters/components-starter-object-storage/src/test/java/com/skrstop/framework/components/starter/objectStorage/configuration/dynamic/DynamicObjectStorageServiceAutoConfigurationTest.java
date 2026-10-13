package com.skrstop.framework.components.starter.objectStorage.configuration.dynamic;

import com.skrstop.framework.components.starter.common.dsSelector.DsSelector;
import com.skrstop.framework.components.starter.objectStorage.configuration.CosProperties;
import com.skrstop.framework.components.starter.objectStorage.configuration.OssProperties;
import com.skrstop.framework.components.starter.objectStorage.service.ObjectStorageService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.BeanFactory;

import static org.mockito.Mockito.mock;

public class DynamicObjectStorageServiceAutoConfigurationTest {

    @Test
    public void beans() {
        DynamicObjectStorageServiceAutoConfiguration configuration =
                new DynamicObjectStorageServiceAutoConfiguration();

        BeanFactory beanFactory = mock(BeanFactory.class);
        DsSelector dsSelector = configuration.dsSelector(beanFactory);
        Assert.assertNotNull(dsSelector);

        // 无 ftp 数据源, 避免真实 FTP 连接
        DynamicObjectStorageProperties properties = new DynamicObjectStorageProperties();
        CosProperties cos = new CosProperties();
        cos.setSecretId("id");
        cos.setSecretKey("key");
        cos.setRegion("ap-beijing-1");
        properties.getCosDataSources().put("cosDs", cos);
        OssProperties oss = new OssProperties();
        oss.setEndpoint("oss-cn-beijing.aliyuncs.com");
        oss.setRegion("cn-beijing");
        properties.getOssDataSources().put("ossDs", oss);

        ObjectStorageService objectStorage = configuration.cosObjectStorage(properties);
        Assert.assertNotNull(objectStorage);

        Advisor aopAdvisor = configuration.dynamicObjectStorageAnnotationAdvisor(dsSelector, properties);
        Assert.assertNotNull(aopAdvisor);
        Advisor serviceAdvisor = configuration.dynamicObjectStorageServiceAdvisor(dsSelector, properties);
        Assert.assertNotNull(serviceAdvisor);

        ObjectStorageService storageService = Mockito.mock(ObjectStorageService.class);
        Object dynamicService = configuration.dynamicRedisService(storageService);
        Assert.assertNotNull(dynamicService);
    }
}
