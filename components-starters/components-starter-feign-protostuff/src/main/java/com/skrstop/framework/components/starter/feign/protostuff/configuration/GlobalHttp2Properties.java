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
@ConfigurationProperties(GlobalConfigConst.HTTP2_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalHttp2Properties {

    /*** 是否开启http2 */
    private boolean enable = false;
    /*** 输出info级别日志 */
    private boolean logInfoLevelForRequest = false;

}
