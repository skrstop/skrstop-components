package com.jphoebe.framework.components.starter.mongodb.config;

import com.jphoebe.framework.components.starter.mongodb.constant.GlobalConfigConst;
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
@ConfigurationProperties(GlobalConfigConst.MONGODB_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalMongodbConfig {

    /**
     * 扫描实体类路径
     */
    private String mapPackage;

    /**
     * 属性是否是下划线格式， 默认：false
     */
    private Boolean underlineProperties;

    public GlobalMongodbConfig() {
        this.mapPackage = "com.jphoebe.business";
        this.underlineProperties = false;
    }

}
