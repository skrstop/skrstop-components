package com.skrstop.framework.components.example.starters.simple.configuration;

import com.skrstop.framework.components.starter.annotation.handle.function.privacyInfo.PrivacyInfoTypeRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * @author 蒋时华
 * @date 2024-05-10 21:56:44
 * @since 1.0.0
 */
@Configuration
public class PrivacyInfoConfiguration {

    @Bean
    public CustomPrivacyInfoTypeRule accessLimitRule1() {
        return new CustomPrivacyInfoTypeRule();
    }


    public static class CustomPrivacyInfoTypeRule implements PrivacyInfoTypeRule {

        @Override
        public Set<String> supportType() {
            return Set.of("test");
        }

        @Override
        public String handle(String originVal) {
            return "处理后的结果";
        }
    }

}
