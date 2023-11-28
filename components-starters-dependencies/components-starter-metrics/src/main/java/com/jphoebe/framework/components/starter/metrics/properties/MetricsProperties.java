package com.jphoebe.framework.components.starter.metrics.properties;

import com.jphoebe.framework.components.core.common.serializable.SerializableBean;
import com.jphoebe.framework.components.starter.metrics.constant.GlobalConfigConst;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author 蒋时华
 * @date 2021-09-07 17:33:18
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(GlobalConfigConst.METRICS_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MetricsProperties extends SerializableBean {
    private static final long serialVersionUID = 8497996001371661542L;

    private Boolean enabled;

    public MetricsProperties() {
        this.enabled = true;
        this.process = new Process();
    }

    @NestedConfigurationProperty
    private Process process;

    @Getter
    @Setter
    static class Process {

        private Boolean enabled;

        public Process() {
            this.enabled = true;
        }
    }

}
