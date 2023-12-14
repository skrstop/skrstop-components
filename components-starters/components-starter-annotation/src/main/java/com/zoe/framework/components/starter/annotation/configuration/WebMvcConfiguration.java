package com.zoe.framework.components.starter.annotation.configuration;

import com.zoe.framework.components.starter.annotation.handle.paramAlias.AliasModelAttributeMethodServletProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author: wangzh
 * @date: 2021/3/11 19:52
 * @description: desc...
 */
@Configuration
@ConditionalOnClass(WebMvcConfigurer.class)
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AliasModelAttributeMethodServletProcessor(true));
    }

}
