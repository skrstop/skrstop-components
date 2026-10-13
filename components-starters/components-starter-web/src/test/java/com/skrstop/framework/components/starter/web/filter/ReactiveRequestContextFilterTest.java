package com.skrstop.framework.components.starter.web.filter;

import com.skrstop.framework.components.starter.web.filter.webflux.ReactiveRequestContextFilter;
import com.skrstop.framework.components.starter.web.filter.webflux.ReactiveRequestContextHolder;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class ReactiveRequestContextFilterTest {

    @Test
    public void testHolder() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/").build();
        ReactiveRequestContextHolder.set(request);
        Assert.assertSame(request, ReactiveRequestContextHolder.get());
        ReactiveRequestContextHolder.remove();
        Assert.assertNull(ReactiveRequestContextHolder.get());
    }

    @Test
    public void testFilter() {
        ReactiveRequestContextFilter filter = new ReactiveRequestContextFilter();
        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/").build());
        WebFilterChain chain = Mockito.mock(WebFilterChain.class);
        Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());
        filter.filter(exchange, chain).block();
        Mockito.verify(chain).filter(exchange);
    }
}
