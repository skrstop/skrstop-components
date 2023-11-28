package com.jphoebe.framework.components.starter.feign.protostuff.autoconfigure.webmvc;

import com.jphoebe.framework.components.starter.feign.protostuff.converter.ProtostuffHttpMessageConverter;
import com.jphoebe.framework.components.starter.feign.protostuff.interceptor.FeignHttpFilter;
import com.jphoebe.framework.components.starter.feign.protostuff.interceptor.FeignMarkInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * @author 蒋时华
 * @desc feign消息转换配置
 * @date 2019/12/2
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class FeignMessageConverterAutoConfiguration {

    @Bean
    public ProtostuffHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtostuffHttpMessageConverter();
    }

    @Bean
    public Encoder springEncoder(final ObjectFactory<HttpMessageConverters> messageConverterObjectFactory) {
        return new SpringEncoder(messageConverterObjectFactory);
    }

    @Bean
    public Decoder springDecoder(final ObjectFactory<HttpMessageConverters> messageConverterObjectFactory) {
        return new ResponseEntityDecoder(new SpringDecoder(messageConverterObjectFactory));
    }

    @Bean
    @ConditionalOnClass({Filter.class, FilterRegistrationBean.class})
    public FilterRegistrationBean<Filter> registerFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new FeignHttpFilter());
        registration.addUrlPatterns("/*");
        registration.setName(FeignHttpFilter.class.getSimpleName());
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FeignMarkInterceptor feignMarkInterceptor() {
        return new FeignMarkInterceptor();
    }

}
