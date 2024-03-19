package com.skrstop.framework.components.starter.objectStorage.configuration;

import com.skrstop.framework.components.starter.objectStorage.service.ObjectStorageService;
import com.skrstop.framework.components.starter.objectStorage.service.impl.CosObjectStorageServiceImpl;
import com.skrstop.framework.components.starter.objectStorage.service.impl.FtpObjectStorageServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
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
@EnableConfigurationProperties(ObjectStorageProperties.class)
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(value = "skrstop.object-storage.dynamic.enable", havingValue = "false", matchIfMissing = true)
public class ObjectStorageServiceAutoConfiguration {

    @Autowired
    private ObjectStorageProperties objectStorageProperties;

    @PostConstruct
    private void init() {
        if (objectStorageProperties.getFtp().isEnable()
                && objectStorageProperties.getCos().isEnable()) {
            throw new IllegalArgumentException("ftp、cos无法同时开始，如果需要同时使用，请开启动态配置：skrstop.object-storage.dynamic");
        }
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnProperty(value = "skrstop.object-storage.ftp.enable", havingValue = "true", matchIfMissing = false)
    public ObjectStorageService ftpObjectStorage(ObjectStorageProperties objectStorageProperties) {
        return new FtpObjectStorageServiceImpl(objectStorageProperties.getFtp());
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnProperty(value = "skrstop.object-storage.cos.enable", havingValue = "true", matchIfMissing = false)
    public ObjectStorageService cosObjectStorage(ObjectStorageProperties objectStorageProperties) {
        return new CosObjectStorageServiceImpl(objectStorageProperties.getCos());
    }

}
