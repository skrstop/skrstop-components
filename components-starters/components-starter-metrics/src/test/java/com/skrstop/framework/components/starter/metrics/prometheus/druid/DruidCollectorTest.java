package com.skrstop.framework.components.starter.metrics.prometheus.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.DruidDataSourceStatManager;
import com.alibaba.druid.stat.JdbcSqlStat;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DruidCollector / DruidPrometheusManager / DruidPrometheusAutoConfiguration 单元测试
 */
public class DruidCollectorTest {

    @After
    public void cleanUp() {
        DruidDataSourceStatManager.clear();
    }

    private DruidCollector newCollector(boolean sql, boolean uri) {
        Map<String, String> tags = new HashMap<>();
        tags.put("env", "test");
        return new DruidCollector(tags, sql, uri);
    }

    @Test
    public void testCollectWithoutSqlAndUri() {
        DruidCollector collector = newCollector(false, false);
        Assert.assertFalse(collector.isEnableSql());
        Assert.assertFalse(collector.isEnableUri());
        List<io.prometheus.client.Collector.MetricFamilySamples> samples = collector.collect();
        // 基础指标 family 均生成（样例内容取决于全局 druid 统计状态，不作强断言）
        Assert.assertFalse(samples.isEmpty());
        Assert.assertTrue(samples.stream().allMatch(s -> "druid_".equals(s.name.substring(0, 6))));
    }

    /**
     * 空实现 Standard MBean，满足 druid JMX 注册要求
     */
    public interface FakeDSMBean {
    }

    public static class FakeDS implements FakeDSMBean {
        /**
         * 缺少 Name 的统计项，覆盖 getSqlStatData 中 Name 为空的跳过分支
         */
        public Map<String, Object> getStatData() {
            Map<String, Object> map = new HashMap<>();
            map.put("URL", "jdbc:mysql://host:3306/db");
            return map;
        }
    }

    /**
     * 未连接的 DruidDataSource 子类，重写统计方法避免依赖真实连接
     */
    public static class FakeDruidDataSource extends DruidDataSource {
        private final Map<String, JdbcSqlStat> sqlStatMap = new HashMap<>();

        public FakeDruidDataSource() {
            JdbcSqlStat stat = new JdbcSqlStat("select 1");
            stat.incrementExecuteSuccessCount();
            sqlStatMap.put("select 1", stat);
        }

        @Override
        public String getName() {
            return "fakeDS";
        }

        @Override
        public Map<String, Object> getStatData() {
            Map<String, Object> map = new HashMap<>();
            map.put("Name", "fakeDS");
            map.put("URL", "jdbc:mysql://host:3306/testdb?x=y");
            map.put("WaitThreadCount", 1);
            map.put("TransactionHistogram", new long[]{1L, 2L});
            map.put("ConnectionHoldTimeHistogram", new long[]{1L});
            return map;
        }

        @Override
        public Map<String, JdbcSqlStat> getSqlStatMap() {
            return sqlStatMap;
        }
    }

    @Test
    public void testCollectWithSqlAndUri() {
        DruidCollector collector = newCollector(true, true);
        // 注册真实（未连接）数据源：statList/sqlList 非空，覆盖 sql 统计遍历成功路径
        DruidDataSourceStatManager.addDataSource(new FakeDruidDataSource(), "fakeDS");
        // 注册非 DruidDataSource 对象：覆盖 sql 统计遍历的类型转换异常分支
        DruidDataSourceStatManager.addDataSource(new FakeDS(), "badDS");
        List<io.prometheus.client.Collector.MetricFamilySamples> samples = collector.collect();
        Assert.assertNotNull(samples);
        Assert.assertFalse(samples.isEmpty());
        Assert.assertTrue(collector.isEnableSql());
        Assert.assertTrue(collector.isEnableUri());
    }

    @Test
    public void testGaugeWithFakeStatData() throws Exception {
        DruidCollector collector = newCollector(true, true);

        Map<String, Object> stat = new HashMap<>();
        stat.put("Name", "testDS");
        stat.put("URL", "jdbc:mysql://host:3306/testdb?useSsl=false");
        stat.put("WaitThreadCount", 1);
        stat.put("TransactionHistogram", new long[]{1L, 2L});
        stat.put("ExecuteCount", 3);
        stat.put("Histogram", new long[]{4L, 5L});
        stat.put("RequestCount", 6);
        stat.put("RequestTimeMillis", 7);
        stat.put("LastErrorTime", new Date());
        stat.put("SQL", "select  *  from  t");
        stat.put("LastErrorClass", "java.lang.Exception");
        stat.put("LastErrorMessage", "boom");
        List<Map<String, Object>> single = new java.util.ArrayList<>();
        single.add(stat);

        // 无 URL 的 map → getDbLabelsValues 异常分支
        Map<String, Object> noUrl = new HashMap<>();
        noUrl.put("Name", "noUrl");
        List<Map<String, Object>> noUrlList = new java.util.ArrayList<>();
        noUrlList.add(noUrl);

        Method createGauge = DruidCollector.class.getDeclaredMethod("createGauge",
                String.class, List.class, java.util.function.Function.class);
        createGauge.setAccessible(true);
        Assert.assertNotNull(createGauge.invoke(collector, "WaitThreadCount", single,
                (java.util.function.Function<Map<String, Object>, Number>) m -> 1));
        Assert.assertNotNull(createGauge.invoke(collector, "WaitThreadCount", noUrlList,
                (java.util.function.Function<Map<String, Object>, Number>) m -> 1));

        Method createHistogram = DruidCollector.class.getDeclaredMethod("createHistogram",
                String.class, List.class, java.util.function.Function.class);
        createHistogram.setAccessible(true);
        Assert.assertNotNull(createHistogram.invoke(collector, "TransactionHistogram", single,
                (java.util.function.Function<Map<String, Object>, long[]>) m -> new long[]{1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L}));

        Method createSqlGauge = DruidCollector.class.getDeclaredMethod("createSqlGauge",
                String.class, List.class, java.util.function.Function.class);
        createSqlGauge.setAccessible(true);
        Assert.assertNotNull(createSqlGauge.invoke(collector, "ExecuteCount", single,
                (java.util.function.Function<Map<String, Object>, Number>) m -> 3));
        // 指标缺失 → 值为 null，跳过样本
        Assert.assertNotNull(createSqlGauge.invoke(collector, "ExecuteCount", noUrlList,
                (java.util.function.Function<Map<String, Object>, Number>) m -> null));

        Method createSqlHistogram = DruidCollector.class.getDeclaredMethod("createSqlHistogram",
                String.class, List.class, java.util.function.Function.class);
        createSqlHistogram.setAccessible(true);
        Assert.assertNotNull(createSqlHistogram.invoke(collector, "Histogram", single,
                (java.util.function.Function<Map<String, Object>, long[]>) m -> new long[]{4L}));
        // 直方图数据缺失 → null 跳过
        Assert.assertNotNull(createSqlHistogram.invoke(collector, "Histogram", noUrlList,
                (java.util.function.Function<Map<String, Object>, long[]>) m -> null));
        // 直方图数据长于桶数量 → 超出部分跳过
        Assert.assertNotNull(createSqlHistogram.invoke(collector, "Histogram", single,
                (java.util.function.Function<Map<String, Object>, long[]>) m -> new long[]{1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L}));

        Method createSqlErrorGauge = DruidCollector.class.getDeclaredMethod("createSqlErrorGauge",
                String.class, List.class, java.util.function.Function.class);
        createSqlErrorGauge.setAccessible(true);
        // LastErrorTime 非空
        Assert.assertNotNull(createSqlErrorGauge.invoke(collector, "LastErrorTime", single,
                (java.util.function.Function<Map<String, Object>, Date>) m -> new Date()));
        // LastErrorTime 为空 → 跳过样本
        Assert.assertNotNull(createSqlErrorGauge.invoke(collector, "LastErrorTime", noUrlList,
                (java.util.function.Function<Map<String, Object>, Date>) m -> null));

        Method createUriGauge = DruidCollector.class.getDeclaredMethod("createUriGauge",
                String.class, List.class, java.util.function.Function.class);
        createUriGauge.setAccessible(true);
        Assert.assertNotNull(createUriGauge.invoke(collector, "RequestCount", single,
                (java.util.function.Function<Map<String, Object>, Number>) m -> 6));
        Assert.assertNotNull(createUriGauge.invoke(collector, "RequestCount", noUrlList,
                (java.util.function.Function<Map<String, Object>, Number>) m -> null));

        Method createUriHistogram = DruidCollector.class.getDeclaredMethod("createUriHistogram",
                String.class, List.class, java.util.function.Function.class);
        createUriHistogram.setAccessible(true);
        Assert.assertNotNull(createUriHistogram.invoke(collector, "Histogram", single,
                (java.util.function.Function<Map<String, Object>, long[]>) m -> new long[]{1L}));
        Assert.assertNotNull(createUriHistogram.invoke(collector, "Histogram", noUrlList,
                (java.util.function.Function<Map<String, Object>, long[]>) m -> null));
        Assert.assertNotNull(createUriHistogram.invoke(collector, "Histogram", single,
                (java.util.function.Function<Map<String, Object>, long[]>) m -> new long[]{1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L}));
    }

    @Test
    public void testCollectExceptionPath() {
        // getUriStatData 抛异常 → collect 的 catch 与 list==null 兜底分支
        Map<String, String> tags = new HashMap<>();
        tags.put("env", "test");
        DruidCollector collector = new DruidCollector(tags, false, false) {
            @Override
            protected List<Map<String, Object>> getUriStatData() {
                throw new IllegalStateException("boom");
            }
        };
        Assert.assertTrue(collector.collect().isEmpty());
    }

    @Test
    public void testCollectWithUriData() {
        // 通过子类注入 URI 统计数据，覆盖 URI gauge/histogram lambda
        Map<String, String> tags = new HashMap<>();
        tags.put("env", "test");
        DruidCollector collector = new DruidCollector(tags, false, true) {
            @Override
            protected List<Map<String, Object>> getUriStatData() {
                Map<String, Object> uri = new HashMap<>();
                uri.put("URI", "/test");
                uri.put("RequestCount", 5);
                uri.put("Histogram", new long[]{1L, 2L});
                return new java.util.ArrayList<>(java.util.Collections.singletonList(uri));
            }
        };
        List<io.prometheus.client.Collector.MetricFamilySamples> samples = collector.collect();
        Assert.assertFalse(samples.isEmpty());
    }

    @Test
    public void testEnableSetters() {
        DruidCollector collector = newCollector(false, false);
        collector.setEnableSql(true);
        collector.setEnableUri(true);
        Assert.assertTrue(collector.isEnableSql());
        Assert.assertTrue(collector.isEnableUri());
    }

    @Test
    public void testManager() {
        Map<String, Boolean> enable = new HashMap<>();
        enable.put("druid", true);
        enable.put("druid-sql", true);
        enable.put("druid-uri", true);
        org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties properties =
                new org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties();
        properties.getEnable().putAll(enable);
        DruidPrometheusManager manager = new DruidPrometheusManager(properties);
        Assert.assertTrue(manager.isEnable());
        Assert.assertTrue(manager.isEnable("druid-sql"));
        Assert.assertFalse(manager.isEnable("unknown"));
        Assert.assertNotNull(manager.getCollector());
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        manager.registerCollector(registry);
        // collector 已注册，能产出指标 family
        Assert.assertFalse(manager.getCollector().collect().isEmpty());

        // 未开启 druid → collector 为 null
        org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties off =
                new org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties();
        DruidPrometheusManager offManager = new DruidPrometheusManager(off);
        Assert.assertFalse(offManager.isEnable());
        Assert.assertNull(offManager.getCollector());
        offManager.registerCollector(new PrometheusMeterRegistry(PrometheusConfig.DEFAULT));
        // 构造后动态开启（enable map 为活引用）→ isEnable 为 true 但 collector 为 null
        off.getEnable().put("druid", Boolean.TRUE);
        Assert.assertTrue(offManager.isEnable());
        offManager.registerCollector(new PrometheusMeterRegistry(PrometheusConfig.DEFAULT));
    }

    @Test
    public void testAutoConfiguration() {
        Map<String, Boolean> enable = new HashMap<>();
        enable.put("druid", true);
        org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties properties =
                new org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties();
        properties.getEnable().putAll(enable);
        DruidPrometheusAutoConfiguration configuration =
                new DruidPrometheusAutoConfiguration(properties, new PrometheusMeterRegistry(PrometheusConfig.DEFAULT));
        Assert.assertNotNull(configuration.druidPrometheusManager());
    }
}
