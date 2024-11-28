package com.skrstop.framework.components.starter.objectStorage.service.impl;

import com.skrstop.framework.components.starter.objectStorage.configuration.CosProperties;
import com.skrstop.framework.components.starter.objectStorage.configuration.FtpProperties;
import com.skrstop.framework.components.starter.objectStorage.configuration.dynamic.DynamicDatasourceContextHolder;
import com.skrstop.framework.components.starter.objectStorage.configuration.dynamic.DynamicObjectStorageProperties;
import com.skrstop.framework.components.starter.objectStorage.entity.StorageTemplateSign;
import com.skrstop.framework.components.starter.objectStorage.service.ObjectStorageService;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 蒋时华
 * @date 2024-03-18 23:41:56
 * @since 1.0.0
 */
@Slf4j
public class MultipleObjectStorageServiceImpl implements ObjectStorageService {

    private final DynamicObjectStorageProperties dynamicObjectStorageProperties;
    private final Map<String, ObjectStorageService> objectStorageDatasourceMap;

    public MultipleObjectStorageServiceImpl(DynamicObjectStorageProperties dynamicObjectStorageProperties) {
        this.dynamicObjectStorageProperties = dynamicObjectStorageProperties;
        // 初始化工作
        LinkedHashMap<String, FtpProperties> ftpDataSources = dynamicObjectStorageProperties.getFtpDataSources();
        LinkedHashMap<String, CosProperties> cosDataSources = dynamicObjectStorageProperties.getCosDataSources();
        int totalDatasourceSize = ftpDataSources.size() + cosDataSources.size();
        if (totalDatasourceSize <= 0) {
            throw new IllegalArgumentException("动态数据源为空, 请检查配置文件");
        }
        this.objectStorageDatasourceMap = new HashMap<>(totalDatasourceSize, 1);
        ftpDataSources.forEach((key, value) -> {
            if (objectStorageDatasourceMap.containsKey(key)) {
                throw new IllegalArgumentException("数据源名称重复: " + key + ", 请检查配置文件");
            }
            this.objectStorageDatasourceMap.put(key, new FtpObjectStorageServiceImpl(value));
        });
        cosDataSources.forEach((key, value) -> {
            if (objectStorageDatasourceMap.containsKey(key)) {
                throw new IllegalArgumentException("数据源名称重复: " + key + ", 请检查配置文件");
            }
            this.objectStorageDatasourceMap.put(key, new CosObjectStorageServiceImpl(value));
        });
    }

    private ObjectStorageService getObjectStorageService() {
        String dsKey = DynamicDatasourceContextHolder.peek();
        ObjectStorageService objectStorageService = this.objectStorageDatasourceMap.get(dsKey);
        if (ObjectUtil.isNotNull(objectStorageService)) {
            return objectStorageService;
        }
        if (this.dynamicObjectStorageProperties.isExceptionWhileNotFound()) {
            throw new IllegalArgumentException("数据源不存在: " + dsKey);
        }
        objectStorageService = this.objectStorageDatasourceMap.get(this.dynamicObjectStorageProperties.getPrimary());
        if (objectStorageService == null) {
            throw new IllegalArgumentException("默认数据源不存在: " + this.dynamicObjectStorageProperties.getPrimary());
        }
        return objectStorageService;
    }

    @Override
    public String getBasePath() {
        return this.getObjectStorageService().getBasePath();
    }

    @Override
    public Object getClient() {
        return this.getObjectStorageService().getClient();
    }

    @Override
    public String getDefaultBucketName() {
        ObjectStorageService objectStorageService = this.getObjectStorageService();
        return objectStorageService.getDefaultBucketName();
    }

    @Override
    public boolean upload(String bucketName, String targetPath, File file) {
        return this.getObjectStorageService().upload(bucketName, targetPath, file);
    }

    @Override
    public boolean upload(String bucketName, String targetPath, byte[] bytes) {
        return this.getObjectStorageService().upload(bucketName, targetPath, bytes);
    }

    @Override
    public boolean upload(String bucketName, String targetPath, InputStream inputStream) {
        return this.getObjectStorageService().upload(bucketName, targetPath, inputStream);
    }

    @Override
    public boolean download(String bucketName, String targetPath, String localPath) {
        return this.getObjectStorageService().download(bucketName, targetPath, localPath);
    }

    @Override
    public boolean download(String bucketName, String targetPath, File localFile) {
        return this.getObjectStorageService().download(bucketName, targetPath, localFile);
    }

    @Override
    public boolean exists(String bucketName, String targetPath) {
        return this.getObjectStorageService().exists(bucketName, targetPath);
    }

    @Override
    public boolean delete(String bucketName, String targetPath) {
        return this.getObjectStorageService().delete(bucketName, targetPath);
    }

    @Override
    public boolean delete(String bucketName, List<String> targetPaths) {
        return this.getObjectStorageService().delete(bucketName, targetPaths);
    }

    @Override
    public boolean copy(String bucketName, String sourcePath, String targetPath) {
        return this.getObjectStorageService().copy(bucketName, sourcePath, targetPath);
    }

    @Override
    public boolean copy(String sourceBucketName, String sourcePath, String targetBucketName, String targetPath) {
        return this.getObjectStorageService().copy(sourceBucketName, sourcePath, targetBucketName, targetPath);
    }

    @Override
    public boolean move(String bucketName, String sourcePath, String targetPath) {
        return this.getObjectStorageService().move(bucketName, sourcePath, targetPath);
    }

    @Override
    public boolean move(String sourceBucketName, String sourcePath, String targetBucketName, String targetPath) {
        return this.getObjectStorageService().move(sourceBucketName, sourcePath, targetBucketName, targetPath);
    }

    @Override
    public String getTemporaryAccessUrl(String bucketName, String targetPath, long expireTime, Map<String, Object> params, boolean useOriginHost) {
        return this.getObjectStorageService().getTemporaryAccessUrl(bucketName, targetPath, expireTime, params, useOriginHost);
    }

    @Override
    public Map<String, String> getTemporaryAccessUrl(String bucketName, List<String> targetPath, long expireTime, Map<String, Object> params, boolean useOriginHost) {
        return this.getObjectStorageService().getTemporaryAccessUrl(bucketName, targetPath, expireTime, params, useOriginHost);
    }

    @Override
    public Map<String, String> getPublicAccessUrl(String bucketName, List<String> targetPath, boolean useOriginHost) {
        return this.getObjectStorageService().getPublicAccessUrl(bucketName, targetPath, useOriginHost);
    }

    @Override
    public String getPublicAccessUrl(String bucketName, String targetPath, boolean useOriginHost) {
        return this.getObjectStorageService().getPublicAccessUrl(bucketName, targetPath, useOriginHost);
    }

    @Override
    public <T extends StorageTemplateSign> T getTemporaryUploadSign(String bucketName, String targetPath, long expireSecondTime, Long minSize, Long maxSize, List<String> contentType) {
        return this.getObjectStorageService().getTemporaryUploadSign(bucketName, targetPath, expireSecondTime, minSize, maxSize, contentType);
    }

    @Override
    public void close() {
        this.objectStorageDatasourceMap.forEach((key, value) -> {
            try {
                value.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        });
    }
}
