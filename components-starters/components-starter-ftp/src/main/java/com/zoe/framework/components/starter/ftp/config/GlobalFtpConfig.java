package com.zoe.framework.components.starter.ftp.config;

import cn.hutool.extra.ftp.FtpMode;
import com.zoe.framework.components.starter.ftp.constant.GlobalConfigConst;
import com.zoe.framework.components.util.constant.CharSetEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * FtpConfig class
 *
 * @author 蒋时华
 * @date 2019/7/2
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(GlobalConfigConst.FTP_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalFtpConfig {

    private Boolean enable;
    private String host;
    private int port;
    private String username;
    private String password;
    private String basePath;
    private String uploadPath;
    private String charset;
    private FtpMode mode;

    public GlobalFtpConfig() {
        enable = true;
        this.port = 21;
        this.charset = CharSetEnum.UTF8.getCharSet();
        this.mode = FtpMode.Active;
    }

}
