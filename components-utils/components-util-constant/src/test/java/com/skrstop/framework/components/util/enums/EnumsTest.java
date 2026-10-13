package com.skrstop.framework.components.util.enums;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class EnumsTest {

    @Test
    public void testContentTypeEnum() {
        Assert.assertEquals("*/*", ContentTypeEnum.ALL.toString());
        Assert.assertEquals("application/json", ContentTypeEnum.APPLICATION_JSON.toString());
        for (ContentTypeEnum value : ContentTypeEnum.values()) {
            Assert.assertNotNull(value.toString());
        }
    }

    @Test
    public void testCharSetEnum() {
        for (CharSetEnum value : CharSetEnum.values()) {
            Assert.assertNotNull(value.toString());
        }
    }

    @Test
    public void testHttpMethodEnum() {
        Assert.assertNotNull(HttpMethodEnum.valueOf("GET"));
        for (HttpMethodEnum value : HttpMethodEnum.values()) {
            Assert.assertNotNull(value.toString());
        }
    }

}
