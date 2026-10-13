package com.skrstop.framework.components.starter.web.response;

import com.skrstop.framework.components.starter.web.configuration.GlobalResponseProperties;
import com.skrstop.framework.components.starter.web.configuration.JacksonAutoConfiguration;
import com.skrstop.framework.components.starter.web.response.webflux.WebFluxResponseJacksonAutoConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.codec.Decoder;
import org.springframework.core.codec.Encoder;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.ArrayList;
import java.util.List;

public class WebFluxResponseJacksonAutoConfigurationTest {

    @Test
    public void test() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());
        JacksonAutoConfiguration jackson = new JacksonAutoConfiguration(new GlobalResponseProperties(), converters);
        WebFluxResponseJacksonAutoConfiguration config = new WebFluxResponseJacksonAutoConfiguration(jackson);

        Encoder<?> encoder = config.jackson2JsonEncoder();
        Assert.assertNotNull(encoder);
        Decoder<?> decoder = config.jackson2JsonDecoder();
        Assert.assertNotNull(decoder);

        WebFluxConfigurer configurer = config.webFluxConfigurer(
                (org.springframework.http.codec.json.Jackson2JsonEncoder) encoder,
                (org.springframework.http.codec.json.Jackson2JsonDecoder) decoder);
        Assert.assertNotNull(configurer);
        ServerCodecConfigurer sc = ServerCodecConfigurer.create();
        configurer.configureHttpMessageCodecs(sc);
        Assert.assertNotNull(sc);
    }
}
