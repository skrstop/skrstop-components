package com.skrstop.framework.components.starter.objectStorage.entiry;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2024-03-18 18:39:16
 * @since 1.0.0
 */
@Getter
@Setter
public class StorageTemplateSign {

    /*** 有效期时间，单位：秒 */
    private Long expireSecondTime;
    /*** 有效期截止时间 */
    private LocalDateTime expireDateTime;
    /*** 文件类型限制 */
    private List<String> limitContentType;
    /*** 最小限制 */
    private Long limitMinSize;
    /*** 最大限制 */
    private Long limitMaxSize;
}
