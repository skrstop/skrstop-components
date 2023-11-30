package com.zoe.framework.components.starter.id.config;

import com.zoe.framework.components.starter.id.constant.GenerateTypeConst;
import com.zoe.framework.components.starter.id.constant.GlobalConfigConst;
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
@ConfigurationProperties(GlobalConfigConst.ID_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalIdProperties {

    /*** 是否开启id service */
    private Boolean enable = true;

    /**
     * id 生成策略
     * {@link GenerateTypeConst}
     */
    private int generateType = GenerateTypeConst.LOCAL;

    /*** 终端id */
    private long workerId = 1;

    /*** 数据中心id */
    private long dataCenterId = 1;

}
