package com.skrstop.framework.components.util.value.data;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class BooleanUtilTest {

    @Test
    public void testToBooleanString() {
        Assert.assertTrue(BooleanUtil.toBoolean("true", false));
        Assert.assertTrue(BooleanUtil.toBoolean("TRUE", false));
        Assert.assertTrue(BooleanUtil.toBoolean("xxx", true));
        Assert.assertFalse(BooleanUtil.toBoolean("false", true));
        Assert.assertFalse(BooleanUtil.toBoolean("FALSE", true));
        Assert.assertFalse(BooleanUtil.toBoolean("xxx", false));
        Assert.assertFalse(BooleanUtil.toBoolean(null, false));
    }

    @Test
    public void testToBooleanObject() {
        Assert.assertFalse(BooleanUtil.toBoolean((Object) null, false));
        Assert.assertTrue(BooleanUtil.toBoolean((Object) null, true));
        Assert.assertTrue(BooleanUtil.toBoolean(Boolean.TRUE, false));
        Assert.assertFalse(BooleanUtil.toBoolean(Boolean.FALSE, true));
        Assert.assertTrue(BooleanUtil.toBoolean((Object) 1, false));
        Assert.assertFalse(BooleanUtil.toBoolean((Object) 0, true));
        Assert.assertTrue(BooleanUtil.toBoolean((Object) "true", false));
        Assert.assertFalse(BooleanUtil.toBoolean((Object) "false", true));
    }

    @Test
    public void testToBooleanNumberAndDefault() {
        Assert.assertTrue(BooleanUtil.toBoolean((Object) 1, false));
        Assert.assertFalse(BooleanUtil.toBoolean((Object) 2L, true));
        Assert.assertTrue(BooleanUtil.toBoolean(new java.math.BigDecimal(1), false));
        Assert.assertEquals(Boolean.TRUE, BooleanUtil.toBoolean(Boolean.TRUE, false));
        // 非 true/false 字符串走默认值
        Assert.assertTrue(BooleanUtil.toBoolean((Object) "xxx", true));
        Assert.assertFalse(BooleanUtil.toBoolean((Object) "xxx", false));
    }

}
