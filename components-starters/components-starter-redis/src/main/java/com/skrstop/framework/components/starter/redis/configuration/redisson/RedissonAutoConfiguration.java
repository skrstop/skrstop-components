package com.skrstop.framework.components.starter.redis.configuration.redisson;

import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.starter.redis.constant.GlobalConfigConst;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author 蒋时华
 * @date 2021-05-25 20:22:53
 */
@ConditionalOnClass({RedissonClient.class, Config.class})
@EnableConfigurationProperties({GlobalRedissonProperties.class})
@Configuration
@ConditionalOnProperty(value = GlobalConfigConst.REDISSON_PREFIX + ".enabled", havingValue = "true", matchIfMissing = false)
public class RedissonAutoConfiguration {

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient(GlobalRedissonProperties globalRedissonProperties, ApplicationContext ctx) throws SkrstopRuntimeException, IOException {
        Config config = null;
        if (StrUtil.isNotBlank(globalRedissonProperties.getConfigFile())) {
            Resource resource = ctx.getResource(globalRedissonProperties.getConfigFile());
            InputStream is = resource.getInputStream();
            config = Config.fromYAML(is);
        } else if (StrUtil.isNotBlank(globalRedissonProperties.getConfigYamlStr())) {
            config = Config.fromYAML(globalRedissonProperties.getConfigYamlStr());
        } else if (StrUtil.isNotBlank(globalRedissonProperties.getConfigJsonStr())) {
            config = Config.fromJSON(globalRedissonProperties.getConfigJsonStr());
        }
        if (ObjectUtil.isNull(config)) {
            throw new SkrstopRuntimeException("redisson未配置相关参数");
        }
        return Redisson.create(config);
    }

}

