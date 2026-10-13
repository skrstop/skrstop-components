package com.skrstop.framework.components.util.serialization.protobuf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 蒋时华
 */
public class ProtostuffUtilTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Bean {
        private String name;
        private Integer age;
    }

    @Test
    public void testNotSupported() {
        Assert.assertTrue(ProtostuffUtil.notSupported(Bean[].class));
        Assert.assertTrue(ProtostuffUtil.notSupported(java.util.List.class));
        Assert.assertTrue(ProtostuffUtil.notSupported(HashMap.class));
        Assert.assertTrue(ProtostuffUtil.notSupported(Thread.State.class));
        Assert.assertTrue(ProtostuffUtil.notSupported(java.util.Set.class));
        Assert.assertFalse(ProtostuffUtil.notSupported(Bean.class));
        Assert.assertFalse(ProtostuffUtil.notSupported(String.class));
    }

    @Test
    public void testPojoRoundTrip() {
        Bean bean = new Bean("tom", 18);
        byte[] bytes = ProtostuffUtil.serialize(bean);
        Bean parsed = ProtostuffUtil.deserialize(bytes, Bean.class);
        Assert.assertEquals(bean, parsed);
        // schema 缓存命中
        byte[] bytes2 = ProtostuffUtil.serialize(new Bean("jerry", 19));
        Bean parsed2 = ProtostuffUtil.deserialize(bytes2, Bean.class);
        Assert.assertEquals(new Bean("jerry", 19), parsed2);
    }

    @Test
    public void testWrapperRoundTrip() {
        // 不支持直接序列化的类型走 wrapper
        java.util.List<Bean> list = Arrays.asList(new Bean("a", 1), new Bean("b", 2));
        byte[] bytes = ProtostuffUtil.serialize(list);
        java.util.List<Bean> parsed = (java.util.List<Bean>) ProtostuffUtil.deserialize(bytes, (Class) java.util.List.class);
        Assert.assertEquals(list, parsed);

        Map<String, Bean> map = new HashMap<>();
        map.put("k", new Bean("tom", 1));
        byte[] mapBytes = ProtostuffUtil.serialize(map);
        Map<String, Bean> parsedMap = (Map<String, Bean>) ProtostuffUtil.deserialize(mapBytes, (Class) Map.class);
        Assert.assertEquals(map, parsedMap);

        byte[] strBytes = ProtostuffUtil.serialize("hello");
        String parsedStr = (String) ProtostuffUtil.deserialize(strBytes, (Class) String.class);
        Assert.assertEquals("hello", parsedStr);
    }

    @Test
    public void testCompress() throws Exception {
        Bean bean = new Bean("tom", 18);
        byte[] compressed = ProtostuffUtil.serializeAndCompress(bean);
        byte[] raw = ProtostuffUtil.uncompress(compressed);
        Assert.assertEquals(bean, ProtostuffUtil.deserialize(raw, Bean.class));

        Assert.assertTrue(ProtostuffUtil.serializeWithCompress(bean, true).length > 0);
        Assert.assertArrayEquals(ProtostuffUtil.serialize(bean), ProtostuffUtil.serializeWithCompress(bean, false));

        // 空与 null
        Assert.assertEquals(0, ProtostuffUtil.compress(null).length);
        Assert.assertEquals(0, ProtostuffUtil.compress(new byte[0]).length);
        Assert.assertEquals(0, ProtostuffUtil.uncompress(null).length);
        Assert.assertEquals(0, ProtostuffUtil.uncompress(new byte[0]).length);
    }

    @Test
    public void testStreamRoundTrip() throws Exception {
        Bean bean = new Bean("tom", 18);
        InputStream in = new ByteArrayInputStream(ProtostuffUtil.serialize(bean));
        Assert.assertEquals(bean, ProtostuffUtil.deserialize(in, Bean.class));

        // wrapper + stream
        InputStream listIn = new ByteArrayInputStream(ProtostuffUtil.serialize(Arrays.asList(bean)));
        java.util.List<Bean> parsed = (java.util.List<Bean>) ProtostuffUtil.deserialize(listIn, (Class) java.util.List.class);
        Assert.assertEquals(Arrays.asList(bean), parsed);

        // result
        InputStream resultIn = new ByteArrayInputStream(ProtostuffUtil.serialize(bean));
        Bean parsedBean = ProtostuffUtil.deserialize(resultIn, Bean.class);
        Assert.assertNotNull(parsedBean);
    }

    @Test
    public void testStreamResults() throws Exception {
        Bean bean = new Bean("tom", 18);
        com.skrstop.framework.components.core.common.response.Result<Bean> result = new com.skrstop.framework.components.core.common.response.Result<>();
        result.setData(new Bean("tom", 1));
        byte[] bytes = ProtostuffUtil.serialize(result);
        com.skrstop.framework.components.core.common.response.Result<Bean> parsed =
                ProtostuffUtil.deserializeToResult(bytes, Bean.class);
        Assert.assertEquals(new Bean("tom", 1), parsed.getData());
        InputStream resultIn = new ByteArrayInputStream(bytes);
        com.skrstop.framework.components.core.common.response.Result<Bean> parsed2 =
                ProtostuffUtil.deserializeToResult(resultIn, Bean.class);
        Assert.assertEquals(new Bean("tom", 1), parsed2.getData());

        com.skrstop.framework.components.core.common.response.DefaultResult defaultResult = new com.skrstop.framework.components.core.common.response.DefaultResult();
        defaultResult.setCode(200);
        byte[] drBytes = ProtostuffUtil.serialize(defaultResult);
        Assert.assertEquals(200.0, ((Number) ProtostuffUtil.deserializeToDefaultResult(drBytes).getCode()).doubleValue(), 0.0);
        InputStream drIn = new ByteArrayInputStream(drBytes);
        Assert.assertEquals(200.0, ((Number) ProtostuffUtil.deserializeToDefaultResult(drIn).getCode()).doubleValue(), 0.0);

        com.skrstop.framework.components.core.common.response.DefaultPageResult pageResult = new com.skrstop.framework.components.core.common.response.DefaultPageResult();
        byte[] prBytes = ProtostuffUtil.serialize(pageResult);
        Assert.assertNotNull(ProtostuffUtil.deserializeToDefaultPageResult(prBytes));
        InputStream prIn = new ByteArrayInputStream(prBytes);
        Assert.assertNotNull(ProtostuffUtil.deserializeToDefaultPageResult(prIn));

        com.skrstop.framework.components.core.common.response.PageListResult<Bean> pageListResult = new com.skrstop.framework.components.core.common.response.PageListResult<>();
        com.skrstop.framework.components.core.common.response.page.SimplePageData<java.util.List<Bean>> pageData = new com.skrstop.framework.components.core.common.response.page.SimplePageData<>();
        pageData.setRows(Arrays.asList(new Bean("a", 1)));
        pageListResult.setData(pageData);
        byte[] plBytes = ProtostuffUtil.serialize(pageListResult);
        com.skrstop.framework.components.core.common.response.PageListResult<Bean> parsedPage =
                ProtostuffUtil.deserializeToPageResult(plBytes, Bean.class);
        Assert.assertEquals(new Bean("a", 1), parsedPage.getData().getRows().get(0));
        InputStream plIn = new ByteArrayInputStream(plBytes);
        com.skrstop.framework.components.core.common.response.PageListResult<Bean> parsedPage2 =
                ProtostuffUtil.deserializeToPageListResult(plIn, Bean.class);
        Assert.assertEquals(new Bean("a", 1), parsedPage2.getData().getRows().get(0));

        // compress + stream roundtrip
        byte[] compressed = ProtostuffUtil.serializeAndCompress(bean);
        InputStream uncompressIn = new ByteArrayInputStream(compressed);
        Assert.assertEquals(bean, ProtostuffUtil.deserializeAndUncompress(uncompressIn, Bean.class));
        Assert.assertEquals(bean, ProtostuffUtil.deserializeWithUncompress(
                new ByteArrayInputStream(compressed), Bean.class, true));
        Assert.assertEquals(bean, ProtostuffUtil.deserializeWithUncompress(
                new ByteArrayInputStream(ProtostuffUtil.serialize(bean)), Bean.class, false));
    }


    @Test(timeout = 10000)
    public void testSerializeInFreshThread() throws Exception {
        // 新线程的 ThreadLocal 无 buffer → 命中分配分支
        final byte[][] holder = new byte[1][];
        final Exception[] error = new Exception[1];
        Thread thread = new Thread(() -> {
            try {
                holder[0] = ProtostuffUtil.serialize(new Bean("fresh", 1));
            } catch (Exception e) {
                error[0] = e;
            }
        });
        thread.start();
        thread.join(5000);
        Assert.assertNull(error[0]);
        Assert.assertNotNull(holder[0]);
        Assert.assertEquals(new Bean("fresh", 1), ProtostuffUtil.deserialize(holder[0], Bean.class));
    }

}
