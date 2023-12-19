package com.skrstop.framework.components.starter.feign.protostuff.configuration;

import com.skrstop.framework.components.starter.feign.protostuff.constant.GlobalConfigConst;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author 蒋时华
 * @date 2020-05-18 12:22:55
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(GlobalConfigConst.FEIGN_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalFeignProperties {

    /*** feign扫描包路径，多个路径逗号分隔，也可以使用@EnableFeignClients */
    private String scanPackage;


}
