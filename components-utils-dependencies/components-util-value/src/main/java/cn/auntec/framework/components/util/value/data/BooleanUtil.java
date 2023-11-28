package cn.auntec.framework.components.util.value.data;

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
        return "true".equalsIgnoreCase(valueStr) || (defaultValue && !"false".equalsIgnoreCase(valueStr));
    }

}
