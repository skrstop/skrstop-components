package cn.auntec.framework.components.core.common.serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

/**
 * @author 蒋时华
 * @version 2020-04-30.
 */
public class SerializableBean implements Serializable {

    private static final long serialVersionUID = -8005755850249027709L;

    @Override
    public String toString() {
        return this.encodePropertyName(PropertyNamingStrategy.NoChange, false);
    }

    public String toStringIgnoreNull() {
        return this.encodePropertyName(PropertyNamingStrategy.NoChange, true);
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

    /**
     * Encode Object To JSON
     *
     * @return json result
     */
    public String encodePropertyName(PropertyNamingStrategy propertyNamingStrategy, boolean ignoreNull) {
        SerializeConfig config = new SerializeConfig();
        config.setPropertyNamingStrategy(propertyNamingStrategy);
        SerializerFeature[] plugins;
        if (ignoreNull) {
            plugins = new SerializerFeature[4];
            plugins[0] = SerializerFeature.WriteDateUseDateFormat;
            plugins[1] = SerializerFeature.WriteBigDecimalAsPlain;
            plugins[2] = SerializerFeature.WriteEnumUsingToString;
            plugins[3] = SerializerFeature.DisableCircularReferenceDetect;
        } else {
            plugins = new SerializerFeature[7];
            plugins[0] = SerializerFeature.WriteDateUseDateFormat;
            plugins[1] = SerializerFeature.WriteBigDecimalAsPlain;
            plugins[2] = SerializerFeature.WriteEnumUsingToString;
            plugins[3] = SerializerFeature.DisableCircularReferenceDetect;
            plugins[4] = SerializerFeature.WriteMapNullValue;
            plugins[5] = SerializerFeature.WriteNullListAsEmpty;
            plugins[6] = SerializerFeature.WriteNullStringAsEmpty;
        }
        return JSON.toJSONString(this
                , config
                , plugins
        );
    }

    /**
     * Encode Object To JSON Bytes
     *
     * @return json result
     */
    public byte[] bytes() {
        return JSON.toJSONBytes(this
                , SerializerFeature.WriteMapNullValue
                , SerializerFeature.WriteNullListAsEmpty
                , SerializerFeature.WriteNullStringAsEmpty
                , SerializerFeature.WriteDateUseDateFormat
                , SerializerFeature.WriteBigDecimalAsPlain
                , SerializerFeature.WriteEnumUsingToString
                // 禁用“循环引用检测”
                , SerializerFeature.DisableCircularReferenceDetect);
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
