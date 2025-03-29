package com.skrstop.framework.components.starter.objectStorage.service;

import com.skrstop.framework.components.starter.objectStorage.entity.StorageTemplateSign;
import com.skrstop.framework.components.starter.objectStorage.entity.TemporaryAccessExtraParam;
import com.skrstop.framework.components.starter.objectStorage.entity.UploadLimit;

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
    Map<String, String> getPublicAccessUrl(String bucketName, List<String> targetPath, boolean useOriginHost);

    default Map<String, String> getPublicAccessUrl(String bucketName, List<String> targetPath) {
        return getPublicAccessUrl(null, targetPath, false);
    }

    default Map<String, String> getPublicAccessUrl(List<String> targetPath) {
        return getPublicAccessUrl(null, targetPath, false);
    }

    /**
     * 获取公共资源访问地址
     *
     * @param bucketName
     * @param targetPath
     * @return
     */
    String getPublicAccessUrl(String bucketName, String targetPath, boolean useOriginHost);

    default String getPublicAccessUrl(String bucketName, String targetPath) {
        return getPublicAccessUrl(null, targetPath, false);
    }

    default String getPublicAccessUrl(String targetPath) {
        return getPublicAccessUrl(null, targetPath, false);
    }

    /**
     * @param bucketName
     * @param targetPath
     * @return
     */
    Map<String, String> getTemporaryAccessUrl(String bucketName, List<String> targetPath, TemporaryAccessExtraParam extraParam);

    default Map<String, String> getTemporaryAccessUrl(String bucketName, List<String> targetPath, long expireSecondTime, Map<String, Object> queryParams) {
        return getTemporaryAccessUrl(bucketName, targetPath, TemporaryAccessExtraParam.builder()
                .expireSecondTime(expireSecondTime)
                .queryParams(queryParams)
                .useOriginHost(false)
                .build());
    }

    default Map<String, String> getTemporaryAccessUrl(List<String> targetPath, long expireSecondTime) {
        return getTemporaryAccessUrl(null, targetPath, TemporaryAccessExtraParam.builder()
                .expireSecondTime(expireSecondTime)
                .useOriginHost(false)
                .build());
    }

    default Map<String, String> getTemporaryAccessUrl(List<String> targetPath, long expireSecondTime, Map<String, Object> queryParams) {
        return getTemporaryAccessUrl(null, targetPath, TemporaryAccessExtraParam.builder()
                .expireSecondTime(expireSecondTime)
                .queryParams(queryParams)
                .useOriginHost(false)
                .build());
    }

    /**
     * 获取临时访问地址
     *
     * @param bucketName
     * @param targetPath
     * @return
     */
    String getTemporaryAccessUrl(String bucketName, String targetPath, TemporaryAccessExtraParam extraParam);

    default String getTemporaryAccessUrl(String bucketName, String targetPath, long expireSecondTime, Map<String, Object> queryParams) {
        return getTemporaryAccessUrl(bucketName, targetPath, TemporaryAccessExtraParam.builder()
                .expireSecondTime(expireSecondTime)
                .queryParams(queryParams)
                .useOriginHost(false)
                .build());
    }

    default String getTemporaryAccessUrl(String targetPath, long expireSecondTime) {
        return getTemporaryAccessUrl(null, targetPath, TemporaryAccessExtraParam.builder()
                .expireSecondTime(expireSecondTime)
                .useOriginHost(false)
                .build());
    }

    default String getTemporaryAccessUrl(String targetPath, long expireSecondTime, Map<String, Object> queryParams) {
        return getTemporaryAccessUrl(null, targetPath, TemporaryAccessExtraParam.builder()
                .expireSecondTime(expireSecondTime)
                .queryParams(queryParams)
                .useOriginHost(false)
                .build());
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
        return getTemporaryUploadSign(bucketName, targetPath, UploadLimit.builder()
                .expireSecondTime(expireSecondTime)
                .build());
    }

    default <T extends StorageTemplateSign> T getTemporaryUploadSign(String targetPath, long expireSecondTime) {
        return getTemporaryUploadSign(null, targetPath, UploadLimit.builder()
                .expireSecondTime(expireSecondTime)
                .build());
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
        return getTemporaryUploadSign(bucketName, targetPath, UploadLimit.builder()
                .expireSecondTime(expireSecondTime)
                .minSize(minSize)
                .maxSize(maxSize)
                .build());
    }

    default <T extends StorageTemplateSign> T getTemporaryUploadSign(String targetPath, long expireSecondTime, Long minSize, Long maxSize) {
        return getTemporaryUploadSign(null, targetPath, UploadLimit.builder()
                .expireSecondTime(expireSecondTime)
                .minSize(minSize)
                .maxSize(maxSize)
                .build());
    }

    /**
     * 获取临时访问秘钥, 限制文件类型
     *
     * @param bucketName
     * @param targetPath
     * @param expireSecondTime
     * @return
     */
    default <T extends StorageTemplateSign> T getTemporaryUploadSign(String bucketName, String targetPath, long expireSecondTime, List<String> contentType) {
        return getTemporaryUploadSign(bucketName, targetPath, UploadLimit.builder()
                .expireSecondTime(expireSecondTime)
                .contentType(contentType)
                .build());
    }

    default <T extends StorageTemplateSign> T getTemporaryUploadSign(String targetPath, long expireSecondTime, List<String> contentType) {
        return getTemporaryUploadSign(null, targetPath, UploadLimit.builder()
                .expireSecondTime(expireSecondTime)
                .contentType(contentType)
                .build());
    }


    /**
     * 获取临时访问秘钥, 限制大小 和 文件类型
     *
     * @param bucketName
     * @param targetPath
     * @return
     */
    <T extends StorageTemplateSign> T getTemporaryUploadSign(String bucketName, String targetPath, UploadLimit uploadLimit);

    default <T extends StorageTemplateSign> T getTemporaryUploadSign(String targetPath, long expireSecondTime
            , Long minSize, Long maxSize, List<String> contentType) {
        return getTemporaryUploadSign(null, targetPath, UploadLimit.builder()
                .expireSecondTime(expireSecondTime)
                .minSize(minSize)
                .maxSize(maxSize)
                .contentType(contentType)
                .build());
    }

    /**
     * 创建软链接
     *
     * @param bucketName
     * @param linkPath
     * @param targetPath
     * @return
     */
    boolean createSymlink(String bucketName, String linkPath, String targetPath);

    /**
     * 创建软链接
     *
     * @param linkPath
     * @param targetPath
     * @return
     */
    default boolean createSymlink(String linkPath, String targetPath) {
        return this.createSymlink(null, linkPath, targetPath);
    }


}
