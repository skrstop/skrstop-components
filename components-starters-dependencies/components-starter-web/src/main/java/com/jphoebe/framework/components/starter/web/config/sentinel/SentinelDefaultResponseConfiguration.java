package com.jphoebe.framework.components.starter.web.config.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.jphoebe.framework.components.starter.web.config.sentinel.webflux.DefaultWebFluxBlockRequestHandler;
import com.jphoebe.framework.components.starter.web.config.sentinel.webmvc.DefaultWebMvcBlockExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 蒋时华
 * @date 2020-07-14 17:12:26
 */
@Configuration
@ConditionalOnClass(SentinelResource.class)
public class SentinelDefaultResponseConfiguration {

    @Bean
    @ConditionalOnClass(BlockExceptionHandler.class)
    @ConditionalOnMissingBean(BlockExceptionHandler.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public DefaultWebMvcBlockExceptionHandler defaultWebMvcBlockExceptionHandler() {
        return new DefaultWebMvcBlockExceptionHandler();
    }

    @Bean
    @ConditionalOnClass(BlockRequestHandler.class)
    @ConditionalOnMissingBean(BlockRequestHandler.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public DefaultWebFluxBlockRequestHandler defaultWebFluxBlockRequestHandler() {
        return new DefaultWebFluxBlockRequestHandler();
    }

}
