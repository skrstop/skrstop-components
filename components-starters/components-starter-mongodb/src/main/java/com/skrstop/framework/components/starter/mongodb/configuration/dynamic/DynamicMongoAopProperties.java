package com.skrstop.framework.components.starter.mongodb.configuration.dynamic;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.core.Ordered;

/**
 * 多数据源aop相关配置
 *
 * @author TaoYu
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class DynamicMongoAopProperties {

    /**
     * enabled default DS annotation default true
     */
    private Boolean enabled = true;
    /**
     * aop order
     */
    private Integer order = Ordered.HIGHEST_PRECEDENCE;
    /**
     * aop allowedPublicOnly
     */
    private Boolean allowedPublicOnly = true;
}