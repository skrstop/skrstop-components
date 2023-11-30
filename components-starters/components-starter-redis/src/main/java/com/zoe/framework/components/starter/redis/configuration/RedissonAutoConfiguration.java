package com.zoe.framework.components.starter.redis.configuration;

import com.zoe.framework.components.core.exception.ZoeException;
import com.zoe.framework.components.starter.redis.constant.GlobalConfigConst;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import com.zoe.framework.components.util.value.data.StrUtil;
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
@EnableConfigurationProperties({RedissonProperties.class})
@Configuration
@ConditionalOnProperty(value = GlobalConfigConst.REDISSON_PREFIX + ".enabled", havingValue = "true", matchIfMissing = false)
public class RedissonAutoConfiguration {

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient(RedissonProperties redissonProperties, ApplicationContext ctx) throws ZoeException, IOException {
        Config config = null;
        if (StrUtil.isNotBlank(redissonProperties.getFile())) {
            Resource resource = ctx.getResource(redissonProperties.getFile());
            InputStream is = resource.getInputStream();
            config = Config.fromYAML(is);
        } else if (StrUtil.isNotBlank(redissonProperties.getConfigYamlStr())) {
            config = Config.fromYAML(redissonProperties.getConfigYamlStr());
        } else if (StrUtil.isNotBlank(redissonProperties.getConfigJsonStr())) {
            config = Config.fromJSON(redissonProperties.getConfigJsonStr());
        }
        if (ObjectUtil.isNull(config)) {
            throw new ZoeException("redisson未配置相关参数");
        }
        return Redisson.create(config);
    }

}

