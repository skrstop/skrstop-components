package com.skrstop.framework.components.starter.database.configuration;

import com.skrstop.framework.components.starter.database.configuration.response.MybatisPlusPageResponseInterceptor;
import com.skrstop.framework.components.starter.database.configuration.response.PageHelperPageResponseInterceptor;
import org.junit.Assert;
import org.junit.Test;

public class ResponseHandlerInterceptorAutoConfigurationTest {

    @Test
    public void beans() {
        ResponseHandlerInterceptorAutoConfiguration configuration = new ResponseHandlerInterceptorAutoConfiguration();
        Assert.assertNotNull(configuration.mybatisPlusPageResponseInterceptor());
        Assert.assertTrue(configuration.mybatisPlusPageResponseInterceptor() instanceof MybatisPlusPageResponseInterceptor);
        Assert.assertNotNull(configuration.pageHelperPageResponseInterceptor());
        Assert.assertTrue(configuration.pageHelperPageResponseInterceptor() instanceof PageHelperPageResponseInterceptor);
    }
}
