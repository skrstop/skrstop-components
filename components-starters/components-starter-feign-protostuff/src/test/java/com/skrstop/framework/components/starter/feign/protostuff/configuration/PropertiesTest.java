package com.skrstop.framework.components.starter.feign.protostuff.configuration;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class PropertiesTest {

    @Test
    public void feignOkHttpProperties() {
        FeignOkHttpProperties properties = new FeignOkHttpProperties();
        Assert.assertTrue(properties.isDisableSslValidation());
        Assert.assertEquals(200, properties.getMaxConnections());
        Assert.assertEquals(900L, properties.getTimeToLive());
        Assert.assertEquals(TimeUnit.SECONDS, properties.getTimeToLiveUnit());
        Assert.assertTrue(properties.isFollowRedirects());
        Assert.assertEquals(2000, properties.getConnectionTimeout());
        Assert.assertEquals(3000, properties.getReadTimeout());
        Assert.assertEquals(3000, properties.getWriteTimeout());
        Assert.assertFalse(properties.isLogInfoLevelForRequest());
        properties.setDisableSslValidation(false);
        properties.setMaxConnections(1);
        properties.setTimeToLive(2L);
        properties.setTimeToLiveUnit(TimeUnit.MINUTES);
        properties.setFollowRedirects(false);
        properties.setConnectionTimeout(3);
        properties.setReadTimeout(4);
        properties.setWriteTimeout(5);
        properties.setLogInfoLevelForRequest(true);
        Assert.assertFalse(properties.isDisableSslValidation());
        Assert.assertEquals(1, properties.getMaxConnections());
        Assert.assertEquals(2L, properties.getTimeToLive());
        Assert.assertEquals(TimeUnit.MINUTES, properties.getTimeToLiveUnit());
        Assert.assertFalse(properties.isFollowRedirects());
        Assert.assertEquals(3, properties.getConnectionTimeout());
        Assert.assertEquals(4, properties.getReadTimeout());
        Assert.assertEquals(5, properties.getWriteTimeout());
        Assert.assertTrue(properties.isLogInfoLevelForRequest());
    }

    @Test
    public void globalFeignProperties() {
        GlobalFeignProperties properties = new GlobalFeignProperties();
        Assert.assertFalse(properties.isEnableHttp2());
        Assert.assertNull(properties.getScanPackage());
        Assert.assertFalse(properties.isStopAutoRemoveGlobalResponse());
        Assert.assertTrue(properties.isLogErrorAutoRemoveGlobalResponse());
        Assert.assertEquals(Boolean.TRUE, properties.getGzipCompress());
        Assert.assertEquals(Boolean.TRUE, properties.getTransferHeader());
        Assert.assertTrue(properties.getTransferHeaderIgnore().contains("content-type"));
        Assert.assertEquals(7, properties.getTransferHeaderIgnore().size());
        properties.setEnableHttp2(true);
        properties.setScanPackage("a,b");
        properties.setStopAutoRemoveGlobalResponse(true);
        properties.setLogErrorAutoRemoveGlobalResponse(false);
        properties.setGzipCompress(false);
        properties.setTransferHeader(false);
        properties.setTransferHeaderIgnore(new HashSet<>());
        Assert.assertTrue(properties.isEnableHttp2());
        Assert.assertEquals("a,b", properties.getScanPackage());
        Assert.assertTrue(properties.isStopAutoRemoveGlobalResponse());
        Assert.assertFalse(properties.isLogErrorAutoRemoveGlobalResponse());
        Assert.assertEquals(Boolean.FALSE, properties.getGzipCompress());
        Assert.assertEquals(Boolean.FALSE, properties.getTransferHeader());
        Assert.assertTrue(properties.getTransferHeaderIgnore().isEmpty());
    }
}
