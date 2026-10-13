package com.skrstop.framework.components.starter.redis.configuration.common;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import com.skrstop.framework.components.util.serialization.format.fastjson.*;
import com.skrstop.framework.components.util.value.data.ArrayUtil;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * JsonRedisTemplate class
 *
 * @author 蒋时华
 * @date 2019/8/9
 */
@SuppressWarnings("all")
public class FastJsonRedisTemplate extends StringRedisTemplate {

    private final GlobalRedisProperties globalRedisProperties;

    public FastJsonRedisTemplate(RedisConnectionFactory connectionFactory
            , GlobalRedisProperties globalRedisProperties) {
        this.globalRedisProperties = globalRedisProperties;
        this.setConnectionFactory(connectionFactory);
        this.afterPropertiesSet();
        RedisSerializer redisSerializer = this.getFastJsonRedisSerializer();
        this.setEnableDefaultSerializer(true);
        this.setDefaultSerializer(redisSerializer);
        this.setKeySerializer(new StringRedisSerializer());
        this.setValueSerializer(redisSerializer);
        this.setHashKeySerializer(new StringRedisSerializer());
        this.setHashValueSerializer(redisSerializer);
        this.setStringSerializer(new StringRedisSerializer());
    }

    private CustomFastJsonRedisSerializer getFastJsonRedisSerializer() {
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);
        /**
         * 主要源码点：
         * {@link com.alibaba.fastjson.serializer.JSONSerializer.fastJsonConfigDateFormatPattern}
         * {@link com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec}
         */
        fastJsonConfig.setDateFormat(globalRedisProperties.getDatetimeFormat());
        // 序列化
        ArrayList<JSONWriter.Feature> serializerFeatures = new ArrayList<>();
        serializerFeatures.add(JSONWriter.Feature.WriteMapNullValue);
        serializerFeatures.add(JSONWriter.Feature.WriteNonStringKeyAsString);
        if (this.globalRedisProperties.isFastjsonPrettyFormatJson()) {
            serializerFeatures.add(JSONWriter.Feature.PrettyFormat);
        }
        if (!this.globalRedisProperties.isFastjsonSafeMode() && this.globalRedisProperties.isFastjsonAutoType()) {
            serializerFeatures.add(JSONWriter.Feature.WriteClassName);
        } else {
            serializerFeatures.add(JSONWriter.Feature.NotWriteRootClassName);
        }
        fastJsonConfig.setWriterFeatures(ArrayUtil.toArray(serializerFeatures, JSONWriter.Feature.class));
        // 反序列化
        ArrayList<JSONReader.Feature> features = new ArrayList<>();

        if (!(!this.globalRedisProperties.isFastjsonSafeMode() && this.globalRedisProperties.isFastjsonAutoType())) {
            features.add(JSONReader.Feature.IgnoreAutoTypeNotMatch);
        }
        fastJsonConfig.setReaderFeatures(ArrayUtil.toArray(features, JSONReader.Feature.class));
        CustomFastJsonRedisSerializer fastJsonRedisSerializer = new CustomFastJsonRedisSerializer(
                Object.class
                , fastJsonConfig
                , globalRedisProperties.getDatetimeFormat()
                , globalRedisProperties.getDateFormat()
                , globalRedisProperties.getTimeFormat());
        return fastJsonRedisSerializer;
    }

    private static class CustomFastJsonRedisSerializer<T> implements RedisSerializer<T> {
        private final FastJsonConfig config;
        private final Class<T> type;
        private final String datetimeFormat;
        private final String dateFormat;
        private final String timeFormat;
        private final JSONWriter.Context writerContext;
        private final JSONReader.Context readerContext;

        public CustomFastJsonRedisSerializer(Class<T> type, FastJsonConfig fastJsonConfig, String datetimeFormat, String dateFormat, String timeFormat) {
            this.type = type;
            this.config = fastJsonConfig;
            this.datetimeFormat = datetimeFormat;
            this.dateFormat = dateFormat;
            this.timeFormat = timeFormat;
            // 初始化
            this.writerContext = new JSONWriter.Context(this.getWriter(), this.config.getWriterFeatures());
            this.writerContext.setDateFormat(datetimeFormat);
            this.writerContext.configFilter(this.config.getWriterFilters());
            this.readerContext = new JSONReader.Context(this.getReader(), this.config.getReaderFeatures());
            this.readerContext.setDateFormat(dateFormat);
            this.readerContext.config(this.config.getReaderFilters());
        }

        private ObjectWriterProvider getWriter() {
            ObjectWriterProvider defaultObjectWriterProvider = new ObjectWriterProvider();
            defaultObjectWriterProvider.register(Date.class, new DateObjectWriter(datetimeFormat));
            defaultObjectWriterProvider.register(LocalDateTime.class, new LocalDateTimeObjectWriter(datetimeFormat));
            defaultObjectWriterProvider.register(LocalDate.class, new LocalDateObjectWriter(dateFormat));
            defaultObjectWriterProvider.register(LocalTime.class, new LocalTimeObjectWriter(timeFormat));
            return defaultObjectWriterProvider;
        }

        private ObjectReaderProvider getReader() {
            ObjectReaderProvider objectReaderProvider = new ObjectReaderProvider();
            objectReaderProvider.register(Date.class, new DateObjectReader(datetimeFormat));
            objectReaderProvider.register(LocalDateTime.class, new LocalDateTimeObjectReader(datetimeFormat));
            objectReaderProvider.register(LocalDate.class, new LocalDateObjectReader(dateFormat));
            objectReaderProvider.register(LocalTime.class, new LocalTimeObjectReader(timeFormat));
            return objectReaderProvider;
        }

        public byte[] serialize(T t) throws SerializationException {
            if (t == null) {
                return new byte[0];
            } else {
                try {
                    try (JSONWriter writer = JSONWriter.ofUTF8(writerContext)) {
                        writer.setRootObject(t);
                        Class<?> valueClass = t.getClass();
                        ObjectWriter<?> objectWriter = writerContext.getObjectWriter(valueClass, valueClass);
                        objectWriter.write(writer, t, null, null, 0);
                        return writer.getBytes();
                    }
                } catch (Exception ex) {
                    throw new SerializationException("Could not serialize: " + ex.getMessage(), ex);
                }
            }
        }

        public T deserialize(byte[] bytes) throws SerializationException {
            if (bytes != null && bytes.length != 0) {
                try {
                    return (T) JSON.parseObject(bytes, this.type, this.config.getDateFormat(), this.config.getReaderFilters(), this.config.getReaderFeatures());
                } catch (Exception ex) {
                    throw new SerializationException("Could not deserialize: " + ex.getMessage(), ex);
                }
            } else {
                return null;
            }
        }

    }

}
