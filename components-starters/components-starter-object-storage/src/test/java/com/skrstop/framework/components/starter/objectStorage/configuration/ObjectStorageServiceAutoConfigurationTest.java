package com.skrstop.framework.components.starter.objectStorage.configuration;

import cn.hutool.extra.ftp.Ftp;
import com.skrstop.framework.components.starter.objectStorage.service.ObjectStorageService;
import com.skrstop.framework.components.starter.objectStorage.service.impl.CosObjectStorageServiceImpl;
import com.skrstop.framework.components.starter.objectStorage.service.impl.FtpObjectStorageServiceImpl;
import com.skrstop.framework.components.starter.objectStorage.service.impl.OssObjectStorageServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;


public class ObjectStorageServiceAutoConfigurationTest {

    @Test(expected = IllegalArgumentException.class)
    public void ftpAndCosBothEnabled() {
        ObjectStorageProperties properties = new ObjectStorageProperties();
        properties.getFtp().setEnable(true);
        properties.getCos().setEnable(true);
        new ObjectStorageServiceAutoConfiguration(properties);
    }

    @Test
    public void beans() {
        try (MockedConstruction<Ftp> ignored = Mockito.mockConstructionWithAnswer(Ftp.class, inv -> Mockito.mock(Ftp.class))) {
            ObjectStorageProperties properties = new ObjectStorageProperties();
            ObjectStorageServiceAutoConfiguration configuration =
                    new ObjectStorageServiceAutoConfiguration(properties);

            properties.getFtp().setEnable(true);
            ObjectStorageService ftp = configuration.ftpObjectStorage(properties);
            Assert.assertTrue(ftp instanceof FtpObjectStorageServiceImpl);

            properties.getCos().setSecretId("id");
            properties.getCos().setSecretKey("key");
            properties.getCos().setRegion("ap-beijing-1");
            properties.getCos().setEnable(true);
            ObjectStorageService cos = configuration.cosObjectStorage(properties);
            Assert.assertTrue(cos instanceof CosObjectStorageServiceImpl);

            properties.getOss().setEndpoint("oss-cn-beijing.aliyuncs.com");
            properties.getOss().setRegion("cn-beijing");
            ObjectStorageService oss = configuration.ossObjectStorage(properties);
            Assert.assertTrue(oss instanceof OssObjectStorageServiceImpl);
        }
    }

    @Test
    public void ftpEnabledOnly() {
        ObjectStorageProperties properties = new ObjectStorageProperties();
        properties.getFtp().setEnable(true);
        // cos 未启用 -> 不抛异常
        new ObjectStorageServiceAutoConfiguration(properties);
    }
}
