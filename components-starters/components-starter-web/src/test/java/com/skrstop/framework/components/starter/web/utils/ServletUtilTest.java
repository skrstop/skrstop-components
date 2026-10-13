package com.skrstop.framework.components.starter.web.utils;

import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ServletUtilTest {

    static class Dummy {
        public String name;
        public String title;
        private int age;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    @Test
    public void testGetParamsAndParamMap() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setParameter("a", "1");
        req.setParameter("b", new String[]{"x", "y"});
        Map<String, String[]> params = ServletUtil.getParams(req);
        Assert.assertEquals(2, params.size());
        Map<String, String> map = ServletUtil.getParamMap(req);
        Assert.assertEquals("1", map.get("a"));
        Assert.assertEquals("x,y", map.get("b"));
    }

    @Test
    public void testGetBodyAndBytes() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setContent("body".getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals("body", ServletUtil.getBody(req));

        MockHttpServletRequest req2 = new MockHttpServletRequest();
        req2.setContent("body".getBytes(StandardCharsets.UTF_8));
        Assert.assertArrayEquals("body".getBytes(StandardCharsets.UTF_8), ServletUtil.getBodyBytes(req2));
    }

    @Test
    public void testFillBean() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setParameter("name", "n1");
        req.setParameter("title", new String[]{"t1", "t2"});
        req.setParameter("age", "3");
        Dummy d = ServletUtil.fillBean(req, new Dummy(), true);
        Assert.assertEquals("n1", d.name);
        Assert.assertEquals(3, d.getAge());

        Dummy d2 = ServletUtil.toBean(req, Dummy.class, false);
        Assert.assertEquals("n1", d2.name);

        // prefixed param fallback: value provider null branch
        MockHttpServletRequest req2 = new MockHttpServletRequest();
        req2.setParameter("dummy.name", "pfx");
        Dummy d3 = ServletUtil.fillBean(req2, new Dummy(), true);
        Assert.assertEquals("pfx", d3.name);
    }

    @Test
    public void testGetClientIP() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        Assert.assertNotNull(ServletUtil.getClientIP(req));
        req.addHeader("X-Custom-IP", "9.9.9.9");
        Assert.assertEquals("9.9.9.9", ServletUtil.getClientIP(req, "X-Custom-IP"));
    }

    @Test
    public void testGetHeaderMapVariants() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("H1", "v1");
        req.addHeader("H1", "v2");
        Map<String, String> hm = ServletUtil.getHeaderMap(req);
        Assert.assertEquals("v1", hm.get("H1"));
        Map<String, List<String>> hm2 = ServletUtil.getHeadersMap(req);
        Assert.assertEquals(2, hm2.get("H1").size());
        Assert.assertEquals("v1", ServletUtil.getHeaderIgnoreCase(req, "h1"));
        Assert.assertNull(ServletUtil.getHeaderIgnoreCase(req, "nope"));
        Assert.assertNotNull(ServletUtil.getHeader(req, "H1", "UTF-8"));
        Assert.assertNull(ServletUtil.getHeader(req, "nope", "UTF-8"));

        MockHttpServletResponse resp = new MockHttpServletResponse();
        resp.addHeader("RH", "rv");
        Map<String, java.util.Collection<String>> rm = ServletUtil.getHeadersMap(resp);
        Assert.assertEquals(1, rm.size());
    }

    @Test
    public void testIsMethods() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setMethod("GET");
        Assert.assertTrue(ServletUtil.isGetMethod(req));
        Assert.assertFalse(ServletUtil.isPostMethod(req));
        Assert.assertFalse(ServletUtil.isMultipart(req));
        req.setMethod("POST");
        Assert.assertFalse(ServletUtil.isMultipart(req));
        req.setContentType("application/json");
        Assert.assertFalse(ServletUtil.isMultipart(req));
        req.setContentType("multipart/form-data; boundary=x");
        Assert.assertTrue(ServletUtil.isMultipart(req));
        Assert.assertFalse(ServletUtil.isIE(req));
        req.addHeader("User-Agent", "MSIE 10");
        Assert.assertTrue(ServletUtil.isIE(req));
        req.removeHeader("User-Agent");
        req.addHeader("User-Agent", "TRIDENT");
        Assert.assertTrue(ServletUtil.isIE(req));
        req.removeHeader("User-Agent");
        req.addHeader("User-Agent", "Chrome");
        Assert.assertFalse(ServletUtil.isIE(req));
    }

    @Test
    public void testCookies() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setCookies(new Cookie("A", "1"), new Cookie("b", "2"));
        Assert.assertEquals("1", ServletUtil.getCookie(req, "a").getValue());
        Assert.assertNull(ServletUtil.getCookie(req, "nope"));
        Map<String, Cookie> cm = ServletUtil.readCookieMap(req);
        Assert.assertEquals(2, cm.size());
        Assert.assertTrue(ServletUtil.readCookieMap(new MockHttpServletRequest()).isEmpty());

        MockHttpServletResponse resp = new MockHttpServletResponse();
        ServletUtil.addCookie(resp, new Cookie("a", "1"));
        ServletUtil.addCookie(resp, "b", "2");
        ServletUtil.addCookie(resp, "c", "3", 60);
        ServletUtil.addCookie(resp, "d", "4", 60, "/p", "dom");
        Assert.assertEquals(4, resp.getCookies().length);
        Assert.assertEquals("dom", resp.getCookie("d").getDomain());
    }

    @Test
    public void testGetWriter() throws Exception {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        Assert.assertNotNull(ServletUtil.getWriter(resp));
    }

    @Test
    public void testGetMultipartEmptyStreamThrows() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setContentType("multipart/form-data");
        req.setContent(new byte[]{});
        try {
            ServletUtil.getMultipart(req);
            Assert.fail();
        } catch (cn.hutool.core.io.IORuntimeException expected) {
        }
        try {
            ServletUtil.getMultipart(req, new cn.hutool.core.net.multipart.UploadSetting());
            Assert.fail();
        } catch (cn.hutool.core.io.IORuntimeException expected) {
        }
    }

    @Test
    public void testWriteWithIOException() {
        MockHttpServletRequest req = new MockHttpServletRequest() {
            @Override
            public ServletInputStream getInputStream() {
                return new ServletInputStream() {
                    @Override
                    public int read() throws java.io.IOException {
                        throw new java.io.IOException("boom");
                    }

                    @Override
                    public boolean isFinished() {
                        return false;
                    }

                    @Override
                    public boolean isReady() {
                        return false;
                    }

                    @Override
                    public void setReadListener(ReadListener listener) {
                    }
                };
            }
        };
        try {
            ServletUtil.getBodyBytes(req);
            Assert.fail();
        } catch (cn.hutool.core.io.IORuntimeException expected) {
        }
    }

    @Test
    public void testGetMethodConstants() {
        Assert.assertEquals("DELETE", ServletUtil.METHOD_DELETE);
        Assert.assertEquals("HEAD", ServletUtil.METHOD_HEAD);
        Assert.assertEquals("GET", ServletUtil.METHOD_GET);
        Assert.assertEquals("OPTIONS", ServletUtil.METHOD_OPTIONS);
        Assert.assertEquals("POST", ServletUtil.METHOD_POST);
        Assert.assertEquals("PUT", ServletUtil.METHOD_PUT);
        Assert.assertEquals("TRACE", ServletUtil.METHOD_TRACE);
        Assert.assertNotNull(JakartaServletUtil.class);
    }
}
