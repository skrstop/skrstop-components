package com.skrstop.framework.components.starter.metrics.configuration;

import com.skrstop.framework.components.starter.metrics.constant.GlobalConfigConst;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.io.Serializable;

/**
 * @author 蒋时华
 * @date 2021-09-07 17:33:18
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(GlobalConfigConst.METRICS_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MetricsProperties implements Serializable {
    private static final long serialVersionUID = 8497996001371661542L;

    private boolean enabled = true;

}
