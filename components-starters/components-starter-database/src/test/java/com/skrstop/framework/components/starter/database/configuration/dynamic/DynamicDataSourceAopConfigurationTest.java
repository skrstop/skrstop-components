package com.skrstop.framework.components.starter.database.configuration.dynamic;

import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationAdvisor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.processor.DsSpelExpressionProcessor;
import com.skrstop.framework.components.starter.database.configuration.dynamic.common.DynamicDataSourceProperties;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.BeanFactory;

public class DynamicDataSourceAopConfigurationTest {

    @Test
    public void dsProcessor() {
        DynamicDataSourceAopConfiguration configuration =
                new DynamicDataSourceAopConfiguration(new DynamicDataSourceProperties());
        BeanFactory beanFactory = Mockito.mock(BeanFactory.class);
        DsProcessor processor = configuration.dsProcessor(beanFactory);
        Assert.assertTrue(processor instanceof DsSpelExpressionProcessor);
    }

    @Test
    public void dynamicDatasourceAnnotationAdvisor() {
        DynamicDataSourceProperties properties = new DynamicDataSourceProperties();
        properties.getAop().setOrder(9).setAllowedPublicOnly(false);
        DynamicDataSourceAopConfiguration configuration = new DynamicDataSourceAopConfiguration(properties);
        Advisor advisor = configuration.dynamicDatasourceAnnotationAdvisor(Mockito.mock(DsProcessor.class));
        Assert.assertTrue(advisor instanceof DynamicDataSourceAnnotationAdvisor);
        Assert.assertEquals(9, ((org.springframework.core.Ordered) advisor).getOrder());
    }

    @Test
    public void dynamicTransactionAdvisor() {
        DynamicDataSourceProperties properties = new DynamicDataSourceProperties();
        DynamicDataSourceAopConfiguration configuration = new DynamicDataSourceAopConfiguration(properties);
        Advisor advisor = configuration.dynamicTransactionAdvisor();
        Assert.assertTrue(advisor instanceof DynamicDataSourceAnnotationAdvisor);
        Assert.assertNotNull(advisor);
    }
}
