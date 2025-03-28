package com.skrstop.framework.components.starter.objectStorage.configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * oss Config class
 *
 * @author 蒋时华
 * @date 2019/7/2
 */
@Getter
@Setter
public class OssProperties {

    private boolean enable = false;
    /*** accessKeyId */
    private String accessKeyId;
    /*** accessKeySecret */
    private String accessKeySecret;
    /*** 前缀路径 */
    private String basePath = "";
    /*** 访问域名 */
    private String accessUrlProtocol = "https";
    private String accessUrlHost;
    /*** endpoint */
    private String endpoint;
    /*** 区域 */
    private String region;
    /*** 存储桶名 */
    private String bucketName;
    /*** 超时时间，单位：毫秒, 默认：5m */
    private Integer requestTimeout = 5 * 60 * 1000;
    /*** 是否开启多线程分片操作 */
    private boolean useMultiThread = false;
    /*** 分片并发，线程最小 */
    private Integer threadMin = 0;
    /*** 分片并发，线程最大 */
    private Integer threadMax = 32;
    /*** 分片并发，限制时间，单位：分钟，默认：10分钟 */
    private Integer threadKeepAliveTime = 10;
    /*** 分片并发，队列数量，默认：100 */
    private Integer threadQueue = 100;
    /*** 分块上传的块大小，单位：字节, 默认：5MB */
    private Long minimumUploadPartSize = 1024 * 1024 * 5L;
    /*** 大于等于该值则并发的分块上传文件，单位, 默认：5MB */
    private Long multipartUploadThreshold = 1024 * 1024 * 5L;
    /*** 大于等于该值则并发的分块复制文件，单位：字节，默认为5GB */
    private Long multipartCopyThreshold = 1024 * 1024 * 1024L * 5;
    /*** 分块复制的块大小，单位：字节，默认为100MB */
    private Long multipartCopyPartSize = 1024 * 1024 * 100L;
}
