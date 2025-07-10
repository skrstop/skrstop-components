package com.skrstop.framework.components.starter.objectStorage.entity;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author 蒋时华
 * @date 2025-03-29 12:02:17
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class TemporaryAccessExtraParam {

    @Builder.Default
    private long expireSecondTime = 3600L;
    private Map<String, Object> queryParams;
    private Map<String, Object> headerParams;
    private boolean useOriginHost;

}
