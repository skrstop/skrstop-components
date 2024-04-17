package com.skrstop.framework.components.starter.database.configuration;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author 蒋时华
 * @date 2020-05-12 10:20:23
 */
@Configuration
@ConditionalOnMissingClass("com.skrstop.framework.components.starter.id.service.IdService")
@AutoConfigureBefore(MybatisPlusAutoConfiguration.class)
@EnableConfigurationProperties(GlobalDatabaseProperties.class)
public class MybatisPlusNoIdAutoConfiguration extends MybatisPlusCommonAutoConfiguration {

    /**
     * 自定义 sqlSessionFactory
     * 默认id生成
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory customSqlSessionFactory(
            DataSource dataSource
            , MybatisPlusInterceptor mybatisPlusInterceptor
            , GlobalDatabaseProperties globalDataBaseProperties
    ) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        DefaultIdentifierGenerator defaultIdentifierGenerator = new DefaultIdentifierGenerator(1, 1);
        this.initSqlSessionFactory(sqlSessionFactoryBean, defaultIdentifierGenerator, mybatisPlusInterceptor, globalDataBaseProperties);
        return sqlSessionFactoryBean.getObject();
    }

}
