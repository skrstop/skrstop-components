package com.skrstop.framework.components.starter.web.utils;

import jakarta.servlet.http.Cookie;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;

public class WebUtilTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @After
    public void cleanup() {
        RequestContextHolder.resetRequestAttributes();
    }

    private void bind() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));
    }

    static class Dummy {
        @org.springframework.web.bind.annotation.ResponseBody
        public void method() {
        }

        public void plain() {
        }
    }

    @Test
    public void testIsBody() throws Exception {
        Dummy bean = new Dummy();
        HandlerMethod hm = new HandlerMethod(bean, Dummy.class.getMethod("method"));
        Assert.assertTrue(WebUtil.isBody(hm));
        HandlerMethod hm2 = new HandlerMethod(bean, Dummy.class.getMethod("plain"));
        Assert.assertFalse(WebUtil.isBody(hm2));
    }

    @Test
    public void testApplicationContext() {
        WebUtil util = new WebUtil();
        Assert.assertNull(WebUtil.getApplicationContext());
        ApplicationContext ctx = org.mockito.Mockito.mock(ApplicationContext.class);
        util.setApplicationContext(ctx);
        Assert.assertSame(ctx, WebUtil.getApplicationContext());
        util.setApplicationContext(org.mockito.Mockito.mock(ApplicationContext.class));
        Assert.assertSame(ctx, WebUtil.getApplicationContext());
        Environment env = org.mockito.Mockito.mock(Environment.class);
        org.mockito.Mockito.when(ctx.getEnvironment()).thenReturn(env);
        Assert.assertSame(env, WebUtil.getEnvironment());
    }

    @Test
    public void testCookieVal() {
        bind();
        request.setCookies(new Cookie("c", "v"));
        Assert.assertEquals("v", WebUtil.getCookieVal("c"));
        Assert.assertNull(WebUtil.getCookieVal("nope"));
        Assert.assertNull(WebUtil.getCookieVal(request, "nope"));
        Assert.assertEquals("v", WebUtil.getCookieVal(request, "c"));
        WebUtil.removeCookie(response, "k");
        Assert.assertEquals(0, response.getCookie("k").getMaxAge());
        WebUtil.setCookie(response, "n", "val", 60);
        Assert.assertEquals("val", response.getCookie("n").getValue());
        Assert.assertTrue(response.getCookie("n").isHttpOnly());
    }

    @Test
    public void testRenderJson() throws Exception {
        bind();
        WebUtil.renderJson(response, "obj");
        Assert.assertEquals("application/json;charset=UTF-8", response.getContentType());
        Assert.assertEquals("\"obj\"", response.getContentAsString());
    }

    @Test
    public void testGetIP() {
        bind();
        Assert.assertEquals("127.0.0.1", WebUtil.getIP(request));
        Assert.assertEquals("127.0.0.1", WebUtil.getIP());
        request.addHeader("X-Forwarded-For", "1.1.1.1, 2.2.2.2");
        Assert.assertEquals("1.1.1.1", WebUtil.getIP(request));
        request.removeHeader("X-Forwarded-For");
        request.addHeader("X-Forwarded-For", "1.1.1.1");
        Assert.assertEquals("1.1.1.1", WebUtil.getIP(request));
        request.removeHeader("X-Forwarded-For");
        request.addHeader("X-Real-IP", "3.3.3.3");
        Assert.assertEquals("3.3.3.3", WebUtil.getIP(request));
        request.removeHeader("X-Real-IP");
        request.addHeader("Proxy-Client-IP", "4.4.4.4");
        Assert.assertEquals("4.4.4.4", WebUtil.getIP(request));
        request.removeHeader("Proxy-Client-IP");
        request.addHeader("WL-Proxy-Client-IP", "5.5.5.5");
        Assert.assertEquals("5.5.5.5", WebUtil.getIP(request));
        request.removeHeader("WL-Proxy-Client-IP");
        request.addHeader("HTTP_CLIENT_IP", "6.6.6.6");
        Assert.assertEquals("6.6.6.6", WebUtil.getIP(request));
        request.removeHeader("HTTP_CLIENT_IP");
        request.addHeader("HTTP_X_FORWARDED_FOR", "7.7.7.7");
        Assert.assertEquals("7.7.7.7", WebUtil.getIP(request));
    }

    @Test
    public void testRequestStrAndBytes() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setQueryString("a=1&amp;b=%22x%22");
        Assert.assertEquals("a=1&b=\"x\"", WebUtil.getRequestStr(req));
        Assert.assertEquals("a=1&b=\"x\"", WebUtil.getRequestParamString(req));

        MockHttpServletRequest req2 = new MockHttpServletRequest();
        req2.setContentType("text/plain");
        req2.setCharacterEncoding("UTF-8");
        req2.setContent("hello".getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals("hello", WebUtil.getRequestStr(req2));

        MockHttpServletRequest req3 = new MockHttpServletRequest();
        req3.setContent("".getBytes());
        req3.setParameter("p", "v");
        String str3 = WebUtil.getRequestStr(req3, WebUtil.getRequestBytes(req3));
        Assert.assertEquals("p=v", str3);

        MockHttpServletRequest req4 = new MockHttpServletRequest();
        req4.setContent("".getBytes());
        Assert.assertEquals("", WebUtil.getRequestStr(req4, WebUtil.getRequestBytes(req4)));

        Assert.assertNull(WebUtil.getRequestBytes(new MockHttpServletRequest()));
    }

    @Test
    public void testGetRequestParamStringFallback() {
        bind();
        request.setAttribute("x", null);
        // queryString null, content length -1 → empty body; returns ""
        Assert.assertEquals("", WebUtil.getRequestParamString(request));
    }

    @Test
    public void testGetRequestArgs() {
        Object[] empty = {};
        Assert.assertEquals(empty, WebUtil.getRequestArgs(empty));
        Object[] serializable = {new Serializable() {
        }};
        Assert.assertEquals(serializable[0], WebUtil.getRequestArgs(serializable));
        Object[] multi = {new Serializable() {
        }, new Serializable() {
        }};
        Assert.assertArrayEquals(multi, (Object[]) WebUtil.getRequestArgs(multi));
        Object[] filtered = {"s", new Object()};
        Object result = WebUtil.getRequestArgs(filtered);
        Assert.assertEquals("s", ((Object[]) result)[0]);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setParameter("a", "b");
        Object r2 = WebUtil.getRequestArgs(new Object[]{req});
        Assert.assertTrue(r2 instanceof Object[]);
        jakarta.servlet.http.HttpServletRequest serializableReq =
                org.mockito.Mockito.mock(jakarta.servlet.http.HttpServletRequest.class,
                        org.mockito.Mockito.withSettings().serializable());
        org.mockito.Mockito.when(serializableReq.getParameterMap()).thenReturn(java.util.Collections.singletonMap("a", new String[]{"b"}));
        Object r3 = WebUtil.getRequestArgs(new Object[]{new Object(), serializableReq});
        Assert.assertTrue(r3 instanceof Map);
    }

    @Test
    public void testWriteFileAndStreams() throws Exception {
        bind();
        File dir = new File("target");
        File file = new File(dir, "webutil-test-file.txt");
        Files.write(file.toPath(), "content".getBytes(StandardCharsets.UTF_8));
        ServletUtil.write(response, file);
        Assert.assertTrue(response.getHeader("Content-Disposition").contains("attachment"));

        response = new MockHttpServletResponse();
        ServletUtil.write(response, "hello", "text/plain");
        Assert.assertEquals("hello", response.getContentAsString());

        response = new MockHttpServletResponse();
        ServletUtil.write(response, new FileInputStream(file), "application/octet-stream");
        Assert.assertEquals(7, response.getContentAsByteArray().length);

        response = new MockHttpServletResponse();
        ServletUtil.write(response, new FileInputStream(file), "application/octet-stream", "file.txt");
        Assert.assertEquals(7, response.getContentAsByteArray().length);

        response = new MockHttpServletResponse();
        ServletUtil.write(response, new FileInputStream(file));
        Assert.assertEquals(7, response.getContentAsByteArray().length);
        file.delete();
    }

    @Test
    public void testSetHeader() {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        ServletUtil.setHeader(resp, "h1", "str");
        ServletUtil.setHeader(resp, "h2", 5);
        ServletUtil.setHeader(resp, "h3", new java.util.Date(1000L));
        ServletUtil.setHeader(resp, "h4", new StringBuilder("sb"));
        Assert.assertEquals("str", resp.getHeader("h1"));
        Assert.assertEquals("5", resp.getHeader("h2"));
        Assert.assertEquals("sb", resp.getHeader("h4"));
    }

    @Test
    public void testConstants() {
        Assert.assertEquals("user-agent", WebUtil.USER_AGENT_HEADER);
        Assert.assertEquals("unknown", WebUtil.UN_KNOWN);
        Assert.assertNotNull(Arrays.asList(ServletUtil.METHOD_GET));
    }
}
