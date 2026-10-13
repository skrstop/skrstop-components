package com.skrstop.framework.components.starter.web.configuration;

import com.skrstop.framework.components.core.common.response.DefaultResult;
import feign.Request;
import feign.Response;
import feign.codec.Decoder;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class FeignResponseAuthConfigurationTest {

    private Decoder buildDecoder() {
        FeignResponseAuthConfiguration config = new FeignResponseAuthConfiguration();
        ObjectProvider<HttpMessageConverterCustomizer> customizers = Mockito.mock(ObjectProvider.class);
        Mockito.when(customizers.stream()).thenReturn(java.util.stream.Stream.empty());
        ObjectFactory<HttpMessageConverters> convertersFactory = () -> new HttpMessageConverters(
                new MappingJackson2HttpMessageConverter());
        return config.customAutoResponseDecoder(customizers, convertersFactory);
    }

    private Response feignResponse(String body) {
        return Response.builder()
                .status(200)
                .reason("ok")
                .request(Request.create(Request.HttpMethod.GET, "/", Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .headers(Collections.emptyMap())
                .headers(Collections.singletonMap("Content-Type", java.util.Arrays.asList("application/json")))
                .body(body, StandardCharsets.UTF_8)
                .build();
    }

    private Type typeOf(final Class<?> raw, final Type... args) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return args;
            }

            @Override
            public Type getRawType() {
                return raw;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }

    @Test
    public void testDefaultResultTargetReturnsNull() throws Exception {
        Decoder decoder = buildDecoder();
        Object result = decoder.decode(feignResponse("{}"), typeOf(DefaultResult.class));
        Assert.assertNull(result);
    }

    @Test
    public void testIResultTargetDelegates() throws Exception {
        Decoder decoder = buildDecoder();
        Response response = feignResponse("{\"code\":\"0\",\"message\":\"ok\"}");
        Object result = decoder.decode(response, typeOf(com.skrstop.framework.components.core.common.response.Result.class));
        Assert.assertNotNull(result);
    }

    @Test
    public void testPlainType() throws Exception {
        Decoder decoder = buildDecoder();
        // wrapped result → unwraps data
        Response response = feignResponse("{\"code\":\"0\",\"message\":\"ok\",\"data\":{\"name\":\"x\"}}");
        Object result = decoder.decode(response, typeOf(com.alibaba.fastjson2.JSONObject.class));
        Assert.assertNotNull(result);
        Assert.assertEquals("x", ((java.util.Map) result).get("name"));

        // empty body → null
        Response emptyResponse = feignResponse("");
        Assert.assertNull(decoder.decode(emptyResponse, typeOf(com.alibaba.fastjson2.JSONObject.class)));

        // result without data → null
        Response noData = feignResponse("{\"code\":\"0\",\"message\":\"ok\"}");
        Assert.assertNull(decoder.decode(noData, typeOf(com.alibaba.fastjson2.JSONObject.class)));
    }
}
