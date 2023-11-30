package com.zoe.framework.components.starter.database.configuration;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zoe.framework.components.starter.id.service.IdService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author 蒋时华
 * @date 2020-05-12 10:20:23
 */
@Configuration
@ConditionalOnClass(name = "com.zoe.framework.components.starter.id.service.IdService")
@AutoConfigureBefore(MybatisPlusAutoConfiguration.class)
public class MybatisPlusIdAutoConfigure extends MybatisPlusCommonAutoConfigure {

    /**
     * 自定义 sqlSessionFactory
     * 整合 starter-id
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory1(DataSource dataSource
            , IdService idService
            , MybatisPlusInterceptor mybatisPlusInterceptor
    ) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        IdentifierGenerator identifierGenerator = new IdentifierGenerator() {
            @Override
            public Number nextId(Object entity) {
                return idService.getId();
            }

            @Override
            public String nextUUID(Object entity) {
                return idService.getUuidWithoutDash();
            }
        };
        this.initSqlSessionFactory(sqlSessionFactoryBean, identifierGenerator, mybatisPlusInterceptor);
        return sqlSessionFactoryBean.getObject();
    }

}
