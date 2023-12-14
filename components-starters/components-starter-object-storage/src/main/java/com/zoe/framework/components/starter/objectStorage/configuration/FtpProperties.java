package com.zoe.framework.components.starter.objectStorage.configuration;

import cn.hutool.extra.ftp.FtpMode;
import com.zoe.framework.components.util.enums.CharSetEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * FtpConfig class
 *
 * @author 蒋时华
 * @date 2019/7/2
 */
@Getter
@Setter
public class FtpProperties {

    private boolean enable = false;
    private String host;
    private int port = 21;
    private String username;
    private String password;
    private String basePath;
    private String uploadPath;
    private String charset = CharSetEnum.UTF8.getCharSet();
    private FtpMode mode = FtpMode.Active;

}
