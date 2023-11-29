package com.zoe.framework.components.starter.metrics.prometheus.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.DruidDataSourceStatManager;
import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.alibaba.druid.support.http.stat.WebAppStatManager;
import com.zoe.framework.components.util.constant.StringPoolConst;
import com.zoe.framework.components.util.value.data.CollectionUtil;
import com.zoe.framework.components.util.value.data.StrUtil;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 蒋时华
 * @date 2021-12-20 16:21:39
 */
@Slf4j
public class DruidCollector extends Collector {

    private final static String[] DRUID_METRICS_NAMES = {
            "WaitThreadCount", "NotEmptyWaitCount", "NotEmptyWaitMillis", "PoolingCount", "PoolingPeak", "ActiveCount",
            "ActivePeak", "InitialSize", "MinIdle", "MaxActive", "QueryTimeout", "TransactionQueryTimeout", "LoginTimeout",
            "LogicConnectCount", "LogicCloseCount", "LogicConnectErrorCount", "PhysicalConnectCount", "PhysicalCloseCount",
            "PhysicalConnectErrorCount", "ExecuteCount", "ErrorCount", "CommitCount", "RollbackCount", "PSCacheAccessCount",
            "PSCacheHitCount", "PSCacheMissCount", "StartTransactionCount", "ClobOpenCount", "BlobOpenCount", "KeepAliveCheckCount",
            "MaxWait", "MaxWaitThreadCount", "MaxPoolPreparedStatementPerConnectionSize", "RecycleErrorCount",
            "PreparedStatementOpenCount", "PreparedStatementClosedCount", "ExecuteUpdateCount", "ExecuteQueryCount", "ExecuteBatchCount",
    };

    private final static String[] DRUID_METRICS_HISTOGRAM_NAMES = {"TransactionHistogram", "ConnectionHoldTimeHistogram"};

    private final static String[] DRUID_METRICS_SQL_NAMES = {"ExecuteCount", "FetchRowCount", "TotalTime", "MaxTimespan", "RunningCount", "ErrorCount", "ConcurrentMax"};

    private final static String[] DRUID_METRICS_SQL_HISTOGRAM_NAMES = {"Histogram", "FetchRowCountHistogram", "EffectedRowCountHistogram", "ExecuteAndResultHoldTimeHistogram"};

    private final static String[] DRUID_METRICS_URI_NAMES = {"RequestCount", "RequestTimeMillisMax", "RequestTimeMillis", "RunningCount", "ConcurrentMax", "JdbcExecuteTimeMillis", "JdbcExecuteCount", "JdbcExecuteErrorCount"};

    private final static String[] DRUID_METRICS_URI_HISTOGRAM_NAMES = {"Histogram"};

    private final static String SQL_NAME_LAST_ERROR_TIME = "LastErrorTime";
    private static final String NAME_POOL = "pool";
    private static final String NAME_SQL = "sql";
    private static final String NAME_LE = "le";
    private static final String NAME_CLASS = "class";
    private static final String NAME_MESSAGE = "message";
    private static final String NAME_URI = "uri";

    private static final String KEY_NAME = "Name";
    private static final String KEY_SQL = "SQL";
    private static final String KEY_URI = "URI";
    private static final String KEY_URL = "URL";
    private static final String KEY_ERR_CLS = "LastErrorClass";
    private static final String KEY_ERR_MSG = "LastErrorMessage";

    private static final String LABEL_PRE_DRUID = "druid_";
    private static final String LABEL_PRE_SQL = "druid_sql_";
    private static final String LABEL_PRE_URI = "druid_uri_";
    private static final String LABEL_SUF_BUCKET = "_bucket";

    private static final String HELP_PRE_DRUID = "Druid ";
    private static final String HELP_PRE_SQL = "Druid SQL ";
    private static final String HELP_PRE_URI = "Druid URI ";

    private final String[] BUCKETS = {"1ms", "10ms", "100ms", "1s", "10s", "100s", "Inf"};

    private final static Pattern PATTERN_CAMEL = Pattern.compile("(?<=[a-z])(?=[A-Z])");
    private final static Pattern PATTERN_SPACE = Pattern.compile("\\s+");

    private final List<String> LABEL_NAMES;
    private final List<String> LABEL_HISTOGRAM_NAMES;
    private final List<String> LABEL_SQL_NAMES;
    private final List<String> LABEL_SQL_HISTOGRAM_NAMES;
    private final List<String> LABEL_SQL_ERROR_NAMES;
    private final List<String> LABEL_URI_NAMES;
    private final List<String> LABEL_URI_HISTOGRAM_NAMES;

    private final Function<Map<String, Object>, List<String>> LABEL_VALUES_FUNCTION;
    private final BiFunction<Map<String, Object>, String, List<String>> LABEL_HISTOGRAM_VALUES_FUNCTION;
    private final Function<Map<String, Object>, List<String>> LABEL_SQL_VALUES_FUNCTION;
    private final Function<Map<String, Object>, List<String>> LABEL_SQL_ERROR_VALUES_FUNCTION;
    private final BiFunction<Map<String, Object>, String, List<String>> LABEL_SQL_HISTOGRAM_VALUES_FUNCTION;
    private final Function<Map<String, Object>, List<String>> LABEL_URI_VALUES_FUNCTION;
    private final BiFunction<Map<String, Object>, String, List<String>> LABEL_URI_HISTOGRAM_VALUES_FUNCTION;


    private final List<String> DB_INFO_LABELS = CollectionUtil.newArrayList("dbType", "dbHost", "dbName");

    private boolean enableSql;
    private boolean enableUri;

    private static String reduceSpace(String str) {
        return PATTERN_SPACE.matcher(str).replaceAll(" ");
    }

    private static String camelToSnake(String str) {
        return Stream.of(PATTERN_CAMEL.split(str)).filter(s -> s != null && !s.isEmpty()).map(String::toLowerCase).collect(Collectors.joining("_"));
    }

    public DruidCollector(Map<String, String> tags, boolean enableSql, boolean enableUri) {
        this.enableSql = enableSql;
        this.enableUri = enableUri;

        Set<Map.Entry<String, String>> tagEntry = tags.entrySet();

        List<String> tagKey = tagEntry.stream().map(Map.Entry::getKey).collect(Collectors.toList());
        List<String> tagVal = tagEntry.stream().map(Map.Entry::getValue).collect(Collectors.toList());

        LABEL_NAMES = Stream.concat(tagKey.stream(), Stream.of(NAME_POOL)).collect(Collectors.toList());
        LABEL_HISTOGRAM_NAMES = Stream.concat(tagKey.stream(), Stream.of(NAME_POOL, NAME_LE)).collect(Collectors.toList());

        LABEL_SQL_NAMES = Stream.concat(tagKey.stream(), Stream.of(NAME_POOL, NAME_SQL)).collect(Collectors.toList());
        LABEL_SQL_HISTOGRAM_NAMES = Stream.concat(tagKey.stream(), Stream.of(NAME_POOL, NAME_SQL, NAME_LE)).collect(Collectors.toList());
        LABEL_SQL_ERROR_NAMES = Stream.concat(tagKey.stream(), Stream.of(NAME_POOL, NAME_SQL, NAME_CLASS, NAME_MESSAGE)).collect(Collectors.toList());

        LABEL_URI_NAMES = Stream.concat(tagKey.stream(), Stream.of(NAME_URI)).collect(Collectors.toList());
        LABEL_URI_HISTOGRAM_NAMES = Stream.concat(tagKey.stream(), Stream.of(NAME_URI, NAME_LE)).collect(Collectors.toList());

        // func
        LABEL_VALUES_FUNCTION = map -> Stream.concat(tagVal.stream(), Stream.of(StrUtil.toString(map.get(KEY_NAME), StringPoolConst.EMPTY))).collect(Collectors.toList());
        LABEL_HISTOGRAM_VALUES_FUNCTION = (map, le) -> Stream.concat(tagVal.stream(), Stream.of(StrUtil.toString(map.get(KEY_NAME), StringPoolConst.EMPTY), le)).collect(Collectors.toList());

        LABEL_SQL_VALUES_FUNCTION = map -> Stream.concat(tagVal.stream(), Stream.of(StrUtil.toString(map.get(KEY_NAME), StringPoolConst.EMPTY), reduceSpace(StrUtil.toString(map.get(KEY_SQL), StringPoolConst.EMPTY)))).collect(Collectors.toList());
        LABEL_SQL_HISTOGRAM_VALUES_FUNCTION = (map, le) -> Stream.concat(tagVal.stream(), Stream.of(StrUtil.toString(map.get(KEY_NAME), StringPoolConst.EMPTY), reduceSpace(StrUtil.toString(map.get(KEY_SQL), StringPoolConst.EMPTY)), le)).collect(Collectors.toList());
        LABEL_SQL_ERROR_VALUES_FUNCTION = map -> Stream.concat(tagVal.stream(), Stream.of(StrUtil.toString(map.get(KEY_NAME), StringPoolConst.EMPTY), reduceSpace(StrUtil.toString(map.get(KEY_SQL), StringPoolConst.EMPTY)), StrUtil.toString(map.get(KEY_ERR_CLS), StringPoolConst.EMPTY), StrUtil.toString(map.get(KEY_ERR_MSG), StringPoolConst.EMPTY))).collect(Collectors.toList());

        LABEL_URI_VALUES_FUNCTION = map -> Stream.concat(tagVal.stream(), Stream.of(StrUtil.toString(map.get(KEY_URI), StringPoolConst.EMPTY))).collect(Collectors.toList());
        LABEL_URI_HISTOGRAM_VALUES_FUNCTION = (map, le) -> Stream.concat(tagVal.stream(), Stream.of(StrUtil.toString(map.get(KEY_URI), StringPoolConst.EMPTY), le)).collect(Collectors.toList());
    }

    private List<String> getDbLabelsValues(Map<String, Object> stat) {
        ArrayList<String> values = CollectionUtil.newArrayList();
        // dbType
        String dbType = StrUtil.toString(stat.get("DbType"), StringPoolConst.EMPTY);
        values.add(dbType);
        try {
            String url = StrUtil.subAfter(StrUtil.subBefore(stat.get(KEY_URL).toString(), "?", false), "//", true);
            List<String> split = StrUtil.split(url, "/");
            // dbHost
            values.add(split.get(0));
            // dbName
            values.add(split.get(1));
        } catch (Exception e) {
            values.add(StringPoolConst.EMPTY);
            values.add(StringPoolConst.EMPTY);
        }
        return values;
    }

    @Override
    public List<MetricFamilySamples> collect() {

        List<MetricFamilySamples> list = null;

        try {
            DruidStatManagerFacade druidStatManagerFacade = DruidStatManagerFacade.getInstance();
            List<Map<String, Object>> statList = druidStatManagerFacade.getDataSourceStatDataList();

            List<Map<String, Object>> sqlList = getSqlStatData(statList);
            List<Map<String, Object>> uriList = getUriStatData();

            int initialCapacity =
                    statList.size() * (DRUID_METRICS_NAMES.length + DRUID_METRICS_HISTOGRAM_NAMES.length) +
                            sqlList.size() * (DRUID_METRICS_SQL_NAMES.length + DRUID_METRICS_SQL_HISTOGRAM_NAMES.length + 1) +
                            uriList.size() * (DRUID_METRICS_URI_NAMES.length + DRUID_METRICS_URI_HISTOGRAM_NAMES.length);

            list = new ArrayList<>(initialCapacity);

            Stream.of(DRUID_METRICS_NAMES)
                    .map(name -> createGauge(name, statList, m -> (Number) m.get(name))).forEach(list::add);
            Stream.of(DRUID_METRICS_HISTOGRAM_NAMES)
                    .map(name -> createHistogram(name, statList, m -> (long[]) m.get(name))).forEach(list::add);

            if (enableSql) {
                Stream.of(DRUID_METRICS_SQL_NAMES)
                        .map(name -> createSqlGauge(name, sqlList, m -> (Number) m.get(name))).forEach(list::add);
                Stream.of(DRUID_METRICS_SQL_HISTOGRAM_NAMES)
                        .map(name -> createSqlHistogram(name, sqlList, m -> (long[]) m.get(name))).forEach(list::add);

                list.add(createSqlErrorGauge(SQL_NAME_LAST_ERROR_TIME, sqlList, m -> (Date) m.get(SQL_NAME_LAST_ERROR_TIME)));
            }

            if (enableUri) {
                Stream.of(DRUID_METRICS_URI_NAMES)
                        .map(name -> createUriGauge(name, uriList, m -> (Number) m.get(name))).forEach(list::add);
                Stream.of(DRUID_METRICS_URI_HISTOGRAM_NAMES)
                        .map(name -> createUriHistogram(name, uriList, m -> (long[]) m.get(name))).forEach(list::add);
            }
        } catch (Exception e) {
            log.error("获取druid监控指标失败", e);
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    private List<Map<String, Object>> getSqlStatData(List<Map<String, Object>> statList) {
        if (!enableSql) {
            return Collections.emptyList();
        }
        Map<String, Map<String, Object>> dataSourcesInfo = new HashMap<>();
        for (int i = 0; i < statList.size(); i++) {
            Map<String, Object> stringObjectMap = statList.get(i);
            String key = stringObjectMap.get(KEY_NAME).toString();
            Map<String, Object> info = new HashMap<>();
            info.put(KEY_NAME, stringObjectMap.get(KEY_NAME));
            info.put(KEY_URL, stringObjectMap.get(KEY_URL));
            dataSourcesInfo.put(key, info);
        }
        DruidStatManagerFacade druidStatManagerFacade = DruidStatManagerFacade.getInstance();
        Set<Object> dataSources = DruidDataSourceStatManager.getInstances().keySet();
        return dataSources.stream().flatMap(obj -> {
            List<Map<String, Object>> sqlStatDataList = druidStatManagerFacade.getSqlStatDataList(obj);
            try {
                String name = ((DruidDataSource) obj).getName();
                Map<String, Object> datasourceInfo = dataSourcesInfo.get(name);
                for (Map<String, Object> stringObjectMap : sqlStatDataList) {
                    stringObjectMap.putAll(datasourceInfo);
                }
            } catch (Exception e) {
                // no nothing
            }
            return sqlStatDataList.stream();
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> getUriStatData() {
        if (!enableUri) {
            return Collections.emptyList();
        }
        WebAppStatManager webAppStatManager = WebAppStatManager.getInstance();
        return webAppStatManager.getURIStatData();
    }

    private GaugeMetricFamily createGauge(String metric, List<Map<String, Object>> list, Function<Map<String, Object>, Number> metricValueFunc) {
        ArrayList<String> labels = (ArrayList<String>) CollectionUtil.addAll(CollectionUtil.newArrayList(LABEL_NAMES), DB_INFO_LABELS);
        GaugeMetricFamily metricFamily = new GaugeMetricFamily(LABEL_PRE_DRUID + camelToSnake(metric), HELP_PRE_DRUID + metric, labels);
        list.forEach((m) -> metricFamily.addMetric(
                (ArrayList<String>) CollectionUtil.addAll(CollectionUtil.newArrayList(LABEL_VALUES_FUNCTION.apply(m)), getDbLabelsValues(m))
                , metricValueFunc.apply(m).doubleValue()));
        return metricFamily;
    }

    private GaugeMetricFamily createHistogram(String metric, List<Map<String, Object>> list, Function<Map<String, Object>, long[]> metricValueFunc) {
        ArrayList<String> labels = (ArrayList<String>) CollectionUtil.addAll(CollectionUtil.newArrayList(LABEL_HISTOGRAM_NAMES), DB_INFO_LABELS);
        GaugeMetricFamily metricFamily = new GaugeMetricFamily(LABEL_PRE_DRUID + camelToSnake(metric) + LABEL_SUF_BUCKET
                , HELP_PRE_DRUID + metric, labels);
        list.forEach((m) -> {
            long[] data = metricValueFunc.apply(m);
            for (int i = 0; i < data.length && i < BUCKETS.length; i++) {
                metricFamily.addMetric(
                        (ArrayList<String>) CollectionUtil.addAll(CollectionUtil.newArrayList(LABEL_HISTOGRAM_VALUES_FUNCTION.apply(m, BUCKETS[i])), getDbLabelsValues(m))
                        , data[i]);
            }
        });
        return metricFamily;
    }

    private GaugeMetricFamily createSqlGauge(String metric, List<Map<String, Object>> list, Function<Map<String, Object>, Number> metricValueFunc) {
        ArrayList<String> labels = (ArrayList<String>) CollectionUtil.addAll(CollectionUtil.newArrayList(LABEL_SQL_NAMES), DB_INFO_LABELS);
        GaugeMetricFamily metricFamily = new GaugeMetricFamily(LABEL_PRE_SQL + camelToSnake(metric), HELP_PRE_SQL + metric, labels);
        list.forEach((m) -> metricFamily.addMetric(
                (ArrayList<String>) CollectionUtil.addAll(CollectionUtil.newArrayList(LABEL_SQL_VALUES_FUNCTION.apply(m)), getDbLabelsValues(m))
                , metricValueFunc.apply(m).doubleValue()));
        return metricFamily;
    }

    private GaugeMetricFamily createSqlErrorGauge(String metric, List<Map<String, Object>> list, Function<Map<String, Object>, Date> metricValueFunc) {
        ArrayList<String> labels = (ArrayList<String>) CollectionUtil.addAll(CollectionUtil.newArrayList(LABEL_SQL_ERROR_NAMES), DB_INFO_LABELS);
        GaugeMetricFamily metricFamily = new GaugeMetricFamily(LABEL_PRE_SQL + camelToSnake(metric), HELP_PRE_SQL + metric, labels);
        list.forEach((m) -> {
            Date date = metricValueFunc.apply(m);
            if (date != null) {
                metricFamily.addMetric(
                        (ArrayList<String>) CollectionUtil.addAll(CollectionUtil.newArrayList(LABEL_SQL_ERROR_VALUES_FUNCTION.apply(m)), getDbLabelsValues(m))
                        , date.getTime());
            }
        });
        return metricFamily;
    }

    private GaugeMetricFamily createSqlHistogram(String metric, List<Map<String, Object>> list, Function<Map<String, Object>, long[]> metricValueFunc) {
        ArrayList<String> labels = (ArrayList<String>) CollectionUtil.addAll(CollectionUtil.newArrayList(LABEL_SQL_HISTOGRAM_NAMES), DB_INFO_LABELS);
        GaugeMetricFamily metricFamily = new GaugeMetricFamily(LABEL_PRE_SQL + camelToSnake(metric) + LABEL_SUF_BUCKET
                , HELP_PRE_SQL + metric, labels);
        list.forEach((m) -> {
            long[] data = metricValueFunc.apply(m);
            for (int i = 0; i < data.length && i < BUCKETS.length; i++) {
                metricFamily.addMetric(
                        (ArrayList<String>) CollectionUtil.addAll(CollectionUtil.newArrayList(LABEL_SQL_HISTOGRAM_VALUES_FUNCTION.apply(m, BUCKETS[i])), getDbLabelsValues(m))
                        , data[i]);
            }
        });
        return metricFamily;
    }

    private GaugeMetricFamily createUriGauge(String metric, List<Map<String, Object>> list, Function<Map<String, Object>, Number> metricValueFunc) {
        ArrayList<String> labels = (ArrayList<String>) CollectionUtil.addAll(CollectionUtil.newArrayList(LABEL_URI_NAMES), DB_INFO_LABELS);
        GaugeMetricFamily metricFamily = new GaugeMetricFamily(LABEL_PRE_URI + camelToSnake(metric), HELP_PRE_URI + metric, labels);
        list.forEach((m) -> metricFamily.addMetric(
                (ArrayList<String>) CollectionUtil.addAll(CollectionUtil.newArrayList(LABEL_URI_VALUES_FUNCTION.apply(m)), getDbLabelsValues(m))
                , metricValueFunc.apply(m).doubleValue()));
        return metricFamily;
    }

    private GaugeMetricFamily createUriHistogram(String metric, List<Map<String, Object>> list, Function<Map<String, Object>, long[]> metricValueFunc) {
        ArrayList<String> labels = (ArrayList<String>) CollectionUtil.addAll(CollectionUtil.newArrayList(LABEL_URI_HISTOGRAM_NAMES), DB_INFO_LABELS);
        GaugeMetricFamily metricFamily = new GaugeMetricFamily(LABEL_PRE_URI + camelToSnake(metric) + LABEL_SUF_BUCKET
                , HELP_PRE_URI + metric, labels);
        list.forEach((m) -> {
            long[] data = metricValueFunc.apply(m);
            for (int i = 0; i < data.length && i < BUCKETS.length; i++) {
                metricFamily.addMetric(
                        (ArrayList<String>) CollectionUtil.addAll(CollectionUtil.newArrayList(LABEL_URI_HISTOGRAM_VALUES_FUNCTION.apply(m, BUCKETS[i])), getDbLabelsValues(m))
                        , data[i]);
            }
        });
        return metricFamily;
    }

    public boolean isEnableSql() {
        return enableSql;
    }

    public void setEnableSql(boolean enableSql) {
        this.enableSql = enableSql;
    }

    public boolean isEnableUri() {
        return enableUri;
    }

    public void setEnableUri(boolean enableUri) {
        this.enableUri = enableUri;
    }
}
