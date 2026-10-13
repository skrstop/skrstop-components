package com.skrstop.framework.components.starter.annotation.anno.server.entity;

import org.junit.Assert;
import org.junit.Test;

public class ObjectScopeTest {

    @Test
    public void testValues() {
        ObjectScope[] values = ObjectScope.values();
        Assert.assertEquals(5, values.length);
        Assert.assertEquals(ObjectScope.NOMAL, ObjectScope.valueOf("NOMAL"));
        Assert.assertEquals(ObjectScope.REQUEST, ObjectScope.valueOf("REQUEST"));
        Assert.assertEquals(ObjectScope.RESPONSE, ObjectScope.valueOf("RESPONSE"));
        Assert.assertEquals(ObjectScope.CONVERSION, ObjectScope.valueOf("CONVERSION"));
        Assert.assertEquals(ObjectScope.TEMP, ObjectScope.valueOf("TEMP"));
    }
}
