package com.skrstop.framework.components.starter.database.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class SuperParamsUtilTest {

    @Test
    public void getParamMap() {
        Map<String, String> params = SuperParamsUtil.getParamMap(
                "a=#{entity.createBy},b=#{entity.updateBy.updateTime},c=#{x}");
        Assert.assertEquals("createBy", params.get("a"));
        Assert.assertEquals("updateTime", params.get("b"));
        Assert.assertEquals("x", params.get("c"));
    }

    @Test
    public void constructorNotInstantiable() {
        try {
            java.lang.reflect.Constructor<SuperParamsUtil> ctor = SuperParamsUtil.class.getDeclaredConstructor();
            ctor.setAccessible(true);
            ctor.newInstance();
            Assert.fail("should throw");
        } catch (java.lang.reflect.InvocationTargetException e) {
            Assert.assertTrue(e.getCause() instanceof IllegalStateException);
        } catch (Exception e) {
            Assert.fail("unexpected: " + e);
        }
    }
}
