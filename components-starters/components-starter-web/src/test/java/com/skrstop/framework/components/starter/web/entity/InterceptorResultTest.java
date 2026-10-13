package com.skrstop.framework.components.starter.web.entity;

import com.skrstop.framework.components.core.common.response.core.IResult;
import org.junit.Assert;
import org.junit.Test;

public class InterceptorResultTest {

    @Test
    public void testAll() {
        InterceptorResult r = InterceptorResult.builder()
                .next(false)
                .responseStatus(200)
                .result((IResult) null)
                .build();
        Assert.assertEquals(Boolean.FALSE, r.isNext());
        Assert.assertEquals(Integer.valueOf(200), r.getResponseStatus());
        Assert.assertNull(r.getResult());

        r.setNext(true);
        r.setResponseStatus(404);
        Assert.assertTrue(r.isNext());
        Assert.assertEquals(Integer.valueOf(404), r.getResponseStatus());
        Assert.assertNotNull(r.toString());
        Assert.assertNotEquals(0, r.hashCode());
    }
}
