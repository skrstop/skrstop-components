package com.skrstop.framework.components.core.common;

import com.skrstop.framework.components.core.common.request.ClientPageQuery;
import com.skrstop.framework.components.core.common.response.DefaultPageResult;
import com.skrstop.framework.components.core.common.response.PageListResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.page.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 覆盖分页相关类
 *
 * @author 蒋时华
 */
public class PageTest {

    @Test
    public void testClientPageQuery() {
        ClientPageQuery query = new ClientPageQuery();
        Assert.assertEquals(Integer.valueOf(1), query.getPageNumber());
        Assert.assertEquals(Integer.valueOf(10), query.getPageSize());
        Assert.assertTrue(query.getAscs().isEmpty());
        Assert.assertTrue(query.getDescs().isEmpty());

        query.setPageNumber(-1);
        Assert.assertEquals(Integer.valueOf(1), query.getPageNumber());
        query.setPageNumber(0);
        Assert.assertEquals(Integer.valueOf(1), query.getPageNumber());
        query.setPageNumber(5);
        Assert.assertEquals(Integer.valueOf(5), query.getPageNumber());

        query.setPageSize(0);
        Assert.assertEquals(Integer.valueOf(10), query.getPageSize());
        query.setPageSize(20);
        Assert.assertEquals(Integer.valueOf(20), query.getPageSize());

        query.getAscs().add("a");
        query.getDescs().add("d");
        Assert.assertEquals(Arrays.asList("a"), query.getAscs());
        Assert.assertEquals(Arrays.asList("d"), query.getDescs());

        ClientPageQuery allArgs = new ClientPageQuery(2, 5, Arrays.asList("a"), Arrays.asList("d"), "last");
        Assert.assertEquals(Integer.valueOf(2), allArgs.getPageNumber());
        Assert.assertEquals("last", allArgs.getLastPageInfo());
        Assert.assertNotNull(ClientPageQuery.builder().build());
    }

    @Test
    public void testSimplePageData() {
        SimplePageData<String> pageData = new SimplePageData<>(1, 10, 100);
        Assert.assertEquals(1, pageData.getPageNumber());
        Assert.assertEquals(10, pageData.getPageSize());
        Assert.assertEquals(100, pageData.getTotal());
        pageData.setLastPageInfo("last");
        Assert.assertEquals("last", pageData.getLastPageInfo());
        pageData.setRows("row");
        Assert.assertEquals("row", pageData.getRows());

        Assert.assertEquals(0, new SimplePageData<String>().getPageNumber());
    }

    @Test
    public void testListSimplePageDataConvert() {
        ListSimplePageData<String> pageData = new ListSimplePageData<>();
        pageData.setPageNumber(1);
        pageData.setPageSize(10);
        pageData.setTotal(2);
        pageData.setLastPageInfo("last");
        pageData.setRows(Arrays.asList("1", "2"));

        ListSimplePageData<Integer> converted = pageData.convert(Integer::parseInt);
        Assert.assertEquals(Arrays.asList(1, 2), converted.getRows());
        Assert.assertEquals(1, converted.getPageNumber());
        Assert.assertEquals(10, converted.getPageSize());
        Assert.assertEquals(2, converted.getTotal());
        Assert.assertEquals("last", converted.getLastPageInfo());
    }

    @Test
    public void testSetSimplePageDataConvert() {
        SetSimplePageData<String> pageData = new SetSimplePageData<>();
        pageData.setPageNumber(1);
        pageData.setPageSize(10);
        pageData.setTotal(2);
        pageData.setLastPageInfo("last");
        pageData.setRows(new LinkedHashSet<>(Arrays.asList("1", "2")));

        SetSimplePageData<Integer> converted = pageData.convert(Integer::parseInt);
        Assert.assertEquals(new LinkedHashSet<>(Arrays.asList(1, 2)), converted.getRows());
        Assert.assertEquals(1, converted.getPageNumber());
        Assert.assertEquals(10, converted.getPageSize());
        Assert.assertEquals(2, converted.getTotal());
        Assert.assertEquals("last", converted.getLastPageInfo());
    }

    @Test
    public void testCollectionSimplePageData() {
        CollectionSimplePageData<String> pageData = new CollectionSimplePageData<>();
        pageData.setRows(Arrays.asList("a"));
        Assert.assertEquals(Arrays.asList("a"), pageData.getRows());
    }

    @Test
    public void testFullPageData() {
        FullPageData full = new FullPageData(3, 10, 95);
        Assert.assertEquals(3, full.getPageNumber());
        Assert.assertEquals(10, full.getPageSize());
        Assert.assertEquals(95, full.getTotal());
        Assert.assertEquals(10, full.getTotalPage());
        Assert.assertEquals(1, full.getFirstPage());
        Assert.assertEquals(10, full.getLastPage());
        Assert.assertEquals(2, full.getPrePage());
        Assert.assertEquals(4, full.getNextPage());

        FullPageData zero = new FullPageData(-1, 1);
        Assert.assertEquals(0, zero.getPageNumber());
        Assert.assertEquals(1, zero.getPageSize());
        Assert.assertEquals(0, zero.getPrePage());

        FullPageData zeroTotal = new FullPageData(1, 10, -5);
        Assert.assertEquals(0, zeroTotal.getTotal());
        Assert.assertEquals(0, zeroTotal.getTotalPage());

        // pageSize 非法时不再除零
        FullPageData invalidPageSize = new FullPageData(1, 0);
        Assert.assertEquals(0, invalidPageSize.getTotalPage());

        FullPageData empty = new FullPageData();
        Assert.assertEquals(0, empty.getPageNumber());
        empty.setTotalPage(1);
        empty.setFirstPage(1);
        empty.setLastPage(1);
        empty.setPrePage(0);
        empty.setNextPage(1);
        empty.setCanFirstPage(true);
        empty.setCanLastPage(true);
        empty.setHasPreviousPage(false);
        empty.setHasNextPage(true);
        Assert.assertTrue(empty.isCanFirstPage());
        Assert.assertTrue(empty.isCanLastPage());
        Assert.assertFalse(empty.isHasPreviousPage());
        Assert.assertTrue(empty.isHasNextPage());
    }

    @Test
    public void testDefaultPageResult() {
        DefaultPageResult success = DefaultPageResult.Builder.success();
        Assert.assertEquals(CommonResultCode.SUCCESS.getCode(), success.getCode());

        DefaultPageResult withPaging = DefaultPageResult.Builder.success(1, 10);
        Assert.assertNotNull(withPaging.getData());

        DefaultPageResult withTotal = DefaultPageResult.Builder.success(1, 10, 100);
        Assert.assertEquals(100, withTotal.getData().getTotal());

        SimplePageData pageData = new SimplePageData(1, 10, 5);
        DefaultPageResult withPageData = DefaultPageResult.Builder.success(pageData);
        Assert.assertEquals(5, withPageData.getData().getTotal());

        Assert.assertEquals(CommonResultCode.FAIL.getCode(), DefaultPageResult.Builder.error().getCode());
        Assert.assertEquals(CommonResultCode.FAIL.getCode(), DefaultPageResult.Builder.error(1, 10).getCode());
        Assert.assertEquals(CommonResultCode.FAIL.getCode(), DefaultPageResult.Builder.error(1, 10, 5).getCode());
        Assert.assertEquals(CommonResultCode.FAIL.getCode(), DefaultPageResult.Builder.error(pageData).getCode());

        // result(IResult) 只拷贝 code/message，不拷贝 data
        DefaultPageResult copy = DefaultPageResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) withPageData);
        Assert.assertNull(copy.getData());

        // IPageResult 构造器
        DefaultPageResult copyFromPage = new DefaultPageResult(withPageData);
        Assert.assertNotNull(copyFromPage.getData());

        DefaultPageResult copy2 = DefaultPageResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success);
        Assert.assertEquals(success.getCode(), copy2.getCode());

        DefaultPageResult copy3 = DefaultPageResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10);
        Assert.assertNotNull(copy3.getData());

        DefaultPageResult copy4 = DefaultPageResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5);
        Assert.assertEquals(5, copy4.getData().getTotal());

        DefaultPageResult copy5 = DefaultPageResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, pageData);
        Assert.assertEquals(5, copy5.getData().getTotal());

        DefaultPageResult empty = new DefaultPageResult();
        Assert.assertNull(empty.getData());
        Assert.assertNotNull(new DefaultPageResult((com.skrstop.framework.components.core.common.response.core.IResult) success));
        Assert.assertNotNull(new DefaultPageResult(success, pageData));
        Assert.assertNotNull(new DefaultPageResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5));
    }

    @Test
    public void testPageListResult() {
        List<String> rows = Arrays.asList("a", "b");
        PageListResult success = PageListResult.Builder.success();
        Assert.assertEquals(CommonResultCode.SUCCESS.getCode(), success.getCode());

        PageListResult withPaging = PageListResult.Builder.success(1, 10);
        Assert.assertNotNull(withPaging.getData());

        PageListResult<String> withRows = PageListResult.Builder.success(1, 10, rows);
        Assert.assertEquals(rows, withRows.getData().getRows());

        PageListResult<String> withTotal = PageListResult.Builder.success(1, 10, 100);
        Assert.assertEquals(100, withTotal.getData().getTotal());

        PageListResult<String> full = PageListResult.Builder.success(1, 10, 100, rows);
        Assert.assertEquals(rows, full.getData().getRows());
        Assert.assertEquals(100, full.getData().getTotal());

        SimplePageData<List<String>> pageData = new SimplePageData<>(1, 10, 2);
        pageData.setRows(rows);
        PageListResult<String> withPageData = PageListResult.Builder.success(pageData);
        Assert.assertEquals(rows, withPageData.getData().getRows());

        PageListResult<String> withPageDataRows = PageListResult.Builder.success(pageData, rows);
        Assert.assertEquals(rows, withPageDataRows.getData().getRows());

        Assert.assertEquals(CommonResultCode.FAIL.getCode(), PageListResult.Builder.error().getCode());
        Assert.assertEquals(CommonResultCode.FAIL.getCode(), PageListResult.Builder.error(1, 10).getCode());
        Assert.assertEquals(CommonResultCode.FAIL.getCode(), PageListResult.Builder.error(1, 10, 5).getCode());
        Assert.assertNotNull(PageListResult.Builder.error(1, 10, 5, rows));
        Assert.assertNotNull(PageListResult.Builder.error(pageData));
        Assert.assertNotNull(PageListResult.Builder.error(pageData, rows));

        PageListResult<String> copy = PageListResult.Builder.result(withPageData);
        Assert.assertEquals(rows, copy.getData().getRows());

        PageListResult<String> copy2 = PageListResult.Builder.result(withPageData, rows);
        Assert.assertEquals(rows, copy2.getData().getRows());

        PageListResult<String> copy3 = PageListResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, rows);
        Assert.assertEquals(rows, copy3.getData().getRows());

        PageListResult<String> copy4 = PageListResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5);
        Assert.assertEquals(5, copy4.getData().getTotal());

        PageListResult<String> copy5 = PageListResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5, rows);
        Assert.assertEquals(rows, copy5.getData().getRows());

        PageListResult<String> copy6 = PageListResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, pageData);
        Assert.assertNotNull(copy6.getData());

        PageListResult<String> copy7 = PageListResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, pageData, rows);
        Assert.assertEquals(rows, copy7.getData().getRows());

        Assert.assertNull(new PageListResult().getData());
        Assert.assertNotNull(new PageListResult((com.skrstop.framework.components.core.common.response.core.IResult) success));
        // pageData 为 null 时 data 分支
        Assert.assertNull(new PageListResult((com.skrstop.framework.components.core.common.response.core.IResult) success, null, rows).getData());
        // IPageResult data 为 null 时的分支
        PageListResult noDataResult = PageListResult.Builder.success();
        Assert.assertNotNull(new PageListResult(noDataResult, rows));
        Assert.assertNotNull(new PageListResult((com.skrstop.framework.components.core.common.response.core.IResult) success, pageData));
        Assert.assertNotNull(new PageListResult(withPageData));
        Assert.assertNotNull(new PageListResult(withPageData, rows));
        Assert.assertNotNull(new PageListResult((com.skrstop.framework.components.core.common.response.core.IResult) success, pageData, rows));
    }

}
