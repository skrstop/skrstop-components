package com.zoe.framework.components.starter.annotation.configuration;

import com.zoe.framework.components.starter.annotation.constant.GlobalConfigConst;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * GlobalResponseConfig class
 *
 * @author 蒋时华
 * @date 2019/6/4
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(GlobalConfigConst.ANNOTATION_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
@RefreshScope
public class AnnotationProperties {
    /*** 只允许public方法 */
    private boolean allowedPublicOnly = false;

    @NestedConfigurationProperty
    private AccessControl accessControl = new AccessControl();

    @NestedConfigurationProperty
    private ExecuteTimeLog executeTimeLog = new ExecuteTimeLog();

    @Getter
    @Setter
    public static class AccessControl {

        /*** 放行的方法列表 */
        private List<String> releaseAlias = new ArrayList<>();

    }

    @Getter
    @Setter
    public static class ExecuteTimeLog {

        /*** log日志级别为:info, 否则为:debug */
        private boolean logLevelInfo = true;

    }


}
