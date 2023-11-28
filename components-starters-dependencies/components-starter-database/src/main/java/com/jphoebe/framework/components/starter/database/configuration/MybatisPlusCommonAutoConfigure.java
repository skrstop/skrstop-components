package com.jphoebe.framework.components.starter.database.configuration;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.jphoebe.framework.components.starter.database.config.GlobalDataConfig;
import com.jphoebe.framework.components.starter.database.constant.DatabaseConst;
import com.jphoebe.framework.components.starter.database.constant.GlobalConfigConst;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author 蒋时华
 * @date 2020-05-12 10:20:23
 */
@Configuration
@EnableTransactionManagement
@AutoConfigureBefore(MybatisPlusAutoConfiguration.class)
@EnableConfigurationProperties(GlobalDataConfig.class)
public class MybatisPlusCommonAutoConfigure {

    /**
     * 初始化 {@link MybatisSqlSessionFactoryBean}
     *
     * @param sqlSessionFactoryBean
     * @param identifierGenerator
     * @throws Exception
     */
    protected void initSqlSessionFactory(MybatisSqlSessionFactoryBean sqlSessionFactoryBean
            , IdentifierGenerator identifierGenerator
            , MybatisPlusInterceptor mybatisPlusInterceptor
    ) throws Exception {
        // 不显示mp banner
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setBanner(false);
        // id生成配置
        globalConfig.setIdentifierGenerator(identifierGenerator);
        sqlSessionFactoryBean.setGlobalConfig(globalConfig);
        // 扫描mapper.xml文件
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(GlobalConfigConst.MAPPER_LOCATIONS);
        sqlSessionFactoryBean.setMapperLocations(resources);
        sqlSessionFactoryBean.setPlugins(mybatisPlusInterceptor);
        // 下划线转驼峰
        sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.getObject().getConfiguration().setLazyLoadingEnabled(true);
    }

    /**
     * 插件初始化
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor(GlobalDataConfig globalDataConfig) {
        MybatisPlusInterceptor mybatisPlusInterceptor = this.commonInnerInterceptor();
        if (globalDataConfig.getSqlHealthyCheck()) {
            // sql性能插件
            IllegalSQLInnerInterceptor illegalSQLInnerInterceptor = new IllegalSQLInnerInterceptor();
            mybatisPlusInterceptor.addInnerInterceptor(illegalSQLInnerInterceptor);
        }
        return mybatisPlusInterceptor;
    }

    /**
     * 通用插件
     *
     * @return
     */
    private MybatisPlusInterceptor commonInnerInterceptor() {
        // mp插件
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        // 乐观锁插件
        OptimisticLockerInnerInterceptor optimisticLockerInterceptor = new OptimisticLockerInnerInterceptor();
        interceptor.addInnerInterceptor(optimisticLockerInterceptor);
        // 进制全表更新、删除插件
        BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
        interceptor.addInnerInterceptor(blockAttackInnerInterceptor);
        return interceptor;
    }

//    @Bean
//    public ConfigurationCustomizer configurationCustomizer() {
//        return configuration -> configuration.setUseDeprecatedExecutor(false);
//    }

    @Bean(DatabaseConst.TRANSACTION_NAME)
//    @ConditionalOnSingleCandidate(DataSource.class)
    @ConditionalOnMissingBean(DataSourceTransactionManager.class)
    DataSourceTransactionManager transactionManager(DataSource dataSource,
                                                    ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(transactionManager));
        return transactionManager;
    }

}
