package com.skrstop.framework.components.starter.web.configuration.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.skrstop.framework.components.starter.web.configuration.sentinel.webflux.DefaultWebFluxBlockRequestHandler;
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
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnClass({SentinelResource.class, BlockRequestHandler.class})
@ConditionalOnMissingBean(BlockRequestHandler.class)
public class SentinelDefaultResponseReactiveAutoConfiguration {

    @Bean
    public DefaultWebFluxBlockRequestHandler defaultWebFluxBlockRequestHandler() {
        return new DefaultWebFluxBlockRequestHandler();
    }

}
