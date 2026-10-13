package com.skrstop.framework.components.starter.redis.filter;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;

public class FastjsonValueFilterTest {

    @Test
    public void filterEach() {
        FastjsonValueFilter filter = new FastjsonValueFilter(true, true);
        Assert.assertNull(filter.filter(null, String.class));
        Assert.assertEquals("s", filter.filter("s", String.class));
        Object o = 123;
        Assert.assertEquals("123", filter.filter(o, String.class));

        Assert.assertNull(filter.filterStr2List(null, String.class));
        Assert.assertNull(filter.filterStr2List("not json", String.class));
        Assert.assertNull(filter.filterStr2List("{\"a\":1}", String.class));
        List<String> list = filter.filterStr2List("[\"a\",\"b\"]", String.class);
        Assert.assertEquals(Arrays.asList("a", "b"), list);

        Set<Object> set = new HashSet<>(Arrays.asList("a", null, "b"));
        Assert.assertNull(filter.filterSet(null, String.class));
        Set<String> rs = filter.filterSet(set, String.class);
        Assert.assertEquals(new HashSet<>(Arrays.asList("a", "b")), rs);

        Assert.assertNull(filter.filterSetScore(null, String.class));
        Set<ZSetOperations.TypedTuple<Object>> scored = new HashSet<>();
        scored.add(new DefaultTypedTuple<>((Object) "a", 1.0));
        scored.add(null);
        Set<ZSetOperations.TypedTuple<String>> rs2 = filter.filterSetScore((Set) scored, String.class);
        Assert.assertEquals(1, rs2.size());

        List<Object> listObj = new ArrayList<>(Arrays.asList("a", null, "b"));
        Assert.assertNull(filter.filterList(null, String.class));
        List<String> rl = filter.filterList(listObj, String.class);
        Assert.assertEquals(Arrays.asList("a", "b"), rl);
    }

    @Test
    public void noAutoType() {
        FastjsonValueFilter filter = new FastjsonValueFilter(false, false);
        Object o = 123;
        Assert.assertEquals("123", filter.filter(o, String.class));
        Set<Object> set = new HashSet<>(Arrays.asList("a", "b"));
        Set<String> rs = filter.filterSet(set, String.class);
        Assert.assertEquals(new HashSet<>(Arrays.asList("a", "b")), rs);
        List<Object> listObj = new ArrayList<>(Arrays.asList("a", "b"));
        List<String> rl = filter.filterList(listObj, String.class);
        Assert.assertEquals(Arrays.asList("a", "b"), rl);
        // 相同类型逐个保留
        Set<Object> sameSet = new HashSet<>(Arrays.asList("a"));
        Assert.assertEquals(new HashSet<>(Arrays.asList("a")), filter.filterSet(sameSet, String.class));
        List<Object> sameList = new ArrayList<>(Arrays.asList("a"));
        Assert.assertEquals(Arrays.asList("a"), filter.filterList(sameList, String.class));
    }

    @Test
    public void autoType() {
        FastjsonValueFilter filter = new FastjsonValueFilter(false, true);
        Set<Object> set = new HashSet<>(Arrays.asList("a", "b"));
        Set<String> rs = filter.filterSet(set, String.class);
        Assert.assertEquals(new HashSet<>(Arrays.asList("a", "b")), rs);
        List<Object> listObj = new ArrayList<>(Arrays.asList("a", "b"));
        List<String> rl = filter.filterList(listObj, String.class);
        Assert.assertEquals(Arrays.asList("a", "b"), rl);
    }

    @Test
    public void filterEachSameClass() {
        FastjsonValueFilter filter = new FastjsonValueFilter(true, false);
        Set<Object> set = new HashSet<>(Arrays.asList("a"));
        Assert.assertEquals(new HashSet<>(Arrays.asList("a")), filter.filterSet(set, String.class));
        List<Object> listObj = new ArrayList<>(Arrays.asList("a"));
        Assert.assertEquals(Arrays.asList("a"), filter.filterList(listObj, String.class));
    }

    @Test
    public void filterEachNonEqualClass() {
        // filterEach=true 且元素类型与目标类型不同：走 FastJson 转换
        FastjsonValueFilter filter = new FastjsonValueFilter(true, false);
        Set<Object> set = new HashSet<>(Arrays.asList(123));
        Set<Long> rs = filter.filterSet(set, Long.class);
        Assert.assertEquals(new HashSet<>(Arrays.asList(123L)), rs);
        List<Object> listObj = new ArrayList<>(Arrays.asList(123));
        List<Long> rl = filter.filterList(listObj, Long.class);
        Assert.assertEquals(Arrays.asList(123L), rl);
    }

    @Test
    public void setScoreEqualTupleClass() {
        // cls 与 TypedTuple 自身类型相同：走 result.add(o)
        FastjsonValueFilter filter = new FastjsonValueFilter(false, true);
        Set scored = new HashSet<>();
        scored.add(new DefaultTypedTuple("a", 1.0));
        Set rs = filter.filterSetScore(scored, DefaultTypedTuple.class);
        Assert.assertEquals(1, rs.size());
        Assert.assertSame(scored.iterator().next(), rs.iterator().next());
    }

    @Test
    public void setScoreSameClass() {
        FastjsonValueFilter filter = new FastjsonValueFilter(false, true);
        Set<ZSetOperations.TypedTuple<String>> scored = new HashSet<>();
        scored.add(new DefaultTypedTuple<>("a", 1.0));
        Assert.assertEquals(1, filter.filterSetScore(scored, String.class).size());
        // 非同类转换
        Set<ZSetOperations.TypedTuple<Object>> scored2 = new HashSet<>();
        scored2.add(new DefaultTypedTuple<>((Object) 123, 2.0));
        Set<ZSetOperations.TypedTuple<String>> rs = filter.filterSetScore((Set) scored2, String.class);
        Assert.assertEquals(1, rs.size());
        Assert.assertEquals("123", rs.iterator().next().getValue());
    }
}
