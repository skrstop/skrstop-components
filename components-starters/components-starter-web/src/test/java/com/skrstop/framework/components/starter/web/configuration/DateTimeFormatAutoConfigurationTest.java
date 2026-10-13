package com.skrstop.framework.components.starter.web.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class DateTimeFormatAutoConfigurationTest {

    @Test
    public void testNullProps() {
        DateTimeFormatAutoConfiguration config = new DateTimeFormatAutoConfiguration();
        config.addFormatters(new DefaultFormattingConversionService());
    }

    @Test
    public void testAllBlank() {
        DateTimeFormatAutoConfiguration config = new DateTimeFormatAutoConfiguration();
        GlobalResponseProperties props = new GlobalResponseProperties();
        props.setDateTimeFormat("");
        props.setDateFormat("");
        props.setTimeFormat("");
        ReflectionTestUtils.setField(config, "globalResponseProperties", props);
        config.addFormatters(new DefaultFormattingConversionService());
    }

    @Test
    public void testNormal() {
        DateTimeFormatAutoConfiguration config = new DateTimeFormatAutoConfiguration();
        GlobalResponseProperties props = new GlobalResponseProperties();
        ReflectionTestUtils.setField(config, "globalResponseProperties", props);
        DefaultFormattingConversionService registry = new DefaultFormattingConversionService();
        config.addFormatters(registry);

        // string → LocalDateTime
        LocalDateTime dt = registry.convert("2024-01-02 03:04:05", LocalDateTime.class);
        Assert.assertNotNull(dt);
        LocalDate d = registry.convert("2024-01-02", LocalDate.class);
        Assert.assertNotNull(d);
        LocalTime t = registry.convert("03:04:05", LocalTime.class);
        Assert.assertNotNull(t);

        // converter string → Date
        Date date = registry.convert("2024-01-02 03:04:05", Date.class);
        Assert.assertNotNull(date);

        // converter null/empty source
        DateTimeFormatAutoConfiguration.StringToDateConverter converter =
                new DateTimeFormatAutoConfiguration.StringToDateConverter("yyyy-MM-dd HH:mm:ss");
        Assert.assertNull(converter.convert(null));
        Assert.assertNull(converter.convert(""));

        // bad format throws
        try {
            converter.convert("bad");
            Assert.fail();
        } catch (RuntimeException expected) {
        }
    }

    @Test
    public void testCustomFormatterFactory() {
        DateTimeFormatAutoConfiguration config = new DateTimeFormatAutoConfiguration();
        GlobalResponseProperties props = new GlobalResponseProperties();
        ReflectionTestUtils.setField(config, "globalResponseProperties", props);
        DateTimeFormatAutoConfiguration.CustomDateTimeFormatAnnotationFormatterFactory factory =
                new DateTimeFormatAutoConfiguration.CustomDateTimeFormatAnnotationFormatterFactory("yyyy-MM-dd HH:mm:ss");
        Assert.assertTrue(factory.getFieldTypes().contains(Date.class));
    }

    public static class AnnoHolder {
        @org.springframework.format.annotation.DateTimeFormat
        public Date styleOnly;

        @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
        public Date patternOnly;

        @org.springframework.format.annotation.DateTimeFormat(pattern = "", fallbackPatterns = {"yyyy/MM/dd"})
        public Date fallback;

        @org.springframework.format.annotation.DateTimeFormat(style = "")
        public Date styleBlank;

        @org.springframework.format.annotation.DateTimeFormat(fallbackPatterns = {"", "yyyy/MM/dd"})
        public Date blankFallback;
    }

    @Test
    public void testGetFormatter() throws Exception {
        DateTimeFormatAutoConfiguration.CustomDateTimeFormatAnnotationFormatterFactory factory =
                new DateTimeFormatAutoConfiguration.CustomDateTimeFormatAnnotationFormatterFactory("yyyy-MM-dd HH:mm:ss");

        // style 默认 "SS"，pattern 空 -> 走 defaultPattern
        org.springframework.format.annotation.DateTimeFormat a1 = AnnoHolder.class.getField("styleOnly").getAnnotation(org.springframework.format.annotation.DateTimeFormat.class);
        Object f1 = ReflectionTestUtils.invokeMethod(factory, "getFormatter", a1, Date.class);
        Assert.assertNotNull(f1);

        // pattern 非空
        org.springframework.format.annotation.DateTimeFormat a2 = AnnoHolder.class.getField("patternOnly").getAnnotation(org.springframework.format.annotation.DateTimeFormat.class);
        Object f2 = ReflectionTestUtils.invokeMethod(factory, "getFormatter", a2, Date.class);
        Assert.assertNotNull(f2);

        // style 为空 -> 不设置 style
        org.springframework.format.annotation.DateTimeFormat a3 = AnnoHolder.class.getField("styleBlank").getAnnotation(org.springframework.format.annotation.DateTimeFormat.class);
        Object f3 = ReflectionTestUtils.invokeMethod(factory, "getFormatter", a3, Date.class);
        Assert.assertNotNull(f3);

        // fallbackPatterns 非空
        org.springframework.format.annotation.DateTimeFormat a4 = AnnoHolder.class.getField("fallback").getAnnotation(org.springframework.format.annotation.DateTimeFormat.class);
        Object f4 = ReflectionTestUtils.invokeMethod(factory, "getFormatter", a4, Date.class);
        Assert.assertNotNull(f4);
    }

    @Test
    public void testAddFormattersAllFormats() {
        DateTimeFormatAutoConfiguration config = new DateTimeFormatAutoConfiguration();
        GlobalResponseProperties props = new GlobalResponseProperties();
        props.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
        props.setDateFormat("yyyy-MM-dd");
        props.setTimeFormat("HH:mm:ss");
        ReflectionTestUtils.setField(config, "globalResponseProperties", props);
        DefaultFormattingConversionService registry = new DefaultFormattingConversionService();
        config.addFormatters(registry);
        Assert.assertNotNull(registry.convert("2024-01-02 03:04:05", Date.class));
    }

    @Test
    public void testAddFormattersCombos() {
        // date 有值，dateTime/time 为空
        DateTimeFormatAutoConfiguration config = new DateTimeFormatAutoConfiguration();
        GlobalResponseProperties props = new GlobalResponseProperties();
        props.setDateTimeFormat("");
        props.setDateFormat("yyyy-MM-dd");
        props.setTimeFormat("");
        ReflectionTestUtils.setField(config, "globalResponseProperties", props);
        config.addFormatters(new DefaultFormattingConversionService());

        // time 有值，其余为空
        props.setDateTimeFormat("");
        props.setDateFormat("");
        props.setTimeFormat("HH:mm:ss");
        config.addFormatters(new DefaultFormattingConversionService());

        // dateTime + date 有值，time 为空
        props.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
        props.setDateFormat("yyyy-MM-dd");
        props.setTimeFormat("");
        config.addFormatters(new DefaultFormattingConversionService());

        // dateTime + time 有值，date 为空
        props.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
        props.setDateFormat("");
        props.setTimeFormat("HH:mm:ss");
        config.addFormatters(new DefaultFormattingConversionService());

        Assert.assertNotNull(config);
    }

    @Test
    public void testGetFormatterBlankDefaultPattern() throws Exception {
        DateTimeFormatAutoConfiguration.CustomDateTimeFormatAnnotationFormatterFactory factory =
                new DateTimeFormatAutoConfiguration.CustomDateTimeFormatAnnotationFormatterFactory("");
        org.springframework.format.annotation.DateTimeFormat a = AnnoHolder.class.getField("styleOnly").getAnnotation(org.springframework.format.annotation.DateTimeFormat.class);
        Object f = ReflectionTestUtils.invokeMethod(factory, "getFormatter", a, Date.class);
        Assert.assertNotNull(f);
    }

    @Test
    public void testGetFormatterBlankFallback() throws Exception {
        DateTimeFormatAutoConfiguration.CustomDateTimeFormatAnnotationFormatterFactory factory =
                new DateTimeFormatAutoConfiguration.CustomDateTimeFormatAnnotationFormatterFactory("yyyy-MM-dd");
        org.springframework.format.annotation.DateTimeFormat a = AnnoHolder.class.getField("blankFallback").getAnnotation(org.springframework.format.annotation.DateTimeFormat.class);
        Object f = ReflectionTestUtils.invokeMethod(factory, "getFormatter", a, Date.class);
        Assert.assertNotNull(f);
    }
}
