package com.zoe.framework.components.starter.web.response.webflux;

import com.zoe.framework.components.starter.web.config.GlobalResponseConfig;
import com.zoe.framework.components.starter.web.config.JacksonAutoConfig;
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
@EnableConfigurationProperties({GlobalResponseConfig.class})
public class WebFluxResponseJacksonAutoConfig {

    private JacksonAutoConfig jacksonAutoConfig;

    public WebFluxResponseJacksonAutoConfig(JacksonAutoConfig jacksonAutoConfig) {
        this.jacksonAutoConfig = jacksonAutoConfig;
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnClass(WebFluxConfigurer.class)
    public Jackson2JsonEncoder jackson2JsonEncoder() {
        return new Jackson2JsonEncoder(jacksonAutoConfig.getGlobalObjectMapper());
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnClass(WebFluxConfigurer.class)
    public Jackson2JsonDecoder jackson2JsonDecoder() {
        return new Jackson2JsonDecoder(jacksonAutoConfig.getGlobalObjectMapper());
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
