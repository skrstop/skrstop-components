package com.zoe.framework.components.starter.id.configuration;

import com.zoe.framework.components.starter.id.config.GlobalIdProperties;
import com.zoe.framework.components.starter.id.constant.GenerateTypeConst;
import com.zoe.framework.components.starter.id.service.IdService;
import com.zoe.framework.components.starter.id.service.strategy.LocalIdServiceImpl;
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
@EnableConfigurationProperties(GlobalIdProperties.class)
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(value = "zoe.id.config.enable", havingValue = "true", matchIfMissing = true)
public class IdServiceAutoConfiguration {

    @Bean
    public IdService idGenerationService(GlobalIdProperties globalIdProperties) {
        if (GenerateTypeConst.LOCAL == globalIdProperties.getGenerateType()) {
            return new LocalIdServiceImpl(globalIdProperties);
        }
        return new LocalIdServiceImpl(globalIdProperties);
    }

}
