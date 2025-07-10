package com.skrstop.framework.components.starter.objectStorage.configuration.dynamic;

import com.skrstop.framework.components.starter.objectStorage.configuration.CosProperties;
import com.skrstop.framework.components.starter.objectStorage.configuration.FtpProperties;
import com.skrstop.framework.components.starter.objectStorage.configuration.OssProperties;
import com.skrstop.framework.components.starter.objectStorage.constant.GlobalConfigConst;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.LinkedHashMap;

/**
 * ObjectStorageProperties
 *
 * @author 蒋时华
 * @date 2019/7/2
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(GlobalConfigConst.DYNAMIC_OBJECT_STORAGE_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
@RefreshScope
public class DynamicObjectStorageProperties {

    /*** 是否开启动态数据源 */
    private boolean enable = false;

    /*** 设置默认数据源 */
    private String primary = "default";

    /*** 当为true时，未找到指定的数据源时会抛出异常，为false时，则使用默认数据源 */
    private boolean exceptionWhileNotFound = false;

    /*** aop相关配置 */
    @NestedConfigurationProperty
    private Aop aop = new Aop();

    /*** service相关配置 */
    @NestedConfigurationProperty
    private Service service = new Service();

    /*** ftp数据源 */
    private LinkedHashMap<String, FtpProperties> ftpDataSources = new LinkedHashMap<>();

    /*** cos多数据源 */
    private LinkedHashMap<String, CosProperties> cosDataSources = new LinkedHashMap<>();

    /*** oss多数据源 */
    private LinkedHashMap<String, OssProperties> ossDataSources = new LinkedHashMap<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class Aop {
        /*** 开启aop注解 */
        private boolean enabled = true;
        /**
         * aop order
         */
        private Integer order = Ordered.HIGHEST_PRECEDENCE + 1;
        /**
         * aop allowedPublicOnly
         */
        private boolean allowedPublicOnly = true;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class Service {
        /*** 开启 DynamicObjectStorageService */
        private boolean enabled = true;
        /**
         * service order
         */
        private Integer order = Ordered.HIGHEST_PRECEDENCE;
    }
}
