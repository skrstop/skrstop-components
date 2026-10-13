package com.skrstop.framework.components.util.value.data;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author 蒋时华
 */
public class PatternValidUtilTest {

    @Test
    public void testListAllSubStr() {
        Assert.assertEquals(
                Arrays.asList("ab", "ab"),
                PatternValidUtil.listAllSubStr("a+b", "xxab yyab", false));
        Assert.assertEquals(
                Collections.singletonList("ab"),
                PatternValidUtil.listAllSubStr("a+b", "xxab yyab", true));
        Assert.assertTrue(PatternValidUtil.listAllSubStr("a+b", null, true).isEmpty());
        Assert.assertTrue(PatternValidUtil.listAllSubStr("", "abc", true).isEmpty());
        Assert.assertTrue(PatternValidUtil.listAllSubStr(null, "abc", true).isEmpty());
    }

    @Test
    public void testIsMatchCaseInsensitive() {
        Assert.assertTrue(PatternValidUtil.isMatchCaseInsensitive("A+", "aaa"));
        Assert.assertFalse(PatternValidUtil.isMatchCaseInsensitive("A+", "bbb"));
        Assert.assertFalse(PatternValidUtil.isMatchCaseInsensitive("a+", null));
        Assert.assertTrue(PatternValidUtil.isMatchCaseInsensitive("", "anything"));
    }

    @Test
    public void testMatchPhone() {
        Assert.assertTrue(PatternValidUtil.matchPhone("13800138000"));
        Assert.assertFalse(PatternValidUtil.matchPhone("12345"));
    }

    @Test
    public void testMatchEmail() {
        Assert.assertTrue(PatternValidUtil.matchEmail("a@b.com"));
        Assert.assertFalse(PatternValidUtil.matchEmail("not-an-email"));
    }

    @Test
    public void testMatchVehicle() {
        Assert.assertTrue(PatternValidUtil.matchVehicle("京A12345"));
        Assert.assertFalse(PatternValidUtil.matchVehicle("abc"));
    }

    @Test
    public void testMatchDateTime() {
        Assert.assertTrue(PatternValidUtil.matchDateTime("2020-01-02 03:04:05"));
        Assert.assertFalse(PatternValidUtil.matchDateTime("2020-01-02"));
    }

    @Test
    public void testMatchDate() {
        Assert.assertTrue(PatternValidUtil.matchDate("2020-01-02"));
        Assert.assertFalse(PatternValidUtil.matchDate("2020-01-02 03:04:05"));
    }

    @Test
    public void testMatchTime() {
        Assert.assertTrue(PatternValidUtil.matchTime("03:04:05"));
        Assert.assertFalse(PatternValidUtil.matchTime("2020-01-02"));
    }

    @Test
    public void testMatchUrl() {
        Assert.assertTrue(PatternValidUtil.matchHttpUrl("http://a.com/x"));
        Assert.assertTrue(PatternValidUtil.matchHttpUrl("https://a.com/x"));
        Assert.assertFalse(PatternValidUtil.matchHttpUrl("ftp://a.com"));
        Assert.assertTrue(PatternValidUtil.matchFtpUrl("ftp://a.com/x"));
        Assert.assertFalse(PatternValidUtil.matchFtpUrl("http://a.com"));
    }

}
