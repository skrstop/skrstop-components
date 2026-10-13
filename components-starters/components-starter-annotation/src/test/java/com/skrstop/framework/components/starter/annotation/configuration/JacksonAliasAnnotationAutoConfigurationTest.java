package com.skrstop.framework.components.starter.annotation.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Collections;

/**
 * @author 蒋时华
 */
public class JacksonAliasAnnotationAutoConfigurationTest {

    @Configuration
    static class ConverterConfig {
        @Bean
        public MappingJackson2HttpMessageConverter jacksonConverter() {
            return new MappingJackson2HttpMessageConverter();
        }

        @Bean
        public StringHttpMessageConverter stringConverter() {
            return new StringHttpMessageConverter();
        }
    }

    @Test
    public void testConstructorInstallsIntrospector() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        new JacksonAliasAnnotationAutoConfiguration(Collections.singletonList(converter));
        // introspector 已成对安装：别名字段可通过别名反序列化
        ObjectMapper mapper = converter.getObjectMapper();
        com.fasterxml.jackson.databind.AnnotationIntrospector introspector =
                mapper.getSerializationConfig().getAnnotationIntrospector();
        Assert.assertTrue(introspector instanceof AnnotationIntrospectorPair);
    }

    @Test
    public void testNonJacksonConverterIgnored() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // 仅非 Jackson converter → 不修改
        new JacksonAliasAnnotationAutoConfiguration(Collections.singletonList(new StringHttpMessageConverter()));
        // 再验证 Jackson 的正常路径
        new JacksonAliasAnnotationAutoConfiguration(Collections.singletonList(converter));
        Assert.assertNotNull(converter.getObjectMapper().getSerializationConfig().getAnnotationIntrospector());
    }

}