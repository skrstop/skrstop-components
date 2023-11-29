package com.zoe.framework.components.util.serialization.json;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.zoe.framework.components.core.common.response.*;
import com.zoe.framework.components.core.common.response.common.CommonResultCode;
import com.zoe.framework.components.core.common.response.core.IResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * fastjson 工具类
 * fastjson 打开autotype功能，默认关闭
 * -Dfastjson2.parser.safeMode=true
 *
 * @author 蒋时华
 * @date 2018/11/29
 */
public class FastJsonUtil {

    /**
     * object 2 json
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        if (ObjectUtil.isNull(object)) {
            return null;
        }
        // 禁用循环引用检测
        return JSON.toJSONString(object);
    }

    /**
     * object 2 json
     *
     * @param object
     * @return
     */
    public static String toJsonWithDateFormat(Object object, String dateFormat) {
        if (ObjectUtil.isNull(object)) {
            return null;
        }
        // 禁用循环引用检测
        return JSON.toJSONString(object, dateFormat);
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
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(object);
    }

    /**
     * json to bean
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T toBean(String json, Class<T> cls) {
        if (StrUtil.isBlank(json) || ObjectUtil.isNull(cls)) {
            return null;
        }
        return JSON.parseObject(json
                , cls
                , JSONReader.Feature.IgnoreAutoTypeNotMatch
        );
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
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.readValue(json, clazz);
    }

    /**
     * json to List<T>
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> toBeanForList(String json,
                                            Class<T> cls) {
        if (StrUtil.isBlank(json) || ObjectUtil.isNull(cls)) {
            return null;
        }
        return JSON.parseArray(json, cls);
    }

    /**
     * json to  List<Map<String, String>>
     *
     * @param json
     * @return
     */
    public static <T> List<Map<String, T>> toBeanForListMap(
            String json) {
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return JSON.parseObject(json
                , new TypeReference<List<Map<String, T>>>() {
                }
                , JSONReader.Feature.IgnoreAutoTypeNotMatch
        );
    }

    /**
     * json to  List<Map<String, String>>
     *
     * @param json
     * @return
     */
    public static <T> List<Map<String, T>> toBeanForListMap(
            String json, Class<T> cls) {
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return JSON.parseObject(json
                , new TypeReference<List<Map<String, T>>>() {
                }
                , JSONReader.Feature.IgnoreAutoTypeNotMatch
        );
    }

    /**
     * json to HashMap<String, String>
     *
     * @param jsonString
     * @return
     */
    public static <T> HashMap<String, T> toBeanForHashMap(
            String jsonString) {
        if (StrUtil.isBlank(jsonString)) {
            return null;
        }
        return JSON.parseObject(jsonString,
                new TypeReference<HashMap<String, T>>() {
                }
                , JSONReader.Feature.IgnoreAutoTypeNotMatch
        );
    }

    /**
     * json to HashMap<String, String>
     *
     * @param jsonString
     * @return
     */
    public static <T> HashMap<String, T> toBeanForHashMap(
            String jsonString, Class<T> cls) {
        if (StrUtil.isBlank(jsonString)) {
            return null;
        }
        return JSON.parseObject(jsonString,
                new TypeReference<HashMap<String, T>>() {
                }
                , JSONReader.Feature.IgnoreAutoTypeNotMatch
        );
    }

    /**
     * json to Result<T>
     *
     * @param json
     * @param data
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> CollectionResult<T> toBeanForListResult(String json, Class<T> data) {
        if (StrUtil.isBlank(json) || ObjectUtil.isNull(data)) {
            return null;
        }
        CollectionResult<T> result = toBean(json, CollectionResult.class);
        if (ObjectUtil.isNotNull(result) && ObjectUtil.isNotNull(result.getData())) {
            List<T> t = toBeanForList(result.getData().toString(), data);
            result.setData(t);
        }
        return result;
    }

    /**
     * json to Result<T>
     *
     * @param json
     * @param data
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> toBeanForResult(String json, Class<T> data) {
        if (StrUtil.isBlank(json) || ObjectUtil.isNull(data)) {
            return null;
        }
        Result<T> result = toBean(json, Result.class);
        if (ObjectUtil.isNotNull(result) && ObjectUtil.isNotNull(result.getData())) {
            T t = toBean(result.getData().toString(), data);
            result.setData(t);
        }
        return result;
    }

    /**
     * json to DefaultResult
     *
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public static DefaultResult toBeanForDefaultResult(String json) {
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return toBean(json, DefaultResult.class);
    }

    /**
     * json to PageResult<T>
     *
     * @param json
     * @param data
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> PageListResult<T> toBeanForPageListResult(String json, Class<T> data) {
        if (StrUtil.isBlank(json) || ObjectUtil.isNull(data)) {
            return null;
        }
        PageListResult<T> result = toBean(json, PageListResult.class);
        if (ObjectUtil.isNotNull(result) && ObjectUtil.isNotNull(result.getData())) {
            List<T> ts = toBeanForList(result.getData().toString(), data);
            result.setData(ts);
        }
        return result;
    }

    /**
     * json to DefaultPageResult
     *
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public static DefaultPageResult toBeanForDefaultPageResult(String json) {
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return toBean(json, DefaultPageResult.class);
    }


    public static void main(String[] args) {
//        CommonResultCode success = CommonResultCode.SUCCESS;
        IResult success = CommonResultCode.BUSY;
        Result<DefaultResult> result = Result.Builder.result(success, DefaultResult.Builder.result(success));
        String json = FastJsonUtil.toJson(result);
        System.out.println(json);

        Result<DefaultResult> defaultResultResult = FastJsonUtil.toBeanForResult(json, DefaultResult.class);
        System.out.println(defaultResultResult);

        final HashMap<String, Object> stringObjectHashMap = FastJsonUtil.toBeanForHashMap("");
        final HashMap<String, String> stringStringHashMap = FastJsonUtil.toBeanForHashMap("", String.class);

    }

}

