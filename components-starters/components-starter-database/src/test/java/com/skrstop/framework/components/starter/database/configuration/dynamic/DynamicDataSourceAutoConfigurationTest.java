package com.skrstop.framework.components.starter.database.configuration.dynamic;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.skrstop.framework.components.starter.database.configuration.dynamic.common.DynamicDataSourceProperties;
import com.skrstop.framework.components.starter.database.configuration.dynamic.common.DynamicDataSourcePropertiesCustomizer;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectProvider;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DynamicDataSourceAutoConfigurationTest {

    @SuppressWarnings("unchecked")
    private ObjectProvider<List<DynamicDataSourcePropertiesCustomizer>> provider(List<DynamicDataSourcePropertiesCustomizer> list) {
        ObjectProvider<List<DynamicDataSourcePropertiesCustomizer>> provider = Mockito.mock(ObjectProvider.class);
        Mockito.when(provider.getIfAvailable()).thenReturn(list);
        return provider;
    }

    @Test
    public void afterPropertiesSetWithCustomizers() {
        DynamicDataSourceProperties properties = new DynamicDataSourceProperties();
        DynamicDataSourcePropertiesCustomizer customizer = Mockito.mock(DynamicDataSourcePropertiesCustomizer.class);
        DynamicDataSourceAutoConfiguration configuration =
                new DynamicDataSourceAutoConfiguration(properties, provider(Arrays.asList(customizer, customizer)));
        configuration.afterPropertiesSet();
        Mockito.verify(customizer, Mockito.times(2)).customize(properties);
    }

    @Test
    public void afterPropertiesSetWithoutCustomizers() {
        DynamicDataSourceProperties properties = new DynamicDataSourceProperties();
        DynamicDataSourceAutoConfiguration configuration =
                new DynamicDataSourceAutoConfiguration(properties, provider(null));
        configuration.afterPropertiesSet();
        // 不抛异常即可
    }

    @Test
    public void dataSourceBean() {
        DynamicDataSourceProperties properties = new DynamicDataSourceProperties();
        properties.setPrimary("master").setStrict(true).setP6spy(false).setSeata(false).setGraceDestroy(false);
        DynamicDataSourceAutoConfiguration configuration =
                new DynamicDataSourceAutoConfiguration(properties, provider(null));
        List<DynamicDataSourceProvider> providers = Collections.singletonList(Mockito.mock(DynamicDataSourceProvider.class));
        DataSource dataSource = configuration.dataSource(providers);
        Assert.assertNotNull(dataSource);
        Assert.assertTrue(dataSource instanceof DynamicRoutingDataSource);
    }

    @Test
    public void druidConfigClassInstantiable() {
        // 空配置类，仅校验可实例化（默认构造器）
        Assert.assertNotNull(new com.skrstop.framework.components.starter.database.configuration.dynamic.DruidDynamicDataSourceConfiguration());
    }
}
