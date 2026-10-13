package com.skrstop.framework.components.starter.database.wrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PageQueryTest {

    @Test
    public void toPageDefaults() {
        PageQuery query = new PageQuery();
        Page<Object> page = query.toPage(Object.class);
        Assert.assertEquals(1, page.getCurrent());
        Assert.assertEquals(10, page.getSize());
        Assert.assertTrue(page.orders().isEmpty());
        Assert.assertTrue(page.searchCount());

        Page raw = query.toPage();
        Assert.assertEquals(1, raw.getCurrent());
        Assert.assertEquals(10, raw.getSize());
    }

    @Test
    public void toPageWithOrdersAndCalcCount() {
        PageQuery query = new PageQuery();
        query.setPageNumber(3);
        query.setPageSize(20);
        query.setAscs(Arrays.asList("a", "b"));
        query.setDescs(Arrays.asList("c"));

        Page<Object> page = query.toPage(Object.class, false);
        Assert.assertEquals(3, page.getCurrent());
        Assert.assertEquals(20, page.getSize());
        Assert.assertFalse(page.searchCount());
        Assert.assertEquals(3, page.orders().size());
        Assert.assertTrue(page.orders().stream().anyMatch(o -> "a".equals(o.getColumn()) && o.isAsc()));
        Assert.assertTrue(page.orders().stream().anyMatch(o -> "c".equals(o.getColumn()) && !o.isAsc()));

        Page raw = query.toPage(true);
        Assert.assertTrue(raw.searchCount());
        Assert.assertEquals(3, raw.orders().size());
    }

    @Test
    public void pageNumberAndSizeFallback() {
        PageQuery query = new PageQuery();
        query.setPageNumber(-1);
        query.setPageSize(0);
        Page<Object> page = query.toPage(Object.class);
        Assert.assertEquals(1, page.getCurrent());
        Assert.assertEquals(10, page.getSize());
    }
}
