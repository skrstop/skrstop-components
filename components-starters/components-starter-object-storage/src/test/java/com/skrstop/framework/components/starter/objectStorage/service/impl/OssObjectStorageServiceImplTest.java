package com.skrstop.framework.components.starter.objectStorage.service.impl;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentials;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.VoidResult;
import com.skrstop.framework.components.starter.objectStorage.configuration.OssProperties;
import com.skrstop.framework.components.starter.objectStorage.entity.OssStorageTemplateSign;
import com.skrstop.framework.components.starter.objectStorage.entity.TemporaryAccessExtraParam;
import com.skrstop.framework.components.starter.objectStorage.entity.UploadLimit;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OssObjectStorageServiceImplTest {

    private OssProperties properties() {
        OssProperties properties = new OssProperties();
        properties.setAccessKeyId("ak");
        properties.setAccessKeySecret("sk");
        properties.setRegion("cn-beijing");
        properties.setEndpoint("oss-cn-beijing.aliyuncs.com");
        properties.setBucketName("bucket");
        return properties;
    }

    private OssObjectStorageServiceImpl service(OssProperties properties) {
        OssObjectStorageServiceImpl service = new OssObjectStorageServiceImpl(properties);
        OSSClient ossClient = mock(OSSClient.class);
        ReflectionTestUtils.setField(service, "ossClient", ossClient);
        return service;
    }

    @Test
    public void ctorBranches() {
        OssObjectStorageServiceImpl service = new OssObjectStorageServiceImpl(properties());
        Assert.assertNotNull(service.getOssClient());
        Assert.assertEquals("", service.getBasePath());
        OssProperties withBase = properties();
        withBase.setBasePath("bp");
        Assert.assertEquals("bp", new OssObjectStorageServiceImpl(withBase).getBasePath());
        // requestTimeout 为 null -> 拆箱 NPE -> 包装
        OssProperties bad = properties();
        bad.setRequestTimeout(null);
        try {
            new OssObjectStorageServiceImpl(bad);
            Assert.fail("should throw");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("oss客户端创建失败", e.getMessage());
        }
        try {
            new OssObjectStorageServiceImpl(null);
            Assert.fail("should throw");
        } catch (NullPointerException e) {
            Assert.assertNotNull(e);
        }
    }

    @Test
    public void uploadAndDelete() throws Exception {
        OssObjectStorageServiceImpl service = service(properties());
        OSSClient ossClient = service.getOssClient();

        Mockito.doReturn(new com.aliyun.oss.model.PutObjectResult()).when(ossClient).putObject(anyString(), anyString(), any(File.class));
        Assert.assertTrue(service.upload("b", "t/f.txt", new File("f")));
        Mockito.doThrow(new RuntimeException("x")).when(ossClient).putObject(anyString(), anyString(), any(File.class));
        Assert.assertFalse(service.upload("b", "t/f.txt", new File("f")));

        Mockito.doReturn(new com.aliyun.oss.model.PutObjectResult()).when(ossClient).putObject(anyString(), anyString(), any(InputStream.class));
        Assert.assertTrue(service.upload("b", "t/f.txt", new byte[0]));
        Mockito.doThrow(new RuntimeException("x")).when(ossClient).putObject(anyString(), anyString(), any(InputStream.class));
        Assert.assertFalse(service.upload("b", "t/f.txt", new byte[0]));

        Mockito.doReturn(new com.aliyun.oss.model.ObjectMetadata()).when(ossClient).getObject(any(com.aliyun.oss.model.GetObjectRequest.class), any(File.class));
        Assert.assertTrue(service.download("b", "t/f.txt", new File("f")));
        Mockito.doThrow(new RuntimeException("x")).when(ossClient).getObject(any(com.aliyun.oss.model.GetObjectRequest.class), any(File.class));
        Assert.assertFalse(service.download("b", "t/f.txt", new File("f")));
        Mockito.doReturn(new com.aliyun.oss.model.ObjectMetadata()).when(ossClient).getObject(any(com.aliyun.oss.model.GetObjectRequest.class), any(File.class));
        Assert.assertTrue(service.download("b", "t/f.txt", "local"));

        OSSObject ossObject = mock(OSSObject.class);
        when(ossObject.getObjectContent()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(ossClient.getObject(any(com.aliyun.oss.model.GetObjectRequest.class))).thenReturn(ossObject);
        Assert.assertNotNull(service.downloadInputStream("b", "t/f.txt"));
        when(ossClient.getObject(any(com.aliyun.oss.model.GetObjectRequest.class))).thenThrow(new RuntimeException("x"));
        Assert.assertNull(service.downloadInputStream("b", "t/f.txt"));

        when(ossClient.doesObjectExist(anyString(), anyString())).thenReturn(true);
        Assert.assertTrue(service.exists("b", "t"));

        Mockito.doReturn(new com.aliyun.oss.model.VoidResult()).when(ossClient).deleteObject(anyString(), anyString());
        Assert.assertTrue(service.delete("b", "t"));
        Mockito.doThrow(new RuntimeException("x")).when(ossClient).deleteObject(anyString(), anyString());
        Assert.assertFalse(service.delete("b", "t"));

        Mockito.doReturn(new com.aliyun.oss.model.DeleteObjectsResult(new java.util.ArrayList<>())).when(ossClient).deleteObjects(any(com.aliyun.oss.model.DeleteObjectsRequest.class));
        Assert.assertTrue(service.delete("b", Arrays.asList("a", "b")));
        Mockito.doThrow(new RuntimeException("x")).when(ossClient).deleteObjects(any(com.aliyun.oss.model.DeleteObjectsRequest.class));
        Assert.assertFalse(service.delete("b", Arrays.asList("a", "b")));

        Mockito.doReturn(new com.aliyun.oss.model.CopyObjectResult()).when(ossClient).copyObject(anyString(), anyString(), anyString(), anyString());
        Assert.assertTrue(service.copy("b", "s", "t"));
        Mockito.doThrow(new RuntimeException("x")).when(ossClient).copyObject(anyString(), anyString(), anyString(), anyString());
        Assert.assertFalse(service.copy("b", "s", "t"));
        Assert.assertFalse(service.copy("b", "s", "b2", "t"));

        Mockito.doReturn(new com.aliyun.oss.model.CopyObjectResult()).when(ossClient).copyObject(anyString(), anyString(), anyString(), anyString());
        Mockito.doReturn(new com.aliyun.oss.model.VoidResult()).when(ossClient).deleteObject(anyString(), anyString());
        Assert.assertTrue(service.move("b", "s", "t"));
        Mockito.doThrow(new RuntimeException("x")).when(ossClient).copyObject(anyString(), anyString(), anyString(), anyString());
        Assert.assertFalse(service.move("b", "s", "t"));
        Assert.assertFalse(service.move("b", "s", "b2", "t"));

        Mockito.doReturn(new com.aliyun.oss.model.VoidResult()).when(ossClient).deleteObject(anyString(), anyString());
        Assert.assertTrue(service.delete(null, "t"));
    }

    @Test
    public void temporaryAccessUrl() throws Exception {
        OssObjectStorageServiceImpl service = service(properties());
        OSSClient ossClient = service.getOssClient();
        when(ossClient.generatePresignedUrl(any(com.aliyun.oss.model.GeneratePresignedUrlRequest.class)))
                .thenReturn(new URL("https://bucket.oss-cn-beijing.aliyuncs.com:443/t/a.txt?q=1"));

        TemporaryAccessExtraParam param = TemporaryAccessExtraParam.builder()
                .expireSecondTime(10L)
                .useOriginHost(true)
                .build();
        String url = service.getTemporaryAccessUrl("b", "t/a.txt", param);
        Assert.assertTrue(url.contains("bucket.oss-cn-beijing.aliyuncs.com"));

        OssProperties withHost = properties();
        withHost.setAccessUrlHost("cdn.example.com");
        OssObjectStorageServiceImpl service2 = service(withHost);
        when(service2.getOssClient().generatePresignedUrl(any(com.aliyun.oss.model.GeneratePresignedUrlRequest.class)))
                .thenReturn(new URL("https://bucket.oss-cn-beijing.aliyuncs.com:443/t/a.txt?q=1"));
        TemporaryAccessExtraParam param2 = TemporaryAccessExtraParam.builder()
                .expireSecondTime(10L)
                .useOriginHost(false)
                .queryParams(new HashMap<String, Object>(Map.of("k", "v")))
                .headerParams(new HashMap<String, Object>(Map.of("h", "v")))
                .build();
        String url2 = service2.getTemporaryAccessUrl("b", "t/a.txt", param2);
        Assert.assertTrue(url2.contains("cdn.example.com"));

        OssProperties withBase = properties();
        withBase.setBasePath("bp/");
        OssObjectStorageServiceImpl service3 = service(withBase);
        when(service3.getOssClient().generatePresignedUrl(any(com.aliyun.oss.model.GeneratePresignedUrlRequest.class)))
                .thenReturn(new URL("http://bucket.oss-cn-beijing.aliyuncs.com:80/t/a.txt"));
        TemporaryAccessExtraParam param3 = TemporaryAccessExtraParam.builder().build();
        String url3 = service3.getTemporaryAccessUrl("b", "t/a.txt", param3);
        Assert.assertFalse(url3.contains(":80"));
        Assert.assertEquals(1, service3.getTemporaryAccessUrl("b", Arrays.asList("t/a.txt"), param3).size());
    }

    @Test
    public void publicAccessUrl() throws Exception {
        OssProperties withHost = properties();
        withHost.setAccessUrlHost("cdn.example.com");
        withHost.setAccessUrlProtocol("https");
        OssObjectStorageServiceImpl service = service(withHost);
        String url = service.getPublicAccessUrl("b", "t/a.txt", false);
        Assert.assertEquals("https://cdn.example.com/t/a.txt", url);
        String urlWithSlash = service.getPublicAccessUrl("b", "/t/a.txt", false);
        Assert.assertEquals("https://cdn.example.com/t/a.txt", urlWithSlash);
        Assert.assertEquals(1, service.getPublicAccessUrl("b", Arrays.asList("t/a.txt"), false).size());

        OssObjectStorageServiceImpl service2 = service(properties());
        OSSClient ossClient = service2.getOssClient();
        ClientConfiguration clientConfiguration = mock(ClientConfiguration.class);
        when(clientConfiguration.getProtocol()).thenReturn(Protocol.HTTPS);
        when(ossClient.getClientConfiguration()).thenReturn(clientConfiguration);
        String url2 = service2.getPublicAccessUrl("b", "t/a.txt", false);
        Assert.assertEquals("https://b.oss-cn-beijing.aliyuncs.com/t/a.txt", url2);
        String url3 = service2.getPublicAccessUrl("b", "/t/a.txt", false);
        Assert.assertEquals("https://b.oss-cn-beijing.aliyuncs.com/t/a.txt", url3);
        Assert.assertEquals("https://bucket.oss-cn-beijing.aliyuncs.com/t/a.txt",
                service2.getPublicAccessUrl(null, "/t/a.txt", false));
    }

    @Test
    public void temporaryUploadSign() throws Exception {
        OssObjectStorageServiceImpl service = service(properties());
        OSSClient ossClient = service.getOssClient();
        when(ossClient.generatePostPolicy(any(java.util.Date.class), any(com.aliyun.oss.model.PolicyConditions.class)))
                .thenReturn("policy");
        when(ossClient.calculatePostSignature(anyString())).thenReturn("sig");
        CredentialsProvider credentialsProvider = mock(CredentialsProvider.class);
        when(credentialsProvider.getCredentials()).thenReturn(new DefaultCredentials("ak", "sk", null));
        when(ossClient.getCredentialsProvider()).thenReturn(credentialsProvider);

        // 用例1: 0 <= min <= max + contentType + 非精确匹配
        OssProperties withBase = properties();
        withBase.setBasePath("bp");
        OssObjectStorageServiceImpl serviceWithBase = service(withBase);
        when(serviceWithBase.getOssClient().generatePostPolicy(any(java.util.Date.class), any(com.aliyun.oss.model.PolicyConditions.class)))
                .thenReturn("policy");
        when(serviceWithBase.getOssClient().calculatePostSignature(anyString())).thenReturn("sig");
        CredentialsProvider credentialsProvider2 = mock(CredentialsProvider.class);
        when(credentialsProvider2.getCredentials()).thenReturn(new DefaultCredentials("ak", "sk", null));
        when(serviceWithBase.getOssClient().getCredentialsProvider()).thenReturn(credentialsProvider2);
        UploadLimit limit = UploadLimit.builder()
                .minSize(0L)
                .maxSize(10L)
                .contentType(Arrays.asList("image/png"))
                .expireSecondTime(100L)
                .exactMatchTarget(false)
                .build();
        OssStorageTemplateSign sign = serviceWithBase.getTemporaryUploadSign("bucket", "t/a.txt", limit);
        Assert.assertEquals("ak", sign.getAccessKeyId());
        Assert.assertEquals("cG9saWN5", sign.getPolicy());
        Assert.assertEquals("sig", sign.getSignature());
        Assert.assertEquals(Long.valueOf(0), sign.getLimitMinSize());
        Assert.assertEquals(Long.valueOf(10), sign.getLimitMaxSize());
        Assert.assertNotNull(sign.getLimitContentType());
        Assert.assertEquals("bpt/a.txt", sign.getTargetPath());
        Assert.assertEquals("bucket", sign.getBucketName());
        Assert.assertEquals("cn-beijing", sign.getRegion());
        Assert.assertEquals("oss-cn-beijing.aliyuncs.com", sign.getEndpoint());
        Assert.assertNotNull(sign.getExpireDateTime());

        // 用例2: 精确匹配, min > 0, 无 max
        UploadLimit limit2 = UploadLimit.builder()
                .minSize(5L)
                .expireSecondTime(100L)
                .exactMatchTarget(true)
                .build();
        OssStorageTemplateSign sign2 = service.getTemporaryUploadSign("bucket", "t/a.txt", limit2);
        Assert.assertEquals(Long.valueOf(5), sign2.getLimitMinSize());
        Assert.assertNull(sign2.getLimitMaxSize());

        // 异常分支
        when(ossClient.generatePostPolicy(any(java.util.Date.class), any(com.aliyun.oss.model.PolicyConditions.class)))
                .thenThrow(new RuntimeException("x"));
        try {
            service.getTemporaryUploadSign("bucket", "t/a.txt", limit2);
            Assert.fail("should throw");
        } catch (RuntimeException e) {
            Assert.assertNotNull(e.getCause());
        }
    }

    @Test
    public void createSymlinkAndClose() throws Exception {
        OssObjectStorageServiceImpl service = service(properties());
        OSSClient ossClient = service.getOssClient();
        Mockito.doReturn(new VoidResult()).when(ossClient).createSymlink(anyString(), anyString(), anyString());
        Assert.assertTrue(service.createSymlink("b", "l", "t"));
        Mockito.doThrow(new RuntimeException("x")).when(ossClient).createSymlink(anyString(), anyString(), anyString());
        Assert.assertFalse(service.createSymlink("b", "l", "t"));

        Mockito.doNothing().when(ossClient).shutdown();
        service.close();
        Mockito.verify(ossClient).shutdown();
        Assert.assertNotNull(service.getClient());
        Assert.assertEquals("bucket", service.getDefaultBucketName());
    }

    @Test
    public void extraBranches() throws Exception {
        // bucketName 与入参均为空 -> bucketName不能为空
        OssProperties noBucket = properties();
        noBucket.setBucketName(null);
        OssObjectStorageServiceImpl noBucketService = new OssObjectStorageServiceImpl(noBucket);
        try {
            noBucketService.exists(null, "t");
            Assert.fail("should throw");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("bucketName不能为空", e.getMessage());
        }

        OssObjectStorageServiceImpl service = service(properties());
        OSSClient ossClient = service.getOssClient();

        // upload(InputStream) 成功
        Mockito.doReturn(new com.aliyun.oss.model.PutObjectResult()).when(ossClient).putObject(anyString(), anyString(), any(InputStream.class));
        Assert.assertTrue(service.upload("b", "t/f.txt", new ByteArrayInputStream(new byte[0])));

        // move 4参 成功路径
        Mockito.doReturn(new com.aliyun.oss.model.CopyObjectResult()).when(ossClient).copyObject(anyString(), anyString(), anyString(), anyString());
        Mockito.doReturn(new com.aliyun.oss.model.VoidResult()).when(ossClient).deleteObject(anyString(), anyString());
        Assert.assertTrue(service.move("b", "s", "b2", "t"));

        // upload(InputStream) 成功
        Mockito.doReturn(new com.aliyun.oss.model.PutObjectResult()).when(ossClient).putObject(anyString(), anyString(), any(InputStream.class));
        Assert.assertTrue(service.upload("b", "t/f.txt", new ByteArrayInputStream(new byte[0])));

        // upload(InputStream): 抛异常 + close 抛异常
        Mockito.doThrow(new RuntimeException("x")).when(ossClient).putObject(anyString(), anyString(), any(InputStream.class));
        InputStream badClose = new InputStream() {
            @Override
            public int read() {
                return -1;
            }

            @Override
            public void close() throws java.io.IOException {
                throw new java.io.IOException("close failed");
            }
        };
        Assert.assertFalse(service.upload("b", "t/f.txt", badClose));

        // URISyntaxException 分支: accessUrlProtocol 含非法字符 -> new URI 抛出
        OssProperties badProtocol = properties();
        badProtocol.setAccessUrlProtocol("ht tps");
        OssObjectStorageServiceImpl badService = new OssObjectStorageServiceImpl(badProtocol);
        OSSClient badClient = mock(OSSClient.class);
        org.springframework.test.util.ReflectionTestUtils.setField(badService, "ossClient", badClient);
        when(badClient.generatePresignedUrl(any(com.aliyun.oss.model.GeneratePresignedUrlRequest.class)))
                .thenReturn(new URL("https://host/p.txt"));
        try {
            badService.getTemporaryAccessUrl("b", "t/a.txt", TemporaryAccessExtraParam.builder().build());
            Assert.fail("should throw");
        } catch (RuntimeException e) {
            Assert.assertNotNull(e.getCause());
        }

        // getPublicAccessUrl: basePath 非空 + host 非空 + useOriginHost=true -> else 分支
        OssProperties withBaseHost = properties();
        withBaseHost.setBasePath("bp");
        withBaseHost.setAccessUrlHost("cdn.example.com");
        OssObjectStorageServiceImpl service2 = service(withBaseHost);
        OSSClient ossClient2 = service2.getOssClient();
        ClientConfiguration clientConfiguration = mock(ClientConfiguration.class);
        when(clientConfiguration.getProtocol()).thenReturn(Protocol.HTTPS);
        when(ossClient2.getClientConfiguration()).thenReturn(clientConfiguration);
        Assert.assertTrue(service2.getPublicAccessUrl("b", "t/a.txt", true).contains("a.txt"));
        Assert.assertTrue(service2.getPublicAccessUrl("b", "t/a.txt", false).startsWith("https://cdn.example.com/bp"));
    }

    @Test
    public void extraSignBranches() throws Exception {
        OssObjectStorageServiceImpl service = service(properties());
        OSSClient ossClient = service.getOssClient();
        when(ossClient.generatePostPolicy(any(java.util.Date.class), any(com.aliyun.oss.model.PolicyConditions.class)))
                .thenReturn("policy");
        when(ossClient.calculatePostSignature(anyString())).thenReturn("sig");
        CredentialsProvider credentialsProvider = mock(CredentialsProvider.class);
        when(credentialsProvider.getCredentials()).thenReturn(new DefaultCredentials("ak", "sk", null));
        when(ossClient.getCredentialsProvider()).thenReturn(credentialsProvider);

        // max=0: maxSize>0 false
        OssStorageTemplateSign sign0 = service.getTemporaryUploadSign("bucket", "t/a.txt",
                UploadLimit.builder().maxSize(0L).expireSecondTime(10L).build());
        Assert.assertNotNull(sign0);

        // min=null,max=1: 第一个条件 false, 第二个 true, 第三个 false
        OssStorageTemplateSign sign = service.getTemporaryUploadSign("bucket", "t/a.txt",
                UploadLimit.builder().maxSize(1L).expireSecondTime(10L).build());
        Assert.assertNull(sign.getLimitMinSize());
        // min=-1,max=null: min>=0 false
        sign = service.getTemporaryUploadSign("bucket", "t/a.txt",
                UploadLimit.builder().minSize(-1L).expireSecondTime(10L).build());
        Assert.assertNull(sign.getLimitMaxSize());
        // min=5,max=1: max>=min false
        sign = service.getTemporaryUploadSign("bucket", "t/a.txt",
                UploadLimit.builder().minSize(5L).maxSize(1L).expireSecondTime(10L).build());
        Assert.assertNotNull(sign);
    }
}
