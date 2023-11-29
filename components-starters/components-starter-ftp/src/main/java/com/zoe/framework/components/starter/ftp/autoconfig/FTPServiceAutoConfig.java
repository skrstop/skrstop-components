package com.zoe.framework.components.starter.ftp.autoconfig;

import com.zoe.framework.components.starter.ftp.config.GlobalFtpConfig;
import com.zoe.framework.components.starter.ftp.service.FtpService;
import com.zoe.framework.components.starter.ftp.service.impl.FtpServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * @author 蒋时华
 * @date 2020-05-11 20:00:01
 */
@Service
@EnableConfigurationProperties(GlobalFtpConfig.class)
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(value = "zoe.ftp.config.enable", havingValue = "true", matchIfMissing = true)
public class FTPServiceAutoConfig {

    @Bean
    public FtpService ftpService(GlobalFtpConfig globalFtpConfig) {
        return new FtpServiceImpl(globalFtpConfig);
    }

}
