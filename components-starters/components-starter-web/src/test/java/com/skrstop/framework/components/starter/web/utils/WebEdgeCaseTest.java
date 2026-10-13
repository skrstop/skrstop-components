package com.skrstop.framework.components.starter.web.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.InputStream;

public class WebEdgeCaseTest {

    @After
    public void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testDefaultCtors() {
        Assert.assertNotNull(new RequestUtil());
        Assert.assertNotNull(new RequestBeanUtil());
        Assert.assertNotNull(new ReactiveSessionUtil());
        Assert.assertNotNull(new MvcSessionUtil());
    }

    @Test
    public void testRequestUtilBodyIoError() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        try {
            RequestUtil.getRequestBody(request);
        } catch (Exception ignored) {
        }
        jakarta.servlet.ServletInputStream badStream = new jakarta.servlet.ServletInputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("boom");
            }

            @Override
            public boolean isFinished() {return false;}

            @Override
            public boolean isReady() {return false;}

            @Override
            public void setReadListener(jakarta.servlet.ReadListener listener) {}
        };
        MockHttpServletRequest request2 = new MockHttpServletRequest() {
            @Override
            public jakarta.servlet.ServletInputStream getInputStream() {
                return badStream;
            }
        };
        Assert.assertEquals("获取请求body体内容失败", RequestUtil.getRequestBody(request2));
    }

    @Test
    public void testReactiveSessionUtilEmptyCookie() {
        ServerHttpRequest request = Mockito.mock(ServerHttpRequest.class);
        MultiValueMap<String, HttpCookie> cookies = new LinkedMultiValueMap<>();
        cookies.add("c", new HttpCookie("c", "v"));
        cookies.put("empty", new java.util.ArrayList<>());
        Mockito.when(request.getCookies()).thenReturn(cookies);
        Assert.assertEquals("v", ReactiveSessionUtil.getCookie(request, "c"));
        Assert.assertNull(ReactiveSessionUtil.getCookie(request, "empty"));
        Assert.assertNull(ReactiveSessionUtil.getCookie(request, "none"));

        Assert.assertEquals("", ReactiveSessionUtil.encode(""));
        Assert.assertEquals("", ReactiveSessionUtil.encode(null));
        Assert.assertNotNull(ReactiveSessionUtil.encode("a b&c"));
        Assert.assertNotNull(ReactiveSessionUtil.decode("a%20b"));
    }

    @Test
    public void testMvcSessionUtilNullAttributes() {
        RequestContextHolder.resetRequestAttributes();
        Assert.assertNull(MvcSessionUtil.getValueStr("any"));
    }

    @Test
    public void testCookieUtilMaxAgeZero() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));
        CookieUtil.setCookie("/", "n", "v", 0, "example.com");
        Assert.assertNotNull(response.getCookie("n"));
        Assert.assertEquals("example.com", response.getCookie("n").getDomain());
    }

    @Test
    public void testMultipartFileUtilIoError() throws Exception {
        InputStream bad = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("boom");
            }
        };
        // 不应抛异常（内部吞掉 IOException）
        Assert.assertNotNull(MultipartFileUtil.createFileItem(bad, "f.txt"));
    }

    @Test
    public void testRequestContextHolder() {
        com.skrstop.framework.components.starter.web.filter.webflux.ReactiveRequestContextHolder holder = null;
        Assert.assertNull(com.skrstop.framework.components.starter.web.filter.webflux.ReactiveRequestContextHolder.get());
        ServerHttpRequest mock = Mockito.mock(ServerHttpRequest.class);
        com.skrstop.framework.components.starter.web.filter.webflux.ReactiveRequestContextHolder.set(mock);
        Assert.assertSame(mock, com.skrstop.framework.components.starter.web.filter.webflux.ReactiveRequestContextHolder.get());
        com.skrstop.framework.components.starter.web.filter.webflux.ReactiveRequestContextHolder.remove();
        Assert.assertNull(com.skrstop.framework.components.starter.web.filter.webflux.ReactiveRequestContextHolder.get());
        // 默认构造器
        Assert.assertNotNull(new com.skrstop.framework.components.starter.web.filter.webflux.ReactiveRequestContextHolder());
        Assert.assertNotNull(new com.skrstop.framework.components.starter.web.configuration.sentinel.webflux.DefaultWebFluxBlockRequestHandler());
    }
}
