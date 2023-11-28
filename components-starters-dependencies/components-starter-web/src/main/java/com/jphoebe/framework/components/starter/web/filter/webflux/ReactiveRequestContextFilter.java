package com.jphoebe.framework.components.starter.web.filter.webflux;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author 蒋时华
 * @date 2020-07-06 11:58:50
 */
public class ReactiveRequestContextFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        try {
            Mono<Void> filter = chain.filter(exchange);
            return filter;
        } finally {
            ReactiveRequestContextHolder.remove();
        }

    }

}
