package com.skrstop.framework.components.core.common.serializable;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 蒋时华
 * @version 2020-04-30.
 */
public class SerializableBean implements Serializable {

    private static final long serialVersionUID = -8005755850249027709L;
    private static final ConcurrentHashMap<Boolean, ConcurrentHashMap<Object, JSONWriter.Context>> CONFIG_PROVIDER = new ConcurrentHashMap<>();
    private static final JSONWriter.Feature[] IGNORE_NULL_FEATURES = new JSONWriter.Feature[2];
    private static final JSONWriter.Feature[] NOT_IGNORE_NULL_FEATURES = new JSONWriter.Feature[5];
    private static final Object DEFAULT_PROVIDER_KEY = new Object();

    static {
        IGNORE_NULL_FEATURES[0] = JSONWriter.Feature.WriteBigDecimalAsPlain;
        IGNORE_NULL_FEATURES[1] = JSONWriter.Feature.WriteEnumUsingToString;
        NOT_IGNORE_NULL_FEATURES[0] = JSONWriter.Feature.WriteBigDecimalAsPlain;
        NOT_IGNORE_NULL_FEATURES[1] = JSONWriter.Feature.WriteEnumUsingToString;
        NOT_IGNORE_NULL_FEATURES[2] = JSONWriter.Feature.WriteMapNullValue;
        NOT_IGNORE_NULL_FEATURES[3] = JSONWriter.Feature.WriteNullListAsEmpty;
        NOT_IGNORE_NULL_FEATURES[4] = JSONWriter.Feature.WriteNullStringAsEmpty;
    }

    @Override
    public String toString() {
        return this.encodePropertyName(null, false);
    }

    public String toStringIgnoreNull() {
        return this.encodePropertyName(null, true);
    }

    public String toStringPascalCase() {
        return this.encodePropertyName(PropertyNamingStrategy.PascalCase, false);
    }

    public String toStringCamelCase() {
        return this.encodePropertyName(PropertyNamingStrategy.CamelCase, false);
    }

    public String toStringSnakeCase() {
        return this.encodePropertyName(PropertyNamingStrategy.SnakeCase, false);
    }

    public String toStringKebabCase() {
        return this.encodePropertyName(PropertyNamingStrategy.KebabCase, false);
    }

    private JSONWriter.Context getJsonWriterContext(Object propertyNamingStrategy, Boolean ignoreNull) {
        if (ignoreNull == null) {
            ignoreNull = true;
        }
        if (propertyNamingStrategy == null) {
            propertyNamingStrategy = DEFAULT_PROVIDER_KEY;
        }
        ConcurrentHashMap<Object, JSONWriter.Context> contextMap = CONFIG_PROVIDER.computeIfAbsent(ignoreNull, k -> new ConcurrentHashMap<>());
        Object finalPropertyNamingStrategy = propertyNamingStrategy;
        Boolean finalIgnoreNull = ignoreNull;
        return contextMap.computeIfAbsent(propertyNamingStrategy, k -> {
            ObjectWriterProvider provider = new ObjectWriterProvider();
            if (finalPropertyNamingStrategy instanceof PropertyNamingStrategy) {
                provider.setNamingStrategy((PropertyNamingStrategy) finalPropertyNamingStrategy);
            }
            return new JSONWriter.Context(provider, finalIgnoreNull ? IGNORE_NULL_FEATURES : NOT_IGNORE_NULL_FEATURES);
        });
    }

    /**
     * Encode Object To JSON
     *
     * @return json result
     */
    public String encodePropertyName(PropertyNamingStrategy propertyNamingStrategy, boolean ignoreNull) {
        JSONWriter.Context context = this.getJsonWriterContext(propertyNamingStrategy, ignoreNull);
        return JSON.toJSONString(this, context);
    }

    /**
     * Encode Object To JSON Bytes
     *
     * @return json result
     */
    public byte[] bytes() {
        // 循环引用，fastjson2中默认是关闭的【同fastjson1种SerializerFeature.DisableCircularReferenceDetect, 1中默认是开启的，参数语义相反】
//        JSONWriter.Feature.ReferenceDetection
        // fastjson2 默认就是fastjson1中的 SerializerFeature.WriteDateUseDateFormat
        return JSON.toJSONBytes(this
                , JSONWriter.Feature.WriteMapNullValue
                , JSONWriter.Feature.WriteNullListAsEmpty
                , JSONWriter.Feature.WriteNullStringAsEmpty
                , JSONWriter.Feature.WriteBigDecimalAsPlain
                , JSONWriter.Feature.WriteEnumUsingToString
        );
    }

    /**
     * Decode Json text to Object Instance
     *
     * @param json  json content
     * @param clazz target class type
     * @param <T>   T
     * @return bean instance
     */
    public static <T> T decode(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * Decode Json bytes to Object Instance
     *
     * @param jsonBytes json content bytes
     * @param clazz     target class type
     * @param <T>       T
     * @return bean instance
     */
    public static <T> T decode(byte[] jsonBytes, Class<T> clazz) {
        return JSON.parseObject(jsonBytes, clazz);
    }
}
