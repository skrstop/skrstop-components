package com.skrstop.framework.components.util.value.data;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author 蒋时华
 */
public class CollectionUtilTest {

    @Test
    public void testEquals() {
        Assert.assertTrue(CollectionUtil.equals(null, null));
        Assert.assertTrue(CollectionUtil.equals(Arrays.asList("a", "b"), Arrays.asList("b", "a")));
        // 首个 containsAll 为 false，短路
        Assert.assertFalse(CollectionUtil.equals(Arrays.asList("x"), Arrays.asList("a")));
        Assert.assertFalse(CollectionUtil.equals(Arrays.asList("a"), Arrays.asList("b")));
        Assert.assertFalse(CollectionUtil.equals(Arrays.asList("a"), null));
        Assert.assertFalse(CollectionUtil.equals(null, Arrays.asList("a")));
    }

    @Test
    public void testContains() {
        Assert.assertTrue(CollectionUtil.contains(Arrays.asList("a").iterator(), "a"));
        Assert.assertFalse(CollectionUtil.contains(Arrays.asList("a").iterator(), "b"));
        Assert.assertFalse(CollectionUtil.contains((Iterator<?>) null, "a"));

        Vector<String> v = new Vector<>(Arrays.asList("a", "b"));
        Assert.assertTrue(CollectionUtil.contains(v.elements(), "a"));
        Assert.assertFalse(CollectionUtil.contains(v.elements(), "c"));
        Assert.assertFalse(CollectionUtil.contains((Enumeration<?>) null, "a"));
    }

    @Test
    public void testToList() {
        Assert.assertEquals(Arrays.asList("1", "2"), CollectionUtil.toStrList("1,2"));
        Assert.assertEquals(Arrays.asList("1", "2"), CollectionUtil.toStrList(",", "1,2"));
        Assert.assertEquals(Arrays.asList(1, 2), CollectionUtil.toIntList("1,2"));
        Assert.assertEquals(Arrays.asList(1, 2), CollectionUtil.toIntList(",", "1,2"));
        Assert.assertEquals(Arrays.asList(1L, 2L), CollectionUtil.toLongList("1,2"));
        Assert.assertEquals(Arrays.asList(1L, 2L), CollectionUtil.toLongList(",", "1,2"));
        Assert.assertEquals(Arrays.asList(1.5, 2.5), CollectionUtil.toDoubleList("1.5,2.5"));
        Assert.assertEquals(Arrays.asList(1.5, 2.5), CollectionUtil.toDoubleList(",", "1.5,2.5"));
        Assert.assertEquals(Arrays.asList(1.5f, 2.5f), CollectionUtil.toFloatList("1.5,2.5"));
        Assert.assertEquals(Arrays.asList(1.5f, 2.5f), CollectionUtil.toFloatList(",", "1.5,2.5"));
        Assert.assertEquals(Arrays.asList((short) 1, (short) 2), CollectionUtil.toShortList("1,2"));
        Assert.assertEquals(Arrays.asList((short) 1, (short) 2), CollectionUtil.toShortList(",", "1,2"));
        Assert.assertEquals(Arrays.asList(true, false), CollectionUtil.toBoolList("true,false"));
        Assert.assertEquals(Arrays.asList(true, false), CollectionUtil.toBoolList(",", "true,false"));
        // 空输入
        Assert.assertTrue(CollectionUtil.toStrList((String) null).isEmpty());
        Assert.assertTrue(CollectionUtil.toIntList((String) null).isEmpty());
        Assert.assertTrue(CollectionUtil.toLongList("").isEmpty());
        Assert.assertTrue(CollectionUtil.toDoubleList("").isEmpty());
        Assert.assertTrue(CollectionUtil.toFloatList((String) null).isEmpty());
        Assert.assertTrue(CollectionUtil.toShortList("").isEmpty());
        Assert.assertTrue(CollectionUtil.toBoolList((String) null).isEmpty());
    }

    @Test
    public void testAddAll() {
        Collection<String> target = new ArrayList<>(Collections.singletonList("a"));
        Collection<String> result =
                CollectionUtil.addAll(target, Arrays.asList("b"), Arrays.asList("c"));
        Assert.assertEquals(Arrays.asList("a", "b", "c"), new ArrayList<>(result));
    }

    @Test
    public void testEqualsCollections() {
        Assert.assertTrue(CollectionUtil.equals(null, null));
        Assert.assertTrue(CollectionUtil.equals(Arrays.asList("a", "b"), Arrays.asList("b", "a")));
        // 首个 containsAll 为 false，短路
        Assert.assertFalse(CollectionUtil.equals(Arrays.asList("x"), Arrays.asList("a")));
        Assert.assertFalse(CollectionUtil.equals(Arrays.asList("a"), Arrays.asList("a", "b")));
        // 第一个 containsAll 为 true、第二个为 false
        Assert.assertFalse(CollectionUtil.equals(Arrays.asList("a", "b"), Arrays.asList("a")));
        // null target 走 newArrayList
        Collection<String> merged2 = CollectionUtil.addAll(null, Arrays.asList("a"), Arrays.asList("b"));
        Assert.assertEquals(Arrays.asList("a", "b"), new ArrayList<>(merged2));
        Assert.assertFalse(CollectionUtil.equals(Arrays.asList("a"), null));
        Assert.assertFalse(CollectionUtil.equals(null, Arrays.asList("a")));
        // addAll 空参数
        Collection<String> t = CollectionUtil.newArrayList();
        // 空 source 不追加
        Collection<String> merged = CollectionUtil.addAll(t, java.util.Collections.emptyList(), Arrays.asList("a"));
        Assert.assertEquals(Arrays.asList("a"), new ArrayList<>(merged));
        Assert.assertSame(t, CollectionUtil.addAll(t));
    }

}
