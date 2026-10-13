package com.skrstop.framework.components.util.serialization.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class JacksonUtilTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Bean {
        private String userName;
        private Integer age;
    }

    @Test
    public void testSnakeCaseRoundTrip() throws Exception {
        Bean bean = new Bean("tom", 18);
        String json = JacksonUtil.toJsonBySnakeCase(bean);
        Assert.assertEquals("{\"user_name\":\"tom\",\"age\":18}", json);
        Bean parsed = JacksonUtil.toBeanBySnakeCase(json, Bean.class);
        Assert.assertEquals("tom", parsed.getUserName());
        Assert.assertEquals(Integer.valueOf(18), parsed.getAge());
    }

    @Test
    public void testNullAndBlank() throws Exception {
        Assert.assertNull(JacksonUtil.toJsonBySnakeCase(null));
        Assert.assertNull(JacksonUtil.toBeanBySnakeCase(null, Bean.class));
        Assert.assertNull(JacksonUtil.toBeanBySnakeCase("", Bean.class));
        Assert.assertNull(JacksonUtil.toBeanBySnakeCase(" ", Bean.class));
        Assert.assertNull(JacksonUtil.toBeanBySnakeCase("{\"a\":1}", null));
    }


    @Test
    public void testInstantiate() {
        Assert.assertNotNull(new JacksonUtil());
    }

}
