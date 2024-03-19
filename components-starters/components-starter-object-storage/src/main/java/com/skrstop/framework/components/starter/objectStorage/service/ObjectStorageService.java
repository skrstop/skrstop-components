package com.skrstop.framework.components.starter.objectStorage.service;

import com.skrstop.framework.components.starter.objectStorage.entiry.StorageTemplateSign;

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
     * 上传文件
     *
     * @param bucketName 存储桶名字，ftp不需要此参数，传空即可
     * @param targetPath 目标路径
     * @param file       文件
     * @return
     */
    boolean upload(String bucketName, String targetPath, File file);

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名字，ftp不需要此参数，传空即可
     * @param targetPath 目标路径
     * @param bytes      文件
     * @return
     */
    boolean upload(String bucketName, String targetPath, byte[] bytes);

    /**
     * 上传文件
     *
     * @param bucketName  存储桶名字，ftp不需要此参数，传空即可
     * @param targetPath  目标路径
     * @param inputStream 文件
     * @return
     */
    boolean upload(String bucketName, String targetPath, InputStream inputStream);

    /**
     * 下载文件
     *
     * @param bucketName 存储桶名字，ftp不需要此参数，传空即可
     * @param targetPath
     * @param localPath
     */
    void download(String bucketName, String targetPath, String localPath);

    /**
     * 下载文件
     *
     * @param bucketName 存储桶名字，ftp不需要此参数，传空即可
     * @param targetPath
     * @param localFile
     */
    void download(String bucketName, String targetPath, File localFile);

    /**
     * 检查文件是否存在
     *
     * @param bucketName 存储桶名字，ftp不需要此参数，传空即可
     * @param targetPath
     */
    boolean exists(String bucketName, String targetPath);

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名字，ftp不需要此参数，传空即可
     * @param targetPath
     * @return
     */
    boolean delete(String bucketName, String targetPath);

    /**
     * 删除多个文件
     *
     * @param bucketName  存储桶名字，ftp不需要此参数，传空即可
     * @param targetPaths
     * @return
     */
    boolean delete(String bucketName, List<String> targetPaths);

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

    boolean move(String sourceBucketName, String sourcePath, String targetBucketName, String targetPath);

    /**
     * 获取临时访问地址
     *
     * @param bucketName
     * @param targetPath
     * @param expireTime 有效时间，单位：秒
     * @return
     */
    String getTemporaryAccessUrl(String bucketName, String targetPath, long expireTime);

    /**
     * @param bucketName
     * @param targetPath
     * @param expireTime 有效时间，单位：秒
     * @return
     */
    Map<String, String> getTemporaryAccessUrl(String bucketName, List<String> targetPath, long expireTime);

    /**
     * 获取临时访问秘钥
     *
     * @param bucketName
     * @param targetPath
     * @param expireSecondTime
     * @return
     */
    <T extends StorageTemplateSign> T getTemporaryAccessSign(String bucketName, String targetPath, long expireSecondTime);

}
