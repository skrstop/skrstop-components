package com.skrstop.framework.components.starter.web.response.webflux;

import com.skrstop.framework.components.core.common.response.DefaultResult;
import com.skrstop.framework.components.starter.web.configuration.GlobalResponseProperties;
import com.skrstop.framework.components.starter.web.response.DisableGlobalResponse;
import com.skrstop.framework.components.starter.web.response.core.ResponseHandleChainPattern;
import com.skrstop.framework.components.util.constant.FeignConst;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;

public class ReturnResponseAutoConfigurationTest {

    private ResponseBodyResultHandler handler;
    private GlobalResponseProperties props;
    private RequestedContentTypeResolver resolver;

    public static class Controller {
        public String plain() {return "hi";}

        @DisableGlobalResponse
        public String disabled() {return "hi";}
    }

    @Before
    public void setup() {
        props = new GlobalResponseProperties();
        props.setEnable(true);
        ResponseHandleChainPattern chain = new ResponseHandleChainPattern(new ArrayList<>());
        ReflectionTestUtils.invokeMethod(chain, "initChainPattern");
        ReturnResponseAutoConfiguration config = new ReturnResponseAutoConfiguration(props, chain);
        resolver = Mockito.mock(RequestedContentTypeResolver.class);
        Mockito.when(resolver.resolveMediaTypes(Mockito.any())).thenReturn(Collections.singletonList(MediaType.APPLICATION_JSON));
        handler = config.responseBodyResultHandler(new ReactiveAdapterRegistry(), ServerCodecConfigurer.create(), resolver);
    }

    private HandlerResult result(String methodName, Object returnValue) throws Exception {
        java.lang.reflect.Method m = Controller.class.getDeclaredMethod(methodName);
        org.springframework.web.method.HandlerMethod hm = new org.springframework.web.method.HandlerMethod(new Controller(), m);
        MethodParameter mp = new MethodParameter(m, -1);
        return new HandlerResult(hm, returnValue, mp);
    }

    @Test
    public void testDisabled() throws Exception {
        // props 关闭
        props.setEnable(false);
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        handler.handleResult(exchange, result("plain", "hi")).block();
        Assert.assertNotNull(exchange.getResponse().getBodyAsString().block());

        // props 开启
        props.setEnable(true);
        exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        handler.handleResult(exchange, result("plain", "hi")).block();
        Assert.assertNotNull(exchange.getResponse().getBodyAsString().block());
    }

    @Test
    public void testValidPath() throws Exception {
        props.setDisableGlobalResponseList(java.util.Arrays.asList("**"));
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        handler.handleResult(exchange, result("plain", "hi")).block();
        Assert.assertNotNull(exchange.getResponse().getBodyAsString().block());
    }

    @Test
    public void testDisableGlobalResponseAnno() throws Exception {
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        handler.handleResult(exchange, result("disabled", "hi")).block();
        Assert.assertNotNull(exchange.getResponse().getBodyAsString().block());
    }

    @Test
    public void testNotSupportFeign() throws Exception {
        props.setSupportFeign(false);
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").header(FeignConst.USE_FEIGN_NAME, FeignConst.USE_FEIGN_VALUE).build());
        handler.handleResult(exchange, result("plain", "hi")).block();
        Assert.assertNotNull(exchange.getResponse().getBodyAsString().block());
    }

    @Test
    public void testPlainReturnValue() throws Exception {
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        handler.handleResult(exchange, result("plain", "hi")).block();
        Assert.assertEquals(200, exchange.getResponse().getRawStatusCode().intValue());
    }

    @Test
    public void testMonoReturnValue() throws Exception {
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        handler.handleResult(exchange, result("plain", Mono.just(DefaultResult.Builder.success()))).block();
        Assert.assertEquals(200, exchange.getResponse().getRawStatusCode().intValue());

        // Mono.empty -> defaultIfEmpty
        exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        handler.handleResult(exchange, result("plain", Mono.empty())).block();
        Assert.assertEquals(200, exchange.getResponse().getRawStatusCode().intValue());
    }

    @Test
    public void testFluxReturnValue() throws Exception {
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        handler.handleResult(exchange, result("plain", Flux.just(DefaultResult.Builder.success()))).block();
        Assert.assertNotNull(exchange.getResponse().getBodyAsString().block());

        // Flux.empty -> defaultIfEmpty
        exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        handler.handleResult(exchange, result("plain", Flux.empty())).block();
        Assert.assertNotNull(exchange.getResponse().getBodyAsString().block());
    }

    public static class TransController {
        @com.skrstop.framework.components.starter.web.response.DisableTransResultTypeResponse
        public String transDisabled() {return "hi";}

        public String plain() {return "hi";}
    }

    @Test
    public void testDisableTransVariants() throws Exception {
        // 注解关闭转换
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        java.lang.reflect.Method m = TransController.class.getMethod("transDisabled");
        org.springframework.web.method.HandlerMethod hm = new org.springframework.web.method.HandlerMethod(new TransController(), m);
        handler.handleResult(exchange, new HandlerResult(hm, "hi", new MethodParameter(m, -1))).block();
        Assert.assertEquals(200, exchange.getResponse().getRawStatusCode().intValue());

        // props 全局关闭转换
        props.setDisableGlobalTransResultTypeResponse(true);
        exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        java.lang.reflect.Method m2 = TransController.class.getMethod("plain");
        org.springframework.web.method.HandlerMethod hm2 = new org.springframework.web.method.HandlerMethod(new TransController(), m2);
        handler.handleResult(exchange, new HandlerResult(hm2, "hi", new MethodParameter(m2, -1))).block();
        Assert.assertEquals(200, exchange.getResponse().getRawStatusCode().intValue());
        props.setDisableGlobalTransResultTypeResponse(false);
    }

    @Test
    public void testValidPathException() throws Exception {
        // 列表包含 null -> UrlFilterUtil 抛异常 -> catch 分支
        props.setDisableGlobalResponseList(java.util.Collections.singletonList(null));
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        handler.handleResult(exchange, result("plain", "hi")).block();
        Assert.assertNotNull(exchange.getResponse().getBodyAsString().block());
        props.setDisableGlobalResponseList(new java.util.ArrayList<>());
    }

    @Test
    public void testIResultMethodParameter() throws Exception {
        java.lang.reflect.Method m = TransController.class.getMethod("plain");
        MethodParameter mp = new MethodParameter(m, -1);
        ReturnResponseAutoConfiguration.IResultMethodParameter wrapped = new ReturnResponseAutoConfiguration.IResultMethodParameter(mp);
        Assert.assertEquals(com.skrstop.framework.components.core.common.response.core.IResult.class, wrapped.getParameterType());
        Assert.assertEquals(com.skrstop.framework.components.core.common.response.core.IResult.class, wrapped.getGenericParameterType());
    }

    @Test
    public void testSupportFeignTrue() throws Exception {
        props.setSupportFeign(true);
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").header(FeignConst.USE_FEIGN_NAME, FeignConst.USE_FEIGN_VALUE).build());
        handler.handleResult(exchange, result("plain", "hi")).block();
        Assert.assertEquals(200, exchange.getResponse().getRawStatusCode().intValue());
    }
}
