package com.skrstop.framework.components.starter.web.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestUtilTest {

    @After
    public void cleanup() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testRequestParams() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setParameter("a", "1");
        req.setParameter("b", new String[]{"x", "y"});
        Map<String, String> params = RequestUtil.getRequestParams(req);
        Assert.assertEquals("1", params.get("a"));
        Assert.assertEquals("x,y", params.get("b"));
        Assert.assertEquals("1", RequestUtil.getRequestParam(req, "a"));
        Assert.assertNull(RequestUtil.getRequestParam(req, "zz"));
        Assert.assertNull(RequestUtil.getRequestQueryPath(null));
        req.setQueryString("a=1&b=2");
        Assert.assertEquals("a=1&b=2", RequestUtil.getRequestQueryPath(req));
    }

    @Test
    public void testRequestBody() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setContent("hello".getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals("hello", RequestUtil.getRequestBody(req));
    }

    @Test
    public void testRequestContent() {
        Assert.assertNull(RequestUtil.getRequestContent(null));
        Map<String, String> m = new LinkedHashMap<>();
        m.put("b", "2");
        m.put("a", "1");
        Assert.assertEquals("a=1&b=2", RequestUtil.getRequestContent(m));
    }

    @Test
    public void testRequestBeanUtil() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resp = new MockHttpServletResponse();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(req, resp));

        Assert.assertNull(RequestBeanUtil.getBean(null, (Class) null));

        req.setContentType("application/json; charset=utf-8");
        req.setContent("{\"name\":\"jsonName\"}".getBytes(StandardCharsets.UTF_8));
        Map bean = RequestBeanUtil.getBean(new java.util.HashMap(), java.util.HashMap.class);
        Assert.assertEquals("jsonName", bean.get("name"));

        req.setContentType("text/plain");
        Map bean2 = RequestBeanUtil.getBean(new java.util.HashMap(), java.util.HashMap.class);
        Assert.assertTrue(bean2.isEmpty());

        RequestBeanUtil.getBeanWithParamCheck(new java.util.HashMap(), java.util.HashMap.class);
    }
}
