package com.skrstop.framework.components.util.value.data;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * @author 蒋时华
 */
public class EnumUtilTest {

    private enum TestEnum {
        A, B, C
    }

    @Test
    public void testGetInstance() {
        Assert.assertEquals(Optional.of(TestEnum.A), EnumUtil.getInstance("A", TestEnum.class));
        Assert.assertEquals(Optional.empty(), EnumUtil.getInstance("D", TestEnum.class));
        Assert.assertEquals(Optional.empty(), EnumUtil.getInstance("", TestEnum.class));
        Assert.assertEquals(Optional.empty(), EnumUtil.getInstance(null, TestEnum.class));
    }

    @Test
    public void testGetInstanceQuirk() {
        // 源码条件为 ||，双 null 时条件为 false，直接返回 empty
        Assert.assertEquals(Optional.empty(), EnumUtil.getInstance(null, null));
    }

}
