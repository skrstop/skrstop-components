package cn.auntec.framework.components.starter.metrics.prometheus.druid;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;

/**
 * @author 蒋时华
 * @date 2021-12-20 16:26:04
 */
public class DruidPrometheusManager {

    private final MetricsProperties properties;

    private DruidCollector collector;

    public DruidPrometheusManager(MetricsProperties properties) {
        this.properties = properties;

        if (this.isEnable()) {
            this.collector = new DruidCollector(properties.getTags(), isEnable("druid-sql"), isEnable("druid-uri"));
        }
    }

    public void registerCollector(PrometheusMeterRegistry registry) {
        if (this.isEnable() && this.collector != null) {
            collector.register(registry.getPrometheusRegistry());
        }
    }

    public boolean isEnable() {
        return isEnable("druid");
    }

    public boolean isEnable(String key) {
        return properties.getEnable().getOrDefault(key, false);
    }

    public DruidCollector getCollector() {
        return collector;
    }
}
