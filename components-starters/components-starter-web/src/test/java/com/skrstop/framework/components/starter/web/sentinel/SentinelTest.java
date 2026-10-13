package com.skrstop.framework.components.starter.web.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.skrstop.framework.components.starter.web.configuration.sentinel.SentinelDefaultResponseMvcAutoConfiguration;
import com.skrstop.framework.components.starter.web.configuration.sentinel.SentinelDefaultResponseReactiveAutoConfiguration;
import com.skrstop.framework.components.starter.web.configuration.sentinel.webflux.DefaultWebFluxBlockRequestHandler;
import com.skrstop.framework.components.starter.web.configuration.sentinel.webmvc.DefaultWebMvcBlockExceptionHandler;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.server.MockServerWebExchange;

public class SentinelTest {

    @Test
    public void testMvcAutoConfiguration() {
        Assert.assertNotNull(new SentinelDefaultResponseMvcAutoConfiguration().defaultWebMvcBlockExceptionHandler());
        Assert.assertNotNull(new SentinelDefaultResponseReactiveAutoConfiguration().defaultWebFluxBlockRequestHandler());
    }

    @Test
    public void testWebMvcBlockExceptionHandler() throws Exception {
        DefaultWebMvcBlockExceptionHandler handler = new DefaultWebMvcBlockExceptionHandler();
        MockHttpServletResponse response = new MockHttpServletResponse();
        BlockException e = Mockito.mock(BlockException.class);
        handler.handle(new MockHttpServletRequest(), response, "rule", e);
        Assert.assertEquals(200, response.getStatus());
        Assert.assertFalse(response.getContentAsString().isEmpty());
    }

    @Test
    public void testWebFluxBlockRequestHandler() {
        DefaultWebFluxBlockRequestHandler handler = new DefaultWebFluxBlockRequestHandler();
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/").build());
        BlockException e = Mockito.mock(BlockException.class);
        Mockito.when(e.getMessage()).thenReturn("blocked");
        handler.handleRequest(exchange, e).block();
    }
}
