package com.zoe.framework.components.starter.redis.configuration;

import com.zoe.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.zoe.framework.components.starter.redis.constant.GlobalConfigConst;
import com.zoe.framework.components.starter.redis.constant.ValueProcessorConst;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * GlobalResponseConfig class
 *
 * @author 蒋时华
 * @date 2019/6/4
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(GlobalConfigConst.REDIS_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalRedisProperties {

    /**
     * 是否开启redisService
     */
    private Boolean enabled = true;

    /*** 值处理器 */
    private Byte valueProcessor = ValueProcessorConst.FAST_JSON;

    // fastjson
    /*** 是否格式化json */
    private Boolean fastjsonPrettyFormatJson = false;
    /*** 是否开启safeMode */
    private Boolean fastjsonSafeMode = true;
    /*** 是否开启autoType */
    private Boolean fastjsonAutoType = false;
    /*** 此开关用于filter过滤，当autoType序列化和非autoType穿插使用, 并且autoType是开启状态时则开启遍历过滤，建议不要autoType穿插使用 */
    private Boolean fastjsonFilterEach = false;

    public GlobalRedisProperties setValueProcessor(Byte valueProcessor) {
        if (!ValueProcessorConst.VALUES.contains(valueProcessor)) {
            throw new NotSupportedException("不支持此json处理器【json-processor】：" + valueProcessor);
        }
        this.valueProcessor = valueProcessor;
        return this;
    }
}
