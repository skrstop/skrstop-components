package com.skrstop.framework.components.starter.annotation.constant;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class SimpleConstantAndEnumTest {

    @Test
    public void testGlobalConfigConst() {
        Assert.assertEquals("skrstop.annotation.config", GlobalConfigConst.ANNOTATION_PREFIX);
    }

    @Test
    public void testPrivacyInfoType() {
        Assert.assertEquals("DEFAULT", PrivacyInfoType.DEFAULT);
        Assert.assertEquals("SET_NULL", PrivacyInfoType.SET_NULL);
        Assert.assertEquals("DEFAULT_BANK_CARD", PrivacyInfoType.DEFAULT_BANK_CARD);
        Assert.assertEquals("DEFAULT_ID_CARD", PrivacyInfoType.DEFAULT_ID_CARD);
        Assert.assertEquals("DEFAULT_PHONE", PrivacyInfoType.DEFAULT_PHONE);
        Assert.assertEquals("DEFAULT_EMAIL", PrivacyInfoType.DEFAULT_EMAIL);
    }

    @Test
    public void testTrimmerType() {
        Assert.assertEquals(4, com.skrstop.framework.components.starter.annotation.handle.trim.TrimmerType.values().length);
        Assert.assertEquals(com.skrstop.framework.components.starter.annotation.handle.trim.TrimmerType.ALL,
                com.skrstop.framework.components.starter.annotation.handle.trim.TrimmerType.valueOf("ALL"));
    }

}