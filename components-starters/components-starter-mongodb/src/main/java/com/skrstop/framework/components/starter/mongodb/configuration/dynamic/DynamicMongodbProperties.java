package com.skrstop.framework.components.starter.mongodb.configuration.dynamic;

import com.skrstop.framework.components.starter.mongodb.constant.GlobalConfigConst;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * mongo Config properties
 *
 * @author 蒋时华
 * @date 2019/7/2
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(GlobalConfigConst.MONGODB_DYNAMIC)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DynamicMongodbProperties {

    private Boolean enabled = false;

    /**
     * 必须设置默认的库,默认master
     */
    private String primary = "master";

    /**
     * 多数据配置数据源
     */
    private Map<String, MongoProperties> dataSources = new LinkedHashMap<>();

    /**
     * aop with default ds annotation
     */
    @NestedConfigurationProperty
    private DynamicMongoAopProperties aop = new DynamicMongoAopProperties();

}
