package com.skrstop.framework.components.util.serialization.format.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Assert;
import org.junit.Test;

import java.time.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 蒋时华
 */
public class JacksonFormatTest {

    private ObjectMapper mapper(SimpleModule module) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        return mapper;
    }

    @Test
    public void testDate() throws Exception {
        ObjectMapper mapper = mapper(new SimpleModule()
                .addSerializer(Date.class, new DateSerializer("yyyy-MM-dd HH:mm:ss"))
                .addDeserializer(Date.class, new DateDeserializer("yyyy-MM-dd HH:mm:ss")));
        Date date = SimpleDateFormatHolder.parse("2024-01-02 03:04:05");
        String json = mapper.writeValueAsString(date);
        Assert.assertEquals("\"2024-01-02 03:04:05\"", json);
        Date parsed = mapper.readValue(json, Date.class);
        Assert.assertEquals(date.getTime() / 1000, parsed.getTime() / 1000);
    }

    @Test
    public void testCalendar() throws Exception {
        ObjectMapper mapper = mapper(new SimpleModule()
                .addSerializer(Calendar.class, new CalendarSerializer("yyyy-MM-dd"))
                .addDeserializer(Calendar.class, new CalendarDeserializer("yyyy-MM-dd")));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(SimpleDateFormatHolder.parse("2024-05-06 00:00:00"));
        String json = mapper.writeValueAsString(calendar);
        Assert.assertEquals("\"2024-05-06\"", json);
        Calendar parsed = mapper.readValue(json, Calendar.class);
        Assert.assertEquals(2024, parsed.get(Calendar.YEAR));
        Assert.assertEquals(4, parsed.get(Calendar.MONTH));
    }

    @Test
    public void testLocalDateTime() throws Exception {
        ObjectMapper mapper = mapper(new SimpleModule()
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer("yyyy/MM/dd HH:mm:ss"))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer("yyyy/MM/dd HH:mm:ss")));
        LocalDateTime value = LocalDateTime.of(2024, 1, 2, 3, 4, 5);
        String json = mapper.writeValueAsString(value);
        Assert.assertEquals("\"2024/01/02 03:04:05\"", json);
        Assert.assertEquals(value, mapper.readValue(json, LocalDateTime.class));
    }

    @Test
    public void testLocalDate() throws Exception {
        ObjectMapper mapper = mapper(new SimpleModule()
                .addSerializer(LocalDate.class, new LocalDateSerializer("yyyy*MM*dd"))
                .addDeserializer(LocalDate.class, new LocalDateDeserializer("yyyy*MM*dd")));
        LocalDate value = LocalDate.of(2024, 1, 2);
        String json = mapper.writeValueAsString(value);
        Assert.assertEquals("\"2024*01*02\"", json);
        Assert.assertEquals(value, mapper.readValue(json, LocalDate.class));
    }

    @Test
    public void testLocalTime() throws Exception {
        ObjectMapper mapper = mapper(new SimpleModule()
                .addSerializer(LocalTime.class, new LocalTimeSerializer("HH-mm-ss"))
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer("HH-mm-ss")));
        LocalTime value = LocalTime.of(3, 4, 5);
        String json = mapper.writeValueAsString(value);
        Assert.assertEquals("\"03-04-05\"", json);
        Assert.assertEquals(value, mapper.readValue(json, LocalTime.class));
    }

    @Test
    public void testYear() throws Exception {
        ObjectMapper mapper = mapper(new SimpleModule()
                .addSerializer(Year.class, new YearSerializer("yyyy年"))
                .addDeserializer(Year.class, new YearDeserializer("yyyy年")));
        String json = mapper.writeValueAsString(Year.of(2024));
        Assert.assertEquals("\"2024年\"", json);
        Assert.assertEquals(Year.of(2024), mapper.readValue(json, Year.class));
    }

    @Test
    public void testYearMonth() throws Exception {
        ObjectMapper mapper = mapper(new SimpleModule()
                .addSerializer(YearMonth.class, new YearMonthSerializer("yyyy/MM"))
                .addDeserializer(YearMonth.class, new YearMonthDeserializer("yyyy/MM")));
        YearMonth value = YearMonth.of(2024, 5);
        String json = mapper.writeValueAsString(value);
        Assert.assertEquals("\"2024/05\"", json);
        Assert.assertEquals(value, mapper.readValue(json, YearMonth.class));
    }

    @Test
    public void testMonthDay() throws Exception {
        ObjectMapper mapper = mapper(new SimpleModule()
                .addSerializer(MonthDay.class, new MonthDaySerializer("MM月dd日"))
                .addDeserializer(MonthDay.class, new MonthDayDeserializer("MM月dd日")));
        MonthDay value = MonthDay.of(5, 6);
        String json = mapper.writeValueAsString(value);
        Assert.assertEquals("\"05月06日\"", json);
        Assert.assertEquals(value, mapper.readValue(json, MonthDay.class));
    }

    private static class SimpleDateFormatHolder {
        static java.util.Date parse(String s) {
            try {
                return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
