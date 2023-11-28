package com.jphoebe.framework.components.util.constant;

import lombok.Getter;

/**
 * http 方法常量池
 *
 * @author 蒋时华
 * @date 2019/5/30
 */
public enum HttpMethodEnum {

    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH"),
    HEAD("HEAD"),
    CONNECT("CONNECT"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE"),
    ;

    @Getter
    private String method;

    HttpMethodEnum(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return this.getMethod();
    }
}
