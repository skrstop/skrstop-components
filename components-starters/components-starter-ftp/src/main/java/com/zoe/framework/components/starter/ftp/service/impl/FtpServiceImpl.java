package com.zoe.framework.components.starter.ftp.service.impl;

import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
import com.zoe.framework.components.starter.ftp.configuration.GlobalFtpProperties;
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
    private GlobalFtpProperties globalFtpProperties;
    private Ftp ftp;
    @Getter
    private String path = "";

    public FtpServiceImpl(GlobalFtpProperties globalFtpProperties) {
        this.globalFtpProperties = globalFtpProperties;
        try {
            ftp = new Ftp(globalFtpProperties.getHost()
                    , globalFtpProperties.getPort()
                    , globalFtpProperties.getUsername()
                    , globalFtpProperties.getPassword()
                    , Charset.forName(globalFtpProperties.getCharset()));
        } catch (Exception e) {
            throw new IllegalArgumentException("FTP连接失败");
        }
        ftp.setMode(globalFtpProperties.getMode());
        if (StrUtil.isNoneBlank(globalFtpProperties.getBasePath())) {
            path += globalFtpProperties.getBasePath();
        }
        if (StrUtil.isNoneBlank(globalFtpProperties.getUploadPath())) {
            path += globalFtpProperties.getUploadPath();
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
    public GlobalFtpProperties getFtpConfig() {
        return this.globalFtpProperties;
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
