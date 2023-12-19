package com.skrstop.framework.components.starter.web.exception.global.webflux;

import com.skrstop.framework.components.starter.web.configuration.GlobalExceptionProperties;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ErrorHandleChainPattern;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandleChainPattern;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

/**
 * ExceptionConfig class
 *
 * @author 蒋时华
 * @date 2019/1/22
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnClass({ErrorWebExceptionHandler.class, ViewResolver.class, ErrorWebExceptionHandler.class})
@ConditionalOnProperty(value = "skrstop.exception.config.enable", havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(WebFluxAutoConfiguration.class)
@EnableConfigurationProperties({WebFluxProperties.class, GlobalExceptionProperties.class})
public class ExceptionAutoConfiguration {

    @Value("${server.error.path:${error.path:/error}}")
    private String errorPath;

    @Primary
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ObjectProvider<List<ViewResolver>> viewResolversProvider
            , ServerCodecConfigurer serverCodecConfigurer
            , ExceptionHandleChainPattern exceptionHandleChainPattern
            , ErrorHandleChainPattern errorHandleChainPattern
            , GlobalExceptionProperties globalExceptionProperties) {
        RequestExceptionHandler requestExceptionHandler = new RequestExceptionHandler(exceptionHandleChainPattern
                , errorHandleChainPattern
                , globalExceptionProperties
                , errorPath);
        requestExceptionHandler.setViewResolvers(viewResolversProvider.getIfAvailable(Collections::emptyList));
        requestExceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        requestExceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return requestExceptionHandler;
    }

}
