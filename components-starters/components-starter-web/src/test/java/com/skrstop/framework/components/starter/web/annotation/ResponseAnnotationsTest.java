package com.skrstop.framework.components.starter.web.annotation;

import com.skrstop.framework.components.starter.web.response.DisableGlobalResponse;
import com.skrstop.framework.components.starter.web.response.DisableTransResultTypeResponse;
import org.junit.Assert;
import org.junit.Test;

public class ResponseAnnotationsTest {

    @DisableGlobalResponse
    @DisableTransResultTypeResponse
    public void dummy() {
    }

    @Test
    public void testAnnotations() throws Exception {
        Assert.assertNotNull(getClass().getMethod("dummy").getAnnotation(DisableGlobalResponse.class));
        Assert.assertNotNull(getClass().getMethod("dummy").getAnnotation(DisableTransResultTypeResponse.class));
        Assert.assertEquals("disableglobalresponse", DisableGlobalResponse.class.getSimpleName().toLowerCase());
        Assert.assertNotNull(DisableGlobalResponse.class.getAnnotation(java.lang.annotation.Retention.class));
        Assert.assertNotNull(DisableTransResultTypeResponse.class.getAnnotation(java.lang.annotation.Retention.class));
    }
}
