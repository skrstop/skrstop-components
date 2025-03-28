package com.skrstop.framework.components.starter.redis.configuration.common;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONB;
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

    private void transaction() {
        // 事务
        /**
         * redis最好的事务方式还是用它自己的watch 读数据，然后再用multi进行锁定，最后用exec执行，如果成功返回[null,true]，如果失败返回操作结果[结果,false]
         * redis的事务很容易与mysql数据库的事务混在一起，尽量不要打开。默认redis的事务是关闭的。非要打开的可以
         * template.setEnableTransactionSupport(true);
         */
        /**
         * 坑：
         * 打开事务支持的template,只是解绑了连接，根本没有做close的操作。关于什么是解绑，其实这个方法的注释中已经说的比较清楚了，
         * 对于开启了事务的Template，由于已经绑定了线程中连接，所以这里是不会关闭的，只是做了解绑的操作。
         * 到这里原因就很清楚了，就是只要template开启了事务支持，spring就认为只要使用这个template就会包含在事务当中，
         * 因为一个事务中的操作必须在同一个连接中完成，
         * 所以在每次get/set之后，template是不会关闭链接的，因为它不知道事务有没有结束。
         *
         * 解决办法：
         * 既然RedisTemlate在setEnableTransactionSupport会造成连接不关闭，
         * 那怎么样才能正常关闭呢？我们将事务支持开关和@Transanctional结合起来用看看会怎么样。
         * spring中要使用@Transanctional首先要配transactionManager，但是spring没有专门针对Redis的事务管理器实现，
         * 而是所有调用RedisTemplate的方法最终都会调用到RedisConnctionUtils这个类的方法上面，
         * 在这个类里面会判断是不是进入到事务里面，也就是说Redis的事务管理的功能是由RedisConnctionUtils内部实现的。
         * 根据官方文档，我只想用Redis事务，也必须把JDBC捎上。
         *
         * 总结：
         * 一旦开启redis配置，所有使用这个template的redis操作都必须走注解式事务，要不然会导致连接一直占用，不关闭
         * 如果没有特殊需求、尽量不要配置事务
         */
//        template.setEnableTransactionSupport(true);
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
                    if (this.config.isJSONB()) {
                        return JSONB.toBytes(t, this.config.getSymbolTable(), this.config.getWriterFilters(), this.config.getWriterFeatures());
                    }
                    try (JSONWriter writer = JSONWriter.ofUTF8(writerContext)) {
                        if (t == null) {
                            writer.writeNull();
                        } else {
                            writer.setRootObject(t);
                            Class<?> valueClass = t.getClass();
                            ObjectWriter<?> objectWriter = writerContext.getObjectWriter(valueClass, valueClass);
                            objectWriter.write(writer, t, null, null, 0);
                        }
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
                    return (T) (this.config.isJSONB()
                            ? JSONB.parseObject(bytes, this.type, this.config.getSymbolTable(), this.config.getReaderFilters(), this.config.getReaderFeatures())
                            : JSON.parseObject(bytes, this.type, this.config.getDateFormat(), this.config.getReaderFilters(), this.config.getReaderFeatures()));
                } catch (Exception ex) {
                    throw new SerializationException("Could not deserialize: " + ex.getMessage(), ex);
                }
            } else {
                return null;
            }
        }
    }

}
