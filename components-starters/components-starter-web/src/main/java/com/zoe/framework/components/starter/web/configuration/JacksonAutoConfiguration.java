package com.zoe.framework.components.starter.web.configuration;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zoe.framework.components.core.common.response.page.PageInfo;
import com.zoe.framework.components.starter.web.configuration.format.*;
import com.zoe.framework.components.util.value.data.StrUtil;
import lombok.Getter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * WebMvcConfig class
 *
 * @author 蒋时华
 * @date 2018/11/22
 */
@Configuration
@ConditionalOnClass({MappingJackson2HttpMessageConverter.class})
@EnableConfigurationProperties({GlobalResponseProperties.class})
public class JacksonAutoConfiguration {

    @Getter
    private ObjectMapper globalObjectMapper;

    public JacksonAutoConfiguration(GlobalResponseProperties globalResponseProperties
            , List<HttpMessageConverter<?>> converters) {
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
        SimpleModule simpleModule = new SimpleModule();
        // 序列换成json时,将所有的long变成string
        if (globalResponseProperties == null || globalResponseProperties.getLongToString()) {
            simpleModule.addSerializer(PageInfo.class, new JsonSerializer<PageInfo>() {
                @Override
                public void serialize(PageInfo value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    gen.writeRawValue(JSON.toJSONString(value));
                }
            });
            simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
            simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        }
        // LocalDateTime, 格式化
        if (globalResponseProperties == null || !StrUtil.isBlank(globalResponseProperties.getDateTimeFormat())) {
            simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(globalResponseProperties.getDateTimeFormat()));
            simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(globalResponseProperties.getDateTimeFormat()));
        }
        if (globalResponseProperties == null || !StrUtil.isBlank(globalResponseProperties.getDateFormat())) {
            simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(globalResponseProperties.getDateFormat()));
            simpleModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(globalResponseProperties.getDateFormat()));
        }
        if (globalResponseProperties == null || !StrUtil.isBlank(globalResponseProperties.getTimeFormat())) {
            simpleModule.addSerializer(LocalTime.class, new LocalTimeSerializer(globalResponseProperties.getTimeFormat()));
            simpleModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(globalResponseProperties.getTimeFormat()));
        }
        // 设置过滤掉null值得属性
        if (globalResponseProperties == null || !globalResponseProperties.getShowNullValue()) {
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        globalObjectMapper = objectMapper.registerModule(simpleModule);
        return globalObjectMapper;
    }

}
