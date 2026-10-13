package com.skrstop.framework.components.core.common;

import com.skrstop.framework.components.core.common.serializable.SerializableBean;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author 蒋时华
 */
public class SerializableBeanTest {

    @Getter
    @Setter
    public static class Demo extends SerializableBean {
        private String name;
        private Integer age;
        private BigDecimal amount;
        private List<String> tags;

        public Demo(String name, Integer age, BigDecimal amount, List<String> tags) {
            this.name = name;
            this.age = age;
            this.amount = amount;
            this.tags = tags;
        }
    }

    @Test
    public void testToStringVariants() {
        Demo demo = new Demo("Tom", 18, new BigDecimal("1.50"), Arrays.asList("t1"));

        // 默认输出（ignoreNull=false 语义）
        Assert.assertNotNull(demo.toString());
        Assert.assertTrue(demo.toString().contains("name"));

        Assert.assertNotNull(demo.toStringIgnoreNull());
        Assert.assertFalse(demo.toStringIgnoreNull().contains("null"));

        Assert.assertNotNull(demo.toStringPascalCase());
        Assert.assertTrue(demo.toStringPascalCase().contains("Name"));

        Assert.assertNotNull(demo.toStringCamelCase());
        Assert.assertTrue(demo.toStringCamelCase().contains("name"));

        Assert.assertNotNull(demo.toStringSnakeCase());
        Assert.assertTrue(demo.toStringSnakeCase().contains("name"));

        Assert.assertNotNull(demo.toStringKebabCase());
    }

    @Test
    public void testBytes() {
        Demo demo = new Demo("Tom", null, null, null);
        byte[] bytes = demo.bytes();
        String json = new String(bytes, StandardCharsets.UTF_8);
        Assert.assertTrue(json.contains("\"name\":\"Tom\""));
        Assert.assertTrue(json.contains("null"));
    }

    @Test
    public void testDecode() {
        Demo demo = new Demo("Tom", 18, new BigDecimal("1.50"), Arrays.asList("t1"));
        String json = demo.toString();

        Demo decoded = SerializableBean.decode(json, Demo.class);
        Assert.assertEquals("Tom", decoded.getName());
        Assert.assertEquals(Integer.valueOf(18), decoded.getAge());

        byte[] jsonBytes = demo.bytes();
        Demo decoded2 = SerializableBean.decode(jsonBytes, Demo.class);
        Assert.assertEquals("Tom", decoded2.getName());
    }

}
