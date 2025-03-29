package com.skrstop.framework.components.starter.objectStorage.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;
import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.skrstop.framework.components.starter.objectStorage.configuration.FtpProperties;
import com.skrstop.framework.components.starter.objectStorage.entity.StorageTemplateSign;
import com.skrstop.framework.components.starter.objectStorage.entity.TemporaryAccessExtraParam;
import com.skrstop.framework.components.starter.objectStorage.entity.UploadLimit;
import com.skrstop.framework.components.starter.objectStorage.service.ObjectStorageService;
import com.skrstop.framework.components.util.value.data.StrUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * FtpServiceImpl class
 *
 * @author 蒋时华
 * @date 2019/7/2
 */
@Slf4j
public class FtpObjectStorageServiceImpl implements ObjectStorageService {

    @Getter
    private FtpProperties ftpProperties;
    @Getter
    private Ftp ftpClient;
    @Getter
    private String basePath = "";

    public FtpObjectStorageServiceImpl(FtpProperties ftpProperties) {
        this.ftpProperties = ftpProperties;
        try {
            ftpClient = new Ftp(ftpProperties.getHost()
                    , ftpProperties.getPort()
                    , ftpProperties.getUsername()
                    , ftpProperties.getPassword()
                    , Charset.forName(ftpProperties.getCharset()));
        } catch (Exception e) {
            throw new IllegalArgumentException("FTP连接失败");
        }
        ftpClient.setMode(ftpProperties.getMode());
        if (StrUtil.isNotBlank(ftpProperties.getBasePath())) {
            basePath = ftpProperties.getBasePath();
        }
        if (StrUtil.isBlank(basePath)) {
            basePath = "/";
        }
    }

    @Override
    public Object getClient() {
        return this.ftpClient;
    }

    @Override
    public String getDefaultBucketName() {
        return null;
    }

    @Override
    public boolean upload(String bucketName, String targetPath, File file) {
        targetPath = basePath + targetPath;
        String fileName = FileUtil.getName(targetPath);
        return this.ftpClient.upload(targetPath, fileName, file);
    }

    @Override
    public boolean upload(String bucketName, String targetPath, byte[] bytes) {
        targetPath = basePath + targetPath;
        return this.upload(bucketName, targetPath, new ByteArrayInputStream(bytes));
    }

    @Override
    public boolean upload(String bucketName, String targetPath, InputStream inputStream) {
        targetPath = basePath + targetPath;
        String fileName = FileUtil.getName(targetPath);
        try {
            return this.ftpClient.upload(targetPath, fileName, inputStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return false;
    }

    @Override
    public boolean download(String bucketName, String targetPath, String localPath) {
        targetPath = basePath + targetPath;
        return this.download(bucketName, targetPath, new File(localPath));
    }

    @Override
    public boolean download(String bucketName, String targetPath, File localFile) {
        targetPath = basePath + targetPath;
        String fileName = FileUtil.getName(targetPath);
        try {
            this.ftpClient.download(targetPath, fileName, localFile);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean exists(String bucketName, String targetPath) {
        targetPath = basePath + targetPath;
        return this.ftpClient.exist(targetPath);
    }

    @Override
    public boolean delete(String bucketName, String targetPath) {
        targetPath = basePath + targetPath;
        boolean dir = this.ftpClient.isDir(targetPath);
        if (dir) {
            return this.ftpClient.delDir(targetPath);
        }
        return this.ftpClient.delFile(targetPath);
    }

    @Override
    public boolean delete(String bucketName, List<String> targetPaths) {
        for (String targetPath : targetPaths) {
            this.delete(bucketName, targetPath);
        }
        return true;
    }

    @Override
    public boolean copy(String bucketName, String sourcePath, String targetPath) {
        throw new NotSupportedException("FTP不支持复制文件，请手动使用复合操作");
    }

    @Override
    public boolean copy(String sourceBucketName, String sourcePath, String targetBucketName, String targetPath) {
        throw new NotSupportedException("FTP不支持改操作，请手动使用复合操作");
    }

    @Override
    public boolean move(String bucketName, String sourcePath, String targetPath) {
        throw new NotSupportedException("FTP不支持移动文件，请手动使用复合操作");
    }

    @Override
    public boolean move(String sourceBucketName, String sourcePath, String targetBucketName, String targetPath) {
        throw new NotSupportedException("FTP不支持移动文件，请手动使用复合操作");
    }

    @Override
    public String getTemporaryAccessUrl(String bucketName, String targetPath, TemporaryAccessExtraParam extraParam) {
        throw new NotSupportedException("FTP不支持该操作");
    }

    @Override
    public Map<String, String> getTemporaryAccessUrl(String bucketName, List<String> targetPath, TemporaryAccessExtraParam extraParam) {
        throw new NotSupportedException("FTP不支持该操作");
    }

    @Override
    public Map<String, String> getPublicAccessUrl(String bucketName, List<String> targetPath, boolean useOriginHost) {
        throw new NotSupportedException("FTP不支持该操作");
    }

    @Override
    public String getPublicAccessUrl(String bucketName, String targetPath, boolean useOriginHost) {
        throw new NotSupportedException("FTP不支持该操作");
    }

    @Override
    public <T extends StorageTemplateSign> T getTemporaryUploadSign(String bucketName, String targetPath, UploadLimit uploadLimit) {
        throw new NotSupportedException("FTP不支持该操作");
    }

    @Override
    public boolean createSymlink(String bucketName, String linkPath, String targetPath) {
        throw new NotSupportedException("FTP不支持该操作");
    }

    @Override
    public void close() throws IOException {
        this.ftpClient.close();
    }
}
