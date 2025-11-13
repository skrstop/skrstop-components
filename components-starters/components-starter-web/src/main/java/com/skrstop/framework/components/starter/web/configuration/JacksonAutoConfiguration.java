package com.skrstop.framework.components.starter.web.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.skrstop.framework.components.util.serialization.format.jackson.*;
import com.skrstop.framework.components.util.value.data.StrUtil;
import lombok.Getter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * WebMvcConfig class
 *
 * @author 蒋时华
 * @date 2018/11/22
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnClass({MappingJackson2HttpMessageConverter.class})
@EnableConfigurationProperties({GlobalResponseProperties.class})
public class JacksonAutoConfiguration {

    @Getter
    private ObjectMapper globalObjectMapper;

    public JacksonAutoConfiguration(GlobalResponseProperties globalResponseProperties
            , List<HttpMessageConverter<?>> converters) {
        converters.add(0, new ByteArrayHttpMessageConverter());
        this.configureMessageConverters(converters, globalResponseProperties);
    }

    private void configureMessageConverters(List<HttpMessageConverter<?>> converters, GlobalResponseProperties globalResponseProperties) {

        AtomicBoolean hasJackson = new AtomicBoolean(false);
        converters.forEach(httpMessageConverter -> {
            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
                hasJackson.set(true);
                MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) httpMessageConverter;
                ObjectMapper objectMapper = jackson2HttpMessageConverter.getObjectMapper();
                jackson2HttpMessageConverter.setObjectMapper(this.setObjectMapper(objectMapper, globalResponseProperties));
            }
        });
        if (!hasJackson.get()) {
            // 新建jackson解析器
            MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
            ObjectMapper objectMapper = new ObjectMapper();
            jackson2HttpMessageConverter.setObjectMapper(this.setObjectMapper(objectMapper, globalResponseProperties));
            converters.add(jackson2HttpMessageConverter);
        }

    }

    private ObjectMapper setObjectMapper(ObjectMapper objectMapper, GlobalResponseProperties globalResponseProperties) {
        SimpleModule simpleModule = new SimpleModule("skrstop-web-jackson-module");
        // 序列换成json时,将所有的long变成string
        if (globalResponseProperties == null || globalResponseProperties.isLongToString()) {
//            simpleModule.addSerializer(PageData.class, new JsonSerializer<PageData>() {
//                @Override
//                public void serialize(PageData value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//                    gen.writeRawValue(JSON.toJSONString(value, JSONWriter.Feature.WriteLongAsString));
//                }
//            });
            simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
            simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        }
        // 时间格式化
        if (globalResponseProperties != null && StrUtil.isNotBlank(globalResponseProperties.getDateTimeFormat())) {
            simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(globalResponseProperties.getDateTimeFormat()));
            simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(globalResponseProperties.getDateTimeFormat()));
            simpleModule.addSerializer(Date.class, new DateSerializer(globalResponseProperties.getDateTimeFormat()));
            simpleModule.addDeserializer(Date.class, new DateDeserializer(globalResponseProperties.getDateTimeFormat()));
            simpleModule.addSerializer(Calendar.class, new CalendarSerializer(globalResponseProperties.getDateTimeFormat()));
            simpleModule.addDeserializer(Calendar.class, new CalendarDeserializer(globalResponseProperties.getDateTimeFormat()));
        }
        if (globalResponseProperties != null && StrUtil.isNotBlank(globalResponseProperties.getDateFormat())) {
            simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(globalResponseProperties.getDateFormat()));
            simpleModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(globalResponseProperties.getDateFormat()));
        }
        if (globalResponseProperties != null && StrUtil.isNotBlank(globalResponseProperties.getTimeFormat())) {
            simpleModule.addSerializer(LocalTime.class, new LocalTimeSerializer(globalResponseProperties.getTimeFormat()));
            simpleModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(globalResponseProperties.getTimeFormat()));
        }
        if (globalResponseProperties != null && StrUtil.isNotBlank(globalResponseProperties.getYearFormat())) {
            simpleModule.addSerializer(Year.class, new YearSerializer(globalResponseProperties.getYearFormat()));
            simpleModule.addDeserializer(Year.class, new YearDeserializer(globalResponseProperties.getYearFormat()));
        }
        if (globalResponseProperties != null && StrUtil.isNotBlank(globalResponseProperties.getMonthDayFormat())) {
            simpleModule.addSerializer(MonthDay.class, new MonthDaySerializer(globalResponseProperties.getMonthDayFormat()));
            simpleModule.addDeserializer(MonthDay.class, new MonthDayDeserializer(globalResponseProperties.getMonthDayFormat()));
        }
        if (globalResponseProperties != null && StrUtil.isNotBlank(globalResponseProperties.getYearMonthFormat())) {
            simpleModule.addSerializer(YearMonth.class, new YearMonthSerializer(globalResponseProperties.getYearMonthFormat()));
            simpleModule.addDeserializer(YearMonth.class, new YearMonthDeserializer(globalResponseProperties.getYearMonthFormat()));
        }
        // 设置过滤掉null值得属性
        if (globalResponseProperties == null || !globalResponseProperties.isShowNullValue()) {
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        globalObjectMapper = objectMapper.registerModule(simpleModule);
        globalObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return globalObjectMapper;
    }

}
