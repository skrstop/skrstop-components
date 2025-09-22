package com.skrstop.framework.components.starter.mongodb.configuration;

import com.skrstop.framework.components.starter.id.service.IdService;
import com.skrstop.framework.components.starter.mongodb.configuration.generator.DefaultIdentifierGenerator;
import com.skrstop.framework.components.starter.mongodb.configuration.generator.IdentifierGenerator;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 蒋时华
 * @date 2020-05-12 10:20:23
 */
@Configuration
@ConditionalOnClass(name = "com.skrstop.framework.components.starter.id.service.IdService")
@AutoConfigureBefore(MorphiaAutoConfiguration.class)
@EnableConfigurationProperties(GlobalMongodbProperties.class)
public class MorphiaIdAutoConfiguration {

    /**
     * 默认id生成器
     *
     * @param properties
     * @return
     * @throws Exception
     */
    @Bean
    public IdentifierGenerator identifierGenerator(GlobalMongodbProperties properties, IdService idService) {
        if (ObjectUtil.isNull(idService)) {
            return new DefaultIdentifierGenerator();
        }
        return new IdentifierGenerator() {

            @Override
            public Number nextId() {
                return idService.getId();
            }

            @Override
            public String nextUUID() {
                return idService.getUuidWithoutDash();
            }
        };
    }

}
