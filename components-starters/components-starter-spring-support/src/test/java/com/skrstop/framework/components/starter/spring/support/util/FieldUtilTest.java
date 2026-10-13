package com.skrstop.framework.components.starter.spring.support.util;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @author 蒋时华
 */
public class FieldUtilTest {

    private static class Bean {
        private String name = "n";
        private int number = 1;
        private String empty = null;
        public String publicField = "pub";
    }

    @Test
    public void testGetFieldValue() throws Exception {
        Bean bean = new Bean();
        Assert.assertEquals("n", (Object) FieldUtil.getFieldValue(bean, "name"));
        // 字段不存在
        Assert.assertNull(FieldUtil.getFieldValue(bean, "missing"));
        // 3 参默认值重载（defaultValue）
        Assert.assertEquals("n", FieldUtil.getFieldValue(bean, "name", (String) null));
        Assert.assertEquals("d", FieldUtil.getFieldValue(bean, "empty", "d"));
        Assert.assertEquals("d", FieldUtil.getFieldValue(bean, "missing", "d"));
        // 3 参类型重载（fieldType）
        Field f = Bean.class.getDeclaredField("name");
        f.setAccessible(false);
        Assert.assertEquals("n", (Object) FieldUtil.<String>getFieldValue(bean, "name", String.class));
        Assert.assertEquals(Integer.valueOf(1), (Object) FieldUtil.getFieldValue(bean, "number", int.class));
        // public 字段：accessible 已为 true → 跳过 makeAccessible 与恢复
        Assert.assertEquals("pub", (Object) FieldUtil.<String>getFieldValue(bean, "publicField", String.class));
        // 2 参入口走 public 字段
        Assert.assertEquals("pub", (Object) FieldUtil.<String>getFieldValue(bean, "publicField"));
        // 私有字段访问后恢复 accessible 状态
        Assert.assertFalse(f.isAccessible());
    }

}