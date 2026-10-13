package com.skrstop.framework.components.util.value.data;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author 蒋时华
 */
public class ArrayUtilTest {

    @Test
    public void testToIntArray() {
        Assert.assertArrayEquals(new Integer[]{1, 2}, ArrayUtil.toIntArray("1,2"));
        Assert.assertArrayEquals(new Integer[]{1, 2}, ArrayUtil.toIntArray(",", "1,2"));
        Assert.assertEquals(0, ArrayUtil.toIntArray((String) null).length);
        Assert.assertArrayEquals(new Integer[]{1}, ArrayUtil.toIntArray("", "1"));
        Assert.assertEquals(0, ArrayUtil.toIntArray(",", "").length);
        Assert.assertNull(ArrayUtil.toIntArray((java.util.Collection<Integer>) null));
        Assert.assertNull(ArrayUtil.toIntArray(Collections.<Integer>emptyList()));
        Assert.assertArrayEquals(new Integer[]{1, 2}, ArrayUtil.toIntArray(Arrays.asList(1, 2)));
        // 非法数字 → 0
        Assert.assertArrayEquals(new Integer[]{0}, ArrayUtil.toIntArray("abc"));
    }

    @Test
    public void testToStrArray() {
        Assert.assertArrayEquals(new String[]{"a", "b"}, ArrayUtil.toStrArray("a,b"));
        Assert.assertArrayEquals(new String[]{"a", "b"}, ArrayUtil.toStrArray(";", "a;b"));
        Assert.assertArrayEquals(new String[]{"a;b"}, ArrayUtil.toStrArray(",", "a;b"));
        Assert.assertEquals(0, ArrayUtil.toStrArray((String) null).length);
        Assert.assertEquals(0, ArrayUtil.toStrArray("  ").length);
        Assert.assertNull(ArrayUtil.toStrArray((java.util.Collection<String>) null));
        Assert.assertArrayEquals(new String[]{"a"}, ArrayUtil.toStrArray(Collections.singletonList("a")));
    }

    @Test
    public void testToLongArray() {
        Assert.assertArrayEquals(new Long[]{1L, 2L}, ArrayUtil.toLongArray("1,2"));
        Assert.assertArrayEquals(new Long[]{1L}, ArrayUtil.toLongArray(",", "1"));
        Assert.assertEquals(0, ArrayUtil.toLongArray((String) null).length);
        Assert.assertNull(ArrayUtil.toLongArray((java.util.Collection<Long>) null));
        Assert.assertArrayEquals(new Long[]{1L}, ArrayUtil.toLongArray(Collections.singletonList(1L)));
    }

    @Test
    public void testToDoubleArray() {
        Assert.assertArrayEquals(new Double[]{1.5}, ArrayUtil.toDoubleArray("1.5"));
        Assert.assertArrayEquals(new Double[]{1.5}, ArrayUtil.toDoubleArray(",", "1.5"));
        Assert.assertEquals(0, ArrayUtil.toDoubleArray((String) null).length);
        Assert.assertNull(ArrayUtil.toDoubleArray((java.util.Collection<Double>) null));
        Assert.assertArrayEquals(new Double[]{1.5}, ArrayUtil.toDoubleArray(Collections.singletonList(1.5)));
    }

    @Test
    public void testToFloatArray() {
        Assert.assertArrayEquals(new Float[]{1.5f}, ArrayUtil.toFloatArray("1.5"));
        Assert.assertArrayEquals(new Float[]{1.5f}, ArrayUtil.toFloatArray(",", "1.5"));
        Assert.assertEquals(0, ArrayUtil.toFloatArray((String) null).length);
        Assert.assertNull(ArrayUtil.toFloatArray((java.util.Collection<Float>) null));
        Assert.assertArrayEquals(new Float[]{1.5f}, ArrayUtil.toFloatArray(Collections.singletonList(1.5f)));
    }

    @Test
    public void testToBoolArray() {
        Assert.assertArrayEquals(new Boolean[]{true, false}, ArrayUtil.toBoolArray("true,false"));
        Assert.assertArrayEquals(new Boolean[]{true}, ArrayUtil.toBoolArray(",", "true"));
        Assert.assertEquals(0, ArrayUtil.toBoolArray((String) null).length);
        Assert.assertNull(ArrayUtil.toBoolArray((java.util.Collection<Boolean>) null));
        Assert.assertArrayEquals(
                new Boolean[]{true}, ArrayUtil.toBoolArray(Collections.singletonList(true)));
    }

    @Test
    public void testToShortArray() {
        Assert.assertArrayEquals(new Short[]{1, 2}, ArrayUtil.toShortArray("1,2"));
        Assert.assertArrayEquals(new Short[]{3}, ArrayUtil.toShortArray(",", "3"));
        Assert.assertEquals(0, ArrayUtil.toShortArray((String) null).length);
        Assert.assertNull(ArrayUtil.toShortArray((java.util.Collection<Short>) null));
        Assert.assertArrayEquals(new Short[]{4}, ArrayUtil.toShortArray(Collections.singletonList((short) 4)));
    }

    @Test
    public void testContains() {
        Assert.assertTrue(ArrayUtil.contains(new String[]{"a", "b"}, "a"));
        Assert.assertFalse(ArrayUtil.contains(new String[]{"a"}, "c"));
        Assert.assertFalse(ArrayUtil.contains(null, "a"));
    }

    @Test
    public void testNullCollections() {
        Assert.assertNull(ArrayUtil.toIntArray((java.util.Collection<Integer>) null));
        Assert.assertNull(ArrayUtil.toIntArray(java.util.Collections.emptyList()));
        Assert.assertNull(ArrayUtil.toStrArray((java.util.Collection<String>) null));
        Assert.assertNull(ArrayUtil.toStrArray(java.util.Collections.emptyList()));
        Assert.assertNull(ArrayUtil.toLongArray((java.util.Collection<Long>) null));
        Assert.assertNull(ArrayUtil.toLongArray(java.util.Collections.emptyList()));
        Assert.assertNull(ArrayUtil.toDoubleArray((java.util.Collection<Double>) null));
        Assert.assertNull(ArrayUtil.toDoubleArray(java.util.Collections.emptyList()));
        Assert.assertNull(ArrayUtil.toFloatArray((java.util.Collection<Float>) null));
        Assert.assertNull(ArrayUtil.toFloatArray(java.util.Collections.emptyList()));
        Assert.assertNull(ArrayUtil.toBoolArray((java.util.Collection<Boolean>) null));
        Assert.assertNull(ArrayUtil.toBoolArray(java.util.Collections.emptyList()));
        Assert.assertNull(ArrayUtil.toShortArray((java.util.Collection<Short>) null));
        Assert.assertNull(ArrayUtil.toShortArray(java.util.Collections.emptyList()));
    }

}
