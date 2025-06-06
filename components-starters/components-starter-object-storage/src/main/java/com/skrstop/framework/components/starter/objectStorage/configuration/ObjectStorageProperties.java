package com.skrstop.framework.components.starter.objectStorage.configuration;

import com.skrstop.framework.components.starter.objectStorage.constant.GlobalConfigConst;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * ObjectStorageProperties
 *
 * @author 蒋时华
 * @date 2019/7/2
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(GlobalConfigConst.OBJECT_STORAGE_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
@RefreshScope
public class ObjectStorageProperties {

    @NestedConfigurationProperty
    private FtpProperties ftp = new FtpProperties();

    @NestedConfigurationProperty
    private CosProperties cos = new CosProperties();

    @NestedConfigurationProperty
    private OssProperties oss = new OssProperties();

}
