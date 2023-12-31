package com.zoe.framework.components.starter.objectStorage.service;

import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
import com.zoe.framework.components.starter.objectStorage.configuration.FtpProperties;

/**
 * FtpService class
 *
 * @author 蒋时华
 * @date 2019/7/2
 */
public interface FtpService {

    Ftp getFtpClient();

    String getPath();

    FtpProperties getFtpConfig();

    String getHost();

    int getPort();

    String getUsername();

    String getPassword();

    String getBasePath();

    String getUploadPath();

    String getCharset();

    FtpMode getMode();

}
