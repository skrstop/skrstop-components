package com.zoe.framework.components.starter.objectStorage.service.impl;

import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
import com.zoe.framework.components.starter.objectStorage.configuration.FtpProperties;
import com.zoe.framework.components.starter.objectStorage.service.FtpService;
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

    public FtpServiceImpl(FtpProperties ftpProperties) {
        this.ftpProperties = ftpProperties;
        try {
            ftp = new Ftp(ftpProperties.getHost()
                    , ftpProperties.getPort()
                    , ftpProperties.getUsername()
                    , ftpProperties.getPassword()
                    , Charset.forName(ftpProperties.getCharset()));
        } catch (Exception e) {
            throw new IllegalArgumentException("FTP连接失败");
        }
        ftp.setMode(ftpProperties.getMode());
        if (StrUtil.isNoneBlank(ftpProperties.getBasePath())) {
            path += ftpProperties.getBasePath();
        }
        if (StrUtil.isNoneBlank(ftpProperties.getUploadPath())) {
            path += ftpProperties.getUploadPath();
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
    public FtpProperties getFtpConfig() {
        return this.ftpProperties;
    }

    @Override
    public String getHost() {
        return this.getFtpConfig().getHost();
    }

    @Override
    public int getPort() {
        return this.getFtpConfig().getPort();
    }

    @Override
    public String getUsername() {
        return this.getFtpConfig().getUsername();
    }

    @Override
    public String getPassword() {
        return this.getFtpConfig().getPassword();
    }

    @Override
    public String getBasePath() {
        return this.getFtpConfig().getBasePath();
    }

    @Override
    public String getUploadPath() {
        return this.getFtpConfig().getUploadPath();
    }

    @Override
    public String getCharset() {
        return this.getFtpConfig().getCharset();
    }

    @Override
    public FtpMode getMode() {
        return this.getFtpConfig().getMode();
    }
}
