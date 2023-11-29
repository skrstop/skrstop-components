package com.zoe.framework.components.starter.redis.redisson;

import com.zoe.framework.components.starter.redis.constant.GlobalConfigConst;
import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 蒋时华
 * @date 2021-05-25 19:50:45
 */
@Getter
@Setter
@Configuration
@ConditionalOnClass({Config.class, RedissonClient.class})
@ConfigurationProperties(GlobalConfigConst.REDISSON_PREFIX)
public class RedissonProperties {

    /**
     * 是否开启redisson.
     */
    private Boolean enabled;
    /**
     * 指定redisson配置文件.
     */
    private String file;
    /**
     * yaml配置文本.
     */
    private String configYamlStr;
    /**
     * json配置文本
     */
    @Deprecated
    private String configJsonStr;

    public RedissonProperties() {
        this.enabled = true;
    }

}
