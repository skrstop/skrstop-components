package com.zoe.framework.components.starter.ftp.service.impl;

import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
import com.zoe.framework.components.starter.ftp.config.GlobalFtpConfig;
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
    private GlobalFtpConfig globalFtpConfig;
    private Ftp ftp;
    @Getter
    private String path = "";

    public FtpServiceImpl(GlobalFtpConfig globalFtpConfig) {
        this.globalFtpConfig = globalFtpConfig;
        try {
            ftp = new Ftp(globalFtpConfig.getHost()
                    , globalFtpConfig.getPort()
                    , globalFtpConfig.getUsername()
                    , globalFtpConfig.getPassword()
                    , Charset.forName(globalFtpConfig.getCharset()));
        } catch (Exception e) {
            throw new IllegalArgumentException("FTP连接失败");
        }
        ftp.setMode(globalFtpConfig.getMode());
        if (StrUtil.isNoneBlank(globalFtpConfig.getBasePath())) {
            path += globalFtpConfig.getBasePath();
        }
        if (StrUtil.isNoneBlank(globalFtpConfig.getUploadPath())) {
            path += globalFtpConfig.getUploadPath();
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
    public GlobalFtpConfig getFtpConfig() {
        return this.globalFtpConfig;
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
