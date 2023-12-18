package com.zoe.framework.components.starter.redis.configuration.commonDynamic.builder;

/**
 * @author 蒋时华
 * @date 2023-12-07 17:59:29
 */
public class RedisUrlSyntaxException extends RuntimeException {

    private final String url;

    RedisUrlSyntaxException(String url, Exception cause) {
        super(buildMessage(url), cause);
        this.url = url;
    }

    RedisUrlSyntaxException(String url) {
        super(buildMessage(url));
        this.url = url;
    }

    String getUrl() {
        return this.url;
    }

    private static String buildMessage(String url) {
        return "Invalid Redis URL '" + url + "'";
    }

}
