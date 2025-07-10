package com.skrstop.framework.components.starter.objectStorage.configuration;

import com.skrstop.framework.components.starter.objectStorage.service.ObjectStorageService;
import com.skrstop.framework.components.starter.objectStorage.service.impl.CosObjectStorageServiceImpl;
import com.skrstop.framework.components.starter.objectStorage.service.impl.FtpObjectStorageServiceImpl;
import com.skrstop.framework.components.starter.objectStorage.service.impl.OssObjectStorageServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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

    public ObjectStorageServiceAutoConfiguration(ObjectStorageProperties objectStorageProperties) {
        if (objectStorageProperties.getFtp().isEnable()
                && objectStorageProperties.getCos().isEnable()) {
            throw new IllegalArgumentException("objectStorage多数据源无法同时开始，如果需要同时使用，请使用动态配置：skrstop.object-storage.dynamic");
        }
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "skrstop.object-storage.ftp.enable", havingValue = "true", matchIfMissing = false)
    public ObjectStorageService ftpObjectStorage(ObjectStorageProperties objectStorageProperties) {
        return new FtpObjectStorageServiceImpl(objectStorageProperties.getFtp());
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "skrstop.object-storage.cos.enable", havingValue = "true", matchIfMissing = false)
    public ObjectStorageService cosObjectStorage(ObjectStorageProperties objectStorageProperties) {
        return new CosObjectStorageServiceImpl(objectStorageProperties.getCos());
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "ynw.object-storage.oss.enable", havingValue = "true", matchIfMissing = false)
    public ObjectStorageService ossObjectStorage(ObjectStorageProperties objectStorageProperties) {
        return new OssObjectStorageServiceImpl(objectStorageProperties.getOss());
    }

}
