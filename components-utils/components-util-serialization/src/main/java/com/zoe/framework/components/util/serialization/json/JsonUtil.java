package com.zoe.framework.components.util.serialization.json;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author 蒋时华
 */
@UtilityClass
public class JsonUtil extends JSONUtil {

    /**
     * json数据转set
     *
     * @param jsonString
     * @return
     */
    public static Set<String> listAllProperties(String jsonString) {
        LinkedHashSet<String> flattened = new LinkedHashSet<>();
        listAllProperties(JSONUtil.parseObj(jsonString), "", flattened, true);
        return flattened;
    }

    /**
     * json数据转set
     *
     * @param obj
     * @return
     */
    public static Set<String> listAllProperties(JSONObject obj) {
        LinkedHashSet<String> flattened = new LinkedHashSet<>();
        listAllProperties(obj, "", flattened, true);
        return flattened;
    }

    /**
     * json数据转set
     *
     * @param jsonString
     * @return
     */
    public static Set<String> listAllProperties(String jsonString, boolean showArraySuffix) {
        LinkedHashSet<String> flattened = new LinkedHashSet<>();
        listAllProperties(JSONUtil.parseObj(jsonString), "", flattened, showArraySuffix);
        return flattened;
    }

    /**
     * json数据转set
     *
     * @param obj
     * @return
     */
    public static Set<String> listAllProperties(JSONObject obj, boolean showArraySuffix) {
        LinkedHashSet<String> flattened = new LinkedHashSet<>();
        listAllProperties(obj, "", flattened, showArraySuffix);
        return flattened;
    }

    /**
     * json数据转set
     *
     * @param obj
     * @param prefix
     * @param flattened
     * @param showArraySuffix
     */
    private static void listAllProperties(JSONObject obj, String prefix, LinkedHashSet<String> flattened, boolean showArraySuffix) {
        for (String key : obj.keySet()) {
            Object value = obj.get(key);
            String newPrefix = prefix + key;
            if (value instanceof JSONObject) {
                listAllProperties((JSONObject) value, newPrefix + ".", flattened, showArraySuffix);
            } else if (value instanceof JSONArray) {
                JSONArray arr = (JSONArray) value;
                for (int i = 0; i < arr.size(); i++) {
                    Object arrValue = arr.get(i);
                    if (arrValue instanceof JSONObject) {
                        listAllProperties((JSONObject) arrValue
                                , showArraySuffix ? newPrefix + "[]." : newPrefix + "."
                                , flattened
                                , showArraySuffix);
                    } else {
                        flattened.add(newPrefix);
                    }
                }
            } else {
                flattened.add(newPrefix);
            }
        }
    }

    /**
     * json 转 map, 拉平所有对象
     * @param jsonString
     * @return
     */
    public static LinkedHashMap<String, Object> listAllPropertiesValue(String jsonString) {
        LinkedHashMap<String, Object> flattened = new LinkedHashMap<>();
        listAllPropertiesValue(JSONUtil.parseObj(jsonString), "", flattened);
        return flattened;
    }

    /**
     * json 转 map, 拉平所有对象
     *
     * @param obj
     * @return
     */
    public static LinkedHashMap<String, Object> listAllPropertiesValue(JSONObject obj) {
        LinkedHashMap<String, Object> flattened = new LinkedHashMap<>();
        listAllPropertiesValue(obj, "", flattened);
        return flattened;
    }

    /**
     * json 转 map, 拉平所有对象
     *
     * @param obj
     * @param prefix
     * @param flattened
     */
    private static void listAllPropertiesValue(JSONObject obj, String prefix, LinkedHashMap<String, Object> flattened) {
        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String newPrefix = prefix + key;
            if (value instanceof JSONObject) {
                listAllPropertiesValue((JSONObject) value, newPrefix + ".", flattened);
            } else if (value instanceof JSONArray) {
                JSONArray arr = (JSONArray) value;
                for (int i = 0; i < arr.size(); i++) {
                    Object arrValue = arr.get(i);
                    if (arrValue instanceof JSONObject) {
                        listAllPropertiesValue((JSONObject) arrValue, newPrefix + "[" + i + "].", flattened);
                    } else {
                        flattened.put(newPrefix, value);
                    }
                }
            } else {
                flattened.put(newPrefix, value);
            }
        }
    }

    /**
     * 对json字符串格式化输出
     *
     * @param jsonStr
     * @return
     */
    public static String format(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }
        return sb.toString();
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

    /**
     * 单位缩进字符串。
     */
    private static String SPACE = "  ";

    /**
     * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。
     *
     * @param number 缩进次数。
     * @return 指定缩进次数的字符串。
     */
    private String indent(int number) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < number; i++) {
            result.append(SPACE);
        }
        return result.toString();
    }


}