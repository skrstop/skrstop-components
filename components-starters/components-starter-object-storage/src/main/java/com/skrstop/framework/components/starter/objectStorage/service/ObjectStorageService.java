package com.skrstop.framework.components.starter.objectStorage.service;

import com.skrstop.framework.components.starter.objectStorage.entity.StorageTemplateSign;
import com.skrstop.framework.components.util.enums.ContentTypeEnum;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * FtpService class
 *
 * @author 蒋时华
 * @date 2019/7/2
 */
public interface ObjectStorageService extends Closeable {

    String getBasePath();

    Object getClient();

    /**
     * 默认存储桶名字
     *
     * @return
     */
    String getDefaultBucketName();

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名字，ftp不需要此参数，传空即可
     * @param targetPath 目标路径
     * @param file       文件
     * @return
     */
    boolean upload(String bucketName, String targetPath, File file);

    default boolean upload(String targetPath, File file) {
        return upload(null, targetPath, file);
    }

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名字，ftp不需要此参数，传空即可
     * @param targetPath 目标路径
     * @param bytes      文件
     * @return
     */
    boolean upload(String bucketName, String targetPath, byte[] bytes);

    default boolean upload(String targetPath, byte[] bytes) {
        return upload(null, targetPath, bytes);
    }

    /**
     * 上传文件
     *
     * @param bucketName  存储桶名字，ftp不需要此参数，传空即可
     * @param targetPath  目标路径
     * @param inputStream 文件
     * @return
     */
    boolean upload(String bucketName, String targetPath, InputStream inputStream);

    default boolean upload(String targetPath, InputStream inputStream) {
        return upload(null, targetPath, inputStream);
    }

    /**
     * 下载文件
     *
     * @param bucketName 存储桶名字，ftp不需要此参数，传空即可
     * @param targetPath
     * @param localPath
     */
    boolean download(String bucketName, String targetPath, String localPath);

    default boolean download(String targetPath, String localPath) {
        return download(null, targetPath, localPath);
    }

    /**
     * 下载文件
     *
     * @param bucketName 存储桶名字，ftp不需要此参数，传空即可
     * @param targetPath
     * @param localFile
     */
    boolean download(String bucketName, String targetPath, File localFile);

    default void download(String targetPath, File localFile) {
        download(null, targetPath, localFile);
    }

    /**
     * 检查文件是否存在
     *
     * @param bucketName 存储桶名字，ftp不需要此参数，传空即可
     * @param targetPath
     */
    boolean exists(String bucketName, String targetPath);

    default boolean exists(String targetPath) {
        return exists(null, targetPath);
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名字，ftp不需要此参数，传空即可
     * @param targetPath
     * @return
     */
    boolean delete(String bucketName, String targetPath);

    default boolean delete(String targetPath) {
        return delete(null, targetPath);
    }

    /**
     * 删除多个文件
     *
     * @param bucketName  存储桶名字，ftp不需要此参数，传空即可
     * @param targetPaths
     * @return
     */
    boolean delete(String bucketName, List<String> targetPaths);

    default boolean delete(List<String> targetPaths) {
        return delete(null, targetPaths);
    }

    /**
     * 复制文件
     * ftp不支持该操作
     *
     * @param bucketName 存储桶名字，ftp不需要此参数，传空即可
     * @param sourcePath
     * @param targetPath
     * @return
     */
    boolean copy(String bucketName, String sourcePath, String targetPath);

    default boolean copy(String sourcePath, String targetPath) {
        return copy(null, sourcePath, targetPath);
    }

    boolean copy(String sourceBucketName, String sourcePath, String targetBucketName, String targetPath);

    /**
     * 移动文件
     * ftp不支持该操作
     *
     * @param bucketName 存储桶名字，ftp不需要此参数，传空即可
     * @param sourcePath
     * @param targetPath
     * @return
     */
    boolean move(String bucketName, String sourcePath, String targetPath);

    default boolean move(String sourcePath, String targetPath) {
        return move(null, sourcePath, targetPath);
    }

    boolean move(String sourceBucketName, String sourcePath, String targetBucketName, String targetPath);

    /**
     * @param bucketName
     * @param targetPath
     * @return
     */
    Map<String, String> getPublicAccessUrl(String bucketName, List<String> targetPath);

    default Map<String, String> getPublicAccessUrl(List<String> targetPath) {
        return getPublicAccessUrl(null, targetPath);
    }

    /**
     * 获取公共资源访问地址
     *
     * @param bucketName
     * @param targetPath
     * @return
     */
    String getPublicAccessUrl(String bucketName, String targetPath);

    default String getPublicAccessUrl(String targetPath) {
        return getPublicAccessUrl(null, targetPath);
    }

    /**
     * @param bucketName
     * @param targetPath
     * @param expireTime 有效时间，单位：秒
     * @return
     */
    Map<String, String> getTemporaryAccessUrl(String bucketName, List<String> targetPath, long expireTime, Map<String, Object> params);

    default Map<String, String> getTemporaryAccessUrl(List<String> targetPath, long expireTime) {
        return getTemporaryAccessUrl(null, targetPath, expireTime, null);
    }

    default Map<String, String> getTemporaryAccessUrl(List<String> targetPath, long expireTime, Map<String, Object> params) {
        return getTemporaryAccessUrl(null, targetPath, expireTime, params);
    }

    /**
     * 获取临时访问地址
     *
     * @param bucketName
     * @param targetPath
     * @param expireTime 有效时间，单位：秒
     * @return
     */
    String getTemporaryAccessUrl(String bucketName, String targetPath, long expireTime, Map<String, Object> params);

    default String getTemporaryAccessUrl(String targetPath, long expireTime) {
        return getTemporaryAccessUrl(null, targetPath, expireTime, null);
    }

    default String getTemporaryAccessUrl(String targetPath, long expireTime, Map<String, Object> params) {
        return getTemporaryAccessUrl(null, targetPath, expireTime, params);
    }

    /**
     * 获取临时访问秘钥
     *
     * @param bucketName
     * @param targetPath
     * @param expireSecondTime
     * @return
     */
    default <T extends StorageTemplateSign> T getTemporaryUploadSign(String bucketName, String targetPath, long expireSecondTime) {
        return getTemporaryUploadSign(bucketName, targetPath, expireSecondTime, null, null, null);
    }

    default <T extends StorageTemplateSign> T getTemporaryUploadSign(String targetPath, long expireSecondTime) {
        return getTemporaryUploadSign(null, targetPath, expireSecondTime, null, null, null);
    }

    /**
     * 获取临时访问秘钥, 限制大小
     *
     * @param bucketName
     * @param targetPath
     * @param expireSecondTime
     * @return
     */
    default <T extends StorageTemplateSign> T getTemporaryUploadSign(String bucketName, String targetPath, long expireSecondTime, Long minSize, Long maxSize) {
        return getTemporaryUploadSign(bucketName, targetPath, expireSecondTime, minSize, maxSize, null);
    }

    default <T extends StorageTemplateSign> T getTemporaryUploadSign(String targetPath, long expireSecondTime, Long minSize, Long maxSize) {
        return getTemporaryUploadSign(null, targetPath, expireSecondTime, minSize, maxSize, null);
    }

    /**
     * 获取临时访问秘钥, 限制文件类型
     *
     * @param bucketName
     * @param targetPath
     * @param expireSecondTime
     * @return
     */
    default <T extends StorageTemplateSign> T getTemporaryUploadSign(String bucketName, String targetPath, long expireSecondTime, List<ContentTypeEnum> contentType) {
        return getTemporaryUploadSign(bucketName, targetPath, expireSecondTime, null, null, contentType);
    }

    default <T extends StorageTemplateSign> T getTemporaryUploadSign(String targetPath, long expireSecondTime, List<ContentTypeEnum> contentType) {
        return getTemporaryUploadSign(null, targetPath, expireSecondTime, null, null, contentType);
    }


    /**
     * 获取临时访问秘钥, 限制大小 和 文件类型
     *
     * @param bucketName
     * @param targetPath
     * @param expireSecondTime
     * @param minSize          最小文件大小 单位：b
     * @param maxSize          最大文件大小 单位：b
     * @param contentType
     * @return
     */
    <T extends StorageTemplateSign> T getTemporaryUploadSign(String bucketName, String targetPath, long expireSecondTime
            , Long minSize, Long maxSize, List<ContentTypeEnum> contentType);

    default <T extends StorageTemplateSign> T getTemporaryUploadSign(String targetPath, long expireSecondTime
            , Long minSize, Long maxSize, List<ContentTypeEnum> contentType) {
        return getTemporaryUploadSign(null, targetPath, expireSecondTime, minSize, maxSize, contentType);
    }

}
