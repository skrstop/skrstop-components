package com.skrstop.framework.components.util.serialization.json;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

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

}
