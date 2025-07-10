package com.skrstop.framework.components.starter.objectStorage.entity;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author 蒋时华
 * @date 2025-03-29 11:10:22
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class UploadLimit {

    private Long minSize;
    private Long maxSize;
    private List<String> contentType;
    @Builder.Default
    private Long expireSecondTime = 3600L;
    /*** 是否精确匹配target */
    @Builder.Default
    private Boolean exactMatchTarget = false;

}
