package com.skrstop.framework.components.starter.metrics.prometheus.log4j2;

import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.apache.logging.log4j.LogManager;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.ObjectProvider;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Log4j2RingBufferExportAutoConfiguration 单元测试
 */
public class Log4j2RingBufferExportAutoConfigurationTest {

    private Log4j2RingBufferExportAutoConfiguration newConfiguration(PrometheusMeterRegistry registry) throws Exception {
        Log4j2RingBufferExportAutoConfiguration configuration = new Log4j2RingBufferExportAutoConfiguration();
        Field field = Log4j2RingBufferExportAutoConfiguration.class.getDeclaredField("meterRegistry");
        field.setAccessible(true);
        field.set(configuration, new ObjectProvider<PrometheusMeterRegistry>() {
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
        });
        return configuration;
    }

    private Number remainingCapacity(String ctxName, String cfgName) throws Exception {
        Log4j2RingBufferExportAutoConfiguration configuration = newConfiguration(null);
        Method method = Log4j2RingBufferExportAutoConfiguration.class
                .getDeclaredMethod("getRingBufferRemainingCapacity", String.class, String.class);
        method.setAccessible(true);
        return (Number) method.invoke(configuration, ctxName, cfgName);
    }

    @Test
    public void testInitRegistersGauges() throws Exception {
        // 向当前 Configuration 注册命名 LoggerConfig，保证 getLoggers() 包含 root 与命名项
        org.apache.logging.log4j.core.LoggerContext ctx =
                (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        ctx.getConfiguration().addLogger("ringBufferTestLogger",
                new org.apache.logging.log4j.core.config.LoggerConfig(
                        "ringBufferTestLogger", org.apache.logging.log4j.Level.INFO, false));
        // 空 key（root）覆盖 isBlank 命名分支
        ctx.getConfiguration().addLogger("",
                new org.apache.logging.log4j.core.config.LoggerConfig(
                        "", org.apache.logging.log4j.Level.INFO, false));
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        Log4j2RingBufferExportAutoConfiguration configuration;
        try {
            configuration = newConfiguration(registry);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        configuration.init();
        // 第二次调用被 isInitialized 拦截
        configuration.init();
        Assert.assertFalse("no meters, count=" + registry.getMeters().size(),
                registry.getMeters().isEmpty());
        // 调用 gauge.value() 触发取值 lambda（真实 MBean 不存在 → InstanceNotFoundException → -1）
        Assert.assertFalse("no gauges", registry.getMeters().isEmpty());
        // 直接调用 gauge 的取值函数（真实 MBean 不存在 → InstanceNotFoundException → -1）
        for (io.micrometer.core.instrument.Meter meter : registry.getMeters()) {
            java.lang.reflect.Field fnField = meter.getClass().getDeclaredField("value");
            fnField.setAccessible(true);
            java.util.function.ToDoubleFunction<Object> fn =
                    (java.util.function.ToDoubleFunction<Object>) fnField.get(meter);
            Assert.assertTrue(fn.applyAsDouble(configuration) < 0);
        }
    }

    @Test
    public void testInitWithNullRegistry() {
        // 确保当前 configuration 存在 logger 项
        org.apache.logging.log4j.core.LoggerContext ctx =
                (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        ctx.getConfiguration().addLogger("ringBufferNullRegistryLogger",
                new org.apache.logging.log4j.core.config.LoggerConfig(
                        "ringBufferNullRegistryLogger", org.apache.logging.log4j.Level.INFO, false));
        // registry 为 null → register 抛异常 → 外层 catch
        try {
            newConfiguration(null).init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRemainingCapacityFromMBean() throws Exception {
        // 注册一个符合 RingBufferAdminMBean 命名模式的 dummy MBean → getAttribute 成功
        String ctxName = ((org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false)).getName();
        String pattern = (String) Class.forName("org.apache.logging.log4j.core.jmx.RingBufferAdminMBean")
                .getField("PATTERN_ASYNC_LOGGER_CONFIG").get(null);
        ObjectName objectName = new ObjectName(String.format(pattern, ctxName, "root"));
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        server.registerMBean(new RingBufferDummy(), objectName);
        try {
            Assert.assertEquals(42L, remainingCapacity(ctxName, "root").longValue());
        } finally {
            server.unregisterMBean(objectName);
        }
    }

    @Test
    public void testRemainingCapacityNotFound() throws Exception {
        // MBean 不存在 → InstanceNotFoundException → -1
        Assert.assertEquals(-1L, remainingCapacity("defaultCtx", "no-such-config").longValue());
    }

    @Test
    public void testRemainingCapacityInvalidName() throws Exception {
        // ObjectName 非法（未闭合引号）→ MalformedObjectNameException → generic catch → -1
        Assert.assertEquals(-1L, remainingCapacity("ctx", "bad\"cfg").longValue());
    }

    public interface RingBufferDummyMBean {
        long getRemainingCapacity();
    }

    public static class RingBufferDummy implements RingBufferDummyMBean {
        @Override
        public long getRemainingCapacity() {
            return 42L;
        }
    }
}
