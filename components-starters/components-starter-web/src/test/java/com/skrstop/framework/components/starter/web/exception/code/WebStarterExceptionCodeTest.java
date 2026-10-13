package com.skrstop.framework.components.starter.web.exception.code;

import org.junit.Assert;
import org.junit.Test;

public class WebStarterExceptionCodeTest {

    @Test
    public void testAll() {
        Assert.assertEquals(8, WebStarterExceptionCode.values().length);
        for (WebStarterExceptionCode code : WebStarterExceptionCode.values()) {
            Assert.assertNotNull(code.getCode());
            Assert.assertNotNull(code.getMessage());
            code.setCode("c");
            code.setMessage("m");
            Assert.assertEquals("c", code.getCode());
            Assert.assertEquals("m", code.getMessage());
        }
        Assert.assertEquals(WebStarterExceptionCode.MATCH_PARAMETER, WebStarterExceptionCode.valueOf("MATCH_PARAMETER"));
    }
}
