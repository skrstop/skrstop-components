package com.jphoebe.framework.components.util.value.data;

import com.jphoebe.framework.components.util.constant.StringPoolConst;

/**
 * @author 蒋时华
 * @date 2020-08-07 10:10:52
 */
public class BooleanUtil extends cn.hutool.core.util.BooleanUtil {

    /**
     * string 转 boolean
     *
     * @param valueStr
     * @param defaultValue
     * @return
     */
    public static boolean toBoolean(final String valueStr, final boolean defaultValue) {
        return StringPoolConst.TRUE.equalsIgnoreCase(valueStr) || (defaultValue && !StringPoolConst.FALSE.equalsIgnoreCase(valueStr));
    }

    /**
     * object 转 boolean
     *
     * @param val
     * @param defaultValue
     * @return
     */
    public static boolean toBoolean(final Object val, final boolean defaultValue) {
        if (ObjectUtil.isNull(val)) {
            return defaultValue;
        }
        if (val instanceof Boolean) {
            return (Boolean) val;
        } else if (val instanceof Number) {
            return ((Number) val).intValue() == 1;
        }
        return toBoolean(val.toString(), defaultValue);
    }

}
