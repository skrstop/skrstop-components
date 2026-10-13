package com.skrstop.framework.components.starter.annotation.exception.code;

import com.skrstop.framework.components.core.common.response.core.IResult;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class WebAnnotationExceptionCodeTest {

    @Test
    public void testCodes() {
        Assert.assertEquals(5, WebAnnotationExceptionCode.values().length);
        for (WebAnnotationExceptionCode code : WebAnnotationExceptionCode.values()) {
            Assert.assertNotNull(code.getCode());
            Assert.assertNotNull(code.getMessage());
            Assert.assertTrue(code instanceof IResult);
        }
        Assert.assertEquals("server.limit.access", WebAnnotationExceptionCode.ACCESS_LIMIT.getCode());
        Assert.assertEquals("server.deprecated.api", WebAnnotationExceptionCode.CONTROLLER_DEPRECATED.getCode());
        Assert.assertEquals("server.limit.intranet", WebAnnotationExceptionCode.INTRANET_LIMIT.getCode());
        Assert.assertEquals("server.limit.privacyInfo", WebAnnotationExceptionCode.PRIVACY_INFO.getCode());
        Assert.assertEquals("server.no.processor", WebAnnotationExceptionCode.NO_PROCESSOR.getCode());
    }

}