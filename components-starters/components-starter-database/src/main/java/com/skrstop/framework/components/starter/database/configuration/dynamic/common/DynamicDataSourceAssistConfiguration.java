package com.skrstop.framework.components.starter.database.configuration.dynamic.common;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.event.DataSourceInitEvent;
import com.baomidou.dynamic.datasource.event.EncDataSourceInitEvent;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.YmlDynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.strategy.DynamicDataSourceStrategy;
import com.baomidou.dynamic.datasource.tx.DsTxEventListenerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * 动态数据源核心自动配置类
 *
 * @author TaoYu Kanyuxia
 * @see DynamicDataSourceProvider
 * @see DynamicDataSourceStrategy
 * @see DynamicRoutingDataSource
 * @since 1.0.0
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class DynamicDataSourceAssistConfiguration {

    private final DynamicDataSourceProperties properties;

    @Bean
    @Order(0)
    public DynamicDataSourceProvider ymlDynamicDataSourceProvider(DefaultDataSourceCreator defaultDataSourceCreator) {
        return new YmlDynamicDataSourceProvider(defaultDataSourceCreator, properties.getDataSources());
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSourceInitEvent dataSourceInitEvent() {
        return new EncDataSourceInitEvent();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultDataSourceCreator dataSourceCreator(List<DataSourceCreator> dataSourceCreators, DataSourceInitEvent dataSourceInitEvent) {
        DefaultDataSourceCreator creator = new DefaultDataSourceCreator();
        creator.setCreators(dataSourceCreators);
        creator.setDataSourceInitEvent(dataSourceInitEvent);
        creator.setPublicKey(properties.getPublicKey());
        creator.setLazy(properties.getLazy());
        creator.setP6spy(properties.getP6spy());
        creator.setSeata(properties.getSeata());
        creator.setSeataMode(properties.getSeataMode());
        return creator;
    }

    @Configuration
    static class DsTxEventListenerFactoryConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public DsTxEventListenerFactory dsTxEventListenerFactory() {
            return new DsTxEventListenerFactory();
        }
    }
}