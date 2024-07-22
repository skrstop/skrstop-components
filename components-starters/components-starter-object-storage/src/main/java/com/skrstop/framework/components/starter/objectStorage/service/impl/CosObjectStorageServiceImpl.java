package com.skrstop.framework.components.starter.objectStorage.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.URLUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.DeleteObjectsRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.Download;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import com.qcloud.cos.utils.UrlEncoderUtils;
import com.skrstop.framework.components.starter.objectStorage.configuration.CosProperties;
import com.skrstop.framework.components.starter.objectStorage.entity.CosStorageTemplateSign;
import com.skrstop.framework.components.starter.objectStorage.entity.StorageTemplateSign;
import com.skrstop.framework.components.starter.objectStorage.service.ObjectStorageService;
import com.skrstop.framework.components.util.enums.ContentTypeEnum;
import com.skrstop.framework.components.util.executor.ThreadPoolUtil;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.DateUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Response;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * FtpServiceImpl class
 *
 * @author 蒋时华
 * @date 2019/7/2
 */
@Slf4j
public class CosObjectStorageServiceImpl implements ObjectStorageService {

    @Getter
    private CosProperties cosProperties;
    @Getter
    private COSClient cosClient;
    @Getter
    private TransferManager transferManager;
    private ThreadPoolExecutor executor;
    @Getter
    private String basePath = "";

    public CosObjectStorageServiceImpl(CosProperties cosProperties) {
        this.cosProperties = cosProperties;
        if (StrUtil.isNotBlank(cosProperties.getBasePath())) {
            basePath = cosProperties.getBasePath();
        }
        if (StrUtil.isBlank(basePath)) {
            basePath = "/";
        }
        try {
            // 构建client
            COSCredentials cred = new BasicCOSCredentials(cosProperties.getSecretId(), cosProperties.getSecretKey());
            Region region = new Region(cosProperties.getRegion());
            ClientConfig clientConfig = new ClientConfig(region);
            clientConfig.setHttpProtocol(cosProperties.isUseHttpSsl() ? HttpProtocol.https : HttpProtocol.http);
            clientConfig.setRequestTimeOutEnable(true);
            clientConfig.setRequestTimeout(cosProperties.getRequestTimeout());
            this.cosClient = new COSClient(cred, clientConfig);
            if (!cosProperties.isUseMultiThread()) {
                this.transferManager = new TransferManager(this.cosClient);
                return;
            }
            executor = new ThreadPoolExecutor(
                    cosProperties.getThreadMin(),
                    cosProperties.getThreadMax(),
                    cosProperties.getThreadKeepAliveTime(),
                    TimeUnit.MINUTES,
                    ThreadPoolUtil.buildQueue(cosProperties.getThreadQueue()),
                    ThreadPoolUtil.policyAbort());
            this.transferManager = new TransferManager(this.cosClient, executor);
        } catch (Exception e) {
            throw new IllegalArgumentException("cos客户端创建失败");
        }
    }

    private String getOrDefaultBucketName(String bucketName) {
        bucketName = StrUtil.blankToDefault(bucketName, this.getDefaultBucketName());
        if (StrUtil.isBlank(bucketName)) {
            throw new IllegalArgumentException("bucketName不能为空");
        }
        return bucketName;
    }

    @Override
    public Object getClient() {
        return this.transferManager;
    }

    @Override
    public String getDefaultBucketName() {
        return this.cosProperties.getBucketName();
    }

    @Override
    public boolean upload(String bucketName, String targetPath, File file) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        try {
            targetPath = basePath + targetPath;
            Upload upload = this.transferManager.upload(bucketName, targetPath, file);
            upload.waitForUploadResult();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean upload(String bucketName, String targetPath, byte[] bytes) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        targetPath = basePath + targetPath;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            Upload upload = this.transferManager.upload(bucketName, targetPath, byteArrayInputStream, objectMetadata);
            upload.waitForUploadResult();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean upload(String bucketName, String targetPath, InputStream inputStream) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        targetPath = basePath + targetPath;
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            Upload upload = this.transferManager.upload(bucketName, targetPath, inputStream, objectMetadata);
            upload.waitForUploadResult();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return false;
    }

    @Override
    public boolean download(String bucketName, String targetPath, String localPath) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        targetPath = basePath + targetPath;
        return this.download(bucketName, targetPath, new File(localPath));
    }

    @Override
    public boolean download(String bucketName, String targetPath, File localFile) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        targetPath = basePath + targetPath;
        Download download = this.transferManager.download(bucketName, targetPath, localFile);
        try {
            download.waitForCompletion();
            return true;
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean exists(String bucketName, String targetPath) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        targetPath = basePath + targetPath;
        return this.cosClient.doesObjectExist(bucketName, targetPath);
    }

    @Override
    public boolean delete(String bucketName, String targetPath) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        targetPath = basePath + targetPath;
        try {
            this.cosClient.deleteObject(bucketName, targetPath);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean delete(String bucketName, List<String> targetPaths) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
        ArrayList<DeleteObjectsRequest.KeyVersion> keyList = new ArrayList<>();
        for (String targetPath : targetPaths) {
            targetPath = basePath + targetPath;
            keyList.add(new DeleteObjectsRequest.KeyVersion(targetPath));
        }
        deleteObjectsRequest.setKeys(keyList);
        try {
            this.cosClient.deleteObjects(deleteObjectsRequest);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean copy(String bucketName, String sourcePath, String targetPath) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        return this.copy(bucketName, sourcePath, bucketName, targetPath);
    }

    @Override
    public boolean copy(String sourceBucketName, String sourcePath, String targetBucketName, String targetPath) {
        try {
            this.transferManager.copy(sourceBucketName, sourcePath, targetBucketName, targetPath).waitForCopyResult();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean move(String bucketName, String sourcePath, String targetPath) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        boolean copy = this.copy(bucketName, sourcePath, bucketName, targetPath);
        if (!copy) {
            return false;
        }
        return this.delete(bucketName, sourcePath);
    }

    @Override
    public boolean move(String sourceBucketName, String sourcePath, String targetBucketName, String targetPath) {
        boolean copy = this.copy(sourceBucketName, sourcePath, targetBucketName, targetPath);
        if (!copy) {
            return false;
        }
        return this.delete(sourceBucketName, sourcePath);
    }

    @Override
    public String getTemporaryAccessUrl(String bucketName, String targetPath, long expireTime) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        LocalDateTime endTime = LocalDateTime.now().plusSeconds(expireTime);
        URL url = this.cosClient.generatePresignedUrl(bucketName, targetPath
                , DateUtil.toDate(endTime), HttpMethodName.GET
                , new HashMap<String, String>(), new HashMap<String, String>(), false, false);
        try {
            URI newUrl = new URI(StrUtil.blankToDefault(cosProperties.getAccessUrlProtocol(), url.getProtocol())
                    , url.getUserInfo()
                    , StrUtil.blankToDefault(cosProperties.getAccessUrlHost(), url.getHost())
                    , url.getPort()
                    , url.getPath()
                    , URLUtil.decode(url.getQuery())
                    , null
            );
            String result = newUrl.toString();
            result = result.replace(":80", "");
            result = result.replace(":443", "");
            return result;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> getTemporaryAccessUrl(String bucketName, List<String> targetPath, long expireTime) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        Map<String, String> result = new HashMap<>(targetPath.size(), 1);
        for (String path : targetPath) {
            result.put(path, this.getTemporaryAccessUrl(bucketName, path, expireTime));
        }
        return result;
    }

    @Override
    public String getPublicAccessUrl(String bucketName, String targetPath) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        if (StrUtil.isNotBlank(cosProperties.getAccessUrlHost())) {
            return this.cosProperties.getAccessUrlProtocol() + "://" + this.cosProperties.getAccessUrlHost() + targetPath;
        } else {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(this.cosClient.getClientConfig().getHttpProtocol().toString()).append("://");
            strBuilder.append(this.cosClient.getClientConfig().getEndpointBuilder()
                    .buildGeneralApiEndpoint(bucketName));
            strBuilder.append(UrlEncoderUtils.encodeUrlPath(targetPath));
            return strBuilder.toString();
        }
    }

    @Override
    public Map<String, String> getPublicAccessUrl(String bucketName, List<String> targetPath) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        Map<String, String> result = new HashMap<>(targetPath.size(), 1);
        for (String path : targetPath) {
            result.put(path, this.getPublicAccessUrl(bucketName, path));
        }
        return result;
    }

    @Override
    public <T extends StorageTemplateSign> T getTemporaryAccessSign(String bucketName, String targetPath, long expireSecondTime, Long minSize, Long maxSize, List<ContentTypeEnum> contentType) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        CosStorageTemplateSign sign = new CosStorageTemplateSign();
        try {
            TreeMap<String, Object> config = new TreeMap<>();
            config.put("secretId", cosProperties.getSecretId());
            config.put("secretKey", cosProperties.getSecretKey());
            // 临时密钥有效时长，单位是秒
            config.put("durationSeconds", expireSecondTime);
            // 换成你的 bucket
            config.put("bucket", bucketName);
            // 换成 bucket 所在地区
            config.put("region", cosProperties.getRegion());
            // 可以通过 allowPrefixes 指定前缀数组, 例子： a.jpg 或者 a/* 或者 * (使用通配符*存在重大安全风险, 请谨慎评估使用)
            config.put("allowPrefixes", CollectionUtil.newArrayList(targetPath));
            // 密钥的权限列表。简单上传和分片需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            config.put("allowActions", CollectionUtil.newArrayList(
                    // 简单上传
                    "name/cos:PutObject",
                    "name/cos:PostObject",
                    // 分片上传
                    "name/cos:InitiateMultipartUpload",
                    "name/cos:ListMultipartUploads",
                    "name/cos:ListParts",
                    "name/cos:UploadPart",
                    "name/cos:CompleteMultipartUpload"
            ));
            Map<String, Object> policy = new HashMap<>();
            Map<String, Object> policyCondition = new HashMap<>();
            policy.put("condition", policyCondition);
            if (ObjectUtil.isNotNull(minSize)) {
                // 限制大小
                policyCondition.put("numeric_greater_than_equal", MapUtil.builder("cos:content-length", minSize * 1024));
            }
            if (ObjectUtil.isNotNull(maxSize)) {
                // 限制大小
                policyCondition.put("numeric_less_than_equal", MapUtil.builder("cos:content-length", maxSize * 1024));
            }
            if (CollectionUtil.isNotEmpty(contentType)) {
                // 限制类型
                List<String> contentTypeStrList = contentType.stream().map(ContentTypeEnum::getContentType).collect(Collectors.toList());
                policyCondition.put("string_like", MapUtil.builder("cos:content-type", contentTypeStrList));
            }
            if (MapUtil.isNotEmpty(policyCondition)) {
                config.put("policy", policy);
            }
            Response response = CosStsClient.getCredential(config);
            sign.setTmpSecretId(response.credentials.tmpSecretId);
            sign.setTmpSecretKey(response.credentials.tmpSecretKey);
            sign.setSessionToken(response.credentials.sessionToken);
            sign.setExpireSecondTime(expireSecondTime);
            sign.setExpireDateTime(LocalDateTime.now().plusSeconds(expireSecondTime));
            return (T) sign;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        this.transferManager.shutdownNow(true);
    }
}
