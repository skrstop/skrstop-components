package com.skrstop.framework.components.starter.feign.protostuff.configuration;

import feign.Contract;
import feign.QueryMapEncoder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.cloud.openfeign.support.SpringMvcContract;

public class FeignCustomAutoConfigurationTest {

    @Test
    public void beans() {
        FeignCustomAutoConfiguration configuration = new FeignCustomAutoConfiguration();
        Contract contract = configuration.feignContract();
        Assert.assertTrue(contract instanceof SpringMvcContract);
        QueryMapEncoder encoder = configuration.feignQueryMapEncoderPageable();
        Assert.assertNotNull(encoder);
        // 常量覆盖
        Assert.assertTrue(FeignOkHttpProperties.DEFAULT_DISABLE_SSL_VALIDATION);
        Assert.assertEquals(200, FeignOkHttpProperties.DEFAULT_MAX_CONNECTIONS);
        Assert.assertEquals(900L, FeignOkHttpProperties.DEFAULT_TIME_TO_LIVE);
        Assert.assertNotNull(FeignOkHttpProperties.DEFAULT_TIME_TO_LIVE_UNIT);
        Assert.assertTrue(FeignOkHttpProperties.DEFAULT_FOLLOW_REDIRECTS);
        Assert.assertEquals(2000, FeignOkHttpProperties.DEFAULT_CONNECTION_TIMEOUT);
        Assert.assertEquals(3000, FeignOkHttpProperties.DEFAULT_READ_TIMEOUT);
        Assert.assertEquals(3000, FeignOkHttpProperties.DEFAULT_WRITE_TIMEOUT);
    }
}
