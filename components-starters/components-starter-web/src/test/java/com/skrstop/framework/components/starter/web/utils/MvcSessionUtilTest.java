package com.skrstop.framework.components.starter.web.utils;

import jakarta.servlet.http.Cookie;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class MvcSessionUtilTest {

    private MockHttpServletRequest request;

    @After
    public void cleanup() {
        RequestContextHolder.resetRequestAttributes();
    }

    private void bind() {
        request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, new MockHttpServletResponse()));
    }

    @Test
    public void testEncodeDecode() {
        Assert.assertEquals("", MvcSessionUtil.encode(null));
        Assert.assertEquals("", MvcSessionUtil.encode(" "));
        String enc = MvcSessionUtil.encode("a b&c");
        Assert.assertEquals("a%20b&c", enc);
        Assert.assertEquals("a b&c", MvcSessionUtil.decode(enc));
    }

    @Test
    public void testGetValueStrNoContext() {
        Assert.assertNull(MvcSessionUtil.getValueStr("x"));
    }

    @Test
    public void testGetValueStrAttributeAndHeader() {
        bind();
        request.setAttribute("k", "attrValue");
        Assert.assertEquals("attrValue", MvcSessionUtil.getValueStr("k"));

        MockHttpServletRequest req2 = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(req2, new MockHttpServletResponse()));
        req2.addHeader("h", MvcSessionUtil.encode("headValue"));
        Assert.assertEquals("headValue", MvcSessionUtil.getValueStr("h"));
    }

    @Test
    public void testGetValueStrCookieAndParam() {
        bind();
        request.setCookies(new Cookie("c", MvcSessionUtil.encode("cookieValue")));
        Assert.assertEquals("cookieValue", MvcSessionUtil.getValueStr("c"));

        bind();
        request.setParameter("p", "plainValue");
        Assert.assertEquals("plainValue", MvcSessionUtil.getValueStr("p"));

        bind();
        Assert.assertNull(MvcSessionUtil.getValueStr("nothing"));
    }

    @Test
    public void testTypeConverters() {
        bind();
        request.setParameter("v", "123");
        Assert.assertEquals(Long.valueOf(123L), MvcSessionUtil.getValueLong("v"));
        Assert.assertEquals(Integer.valueOf(123), MvcSessionUtil.getValueInteger("v"));
        Assert.assertEquals(Byte.valueOf((byte) 123), MvcSessionUtil.getValueByte("v"));
        Assert.assertEquals(Short.valueOf((short) 123), MvcSessionUtil.getValueShort("v"));
        Assert.assertEquals(Float.valueOf(123f), MvcSessionUtil.getValueFloat("v"));
        Assert.assertEquals(Double.valueOf(123d), MvcSessionUtil.getValueDouble("v"));
        Assert.assertEquals(new java.math.BigDecimal("123"), MvcSessionUtil.getValueBigDecimal("v"));
        request.setParameter("bad", "abc");
        Assert.assertNull(MvcSessionUtil.getValueLong("bad"));
    }

    @Test
    public void testGetCookie() {
        bind();
        request.setCookies(new Cookie("ck", "cv"));
        Assert.assertEquals("cv", MvcSessionUtil.getCookie("ck"));
    }
}
