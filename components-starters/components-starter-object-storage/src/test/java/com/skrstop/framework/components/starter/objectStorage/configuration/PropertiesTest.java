package com.skrstop.framework.components.starter.objectStorage.configuration;

import com.skrstop.framework.components.starter.objectStorage.configuration.dynamic.DynamicObjectStorageProperties;
import org.junit.Assert;
import org.junit.Test;

public class PropertiesTest {

    @Test
    public void objectStorageProperties() {
        ObjectStorageProperties properties = new ObjectStorageProperties();
        Assert.assertNotNull(properties.getFtp());
        Assert.assertNotNull(properties.getCos());
        Assert.assertNotNull(properties.getOss());
    }

    @Test
    public void ftpProperties() {
        FtpProperties properties = new FtpProperties();
        properties.setHost("h");
        properties.setPort(21);
        properties.setUsername("u");
        properties.setPassword("p");
        properties.setBasePath("/bp");
        properties.setCharset("UTF-8");
        Assert.assertFalse(properties.isEnable());
        Assert.assertEquals("h", properties.getHost());
        Assert.assertEquals(21, properties.getPort());
        Assert.assertEquals("u", properties.getUsername());
        Assert.assertEquals("p", properties.getPassword());
        Assert.assertEquals("/bp", properties.getBasePath());
        Assert.assertEquals("UTF-8", properties.getCharset());
        Assert.assertNotNull(properties.getMode());
    }

    @Test
    public void cosProperties() {
        CosProperties properties = new CosProperties();
        properties.setSecretId("id");
        properties.setSecretKey("key");
        properties.setRegion("r");
        properties.setBucketName("b");
        properties.setAccessUrlHost("h");
        properties.setAccessUrlProtocol(null);
        properties.setUseHttpSsl(false);
        properties.setUseMultiThread(true);
        properties.setRequestTimeout(1);
        properties.setThreadMin(1);
        properties.setThreadMax(2);
        properties.setThreadKeepAliveTime(3);
        properties.setThreadQueue(4);
        properties.setMinimumUploadPartSize(1L);
        properties.setMultipartUploadThreshold(2L);
        properties.setMultipartCopyThreshold(3L);
        properties.setMultipartCopyPartSize(4L);
        properties.setEnable(true);
        Assert.assertEquals("id", properties.getSecretId());
        Assert.assertEquals("key", properties.getSecretKey());
        Assert.assertEquals("r", properties.getRegion());
        Assert.assertEquals("b", properties.getBucketName());
        Assert.assertEquals("h", properties.getAccessUrlHost());
        Assert.assertNull(properties.getAccessUrlProtocol());
        Assert.assertFalse(properties.isUseHttpSsl());
        Assert.assertTrue(properties.isUseMultiThread());
        Assert.assertEquals(Integer.valueOf(1), properties.getRequestTimeout());
        Assert.assertEquals(Integer.valueOf(1), properties.getThreadMin());
        Assert.assertEquals(Integer.valueOf(2), properties.getThreadMax());
        Assert.assertEquals(Integer.valueOf(3), properties.getThreadKeepAliveTime());
        Assert.assertEquals(Integer.valueOf(4), properties.getThreadQueue());
        Assert.assertEquals(Long.valueOf(1), properties.getMinimumUploadPartSize());
        Assert.assertEquals(Long.valueOf(2), properties.getMultipartUploadThreshold());
        Assert.assertEquals(Long.valueOf(3), properties.getMultipartCopyThreshold());
        Assert.assertEquals(Long.valueOf(4), properties.getMultipartCopyPartSize());
        Assert.assertTrue(properties.isEnable());
    }

    @Test
    public void ossProperties() {
        OssProperties properties = new OssProperties();
        properties.setAccessKeyId("ak");
        properties.setAccessKeySecret("sk");
        properties.setEndpoint("e");
        properties.setRegion("r");
        properties.setBucketName("b");
        properties.setAccessUrlHost("h");
        Assert.assertEquals("ak", properties.getAccessKeyId());
        Assert.assertEquals("sk", properties.getAccessKeySecret());
        Assert.assertEquals("e", properties.getEndpoint());
        Assert.assertEquals("r", properties.getRegion());
        Assert.assertEquals("b", properties.getBucketName());
        Assert.assertEquals("h", properties.getAccessUrlHost());
        Assert.assertNotNull(properties.getRequestTimeout());
        Assert.assertEquals("https", properties.getAccessUrlProtocol());
    }

    @Test
    public void dynamicObjectStorageProperties() {
        DynamicObjectStorageProperties properties = new DynamicObjectStorageProperties();
        Assert.assertFalse(properties.isEnable());
        Assert.assertEquals("default", properties.getPrimary());
        Assert.assertFalse(properties.isExceptionWhileNotFound());
        Assert.assertNotNull(properties.getAop());
        Assert.assertNotNull(properties.getService());
        Assert.assertNotNull(properties.getFtpDataSources());
        Assert.assertNotNull(properties.getCosDataSources());
        Assert.assertNotNull(properties.getOssDataSources());
        properties.setEnable(true);
        properties.setPrimary("p");
        properties.setExceptionWhileNotFound(true);

        DynamicObjectStorageProperties.Aop aop = DynamicObjectStorageProperties.Aop.builder()
                .enabled(false)
                .order(1)
                .allowedPublicOnly(false)
                .build();
        Assert.assertFalse(aop.isEnabled());
        Assert.assertEquals(Integer.valueOf(1), aop.getOrder());
        Assert.assertFalse(aop.isAllowedPublicOnly());
        aop.setEnabled(true).setOrder(2).setAllowedPublicOnly(true);
        Assert.assertTrue(aop.isEnabled());
        Assert.assertEquals(Integer.valueOf(2), aop.getOrder());
        Assert.assertTrue(aop.isAllowedPublicOnly());

        DynamicObjectStorageProperties.Service service = DynamicObjectStorageProperties.Service.builder()
                .enabled(false)
                .order(3)
                .build();
        Assert.assertFalse(service.isEnabled());
        Assert.assertEquals(Integer.valueOf(3), service.getOrder());
        service.setEnabled(true).setOrder(4);
        Assert.assertTrue(service.isEnabled());
        Assert.assertEquals(Integer.valueOf(4), service.getOrder());
    }
}
