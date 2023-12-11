package com.zoe.framework.components.starter.metrics.prometheus.log4j2;

import io.micrometer.core.instrument.binder.logging.Log4j2Metrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.CompositeFilter;
import org.apache.logging.log4j.core.jmx.RingBufferAdminMBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.Arrays;

/**
 * @author 蒋时华
 * @date 2021-09-28 12:57:37
 */
@Configuration(proxyBeanMethods = false)
@Slf4j
@ConditionalOnEnabledHealthIndicator("prometheus")
@ConditionalOnClass({LogManager.class, LoggerContext.class, RingBufferAdminMBean.class})
@Order(Ordered.LOWEST_PRECEDENCE)
@ConditionalOnProperty(
        name = {"management.metrics.enable.log4j2.count", "zoe.metrics.enabled"},
        havingValue = "true",
        matchIfMissing = true
)
public class Log4j2CountExportAutoConfiguration {

    @Autowired
    private ObjectProvider<PrometheusMeterRegistry> meterRegistry;

    private final String className = "io.micrometer.core.instrument.binder.logging.Log4j2Metrics$MetricsFilter";

    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        // 注入log4j2 统计指标
        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        org.apache.logging.log4j.core.config.Configuration configuration = loggerContext.getConfiguration();
        LoggerConfig loggerConfig = configuration.getRootLogger();
        Filter logFilter = loggerConfig.getFilter();
        if (logFilter == null) {
            new Log4j2Metrics().bindTo(meterRegistry.getObject());
            return;
        }
        if ((logFilter instanceof CompositeFilter && Arrays.stream(((CompositeFilter) logFilter).getFiltersArray())
                .anyMatch(innerFilter -> className.equals(innerFilter.getClass().getName())))) {
            return;
        }
        if (className.equals(logFilter.getClass().getName())) {
            return;
        }
        new Log4j2Metrics().bindTo(meterRegistry.getObject());
    }

}

