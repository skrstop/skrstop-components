package com.skrstop.framework.components.starter.database.configuration.dynamic.common;

import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.atomikos.AtomikosConfig;
import com.baomidou.dynamic.datasource.creator.beecp.BeeCpConfig;
import com.baomidou.dynamic.datasource.creator.dbcp.Dbcp2Config;
import com.baomidou.dynamic.datasource.creator.druid.DruidConfig;
import com.baomidou.dynamic.datasource.creator.hikaricp.HikariCpConfig;
import com.baomidou.dynamic.datasource.enums.SeataMode;
import com.baomidou.dynamic.datasource.strategy.DynamicDataSourceStrategy;
import com.baomidou.dynamic.datasource.strategy.LoadBalanceDynamicDataSourceStrategy;
import com.baomidou.dynamic.datasource.toolkit.CryptoUtils;
import com.skrstop.framework.components.starter.database.constant.GlobalConfigConst;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DynamicDataSourceProperties
 *
 * @author TaoYu Kanyuxia
 * @since 1.0.0
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ConfigurationProperties(prefix = GlobalConfigConst.DATABASE_DYNAMIC)
public class DynamicDataSourceProperties {

    private Boolean enabled = false;
    /**
     * 必须设置默认的库,默认master
     */
    private String primary = "master";
    /**
     * 是否启用严格模式,默认不启动. 严格模式下未匹配到数据源直接报错, 非严格模式下则使用默认数据源primary所设置的数据源
     */
    private Boolean strict = false;
    /**
     * 是否使用p6spy输出，默认不输出
     */
    private Boolean p6spy = false;
    /**
     * 是否使用开启seata，默认不开启
     */
    private Boolean seata = false;
    /**
     * 是否懒加载数据源
     */
    private Boolean lazy = false;
    /**
     * 是否优雅关闭数据源,等待一段时间后再将数据源销毁
     */
    private Boolean graceDestroy = false;
    /**
     * seata使用模式，默认AT
     */
    private SeataMode seataMode = SeataMode.AT;
    /**
     * 全局默认publicKey
     */
    private String publicKey = CryptoUtils.DEFAULT_PUBLIC_KEY_STRING;
    /**
     * 多数据配置数据源
     */
    private Map<String, DataSourceProperty> dataSources = new LinkedHashMap<>();
    /**
     * 多数据源选择算法clazz，默认负载均衡算法
     */
    private Class<? extends DynamicDataSourceStrategy> strategy = LoadBalanceDynamicDataSourceStrategy.class;
    /**
     * Druid全局参数配置
     */
    @NestedConfigurationProperty
    private DruidConfig druid = new DruidConfig();
    /**
     * HikariCp全局参数配置
     */
    @NestedConfigurationProperty
    private HikariCpConfig hikari = new HikariCpConfig();
    /**
     * BeeCp全局参数配置
     */
    @NestedConfigurationProperty
    private BeeCpConfig beecp = new BeeCpConfig();
    /**
     * DBCP2全局参数配置
     */
    @NestedConfigurationProperty
    private Dbcp2Config dbcp2 = new Dbcp2Config();
    /**
     * atomikos全局参数配置
     */
    @NestedConfigurationProperty
    private AtomikosConfig atomikos = new AtomikosConfig();

    /**
     * aop with default ds annotation
     */
    @NestedConfigurationProperty
    private DynamicDatasourceAopProperties aop = new DynamicDatasourceAopProperties();
}