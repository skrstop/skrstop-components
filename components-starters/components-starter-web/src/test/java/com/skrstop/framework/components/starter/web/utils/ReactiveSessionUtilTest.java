package com.skrstop.framework.components.starter.web.utils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;

import java.math.BigDecimal;

public class ReactiveSessionUtilTest {

    @Test
    public void testEncodeDecode() {
        Assert.assertEquals("", ReactiveSessionUtil.encode(null));
        Assert.assertEquals("", ReactiveSessionUtil.encode(" "));
        String enc = ReactiveSessionUtil.encode("a b&c");
        Assert.assertEquals("a%20b&c", enc);
        Assert.assertEquals("a b&c", ReactiveSessionUtil.decode(enc));
    }

    @Test
    public void testGetCookie() {
        MockServerHttpRequest req = MockServerHttpRequest.get("/").cookie(new org.springframework.http.HttpCookie("a", "1"), new org.springframework.http.HttpCookie("b", "2")).build();
        Assert.assertEquals("1", ReactiveSessionUtil.getCookie(req, "a"));
        Assert.assertNull(ReactiveSessionUtil.getCookie(req, "nope"));
    }

    @Test
    public void testGetValueStrHeaderCookieParam() {
        MockServerHttpRequest req = MockServerHttpRequest.get("/?p=paramValue")
                .header("h", ReactiveSessionUtil.encode("headValue"))
                .cookie(new org.springframework.http.HttpCookie("c", ReactiveSessionUtil.encode("cookieValue")))
                .build();
        Assert.assertEquals("headValue", ReactiveSessionUtil.getValueStr(req, "h"));
        Assert.assertEquals("cookieValue", ReactiveSessionUtil.getValueStr(req, "c"));
        Assert.assertEquals("paramValue", ReactiveSessionUtil.getValueStr(req, "p"));
        Assert.assertNull(ReactiveSessionUtil.getValueStr(req, "nope"));
    }

    @Test
    public void testTypeConverters() {
        MockServerHttpRequest req = MockServerHttpRequest.get("/?v=123&bad=abc").build();
        Assert.assertEquals(Long.valueOf(123L), ReactiveSessionUtil.getValueLong(req, "v"));
        Assert.assertEquals(Integer.valueOf(123), ReactiveSessionUtil.getValueInteger(req, "v"));
        Assert.assertEquals(Byte.valueOf((byte) 123), ReactiveSessionUtil.getValueByte(req, "v"));
        Assert.assertEquals(Short.valueOf((short) 123), ReactiveSessionUtil.getValueShort(req, "v"));
        Assert.assertEquals(Float.valueOf(123f), ReactiveSessionUtil.getValueFloat(req, "v"));
        Assert.assertEquals(Double.valueOf(123d), ReactiveSessionUtil.getValueDouble(req, "v"));
        Assert.assertEquals(new BigDecimal("123"), ReactiveSessionUtil.getValueBigDecimal(req, "v"));
        Assert.assertNull(ReactiveSessionUtil.getValueLong(req, "bad"));
        Assert.assertNull(ReactiveSessionUtil.getValueLong(req, "nope"));
    }
}
