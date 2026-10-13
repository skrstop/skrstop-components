package com.skrstop.framework.components.util.value.url;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author 蒋时华
 */
public class UrlFilterUtilTest {

    @Test
    public void testValidSingle() {
        Assert.assertTrue(UrlFilterUtil.valid("**", "/any/path"));
        // **xxx
        Assert.assertTrue(UrlFilterUtil.valid("**/end", "/a/b/end"));
        Assert.assertFalse(UrlFilterUtil.valid("**/end", "/a/end/x"));
        // xxx**
        Assert.assertTrue(UrlFilterUtil.valid("/start/**", "/start/a/b"));
        Assert.assertFalse(UrlFilterUtil.valid("/start/**", "/x/start/a"));
        // **xxx**
        Assert.assertTrue(UrlFilterUtil.valid("**middle**", "/a/middle/b"));
        Assert.assertFalse(UrlFilterUtil.valid("**middle**", "/a/midxdle/b"));
        // 完全相等
        Assert.assertTrue(UrlFilterUtil.valid("/exact", "/exact"));
        Assert.assertFalse(UrlFilterUtil.valid("/exact", "/other"));
    }

    @Test
    public void testValidCollection() {
        Assert.assertFalse(UrlFilterUtil.valid((java.util.Collection<String>) null, "/path"));
        Assert.assertFalse(UrlFilterUtil.valid(Collections.emptyList(), "/path"));
        Assert.assertFalse(UrlFilterUtil.valid(Arrays.asList("**"), ""));
        Assert.assertTrue(UrlFilterUtil.valid(Arrays.asList("/x", "**"), "/path"));
        Assert.assertFalse(UrlFilterUtil.valid(Arrays.asList("/x", "/y"), "/path"));
    }

    @Test
    public void testBothPattern() {
        // **xxx** 分支：startWith 且 endWith
        Assert.assertTrue(UrlFilterUtil.valid("**b**", "abcba"));
        Assert.assertTrue(UrlFilterUtil.valid("**a**", "xay"));
    }

}
