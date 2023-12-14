package com.zoe.framework.components.starter.database.configuration;

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
@ConditionalOnMissingClass("com.zoe.framework.components.starter.id.service.IdService")
@AutoConfigureBefore(MybatisPlusAutoConfiguration.class)
@EnableConfigurationProperties(GlobalDataBaseProperties.class)
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
    public SqlSessionFactory sqlSessionFactory2(
            DataSource dataSource
            , MybatisPlusInterceptor mybatisPlusInterceptor
            , GlobalDataBaseProperties globalDataBaseProperties
    ) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        DefaultIdentifierGenerator defaultIdentifierGenerator = new DefaultIdentifierGenerator(1, 1);
        this.initSqlSessionFactory(sqlSessionFactoryBean, defaultIdentifierGenerator, mybatisPlusInterceptor, globalDataBaseProperties);
        return sqlSessionFactoryBean.getObject();
    }

}
