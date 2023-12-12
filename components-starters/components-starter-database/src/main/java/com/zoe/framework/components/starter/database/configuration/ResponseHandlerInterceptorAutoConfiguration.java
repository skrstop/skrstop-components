package com.zoe.framework.components.starter.database.configuration;

import com.zoe.framework.components.starter.database.configuration.response.MybatisPlusPageResponseInterceptor;
import com.zoe.framework.components.starter.database.configuration.response.PageHelperPageResponseInterceptor;
import com.zoe.framework.components.starter.web.response.core.ResponseHandlerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 蒋时华
 * @date 2023-12-12 15:53:04
 */
@Configuration
@ConditionalOnClass(ResponseHandlerInterceptor.class)
@Slf4j
public class ResponseHandlerInterceptorAutoConfiguration {

    @Bean
    public MybatisPlusPageResponseInterceptor mybatisPlusPageResponseInterceptor() {
        return new MybatisPlusPageResponseInterceptor();
    }

    @Bean
    @ConditionalOnClass(name = "com.github.pagehelper.PageInfo")
    public PageHelperPageResponseInterceptor pageHelperPageResponseInterceptor() {
        return new PageHelperPageResponseInterceptor();
    }

}
