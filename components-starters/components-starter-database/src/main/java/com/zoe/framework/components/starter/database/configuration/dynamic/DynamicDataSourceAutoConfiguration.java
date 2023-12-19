package com.zoe.framework.components.starter.database.configuration.dynamic;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.zoe.framework.components.starter.database.configuration.dynamic.common.DynamicDataSourceAssistConfiguration;
import com.zoe.framework.components.starter.database.configuration.dynamic.common.DynamicDataSourceCreatorAutoConfiguration;
import com.zoe.framework.components.starter.database.configuration.dynamic.common.DynamicDataSourceProperties;
import com.zoe.framework.components.starter.database.configuration.dynamic.common.DynamicDataSourcePropertiesCustomizer;
import com.zoe.framework.components.starter.database.constant.GlobalConfigConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.List;

/**
 * 动态数据源核心自动配置类
 *
 * @author TaoYu Kanyuxia
 * @since 1.0.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@AutoConfigureBefore(
        value = DataSourceAutoConfiguration.class,
        name = {
                "com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure",
                "com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure"
        })
@Import({DruidDynamicDataSourceConfiguration.class, DynamicDataSourceCreatorAutoConfiguration.class, DynamicDataSourceAopConfiguration.class, DynamicDataSourceAssistConfiguration.class})
@ConditionalOnProperty(prefix = GlobalConfigConst.DATABASE_DYNAMIC, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DynamicDataSourceAutoConfiguration implements InitializingBean {

    private final DynamicDataSourceProperties properties;

    private final List<DynamicDataSourcePropertiesCustomizer> dataSourcePropertiesCustomizers;

    public DynamicDataSourceAutoConfiguration(
            DynamicDataSourceProperties properties,
            ObjectProvider<List<DynamicDataSourcePropertiesCustomizer>> dataSourcePropertiesCustomizers) {
        this.properties = properties;
        this.dataSourcePropertiesCustomizers = dataSourcePropertiesCustomizers.getIfAvailable();
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource(List<DynamicDataSourceProvider> providers) {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource(providers);
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrict(properties.getStrict());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setSeata(properties.getSeata());
        dataSource.setGraceDestroy(properties.getGraceDestroy());
        return dataSource;
    }

    @Override
    public void afterPropertiesSet() {
        if (!CollectionUtils.isEmpty(dataSourcePropertiesCustomizers)) {
            for (DynamicDataSourcePropertiesCustomizer customizer : dataSourcePropertiesCustomizers) {
                customizer.customize(properties);
            }
        }
    }

}