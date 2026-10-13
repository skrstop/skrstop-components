package com.skrstop.framework.components.starter.web.configuration;

import org.junit.Assert;
import org.junit.Test;

public class GlobalExceptionPropertiesTest {

    @Test
    public void test() {
        GlobalExceptionProperties p = new GlobalExceptionProperties();
        Assert.assertTrue(p.isEnable());
        Assert.assertFalse(p.isHasHtmlError());
        Assert.assertFalse(p.isLogBusinessServiceException());
        Assert.assertFalse(p.isAlwaysReturnHttpOk());
        p.setEnable(false);
        p.setHasHtmlError(true);
        p.setLogBusinessServiceException(true);
        p.setAlwaysReturnHttpOk(true);
        Assert.assertFalse(p.isEnable());
        Assert.assertTrue(p.isHasHtmlError());
        Assert.assertTrue(p.isLogBusinessServiceException());
        Assert.assertTrue(p.isAlwaysReturnHttpOk());
    }
}
