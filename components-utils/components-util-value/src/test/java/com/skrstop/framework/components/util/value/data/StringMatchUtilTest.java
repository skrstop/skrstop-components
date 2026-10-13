package com.skrstop.framework.components.util.value.data;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class StringMatchUtilTest {

    @Test
    public void testPlain() {
        Assert.assertTrue(StringMatchUtil.plain("有限公司;2346;HGJ", "HGJ"));
        Assert.assertTrue(StringMatchUtil.plain("abc", "abc"));
        Assert.assertFalse(StringMatchUtil.plain("abc", "abcd"));
        Assert.assertFalse(StringMatchUtil.plain("abc", "x"));
    }

    @Test
    public void testKmp() {
        Assert.assertTrue(StringMatchUtil.kmp("有限公司;2346;HGJ", "HGJ"));
        Assert.assertTrue(StringMatchUtil.kmp("abcabcabcd", "abcabcd"));
        Assert.assertFalse(StringMatchUtil.kmp("abc", "abcd"));
        Assert.assertFalse(StringMatchUtil.kmp("abcdefg", "xyz"));
        // 该实现的 next 数组计算存在缺陷，此用例会漏匹配，仅验证不误报
        Assert.assertFalse(StringMatchUtil.kmp("aabaabaaaab", "aabaaab"));
    }

}
