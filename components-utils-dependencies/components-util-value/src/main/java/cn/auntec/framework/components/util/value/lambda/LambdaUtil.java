package cn.auntec.framework.components.util.value.lambda;

import cn.auntec.framework.components.core.exception.AuntecRuntimeException;
import cn.auntec.framework.components.core.exception.util.ThrowableStackTraceUtil;
import cn.auntec.framework.components.util.constant.StringPoolConst;
import cn.auntec.framework.components.util.value.data.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeanUtil class
 *
 * @author 蒋时华
 * @date 2019/12/14
 */
@Slf4j
public class LambdaUtil {

    private static String IS = "is";
    private static String GET = "get";
    private static String SET = "set";

    // SerializedLambda 反序列化缓存
    private static final Map<String, WeakReference<SerializedLambda>> FUNC_CACHE = new ConcurrentHashMap<>();

    /**
     * 首字母转小写
     *
     * @param s
     * @return
     */
    private static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
        }
    }

    /**
     * 获取SerializedLambda
     */
    private static SerializedLambda getSerializedLambda(Serializable fn) {
        Class<?> clazz = fn.getClass();
        String name = clazz.getName();
        return Optional.ofNullable(FUNC_CACHE.get(name))
                .map(WeakReference::get)
                .orElseGet(() -> {
                    try {
                        Method method = fn.getClass().getDeclaredMethod("writeReplace");
                        method.setAccessible(Boolean.TRUE);
                        SerializedLambda lambda = (SerializedLambda) method.invoke(fn);
                        FUNC_CACHE.put(name, new WeakReference<>(lambda));
                        return lambda;
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        log.error(ThrowableStackTraceUtil.getStackTraceStr(e));
                        throw new AuntecRuntimeException(e);
                    }
                });
    }

    /**
     * 转换方法引用为属性名
     *
     * @param fn
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static String getFieldName(Serializable fn) {
        SerializedLambda lambda = getSerializedLambda(fn);
        // 获取方法名
        String methodName = lambda.getImplMethodName();
        String prefix = null;
        if (methodName.startsWith(GET)) {
            prefix = GET;
        } else if (methodName.startsWith(IS)) {
            prefix = IS;
        } else if (methodName.startsWith(SET)) {
            prefix = SET;
        }
        if (prefix == null) {
            throw new IllegalArgumentException("无效的getter方法、setter方法、is方法: {}" + methodName);
        }
        // 截取get/is之后的字符串并转换首字母为小写
        return toLowerCaseFirstOne(methodName.replace(prefix, StringPoolConst.EMPTY));
    }

    /**
     * 转换方法引用为属性名
     *
     * @param fn
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> String convertToFieldName(GetterFunction<T> fn) {
        return getFieldName(fn);
    }

    /**
     * 转换方法引用为属性名(下划线)
     *
     * @param fn
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> String convertToFieldNameByUnderline(GetterFunction<T> fn) {
        String fieldName = getFieldName(fn);
        return StrUtil.humpToUnderline(fieldName);
    }

    /**
     * 转换方法引用为属性名(驼峰)
     *
     * @param fn
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> String convertToFieldNameByHump(GetterFunction<T> fn) {
        String fieldName = getFieldName(fn);
        return StrUtil.underlineToHump(fieldName);
    }

    /**
     * 转换方法引用为属性名
     *
     * @param fn
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T, U> String convertToFieldName(SetterFunction<T, U> fn) {
        return getFieldName(fn);
    }

    /**
     * 转换方法引用为属性名(下划线)
     *
     * @param fn
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T, U> String convertToFieldNameByUnderline(SetterFunction<T, U> fn) {
        String fieldName = getFieldName(fn);
        return StrUtil.humpToUnderline(fieldName);
    }

    /**
     * 转换方法引用为属性名(驼峰)
     *
     * @param fn
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T, U> String convertToFieldNameByHump(SetterFunction<T, U> fn) {
        String fieldName = getFieldName(fn);
        return StrUtil.underlineToHump(fieldName);
    }


}
