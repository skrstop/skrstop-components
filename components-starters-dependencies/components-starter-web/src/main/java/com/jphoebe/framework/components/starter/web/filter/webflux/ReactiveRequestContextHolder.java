package com.jphoebe.framework.components.starter.web.filter.webflux;

import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * @author 蒋时华
 * @date 2020-07-06 11:56:18
 */
public class ReactiveRequestContextHolder {

    static ThreadLocal<ServerHttpRequest> REQUEST_CONTEXT = new ThreadLocal<>();

    public static void set(ServerHttpRequest serverHttpRequest) {
        ReactiveRequestContextHolder.REQUEST_CONTEXT.set(serverHttpRequest);
    }

    public static ServerHttpRequest get() {
        return ReactiveRequestContextHolder.REQUEST_CONTEXT.get();
    }

    public static void remove() {
        ReactiveRequestContextHolder.REQUEST_CONTEXT.remove();
    }

}
