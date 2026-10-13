package com.skrstop.framework.components.starter.web.constant;

import org.junit.Assert;
import org.junit.Test;

public class ConstantsTest {

    @Test
    public void testGlobalConfigConst() {
        Assert.assertEquals("skrstop.response.config", GlobalConfigConst.RESPONSE_PREFIX);
        Assert.assertEquals("skrstop.exception.config", GlobalConfigConst.EXCEPTION_PREFIX);
    }

    @Test
    public void testRequestConst() {
        Assert.assertEquals("body_cache", RequestConst.BODY_CACHE);
        Assert.assertEquals("body_cache_name", RequestConst.BODY_CACHE_NAME);
    }

    @Test
    public void testRequestPathConst() {
        Assert.assertEquals("/actuator", RequestPathConst.ACTUATOR_PATH_1);
        Assert.assertEquals("metrics", RequestPathConst.METRICS_PATH_1);
        Assert.assertEquals("druid", RequestPathConst.DRUID_PATH_1);
        Assert.assertEquals("/v3/api-docs", RequestPathConst.API_DOC);
        Assert.assertNotNull(RequestPathConst.DEFAULT_NOT_GLOBAL_RESPONSE_RESULT_LIST);
        Assert.assertEquals(13, RequestPathConst.DEFAULT_NOT_GLOBAL_RESPONSE_RESULT_LIST.size());
    }
}
