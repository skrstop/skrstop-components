package com.zoe.framework.components.starter.annotation.configutation;

import com.zoe.framework.components.starter.annotation.constant.GlobalConfigConst;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@ConfigurationProperties(GlobalConfigConst.ACCESS_CONTROL_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
@RefreshScope
public class AccessControlProperties {

    /*** 放行的方法列表 */
    private List<String> releaseAlias = new ArrayList<>();

}
