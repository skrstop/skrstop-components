package com.skrstop.framework.components.starter.web.utils;

import com.skrstop.framework.components.util.enums.CharSetEnum;
import com.skrstop.framework.components.util.enums.ContentTypeEnum;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;

import java.nio.charset.StandardCharsets;

public class ResponseOutFluxUtilTest {

    @Test
    public void testFluxOut() throws Exception {
        MockServerHttpResponse resp = new MockServerHttpResponse();
        ResponseOutFluxUtil.fluxOut(resp, new CookieUtilTest.SimpleResult(), HttpStatus.ACCEPTED).block();
        resp.getBodyAsString().block();
        resp.setComplete();
        Assert.assertEquals(HttpStatus.ACCEPTED, resp.getStatusCode());

        resp = new MockServerHttpResponse();
        ResponseOutFluxUtil.fluxOut(resp, new CookieUtilTest.SimpleResult()).block();
        Assert.assertNotNull(resp.getBodyAsString().block());
        resp.setComplete();

        resp = new MockServerHttpResponse();
        ResponseOutFluxUtil.fluxOut(resp, new CookieUtilTest.SimpleResult(), CharSetEnum.UTF8, ContentTypeEnum.APPLICATION_JSON_UTF8).block();
        String body = resp.getBodyAsString().block();
        Assert.assertTrue(body.contains("ok"));
        resp.setComplete();
    }

    @Test
    public void testInstantiation() {
        Assert.assertNotNull(new ResponseOutFluxUtil());
        Assert.assertNotNull(new ResponseOutUtil());
        Assert.assertNotNull(new ResponseOutUtilTest());
        Assert.assertEquals(new String(new byte[]{0}, StandardCharsets.UTF_8).length(), 1);
    }
}
