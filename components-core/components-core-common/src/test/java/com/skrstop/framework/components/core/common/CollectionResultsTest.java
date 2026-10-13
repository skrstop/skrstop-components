package com.skrstop.framework.components.core.common;

import com.skrstop.framework.components.core.common.response.*;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * 覆盖各类集合结果封装类
 *
 * @author 蒋时华
 */
public class CollectionResultsTest {

    @Test
    public void testCollectionResult() {
        CollectionResult success = CollectionResult.Builder.success();
        Assert.assertEquals(CommonResultCode.SUCCESS.getCode(), success.getCode());
        Assert.assertNull(success.getData());

        List<String> list = Arrays.asList("a", "b");
        CollectionResult<String> withData = CollectionResult.Builder.success((Collection<String>) list);
        Assert.assertEquals(list, withData.getData());

        CollectionResult error = CollectionResult.Builder.error();
        Assert.assertEquals(CommonResultCode.FAIL.getCode(), error.getCode());

        CollectionResult<String> errorData = CollectionResult.Builder.error((Collection<String>) list);
        Assert.assertEquals(list, errorData.getData());

        CollectionResult<String> copy = CollectionResult.Builder.result(withData);
        Assert.assertEquals(list, copy.getData());

        CollectionResult<String> fromIResult = CollectionResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success);
        Assert.assertEquals(success.getCode(), fromIResult.getCode());

        CollectionResult<String> merged = CollectionResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, (Collection<String>) list);
        Assert.assertEquals(list, merged.getData());

        CollectionResult empty = new CollectionResult();
        Assert.assertTrue(empty.getData().isEmpty());
        empty.setData(list);
        Assert.assertEquals(list, empty.getData());

        // copy 构造器
        CollectionResult<String> c2 = new CollectionResult<>(withData);
        Assert.assertEquals(list, c2.getData());
    }

    @Test
    public void testListResult() {
        ListResult success = ListResult.Builder.success();
        Assert.assertEquals(CommonResultCode.SUCCESS.getCode(), success.getCode());
        Assert.assertNull(success.getData());

        List<String> list = Arrays.asList("a", "b");
        ListResult<String> withData = ListResult.Builder.success(list);
        Assert.assertEquals(list, withData.getData());

        ListResult error = ListResult.Builder.error();
        Assert.assertEquals(CommonResultCode.FAIL.getCode(), error.getCode());

        ListResult<String> errorData = ListResult.Builder.error(list);
        Assert.assertEquals(list, errorData.getData());

        ListResult<String> copy = ListResult.Builder.result(withData);
        Assert.assertEquals(list, copy.getData());

        ListResult<String> fromIResult = ListResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success);
        Assert.assertEquals(success.getCode(), fromIResult.getCode());

        ListResult<String> merged = ListResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, list);
        Assert.assertEquals(list, merged.getData());

        ListResult empty = new ListResult();
        Assert.assertTrue(empty.getData().isEmpty());
        empty.setData(list);
        Assert.assertEquals(list, empty.getData());

        ListResult<String> c2 = new ListResult<>(withData);
        Assert.assertEquals(list, c2.getData());
    }

    @Test
    public void testMapResult() {
        MapResult success = MapResult.Builder.success();
        Assert.assertEquals(CommonResultCode.SUCCESS.getCode(), success.getCode());
        Assert.assertNull(success.getData());

        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        MapResult<String, Integer> withData = MapResult.Builder.success(map);
        Assert.assertEquals(map, withData.getData());

        MapResult error = MapResult.Builder.error();
        Assert.assertEquals(CommonResultCode.FAIL.getCode(), error.getCode());

        MapResult<String, Integer> errorData = MapResult.Builder.error(map);
        Assert.assertEquals(map, errorData.getData());

        MapResult<String, Integer> copy = MapResult.Builder.result(withData);
        Assert.assertEquals(map, copy.getData());

        MapResult<String, Integer> fromIResult = MapResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success);
        Assert.assertEquals(success.getCode(), fromIResult.getCode());

        MapResult<String, Integer> merged = MapResult.Builder.result((com.skrstop.framework.components.core.common.response.core.IResult) success, map);
        Assert.assertEquals(map, merged.getData());

        MapResult empty = new MapResult();
        Assert.assertTrue(empty.getData().isEmpty());
        empty.setData(map);
        Assert.assertEquals(map, empty.getData());

        MapResult<String, Integer> c2 = new MapResult<>(withData);
        Assert.assertEquals(map, c2.getData());
    }

    @Test
    public void testSetResults() {
        HashSet<String> set = new HashSet<>(Arrays.asList("a"));
        SetResult<String> setResult = SetResult.Builder.success((Set<String>) set);
        Assert.assertEquals(set, setResult.getData());
        Assert.assertNotNull(SetResult.Builder.success());
        Assert.assertEquals(CommonResultCode.FAIL.getCode(), SetResult.Builder.error().getCode());
        Assert.assertTrue(new SetResult<String>().getData().isEmpty());
        Assert.assertEquals(set, new SetResult<>(setResult).getData());

        HashSetResult<String> hashSetResult = HashSetResult.Builder.success(set);
        Assert.assertEquals(set, hashSetResult.getData());
        Assert.assertTrue(new HashSetResult<String>().getData().isEmpty());
        Assert.assertEquals(set, new HashSetResult<>(hashSetResult).getData());

        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>(Arrays.asList("a"));
        LinkedSetResult<String> linkedSetResult = LinkedSetResult.Builder.success(linkedHashSet);
        Assert.assertEquals(linkedHashSet, linkedSetResult.getData());
        Assert.assertTrue(new LinkedSetResult<String>().getData().isEmpty());
        Assert.assertEquals(linkedHashSet, new LinkedSetResult<>(linkedSetResult).getData());

        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("k", 1);
        LinkedMapResult<String, Integer> linkedMapResult = LinkedMapResult.Builder.success(linkedHashMap);
        Assert.assertEquals(linkedHashMap, linkedMapResult.getData());
        Assert.assertTrue(new LinkedMapResult<String, Integer>().getData().isEmpty());
        Assert.assertEquals(linkedHashMap, new LinkedMapResult<>(linkedMapResult).getData());
    }

    private Map<String, Integer> mapOf() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("k", 1);
        return map;
    }

}
