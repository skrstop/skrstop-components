package com.skrstop.framework.components.util.value.data;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class StringLengthUtilTest {

    @Test
    public void testEqNeq() {
        Assert.assertTrue(StringLengthUtil.eq("abc", 3L));
        Assert.assertFalse(StringLengthUtil.eq("abc", 4L));
        Assert.assertFalse(StringLengthUtil.neq("abc", 3L));
        Assert.assertTrue(StringLengthUtil.neq("abc", 4L));
        // 中文按字节（UTF-8 3 字节）
        Assert.assertTrue(StringLengthUtil.eq("中", 3L, "UTF-8"));
        Assert.assertFalse(StringLengthUtil.eq("中", 3L, "GBK"));
        Assert.assertTrue(StringLengthUtil.neq("中", 3L, "GBK"));
        Assert.assertFalse(StringLengthUtil.neq("中", 2L, "GBK"));
    }

    @Test
    public void testLtEltGtEgt() {
        Assert.assertTrue(StringLengthUtil.lt("ab", 3L));
        Assert.assertFalse(StringLengthUtil.lt("abc", 3L));
        Assert.assertTrue(StringLengthUtil.elt("abc", 3L));
        Assert.assertFalse(StringLengthUtil.elt("abcd", 3L));
        Assert.assertTrue(StringLengthUtil.gt("abcd", 3L));
        Assert.assertFalse(StringLengthUtil.gt("abc", 3L));
        Assert.assertTrue(StringLengthUtil.egt("abc", 3L));
        Assert.assertFalse(StringLengthUtil.egt("ab", 3L));
        // 字节模式
        Assert.assertTrue(StringLengthUtil.lt("中", 4L, "UTF-8"));
        Assert.assertFalse(StringLengthUtil.lt("中", 3L, "UTF-8"));
        Assert.assertTrue(StringLengthUtil.elt("中", 3L, "UTF-8"));
        Assert.assertTrue(StringLengthUtil.gt("中", 2L, "UTF-8"));
        Assert.assertFalse(StringLengthUtil.gt("中", 3L, "UTF-8"));
        Assert.assertTrue(StringLengthUtil.egt("中", 3L, "UTF-8"));
        Assert.assertFalse(StringLengthUtil.egt("中", 4L, "UTF-8"));
    }

    @Test
    public void testRanges() {
        // 注意：源码命名与实现相反 —— gtAndlt 实际为闭区间 [min,max]，egtAndelt 为开区间 (min,max)
        Assert.assertTrue(StringLengthUtil.gtAndlt("bc", 1L, 4L));
        Assert.assertTrue(StringLengthUtil.gtAndlt("abc", 1L, 3L));
        Assert.assertFalse(StringLengthUtil.gtAndlt("abcd", 1L, 3L));
        Assert.assertFalse(StringLengthUtil.egtAndelt("abc", 3L, 3L));
        Assert.assertTrue(StringLengthUtil.egtAndelt("bc", 1L, 4L));
        Assert.assertFalse(StringLengthUtil.egtAndelt("abcd", 3L, 3L));
        Assert.assertTrue(StringLengthUtil.gtAndlt("中", 1L, 4L, "UTF-8"));
        Assert.assertTrue(StringLengthUtil.gtAndlt("中", 1L, 3L, "UTF-8"));
        Assert.assertFalse(StringLengthUtil.gtAndlt("中", 4L, 5L, "UTF-8"));
        Assert.assertFalse(StringLengthUtil.egtAndelt("中", 3L, 3L, "UTF-8"));
        Assert.assertTrue(StringLengthUtil.egtAndelt("中", 2L, 4L, "UTF-8"));
        Assert.assertFalse(StringLengthUtil.egtAndelt("中", 3L, 2L, "UTF-8"));
    }

    @Test
    public void testNullAndCharsetEdge() {
        // null 输入各分支
        Assert.assertFalse(StringLengthUtil.eq(null, 3L, "UTF-8"));
        Assert.assertFalse(StringLengthUtil.neq(null, 3L, "UTF-8"));
        Assert.assertFalse(StringLengthUtil.lt(null, 3L, "UTF-8"));
        Assert.assertFalse(StringLengthUtil.elt(null, 3L, "UTF-8"));
        Assert.assertFalse(StringLengthUtil.gt(null, 3L, "UTF-8"));
        Assert.assertFalse(StringLengthUtil.egt(null, 3L, "UTF-8"));
        Assert.assertFalse(StringLengthUtil.gtAndlt(null, 1L, 3L, "UTF-8"));
        Assert.assertFalse(StringLengthUtil.egtAndelt(null, 1L, 3L, "UTF-8"));
        // 非法 charset 走 EncodingException
        try {
            StringLengthUtil.eq("abc", 3L, "NO_SUCH_CHARSET");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        for (Runnable r : new Runnable[]{
                () -> StringLengthUtil.neq("a", 1L, "BAD"),
                () -> StringLengthUtil.lt("a", 1L, "BAD"),
                () -> StringLengthUtil.elt("a", 1L, "BAD"),
                () -> StringLengthUtil.gt("a", 1L, "BAD"),
                () -> StringLengthUtil.egt("a", 1L, "BAD"),
                () -> StringLengthUtil.gtAndlt("a", 1L, 2L, "BAD"),
                () -> StringLengthUtil.egtAndelt("a", 1L, 2L, "BAD")}) {
            try {
                r.run();
                Assert.fail();
            } catch (RuntimeException e) {
                // EncodingException
            }
        }
    }

}
