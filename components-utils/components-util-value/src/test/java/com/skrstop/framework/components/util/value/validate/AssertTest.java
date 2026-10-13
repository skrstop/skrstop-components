package com.skrstop.framework.components.util.value.validate;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 蒋时华
 */
public class AssertTest {

    @Test
    public void testTrueFalse() {
        com.skrstop.framework.components.util.value.validate.Assert.isTrue(true);
        com.skrstop.framework.components.util.value.validate.Assert.isTrue(true, "msg");
        com.skrstop.framework.components.util.value.validate.Assert.isTrue(true, "msg {}", "p");
        try {
            com.skrstop.framework.components.util.value.validate.Assert.isTrue(false, "e{}", 1);
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("e1", e.getMessage());
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.isTrue(false);
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().startsWith("[Assertion failed]"));
        }
        com.skrstop.framework.components.util.value.validate.Assert.isFalse(false);
        com.skrstop.framework.components.util.value.validate.Assert.isFalse(false, "m");
        try {
            com.skrstop.framework.components.util.value.validate.Assert.isFalse(true, "f");
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("f", e.getMessage());
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.isFalse(true);
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
    }

    @Test
    public void testNullNotNull() {
        com.skrstop.framework.components.util.value.validate.Assert.isNull(null);
        com.skrstop.framework.components.util.value.validate.Assert.isNull(null, "m");
        try {
            com.skrstop.framework.components.util.value.validate.Assert.isNull("x", "not null");
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("not null", e.getMessage());
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.isNull("x");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        com.skrstop.framework.components.util.value.validate.Assert.notNull("x");
        try {
            com.skrstop.framework.components.util.value.validate.Assert.notNull(null);
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.notNull(null, "nn");
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("nn", e.getMessage());
        }
    }

    @Test
    public void testStringAsserts() {
        Assert.assertEquals("t", com.skrstop.framework.components.util.value.validate.Assert.notEmpty("t"));
        Assert.assertEquals("t", com.skrstop.framework.components.util.value.validate.Assert.notEmpty("t", "m"));
        try {
            com.skrstop.framework.components.util.value.validate.Assert.notEmpty("", "empty!");
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("empty!", e.getMessage());
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.notEmpty("");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        Assert.assertEquals("b", com.skrstop.framework.components.util.value.validate.Assert.notBlank("b"));
        Assert.assertEquals("b", com.skrstop.framework.components.util.value.validate.Assert.notBlank("b", "m"));
        try {
            com.skrstop.framework.components.util.value.validate.Assert.notBlank("  ", "blank!");
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("blank!", e.getMessage());
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.notBlank(" ");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        Assert.assertEquals("xy", com.skrstop.framework.components.util.value.validate.Assert.notContain("abc", "xy"));
        Assert.assertEquals("xy", com.skrstop.framework.components.util.value.validate.Assert.notContain("abc", "xy", "m"));
        try {
            com.skrstop.framework.components.util.value.validate.Assert.notContain("abc", "b");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        // list contain
        com.skrstop.framework.components.util.value.validate.Assert.contain("a", Arrays.asList("a", "b"), "err");
        try {
            com.skrstop.framework.components.util.value.validate.Assert.contain("c", Arrays.asList("a", "b"), "no contain");
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("no contain", e.getMessage());
        }
        com.skrstop.framework.components.util.value.validate.Assert.notContain("c", Arrays.asList("a", "b"), "err");
        // notContain 条件短路分支
        com.skrstop.framework.components.util.value.validate.Assert.notContain("", "b", "m");
        com.skrstop.framework.components.util.value.validate.Assert.notContain("abc", "", "m");
        try {
            com.skrstop.framework.components.util.value.validate.Assert.notContain(null, "b");
        } catch (RuntimeException ignored) {
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.notContain("a", Arrays.asList("a", "b"), "has");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
    }

    @Test
    public void testConstructAndOneArgSuccess() {
        // 覆盖默认构造器与 1 参成功路径
        new com.skrstop.framework.components.util.value.validate.Assert();
        Assert.assertNotNull(com.skrstop.framework.components.util.value.validate.Assert.notEmpty(new Object[]{"a"}));
        Assert.assertNotNull(com.skrstop.framework.components.util.value.validate.Assert.noNullElements(new String[]{"a"}));
        Assert.assertNotNull(com.skrstop.framework.components.util.value.validate.Assert.notEmpty(Arrays.asList("a")));
        java.util.Map<String, String> m = new HashMap<>();
        m.put("k", "v");
        Assert.assertNotNull(com.skrstop.framework.components.util.value.validate.Assert.notEmpty(m));
    }

    @Test
    public void testUrlAsserts() {
        com.skrstop.framework.components.util.value.validate.Assert.httpUrl("http://a.com/x?y=1");
        try {
            com.skrstop.framework.components.util.value.validate.Assert.httpUrl("ftp://a.com");
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("不规范的http url地址", e.getMessage());
        }
        com.skrstop.framework.components.util.value.validate.Assert.ftpUrl("ftp://a.com/x");
        try {
            com.skrstop.framework.components.util.value.validate.Assert.ftpUrl("http://a.com");
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("不规范的ftp url地址", e.getMessage());
        }
    }

    @Test
    public void testCollectionArrayAsserts() {
        Object[] arr = com.skrstop.framework.components.util.value.validate.Assert.notEmpty(new Object[]{"a"}, "m");
        Assert.assertEquals(1, arr.length);
        try {
            com.skrstop.framework.components.util.value.validate.Assert.notEmpty(new Object[0]);
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.notEmpty((Object[]) null);
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        String[] noNull = com.skrstop.framework.components.util.value.validate.Assert.noNullElements(new String[]{"a", "b"}, "m");
        Assert.assertEquals(2, noNull.length);
        try {
            com.skrstop.framework.components.util.value.validate.Assert.noNullElements(new String[]{"a", null}, "has null");
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("has null", e.getMessage());
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.noNullElements(new String[]{"a", null});
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        java.util.Collection<String> c = com.skrstop.framework.components.util.value.validate.Assert.notEmpty(Arrays.asList("a"), "m");
        Assert.assertEquals(1, c.size());
        try {
            com.skrstop.framework.components.util.value.validate.Assert.notEmpty(Collections.emptyList());
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.notEmpty((java.util.Collection<String>) null);
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        Map<String, String> map = new HashMap<>();
        map.put("k", "v");
        Map<String, String> map2 = com.skrstop.framework.components.util.value.validate.Assert.notEmpty(map, "m");
        Assert.assertEquals(1, map2.size());
        try {
            com.skrstop.framework.components.util.value.validate.Assert.notEmpty(Collections.emptyMap());
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.notEmpty((Map<String, String>) null);
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
    }

    @Test
    public void testInstanceAndAssignable() {
        Assert.assertEquals("s", com.skrstop.framework.components.util.value.validate.Assert.isInstanceOf(String.class, "s"));
        Assert.assertEquals("s", com.skrstop.framework.components.util.value.validate.Assert.isInstanceOf(String.class, "s", "m"));
        try {
            com.skrstop.framework.components.util.value.validate.Assert.isInstanceOf(Integer.class, "s");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        com.skrstop.framework.components.util.value.validate.Assert.isAssignable(Object.class, String.class);
        com.skrstop.framework.components.util.value.validate.Assert.isAssignable(Object.class, String.class, "m");
        try {
            com.skrstop.framework.components.util.value.validate.Assert.isAssignable(String.class, Integer.class);
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        // subType / superType 为 null 的分支
        try {
            com.skrstop.framework.components.util.value.validate.Assert.isAssignable(Object.class, null);
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.isAssignable(null, String.class);
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
    }

    @Test
    public void testState() {
        com.skrstop.framework.components.util.value.validate.Assert.state(true);
        com.skrstop.framework.components.util.value.validate.Assert.state(true, "m");
        try {
            com.skrstop.framework.components.util.value.validate.Assert.state(false, "bad state");
            Assert.fail();
        } catch (IllegalStateException e) {
            Assert.assertEquals("bad state", e.getMessage());
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.state(false);
            Assert.fail();
        } catch (IllegalStateException ignored) {
        }
    }

    @Test
    public void testIndexAndBetween() {
        Assert.assertEquals(1, com.skrstop.framework.components.util.value.validate.Assert.checkIndex(1, 3));
        Assert.assertEquals(1, com.skrstop.framework.components.util.value.validate.Assert.checkIndex(1, 3, "m"));
        try {
            com.skrstop.framework.components.util.value.validate.Assert.checkIndex(-1, 3);
            Assert.fail();
        } catch (IndexOutOfBoundsException ignored) {
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.checkIndex(3, 3);
            Assert.fail();
        } catch (IndexOutOfBoundsException ignored) {
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.checkIndex(1, -1);
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        for (int[] c : new int[][]{{1, 1, 3}, {3, 1, 3}, {0, 1, 3}, {4, 1, 3}}) {
            try {
                com.skrstop.framework.components.util.value.validate.Assert.checkBetween(c[0], c[1], c[2]);
                Assert.assertTrue(c[0] >= c[1] && c[0] <= c[2]);
            } catch (RuntimeException e) {
                Assert.assertTrue(c[0] < c[1] || c[0] > c[2]);
            }
        }
        // Number 版越界
        try {
            com.skrstop.framework.components.util.value.validate.Assert.checkBetween((Number) 4, 1, 3);
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.checkBetween((Number) 0, 1, 3);
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.checkBetween(4, 1, 3);
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        for (long[] c : new long[][]{{1, 1, 3}, {3, 1, 3}, {0, 1, 3}, {4, 1, 3}}) {
            try {
                com.skrstop.framework.components.util.value.validate.Assert.checkBetween(c[0], c[1], c[2]);
                Assert.assertTrue(c[0] >= c[1] && c[0] <= c[2]);
            } catch (RuntimeException e) {
                Assert.assertTrue(c[0] < c[1] || c[0] > c[2]);
            }
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.checkBetween(0L, 1L, 3L);
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        for (double[] c : new double[][]{{1, 1, 3}, {3, 1, 3}, {0, 1, 3}, {4, 1, 3}}) {
            try {
                com.skrstop.framework.components.util.value.validate.Assert.checkBetween(c[0], c[1], c[2]);
                Assert.assertTrue(c[0] >= c[1] && c[0] <= c[2]);
            } catch (RuntimeException e) {
                Assert.assertTrue(c[0] < c[1] || c[0] > c[2]);
            }
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.checkBetween(4.0, 1.0, 3.0);
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        Assert.assertEquals(2, com.skrstop.framework.components.util.value.validate.Assert.checkBetween((Number) 2, 1, 3));
        try {
            com.skrstop.framework.components.util.value.validate.Assert.checkBetween((Number) null, 1, 3);
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.checkBetween(5, (Number) null, 3);
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        try {
            com.skrstop.framework.components.util.value.validate.Assert.checkBetween(5, 1, (Number) null);
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
    }

}