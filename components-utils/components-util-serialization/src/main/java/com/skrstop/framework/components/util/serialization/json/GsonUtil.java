package com.skrstop.framework.components.util.serialization.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skrstop.framework.components.core.common.response.*;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GsonUtil class
 *
 * @author 蒋时华
 * @date 2019/6/10
 */
@UtilityClass
public class GsonUtil {

    /**
     * to json
     *
     * @param src
     * @return
     */
    public static String toJson(Object src) {
        return new Gson().toJson(src);
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
        return new Gson().fromJson(json, cls);
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
        return new Gson().fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }

    /**
     * json to List<Map<String, T>>
     *
     * @param json
     * @param <T>
     * @return
     */
    public static <T> List<Map<String, T>> toBeanForListMap(
            String json) {
        return new Gson().fromJson(json, new TypeToken<List<Map<String, T>>>() {
        }.getType());
    }

    /**
     * json to List<Map<String, T>>
     *
     * @param json
     * @param <T>
     * @return
     */
    public static <T> List<Map<String, T>> toBeanForListMap(
            String json, Class<T> cls) {
        return new Gson().fromJson(json, new TypeToken<List<Map<String, T>>>() {
        }.getType());
    }

    /**
     * json to HashMap
     *
     * @param json
     * @param <T>
     * @return
     */
    public static <T> HashMap<String, T> toBeanForHashMap(
            String json) {
        return new Gson().fromJson(json, new TypeToken<HashMap<String, T>>() {
        }.getType());
    }

    /**
     * json to HashMap
     *
     * @param json
     * @param <T>
     * @return
     */
    public static <T> HashMap<String, T> toBeanForHashMap(
            String json, Class<T> cls) {
        return new Gson().fromJson(json, new TypeToken<HashMap<String, T>>() {
        }.getType());
    }

    /**
     * json to ListResult<T>
     *
     * @param json
     * @param data
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> CollectionResult<T> toBeanForListResult(String json, Class<T> data) {
        CollectionResult<T> result = GsonUtil.toBean(json, CollectionResult.class);
        if (result.getData() != null) {
            List<T> t = GsonUtil.toBeanForList(result.getData().toString(), data);
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
        Result<T> result = GsonUtil.toBean(json, Result.class);
        if (result.getData() != null) {
            T t = GsonUtil.toBean(result.getData().toString(), data);
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
        return GsonUtil.toBean(json, DefaultResult.class);
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
        PageListResult<T> pageResult = GsonUtil.toBean(json, PageListResult.class);
        if (pageResult.getData() != null) {
            List<T> ts = GsonUtil.toBeanForList(pageResult.getData().toString(), data);
            pageResult.setData(ts);
        }
        return pageResult;
    }

    /**
     * json to DefaultPageResult
     *
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public static DefaultPageResult toBeanForDefaultPageResult(String json) {
        return GsonUtil.toBean(json, DefaultPageResult.class);
    }

}
