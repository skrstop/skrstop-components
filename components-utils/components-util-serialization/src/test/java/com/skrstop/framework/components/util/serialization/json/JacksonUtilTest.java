package com.skrstop.framework.components.util.serialization.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

/**
 * @author 蒋时华
 */
public class JacksonUtilTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private String userName;
        private Integer age;
        private String nickName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TimeBean {
        private LocalDateTime localDateTime;
        private LocalDate localDate;
        private LocalTime localTime;
        private YearMonth yearMonth;
        private MonthDay monthDay;
        private Year year;
        private Date date;
        private Calendar calendar;
    }

    @Test
    public void testToJsonNull() {
        Assert.assertEquals("", JacksonUtil.toJson(null));
    }

    @Test
    public void testToJson() {
        User user = new User("张三", 18, null);
        String json = JacksonUtil.toJson(user);
        // NON_NULL：null 属性不输出
        Assert.assertEquals("{\"userName\":\"张三\",\"age\":18}", json);
    }

    @Test
    public void testToJsonBasic() {
        Assert.assertEquals("1", JacksonUtil.toJson(1));
        Assert.assertEquals("\"abc\"", JacksonUtil.toJson("abc"));
        Assert.assertEquals("1.5", JacksonUtil.toJson(new BigDecimal("1.5")));
    }

    @Test
    public void testFromJson() {
        User user = JacksonUtil.fromJson("{\"userName\":\"张三\",\"age\":18,\"other\":\"x\"}", User.class);
        Assert.assertEquals("张三", user.getUserName());
        Assert.assertEquals(18, user.getAge().intValue());
        // FAIL_ON_UNKNOWN_PROPERTIES=false，未知字段忽略
    }

    @Test
    public void testFromJsonStringType() {
        Assert.assertEquals("abc", JacksonUtil.fromJson("abc", String.class));
    }

    @Test
    public void testFromJsonDateType() {
        // 日期类型且非引号开头，自动补引号
        LocalDateTime localDateTime = JacksonUtil.fromJson("2025-05-27 11:38:20", LocalDateTime.class);
        Assert.assertEquals(LocalDateTime.of(2025, 5, 27, 11, 38, 20), localDateTime);

        LocalDate localDate = JacksonUtil.fromJson("2025-05-27", LocalDate.class);
        Assert.assertEquals(LocalDate.of(2025, 5, 27), localDate);

        Date date = JacksonUtil.fromJson("2025-05-27 11:38:20", Date.class);
        Assert.assertNotNull(date);
    }

    @Test
    public void testFromJsonGeneric() {
        String json = "[{\"userName\":\"a\",\"age\":1},{\"userName\":\"b\",\"age\":2}]";
        // 泛型反序列化通过 Type 构造
        java.lang.reflect.Type type = new com.fasterxml.jackson.core.type.TypeReference<List<User>>() {
        }.getType();
        List<User> users = JacksonUtil.fromJson(json, type);
        Assert.assertEquals(2, users.size());
        Assert.assertEquals("a", users.get(0).getUserName());
    }

    @Test
    public void testFromJsonToMap() {
        String json = "{\"a\":1,\"b\":\"x\",\"c\":null,\"d\":{\"e\":2},\"f\":[1,2]}";
        Map<String, String> map = JacksonUtil.fromJsonToMap(json);
        Assert.assertEquals("1", map.get("a"));
        Assert.assertEquals("x", map.get("b"));
        Assert.assertNull(map.get("c"));
        Assert.assertEquals("{\"e\":2}", map.get("d"));
        Assert.assertEquals("[1,2]", map.get("f"));
    }

    @Test
    public void testToJsonBySnakeCase() throws Exception {
        Assert.assertNull(JacksonUtil.toJsonBySnakeCase(null));
        String json = JacksonUtil.toJsonBySnakeCase(new User("张三", 18, null));
        Assert.assertEquals("{\"user_name\":\"张三\",\"age\":18}", json);
    }

    @Test
    public void testToBeanBySnakeCase() throws Exception {
        Assert.assertNull(JacksonUtil.toBeanBySnakeCase(null, User.class));
        Assert.assertNull(JacksonUtil.toBeanBySnakeCase("", User.class));
        Assert.assertNull(JacksonUtil.toBeanBySnakeCase("{\"user_name\":\"a\"}", null));

        User user = JacksonUtil.toBeanBySnakeCase("{\"user_name\":\"张三\",\"age\":18}", User.class);
        Assert.assertEquals("张三", user.getUserName());
        Assert.assertEquals(18, user.getAge().intValue());
    }

    @Test
    public void testTimeSerialize() throws Exception {
        TimeBean bean = new TimeBean();
        bean.setLocalDateTime(LocalDateTime.of(2025, 5, 27, 11, 38, 20));
        bean.setLocalDate(LocalDate.of(2025, 5, 27));
        bean.setLocalTime(LocalTime.of(11, 38, 20));
        bean.setYearMonth(YearMonth.of(2025, 5));
        bean.setMonthDay(MonthDay.of(5, 27));
        bean.setYear(Year.of(2025));
        bean.setDate(new Date(0));
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(0);
        bean.setCalendar(calendar);

        String json = JacksonUtil.toJson(bean);
        Assert.assertTrue(json.contains("\"localDateTime\":\"2025-05-27 11:38:20\""));
        Assert.assertTrue(json.contains("\"localDate\":\"2025-05-27\""));
        Assert.assertTrue(json.contains("\"localTime\":\"11:38:20\""));
        Assert.assertTrue(json.contains("\"yearMonth\":\"2025-05\""));
        Assert.assertTrue(json.contains("\"monthDay\":\"05-27\""));
        Assert.assertTrue(json.contains("\"year\":\"2025\""));
        Assert.assertTrue(json.contains("\"date\":\"1970-01-01 08:00:00\""));
        Assert.assertTrue(json.contains("\"calendar\":\"1970-01-01 08:00:00\""));

        // 反序列化回对象
        TimeBean parsed = JacksonUtil.fromJson(json, TimeBean.class);
        Assert.assertEquals(bean.getLocalDateTime(), parsed.getLocalDateTime());
        Assert.assertEquals(bean.getLocalDate(), parsed.getLocalDate());
        Assert.assertEquals(bean.getLocalTime(), parsed.getLocalTime());
        Assert.assertEquals(bean.getYearMonth(), parsed.getYearMonth());
        Assert.assertEquals(bean.getMonthDay(), parsed.getMonthDay());
        Assert.assertEquals(bean.getYear(), parsed.getYear());
        Assert.assertEquals(0, parsed.getDate().getTime());
        Assert.assertEquals(0, parsed.getCalendar().getTimeInMillis());
    }

    @Test
    public void testUseObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        boolean changed = JacksonUtil.useObjectMapper(mapper);
        Assert.assertTrue(changed);
        // 相同 mapper 不切换
        Assert.assertFalse(JacksonUtil.useObjectMapper(mapper));
        // null 不切换
        Assert.assertFalse(JacksonUtil.useObjectMapper(null));
        // 切换后 toJson 使用新 mapper（无时间模块，Date 输出时间戳）
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        Assert.assertEquals("{\"a\":1}", JacksonUtil.toJson(map));

        // 还原内置 mapper，避免影响其他用例
        try {
            java.lang.reflect.Field field = JacksonUtil.class.getDeclaredField("OBJECT_MAPPER");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 还原后时间模块生效
        Assert.assertEquals("\"2025-05-27 11:38:20\"",
                JacksonUtil.toJson(LocalDateTime.of(2025, 5, 27, 11, 38, 20)));
    }

    @Test
    public void testIsBasicType() {
        Assert.assertTrue(JacksonUtil.isBasicType(int.class));
        Assert.assertTrue(JacksonUtil.isBasicType(Integer.class));
        Assert.assertTrue(JacksonUtil.isBasicType(String.class));
        Assert.assertTrue(JacksonUtil.isBasicType(BigDecimal.class));
        Assert.assertTrue(JacksonUtil.isBasicType(LocalDateTime.class));
        Assert.assertTrue(JacksonUtil.isBasicType(Date.class));
        Assert.assertFalse(JacksonUtil.isBasicType(User.class));
    }

    @Test
    public void testIsBasicTypeDate() {
        Assert.assertTrue(JacksonUtil.isBasicTypeDate(LocalDateTime.class));
        Assert.assertTrue(JacksonUtil.isBasicTypeDate(LocalDate.class));
        Assert.assertTrue(JacksonUtil.isBasicTypeDate(LocalTime.class));
        Assert.assertTrue(JacksonUtil.isBasicTypeDate(YearMonth.class));
        Assert.assertTrue(JacksonUtil.isBasicTypeDate(MonthDay.class));
        Assert.assertTrue(JacksonUtil.isBasicTypeDate(Year.class));
        Assert.assertTrue(JacksonUtil.isBasicTypeDate(Date.class));
        Assert.assertFalse(JacksonUtil.isBasicTypeDate(String.class));
    }

    @Test
    public void testToObjectString() {
        Assert.assertNull(JacksonUtil.toObjectString(null));
        Assert.assertEquals("2025-05-27 11:38:20",
                JacksonUtil.toObjectString(LocalDateTime.of(2025, 5, 27, 11, 38, 20)));
        Assert.assertEquals("2025-05-27", JacksonUtil.toObjectString(LocalDate.of(2025, 5, 27)));
        Assert.assertEquals("11:38:20", JacksonUtil.toObjectString(LocalTime.of(11, 38, 20)));
        Assert.assertEquals("2025-05", JacksonUtil.toObjectString(YearMonth.of(2025, 5)));
        // hutool 对 MonthDay 按其 toString（--MM-dd）输出
        Assert.assertEquals("--05-27", JacksonUtil.toObjectString(MonthDay.of(5, 27)));
        Assert.assertEquals("2025", JacksonUtil.toObjectString(Year.of(2025)));
        Assert.assertEquals("abc", JacksonUtil.toObjectString("abc"));
        Assert.assertEquals("18", JacksonUtil.toObjectString(18));
    }

}
