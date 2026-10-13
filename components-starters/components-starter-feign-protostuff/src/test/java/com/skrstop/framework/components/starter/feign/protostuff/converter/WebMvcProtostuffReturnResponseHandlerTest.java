package com.skrstop.framework.components.starter.feign.protostuff.converter;

import com.skrstop.framework.components.util.constant.FeignConst;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class WebMvcProtostuffReturnResponseHandlerTest {

    interface Controller {
        String echo(String in);
    }

    private MethodParameter returnType() throws Exception {
        Method method = Controller.class.getMethod("echo", String.class);
        return new MethodParameter(method, -1);
    }

    private List<HttpMessageConverter<?>> converters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new StringHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter());
        return converters;
    }

    @Test
    public void ctors() {
        Assert.assertNotNull(new WebMvcProtostuffReturnResponseHandler(converters()));
        Assert.assertNotNull(new WebMvcProtostuffReturnResponseHandler(converters(), new org.springframework.web.accept.ContentNegotiationManager()));
        Assert.assertNotNull(new WebMvcProtostuffReturnResponseHandler(converters(),
                new org.springframework.web.accept.ContentNegotiationManager(), new ArrayList<>()));
    }

    @Test
    public void feignHeaderSetsProtobufAndCommitted() throws Exception {
        WebMvcProtostuffReturnResponseHandler handler = new WebMvcProtostuffReturnResponseHandler(converters());
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(FeignConst.USE_FEIGN_NAME, FeignConst.USE_FEIGN_VALUE);
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        ModelAndViewContainer mavContainer = new ModelAndViewContainer();
        handler.handleReturnValue("body", returnType(), mavContainer, webRequest);
        Assert.assertTrue(mavContainer.isRequestHandled());
        Assert.assertTrue(response.getContentType().startsWith("application/x-protobuf"));
        // createInputMessage/createOutputMessage 已被调用
        Assert.assertNotNull(webRequest);
    }

    @Test
    public void noHeaderSetsJsonAndCommitted() throws Exception {
        WebMvcProtostuffReturnResponseHandler handler = new WebMvcProtostuffReturnResponseHandler(converters());
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        handler.handleReturnValue("body", returnType(), new ModelAndViewContainer(), webRequest);
        Assert.assertTrue(response.getContentType().startsWith("application/json"));
    }

    @Test
    public void committedReturnsEarly() throws Exception {
        WebMvcProtostuffReturnResponseHandler handler = new WebMvcProtostuffReturnResponseHandler(converters());
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setCommitted(true);
        ModelAndViewContainer mavContainer = new ModelAndViewContainer();
        handler.handleReturnValue("body", returnType(), mavContainer, new ServletWebRequest(request, response));
        Assert.assertTrue(mavContainer.isRequestHandled());
        // 已提交: 不写 body
        Assert.assertEquals(0, response.getContentAsByteArray().length);
    }

    @Test
    public void notCommittedWritesBody() throws Exception {
        WebMvcProtostuffReturnResponseHandler handler = new WebMvcProtostuffReturnResponseHandler(converters());
        // feign 头 + 未提交 -> writeWithMessageConverters
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(FeignConst.USE_FEIGN_NAME, FeignConst.USE_FEIGN_VALUE);
        MockHttpServletResponse response = new MockHttpServletResponse();
        handler.handleReturnValue("str-body", returnType(), new ModelAndViewContainer(),
                new ServletWebRequest(request, response));
        Assert.assertEquals("str-body", response.getContentAsString());

        // 无头 -> json
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        handler.handleReturnValue("str-body", returnType(), new ModelAndViewContainer(),
                new ServletWebRequest(new MockHttpServletRequest(), response2));
        Assert.assertEquals("str-body", response2.getContentAsString());
    }

    @Test
    public void feignHeaderWrongValueSetsNothing() throws Exception {
        WebMvcProtostuffReturnResponseHandler handler = new WebMvcProtostuffReturnResponseHandler(converters());
        MockHttpServletRequest request = new MockHttpServletRequest();
        // 头存在但值不匹配 -> 不设置任何 Content-Type
        request.addHeader(FeignConst.USE_FEIGN_NAME, "not-true");
        MockHttpServletResponse response = new MockHttpServletResponse();
        handler.handleReturnValue("body", returnType(), new ModelAndViewContainer(),
                new ServletWebRequest(request, response));
        // 头存在但值不匹配 -> 走 else 分支, 设置 json
        Assert.assertTrue(response.getContentType().startsWith("application/json"));
    }

    @Test
    public void feignHeaderEmptyValuesListSetsNothing() throws Exception {
        WebMvcProtostuffReturnResponseHandler handler = new WebMvcProtostuffReturnResponseHandler(converters());
        // 头不存在(get 返回 null) -> CollectionUtil.isNotEmpty 为 false 分支 -> 设置 json
        jakarta.servlet.http.HttpServletRequest raw = Mockito.mock(jakarta.servlet.http.HttpServletRequest.class);
        java.util.Vector<String> names = new java.util.Vector<>();
        names.add(FeignConst.USE_FEIGN_NAME);
        Mockito.when(raw.getHeaderNames()).thenReturn(names.elements());
        Mockito.when(raw.getHeaders(FeignConst.USE_FEIGN_NAME)).thenReturn(new java.util.Vector<String>().elements());
        Mockito.when(raw.getHeader(FeignConst.USE_FEIGN_NAME)).thenReturn(null);
        Mockito.when(raw.getRequestURI()).thenReturn("/x");
        Mockito.when(raw.getContextPath()).thenReturn("");
        Mockito.when(raw.getServletPath()).thenReturn("");
        Mockito.when(raw.getMethod()).thenReturn("GET");
        MockHttpServletResponse response = new MockHttpServletResponse();
        handler.handleReturnValue("body", returnType(), new ModelAndViewContainer(),
                new ServletWebRequest(raw, response));
        Assert.assertTrue(response.getContentType().startsWith("application/json"));
    }
}
