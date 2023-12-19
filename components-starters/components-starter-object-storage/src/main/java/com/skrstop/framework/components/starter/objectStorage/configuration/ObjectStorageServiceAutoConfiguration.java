package com.skrstop.framework.components.starter.objectStorage.configuration;

import com.skrstop.framework.components.starter.objectStorage.service.FtpService;
import com.skrstop.framework.components.starter.objectStorage.service.impl.FtpServiceImpl;
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
@EnableConfigurationProperties(GlobalObjectStorageProperties.class)
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ObjectStorageServiceAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "skrstop.object-storage.ftp.enable", havingValue = "true", matchIfMissing = false)
    public FtpService ftpService(GlobalObjectStorageProperties globalObjectStorageProperties) {
        return new FtpServiceImpl(globalObjectStorageProperties.getFtp());
    }

}
