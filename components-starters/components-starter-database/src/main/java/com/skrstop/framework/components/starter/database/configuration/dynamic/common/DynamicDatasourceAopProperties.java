package com.skrstop.framework.components.starter.database.configuration.dynamic.common;

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
public class DynamicDatasourceAopProperties {

    /**
     * enabled default DS annotation default true
     */
    @Builder.Default
    private Boolean enabled = true;
    /**
     * aop order
     */
    @Builder.Default
    private Integer order = Ordered.HIGHEST_PRECEDENCE;
    /**
     * aop allowedPublicOnly
     */
    @Builder.Default
    private Boolean allowedPublicOnly = true;
}