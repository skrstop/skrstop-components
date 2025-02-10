package com.skrstop.framework.components.starter.feign.protostuff.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * @author Ryan Baxter
 */
@ConfigurationProperties(prefix = "spring.cloud.openfeign.ok-http")
@Setter
@Getter
public class FeignOkHttpProperties {

    /**
     * Default value for disabling SSL validation.
     */
    public static final boolean DEFAULT_DISABLE_SSL_VALIDATION = true;

    /**
     * Default value for max number od connections.
     */
    public static final int DEFAULT_MAX_CONNECTIONS = 200;

    /**
     * Default value for time to live.
     */
    public static final long DEFAULT_TIME_TO_LIVE = 900L;

    /**
     * Default time to live unit.
     */
    public static final TimeUnit DEFAULT_TIME_TO_LIVE_UNIT = TimeUnit.SECONDS;

    /**
     * Default value for following redirects.
     */
    public static final boolean DEFAULT_FOLLOW_REDIRECTS = true;

    /**
     * Default value for connection timeout.
     */
    public static final int DEFAULT_CONNECTION_TIMEOUT = 2000;

    /**
     * Default value for read timeout.
     */
    public static final int DEFAULT_READ_TIMEOUT = 3000;
    public static final int DEFAULT_WRITE_TIMEOUT = 3000;

    private boolean disableSslValidation = DEFAULT_DISABLE_SSL_VALIDATION;

    private int maxConnections = DEFAULT_MAX_CONNECTIONS;

    private long timeToLive = DEFAULT_TIME_TO_LIVE;

    private TimeUnit timeToLiveUnit = DEFAULT_TIME_TO_LIVE_UNIT;

    private boolean followRedirects = DEFAULT_FOLLOW_REDIRECTS;

    private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;

    private int readTimeout = DEFAULT_READ_TIMEOUT;

    private int writeTimeout = DEFAULT_WRITE_TIMEOUT;

    /*** 输出info级别日志 */
    private boolean logInfoLevelForRequest = false;

}
