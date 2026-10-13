package com.skrstop.framework.components.starter.database.configuration.dynamic.common;

import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.event.DataSourceInitEvent;
import com.baomidou.dynamic.datasource.event.EncDataSourceInitEvent;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.YmlDynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.tx.DsTxEventListenerFactory;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

public class DynamicDataSourceAssistConfigurationTest {

    @Test
    public void ymlDynamicDataSourceProvider() {
        DynamicDataSourceAssistConfiguration configuration = new DynamicDataSourceAssistConfiguration(new DynamicDataSourceProperties());
        DynamicDataSourceProvider provider = configuration.ymlDynamicDataSourceProvider(new DefaultDataSourceCreator());
        Assert.assertTrue(provider instanceof YmlDynamicDataSourceProvider);
    }

    @Test
    public void dataSourceInitEvent() {
        DynamicDataSourceAssistConfiguration configuration = new DynamicDataSourceAssistConfiguration(new DynamicDataSourceProperties());
        DataSourceInitEvent event = configuration.dataSourceInitEvent();
        Assert.assertTrue(event instanceof EncDataSourceInitEvent);
    }

    @Test
    public void dataSourceCreator() {
        DynamicDataSourceProperties properties = new DynamicDataSourceProperties();
        properties.setPublicKey("pk").setLazy(true).setP6spy(true).setSeata(true).setSeataMode(com.baomidou.dynamic.datasource.enums.SeataMode.XA);
        DynamicDataSourceAssistConfiguration configuration = new DynamicDataSourceAssistConfiguration(properties);
        List<DataSourceCreator> creators = Arrays.asList(
                Mockito.mock(DataSourceCreator.class), Mockito.mock(DataSourceCreator.class));
        DataSourceInitEvent event = Mockito.mock(DataSourceInitEvent.class);
        DefaultDataSourceCreator creator = configuration.dataSourceCreator(creators, event);
        Assert.assertNotNull(creator);
    }

    @Test
    public void dsTxEventListenerFactory() {
        DynamicDataSourceAssistConfiguration.DsTxEventListenerFactoryConfiguration nested =
                new DynamicDataSourceAssistConfiguration.DsTxEventListenerFactoryConfiguration();
        Assert.assertNotNull(nested.dsTxEventListenerFactory());
        Assert.assertTrue(nested.dsTxEventListenerFactory() instanceof DsTxEventListenerFactory);
    }
}
