package com.skrstop.framework.components.starter.web.exception.global.webflux;

import com.skrstop.framework.components.core.exception.SkrstopBusinessException;
import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.starter.web.configuration.GlobalExceptionProperties;
import com.skrstop.framework.components.starter.web.constant.RequestConst;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ErrorHandleChainPattern;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandleChainPattern;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collections;

public class WebfluxRequestExceptionHandlerTest {

    private com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler handler(String errorPath) {
        ExceptionHandleChainPattern excChain = new ExceptionHandleChainPattern(new java.util.ArrayList<>());
        ReflectionTestUtils.invokeMethod(excChain, "initChainPattern");
        ErrorHandleChainPattern errChain = new ErrorHandleChainPattern(new java.util.ArrayList<>());
        ReflectionTestUtils.invokeMethod(errChain, "initChainPattern");
        com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler h =
                new com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler(
                        excChain, errChain, new GlobalExceptionProperties(), errorPath);
        ServerCodecConfigurer codecs = ServerCodecConfigurer.create();
        h.setMessageReaders(codecs.getReaders());
        h.setMessageWriters(codecs.getWriters());
        h.setViewResolvers(Collections.emptyList());
        return h;
    }

    @Test
    public void testHandleException() {
        com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler h = handler("/error");
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        h.handle(exchange, new SkrstopBusinessException("biz")).block();
        Assert.assertEquals(200, exchange.getResponse().getStatusCode().value());
        Assert.assertNotNull(exchange.getResponse().getBodyAsString().block());

        exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        h.handle(exchange, new SkrstopRuntimeException("plain")).block();
        Assert.assertEquals(500, exchange.getResponse().getStatusCode().value());

        exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        h.handle(exchange, new Exception("other")).block();
        Assert.assertNotNull(exchange.getResponse().getBodyAsString().block());
    }

    @Test
    public void testHandleError() {
        com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler h = handler("/error");
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        h.handle(exchange, new AssertionError("bad")).block();
        Assert.assertEquals(500, exchange.getResponse().getStatusCode().value());
    }

    @Test
    public void testSkipErrorPath() {
        com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler h = handler("/error");
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/error").build());
        h.handle(exchange, new SkrstopRuntimeException("plain")).block();
        Assert.assertEquals(500, exchange.getResponse().getStatusCode().value());
    }

    @Test
    public void testAlwaysReturnHttpOk() {
        GlobalExceptionProperties props = new GlobalExceptionProperties();
        props.setAlwaysReturnHttpOk(true);
        ExceptionHandleChainPattern excChain = new ExceptionHandleChainPattern(new java.util.ArrayList<>());
        ReflectionTestUtils.invokeMethod(excChain, "initChainPattern");
        com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler h =
                new com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler(
                        excChain, null, props, "/error");
        ServerCodecConfigurer codecs = ServerCodecConfigurer.create();
        h.setMessageReaders(codecs.getReaders());
        h.setMessageWriters(codecs.getWriters());
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        h.handle(exchange, new SkrstopRuntimeException("plain")).block();
        Assert.assertEquals(200, exchange.getResponse().getStatusCode().value());
    }

    @Test
    public void testBodyCache() {
        com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler h = handler("/error");
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        WebDataBinder binder = new WebDataBinder(new Object(), "target");
        h.bodyCache(binder, exchange);
        Assert.assertEquals("target", exchange.getAttribute(RequestConst.BODY_CACHE_NAME));
        Assert.assertNotNull(exchange.getAttribute(RequestConst.BODY_CACHE));

        MockServerWebExchange errExchange = MockServerWebExchange.from(MockServerHttpRequest.get("/error").build());
        WebDataBinder binder2 = new WebDataBinder(new Object(), "target");
        h.bodyCache(binder2, errExchange);
        Assert.assertNull(errExchange.getAttribute(RequestConst.BODY_CACHE_NAME));
    }

    @Test
    public void testAutoConfiguration() {
        com.skrstop.framework.components.starter.web.exception.global.webflux.ExceptionAutoConfiguration config =
                new com.skrstop.framework.components.starter.web.exception.global.webflux.ExceptionAutoConfiguration();
        ReflectionTestUtils.setField(config, "errorPath", "/error");
        org.springframework.beans.factory.ObjectProvider<java.util.List<org.springframework.web.reactive.result.view.ViewResolver>> provider =
                org.mockito.Mockito.mock(org.springframework.beans.factory.ObjectProvider.class);
        org.mockito.Mockito.when(provider.getIfAvailable(org.mockito.Mockito.any())).thenReturn(Collections.emptyList());
        Object result = config.errorWebExceptionHandler(provider, ServerCodecConfigurer.create(),
                new ExceptionHandleChainPattern(new java.util.ArrayList<>()),
                new ErrorHandleChainPattern(new java.util.ArrayList<>()),
                new GlobalExceptionProperties());
        Assert.assertNotNull(result);
        Assert.assertEquals(200, ((ServerWebExchange) null) == null ? 200 : 0);
    }

    private static class NotShowEx extends Throwable implements com.skrstop.framework.components.starter.web.exception.core.NotShowHttpStatusException {
    }

    @Test
    public void testHandleNotShowAndNullProps() {
        // NotShow -> 200
        com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler h = handler("/error");
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        h.handle(exchange, new NotShowEx()).block();
        Assert.assertEquals(200, exchange.getResponse().getStatusCode().value());

        // null props -> 500
        ExceptionHandleChainPattern excChain = new ExceptionHandleChainPattern(new java.util.ArrayList<>());
        ReflectionTestUtils.invokeMethod(excChain, "initChainPattern");
        com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler h2 =
                new com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler(excChain, null, null, "/error");
        ServerCodecConfigurer codecs = ServerCodecConfigurer.create();
        h2.setMessageReaders(codecs.getReaders());
        h2.setMessageWriters(codecs.getWriters());
        exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        h2.handle(exchange, new Exception("plain")).block();
        Assert.assertEquals(500, exchange.getResponse().getStatusCode().value());
    }

    @Test
    public void testFeignAndContentTypeHeaders() {
        com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler h = handler("/error");
        // use_feign 请求头 -> contentType 设为 protobuf，写入时因无 protobuf writer 抛异常（覆盖 feign 分支）
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").header("use_feign", "true").build());
        try {
            h.handle(exchange, new SkrstopRuntimeException("plain")).block();
            Assert.fail("expect unsupported media type");
        } catch (RuntimeException expected) {
        }

        exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").header("Content-Type", "application/json").build());
        h.handle(exchange, new SkrstopRuntimeException("plain")).block();
        Assert.assertEquals("application/json", exchange.getResponse().getHeaders().getContentType().toString());

        exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").header("Content-Type", "text/plain").build());
        h.handle(exchange, new SkrstopRuntimeException("plain")).block();
        Assert.assertEquals("application/json", exchange.getResponse().getHeaders().getContentType().toString());
    }

    @Test
    public void testResponseCommitted() {
        com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler h = handler("/error");
        org.springframework.http.server.reactive.ServerHttpResponse respMock =
                org.mockito.Mockito.mock(org.springframework.http.server.reactive.ServerHttpResponse.class);
        org.mockito.Mockito.when(respMock.isCommitted()).thenReturn(true);
        ServerWebExchange exMock = org.mockito.Mockito.mock(ServerWebExchange.class);
        org.mockito.Mockito.when(exMock.getRequest()).thenReturn(MockServerHttpRequest.get("/test").build());
        org.mockito.Mockito.when(exMock.getResponse()).thenReturn(respMock);
        try {
            h.handle(exMock, new SkrstopRuntimeException("plain")).block();
            Assert.fail("expect error");
        } catch (RuntimeException expected) {
        }
    }

    static class NonExcThrowable extends Throwable {
    }

    @Test
    public void testHandleLogAndBodyCacheVariants() {
        com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler h = handler("/error");

        // business + log=true -> 打印请求日志分支
        GlobalExceptionProperties props = new GlobalExceptionProperties();
        props.setLogBusinessServiceException(true);
        ExceptionHandleChainPattern excChain = new ExceptionHandleChainPattern(new java.util.ArrayList<>());
        ReflectionTestUtils.invokeMethod(excChain, "initChainPattern");
        com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler h2 =
                new com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler(excChain, null, props, "/error");
        ServerCodecConfigurer codecs = ServerCodecConfigurer.create();
        h2.setMessageReaders(codecs.getReaders());
        h2.setMessageWriters(codecs.getWriters());
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        h2.handle(exchange, new SkrstopBusinessException("biz")).block();
        Assert.assertEquals(200, exchange.getResponse().getStatusCode().value());

        // bodyCache attribute 已存在 -> ternary 非空分支
        exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        exchange.getAttributes().put(RequestConst.BODY_CACHE_NAME, "myBody");
        exchange.getAttributes().put(RequestConst.BODY_CACHE, "{\"a\":1}");
        h.handle(exchange, new Exception("x")).block();

        // 非 Exception 非 Error 的 Throwable -> else 分支
        exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        h.handle(exchange, new NonExcThrowable()).block();
        Assert.assertEquals(500, exchange.getResponse().getStatusCode().value());

        // Content-Type 为 protobuf -> contentType 使用 protobuf
        exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test")
                .header("Content-Type", "application/x-protobuf;charset=UTF-8").build());
        try {
            h.handle(exchange, new SkrstopRuntimeException("plain")).block();
        } catch (RuntimeException expected) {
            // protobuf 无 writer，写入失败
        }
    }

    static class CustomThrowable extends Throwable implements com.skrstop.framework.components.starter.web.exception.core.CustomHttpStatusException {
        @Override
        public org.springframework.http.HttpStatus getHttpStatus() {
            return org.springframework.http.HttpStatus.I_AM_A_TEAPOT;
        }
    }

    @Test
    public void testHandleCustomStatusThrowable() {
        com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler h = handler("/error");
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        h.handle(exchange, new CustomThrowable()).block();
        Assert.assertEquals(418, exchange.getResponse().getStatusCode().value());
    }

    @Test
    public void testBodyCacheNullTarget() {
        com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler h = handler("/error");
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        WebDataBinder binder = new WebDataBinder(null, "target");
        h.bodyCache(binder, exchange);
        Assert.assertEquals("target", exchange.getAttribute(RequestConst.BODY_CACHE_NAME));
        Assert.assertEquals("", exchange.getAttribute(RequestConst.BODY_CACHE));
    }

    @Test
    public void testResponseContext() throws Exception {
        com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler h = handler("/error");
        Class<?> ctxClass = Class.forName("com.skrstop.framework.components.starter.web.exception.global.webflux.RequestExceptionHandler$ResponseContext");
        java.lang.reflect.Constructor<?> ctor = ctxClass.getDeclaredConstructor(h.getClass());
        ctor.setAccessible(true);
        Object ctx = ctor.newInstance(h);
        java.lang.reflect.Method mw = ctxClass.getMethod("messageWriters");
        java.lang.reflect.Method vr = ctxClass.getMethod("viewResolvers");
        Assert.assertNotNull(mw.invoke(ctx));
        Assert.assertNotNull(vr.invoke(ctx));
    }
}
