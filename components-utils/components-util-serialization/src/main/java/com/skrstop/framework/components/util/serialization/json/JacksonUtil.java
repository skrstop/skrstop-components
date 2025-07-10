package com.skrstop.framework.components.util.serialization.json;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.io.IOException;

/**
 * @author 蒋时华
 * @date 2025-05-27 11:38:30
 * @since 1.0.0
 */
public class JacksonUtil {
    private static final ObjectMapper JACKSON_MAPPER = new ObjectMapper();

    static {
        JACKSON_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 忽略未知属性
        JACKSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 驼峰转下划线，例如：userName-->user_name
     *
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    public static String toJsonBySnakeCase(Object object) throws JsonProcessingException {
        if (ObjectUtil.isNull(object)) {
            return null;
        }
        return JACKSON_MAPPER.writeValueAsString(object);
    }

    /**
     * 将下划线转换为驼峰的形式，例如：user_name-->userName
     *
     * @param json
     * @param clazz
     * @return
     * @throws IOException
     */
    public static <T> T toBeanBySnakeCase(String json, Class<T> clazz) throws IOException {
        if (StrUtil.isBlank(json) || ObjectUtil.isNull(clazz)) {
            return null;
        }
        return JACKSON_MAPPER.readValue(json, clazz);
    }

}
