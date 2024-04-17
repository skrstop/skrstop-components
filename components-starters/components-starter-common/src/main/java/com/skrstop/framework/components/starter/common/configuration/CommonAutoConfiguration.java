package com.skrstop.framework.components.starter.common.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

/**
 * @author 蒋时华
 * @date 2024-03-19 10:48:05
 * @since 1.0.0
 */
@Configuration
public class CommonAutoConfiguration implements EnvironmentPostProcessor, Ordered {

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> systemProperties = environment.getSystemProperties();
        systemProperties.put("spring.main.allow-bean-definition-overriding", "true");
        systemProperties.put("spring.main.allowBeanDefinitionOverriding", "true");
    }
}
