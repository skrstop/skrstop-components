package com.skrstop.framework.components.starter.objectStorage.service.impl;

import cn.hutool.core.map.MapUtil;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import com.skrstop.framework.components.starter.objectStorage.configuration.OssProperties;
import com.skrstop.framework.components.starter.objectStorage.entity.OssStorageTemplateSign;
import com.skrstop.framework.components.starter.objectStorage.entity.StorageTemplateSign;
import com.skrstop.framework.components.starter.objectStorage.entity.TemporaryAccessExtraParam;
import com.skrstop.framework.components.starter.objectStorage.entity.UploadLimit;
import com.skrstop.framework.components.starter.objectStorage.service.ObjectStorageService;
import com.skrstop.framework.components.util.enums.CharSetEnum;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.DateUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * ossServiceImpl class
 *
 * @author 蒋时华
 * @date 2019/7/2
 */
@Slf4j
public class OssObjectStorageServiceImpl implements ObjectStorageService {

    @Getter
    private OssProperties ossProperties;
    @Getter
    private OSSClient ossClient;
    private ThreadPoolExecutor executor;
    @Getter
    private String basePath = "";

    public OssObjectStorageServiceImpl(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
        if (StrUtil.isNotBlank(ossProperties.getBasePath())) {
            basePath = ossProperties.getBasePath();
        }
        if (StrUtil.isBlank(basePath)) {
            basePath = "";
        }
        try {
            // 构建client
            // credentials
            CredentialsProvider credentialsProvider = new DefaultCredentialProvider(ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
            // config
            ClientConfiguration config = new ClientConfiguration();
            config.setRequestTimeout(ossProperties.getRequestTimeout());
            config.setConnectionTimeout(ossProperties.getRequestTimeout());
            config.setSocketTimeout(ossProperties.getRequestTimeout());
            config.setRequestTimeoutEnabled(true);
            // client
            this.ossClient = (OSSClient) OSSClientBuilder.create()
                    .region(ossProperties.getRegion())
                    .endpoint(ossProperties.getEndpoint())
                    .clientConfiguration(config)
                    .credentialsProvider(credentialsProvider)
                    .build();
//            if (!ossProperties.isUseMultiThread()) {
//                this.transferManager = new TransferManager(this.cosClient);
//                return;
//            }
//            executor = new ThreadPoolExecutor(
//                    ossProperties.getThreadMin(),
//                    ossProperties.getThreadMax(),
//                    ossProperties.getThreadKeepAliveTime(),
//                    TimeUnit.MINUTES,
//                    ThreadPoolUtil.buildQueue(ossProperties.getThreadQueue()),
//                    ThreadPoolUtil.policyAbort());
//            this.transferManager = new TransferManager(this.cosClient, executor);
        } catch (Exception e) {
            throw new IllegalArgumentException("oss客户端创建失败");
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
        return this.ossClient;
    }

    @Override
    public String getDefaultBucketName() {
        return this.ossProperties.getBucketName();
    }

    @Override
    public boolean upload(String bucketName, String targetPath, File file) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        try {
            targetPath = basePath + targetPath;
            this.ossClient.putObject(bucketName, targetPath, file);
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
            this.ossClient.putObject(bucketName, targetPath, byteArrayInputStream);
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
            this.ossClient.putObject(bucketName, targetPath, inputStream);
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
        try {
            this.ossClient.getObject(new GetObjectRequest(bucketName, targetPath), localFile);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean exists(String bucketName, String targetPath) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        targetPath = basePath + targetPath;
        return this.ossClient.doesObjectExist(bucketName, targetPath);
    }

    @Override
    public boolean delete(String bucketName, String targetPath) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        targetPath = basePath + targetPath;
        try {
            this.ossClient.deleteObject(bucketName, targetPath);
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
        List<String> keyList = targetPaths.stream()
                .map(targetPath -> basePath + targetPath)
                .collect(Collectors.toList());
        deleteObjectsRequest.setKeys(keyList);
        deleteObjectsRequest.setBucketName(bucketName);
        try {
            this.ossClient.deleteObjects(deleteObjectsRequest);
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
            this.ossClient.copyObject(sourceBucketName, sourcePath, targetBucketName, targetPath);
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
    public String getTemporaryAccessUrl(String bucketName, String targetPath, TemporaryAccessExtraParam extraParam) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        LocalDateTime endTime = LocalDateTime.now().plusSeconds(extraParam.getExpireSecondTime());
        HashMap<String, String> requestParams = new HashMap<>();
        HashMap<String, String> requestHeaderParams = new HashMap<>();
        if (MapUtil.isNotEmpty(extraParam.getQueryParams())) {
            extraParam.getQueryParams().forEach((key, val) -> requestParams.put(key, ObjectUtil.defaultIfNull(val, "").toString()));
        }
        if (MapUtil.isNotEmpty(extraParam.getHeaderParams())) {
            extraParam.getHeaderParams().forEach((key, val) -> requestHeaderParams.put(key, ObjectUtil.defaultIfNull(val, "").toString()));
        }
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, targetPath, HttpMethod.GET);
        request.setExpiration(DateUtil.toDate(endTime));
        request.setQueryParameter(requestParams);
        request.setHeaders(requestHeaderParams);
        try {
            URL url = this.ossClient.generatePresignedUrl(request);
            URI newUrl = new URI(StrUtil.blankToDefault(ossProperties.getAccessUrlProtocol(), url.getProtocol())
                    , url.getUserInfo()
                    , extraParam.isUseOriginHost() ? url.getHost() : StrUtil.blankToDefault(ossProperties.getAccessUrlHost(), url.getHost())
                    , url.getPort()
                    , url.getPath()
                    , url.getQuery()
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
    public Map<String, String> getTemporaryAccessUrl(String bucketName, List<String> targetPath, TemporaryAccessExtraParam extraParam) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        Map<String, String> result = new HashMap<>(targetPath.size(), 1);
        for (String path : targetPath) {
            result.put(path, this.getTemporaryAccessUrl(bucketName, path, extraParam));
        }
        return result;
    }

    @Override
    public String getPublicAccessUrl(String bucketName, String targetPath, boolean useOriginHost) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        if (StrUtil.isNotBlank(ossProperties.getAccessUrlHost()) && !useOriginHost) {
            if (!targetPath.startsWith("/")) {
                targetPath = "/" + targetPath;
            }
            return this.ossProperties.getAccessUrlProtocol() + "://" + this.ossProperties.getAccessUrlHost() + targetPath;
        } else {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(this.ossClient.getClientConfiguration().getProtocol().toString()).append("://");
            strBuilder.append(bucketName);
            strBuilder.append(".oss-");
            strBuilder.append(this.ossProperties.getRegion());
            if (targetPath.startsWith("/")) {
                strBuilder.append(".aliyuncs.com");
            } else {
                strBuilder.append(".aliyuncs.com/");
            }
            strBuilder.append(targetPath);
            return strBuilder.toString();
        }
    }

    @Override
    public Map<String, String> getPublicAccessUrl(String bucketName, List<String> targetPath, boolean useOriginHost) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        Map<String, String> result = new HashMap<>(targetPath.size(), 1);
        for (String path : targetPath) {
            result.put(path, this.getPublicAccessUrl(bucketName, path, useOriginHost));
        }
        return result;
    }

    @Override
    public <T extends StorageTemplateSign> T getTemporaryUploadSign(String bucketName, String targetPath, UploadLimit uploadLimit) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        OssStorageTemplateSign sign = new OssStorageTemplateSign();
        try {
            PolicyConditions policyConditions = new PolicyConditions();
            if (uploadLimit.getExactMatchTarget()) {
                policyConditions.addConditionItem(MatchMode.Exact, PolicyConditions.COND_KEY, targetPath);
            } else {
                policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, targetPath);
            }
            if (ObjectUtil.isNotNull(uploadLimit.getMinSize())
                    && uploadLimit.getMinSize() >= 0
                    && ObjectUtil.isNotNull(uploadLimit.getMaxSize())
                    && uploadLimit.getMaxSize() >= uploadLimit.getMinSize()) {
                policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, uploadLimit.getMinSize(), uploadLimit.getMaxSize());
                sign.setLimitMinSize(uploadLimit.getMinSize());
                sign.setLimitMaxSize(uploadLimit.getMaxSize());
            }
            if (ObjectUtil.isNotNull(uploadLimit.getMaxSize())
                    && uploadLimit.getMaxSize() > 0) {
                policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, uploadLimit.getMaxSize());
                sign.setLimitMaxSize(uploadLimit.getMaxSize());
            }
            if (ObjectUtil.isNotNull(uploadLimit.getMinSize())
                    && uploadLimit.getMinSize() > 0) {
                policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, uploadLimit.getMinSize(), Long.MAX_VALUE);
                sign.setLimitMinSize(uploadLimit.getMinSize());
            }
            if (CollectionUtil.isNotEmpty(uploadLimit.getContentType())) {
                for (String mime : uploadLimit.getContentType()) {
                    policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_TYPE, mime);
                }
                sign.setLimitContentType(uploadLimit.getContentType());
            }
            Date expirationDate = DateUtil.plusSeconds(new Date(), uploadLimit.getExpireSecondTime().intValue());
            // 计算签名
            String policy = ossClient.generatePostPolicy(expirationDate, policyConditions);
            String encodedPolicy = BinaryUtil.toBase64String(policy.getBytes(CharSetEnum.UTF8.getCharSet()));
            String signature = ossClient.calculatePostSignature(policy);

            sign.setAccessKeyId(ossClient.getCredentialsProvider().getCredentials().getAccessKeyId());
            sign.setPolicy(encodedPolicy);
            sign.setSignature(signature);
            sign.setExpireSecondTime(uploadLimit.getExpireSecondTime());
            sign.setExpireDateTime(LocalDateTime.now().plusSeconds(uploadLimit.getExpireSecondTime()));
            sign.setTargetPath(targetPath);
            sign.setBucketName(bucketName);
            sign.setRegion(this.ossProperties.getRegion());
            sign.setEndpoint(this.ossProperties.getEndpoint());
            return (T) sign;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean createSymlink(String bucketName, String linkPath, String targetPath) {
        bucketName = this.getOrDefaultBucketName(bucketName);
        try {
            this.ossClient.createSymlink(bucketName, linkPath, targetPath);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void close() {
        this.ossClient.shutdown();
    }
}
