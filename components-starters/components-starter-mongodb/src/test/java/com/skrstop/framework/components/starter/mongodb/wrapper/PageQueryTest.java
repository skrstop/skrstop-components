package com.skrstop.framework.components.starter.mongodb.wrapper;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class PageQueryTest {

    @Test
    public void setters() {
        PageQuery query = new PageQuery();
        query.setPageNumber(2);
        query.setPageSize(20);
        query.setAscs(Collections.singletonList("a"));
        query.setDescs(Collections.singletonList("d"));
        Assert.assertEquals(Integer.valueOf(2), query.getPageNumber());
        Assert.assertEquals(Integer.valueOf(20), query.getPageSize());
        Assert.assertEquals(Collections.singletonList("a"), query.getAscs());
        Assert.assertEquals(Collections.singletonList("d"), query.getDescs());
    }

    @Test
    public void builder() {
        PageQuery query = PageQuery.builder()
                .pageNumber(1)
                .pageSize(10)
                .ascs(Collections.singletonList("a"))
                .descs(Collections.singletonList("d"))
                .build();
        Assert.assertEquals(Integer.valueOf(1), query.getPageNumber());
        Assert.assertEquals(Integer.valueOf(10), query.getPageSize());
        Assert.assertEquals(Collections.singletonList("a"), query.getAscs());
        Assert.assertEquals(Collections.singletonList("d"), query.getDescs());
    }

    @Test
    public void chainedSetters() {
        PageQuery query = new PageQuery();
        // 父类链式 setter 返回 ClientPageQuery
        Assert.assertNotNull(query.setPageNumber(3).setPageSize(30));
        Assert.assertEquals(Integer.valueOf(3), query.getPageNumber());
        Assert.assertEquals(Integer.valueOf(30), query.getPageSize());
    }
}
