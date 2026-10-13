package com.skrstop.framework.components.starter.database.configuration;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.*;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.github.yulichang.autoconfigure.MybatisPlusJoinProperties;
import com.skrstop.framework.components.starter.id.service.IdService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;

import javax.sql.DataSource;
import java.util.List;

public class MybatisPlusAutoConfigurationTest {

    private GlobalDatabaseProperties props() {
        return new GlobalDatabaseProperties();
    }

    @Test
    public void mybatisPlusInterceptorWithoutHealthyCheck() {
        MybatisPlusCommonAutoConfiguration configuration = new MybatisPlusCommonAutoConfiguration(new MybatisPlusJoinProperties());
        MybatisPlusInterceptor interceptor = configuration.mybatisPlusInterceptor(props());
        List<InnerInterceptor> inners = interceptor.getInterceptors();
        Assert.assertEquals(3, inners.size());
        Assert.assertTrue(inners.get(0) instanceof PaginationInnerInterceptor);
        Assert.assertTrue(inners.get(1) instanceof OptimisticLockerInnerInterceptor);
        Assert.assertTrue(inners.get(2) instanceof BlockAttackInnerInterceptor);
    }

    @Test
    public void mybatisPlusInterceptorWithHealthyCheck() {
        MybatisPlusCommonAutoConfiguration configuration = new MybatisPlusCommonAutoConfiguration(new MybatisPlusJoinProperties());
        GlobalDatabaseProperties p = props();
        p.setSqlHealthyCheck(true);
        MybatisPlusInterceptor interceptor = configuration.mybatisPlusInterceptor(p);
        List<InnerInterceptor> inners = interceptor.getInterceptors();
        Assert.assertEquals(4, inners.size());
        Assert.assertTrue(inners.get(3) instanceof IllegalSQLInnerInterceptor);
    }

    @Test
    public void databaseTransactionManagerWithCustomizers() {
        MybatisPlusCommonAutoConfiguration configuration = new MybatisPlusCommonAutoConfiguration(new MybatisPlusJoinProperties());
        DataSource dataSource = Mockito.mock(DataSource.class);
        ObjectProvider<TransactionManagerCustomizers> provider = Mockito.mock(ObjectProvider.class);
        TransactionManagerCustomizers customizers = Mockito.mock(TransactionManagerCustomizers.class);
        Mockito.doAnswer(inv -> {
            java.util.function.Consumer<TransactionManagerCustomizers> consumer = inv.getArgument(0);
            consumer.accept(customizers);
            return null;
        }).when(provider).ifAvailable(Mockito.any());
        org.springframework.jdbc.datasource.DataSourceTransactionManager tm =
                configuration.databaseTransactionManager(dataSource, provider);
        Assert.assertNotNull(tm);
        Assert.assertSame(dataSource, tm.getDataSource());
        Mockito.verify(customizers).customize(tm);
    }

    @Test
    public void databaseTransactionManagerWithoutCustomizers() {
        MybatisPlusCommonAutoConfiguration configuration = new MybatisPlusCommonAutoConfiguration(new MybatisPlusJoinProperties());
        DataSource dataSource = Mockito.mock(DataSource.class);
        ObjectProvider<TransactionManagerCustomizers> provider = Mockito.mock(ObjectProvider.class);
        Mockito.doNothing().when(provider).ifAvailable(Mockito.any());
        org.springframework.jdbc.datasource.DataSourceTransactionManager tm =
                configuration.databaseTransactionManager(dataSource, provider);
        Assert.assertNotNull(tm);
    }

    @Test
    public void noIdCustomSqlSessionFactory() throws Exception {
        MybatisPlusNoIdAutoConfiguration configuration = new MybatisPlusNoIdAutoConfiguration(new MybatisPlusJoinProperties());
        DataSource dataSource = Mockito.mock(DataSource.class);
        SqlSessionFactory factory = configuration.customSqlSessionFactory(dataSource,
                new MybatisPlusAutoConfigurationTest().interceptor(), props(), new MybatisPlusProperties());
        Assert.assertNotNull(factory);
        Assert.assertTrue(factory.getConfiguration().isMapUnderscoreToCamelCase());
        Assert.assertTrue(factory.getConfiguration().isLazyLoadingEnabled());
        Assert.assertFalse(factory.getConfiguration().isCallSettersOnNulls());
    }

    @Test
    public void noIdCustomSqlSessionFactoryWithCallSettersOnNulls() throws Exception {
        MybatisPlusNoIdAutoConfiguration configuration = new MybatisPlusNoIdAutoConfiguration(new MybatisPlusJoinProperties());
        DataSource dataSource = Mockito.mock(DataSource.class);
        MybatisPlusProperties mpProperties = new MybatisPlusProperties();
        MybatisPlusProperties.CoreConfiguration coreConfiguration = new MybatisPlusProperties.CoreConfiguration();
        coreConfiguration.setCallSettersOnNulls(true);
        mpProperties.setConfiguration(coreConfiguration);
        SqlSessionFactory factory = configuration.customSqlSessionFactory(dataSource,
                new MybatisPlusAutoConfigurationTest().interceptor(), props(), mpProperties);
        Assert.assertNotNull(factory);
        Assert.assertTrue(factory.getConfiguration().isCallSettersOnNulls());
    }

    @Test
    public void idCustomSqlSessionFactory() throws Exception {
        MybatisPlusIdAutoConfiguration configuration = new MybatisPlusIdAutoConfiguration(new MybatisPlusJoinProperties());
        DataSource dataSource = Mockito.mock(DataSource.class);
        IdService idService = Mockito.mock(IdService.class);
        Mockito.when(idService.getId()).thenReturn(123L);
        Mockito.when(idService.getUuidWithoutDash()).thenReturn("uuid");
        SqlSessionFactory factory = configuration.customSqlSessionFactory(dataSource, idService,
                new MybatisPlusAutoConfigurationTest().interceptor(), props(), new MybatisPlusProperties());
        Assert.assertNotNull(factory);
        IdentifierGenerator generator = com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils.getGlobalConfig(factory.getConfiguration()).getIdentifierGenerator();
        Assert.assertNotNull(generator);
        Assert.assertEquals(123L, generator.nextId(null).longValue());
        Assert.assertEquals("uuid", generator.nextUUID(null));
    }

    @Test
    public void initSqlSessionFactoryWithUnderscoreDisabled() throws Exception {
        MybatisPlusCommonAutoConfiguration configuration = new MybatisPlusCommonAutoConfiguration(new MybatisPlusJoinProperties());
        GlobalDatabaseProperties p = props();
        p.setMapUnderscoreToCamelCase(false);
        p.setLazyLoadingEnabled(false);
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(Mockito.mock(DataSource.class));
        // 通过 Id 子类调用 protected initSqlSessionFactory
        MybatisPlusIdAutoConfiguration idConfiguration = new MybatisPlusIdAutoConfiguration(new MybatisPlusJoinProperties());
        idConfiguration.initSqlSessionFactory(bean, (IdentifierGenerator) null,
                new MybatisPlusAutoConfigurationTest().interceptor(), p, new MybatisPlusProperties());
        Assert.assertFalse(bean.getObject().getConfiguration().isMapUnderscoreToCamelCase());
        Assert.assertFalse(bean.getObject().getConfiguration().isLazyLoadingEnabled());
        Assert.assertFalse(com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils.getGlobalConfig(bean.getObject().getConfiguration()).isBanner());
    }

    @Test
    public void initSqlSessionFactoryWithoutCallSettersConfig() throws Exception {
        MybatisPlusCommonAutoConfiguration configuration = new MybatisPlusCommonAutoConfiguration(new MybatisPlusJoinProperties());
        MybatisPlusProperties properties = new MybatisPlusProperties();
        // configuration 为 null -> callSettersOnNulls 分支不生效
        properties.setConfiguration(null);
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(Mockito.mock(DataSource.class));
        MybatisPlusIdAutoConfiguration idConfiguration = new MybatisPlusIdAutoConfiguration(new MybatisPlusJoinProperties());
        idConfiguration.initSqlSessionFactory(bean, (IdentifierGenerator) null,
                new MybatisPlusAutoConfigurationTest().interceptor(), props(), properties);
        // 默认值为 false，未被覆盖
        Assert.assertFalse(bean.getObject().getConfiguration().isCallSettersOnNulls());
    }

    @Test
    public void initSqlSessionFactoryWithNullCallSetters() throws Exception {
        MybatisPlusCommonAutoConfiguration configuration = new MybatisPlusCommonAutoConfiguration(new MybatisPlusJoinProperties());
        MybatisPlusProperties properties = new MybatisPlusProperties();
        // configuration 非 null 但 callSettersOnNulls 为 null -> 不覆盖
        properties.setConfiguration(new MybatisPlusProperties.CoreConfiguration());
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(Mockito.mock(DataSource.class));
        MybatisPlusIdAutoConfiguration idConfiguration = new MybatisPlusIdAutoConfiguration(new MybatisPlusJoinProperties());
        idConfiguration.initSqlSessionFactory(bean, (IdentifierGenerator) null,
                new MybatisPlusAutoConfigurationTest().interceptor(), props(), properties);
        Assert.assertFalse(bean.getObject().getConfiguration().isCallSettersOnNulls());
    }

    MybatisPlusInterceptor interceptor() {
        MybatisPlusCommonAutoConfiguration configuration = new MybatisPlusCommonAutoConfiguration(new MybatisPlusJoinProperties());
        return configuration.mybatisPlusInterceptor(props());
    }
}
