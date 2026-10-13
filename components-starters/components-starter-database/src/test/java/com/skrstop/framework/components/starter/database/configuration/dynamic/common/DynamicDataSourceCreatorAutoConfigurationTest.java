package com.skrstop.framework.components.starter.database.configuration.dynamic.common;

import com.baomidou.dynamic.datasource.creator.atomikos.AtomikosDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.basic.BasicDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.beecp.BeeCpDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.dbcp.Dbcp2DataSourceCreator;
import com.baomidou.dynamic.datasource.creator.druid.DruidDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.hikaricp.HikariDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.jndi.JndiDataSourceCreator;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

public class DynamicDataSourceCreatorAutoConfigurationTest {

    @Test
    public void basicAndJndiCreators() {
        DynamicDataSourceCreatorAutoConfiguration configuration = new DynamicDataSourceCreatorAutoConfiguration();
        Assert.assertTrue(configuration.basicDataSourceCreator() instanceof BasicDataSourceCreator);
        Assert.assertTrue(configuration.jndiDataSourceCreator() instanceof JndiDataSourceCreator);
        Assert.assertEquals(7000, DynamicDataSourceCreatorAutoConfiguration.DEFAULT_ORDER);
        Assert.assertEquals(1000, DynamicDataSourceCreatorAutoConfiguration.JNDI_ORDER);
        Assert.assertEquals(2000, DynamicDataSourceCreatorAutoConfiguration.DRUID_ORDER);
        Assert.assertEquals(3000, DynamicDataSourceCreatorAutoConfiguration.HIKARI_ORDER);
        Assert.assertEquals(4000, DynamicDataSourceCreatorAutoConfiguration.BEECP_ORDER);
        Assert.assertEquals(5000, DynamicDataSourceCreatorAutoConfiguration.DBCP2_ORDER);
        Assert.assertEquals(6000, DynamicDataSourceCreatorAutoConfiguration.ATOMIKOS_ORDER);
    }

    @Test
    public void druidCreatorWithoutApplicationContext() {
        DynamicDataSourceCreatorAutoConfiguration.DruidDataSourceCreatorConfiguration nested =
                new DynamicDataSourceCreatorAutoConfiguration.DruidDataSourceCreatorConfiguration();
        // applicationContext 字段未注入 -> null
        DruidDataSourceCreator creator = nested.druidDataSourceCreator(new DynamicDataSourceProperties());
        Assert.assertNotNull(creator);
    }

    @Test
    public void druidCreatorWithApplicationContext() throws Exception {
        DynamicDataSourceCreatorAutoConfiguration.DruidDataSourceCreatorConfiguration nested =
                new DynamicDataSourceCreatorAutoConfiguration.DruidDataSourceCreatorConfiguration();
        ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);
        Mockito.when(applicationContext.getBean(Mockito.anyString(), Mockito.eq(com.alibaba.druid.filter.Filter.class)))
                .thenReturn(Mockito.mock(com.alibaba.druid.filter.Filter.class));
        ReflectionTestUtils.setField(nested, "applicationContext", applicationContext);
        DruidDataSourceCreator creator = nested.druidDataSourceCreator(new DynamicDataSourceProperties());
        Assert.assertNotNull(creator);
        // 触发 proxyFilters lambda (applicationContext 非 null 且 filter 名非空)
        // 触发 proxyFilters lambda：applicationContext 非 null 且 filter 名非空，
        // 其中 "bad" 的 getBean 抛异常走 catch 忽略分支
        com.baomidou.dynamic.datasource.creator.DataSourceProperty property =
                new com.baomidou.dynamic.datasource.creator.DataSourceProperty();
        property.setType(com.alibaba.druid.pool.DruidDataSource.class);
        property.setUrl("jdbc:h2:mem:test");
        property.setUsername("sa");
        property.setPassword("");
        property.setPoolName("druidTest");
        Mockito.when(applicationContext.getBean(Mockito.eq("bad"), Mockito.eq(com.alibaba.druid.filter.Filter.class)))
                .thenThrow(new RuntimeException("no bean"));
        property.getDruid().setProxyFilters("f1,bad");
        Assert.assertNotNull(creator.createDataSource(property));
        // applicationContext 非 null 但 filter 名为空 -> hasText false 分支
        com.baomidou.dynamic.datasource.creator.DataSourceProperty property2 =
                new com.baomidou.dynamic.datasource.creator.DataSourceProperty();
        property2.setType(com.alibaba.druid.pool.DruidDataSource.class);
        property2.setUrl("jdbc:h2:mem:test");
        property2.setPoolName("druidTest2");
        property2.getDruid().setProxyFilters("");
        Assert.assertNotNull(creator.createDataSource(property2));
    }

    @Test
    public void druidCreatorLambdaWithoutApplicationContext() throws Exception {
        // applicationContext 为 null 且 filter 名非空 -> 短路返回空集合
        DynamicDataSourceCreatorAutoConfiguration.DruidDataSourceCreatorConfiguration nested =
                new DynamicDataSourceCreatorAutoConfiguration.DruidDataSourceCreatorConfiguration();
        DruidDataSourceCreator creator = nested.druidDataSourceCreator(new DynamicDataSourceProperties());
        com.baomidou.dynamic.datasource.creator.DataSourceProperty property =
                new com.baomidou.dynamic.datasource.creator.DataSourceProperty();
        property.setType(com.alibaba.druid.pool.DruidDataSource.class);
        property.setUrl("jdbc:h2:mem:test");
        property.setPoolName("druidTest3");
        property.getDruid().setProxyFilters("f1");
        Assert.assertNotNull(creator.createDataSource(property));
    }

    @Test
    public void hikariCreator() {
        DynamicDataSourceCreatorAutoConfiguration.HikariDataSourceCreatorConfiguration nested =
                new DynamicDataSourceCreatorAutoConfiguration.HikariDataSourceCreatorConfiguration();
        Assert.assertTrue(nested.hikariDataSourceCreator(new DynamicDataSourceProperties()) instanceof HikariDataSourceCreator);
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void beeCpCreator() {
        DynamicDataSourceCreatorAutoConfiguration.BeeCpDataSourceCreatorConfiguration nested =
                new DynamicDataSourceCreatorAutoConfiguration.BeeCpDataSourceCreatorConfiguration();
        BeeCpDataSourceCreator creator = nested.beeCpDataSourceCreator(new DynamicDataSourceProperties());
        Assert.assertNotNull(creator);
        // support: type 为 null -> true
        com.baomidou.dynamic.datasource.creator.DataSourceProperty property =
                new com.baomidou.dynamic.datasource.creator.DataSourceProperty();
        Assert.assertTrue(creator.support(property));
        property.setType(cn.beecp.BeeDataSource.class);
        Assert.assertTrue(creator.support(property));
        property.setType((Class) (Class<?>) javax.sql.DataSource.class);
        Assert.assertFalse(creator.support(property));
        // createDataSource: lazy=true -> new BeeDataSource(config)
        property.setType(cn.beecp.BeeDataSource.class);
        property.setLazy(true);
        Assert.assertNotNull(creator.createDataSource(property));
        // lazy=false -> copyTo 分支
        property.setLazy(false);
        Assert.assertNotNull(creator.createDataSource(property));
        // 异常分支(仅 lazy=false 的 config 构造分支有异常包装): 构造失败 -> ErrorCreateDataSourceException
        com.baomidou.dynamic.datasource.creator.DataSourceProperty bad =
                new com.baomidou.dynamic.datasource.creator.DataSourceProperty();
        bad.setPoolName("bad");
        bad.setLazy(false);
        cn.beecp.BeeDataSource.failNextConstruction = true;
        try {
            creator.createDataSource(bad);
            Assert.fail("should throw");
        } catch (com.baomidou.dynamic.datasource.exception.ErrorCreateDataSourceException e) {
            Assert.assertTrue(e.getMessage().contains("bad"));
        } finally {
            cn.beecp.BeeDataSource.failNextConstruction = false;
        }
    }

    @Test
    public void dbcp2Creator() {
        DynamicDataSourceCreatorAutoConfiguration.Dbcp2DataSourceCreatorConfiguration nested =
                new DynamicDataSourceCreatorAutoConfiguration.Dbcp2DataSourceCreatorConfiguration();
        Assert.assertTrue(nested.dbcp2DataSourceCreator(new DynamicDataSourceProperties()) instanceof Dbcp2DataSourceCreator);
    }

    @Test
    public void atomikosCreatorAndTxFactory() {
        DynamicDataSourceCreatorAutoConfiguration.AtomikosDataSourceCreatorConfiguration nested =
                new DynamicDataSourceCreatorAutoConfiguration.AtomikosDataSourceCreatorConfiguration();
        Assert.assertTrue(nested.atomikosDataSourceCreator(new DynamicDataSourceProperties()) instanceof AtomikosDataSourceCreator);
        Assert.assertNotNull(nested.atomikosTransactionFactory());
    }
}
