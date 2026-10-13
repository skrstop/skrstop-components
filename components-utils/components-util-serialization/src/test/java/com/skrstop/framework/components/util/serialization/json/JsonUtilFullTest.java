package com.skrstop.framework.components.util.serialization.json;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Set;

/**
 * @author 蒋时华
 */
public class JsonUtilFullTest {

    private static final String JSON = "{\"a\":1,\"b\":{\"c\":\"x\",\"d\":{\"e\":2}},\"arr\":[{\"f\":3},\"g\"],\"h\":[1,2]}";

    @Test
    public void testListAllPropertiesString() {
        Set<String> set = JsonUtil.listAllProperties(JSON);
        Assert.assertTrue(set.contains("a"));
        Assert.assertTrue(set.contains("b.c"));
        Assert.assertTrue(set.contains("b.d.e"));
        Assert.assertTrue(set.contains("arr[].f"));
        Assert.assertTrue(set.contains("h"));
    }

    @Test
    public void testListAllPropertiesStringNoArraySuffix() {
        Set<String> set = JsonUtil.listAllProperties(JSON, false);
        Assert.assertTrue(set.contains("arr.f"));
        // 数组元素非对象 → add(prefix)
        Assert.assertTrue(set.contains("h"));
    }

    @Test
    public void testListAllPropertiesJSONObject() {
        JSONObject obj = JSONUtil.parseObj(JSON);
        Set<String> set = JsonUtil.listAllProperties(obj);
        Assert.assertTrue(set.contains("b.c"));
        Set<String> set2 = JsonUtil.listAllProperties(obj, false);
        Assert.assertTrue(set2.contains("arr.f"));
    }

    @Test
    public void testListAllPropertiesValue() {
        LinkedHashMap<String, Object> map = JsonUtil.listAllPropertiesValue(JSON);
        Assert.assertEquals(1, map.get("a"));
        Assert.assertEquals("x", map.get("b.c"));
        Assert.assertEquals(2, map.get("b.d.e"));
        Assert.assertNotNull(map.get("arr[0].f"));

        JSONObject obj = JSONUtil.parseObj(JSON);
        LinkedHashMap<String, Object> map2 = JsonUtil.listAllPropertiesValue(obj);
        Assert.assertEquals(1, map2.get("a"));
    }

    @Test
    public void testFormat() {
        Assert.assertEquals("", JsonUtil.format(null));
        Assert.assertEquals("", JsonUtil.format(""));
        String formatted = JsonUtil.format("{\"a\":1,\"b\":[1,2]}");
        Assert.assertTrue(formatted.contains("\n"));
        Assert.assertTrue(formatted.contains("\t"));
        // 逗号前是反斜杠 → 不换行
        String escaped = JsonUtil.format(
                String.valueOf(new char[]{'{', '"', 'a', '"', ':', '"', 'x', '\\', ',', '"', 'b', '"', ':', '1', '}'}));
        Assert.assertNotNull(escaped);
        Assert.assertFalse(escaped.contains("\n\t\t\"b\""));
    }

}
