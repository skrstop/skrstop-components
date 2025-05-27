package com.skrstop.framework.components.util.serialization.json;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.TypeReference;
import com.skrstop.framework.components.core.common.response.CollectionResult;
import com.skrstop.framework.components.core.common.response.DefaultPageResult;
import com.skrstop.framework.components.core.common.response.DefaultResult;
import com.skrstop.framework.components.core.common.response.Result;
import lombok.experimental.UtilityClass;

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
@UtilityClass
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

}

