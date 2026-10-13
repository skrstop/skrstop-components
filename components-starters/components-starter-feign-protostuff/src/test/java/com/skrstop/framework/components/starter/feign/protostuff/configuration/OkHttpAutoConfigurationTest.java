package com.skrstop.framework.components.starter.feign.protostuff.configuration;

import feign.Client;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;

public class OkHttpAutoConfigurationTest {

    @Test
    public void feignClientAndPool() {
        OkHttpAutoConfiguration configuration = new OkHttpAutoConfiguration();
        Client client = configuration.feignClient(Mockito.mock(OkHttpClient.class));
        Assert.assertNotNull(client);
        FeignOkHttpProperties properties = new FeignOkHttpProperties();
        ConnectionPool pool = configuration.httpClientConnectionPool(properties);
        Assert.assertNotNull(pool);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters();
        HttpMessageConverters converters = configuration.getMappingJackson2HttpMessageConverter();
        Assert.assertNotNull(converters);
        Assert.assertNotNull(objectFactory);
    }

    @Test
    public void clientWithSslDisableAndHttp1() throws Exception {
        OkHttpAutoConfiguration configuration = new OkHttpAutoConfiguration();
        FeignOkHttpProperties properties = new FeignOkHttpProperties();
        // disableSslValidation=true 默认
        OkHttpClient okHttpClient = configuration.client(new ConnectionPool(), properties, new GlobalFeignProperties());
        Assert.assertNotNull(okHttpClient);
    }

    @Test
    public void clientWithoutSslDisableAndHttp2() throws Exception {
        OkHttpAutoConfiguration configuration = new OkHttpAutoConfiguration();
        FeignOkHttpProperties properties = new FeignOkHttpProperties();
        properties.setDisableSslValidation(false);
        GlobalFeignProperties global = new GlobalFeignProperties();
        global.setEnableHttp2(true);
        OkHttpClient okHttpClient = configuration.client(new ConnectionPool(), properties, global);
        Assert.assertNotNull(okHttpClient);
    }
}
