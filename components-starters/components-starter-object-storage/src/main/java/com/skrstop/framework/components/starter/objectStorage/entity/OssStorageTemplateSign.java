package com.skrstop.framework.components.starter.objectStorage.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 蒋时华
 * @date 2024-03-18 18:39:27
 * @since 1.0.0
 */
@Getter
@Setter
public class OssStorageTemplateSign extends StorageTemplateSign {
    private String accessKeyId;
    private String policy;
    private String signature;
    private String bucketName;
    private String region;
    private String endpoint;
}
