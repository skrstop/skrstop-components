package com.skrstop.framework.components.core.common;

import com.skrstop.framework.components.core.common.response.*;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.page.SimplePageData;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * 覆盖所有 Page*Result 分页结果类
 *
 * @author 蒋时华
 */
public class PageResultsTest {

    @Test
    public void testPageArrayListResult() {
        ArrayList<String> rows = new ArrayList<>(Arrays.asList("a"));
        PageArrayListResult success = PageArrayListResult.Builder.success();
        Assert.assertEquals(CommonResultCode.SUCCESS.getCode(), success.getCode());

        Assert.assertNotNull(PageArrayListResult.Builder.success(1, 10));
        Assert.assertNotNull(PageArrayListResult.Builder.success(1, 10, rows));
        Assert.assertNotNull(PageArrayListResult.Builder.success(1, 10, 100));
        Assert.assertNotNull(PageArrayListResult.Builder.success(1, 10, 100, rows));

        SimplePageData<ArrayList<String>> pageData = new SimplePageData<>(1, 10, 2);
        pageData.setRows(rows);
        Assert.assertNotNull(PageArrayListResult.Builder.success(pageData));
        Assert.assertNotNull(PageArrayListResult.Builder.success(pageData, rows));

        Assert.assertEquals(CommonResultCode.FAIL.getCode(), PageArrayListResult.Builder.error().getCode());
        Assert.assertNotNull(PageArrayListResult.Builder.error(1, 10));
        Assert.assertNotNull(PageArrayListResult.Builder.error(1, 10, rows));
        Assert.assertNotNull(PageArrayListResult.Builder.error(1, 10, 5));
        Assert.assertNotNull(PageArrayListResult.Builder.error(1, 10, 5, rows));
        Assert.assertNotNull(PageArrayListResult.Builder.error(pageData));
        Assert.assertNotNull(PageArrayListResult.Builder.error(pageData, rows));

        Assert.assertNotNull(PageArrayListResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, rows));
        Assert.assertNotNull(PageArrayListResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5));
        Assert.assertNotNull(PageArrayListResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5, rows));

        Assert.assertNull(new PageArrayListResult().getData());
        Assert.assertNotNull(new PageArrayListResult((com.skrstop.framework.components.core.common.response.core.IResult) success));
        Assert.assertNotNull(new PageArrayListResult((com.skrstop.framework.components.core.common.response.core.IResult) success, pageData));
        Assert.assertNotNull(new PageArrayListResult((com.skrstop.framework.components.core.common.response.core.IResult) success, pageData, rows));
        // pageData 为 null 时的分支
        Assert.assertNull(new PageArrayListResult((com.skrstop.framework.components.core.common.response.core.IResult) success, null, rows).getData());
        Assert.assertNotNull(new PageArrayListResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10));
        Assert.assertNotNull(new PageArrayListResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, rows));
        Assert.assertNotNull(new PageArrayListResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5));
        Assert.assertNotNull(new PageArrayListResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5, rows));

        // IPageResult 拷贝
        PageArrayListResult<String> built = PageArrayListResult.Builder.success(1, 10, rows);
        Assert.assertNotNull(PageArrayListResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IPageResult) built));
        Assert.assertNotNull(PageArrayListResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IPageResult) built, rows));
        Assert.assertNotNull(new PageArrayListResult((com.skrstop.framework.components.core.common.response.core.IPageResult) built));
        Assert.assertNotNull(new PageArrayListResult((com.skrstop.framework.components.core.common.response.core.IPageResult) built, rows));
    }

    @Test
    public void testPageCollectionResult() {
        Collection<String> rows = new ArrayList<>(Arrays.asList("a"));
        PageCollectionResult success = PageCollectionResult.Builder.success();
        Assert.assertEquals(CommonResultCode.SUCCESS.getCode(), success.getCode());

        Assert.assertNotNull(PageCollectionResult.Builder.success(1, 10));
        Assert.assertNotNull(PageCollectionResult.Builder.success(1, 10, rows));
        Assert.assertNotNull(PageCollectionResult.Builder.success(1, 10, 100));
        Assert.assertNotNull(PageCollectionResult.Builder.success(1, 10, 100, rows));

        SimplePageData<Collection<String>> pageData = new SimplePageData<>(1, 10, 2);
        pageData.setRows(rows);
        Assert.assertNotNull(PageCollectionResult.Builder.success(pageData));
        Assert.assertNotNull(PageCollectionResult.Builder.success(pageData, rows));

        Assert.assertEquals(CommonResultCode.FAIL.getCode(), PageCollectionResult.Builder.error().getCode());
        Assert.assertNotNull(PageCollectionResult.Builder.error(1, 10));
        Assert.assertNotNull(PageCollectionResult.Builder.error(1, 10, rows));
        Assert.assertNotNull(PageCollectionResult.Builder.error(1, 10, 5));
        Assert.assertNotNull(PageCollectionResult.Builder.error(1, 10, 5, rows));
        Assert.assertNotNull(PageCollectionResult.Builder.error(pageData));
        Assert.assertNotNull(PageCollectionResult.Builder.error(pageData, rows));

        Assert.assertNotNull(PageCollectionResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, rows));
        Assert.assertNotNull(PageCollectionResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5));
        Assert.assertNotNull(PageCollectionResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5, rows));

        Assert.assertNull(new PageCollectionResult().getData());
        Assert.assertNotNull(new PageCollectionResult((com.skrstop.framework.components.core.common.response.core.IResult) success));
        Assert.assertNotNull(new PageCollectionResult((com.skrstop.framework.components.core.common.response.core.IResult) success, pageData));
        Assert.assertNotNull(new PageCollectionResult((com.skrstop.framework.components.core.common.response.core.IResult) success, pageData, rows));
        // pageData 为 null 时的分支
        Assert.assertNull(new PageCollectionResult((com.skrstop.framework.components.core.common.response.core.IResult) success, null, rows).getData());
        Assert.assertNotNull(new PageCollectionResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10));
        Assert.assertNotNull(new PageCollectionResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, rows));
        Assert.assertNotNull(new PageCollectionResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5));
        Assert.assertNotNull(new PageCollectionResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5, rows));

        // IPageResult 拷贝
        PageCollectionResult<String> built = PageCollectionResult.Builder.success(1, 10, rows);
        Assert.assertNotNull(PageCollectionResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IPageResult) built));
        Assert.assertNotNull(PageCollectionResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IPageResult) built, rows));
        Assert.assertNotNull(new PageCollectionResult((com.skrstop.framework.components.core.common.response.core.IPageResult) built));
        Assert.assertNotNull(new PageCollectionResult((com.skrstop.framework.components.core.common.response.core.IPageResult) built, rows));
    }

    @Test
    public void testPageHashSetResult() {
        HashSet<String> rows = new HashSet<>(Arrays.asList("a"));
        PageHashSetResult success = PageHashSetResult.Builder.success();
        Assert.assertEquals(CommonResultCode.SUCCESS.getCode(), success.getCode());

        Assert.assertNotNull(PageHashSetResult.Builder.success(1, 10));
        Assert.assertNotNull(PageHashSetResult.Builder.success(1, 10, rows));
        Assert.assertNotNull(PageHashSetResult.Builder.success(1, 10, 100));
        Assert.assertNotNull(PageHashSetResult.Builder.success(1, 10, 100, rows));

        SimplePageData<HashSet<String>> pageData = new SimplePageData<>(1, 10, 2);
        pageData.setRows(rows);
        Assert.assertNotNull(PageHashSetResult.Builder.success(pageData));
        Assert.assertNotNull(PageHashSetResult.Builder.success(pageData, rows));

        Assert.assertEquals(CommonResultCode.FAIL.getCode(), PageHashSetResult.Builder.error().getCode());
        Assert.assertNotNull(PageHashSetResult.Builder.error(1, 10));
        Assert.assertNotNull(PageHashSetResult.Builder.error(1, 10, rows));
        Assert.assertNotNull(PageHashSetResult.Builder.error(1, 10, 5));
        Assert.assertNotNull(PageHashSetResult.Builder.error(1, 10, 5, rows));
        Assert.assertNotNull(PageHashSetResult.Builder.error(pageData));
        Assert.assertNotNull(PageHashSetResult.Builder.error(pageData, rows));

        Assert.assertNotNull(PageHashSetResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, rows));
        Assert.assertNotNull(PageHashSetResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5));
        Assert.assertNotNull(PageHashSetResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5, rows));

        Assert.assertNull(new PageHashSetResult().getData());
        Assert.assertNotNull(new PageHashSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success));
        Assert.assertNotNull(new PageHashSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, pageData));
        Assert.assertNotNull(new PageHashSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, pageData, rows));
        // pageData 为 null 时的分支
        Assert.assertNull(new PageHashSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, null, rows).getData());
        Assert.assertNotNull(new PageHashSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10));
        Assert.assertNotNull(new PageHashSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, rows));
        Assert.assertNotNull(new PageHashSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5));
        Assert.assertNotNull(new PageHashSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5, rows));

        // IPageResult 拷贝
        PageHashSetResult<String> built = PageHashSetResult.Builder.success(1, 10, rows);
        Assert.assertNotNull(PageHashSetResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IPageResult) built));
        Assert.assertNotNull(PageHashSetResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IPageResult) built, rows));
        Assert.assertNotNull(new PageHashSetResult((com.skrstop.framework.components.core.common.response.core.IPageResult) built));
        Assert.assertNotNull(new PageHashSetResult((com.skrstop.framework.components.core.common.response.core.IPageResult) built, rows));
    }

    @Test
    public void testPageLinkedListResult() {
        LinkedList<String> rows = new LinkedList<>(Arrays.asList("a"));
        PageLinkedListResult success = PageLinkedListResult.Builder.success();
        Assert.assertEquals(CommonResultCode.SUCCESS.getCode(), success.getCode());

        Assert.assertNotNull(PageLinkedListResult.Builder.success(1, 10));
        Assert.assertNotNull(PageLinkedListResult.Builder.success(1, 10, rows));
        Assert.assertNotNull(PageLinkedListResult.Builder.success(1, 10, 100));
        Assert.assertNotNull(PageLinkedListResult.Builder.success(1, 10, 100, rows));

        SimplePageData<LinkedList<String>> pageData = new SimplePageData<>(1, 10, 2);
        pageData.setRows(rows);
        Assert.assertNotNull(PageLinkedListResult.Builder.success(pageData));
        Assert.assertNotNull(PageLinkedListResult.Builder.success(pageData, rows));

        Assert.assertEquals(CommonResultCode.FAIL.getCode(), PageLinkedListResult.Builder.error().getCode());
        Assert.assertNotNull(PageLinkedListResult.Builder.error(1, 10));
        Assert.assertNotNull(PageLinkedListResult.Builder.error(1, 10, rows));
        Assert.assertNotNull(PageLinkedListResult.Builder.error(1, 10, 5));
        Assert.assertNotNull(PageLinkedListResult.Builder.error(1, 10, 5, rows));
        Assert.assertNotNull(PageLinkedListResult.Builder.error(pageData));
        Assert.assertNotNull(PageLinkedListResult.Builder.error(pageData, rows));

        Assert.assertNotNull(PageLinkedListResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, rows));
        Assert.assertNotNull(PageLinkedListResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5));
        Assert.assertNotNull(PageLinkedListResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5, rows));

        Assert.assertNull(new PageLinkedListResult().getData());
        Assert.assertNotNull(new PageLinkedListResult((com.skrstop.framework.components.core.common.response.core.IResult) success));
        Assert.assertNotNull(new PageLinkedListResult((com.skrstop.framework.components.core.common.response.core.IResult) success, pageData));
        Assert.assertNotNull(new PageLinkedListResult((com.skrstop.framework.components.core.common.response.core.IResult) success, pageData, rows));
        // pageData 为 null 时的分支
        Assert.assertNull(new PageLinkedListResult((com.skrstop.framework.components.core.common.response.core.IResult) success, null, rows).getData());
        Assert.assertNotNull(new PageLinkedListResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10));
        Assert.assertNotNull(new PageLinkedListResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, rows));
        Assert.assertNotNull(new PageLinkedListResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5));
        Assert.assertNotNull(new PageLinkedListResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5, rows));

        // IPageResult 拷贝
        PageLinkedListResult<String> built = PageLinkedListResult.Builder.success(1, 10, rows);
        Assert.assertNotNull(PageLinkedListResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IPageResult) built));
        Assert.assertNotNull(PageLinkedListResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IPageResult) built, rows));
        Assert.assertNotNull(new PageLinkedListResult((com.skrstop.framework.components.core.common.response.core.IPageResult) built));
        Assert.assertNotNull(new PageLinkedListResult((com.skrstop.framework.components.core.common.response.core.IPageResult) built, rows));
    }

    @Test
    public void testPageLinkedSetResult() {
        LinkedHashSet<String> rows = new LinkedHashSet<>(Arrays.asList("a"));
        PageLinkedSetResult success = PageLinkedSetResult.Builder.success();
        Assert.assertEquals(CommonResultCode.SUCCESS.getCode(), success.getCode());

        Assert.assertNotNull(PageLinkedSetResult.Builder.success(1, 10));
        Assert.assertNotNull(PageLinkedSetResult.Builder.success(1, 10, rows));
        Assert.assertNotNull(PageLinkedSetResult.Builder.success(1, 10, 100));
        Assert.assertNotNull(PageLinkedSetResult.Builder.success(1, 10, 100, rows));

        SimplePageData<LinkedHashSet<String>> pageData = new SimplePageData<>(1, 10, 2);
        pageData.setRows(rows);
        Assert.assertNotNull(PageLinkedSetResult.Builder.success(pageData));
        Assert.assertNotNull(PageLinkedSetResult.Builder.success(pageData, rows));

        Assert.assertEquals(CommonResultCode.FAIL.getCode(), PageLinkedSetResult.Builder.error().getCode());
        Assert.assertNotNull(PageLinkedSetResult.Builder.error(1, 10));
        Assert.assertNotNull(PageLinkedSetResult.Builder.error(1, 10, rows));
        Assert.assertNotNull(PageLinkedSetResult.Builder.error(1, 10, 5));
        Assert.assertNotNull(PageLinkedSetResult.Builder.error(1, 10, 5, rows));
        Assert.assertNotNull(PageLinkedSetResult.Builder.error(pageData));
        Assert.assertNotNull(PageLinkedSetResult.Builder.error(pageData, rows));

        Assert.assertNotNull(PageLinkedSetResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, rows));
        Assert.assertNotNull(PageLinkedSetResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5));
        Assert.assertNotNull(PageLinkedSetResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5, rows));

        Assert.assertNull(new PageLinkedSetResult().getData());
        Assert.assertNotNull(new PageLinkedSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success));
        Assert.assertNotNull(new PageLinkedSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, pageData));
        Assert.assertNotNull(new PageLinkedSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, pageData, rows));
        // pageData 为 null 时的分支
        Assert.assertNull(new PageLinkedSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, null, rows).getData());
        Assert.assertNotNull(new PageLinkedSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10));
        Assert.assertNotNull(new PageLinkedSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, rows));
        Assert.assertNotNull(new PageLinkedSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5));
        Assert.assertNotNull(new PageLinkedSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5, rows));

        // IPageResult 拷贝
        PageLinkedSetResult<String> built = PageLinkedSetResult.Builder.success(1, 10, rows);
        Assert.assertNotNull(PageLinkedSetResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IPageResult) built));
        Assert.assertNotNull(PageLinkedSetResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IPageResult) built, rows));
        Assert.assertNotNull(new PageLinkedSetResult((com.skrstop.framework.components.core.common.response.core.IPageResult) built));
        Assert.assertNotNull(new PageLinkedSetResult((com.skrstop.framework.components.core.common.response.core.IPageResult) built, rows));
    }

    @Test
    public void testPageSetResult() {
        Set<String> rows = new HashSet<>(Arrays.asList("a"));
        PageSetResult success = PageSetResult.Builder.success();
        Assert.assertEquals(CommonResultCode.SUCCESS.getCode(), success.getCode());

        Assert.assertNotNull(PageSetResult.Builder.success(1, 10));
        Assert.assertNotNull(PageSetResult.Builder.success(1, 10, rows));
        Assert.assertNotNull(PageSetResult.Builder.success(1, 10, 100));
        Assert.assertNotNull(PageSetResult.Builder.success(1, 10, 100, rows));

        SimplePageData<Set<String>> pageData = new SimplePageData<>(1, 10, 2);
        pageData.setRows(rows);
        Assert.assertNotNull(PageSetResult.Builder.success(pageData));
        Assert.assertNotNull(PageSetResult.Builder.success(pageData, rows));

        Assert.assertEquals(CommonResultCode.FAIL.getCode(), PageSetResult.Builder.error().getCode());
        Assert.assertNotNull(PageSetResult.Builder.error(1, 10));
        Assert.assertNotNull(PageSetResult.Builder.error(1, 10, rows));
        Assert.assertNotNull(PageSetResult.Builder.error(1, 10, 5));
        Assert.assertNotNull(PageSetResult.Builder.error(1, 10, 5, rows));
        Assert.assertNotNull(PageSetResult.Builder.error(pageData));
        Assert.assertNotNull(PageSetResult.Builder.error(pageData, rows));

        Assert.assertNotNull(PageSetResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, rows));
        Assert.assertNotNull(PageSetResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5));
        Assert.assertNotNull(PageSetResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5, rows));

        Assert.assertNull(new PageSetResult().getData());
        Assert.assertNotNull(new PageSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success));
        Assert.assertNotNull(new PageSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, pageData));
        Assert.assertNotNull(new PageSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, pageData, rows));
        // pageData 为 null 时的分支
        Assert.assertNull(new PageSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, null, rows).getData());
        Assert.assertNotNull(new PageSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10));
        Assert.assertNotNull(new PageSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, rows));
        Assert.assertNotNull(new PageSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5));
        Assert.assertNotNull(new PageSetResult((com.skrstop.framework.components.core.common.response.core.IResult) success, 1, 10, 5, rows));

        // IPageResult 拷贝
        PageSetResult<String> built = PageSetResult.Builder.success(1, 10, rows);
        Assert.assertNotNull(PageSetResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IPageResult) built));
        Assert.assertNotNull(PageSetResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IPageResult) built, rows));
        Assert.assertNotNull(new PageSetResult((com.skrstop.framework.components.core.common.response.core.IPageResult) built));
        Assert.assertNotNull(new PageSetResult((com.skrstop.framework.components.core.common.response.core.IPageResult) built, rows));
    }
}
