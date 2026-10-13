package com.skrstop.framework.components.starter.feign.protostuff.configuration.webmvc;

import com.skrstop.framework.components.starter.feign.protostuff.configuration.GlobalFeignProperties;
import com.skrstop.framework.components.starter.feign.protostuff.converter.ProtostuffHttpMessageConverter;
import com.skrstop.framework.components.starter.feign.protostuff.interceptor.FeignHttpFilter;
import feign.codec.Decoder;
import feign.codec.Encoder;
import jakarta.servlet.Filter;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import java.util.stream.Stream;

public class FeignMessageConverterAutoConfigurationTest {

    @Test
    @SuppressWarnings("unchecked")
    public void beans() {
        FeignMessageConverterAutoConfiguration configuration = new FeignMessageConverterAutoConfiguration();
        GlobalFeignProperties properties = new GlobalFeignProperties();
        ProtostuffHttpMessageConverter converter = configuration.protobufHttpMessageConverter(properties);
        Assert.assertNotNull(converter);

        ObjectFactory<HttpMessageConverters> factory = () -> new HttpMessageConverters();
        Encoder encoder = configuration.springEncoder(factory);
        Assert.assertNotNull(encoder);

        ObjectProvider<org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer> customizers =
                Mockito.mock(ObjectProvider.class);
        Mockito.when(customizers.orderedStream()).thenReturn(Stream.empty());
        Decoder decoder = configuration.springDecoder(factory, customizers);
        Assert.assertNotNull(decoder);

        FilterRegistrationBean<Filter> registration = configuration.registerFilter();
        Assert.assertNotNull(registration);
        Assert.assertTrue(registration.getFilter() instanceof FeignHttpFilter);
        Assert.assertEquals(1, registration.getOrder());
    }

    @Test
    public void feignMarkInterceptorBean() {
        FeignMessageConverterAutoConfiguration configuration = new FeignMessageConverterAutoConfiguration();
        Assert.assertNotNull(configuration.feignMarkInterceptor(new GlobalFeignProperties()));
    }
}
