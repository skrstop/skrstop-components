package com.zoe.framework.components.starter.ftp.service.impl;

import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
import com.zoe.framework.components.starter.ftp.configuration.FtpProperties;
import com.zoe.framework.components.starter.ftp.configuration.GlobalObjectStorageProperties;
import com.zoe.framework.components.starter.ftp.service.FtpService;
import com.zoe.framework.components.util.value.data.StrUtil;
import lombok.Getter;

import java.nio.charset.Charset;

/**
 * FtpServiceImpl class
 *
 * @author 蒋时华
 * @date 2019/7/2
 */
public class FtpServiceImpl implements FtpService {

    @Getter
    private FtpProperties ftpProperties;
    private Ftp ftp;
    @Getter
    private String path = "";

    public FtpServiceImpl(GlobalObjectStorageProperties globalObjectStorageProperties) {
        this.ftpProperties = globalObjectStorageProperties.getFtp();
        try {
            ftp = new Ftp(this.ftpProperties.getHost()
                    , this.ftpProperties.getPort()
                    , this.ftpProperties.getUsername()
                    , this.ftpProperties.getPassword()
                    , Charset.forName(this.ftpProperties.getCharset()));
        } catch (Exception e) {
            throw new IllegalArgumentException("FTP连接失败");
        }
        ftp.setMode(this.ftpProperties.getMode());
        if (StrUtil.isNoneBlank(this.ftpProperties.getBasePath())) {
            path += this.ftpProperties.getBasePath();
        }
        if (StrUtil.isNoneBlank(this.ftpProperties.getUploadPath())) {
            path += this.ftpProperties.getUploadPath();
        }
        if (StrUtil.isBlank(path)) {
            path = "/";
        }
    }

    @Override
    public Ftp getFtpClient() {
        ftp.reconnectIfTimeout();
        return this.ftp;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public FtpProperties getConfigProperties() {
        return this.ftpProperties;
    }

    @Override
    public String getHost() {
        return this.getConfigProperties().getHost();
    }

    @Override
    public int getPort() {
        return this.getConfigProperties().getPort();
    }

    @Override
    public String getUsername() {
        return this.getConfigProperties().getUsername();
    }

    @Override
    public String getPassword() {
        return this.getConfigProperties().getPassword();
    }

    @Override
    public String getBasePath() {
        return this.getConfigProperties().getBasePath();
    }

    @Override
    public String getUploadPath() {
        return this.getConfigProperties().getUploadPath();
    }

    @Override
    public String getCharset() {
        return this.getConfigProperties().getCharset();
    }

    @Override
    public FtpMode getMode() {
        return this.getConfigProperties().getMode();
    }
}
