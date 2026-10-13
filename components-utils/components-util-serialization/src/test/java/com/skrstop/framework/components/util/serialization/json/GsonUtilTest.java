package com.skrstop.framework.components.util.serialization.json;

import com.skrstop.framework.components.core.common.response.DefaultPageResult;
import com.skrstop.framework.components.core.common.response.DefaultResult;
import com.skrstop.framework.components.core.common.response.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 蒋时华
 */
public class GsonUtilTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Bean {
        private String name;
        private Integer age;
    }

    @Test
    public void testToJsonAndBean() {
        Bean bean = new Bean("tom", 18);
        String json = GsonUtil.toJson(bean);
        Bean parsed = GsonUtil.toBean(json, Bean.class);
        Assert.assertEquals("tom", parsed.getName());
        Assert.assertEquals(Integer.valueOf(18), parsed.getAge());
    }

    @Test
    public void testList() {
        String json = "[{\"name\":\"a\",\"age\":1},{\"name\":\"b\",\"age\":2}]";
        List<Bean> list = GsonUtil.toBeanForList(json, Bean.class);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("b", list.get(1).getName());
    }

    @Test
    public void testListMap() {
        String json = "[{\"k\":1},{\"k\":2}]";
        List<Map<String, Integer>> list = GsonUtil.toBeanForListMap(json);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals(2, list.size());
        List<Map<String, Integer>> list2 = GsonUtil.toBeanForListMap(json, Integer.class);
        Assert.assertEquals(2, list2.size());
    }

    @Test
    public void testHashMap() {
        String json = "{\"k\":1}";
        HashMap<String, Integer> map = GsonUtil.toBeanForHashMap(json);
        Assert.assertEquals(1.0, ((Number) map.get("k")).doubleValue(), 0.0);
        HashMap<String, Integer> map2 = GsonUtil.toBeanForHashMap(json, Integer.class);
        Assert.assertEquals(1.0, ((Number) map2.get("k")).doubleValue(), 0.0);
    }

    @Test
    public void testResults() {
        DefaultResult defaultResult = new DefaultResult();
        defaultResult.setCode(200);
        String json = GsonUtil.toJson(defaultResult);
        DefaultResult parsed = GsonUtil.toBeanForDefaultResult(json);
        Assert.assertEquals(200.0, parsed.getCode());

        DefaultPageResult defaultPageResult = new DefaultPageResult();
        String json2 = GsonUtil.toJson(defaultPageResult);
        DefaultPageResult parsed2 = GsonUtil.toBeanForDefaultPageResult(json2);
        Assert.assertNotNull(parsed2);

        Result<Bean> beanResult = new Result<>();
        beanResult.setData(new Bean("tom", 1));
        String json3 = GsonUtil.toJson(beanResult);
        Result<Bean> parsed3 = GsonUtil.toBeanForResult(json3, Bean.class);
        Assert.assertEquals("tom", parsed3.getData().getName());

        // data 为 null 的 Result
        Result<Bean> emptyResult = new Result<>();
        String json4 = GsonUtil.toJson(emptyResult);
        Result<Bean> parsed4 = GsonUtil.toBeanForResult(json4, Bean.class);
        Assert.assertNull(parsed4.getData());
    }

    @Test
    public void testListResult() {
        com.skrstop.framework.components.core.common.response.CollectionResult<Bean> cr =
                new com.skrstop.framework.components.core.common.response.CollectionResult<>();
        cr.setData(java.util.Arrays.asList(new Bean("a", 1), new Bean("b", 2)));
        String json = GsonUtil.toJson(cr);
        com.skrstop.framework.components.core.common.response.CollectionResult<Bean> parsed =
                GsonUtil.toBeanForListResult(json, Bean.class);
        Assert.assertEquals(2, parsed.getData().size());
        Assert.assertEquals("b", new java.util.ArrayList<>((java.util.Collection<Bean>) parsed.getData()).get(1).getName());

        // data 为空集合 → 不会再次反序列化
        com.skrstop.framework.components.core.common.response.CollectionResult<Bean> empty =
                new com.skrstop.framework.components.core.common.response.CollectionResult<>();
        String json2 = GsonUtil.toJson(empty);
        com.skrstop.framework.components.core.common.response.CollectionResult<Bean> parsed2 =
                GsonUtil.toBeanForListResult(json2, Bean.class);
        Assert.assertNotNull(parsed2.getData());
        Assert.assertTrue(parsed2.getData().isEmpty());
    }


    @Test
    public void testListResultDataNull() {
        // 无 data 字段 → 跳过二次反序列化
        com.skrstop.framework.components.core.common.response.CollectionResult<Bean> parsed =
                GsonUtil.toBeanForListResult("{\"code\":200,\"data\":null}", Bean.class);
        Assert.assertNull(parsed.getData());
    }

}
