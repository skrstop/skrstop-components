package com.skrstop.framework.components.starter.database.configuration.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.skrstop.framework.components.core.common.response.PageListResult;
import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PageResponseInterceptorsTest {

    @Test
    public void mybatisPlusInterceptor() {
        MybatisPlusPageResponseInterceptor interceptor = new MybatisPlusPageResponseInterceptor();
        Assert.assertEquals(0, interceptor.order());
        Assert.assertFalse(interceptor.support("str"));
        Assert.assertTrue(interceptor.support(new Page<>()));

        Page<Object> page = new Page<>(3, 5);
        page.setTotal(20);
        page.setRecords(Arrays.asList("a", "b"));
        InterceptorResult result = interceptor.execute(page);
        Assert.assertFalse(result.isNext());
        Assert.assertTrue(result.getResult() instanceof PageListResult);
        PageListResult<?> plr = (PageListResult<?>) result.getResult();
        Assert.assertEquals(3L, plr.getData().getPageNumber());
        Assert.assertEquals(5L, plr.getData().getPageSize());
        Assert.assertEquals(20L, plr.getData().getTotal());
        Assert.assertEquals(2, plr.getData().getRows().size());
    }

    @Test
    public void pageHelperInterceptor() {
        PageHelperPageResponseInterceptor interceptor = new PageHelperPageResponseInterceptor();
        Assert.assertEquals(1, interceptor.order());
        Assert.assertFalse(interceptor.support("str"));
        java.util.List<String> list = Arrays.asList("a", "b");
        PageInfo<String> pageInfo = new PageInfo<>(list);
        pageInfo.setPageNum(2);
        pageInfo.setPageSize(4);
        pageInfo.setTotal(9);
        Assert.assertTrue(interceptor.support(pageInfo));

        InterceptorResult result = interceptor.execute(pageInfo);
        Assert.assertFalse(result.isNext());
        Assert.assertTrue(result.getResult() instanceof PageListResult);
        PageListResult<?> plr = (PageListResult<?>) result.getResult();
        Assert.assertEquals(2L, plr.getData().getPageNumber());
        Assert.assertEquals(4L, plr.getData().getPageSize());
        Assert.assertEquals(9L, plr.getData().getTotal());
        Assert.assertEquals(2, plr.getData().getRows().size());
    }
}
