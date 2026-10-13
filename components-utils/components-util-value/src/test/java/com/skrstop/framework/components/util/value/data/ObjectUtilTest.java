package com.skrstop.framework.components.util.value.data;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author 蒋时华
 */
public class ObjectUtilTest {

    @Test
    public void testEquals() {
        Assert.assertTrue(ObjectUtil.equals("a", "a"));
        Assert.assertFalse(ObjectUtil.equals("a", "b"));
        Assert.assertTrue(ObjectUtil.equals(null, null));
        Assert.assertFalse(ObjectUtil.equals(null, "a"));
    }

    @Test
    public void testNullSafeToString() {
        Assert.assertEquals("null", ObjectUtil.nullSafeToString(null));
        Assert.assertEquals("str", ObjectUtil.nullSafeToString("str"));
        Assert.assertEquals("{a, b}", ObjectUtil.nullSafeToString(new String[]{"a", "b"}));
        Assert.assertEquals("{true, false}", ObjectUtil.nullSafeToString(new boolean[]{true, false}));
        Assert.assertEquals("{1, 2}", ObjectUtil.nullSafeToString(new byte[]{1, 2}));
        Assert.assertEquals("{a, b}", ObjectUtil.nullSafeToString(new char[]{'a', 'b'}));
        Assert.assertEquals("{1.0, 2.0}", ObjectUtil.nullSafeToString(new double[]{1, 2}));
        Assert.assertEquals("{1.0, 2.0}", ObjectUtil.nullSafeToString(new float[]{1, 2}));
        Assert.assertEquals("{1, 2}", ObjectUtil.nullSafeToString(new int[]{1, 2}));
        Assert.assertEquals("{1, 2}", ObjectUtil.nullSafeToString(new long[]{1, 2}));
        Assert.assertEquals("{1, 2}", ObjectUtil.nullSafeToString(new short[]{1, 2}));
        Object custom = new Object() {
            @Override
            public String toString() {
                return "custom";
            }
        };
        Assert.assertEquals("custom", ObjectUtil.nullSafeToString(custom));
        // toString 返回 null → 空串
        Assert.assertEquals("", ObjectUtil.nullSafeToString(new Object() {
            @Override
            public String toString() {
                return null;
            }
        }));
    }

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(ObjectUtil.isEmpty(null));
        Assert.assertTrue(ObjectUtil.isEmpty(Optional.empty()));
        Assert.assertFalse(ObjectUtil.isEmpty(Optional.of("a")));
        Assert.assertTrue(ObjectUtil.isEmpty(""));
        Assert.assertFalse(ObjectUtil.isEmpty("a"));
        Assert.assertTrue(ObjectUtil.isEmpty(new int[0]));
        Assert.assertFalse(ObjectUtil.isEmpty(new int[]{1}));
        Assert.assertTrue(ObjectUtil.isEmpty(new ArrayList<>()));
        Assert.assertFalse(ObjectUtil.isEmpty(Arrays.asList("a")));
        Map<String, String> map = new HashMap<>();
        Assert.assertTrue(ObjectUtil.isEmpty(map));
        map.put("a", "b");
        Assert.assertFalse(ObjectUtil.isEmpty(map));
        // 非集合对象 → 走 Map instanceof false
        Assert.assertFalse(ObjectUtil.isEmpty(new Object()));
        Assert.assertFalse(ObjectUtil.isNotEmpty(null));
        Assert.assertTrue(ObjectUtil.isNotEmpty("a"));
    }

    @Test
    public void testIsEmptyBatch() {
        // 全部非空白 → true（注意源码语义：有空白则 false）
        Assert.assertTrue(ObjectUtil.isEmptyBatch("a", "b"));
        Assert.assertFalse(ObjectUtil.isEmptyBatch("a", ""));
        Assert.assertFalse(ObjectUtil.isEmptyBatch("a", " "));
        Assert.assertFalse(ObjectUtil.isEmptyBatch((String) null));
    }

    @Test
    public void testIsBaseType() {
        Assert.assertTrue(ObjectUtil.isBaseType(1));
        Assert.assertTrue(ObjectUtil.isBaseType("s"));
        Assert.assertFalse(ObjectUtil.isBaseType(new Object()));
        Assert.assertFalse(ObjectUtil.isBaseType(null));
        Assert.assertTrue(ObjectUtil.isNotBaseType(new Object()));
        Assert.assertFalse(ObjectUtil.isNotBaseType("s"));
    }

    @Test
    public void testNullSafeEquals() {
        Assert.assertTrue(ObjectUtil.nullSafeEquals(null, null));
        Assert.assertFalse(ObjectUtil.nullSafeEquals(null, "a"));
        Assert.assertFalse(ObjectUtil.nullSafeEquals("a", null));
        Assert.assertTrue(ObjectUtil.nullSafeEquals("a", "a"));
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new int[]{1, 2}, new int[]{1, 2}));
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new long[]{1}, new long[]{1}));
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new byte[]{1}, new byte[]{1}));
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new char[]{'a'}, new char[]{'a'}));
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new double[]{1.0}, new double[]{1.0}));
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new float[]{1.0f}, new float[]{1.0f}));
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new short[]{1}, new short[]{1}));
        Assert.assertFalse(ObjectUtil.nullSafeEquals(new short[]{1}, new short[]{2}));
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new boolean[]{true}, new boolean[]{true}));
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new String[]{"a"}, new String[]{"a"}));
        Assert.assertFalse(ObjectUtil.nullSafeEquals(new int[]{1}, new int[]{2}));
        Assert.assertFalse(ObjectUtil.nullSafeEquals("a", "b"));
        // 类型不同的数组
        Assert.assertFalse(ObjectUtil.nullSafeEquals(new int[]{1}, new long[]{1}));
    }

    @Test
    public void testToObjectArray() {
        Assert.assertArrayEquals(new String[]{"a"}, ObjectUtil.toObjectArray(new String[]{"a"}));
        Assert.assertEquals(0, ObjectUtil.toObjectArray(null).length);
        try {
            ObjectUtil.toObjectArray("not array");
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
        int[] primitive = new int[]{1, 2};
        Object[] objs = ObjectUtil.toObjectArray(primitive);
        Assert.assertEquals(2, objs.length);
        Assert.assertEquals(Integer.valueOf(1), objs[0]);
        Assert.assertEquals(0, ObjectUtil.toObjectArray(new int[0]).length);
    }

    @Test
    public void testOfAndEmptyArray() {
        Assert.assertArrayEquals(new String[]{"a", "b"}, ObjectUtil.of("a", "b"));
        Assert.assertEquals(0, ObjectUtil.emptyArray(String.class).length);
    }

    @Test
    public void testNullSafeEqualsArrays() {
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new int[]{1, 2}, new int[]{1, 2}));
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new byte[]{1}, new byte[]{1}));
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new boolean[]{true}, new boolean[]{true}));
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new char[]{'a'}, new char[]{'a'}));
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new double[]{1.0}, new double[]{1.0}));
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new float[]{1.0f}, new float[]{1.0f}));
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new long[]{1L}, new long[]{1L}));
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new short[]{1}, new short[]{1}));
        Assert.assertFalse(ObjectUtil.nullSafeEquals(new short[]{1}, new short[]{2}));
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new Object[]{"a"}, new Object[]{"a"}));
        Assert.assertFalse(ObjectUtil.nullSafeEquals(new int[]{1}, new int[]{2}));
        Assert.assertFalse(ObjectUtil.nullSafeEquals(new int[]{1}, new long[]{1}));
        Assert.assertFalse(ObjectUtil.nullSafeEquals(new Object[]{"a"}, "a"));
        // 左侧类型匹配但右侧不匹配的短路分支
        Assert.assertFalse(ObjectUtil.nullSafeEquals(new Object[]{1}, new int[]{1}));
        Assert.assertFalse(ObjectUtil.nullSafeEquals(new boolean[]{true}, new int[]{1}));
        Assert.assertFalse(ObjectUtil.nullSafeEquals(new byte[]{1}, new int[]{1}));
        Assert.assertFalse(ObjectUtil.nullSafeEquals(new char[]{'a'}, new int[]{1}));
        Assert.assertFalse(ObjectUtil.nullSafeEquals(new double[]{1}, new int[]{1}));
        Assert.assertFalse(ObjectUtil.nullSafeEquals(new float[]{1}, new int[]{1}));
        Assert.assertFalse(ObjectUtil.nullSafeEquals(new long[]{1}, new int[]{1}));
        Assert.assertFalse(ObjectUtil.nullSafeEquals(new short[]{1}, new int[]{1}));
        // 基础相等为 false 时才进入 arrayEquals
        Assert.assertFalse(ObjectUtil.nullSafeEquals(new int[]{1, 2}, new int[]{1, 3}));
        // 内容相等但对象不同 → equals 分支 return true
        Assert.assertTrue(ObjectUtil.nullSafeEquals(new String("a"), new String("a")));
    }

}
