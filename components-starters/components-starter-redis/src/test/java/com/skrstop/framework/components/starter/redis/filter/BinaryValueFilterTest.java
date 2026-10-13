package com.skrstop.framework.components.starter.redis.filter;

import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BinaryValueFilterTest {

    private final BinaryValueFilter filter = new BinaryValueFilter();

    @Test
    public void all() {
        Assert.assertEquals("s", filter.filter("s", String.class));
        try {
            filter.filterStr2List("[1]", String.class);
            Assert.fail();
        } catch (NotSupportedException e) {
            Assert.assertNotNull(e.getMessage());
        }
        Set<String> set = new HashSet<>(Arrays.asList("a"));
        Assert.assertSame(set, filter.filterSet(set, String.class));
        Set<ZSetOperations.TypedTuple<String>> scored = new HashSet<>();
        scored.add(new DefaultTypedTuple<>("a", 1.0));
        Assert.assertSame(scored, filter.filterSetScore(scored, String.class));
        List<String> list = Arrays.asList("a");
        Assert.assertSame(list, filter.filterList(list, String.class));
    }
}
