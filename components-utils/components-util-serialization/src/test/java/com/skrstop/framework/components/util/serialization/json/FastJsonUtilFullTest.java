package com.skrstop.framework.components.util.serialization.json;

import com.skrstop.framework.components.core.common.response.DefaultPageResult;
import com.skrstop.framework.components.core.common.response.DefaultResult;
import com.skrstop.framework.components.core.common.response.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author 蒋时华
 */
public class FastJsonUtilFullTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Bean {
        private String name;
        private Integer age;
    }

    @Test
    public void testToJson() {
        Assert.assertNull(FastJsonUtil.toJson(null));
        Assert.assertEquals("{\"name\":\"tom\"}", FastJsonUtil.toJson(new Bean("tom", null)));
        Assert.assertNull(FastJsonUtil.toJsonWithDateFormat(null, "yyyy-MM-dd"));
        Assert.assertTrue(FastJsonUtil.toJsonWithDateFormat(new Date(0L), "yyyy-MM-dd").contains("1970-01-01"));
    }

    @Test
    public void testToBean() {
        Assert.assertNull(FastJsonUtil.toBean(null, Bean.class));
        Assert.assertNull(FastJsonUtil.toBean("", Bean.class));
        Assert.assertNull(FastJsonUtil.toBean(" ", Bean.class));
        Assert.assertNull(FastJsonUtil.toBean("{\"name\":\"a\"}", null));
        Bean bean = FastJsonUtil.toBean("{\"name\":\"tom\",\"age\":18}", Bean.class);
        Assert.assertEquals("tom", bean.getName());
        Assert.assertEquals(Integer.valueOf(18), bean.getAge());
    }

    @Test
    public void testToBeanForList() {
        Assert.assertNull(FastJsonUtil.toBeanForList(null, Bean.class));
        Assert.assertNull(FastJsonUtil.toBeanForList("", Bean.class));
        Assert.assertNull(FastJsonUtil.toBeanForList("[]", null));
        List<Bean> list = FastJsonUtil.toBeanForList("[{\"name\":\"a\"},{\"name\":\"b\"}]", Bean.class);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("b", list.get(1).getName());
    }

    @Test
    public void testToBeanForListMap() {
        Assert.assertNull(FastJsonUtil.toBeanForListMap(null));
        Assert.assertNull(FastJsonUtil.toBeanForListMap(""));
        List<Map<String, String>> list = FastJsonUtil.toBeanForListMap("[{\"k\":\"1\"}]");
        Assert.assertEquals("1", list.get(0).get("k"));
        Assert.assertNull(FastJsonUtil.toBeanForListMap(null, String.class));
        Assert.assertNull(FastJsonUtil.toBeanForListMap("", String.class));
        List<Map<String, String>> list2 = FastJsonUtil.toBeanForListMap("[{\"k\":\"2\"}]", String.class);
        Assert.assertEquals("2", list2.get(0).get("k"));
    }

    @Test
    public void testToBeanForHashMap() {
        Assert.assertNull(FastJsonUtil.toBeanForHashMap(null));
        Assert.assertNull(FastJsonUtil.toBeanForHashMap(""));
        HashMap<String, String> map = FastJsonUtil.toBeanForHashMap("{\"k\":\"v\"}");
        Assert.assertEquals("v", map.get("k"));
        Assert.assertNull(FastJsonUtil.toBeanForHashMap(null, String.class));
        Assert.assertNull(FastJsonUtil.toBeanForHashMap("", String.class));
        HashMap<String, String> map2 = FastJsonUtil.toBeanForHashMap("{\"k\":\"v2\"}", String.class);
        Assert.assertEquals("v2", map2.get("k"));
    }

    @Test
    public void testResults() {
        Assert.assertNull(FastJsonUtil.toBeanForListResult(null, Bean.class));
        Assert.assertNull(FastJsonUtil.toBeanForListResult("", Bean.class));
        Assert.assertNull(FastJsonUtil.toBeanForListResult("{}", null));

        com.skrstop.framework.components.core.common.response.CollectionResult<Bean> cr =
                new com.skrstop.framework.components.core.common.response.CollectionResult<>();
        cr.setData(Arrays.asList(new Bean("a", 1)));
        String json = FastJsonUtil.toJson(cr);
        com.skrstop.framework.components.core.common.response.CollectionResult<Bean> parsed =
                FastJsonUtil.toBeanForListResult(json, Bean.class);
        Assert.assertEquals("a", parsed.getData().iterator().next().getName());
        // data null
        com.skrstop.framework.components.core.common.response.CollectionResult<Bean> empty =
                new com.skrstop.framework.components.core.common.response.CollectionResult<>();
        com.skrstop.framework.components.core.common.response.CollectionResult<Bean> parsedEmpty =
                FastJsonUtil.toBeanForListResult(FastJsonUtil.toJson(empty), Bean.class);
        Assert.assertTrue(parsedEmpty.getData() == null || parsedEmpty.getData().isEmpty());

        Assert.assertNull(FastJsonUtil.toBeanForResult(null, Bean.class));
        Assert.assertNull(FastJsonUtil.toBeanForResult("", Bean.class));
        Assert.assertNull(FastJsonUtil.toBeanForResult("{}", null));
        Result<Bean> result = new Result<>();
        result.setData(new Bean("tom", 2));
        Result<Bean> parsedResult = FastJsonUtil.toBeanForResult(FastJsonUtil.toJson(result), Bean.class);
        Assert.assertEquals("tom", parsedResult.getData().getName());
        Result<Bean> nullResult = new Result<>();
        Result<Bean> parsedNull = FastJsonUtil.toBeanForResult(FastJsonUtil.toJson(nullResult), Bean.class);
        Assert.assertNull(parsedNull.getData());

        Assert.assertNull(FastJsonUtil.toBeanForDefaultResult(null));
        Assert.assertNull(FastJsonUtil.toBeanForDefaultResult(""));
        DefaultResult dr = new DefaultResult();
        dr.setCode(500);
        Assert.assertEquals(500, FastJsonUtil.toBeanForDefaultResult(FastJsonUtil.toJson(dr)).getCode());

        Assert.assertNull(FastJsonUtil.toBeanForDefaultPageResult(null));
        Assert.assertNull(FastJsonUtil.toBeanForDefaultPageResult(""));
        DefaultPageResult dpr = new DefaultPageResult();
        DefaultPageResult parsedPage = FastJsonUtil.toBeanForDefaultPageResult(FastJsonUtil.toJson(dpr));
        Assert.assertNotNull(parsedPage);
    }


    @Test
    public void testNullJsonResults() {
        // json 为 "null" → toBean 返回 null → 提前返回
        Assert.assertNull(FastJsonUtil.toBeanForListResult("null", Bean.class));
        Assert.assertNull(FastJsonUtil.toBeanForResult("null", Bean.class));
    }


    @Test
    public void testResultDataNull() {
        // fastjson 不调用构造器 → data 为 null → 跳过二次反序列化
        com.skrstop.framework.components.core.common.response.CollectionResult<Bean> parsed =
                FastJsonUtil.toBeanForListResult("{\"code\":200,\"data\":null}", Bean.class);
        // CollectionResult 构造器初始化 data 为空集合
        Assert.assertTrue(parsed.getData() == null || parsed.getData().isEmpty());
        Result<Bean> parsedResult = FastJsonUtil.toBeanForResult("{\"code\":200}", Bean.class);
        Assert.assertNull(parsedResult.getData());
    }

}
