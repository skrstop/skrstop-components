package cn.auntec.framework.components.starter.spring.support.util;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * {@link Field} Utilities
 *
 * @author 蒋时华
 * @see Field
 * @see ReflectionUtils
 * @since 2017.01.22
 */
public abstract class FieldUtil {

    /**
     * Get {@link Field} Value
     *
     * @param object    {@link Object}
     * @param fieldName field name
     * @param <T>       field type
     * @return {@link Field} Value
     */
    public static <T> T getFieldValue(Object object, String fieldName) {
        return (T) getFieldValue(object, fieldName, null);
    }

    /**
     * Get {@link Field} Value
     *
     * @param object       {@link Object}
     * @param fieldName    field name
     * @param <T>          field type
     * @param defaultValue default value
     * @return {@link Field} Value
     */
    public static <T> T getFieldValue(Object object, String fieldName, T defaultValue) {

        T value = getFieldValue(object, fieldName);

        return value != null ? value : defaultValue;
    }

    /**
     * Get {@link Field} Value
     *
     * @param object    {@link Object}
     * @param fieldName field name
     * @param fieldType field type
     * @param <T>       field type
     * @return {@link Field} Value
     */
    public static <T> T getFieldValue(Object object, String fieldName, Class<T> fieldType) {

        T fieldValue = null;

        Field field = ReflectionUtils.findField(object.getClass(), fieldName, fieldType);

        if (field != null) {

            boolean accessible = field.isAccessible();

            try {

                if (!accessible) {
                    ReflectionUtils.makeAccessible(field);
                }

                fieldValue = (T) ReflectionUtils.getField(field, object);

            } finally {

                if (!accessible) {
                    field.setAccessible(accessible);
                }
            }
        }

        return fieldValue;
    }
}
