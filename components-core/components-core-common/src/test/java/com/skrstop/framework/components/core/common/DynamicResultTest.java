package com.skrstop.framework.components.core.common;

import com.skrstop.framework.components.core.common.response.*;
import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.SimplePageData;
import com.skrstop.framework.components.core.common.util.DynamicResult;
import com.skrstop.framework.components.core.common.util.EnumCodeUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author 蒋时华
 */
public class DynamicResultTest {

    @Test
    public void testDefaultConstructors() {
        // 覆盖工具类默认构造器
        Assert.assertNotNull(new DynamicResult());
        Assert.assertNotNull(new EnumCodeUtil());
    }

    @Test
    public void testBuildNullAndDefault() {
        Assert.assertTrue(DynamicResult.build(null) instanceof Result);
        Object plain = new Object();
        IResult result = DynamicResult.build(plain);
        Assert.assertTrue(result instanceof Result);
        Assert.assertSame(plain, ((Result) result).getData());
    }

    @Test
    public void testBuildCollections() {
        List<String> arrayList = new ArrayList<>(Arrays.asList("a"));
        Assert.assertTrue(DynamicResult.build((Object) arrayList) instanceof ListResult);

        LinkedList<String> linkedList = new LinkedList<>(Arrays.asList("a"));
        Assert.assertTrue(DynamicResult.build((Object) linkedList) instanceof ListResult);

        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("k", 1);
        Assert.assertTrue(DynamicResult.build((Object) linkedHashMap) instanceof LinkedMapResult);

        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put("k", 1);
        Assert.assertTrue(DynamicResult.build((Object) hashMap) instanceof MapResult);

        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>(Arrays.asList("a"));
        Assert.assertTrue(DynamicResult.build((Object) linkedHashSet) instanceof LinkedSetResult);

        HashSet<String> hashSet = new HashSet<>(Arrays.asList("a"));
        Assert.assertTrue(DynamicResult.build((Object) hashSet) instanceof HashSetResult);

        TreeSet<String> treeSet = new TreeSet<>(Arrays.asList("a"));
        Assert.assertTrue(DynamicResult.build((Object) treeSet) instanceof SetResult);

        // Collection 但不是 List/Set
        Collection<String> collection = Collections.unmodifiableCollection(Arrays.asList("a"));
        Assert.assertTrue(DynamicResult.build((Object) collection) instanceof CollectionResult);
    }

    @Test
    public void testBuildIResult() {
        Assert.assertTrue(DynamicResult.build((Object) ListResult.Builder.success()) instanceof ListResult);
        Assert.assertTrue(DynamicResult.build((Object) LinkedMapResult.Builder.success()) instanceof LinkedMapResult);
        Assert.assertTrue(DynamicResult.build((Object) MapResult.Builder.success()) instanceof MapResult);
        Assert.assertTrue(DynamicResult.build((Object) LinkedSetResult.Builder.success()) instanceof LinkedSetResult);
        Assert.assertTrue(DynamicResult.build((Object) HashSetResult.Builder.success()) instanceof HashSetResult);
        Assert.assertTrue(DynamicResult.build((Object) SetResult.Builder.success()) instanceof SetResult);
        Assert.assertTrue(DynamicResult.build((Object) CollectionResult.Builder.success()) instanceof CollectionResult);
        Assert.assertTrue(DynamicResult.build((Object) DefaultResult.Builder.success()) instanceof Result);
    }

    @Test
    public void testBuildPageNull() {
        Assert.assertTrue(DynamicResult.buildPage((SimplePageData) null, (Collection) null) instanceof DefaultPageResult);
        Assert.assertTrue(DynamicResult.buildPage((SimplePageData) null) instanceof DefaultPageResult);
    }

    @Test
    public void testBuildPageCollections() {
        SimplePageData pageData = new SimplePageData(1, 10, 2);
        pageData.setLastPageInfo("last");

        Assert.assertTrue(DynamicResult.buildPage(pageData, (Collection) null) instanceof DefaultPageResult);

        IPageResult r1 = DynamicResult.buildPage(pageData, new ArrayList<>(Arrays.asList("a")));
        Assert.assertTrue(r1 instanceof PageArrayListResult);

        IPageResult r2 = DynamicResult.buildPage(pageData, new LinkedList<>(Arrays.asList("a")));
        Assert.assertTrue(r2 instanceof PageLinkedListResult);

        IPageResult r3 = DynamicResult.buildPage(pageData, Arrays.asList("a"));
        Assert.assertTrue(r3 instanceof PageListResult);

        IPageResult r4 = DynamicResult.buildPage(pageData, new HashSet<>(Arrays.asList("a")));
        Assert.assertTrue(r4 instanceof PageHashSetResult);

        IPageResult r5 = DynamicResult.buildPage(pageData, new LinkedHashSet<>(Arrays.asList("a")));
        Assert.assertTrue(r5 instanceof PageLinkedSetResult);

        IPageResult r6 = DynamicResult.buildPage(pageData, new TreeSet<>(Arrays.asList("a")));
        Assert.assertTrue(r6 instanceof PageSetResult);

        IPageResult r7 = DynamicResult.buildPage(pageData, Collections.unmodifiableCollection(Arrays.asList("a")));
        Assert.assertTrue(r7 instanceof PageCollectionResult);
    }

    @Test
    public void testBuildPageRows() {
        SimplePageData pageData = new SimplePageData(1, 10, 2);
        pageData.setRows(new ArrayList<>(Arrays.asList("a")));
        Assert.assertTrue(DynamicResult.buildPage(pageData) instanceof PageArrayListResult);

        pageData.setRows(new LinkedList<>(Arrays.asList("a")));
        Assert.assertTrue(DynamicResult.buildPage(pageData) instanceof PageLinkedListResult);

        pageData.setRows(Arrays.asList("a"));
        Assert.assertTrue(DynamicResult.buildPage(pageData) instanceof PageListResult);

        pageData.setRows(new LinkedHashSet<>(Arrays.asList("a")));
        Assert.assertTrue(DynamicResult.buildPage(pageData) instanceof PageLinkedSetResult);

        pageData.setRows(new HashSet<>(Arrays.asList("a")));
        Assert.assertTrue(DynamicResult.buildPage(pageData) instanceof PageHashSetResult);

        pageData.setRows(new TreeSet<>(Arrays.asList("a")));
        Assert.assertTrue(DynamicResult.buildPage(pageData) instanceof PageSetResult);

        pageData.setRows(Collections.unmodifiableCollection(Arrays.asList("a")));
        Assert.assertTrue(DynamicResult.buildPage(pageData) instanceof PageCollectionResult);

        pageData.setRows("plain");
        IPageResult fallback = DynamicResult.buildPage(pageData);
        Assert.assertTrue(fallback instanceof PageListResult);
    }

    @Test
    public void testBuildPageResultOverload() {
        Assert.assertTrue(DynamicResult.buildPage((IPageResult) null) instanceof DefaultPageResult);

        SimplePageData pageData = new SimplePageData(1, 10, 5);
        pageData.setRows(Arrays.asList("a"));
        PageListResult pageResult = PageListResult.Builder.success(pageData);
        IPageResult returned = DynamicResult.buildPage(pageResult);
        Assert.assertSame(pageResult, returned);
        Assert.assertNotNull(returned.getData());

        // data 为 null 时补默认值
        PageListResult noData = PageListResult.Builder.success();
        IPageResult returned2 = DynamicResult.buildPage(noData);
        Assert.assertNotNull(returned2.getData());
    }

    @Test
    public void testBuildIResultEnumData() {
        IResult r = DynamicResult.build((Object) ResultTest.EnumDataResult.INSTANCE);
        Assert.assertTrue(r instanceof Result);
        Assert.assertEquals("d", ((Result) r).getData());
    }

    @Test
    public void testBuildPageLinkedHashSetBranch() {
        SimplePageData pageData = new SimplePageData(1, 10, 2);
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>(Arrays.asList("a"));
        IPageResult r = DynamicResult.buildPage(pageData, linkedHashSet);
        Assert.assertTrue(r instanceof PageLinkedSetResult);
    }

}
