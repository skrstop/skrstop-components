package com.skrstop.framework.components.starter.objectStorage.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.endpoint.EndpointBuilder;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.transfer.Copy;
import com.qcloud.cos.transfer.Download;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.skrstop.framework.components.starter.objectStorage.configuration.CosProperties;
import com.skrstop.framework.components.starter.objectStorage.entity.CosStorageTemplateSign;
import com.skrstop.framework.components.starter.objectStorage.entity.TemporaryAccessExtraParam;
import com.skrstop.framework.components.starter.objectStorage.entity.UploadLimit;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Credentials;
import com.tencent.cloud.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CosObjectStorageServiceImplTest {

    private CosProperties properties(boolean useMultiThread) {
        CosProperties properties = new CosProperties();
        properties.setSecretId("id");
        properties.setSecretKey("key");
        properties.setRegion("ap-beijing-1");
        properties.setBucketName("bucket-125000000");
        properties.setUseMultiThread(useMultiThread);
        return properties;
    }

    private CosObjectStorageServiceImpl service(CosProperties properties) {
        CosObjectStorageServiceImpl service = new CosObjectStorageServiceImpl(properties);
        COSClient cosClient = mock(COSClient.class);
        TransferManager transferManager = mock(TransferManager.class);
        ReflectionTestUtils.setField(service, "cosClient", cosClient);
        ReflectionTestUtils.setField(service, "transferManager", transferManager);
        return service;
    }

    @Test
    public void ctorBranches() {
        // 单线程分支
        CosObjectStorageServiceImpl single = new CosObjectStorageServiceImpl(properties(false));
        Assert.assertNotNull(single.getTransferManager());
        Assert.assertNotNull(single.getCosClient());
        // 多线程分支
        CosObjectStorageServiceImpl multi = new CosObjectStorageServiceImpl(properties(true));
        Assert.assertNotNull(multi.getTransferManager());
        // basePath 分支
        CosProperties withBase = properties(false);
        withBase.setBasePath("bp");
        Assert.assertEquals("bp", new CosObjectStorageServiceImpl(withBase).getBasePath());
        // 创建失败: secretId 为空 -> DynamicBasicCOSCredentials 抛出 -> 包装
        CosProperties bad = properties(false);
        bad.setSecretId(null);
        try {
            new CosObjectStorageServiceImpl(bad);
            Assert.fail("should throw");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("cos客户端创建失败", e.getMessage());
        }
        // useHttpSsl=false 分支
        CosProperties http = properties(false);
        http.setUseHttpSsl(false);
        Assert.assertNotNull(new CosObjectStorageServiceImpl(http));

        // cosProperties 为 null -> getBasePath NPE 在 try 外
        try {
            new CosObjectStorageServiceImpl(null);
            Assert.fail("should throw");
        } catch (NullPointerException e) {
            Assert.assertNotNull(e);
        }
    }

    @Test
    public void uploadAndDelete() throws Exception {
        CosObjectStorageServiceImpl service = service(properties(false));
        TransferManager transferManager = (TransferManager) service.getClient();
        COSClient cosClient = service.getCosClient();

        // upload(File)
        Upload upload = mock(Upload.class);
        when(transferManager.upload(anyString(), anyString(), any(File.class))).thenReturn(upload);
        Assert.assertTrue(service.upload("b", "t/f.txt", new File("f")));
        when(transferManager.upload(anyString(), anyString(), any(File.class)))
                .thenThrow(new RuntimeException("x"));
        Assert.assertFalse(service.upload("b", "t/f.txt", new File("f")));

        // upload(bytes)
        when(transferManager.upload(anyString(), anyString(), any(InputStream.class), any(ObjectMetadata.class)))
                .thenReturn(upload);
        Assert.assertTrue(service.upload("b", "t/f.txt", new byte[0]));
        when(transferManager.upload(anyString(), anyString(), any(InputStream.class), any(ObjectMetadata.class)))
                .thenThrow(new RuntimeException("x"));
        Assert.assertFalse(service.upload("b", "t/f.txt", new byte[0]));

        // upload(InputStream) 成功 / 失败
        when(transferManager.upload(anyString(), anyString(), any(InputStream.class), any(ObjectMetadata.class)))
                .thenReturn(upload);
        Assert.assertTrue(service.upload("b", "t/f.txt", new ByteArrayInputStream(new byte[0])));
        when(transferManager.upload(anyString(), anyString(), any(InputStream.class), any(ObjectMetadata.class)))
                .thenThrow(new RuntimeException("x"));
        Assert.assertFalse(service.upload("b", "t/f.txt", new ByteArrayInputStream(new byte[0])));

        // download(File) 成功 / InterruptedException
        Download download = mock(Download.class);
        when(transferManager.download(anyString(), anyString(), any(File.class))).thenReturn(download);
        Assert.assertTrue(service.download("b", "t/f.txt", new File("f")));
        Mockito.doThrow(new InterruptedException("i")).when(download).waitForCompletion();
        Assert.assertFalse(service.download("b", "t/f.txt", new File("f")));

        // download(String) 委托
        when(transferManager.download(anyString(), anyString(), any(File.class))).thenReturn(download);
        Mockito.doNothing().when(download).waitForCompletion();
        Assert.assertTrue(service.download("b", "t/f.txt", "local"));

        // downloadInputStream 成功 / 失败
        COSObject cosObject = mock(COSObject.class);
        when(cosObject.getObjectContent()).thenReturn(new com.qcloud.cos.model.COSObjectInputStream(new ByteArrayInputStream(new byte[0]), null));
        when(cosClient.getObject(anyString(), anyString())).thenReturn(cosObject);
        Assert.assertNotNull(service.downloadInputStream("b", "t/f.txt"));
        when(cosClient.getObject(anyString(), anyString())).thenThrow(new RuntimeException("x"));
        Assert.assertNull(service.downloadInputStream("b", "t/f.txt"));

        // exists
        when(cosClient.doesObjectExist(anyString(), anyString())).thenReturn(true);
        Assert.assertTrue(service.exists("b", "t"));

        // delete 单个 成功/失败
        Mockito.doNothing().when(cosClient).deleteObject(anyString(), anyString());
        Assert.assertTrue(service.delete("b", "t"));
        Mockito.doThrow(new RuntimeException("x")).when(cosClient).deleteObject(anyString(), anyString());
        Assert.assertFalse(service.delete("b", "t"));

        // delete 批量 成功/失败
        Mockito.doReturn(new com.qcloud.cos.model.DeleteObjectsResult(new java.util.ArrayList<>()))
                .when(cosClient).deleteObjects(any(com.qcloud.cos.model.DeleteObjectsRequest.class));
        Assert.assertTrue(service.delete("b", Arrays.asList("a", "b")));
        Mockito.doThrow(new RuntimeException("x")).when(cosClient).deleteObjects(any(com.qcloud.cos.model.DeleteObjectsRequest.class));
        Assert.assertFalse(service.delete("b", Arrays.asList("a", "b")));

        // copy 成功/失败
        Copy copy = mock(Copy.class);
        when(transferManager.copy(anyString(), anyString(), anyString(), anyString())).thenReturn(copy);
        Assert.assertTrue(service.copy("b", "s", "t"));
        Mockito.doThrow(new RuntimeException("x")).when(copy).waitForCopyResult();
        Assert.assertFalse(service.copy("b", "s", "t"));
        Assert.assertFalse(service.copy("b", "s", "b2", "t"));

        // move 成功 / copy 失败
        when(transferManager.copy(anyString(), anyString(), anyString(), anyString())).thenReturn(copy);
        Mockito.doReturn(new com.qcloud.cos.model.CopyResult()).when(copy).waitForCopyResult();
        Mockito.doNothing().when(cosClient).deleteObject(anyString(), anyString());
        Assert.assertTrue(service.move("b", "s", "t"));
        Mockito.doThrow(new RuntimeException("x")).when(copy).waitForCopyResult();
        Assert.assertFalse(service.move("b", "s", "t"));
        Assert.assertFalse(service.move("b", "s", "b2", "t"));

        // bucketName 为空 -> 默认 bucket
        Mockito.doNothing().when(cosClient).deleteObject(anyString(), anyString());
        Assert.assertTrue(service.delete(null, "t"));
    }

    private URL presignedUrl(String spec) throws Exception {
        return new URL(spec);
    }

    @Test
    public void temporaryAccessUrl() throws Exception {
        CosObjectStorageServiceImpl service = service(properties(false));
        COSClient cosClient = service.getCosClient();
        when(cosClient.generatePresignedUrl(anyString(), anyString(), any(java.util.Date.class)
                , any(com.qcloud.cos.http.HttpMethodName.class), anyMap(), anyMap(), anyBoolean(), anyBoolean()))
                .thenReturn(presignedUrl("https://bucket.cos.ap-beijing.myqcloud.com:443/p/a.txt?q=1"));

        // useOriginHost=true
        TemporaryAccessExtraParam param = TemporaryAccessExtraParam.builder()
                .expireSecondTime(10L)
                .useOriginHost(true)
                .build();
        String url = service.getTemporaryAccessUrl("b", "t/a.txt", param);
        Assert.assertTrue(url.contains("bucket.cos.ap-beijing.myqcloud.com"));

        // useOriginHost=false + accessUrlHost
        CosProperties withHost = properties(false);
        withHost.setAccessUrlHost("cdn.example.com");
        CosObjectStorageServiceImpl service2 = service(withHost);
        when(service2.getCosClient().generatePresignedUrl(anyString(), anyString(), any(java.util.Date.class)
                , any(com.qcloud.cos.http.HttpMethodName.class), anyMap(), anyMap(), anyBoolean(), anyBoolean()))
                .thenReturn(presignedUrl("https://bucket.cos.ap-beijing.myqcloud.com:443/p/a.txt?q=1"));
        TemporaryAccessExtraParam param2 = TemporaryAccessExtraParam.builder()
                .expireSecondTime(10L)
                .useOriginHost(false)
                .queryParams(new HashMap<>(Map.of("k", "v")))
                .headerParams(new HashMap<>(Map.of("h", "v")))
                .build();
        String url2 = service2.getTemporaryAccessUrl("b", "t/a.txt", param2);
        Assert.assertTrue(url2.contains("cdn.example.com"));

        // useOriginHost=false + accessUrlHost 为空 -> 原 host, 且 basePath 前缀分支
        CosProperties withBase = properties(false);
        withBase.setBasePath("bp/");
        CosObjectStorageServiceImpl service3 = service(withBase);
        when(service3.getCosClient().generatePresignedUrl(anyString(), anyString(), any(java.util.Date.class)
                , any(com.qcloud.cos.http.HttpMethodName.class), anyMap(), anyMap(), anyBoolean(), anyBoolean()))
                .thenReturn(presignedUrl("http://bucket.cos.ap-beijing.myqcloud.com:80/p/a.txt"));
        TemporaryAccessExtraParam param3 = TemporaryAccessExtraParam.builder().build();
        String url3 = service3.getTemporaryAccessUrl("b", "t/a.txt", param3);
        Assert.assertFalse(url3.contains(":80"));
        // 批量
        Assert.assertEquals(1, service3.getTemporaryAccessUrl("b", Arrays.asList("t/a.txt"), param3).size());
    }

    @Test
    public void publicAccessUrl() throws Exception {
        // 分支1: accessUrlHost 非空且不使用 origin
        CosProperties withHost = properties(false);
        withHost.setAccessUrlHost("cdn.example.com");
        withHost.setAccessUrlProtocol("https");
        CosObjectStorageServiceImpl service = service(withHost);
        String url = service.getPublicAccessUrl("b", "t/a.txt", false);
        Assert.assertEquals("https://cdn.example.com/t/a.txt", url);
        String urlWithSlash = service.getPublicAccessUrl("b", "/t/a.txt", false);
        Assert.assertEquals("https://cdn.example.com/t/a.txt", urlWithSlash);
        Assert.assertEquals(1, service.getPublicAccessUrl("b", Arrays.asList("t/a.txt"), false).size());

        // 分支2: 使用 client config
        CosObjectStorageServiceImpl service2 = service(properties(false));
        COSClient cosClient = service2.getCosClient();
        ClientConfig clientConfig = mock(ClientConfig.class);
        when(clientConfig.getHttpProtocol()).thenReturn(HttpProtocol.https);
        EndpointBuilder endpointBuilder = mock(EndpointBuilder.class);
        when(endpointBuilder.buildGeneralApiEndpoint(anyString())).thenReturn("bucket.cos.ap-beijing.myqcloud.com");
        when(clientConfig.getEndpointBuilder()).thenReturn(endpointBuilder);
        when(cosClient.getClientConfig()).thenReturn(clientConfig);
        String url2 = service2.getPublicAccessUrl("b", "t/a.txt", false);
        Assert.assertTrue(url2.startsWith("https://") && url2.contains("bucket.cos.ap-beijing.myqcloud.com") && url2.contains("a.txt"));
        String url3 = service2.getPublicAccessUrl("b", "/t/a.txt", false);
        Assert.assertTrue(url3.contains("bucket.cos.ap-beijing.myqcloud.com") && url3.contains("a.txt"));
        // bucketName 为空 -> 默认 bucket
        Assert.assertTrue(service2.getPublicAccessUrl(null, "/t/a.txt", false).contains("a.txt"));
    }

    @Test
    public void temporaryUploadSign() throws Exception {
        Response response = new Response();
        response.credentials = new Credentials();
        response.credentials.tmpSecretId = "tid";
        response.credentials.tmpSecretKey = "tkey";
        response.credentials.sessionToken = "st";
        response.credentials.token = "tk";
        try (MockedStatic<CosStsClient> sts = Mockito.mockStatic(CosStsClient.class)) {
            sts.when(() -> CosStsClient.getCredential(any(java.util.TreeMap.class)))
                    .thenReturn(response);

            // 用例1: basePath 非空, 限制大小/类型, 非精确匹配
            CosProperties withBase = properties(false);
            withBase.setBasePath("bp");
            CosObjectStorageServiceImpl service = service(withBase);
            UploadLimit limit = UploadLimit.builder()
                    .minSize(1L)
                    .maxSize(10L)
                    .contentType(Arrays.asList("image/png"))
                    .expireSecondTime(100L)
                    .exactMatchTarget(false)
                    .build();
            CosStorageTemplateSign sign = service.getTemporaryUploadSign("bucket-125000000", "t/a.txt", limit);
            Assert.assertEquals("tid", sign.getTmpSecretId());
            Assert.assertEquals("tkey", sign.getTmpSecretKey());
            Assert.assertEquals("st", sign.getSessionToken());
            Assert.assertEquals("tk", sign.getToken());
            Assert.assertEquals(Long.valueOf(1), sign.getLimitMinSize());
            Assert.assertEquals(Long.valueOf(10), sign.getLimitMaxSize());
            Assert.assertNotNull(sign.getLimitContentType());
            Assert.assertEquals(Long.valueOf(100), sign.getExpireSecondTime());
            Assert.assertNotNull(sign.getExpireDateTime());
            Assert.assertEquals("bpt/a.txt", sign.getTargetPath());
            Assert.assertEquals("bucket-125000000", sign.getBucketName());
            Assert.assertEquals("ap-beijing-1", sign.getRegion());

            // 用例2: 精确匹配 + targetPath 以 * 结尾 + 无大小/类型限制
            CosProperties plain = properties(false);
            CosObjectStorageServiceImpl service2 = service(plain);
            UploadLimit limit2 = UploadLimit.builder()
                    .expireSecondTime(100L)
                    .exactMatchTarget(true)
                    .build();
            CosStorageTemplateSign sign2 = service2.getTemporaryUploadSign("bucket-125000000", "t/a*", limit2);
            Assert.assertNotNull(sign2);

            // 用例3: 精确匹配 + targetPath 不以 * 结尾
            UploadLimit limit3 = UploadLimit.builder()
                    .expireSecondTime(100L)
                    .exactMatchTarget(true)
                    .build();
            CosStorageTemplateSign sign3 = service2.getTemporaryUploadSign("bucket-125000000", "t/a.txt", limit3);
            Assert.assertNotNull(sign3);

            // 异常分支
            sts.when(() -> CosStsClient.getCredential(any(java.util.TreeMap.class)))
                    .thenThrow(new RuntimeException("sts failed"));
            try {
                service2.getTemporaryUploadSign("bucket-125000000", "t/a.txt", limit3);
                Assert.fail("should throw");
            } catch (RuntimeException e) {
                Assert.assertNotNull(e.getCause());
            }
        }
    }

    @Test(expected = NotSupportedException.class)
    public void createSymlinkNotSupported() {
        service(properties(false)).createSymlink("b", "l", "t");
    }

    @Test
    public void closeAndGetters() {
        CosProperties properties = properties(false);
        CosObjectStorageServiceImpl service = new CosObjectStorageServiceImpl(properties);
        TransferManager transferManager = mock(TransferManager.class);
        ReflectionTestUtils.setField(service, "transferManager", transferManager);
        service.close();
        Mockito.verify(transferManager).shutdownNow(true);
        Assert.assertSame(properties, service.getCosProperties());
        Assert.assertEquals("bucket-125000000", service.getDefaultBucketName());
        Assert.assertNotNull(service.getClient());
    }

    @Test
    public void extraBranches() throws Exception {
        // bucketName 与入参均为空 -> bucketName不能为空
        CosProperties noBucket = properties(false);
        noBucket.setBucketName(null);
        CosObjectStorageServiceImpl noBucketService = new CosObjectStorageServiceImpl(noBucket);
        try {
            noBucketService.exists(null, "t");
            Assert.fail("should throw");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("bucketName不能为空", e.getMessage());
        }

        CosObjectStorageServiceImpl service = service(properties(false));
        TransferManager transferManager = (TransferManager) service.getClient();
        COSClient cosClient = service.getCosClient();
        Upload upload = mock(Upload.class);
        Copy copy = mock(Copy.class);

        // upload(InputStream): 成功 + close 抛 IOException
        when(transferManager.upload(anyString(), anyString(), any(InputStream.class), any(ObjectMetadata.class)))
                .thenReturn(upload);
        InputStream badClose = new InputStream() {
            @Override
            public int read() {
                return -1;
            }

            @Override
            public void close() throws IOException {
                throw new IOException("close failed");
            }
        };
        Assert.assertTrue(service.upload("b", "t/f.txt", badClose));

        // move 4参 成功路径
        when(transferManager.copy(anyString(), anyString(), anyString(), anyString())).thenReturn(copy);
        Mockito.doReturn(new com.qcloud.cos.model.CopyResult()).when(copy).waitForCopyResult();
        Mockito.doReturn(new com.qcloud.cos.model.DeleteObjectsResult(new java.util.ArrayList<>()))
                .when(cosClient).deleteObjects(any(com.qcloud.cos.model.DeleteObjectsRequest.class));
        Mockito.doNothing().when(cosClient).deleteObject(anyString(), anyString());
        Assert.assertTrue(service.move("b", "s", "b2", "t"));

        // URISyntaxException 分支: accessUrlProtocol 含非法字符 -> new URI 抛出
        CosProperties badProtocol = properties(false);
        badProtocol.setAccessUrlProtocol("ht tps");
        CosObjectStorageServiceImpl badService = service(badProtocol);
        when(badService.getCosClient().generatePresignedUrl(anyString(), anyString(), any(java.util.Date.class)
                , any(com.qcloud.cos.http.HttpMethodName.class), anyMap(), anyMap(), anyBoolean(), anyBoolean()))
                .thenReturn(new URL("https://host/p.txt"));
        try {
            badService.getTemporaryAccessUrl("b", "t/a.txt", TemporaryAccessExtraParam.builder().build());
            Assert.fail("should throw");
        } catch (RuntimeException e) {
            Assert.assertNotNull(e.getCause());
        }

        // getPublicAccessUrl: basePath 非空 + host 非空 + useOriginHost=true -> else 分支
        CosProperties withBaseHost = properties(false);
        withBaseHost.setBasePath("bp");
        withBaseHost.setAccessUrlHost("cdn.example.com");
        CosObjectStorageServiceImpl service2 = service(withBaseHost);
        ClientConfig clientConfig = mock(ClientConfig.class);
        when(clientConfig.getHttpProtocol()).thenReturn(HttpProtocol.https);
        EndpointBuilder endpointBuilder = mock(EndpointBuilder.class);
        when(endpointBuilder.buildGeneralApiEndpoint(anyString())).thenReturn("bucket.cos.ap-beijing.myqcloud.com");
        when(clientConfig.getEndpointBuilder()).thenReturn(endpointBuilder);
        when(service2.getCosClient().getClientConfig()).thenReturn(clientConfig);
        Assert.assertTrue(service2.getPublicAccessUrl("b", "t/a.txt", true).contains("a.txt"));
        // basePath 非空分支
        Assert.assertTrue(service2.getPublicAccessUrl("b", "t/a.txt", false).startsWith("https://cdn.example.com/bp"));
    }

    @Test
    public void extraSignBranches() throws Exception {
        Response response = new Response();
        response.credentials = new Credentials();
        try (MockedStatic<CosStsClient> sts = Mockito.mockStatic(CosStsClient.class)) {
            sts.when(() -> CosStsClient.getCredential(any(java.util.TreeMap.class)))
                    .thenReturn(response);
            CosObjectStorageServiceImpl service = service(properties(false));
            // targetPath 已以 / 开头: 不加前缀
            CosStorageTemplateSign slashSign = service.getTemporaryUploadSign("bucket-125000000", "/t/a.txt",
                    UploadLimit.builder().expireSecondTime(10L).build());
            Assert.assertNotNull(slashSign);
            // minSize=0/maxSize=0: 数值条件不生效; targetPath 以 * 结尾且非精确匹配
            UploadLimit limit = UploadLimit.builder()
                    .minSize(0L)
                    .maxSize(0L)
                    .expireSecondTime(10L)
                    .exactMatchTarget(false)
                    .build();
            CosStorageTemplateSign sign = service.getTemporaryUploadSign("bucket-125000000", "t/a*", limit);
            Assert.assertNull(sign.getLimitMinSize());
            Assert.assertNull(sign.getLimitMaxSize());
        }
    }
}
