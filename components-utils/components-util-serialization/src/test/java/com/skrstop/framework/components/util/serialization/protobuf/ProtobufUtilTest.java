package com.skrstop.framework.components.util.serialization.protobuf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @author 蒋时华
 */
public class ProtobufUtilTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Bean {
        private String name;
        private Integer age;
    }

    @Test
    public void testPojoRoundTrip() {
        Bean bean = new Bean("tom", 18);
        byte[] bytes = ProtobufUtil.serialize(bean);
        Assert.assertEquals(bean, ProtobufUtil.deserialize(bytes, Bean.class));
        // 缓存命中
        Bean bean2 = new Bean("jerry", 19);
        Assert.assertEquals(bean2, ProtobufUtil.deserialize(ProtobufUtil.serialize(bean2), Bean.class));
    }

    @Test
    public void testWrapperRoundTrip() {
        java.util.List<Bean> list = Arrays.asList(new Bean("a", 1));
        byte[] bytes = ProtobufUtil.serialize(list);
        java.util.List<Bean> parsed = (java.util.List<Bean>) ProtobufUtil.deserialize(bytes, (Class) java.util.List.class);
        Assert.assertEquals(list, parsed);
    }

    @Test
    public void testResults() throws Exception {
        com.skrstop.framework.components.core.common.response.Result<Bean> result = new com.skrstop.framework.components.core.common.response.Result<>();
        result.setData(new Bean("tom", 1));
        byte[] bytes = ProtobufUtil.serialize(result);
        Assert.assertEquals(new Bean("tom", 1), ProtobufUtil.deserializeToResult(bytes, Bean.class).getData());
        Assert.assertEquals(new Bean("tom", 1),
                ProtobufUtil.deserializeToResult(new ByteArrayInputStream(bytes), Bean.class).getData());

        com.skrstop.framework.components.core.common.response.DefaultResult defaultResult = new com.skrstop.framework.components.core.common.response.DefaultResult();
        defaultResult.setCode(500);
        byte[] drBytes = ProtobufUtil.serialize(defaultResult);
        Assert.assertEquals(500.0, ((Number) ProtobufUtil.deserializeToDefaultResult(drBytes).getCode()).doubleValue(), 0.0);
        Assert.assertEquals(500.0, ((Number) ProtobufUtil.deserializeToDefaultResult(new ByteArrayInputStream(drBytes)).getCode()).doubleValue(), 0.0);

        com.skrstop.framework.components.core.common.response.DefaultPageResult pageResult = new com.skrstop.framework.components.core.common.response.DefaultPageResult();
        byte[] prBytes = ProtobufUtil.serialize(pageResult);
        Assert.assertNotNull(ProtobufUtil.deserializeToDefaultPageResult(prBytes));
        Assert.assertNotNull(ProtobufUtil.deserializeToDefaultPageResult(new ByteArrayInputStream(prBytes)));

        com.skrstop.framework.components.core.common.response.PageListResult<Bean> pageListResult = new com.skrstop.framework.components.core.common.response.PageListResult<>();
        com.skrstop.framework.components.core.common.response.page.SimplePageData<java.util.List<Bean>> pageData = new com.skrstop.framework.components.core.common.response.page.SimplePageData<>();
        pageData.setRows(Arrays.asList(new Bean("a", 1)));
        pageListResult.setData(pageData);
        byte[] plBytes = ProtobufUtil.serialize(pageListResult);
        Assert.assertEquals(new Bean("a", 1), ProtobufUtil.deserializeToPageResult(plBytes, Bean.class).getData().getRows().get(0));
        Assert.assertEquals(new Bean("a", 1),
                ProtobufUtil.deserializeToPageListResult(new ByteArrayInputStream(plBytes), Bean.class).getData().getRows().get(0));
    }


    @Test
    public void testWrapperStream() throws Exception {
        java.util.List<Bean> list = Arrays.asList(new Bean("a", 1));
        InputStream in = new ByteArrayInputStream(ProtobufUtil.serialize(list));
        java.util.List<Bean> parsed = (java.util.List<Bean>) ProtobufUtil.deserialize(in, (Class) java.util.List.class);
        Assert.assertEquals(list, parsed);
    }

}
