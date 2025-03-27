package com.skrstop.framework.components.starter.redis.configuration.common;

import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.skrstop.framework.components.starter.redis.constant.GlobalConfigConst;
import com.skrstop.framework.components.starter.redis.constant.ValueProcessorConst;
import com.skrstop.framework.components.util.constant.DateFormatConst;
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
    private boolean enabled = true;

    /*** 值处理器 */
    private Byte valueProcessor = ValueProcessorConst.FAST_JSON;

    // fastjson
    /*** 是否格式化json */
    private boolean fastjsonPrettyFormatJson = false;
    /*** 是否开启safeMode */
    private boolean fastjsonSafeMode = true;
    /*** 是否开启autoType */
    private boolean fastjsonAutoType = false;
    /*** 此开关用于filter过滤，当autoType序列化和非autoType穿插使用, 并且autoType是开启状态时则开启遍历过滤，建议不要autoType穿插使用 */
    private boolean fastjsonFilterEach = false;
    /*** 时间格式化 */
    private String datetimeFormat = DateFormatConst.NORM_DATETIME_PATTERN;
    private String dateFormat = DateFormatConst.NORM_DATE_PATTERN;
    private String timeFormat = DateFormatConst.NORM_TIME_PATTERN;

    public GlobalRedisProperties setValueProcessor(Byte valueProcessor) {
        if (!ValueProcessorConst.VALUES.contains(valueProcessor)) {
            throw new NotSupportedException("不支持此json处理器【json-processor】：" + valueProcessor);
        }
        this.valueProcessor = valueProcessor;
        return this;
    }
}
