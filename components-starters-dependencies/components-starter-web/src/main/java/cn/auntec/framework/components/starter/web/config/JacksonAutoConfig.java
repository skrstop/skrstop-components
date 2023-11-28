package cn.auntec.framework.components.starter.web.config;

import cn.auntec.framework.components.core.common.response.page.PageInfo;
import cn.auntec.framework.components.util.value.data.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * WebMvcConfig class
 *
 * @author 蒋时华
 * @date 2018/11/22
 */
@Configuration
@ConditionalOnClass({MappingJackson2HttpMessageConverter.class})
@EnableConfigurationProperties({GlobalResponseConfig.class})
public class JacksonAutoConfig {

    @Getter
    private ObjectMapper globalObjectMapper;

    public JacksonAutoConfig(GlobalResponseConfig globalResponseConfig
            , List<HttpMessageConverter<?>> converters) {
        this.configureMessageConverters(converters, globalResponseConfig);
    }

    private void configureMessageConverters(List<HttpMessageConverter<?>> converters, GlobalResponseConfig globalResponseConfig) {

        AtomicBoolean hasJackson = new AtomicBoolean(false);
        converters.forEach(httpMessageConverter -> {
            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
                hasJackson.set(true);
                MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) httpMessageConverter;
                ObjectMapper objectMapper = jackson2HttpMessageConverter.getObjectMapper();
                jackson2HttpMessageConverter.setObjectMapper(this.setObjectMapper(objectMapper, globalResponseConfig));
            }
        });
        if (!hasJackson.get()) {
            // 新建jackson解析器
            MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
            ObjectMapper objectMapper = new ObjectMapper();
            jackson2HttpMessageConverter.setObjectMapper(this.setObjectMapper(objectMapper, globalResponseConfig));
            converters.add(jackson2HttpMessageConverter);
        }

    }

    private ObjectMapper setObjectMapper(ObjectMapper objectMapper, GlobalResponseConfig globalResponseConfig) {
        SimpleModule simpleModule = new SimpleModule();
        // 序列换成json时,将所有的long变成string
        if (globalResponseConfig == null || globalResponseConfig.getLongToString()) {
            simpleModule.addSerializer(PageInfo.class, new JsonSerializer<PageInfo>() {
                @Override
                public void serialize(PageInfo value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    gen.writeRawValue(JSON.toJSONString(value
                            , SerializerFeature.DisableCircularReferenceDetect
                    ));
                }
            });
            simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
            simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        }
        // LocalDateTime, 格式化
        if (globalResponseConfig == null || !StrUtil.isBlank(globalResponseConfig.getDateTimeFormat())) {
            simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(globalResponseConfig.getDateTimeFormat()));
            simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(globalResponseConfig.getDateTimeFormat()));
        }
        // 设置过滤掉null值得属性
        if (globalResponseConfig == null || !globalResponseConfig.getShowNullValue()) {
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        globalObjectMapper = objectMapper.registerModule(simpleModule);
        return globalObjectMapper;
    }

    static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

        private String format;

        public LocalDateTimeSerializer(String format) {
            this.format = format;
        }

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            TimeZone zone = TimeZone.getTimeZone("GMT+8");
            gen.writeString(value.format(DateTimeFormatter
                            .ofPattern(format)
                            .withZone(zone.toZoneId())
                    )
            );
        }
    }

    static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

        private String format;

        public LocalDateTimeDeserializer(String format) {
            this.format = format;
        }

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext)
                throws IOException {
            TimeZone zone = TimeZone.getTimeZone("GMT+8");
            return LocalDateTime.parse(p.getValueAsString(), DateTimeFormatter
                    .ofPattern(format)
                    .withZone(zone.toZoneId())
            );
        }
    }

}
