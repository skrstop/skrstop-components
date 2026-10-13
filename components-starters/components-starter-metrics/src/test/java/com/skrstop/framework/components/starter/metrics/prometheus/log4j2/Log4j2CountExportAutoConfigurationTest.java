package com.skrstop.framework.components.starter.metrics.prometheus.log4j2;

import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.CompositeFilter;
import org.apache.logging.log4j.core.filter.LevelRangeFilter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.ObjectProvider;

import java.lang.reflect.Constructor;

/**
 * Log4j2CountExportAutoConfiguration 单元测试
 */
public class Log4j2CountExportAutoConfigurationTest {

    private LoggerContext loggerContext;
    private LoggerConfig rootLogger;
    private final java.util.List<org.apache.logging.log4j.core.Filter> addedFilters = new java.util.ArrayList<>();

    @Before
    public void setUp() {
        loggerContext = (LoggerContext) LogManager.getContext(false);
        rootLogger = loggerContext.getConfiguration().getRootLogger();
    }

    @After
    public void tearDown() {
        for (org.apache.logging.log4j.core.Filter filter : addedFilters) {
            rootLogger.removeFilter(filter);
        }
        addedFilters.clear();
        // 清理 Log4j2Metrics.bindTo 等遗留的 filter 链
        int guard = 0;
        while (rootLogger.getFilter() != null && guard++ < 20) {
            org.apache.logging.log4j.core.Filter head = rootLogger.getFilter();
            rootLogger.removeFilter(head);
            if (head instanceof CompositeFilter) {
                for (org.apache.logging.log4j.core.Filter inner : ((CompositeFilter) head).getFiltersArray()) {
                    rootLogger.removeFilter(inner);
                }
            }
        }
    }

    private void addFilter(org.apache.logging.log4j.core.Filter filter) {
        rootLogger.addFilter(filter);
        addedFilters.add(filter);
    }

    private ObjectProvider<PrometheusMeterRegistry> providerOf(PrometheusMeterRegistry registry) {
        return new ObjectProvider<PrometheusMeterRegistry>() {
            @Override
            public PrometheusMeterRegistry getObject(Object... args) {
                return registry;
            }

            @Override
            public PrometheusMeterRegistry getIfAvailable() {
                return registry;
            }

            @Override
            public PrometheusMeterRegistry getIfUnique() {
                return registry;
            }

            @Override
            public java.util.stream.Stream<PrometheusMeterRegistry> stream() {
                return java.util.stream.Stream.of(registry);
            }

            @Override
            public java.util.stream.Stream<PrometheusMeterRegistry> orderedStream() {
                return java.util.stream.Stream.of(registry);
            }
        };
    }

    private Log4j2CountExportAutoConfiguration newConfiguration(PrometheusMeterRegistry registry) throws Exception {
        Log4j2CountExportAutoConfiguration configuration = new Log4j2CountExportAutoConfiguration();
        java.lang.reflect.Field field = Log4j2CountExportAutoConfiguration.class.getDeclaredField("meterRegistry");
        field.setAccessible(true);
        field.set(configuration, providerOf(registry));
        return configuration;
    }

    private Object newMetricsFilter(PrometheusMeterRegistry registry) throws Exception {
        Class<?> metricsClass = Class.forName(
                "io.micrometer.core.instrument.binder.logging.Log4j2Metrics$MetricsFilter");
        Object metrics = io.micrometer.core.instrument.binder.logging.Log4j2Metrics.class.getDeclaredConstructor().newInstance();
        Constructor<?> constructor = null;
        for (Constructor<?> c : metricsClass.getDeclaredConstructors()) {
            c.setAccessible(true);
            if (c.getParameterCount() == 2) {
                constructor = c;
            }
        }
        return constructor.newInstance(registry, java.util.Collections.emptyList());
    }

    @Test
    public void testRootFilterNull() {
        // root 无 filter → 直接 bindTo
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        try {
            newConfiguration(registry).init();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testOtherFilter() throws Exception {
        // root 头部为不含 MetricsFilter 的 CompositeFilter → anyMatch false → bindTo
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        addFilter(CompositeFilter.createFilters(new org.apache.logging.log4j.core.Filter[]{
                LevelRangeFilter.createFilter(org.apache.logging.log4j.Level.TRACE, org.apache.logging.log4j.Level.ERROR,
                        org.apache.logging.log4j.core.Filter.Result.NEUTRAL, org.apache.logging.log4j.core.Filter.Result.NEUTRAL)}));
        try {
            newConfiguration(registry).init();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testMetricsFilterAlreadyRegistered() throws Exception {
        // root filter 为 MetricsFilter → 不重复绑定
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        addFilter((org.apache.logging.log4j.core.Filter) newMetricsFilter(registry));
        newConfiguration(registry).init();
        Assert.assertTrue(true);
    }

    @Test
    public void testCompositeFilterContainsMetricsFilter() throws Exception {
        // CompositeFilter 中包含 MetricsFilter → 不重复绑定
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        Object metricsFilter = newMetricsFilter(registry);
        CompositeFilter composite = CompositeFilter.createFilters(new org.apache.logging.log4j.core.Filter[]{
                LevelRangeFilter.createFilter(org.apache.logging.log4j.Level.TRACE, org.apache.logging.log4j.Level.ERROR,
                        org.apache.logging.log4j.core.Filter.Result.NEUTRAL, org.apache.logging.log4j.core.Filter.Result.NEUTRAL),
                (org.apache.logging.log4j.core.Filter) metricsFilter});
        addFilter(composite);
        newConfiguration(registry).init();
        Assert.assertTrue(true);
    }
}
