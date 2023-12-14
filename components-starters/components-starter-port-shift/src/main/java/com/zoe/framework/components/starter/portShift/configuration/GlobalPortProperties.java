package com.zoe.framework.components.starter.portShift.configuration;

import com.zoe.framework.components.starter.portShift.constant.GlobalConfigConst;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * GlobalResponseConfig class
 *
 * @author 蒋时华
 * @date 2019/6/4
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(GlobalConfigConst.HTTP_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalPortProperties {

    /*** 是否开启端口偏移 */
    private boolean enable = true;

    /*** 指定具体端口捉着端口范围， eg. 8080 / 8080-9090 */
    private String portArea = "";

}
