package com.zoe.framework.components.starter.id.autoconfig;

import com.zoe.framework.components.starter.id.config.GlobalIdConfig;
import com.zoe.framework.components.starter.id.constant.GenerateTypeConst;
import com.zoe.framework.components.starter.id.service.IdGenerationService;
import com.zoe.framework.components.starter.id.service.strategy.LocalIdGenerationServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * @author 蒋时华
 * @date 2020-05-11 18:53:48
 */
@Service
@EnableConfigurationProperties(GlobalIdConfig.class)
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(value = "zoe.id.config.enable", havingValue = "true", matchIfMissing = true)
public class IdServiceAutoConfig {

    @Bean
    public IdGenerationService idGenerationService(GlobalIdConfig globalIdConfig) {
        if (GenerateTypeConst.LOCAL == globalIdConfig.getGenerateType()) {
            return new LocalIdGenerationServiceImpl(globalIdConfig);
        }
        return new LocalIdGenerationServiceImpl(globalIdConfig);
    }

}
