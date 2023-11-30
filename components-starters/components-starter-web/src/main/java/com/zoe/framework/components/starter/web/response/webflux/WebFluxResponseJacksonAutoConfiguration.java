package com.zoe.framework.components.starter.web.response.webflux;

import com.zoe.framework.components.starter.web.configuration.GlobalResponseProperties;
import com.zoe.framework.components.starter.web.configuration.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * WebMvcConfig class
 *
 * @author 蒋时华
 * @date 2018/11/22
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnClass({MappingJackson2HttpMessageConverter.class, WebFluxConfigurer.class})
@EnableConfigurationProperties({GlobalResponseProperties.class})
public class WebFluxResponseJacksonAutoConfiguration {

    private JacksonAutoConfiguration jacksonAutoConfiguration;

    public WebFluxResponseJacksonAutoConfiguration(JacksonAutoConfiguration jacksonAutoConfiguration) {
        this.jacksonAutoConfiguration = jacksonAutoConfiguration;
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnClass(WebFluxConfigurer.class)
    public Jackson2JsonEncoder jackson2JsonEncoder() {
        return new Jackson2JsonEncoder(jacksonAutoConfiguration.getGlobalObjectMapper());
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnClass(WebFluxConfigurer.class)
    public Jackson2JsonDecoder jackson2JsonDecoder() {
        return new Jackson2JsonDecoder(jacksonAutoConfiguration.getGlobalObjectMapper());
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnClass(WebFluxConfigurer.class)
    public WebFluxConfigurer webFluxConfigurer(Jackson2JsonEncoder encoder, Jackson2JsonDecoder decoder) {
        return new WebFluxConfigurer() {
            @Override
            public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
                configurer.defaultCodecs().jackson2JsonEncoder(encoder);
                configurer.defaultCodecs().jackson2JsonDecoder(decoder);
            }
        };

    }

}
