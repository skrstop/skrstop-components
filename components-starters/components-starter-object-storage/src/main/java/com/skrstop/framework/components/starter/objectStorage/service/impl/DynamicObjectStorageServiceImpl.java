package com.skrstop.framework.components.starter.objectStorage.service.impl;

import com.skrstop.framework.components.starter.objectStorage.entity.StorageTemplateSign;
import com.skrstop.framework.components.starter.objectStorage.service.DynamicObjectStorageService;
import com.skrstop.framework.components.starter.objectStorage.service.ObjectStorageService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author 蒋时华
 * @date 2024-03-18 23:41:56
 * @since 1.0.0
 */
public class DynamicObjectStorageServiceImpl implements DynamicObjectStorageService {

    private final ObjectStorageService objectStorageService;

    public DynamicObjectStorageServiceImpl(ObjectStorageService objectStorageService) {
        this.objectStorageService = objectStorageService;
    }


    @Override
    public String getBasePath(String dsKey) {
        return this.objectStorageService.getBasePath();
    }

    @Override
    public Object getClient(String dsKey) {
        return this.objectStorageService.getClient();
    }

    @Override
    public boolean upload(String dsKey, String bucketName, String targetPath, File file) {
        return this.objectStorageService.upload(bucketName, targetPath, file);
    }

    @Override
    public boolean upload(String dsKey, String bucketName, String targetPath, byte[] bytes) {
        return this.objectStorageService.upload(bucketName, targetPath, bytes);
    }

    @Override
    public boolean upload(String dsKey, String bucketName, String targetPath, InputStream inputStream) {
        return this.objectStorageService.upload(bucketName, targetPath, inputStream);
    }

    @Override
    public boolean download(String dsKey, String bucketName, String targetPath, String localPath) {
        return this.objectStorageService.download(bucketName, targetPath, localPath);
    }

    @Override
    public boolean download(String dsKey, String bucketName, String targetPath, File localFile) {
        return this.objectStorageService.download(bucketName, targetPath, localFile);
    }

    @Override
    public boolean exists(String dsKey, String bucketName, String targetPath) {
        return this.objectStorageService.exists(bucketName, targetPath);
    }

    @Override
    public boolean delete(String dsKey, String bucketName, String targetPath) {
        return this.objectStorageService.delete(bucketName, targetPath);
    }

    @Override
    public boolean delete(String dsKey, String bucketName, List<String> targetPaths) {
        return this.objectStorageService.delete(bucketName, targetPaths);
    }

    @Override
    public boolean copy(String dsKey, String bucketName, String sourcePath, String targetPath) {
        return this.objectStorageService.copy(bucketName, sourcePath, targetPath);
    }

    @Override
    public boolean copy(String dsKey, String sourceBucketName, String sourcePath, String targetBucketName, String targetPath) {
        return this.objectStorageService.copy(sourceBucketName, sourcePath, targetBucketName, targetPath);
    }

    @Override
    public boolean move(String dsKey, String bucketName, String sourcePath, String targetPath) {
        return this.objectStorageService.move(bucketName, sourcePath, targetPath);
    }

    @Override
    public boolean move(String dsKey, String sourceBucketName, String sourcePath, String targetBucketName, String targetPath) {
        return this.objectStorageService.move(sourceBucketName, sourcePath, targetBucketName, targetPath);
    }

    @Override
    public String getTemporaryAccessUrl(String dsKey, String bucketName, String targetPath, long expireTime, Map<String, Object> params) {
        return this.objectStorageService.getTemporaryAccessUrl(bucketName, targetPath, expireTime, params);
    }

    @Override
    public String getPublicAccessUrl(String bucketName, String targetPath) {
        return this.objectStorageService.getPublicAccessUrl(bucketName, targetPath);
    }

    @Override
    public Map<String, String> getPublicAccessUrl(String bucketName, List<String> targetPath) {
        return this.objectStorageService.getPublicAccessUrl(bucketName, targetPath);
    }

    @Override
    public Map<String, String> getTemporaryAccessUrl(String dsKey, String bucketName, List<String> targetPath, long expireTime, Map<String, Object> params) {
        return this.objectStorageService.getTemporaryAccessUrl(bucketName, targetPath, expireTime, params);
    }

    @Override
    public <T extends StorageTemplateSign> T getTemporaryAccessSign(String dsKey, String bucketName, String targetPath, long expireSecondTime, Long minSize, Long maxSize, List<String> contentType) {
        return this.objectStorageService.getTemporaryUploadSign(bucketName, targetPath, expireSecondTime, minSize, maxSize, contentType);
    }

    @Override
    public void close() throws IOException {
        this.objectStorageService.close();
    }
}
