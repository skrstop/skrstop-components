package com.skrstop.framework.components.starter.feign.protostuff.configuration;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 蒋时华
 * @date 2020-05-13 19:00:42
 */
@Configuration
@ConditionalOnClass({Undertow.class, UndertowOptions.class})
@EnableConfigurationProperties(GlobalFeignProperties.class)
public class Http2UndertowAutoConfiguration {


    /**
     * 开启http2 , 关闭ssl
     * eg。
     * server:
     * port: 8080
     * http2:
     * enabled: true
     * ssl:
     * enabled: false
     *
     * @return
     */
    @Bean
    public UndertowServletWebServerFactory undertowServletWebServerFactory(GlobalFeignProperties globalFeignProperties) {
        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
        if (globalFeignProperties.isEnableHttp2()) {
            factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true));
        }
        return factory;
    }

}
