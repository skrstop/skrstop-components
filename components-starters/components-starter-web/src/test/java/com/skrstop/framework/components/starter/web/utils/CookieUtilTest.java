package com.skrstop.framework.components.starter.web.utils;

import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.util.enums.CharSetEnum;
import com.skrstop.framework.components.util.enums.ContentTypeEnum;
import com.skrstop.framework.components.util.serialization.json.FastJsonUtil;
import jakarta.servlet.http.Cookie;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

public class CookieUtilTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Before
    public void setup() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));
    }

    @After
    public void cleanup() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testGetCookiesAndGetCookie() {
        request.setCookies(new Cookie("a", "1"), new Cookie("b", "2"));
        Map<String, String> map = CookieUtil.getCookies();
        Assert.assertEquals(2, map.size());
        Assert.assertEquals("1", map.get("a"));
        Assert.assertEquals("2", CookieUtil.getCookie("b").getValue());
        Assert.assertNull(CookieUtil.getCookie("nope"));
        Assert.assertEquals("1", CookieUtil.getValue("a"));
        Assert.assertNull(CookieUtil.getValue("nope"));
    }

    @Test
    public void testGetCookiesEmpty() {
        Assert.assertTrue(CookieUtil.getCookies().isEmpty());
        Assert.assertNull(CookieUtil.getCookie("x"));
        Assert.assertNull(CookieUtil.getValue("x"));
    }

    @Test
    public void testRemoveAndSetCookie() {
        CookieUtil.removeCookie("k1");
        CookieUtil.removeCookie("k2", "/root");
        CookieUtil.setCookie("/p", "k3", "v3", 100);
        CookieUtil.setCookie("/p", "k4", "v4", 100, "example.com");
        CookieUtil.setCookie("/p", "k5", "v5", 0);
        Assert.assertEquals(5, response.getCookies().length);
        Assert.assertEquals(0, response.getCookie("k1").getMaxAge());
        Assert.assertEquals("/root", response.getCookie("k2").getPath());
        Assert.assertEquals("v3", response.getCookie("k3").getValue());
        Assert.assertEquals("example.com", response.getCookie("k4").getDomain());
        Assert.assertFalse(response.getCookie("k5").getMaxAge() > 0);
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(new CookieUtil() {
        });
    }

    @Test
    public void testEnumsString() {
        Assert.assertEquals("application/json; charset=utf-8", ContentTypeEnum.APPLICATION_JSON_UTF8.toString());
        Assert.assertEquals("UTF-8", CharSetEnum.UTF8.toString());
        Assert.assertNotNull(FastJsonUtil.toJson(new SimpleResult()));
    }

    public static class SimpleResult implements IResult {
        private Object code = "0";
        private String message = "ok";

        @Override
        public Object getCode() {
            return code;
        }

        @Override
        public void setCode(Object code) {
            this.code = code;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
