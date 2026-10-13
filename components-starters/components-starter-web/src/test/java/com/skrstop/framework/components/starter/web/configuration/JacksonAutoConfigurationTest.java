package com.skrstop.framework.components.starter.web.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class JacksonAutoConfigurationTest {

    @Test
    public void testWithProps() throws Exception {
        GlobalResponseProperties props = new GlobalResponseProperties();
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        JacksonAutoConfiguration config = new JacksonAutoConfiguration(props, converters);
        Assert.assertEquals(2, converters.size());
        Assert.assertTrue(converters.get(1) instanceof MappingJackson2HttpMessageConverter);
        ObjectMapper mapper = config.getGlobalObjectMapper();
        Assert.assertNotNull(mapper);

        // long → string, date formats, NON_NULL
        String json = mapper.writeValueAsString(java.util.Collections.singletonMap("l", 123L));
        Assert.assertTrue(json.contains("\"123\""));
        String json2 = mapper.writeValueAsString(java.util.Collections.singletonMap("d", new Date(0)));
        Assert.assertTrue(json2.contains("\"1970-01-01 08:00:00\""));
        String json3 = mapper.writeValueAsString(java.util.Collections.singletonMap("c", Calendar.getInstance()));
        Assert.assertNotNull(json3);
        String json4 = mapper.writeValueAsString(java.util.Collections.singletonMap("n", null));
        Assert.assertEquals("{}", json4);
        mapper.readValue("{\"x\":1}", java.util.Map.class);
    }

    @Test
    public void testNoProps() throws Exception {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        JacksonAutoConfiguration config = new JacksonAutoConfiguration(null, converters);
        Assert.assertNotNull(config.getGlobalObjectMapper());
    }

    @Test
    public void testExistingJacksonConverter() throws Exception {
        GlobalResponseProperties props = new GlobalResponseProperties();
        props.setShowNullValue(true);
        props.setLongToString(false);
        props.setDateTimeFormat("");
        props.setDateFormat("");
        props.setTimeFormat("");
        props.setYearFormat("");
        props.setMonthDayFormat("");
        props.setYearMonthFormat("");
        MappingJackson2HttpMessageConverter existing = new MappingJackson2HttpMessageConverter();
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(existing);
        JacksonAutoConfiguration config = new JacksonAutoConfiguration(props, converters);
        Assert.assertEquals(2, converters.size());
        ObjectMapper mapper = config.getGlobalObjectMapper();
        Assert.assertNotNull(mapper);
        String json = mapper.writeValueAsString(java.util.Collections.singletonMap("n", null));
        Assert.assertTrue(json.contains("null"));
    }

    @Test
    public void testByteArrayConverterAddedFirst() {
        GlobalResponseProperties props = new GlobalResponseProperties();
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());
        new JacksonAutoConfiguration(props, converters);
        Assert.assertTrue(converters.get(0) instanceof ByteArrayHttpMessageConverter);
        Assert.assertEquals(2, converters.size());
    }
}
