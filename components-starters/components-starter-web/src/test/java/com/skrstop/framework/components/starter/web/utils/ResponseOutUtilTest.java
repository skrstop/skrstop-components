package com.skrstop.framework.components.starter.web.utils;

import com.skrstop.framework.components.util.enums.CharSetEnum;
import com.skrstop.framework.components.util.enums.ContentTypeEnum;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

public class ResponseOutUtilTest extends CookieUtilTest {

    @Test
    public void testOut() throws Exception {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        ResponseOutUtil.out(resp, new SimpleResult(), HttpStatus.BAD_REQUEST);
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), resp.getStatus());
        Assert.assertEquals("application/json; charset=utf-8", resp.getContentType());
        Assert.assertEquals("UTF-8", resp.getCharacterEncoding());
        Assert.assertTrue(resp.getContentAsString().contains("\"message\":\"ok\""));

        resp = new MockHttpServletResponse();
        ResponseOutUtil.out(resp, new SimpleResult());
        Assert.assertEquals(HttpStatus.OK.value(), resp.getStatus());

        resp = new MockHttpServletResponse();
        ResponseOutUtil.out(resp, new SimpleResult(), CharSetEnum.UTF8, ContentTypeEnum.APPLICATION_JSON_UTF8, HttpStatus.OK);
        Assert.assertTrue(resp.getContentAsString().contains("ok"));
    }
}
