package com.zoe.framework.components.starter.database.configuration;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zoe.framework.components.starter.database.constant.DatabaseConst;
import com.zoe.framework.components.starter.database.constant.GlobalConfigConst;
import com.zoe.framework.components.util.constant.StringPoolConst;
import com.zoe.framework.components.util.value.data.ArrayUtil;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import com.zoe.framework.components.util.value.data.StrUtil;
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
import java.util.*;

/**
 * @author 蒋时华
 * @date 2020-05-12 10:20:23
 */
@Configuration
@EnableTransactionManagement
@AutoConfigureBefore(MybatisPlusAutoConfiguration.class)
@EnableConfigurationProperties(GlobalDataProperties.class)
public class MybatisPlusCommonAutoConfiguration {

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
            , GlobalDataProperties globalDataProperties
    ) throws Exception {
        // 不显示mp banner
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setBanner(false);
        // id生成配置
        globalConfig.setIdentifierGenerator(identifierGenerator);
        sqlSessionFactoryBean.setGlobalConfig(globalConfig);
        // 扫描mapper.xml文件
        Set<String> xmlLocations = new LinkedHashSet<>(StrUtil.splitTrim(globalDataProperties.getMapperXmlLocation(), StringPoolConst.COMMA));
        // 需要包含默认的mapper/*.xml
        xmlLocations.add(GlobalConfigConst.DEFAULT_MAPPER_XML_LOCATION);
        List<Resource> allResources = new ArrayList<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        for (String xmlLocation : xmlLocations) {
            Resource[] resources = resolver.getResources(xmlLocation);
            allResources.addAll(Arrays.asList(resources));
        }
        sqlSessionFactoryBean.setMapperLocations(ArrayUtil.toArray(allResources, Resource.class));
        sqlSessionFactoryBean.setPlugins(mybatisPlusInterceptor);
        if (ObjectUtil.isNull(sqlSessionFactoryBean.getObject())
                || ObjectUtil.isNull(sqlSessionFactoryBean.getObject().getConfiguration())) {
            return;
        }
        // 下划线转驼峰
        sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(globalDataProperties.getMapUnderscoreToCamelCase());
        // 懒加载
        sqlSessionFactoryBean.getObject().getConfiguration().setLazyLoadingEnabled(globalDataProperties.getLazyLoadingEnabled());
    }

    /**
     * 插件初始化
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor(GlobalDataProperties globalDataProperties) {
        MybatisPlusInterceptor mybatisPlusInterceptor = this.commonInnerInterceptor();
        if (globalDataProperties.getSqlHealthyCheck()) {
            // sql性能插件
            IllegalSQLInnerInterceptor interceptor = new IllegalSQLInnerInterceptor();
            mybatisPlusInterceptor.addInnerInterceptor(interceptor);
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

    @Bean(DatabaseConst.TRANSACTION_NAME)
    @ConditionalOnMissingBean(DataSourceTransactionManager.class)
    DataSourceTransactionManager transactionManager(DataSource dataSource,
                                                    ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(transactionManager));
        return transactionManager;
    }

}
