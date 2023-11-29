package com.zoe.framework.components.starter.metrics.prometheus.druid;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.boot.actuate.autoconfigure.metrics.export.prometheus.PrometheusMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 蒋时华
 * @date 2021-12-20 16:27:07
 */
@Configuration
@ConditionalOnClass({DruidStatManagerFacade.class, MetricsProperties.class})
@ConditionalOnBean(PrometheusMeterRegistry.class)
@EnableConfigurationProperties(MetricsProperties.class)
@AutoConfigureAfter(PrometheusMetricsExportAutoConfiguration.class)
@ConditionalOnEnabledHealthIndicator("prometheus")
public class DruidPrometheusAutoConfiguration {

    private DruidPrometheusManager druidPrometheusManager;

    public DruidPrometheusAutoConfiguration(
            MetricsProperties properties,
            PrometheusMeterRegistry registry
    ) {
        this.druidPrometheusManager = new DruidPrometheusManager(properties);
        this.druidPrometheusManager.registerCollector(registry);
    }

    @Bean
    public DruidPrometheusManager druidPrometheusManager() {
        return druidPrometheusManager;
    }

}
