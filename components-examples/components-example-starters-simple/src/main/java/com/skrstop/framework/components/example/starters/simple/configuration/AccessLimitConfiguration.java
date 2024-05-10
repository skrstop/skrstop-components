package com.skrstop.framework.components.example.starters.simple.configuration;

import com.skrstop.framework.components.starter.annotation.handle.function.accessLimit.AccessLimitRule;
import com.skrstop.framework.components.starter.annotation.handle.function.accessLimit.DefaultAccessLimitRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 蒋时华
 * @date 2024-05-10 21:56:44
 * @since 1.0.0
 */
@Configuration
public class AccessLimitConfiguration {

    @Bean
    public AccessLimitRule accessLimitRule1() {
        return new DefaultAccessLimitRule();
    }

    @Bean
    public CustomAccessLimitRule accessLimitRule2() {
        return new CustomAccessLimitRule();
    }

    public static class CustomAccessLimitRule extends DefaultAccessLimitRule {

    }

}
