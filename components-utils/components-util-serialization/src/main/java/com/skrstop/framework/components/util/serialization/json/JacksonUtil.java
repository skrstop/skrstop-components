package com.skrstop.framework.components.util.serialization.json;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.date.TemporalAccessorUtil;
import cn.hutool.core.util.ClassUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.skrstop.framework.components.util.constant.DateFormatConst;
import com.skrstop.framework.components.util.constant.StringPoolConst;
import com.skrstop.framework.components.util.serialization.format.jackson.*;
import com.skrstop.framework.components.util.value.data.DateUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;

/**
 * Jackson JSON 工具
 * <p>
 * 内置 ObjectMapper 懒加载（首次使用时才创建），默认注册常用时间类型的序列化/反序列化（统一格式）、
 * 忽略未知字段、非 null 属性才输出；默认 mapper 与 snake_case mapper 的时间处理配置一致；
 * 支持运行期切换为容器全局 ObjectMapper（见 {@link #useObjectMapper(ObjectMapper)}）
 *
 * @author 蒋时华
 * @date 2025-05-27 11:38:20
 * @since 1.0.0
 */
public class JacksonUtil {

    /**
     * 懒加载：首次使用时才创建，避免类加载即占用内存
     */
    private static volatile ObjectMapper OBJECT_MAPPER;

    /**
     * 驼峰转下划线专用 mapper（时间处理配置与默认 mapper 一致，仅命名策略不同），同样懒加载
     */
    private static volatile ObjectMapper SNAKE_CASE_MAPPER;

    private JacksonUtil() {
    }

    /**
     * 构建时间序列化模块（两个 mapper 共用同一套时间处理配置）
     */
    private static SimpleModule buildTimeModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateFormatConst.NORM_DATETIME_PATTERN));
        simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateFormatConst.NORM_DATETIME_PATTERN));
        simpleModule.addSerializer(Date.class, new DateSerializer(DateFormatConst.NORM_DATETIME_PATTERN));
        simpleModule.addDeserializer(Date.class, new DateDeserializer(DateFormatConst.NORM_DATETIME_PATTERN));
        simpleModule.addSerializer(Calendar.class, new CalendarSerializer(DateFormatConst.NORM_DATETIME_PATTERN));
        simpleModule.addDeserializer(Calendar.class, new CalendarDeserializer(DateFormatConst.NORM_DATETIME_PATTERN));
        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateFormatConst.NORM_DATE_PATTERN));
        simpleModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateFormatConst.NORM_DATE_PATTERN));
        simpleModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateFormatConst.NORM_TIME_PATTERN));
        simpleModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateFormatConst.NORM_TIME_PATTERN));
        simpleModule.addSerializer(MonthDay.class, new MonthDaySerializer(DateFormatConst.NORM_MONTH_DAY_PATTERN));
        simpleModule.addDeserializer(MonthDay.class, new MonthDayDeserializer(DateFormatConst.NORM_MONTH_DAY_PATTERN));
        simpleModule.addSerializer(YearMonth.class, new YearMonthSerializer(DateFormatConst.NORM_YEAR_MONTH_PATTERN));
        simpleModule.addDeserializer(YearMonth.class, new YearMonthDeserializer(DateFormatConst.NORM_YEAR_MONTH_PATTERN));
        simpleModule.addSerializer(Year.class, new YearSerializer(DateFormatConst.NORM_YEAR_PATTERN));
        simpleModule.addDeserializer(Year.class, new YearDeserializer(DateFormatConst.NORM_YEAR_PATTERN));
        return simpleModule;
    }

    private static ObjectMapper buildMapper(PropertyNamingStrategy namingStrategy) {
        ObjectMapper mapper = new ObjectMapper();
        if (namingStrategy != null) {
            mapper.setPropertyNamingStrategy(namingStrategy);
        }
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(buildTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return mapper;
    }

    private static ObjectMapper defaultMapper() {
        if (OBJECT_MAPPER == null) {
            synchronized (JacksonUtil.class) {
                if (OBJECT_MAPPER == null) {
                    OBJECT_MAPPER = buildMapper(null);
                }
            }
        }
        return OBJECT_MAPPER;
    }

    private static ObjectMapper snakeCaseMapper() {
        if (SNAKE_CASE_MAPPER == null) {
            synchronized (JacksonUtil.class) {
                if (SNAKE_CASE_MAPPER == null) {
                    SNAKE_CASE_MAPPER = buildMapper(PropertyNamingStrategies.SNAKE_CASE);
                }
            }
        }
        return SNAKE_CASE_MAPPER;
    }

    /**
     * 运行期切换为容器全局 ObjectMapper（如 Spring 上下文中的主 mapper），
     * 切换后 {@link #toJson} / {@link #fromJson} / {@link #fromJsonToMap} 均使用新 mapper
     *
     * @param objectMapper 全局 ObjectMapper
     * @return 是否发生切换（为 null 或与当前相同则不切换）
     */
    public static synchronized boolean useObjectMapper(ObjectMapper objectMapper) {
        if (ObjectUtil.isNull(objectMapper)) {
            return false;
        }
        if (objectMapper.equals(OBJECT_MAPPER)) {
            return false;
        }
        OBJECT_MAPPER = objectMapper;
        return true;
    }

    /**
     * 是否是基础类型
     *
     * @param type
     * @return
     */
    public static boolean isBasicType(Class<?> type) {
        if (ClassUtil.isBasicType(type)
                || String.class.equals(type)
                || BigDecimal.class.equals(type)
                || isBasicTypeDate(type)
        ) {
            return true;
        }
        return false;
    }

    /**
     * 是否是基础类型中的日期类型
     *
     * @param type
     * @return
     */
    public static boolean isBasicTypeDate(Class<?> type) {
        if (LocalDateTime.class.equals(type)
                || LocalDate.class.equals(type)
                || LocalTime.class.equals(type)
                || YearMonth.class.equals(type)
                || MonthDay.class.equals(type)
                || Year.class.equals(type)
                || Date.class.equals(type)
        ) {
            return true;
        }
        return false;
    }

    /**
     * 基础类型（含日期）转字符串，日期按统一格式输出；非基础类型走 toString
     *
     * @param param
     * @return
     */
    public static String toObjectString(Object param) {
        if (param == null) {
            return null;
        }
        if (param instanceof LocalDateTime) {
            return LocalDateTimeUtil.format((LocalDateTime) param, DateFormatConst.NORM_DATETIME_PATTERN);
        } else if (param instanceof LocalDate) {
            return LocalDateTimeUtil.format((LocalDate) param, DateFormatConst.NORM_DATE_PATTERN);
        } else if (param instanceof LocalTime) {
            return TemporalAccessorUtil.format((LocalTime) param, DateFormatConst.NORM_TIME_PATTERN);
        } else if (param instanceof YearMonth) {
            return TemporalAccessorUtil.format((YearMonth) param, DateFormatConst.NORM_YEAR_MONTH_PATTERN);
        } else if (param instanceof MonthDay) {
            return TemporalAccessorUtil.format((MonthDay) param, DateFormatConst.NORM_MONTH_DAY_PATTERN);
        } else if (param instanceof Year) {
            return TemporalAccessorUtil.format((Year) param, DateFormatConst.NORM_YEAR_PATTERN);
        } else if (param instanceof Date) {
            return DateUtil.format((Timestamp) param, DateFormatConst.NORM_DATETIME_PATTERN);
        }
        return param.toString();
    }

    public static String toJson(Object data) {
        if (ObjectUtil.isNull(data)) {
            return StringPoolConst.EMPTY;
        }
        try {
            return defaultMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String json, Type type) {
        try {
            if (String.class.equals(type)) {
                return (T) json;
            }
            JavaType javaType = TypeFactory.defaultInstance().constructType(type);
            if (isBasicTypeDate(javaType.getRawClass()) && !json.startsWith(StringPoolConst.QUOTE)) {
                json = (StringPoolConst.QUOTE + json + StringPoolConst.QUOTE);
            }
            return defaultMapper().readValue(json, javaType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> fromJsonToMap(String json) {
        try {
            JsonNode rootNode = defaultMapper().readTree(json);
            Map<String, String> resultMap = new HashMap<>();
            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String value;
                if (entry.getValue().isNull()) {
                    value = null;
                } else if (entry.getValue().isValueNode()) {
                    // 如果是值节点，直接转换为字符串
                    value = entry.getValue().asText();
                } else {
                    // 其他类型，直接转换为字符串
                    value = entry.getValue().toString();
                }
                resultMap.put(entry.getKey(), value);
            }
            return resultMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        return snakeCaseMapper().writeValueAsString(object);
    }

    /**
     * 将下划线转换为驼峰的形式，例如：user_name-->userName
     *
     * @param json
     * @param clazz
     * @return
     * @throws java.io.IOException
     */
    public static <T> T toBeanBySnakeCase(String json, Class<T> clazz) throws java.io.IOException {
        if (ObjectUtil.isNull(json) || json.isEmpty() || ObjectUtil.isNull(clazz)) {
            return null;
        }
        return snakeCaseMapper().readValue(json, clazz);
    }

}
