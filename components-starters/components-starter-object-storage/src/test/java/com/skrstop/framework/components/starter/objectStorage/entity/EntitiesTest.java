package com.skrstop.framework.components.starter.objectStorage.entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class EntitiesTest {

    @Test
    public void cosSign() {
        CosStorageTemplateSign sign = new CosStorageTemplateSign();
        sign.setTmpSecretId("id");
        sign.setTmpSecretKey("key");
        sign.setSessionToken("token");
        sign.setToken("tk");
        sign.setBucketName("b");
        sign.setRegion("r");
        sign.setTargetPath("p");
        sign.setExpireSecondTime(1L);
        sign.setLimitMinSize(1L);
        sign.setLimitMaxSize(2L);
        sign.setLimitContentType(new ArrayList<>());
        Assert.assertEquals("id", sign.getTmpSecretId());
        Assert.assertEquals("key", sign.getTmpSecretKey());
        Assert.assertEquals("token", sign.getSessionToken());
        Assert.assertEquals("tk", sign.getToken());
        Assert.assertEquals("b", sign.getBucketName());
        Assert.assertEquals("r", sign.getRegion());
        Assert.assertEquals("p", sign.getTargetPath());
        Assert.assertEquals(Long.valueOf(1), sign.getExpireSecondTime());
        Assert.assertNotNull(sign.getLimitContentType());
        Assert.assertNotNull(sign.toString());
    }

    @Test
    public void ossSign() {
        OssStorageTemplateSign sign = new OssStorageTemplateSign();
        sign.setAccessKeyId("ak");
        sign.setPolicy("po");
        sign.setSignature("sig");
        sign.setBucketName("b");
        sign.setRegion("r");
        sign.setEndpoint("e");
        Assert.assertEquals("ak", sign.getAccessKeyId());
        Assert.assertEquals("po", sign.getPolicy());
        Assert.assertEquals("sig", sign.getSignature());
        Assert.assertEquals("b", sign.getBucketName());
        Assert.assertEquals("r", sign.getRegion());
        Assert.assertEquals("e", sign.getEndpoint());
    }

    @Test
    public void temporaryAccessExtraParam() {
        TemporaryAccessExtraParam param = new TemporaryAccessExtraParam();
        Assert.assertEquals(3600L, param.getExpireSecondTime());
        Assert.assertFalse(param.isUseOriginHost());
        param.setExpireSecondTime(1L);
        param.setUseOriginHost(true);
        param.setQueryParams(new HashMap<>());
        param.setHeaderParams(new HashMap<>());
        Assert.assertEquals(1L, param.getExpireSecondTime());
        Assert.assertTrue(param.isUseOriginHost());
        Assert.assertNotNull(param.getQueryParams());
        Assert.assertNotNull(param.getHeaderParams());

        TemporaryAccessExtraParam built = TemporaryAccessExtraParam.builder()
                .expireSecondTime(2L)
                .useOriginHost(true)
                .queryParams(new HashMap<>())
                .headerParams(new HashMap<>())
                .build();
        Assert.assertEquals(2L, built.getExpireSecondTime());
        Assert.assertTrue(built.isUseOriginHost());
    }

    @Test
    public void uploadLimit() {
        UploadLimit limit = new UploadLimit();
        Assert.assertEquals(Long.valueOf(3600L), limit.getExpireSecondTime());
        Assert.assertEquals(Boolean.FALSE, limit.getExactMatchTarget());
        Assert.assertNull(limit.getMinSize());
        Assert.assertNull(limit.getMaxSize());
        Assert.assertNull(limit.getContentType());
        limit.setMinSize(1L);
        limit.setMaxSize(2L);
        limit.setContentType(new ArrayList<>());
        Assert.assertNotNull(limit.getContentType());

        UploadLimit built = UploadLimit.builder()
                .minSize(1L)
                .maxSize(2L)
                .expireSecondTime(3L)
                .exactMatchTarget(true)
                .contentType(new ArrayList<>())
                .build();
        Assert.assertEquals(Long.valueOf(3L), built.getExpireSecondTime());
        Assert.assertEquals(Boolean.TRUE, built.getExactMatchTarget());
    }
}
