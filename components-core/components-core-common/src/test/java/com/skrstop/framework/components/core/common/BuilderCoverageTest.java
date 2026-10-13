package com.skrstop.framework.components.core.common;

import com.skrstop.framework.components.core.common.response.*;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.page.SimplePageData;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * 手写 Builder 的静态工厂与隐式构造器补覆盖
 */
public class BuilderCoverageTest {

    @Test
    public void testSimpleBuildersInit() {
        // 隐式构造器覆盖
        Assert.assertNotNull(new Result.Builder());
        Assert.assertNotNull(new ListResult.Builder());
        Assert.assertNotNull(new SetResult.Builder());
        Assert.assertNotNull(new HashSetResult.Builder());
        Assert.assertNotNull(new LinkedSetResult.Builder());
        Assert.assertNotNull(new MapResult.Builder());
        Assert.assertNotNull(new LinkedMapResult.Builder());
        Assert.assertNotNull(new CollectionResult.Builder());
        Assert.assertNotNull(new DefaultResult.Builder());
        Assert.assertNotNull(new DefaultPageResult.Builder());
        Assert.assertNotNull(new PageListResult.Builder());
        Assert.assertNotNull(new PageCollectionResult.Builder());
        Assert.assertNotNull(new PageSetResult.Builder());
        Assert.assertNotNull(new PageHashSetResult.Builder());
        Assert.assertNotNull(new PageLinkedSetResult.Builder());
        Assert.assertNotNull(new PageArrayListResult.Builder());
        Assert.assertNotNull(new PageLinkedListResult.Builder());
    }

    @Test
    public void testSetBuilders() {
        Set<String> set = new LinkedHashSet<>();
        set.add("a");
        java.util.HashSet<String> hashSet = new java.util.HashSet<>();
        hashSet.add("h");
        java.util.LinkedHashSet<String> linkedHashSet = new java.util.LinkedHashSet<>();
        linkedHashSet.add("l");
        Assert.assertNotNull(SetResult.Builder.error());
        Assert.assertNotNull(SetResult.Builder.error(set));
        Assert.assertNotNull(SetResult.Builder.result(new SetResult<>(CommonResultCode.SUCCESS)));
        Assert.assertNotNull(SetResult.Builder.result(CommonResultCode.SUCCESS, set));
        Assert.assertNotNull(SetResult.Builder.result(CommonResultCode.SUCCESS));

        Assert.assertNotNull(HashSetResult.Builder.error());
        Assert.assertNotNull(HashSetResult.Builder.error(hashSet));
        Assert.assertNotNull(HashSetResult.Builder.result((com.skrstop.framework.components.core.common.response.core.ISetResult<String>) new SetResult<String>(CommonResultCode.SUCCESS)));
        Assert.assertNotNull(HashSetResult.Builder.result(CommonResultCode.SUCCESS, hashSet));
        Assert.assertNotNull(HashSetResult.Builder.result(CommonResultCode.SUCCESS));

        Assert.assertNotNull(LinkedSetResult.Builder.error());
        Assert.assertNotNull(LinkedSetResult.Builder.error(linkedHashSet));
        Assert.assertNotNull(LinkedSetResult.Builder.result((com.skrstop.framework.components.core.common.response.core.ISetResult<String>) new SetResult<String>(CommonResultCode.SUCCESS)));
        Assert.assertNotNull(LinkedSetResult.Builder.result(CommonResultCode.SUCCESS, linkedHashSet));
        Assert.assertNotNull(LinkedSetResult.Builder.result(CommonResultCode.SUCCESS));
    }

    @Test
    public void testLinkedMapBuilders() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("a", "b");
        Assert.assertNotNull(LinkedMapResult.Builder.error());
        Assert.assertNotNull(LinkedMapResult.Builder.error(map));
        Assert.assertNotNull(LinkedMapResult.Builder.result(new LinkedMapResult<>(CommonResultCode.SUCCESS)));
        Assert.assertNotNull(LinkedMapResult.Builder.result(CommonResultCode.SUCCESS, map));
        Assert.assertNotNull(LinkedMapResult.Builder.result(CommonResultCode.SUCCESS));
    }

    @Test
    public void testPageBuilders() {
        List<String> list = new ArrayList<>();
        list.add("a");
        // PageListResult.error(pageNumber, pageSize, list)
        Assert.assertNotNull(PageListResult.Builder.error(1, 10, list));

        // result(IResult, SimplePageData) 与 result(IResult, SimplePageData, data)，各页结果类泛型不同
        java.util.HashSet<String> hashSet = new java.util.HashSet<>();
        hashSet.add("x");
        SimplePageData<Set<String>> setPageData = new SimplePageData<>();
        setPageData.setRows(hashSet);
        Assert.assertNotNull(PageSetResult.Builder.result(CommonResultCode.SUCCESS, setPageData));
        Assert.assertNotNull(PageSetResult.Builder.result(CommonResultCode.SUCCESS, setPageData, hashSet));

        SimplePageData<java.util.HashSet<String>> hashSetPageData = new SimplePageData<>();
        hashSetPageData.setRows(hashSet);
        Assert.assertNotNull(PageHashSetResult.Builder.result(CommonResultCode.SUCCESS, hashSetPageData));
        Assert.assertNotNull(PageHashSetResult.Builder.result(CommonResultCode.SUCCESS, hashSetPageData, hashSet));

        SimplePageData<java.util.LinkedHashSet<String>> linkedSetPageData = new SimplePageData<>();
        linkedSetPageData.setRows(new java.util.LinkedHashSet<>());
        Assert.assertNotNull(PageLinkedSetResult.Builder.result(CommonResultCode.SUCCESS, linkedSetPageData));
        Assert.assertNotNull(PageLinkedSetResult.Builder.result(CommonResultCode.SUCCESS, linkedSetPageData, new java.util.LinkedHashSet<>()));

        SimplePageData<Collection<String>> collectionPageData = new SimplePageData<>();
        collectionPageData.setRows(list);
        Assert.assertNotNull(PageCollectionResult.Builder.result(CommonResultCode.SUCCESS, collectionPageData));
        Assert.assertNotNull(PageCollectionResult.Builder.result(CommonResultCode.SUCCESS, collectionPageData, list));

        SimplePageData<java.util.ArrayList<String>> arrayListPageData = new SimplePageData<>();
        arrayListPageData.setRows(new java.util.ArrayList<>());
        Assert.assertNotNull(PageArrayListResult.Builder.result(CommonResultCode.SUCCESS, arrayListPageData));
        Assert.assertNotNull(PageArrayListResult.Builder.result(CommonResultCode.SUCCESS, arrayListPageData, new java.util.ArrayList<>()));

        SimplePageData<java.util.LinkedList<String>> linkedListPageData = new SimplePageData<>();
        linkedListPageData.setRows(new java.util.LinkedList<>());
        Assert.assertNotNull(PageLinkedListResult.Builder.result(CommonResultCode.SUCCESS, linkedListPageData));
        Assert.assertNotNull(PageLinkedListResult.Builder.result(CommonResultCode.SUCCESS, linkedListPageData, new java.util.LinkedList<>()));
    }

}
