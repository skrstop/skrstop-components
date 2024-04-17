package com.skrstop.framework.components.example.starters.cloud.feign.entity;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author 蒋时华
 * @date 2024-04-16 14:25:34
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class DemoInfo {

    private String name;

}
