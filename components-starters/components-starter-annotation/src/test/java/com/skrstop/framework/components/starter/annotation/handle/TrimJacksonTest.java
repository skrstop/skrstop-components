package com.skrstop.framework.components.starter.annotation.handle.trim;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skrstop.framework.components.starter.annotation.anno.trim.Trim;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class TrimJacksonTest {

    public static class Bean {
        @Trim
        @JsonProperty
        @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = TrimJacksonFormatterSerializer.class)
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class NonStringBean {
        @Trim
        @JsonProperty
        @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = TrimJacksonFormatterSerializer.class)
        private Integer num;

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }
    }

    @Test
    public void testDeserialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Bean bean = mapper.readValue("{\"name\":\"  abc  \"}", Bean.class);
        Assert.assertEquals("abc", bean.getName());
    }

    @Test
    public void testSerialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Bean bean = new Bean();
        bean.setName(" abc ");
        String json = mapper.writeValueAsString(bean);
        Assert.assertEquals("{\"name\":\"abc\"}", json);
    }

    @Test
    public void testSerializerDirect() throws Exception {
        // 直接调用 serializer：print 通道（createContextual + serialize）
        ObjectMapper mapper = new ObjectMapper();
        // 通过序列化一个带 @Trim + @JsonSerialize 的 bean 让 Jackson 完成 contextual 解析
        Bean bean = new Bean();
        bean.setName(" abc ");
        String json = mapper.writeValueAsString(bean);
        Assert.assertEquals("{\"name\":\"abc\"}", json);
    }

    @Test
    public void testDeserializeNonStringThrows() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readValue("{\"num\":1}", NonStringBean.class);
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertTrue(String.valueOf(e.getMessage()).contains("Trim"));
        }
    }

    @Test
    public void testSerializeNonStringThrows() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NonStringBean bean = new NonStringBean();
        bean.setNum(1);
        try {
            mapper.writeValueAsString(bean);
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertTrue(String.valueOf(e.getMessage()).contains("Trim"));
        }
    }

}