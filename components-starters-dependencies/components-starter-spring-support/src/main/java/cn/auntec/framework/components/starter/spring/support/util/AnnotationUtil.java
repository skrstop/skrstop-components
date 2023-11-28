package cn.auntec.framework.components.starter.spring.support.util;

import cn.auntec.framework.components.util.value.data.CollectionUtil;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.util.ClassUtils;

import java.lang.annotation.*;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static org.springframework.core.annotation.AnnotationAttributes.fromMap;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import static org.springframework.core.annotation.AnnotationUtils.getDefaultValue;
import static org.springframework.util.ClassUtils.resolveClassName;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.ObjectUtils.nullSafeEquals;
import static org.springframework.util.ReflectionUtils.findMethod;
import static org.springframework.util.ReflectionUtils.invokeMethod;
import static org.springframework.util.StringUtils.trimWhitespace;

/**
 * {@link Annotation} Utilities
 *
 * @author 蒋时华
 * @see Annotation
 * @since 2017.01.13
 */
public abstract class AnnotationUtil {

    /**
     * The class name of AnnotatedElementUtils that is introduced since Spring Framework 4
     */
    public static final String ANNOTATED_ELEMENT_UTILS_CLASS_NAME = "org.springframework.core.annotation.AnnotatedElementUtils";

    /**
     * Is specified {@link Annotation} present on {@link Method}'s declaring class or parameters or
     * itself.
     *
     * @param method          {@link Method}
     * @param annotationClass {@link Annotation} type
     * @param <A>             {@link Annotation} type
     * @return If present , return <code>true</code> , or <code>false</code>
     */
    public static <A extends Annotation> boolean isPresent(Method method, Class<A> annotationClass) {

        Map<ElementType, List<A>> annotationsMap = findAnnotations(method, annotationClass);

        return !annotationsMap.isEmpty();
    }

    /**
     * Find specified {@link Annotation} type maps from {@link Method}
     *
     * @param method          {@link Method}
     * @param annotationClass {@link Annotation} type
     * @param <A>             {@link Annotation} type
     * @return {@link Annotation} type maps , the {@link ElementType} as key , the list of {@link
     * Annotation} as value. If {@link Annotation} was annotated on {@link Method}'s
     * parameters{@link ElementType#PARAMETER} , the associated {@link Annotation} list may
     * contain multiple elements.
     */
    public static <A extends Annotation> Map<ElementType, List<A>> findAnnotations(
            Method method, Class<A> annotationClass) {

        Retention retention = annotationClass.getAnnotation(Retention.class);

        RetentionPolicy retentionPolicy = retention.value();

        if (!RetentionPolicy.RUNTIME.equals(retentionPolicy)) {
            return Collections.emptyMap();
        }

        Map<ElementType, List<A>> annotationsMap = new LinkedHashMap<ElementType, List<A>>();

        Target target = annotationClass.getAnnotation(Target.class);

        ElementType[] elementTypes = target.value();

        for (ElementType elementType : elementTypes) {

            List<A> annotationsList = new LinkedList<A>();

            switch (elementType) {
                case PARAMETER:
                    Annotation[][] parameterAnnotations = method.getParameterAnnotations();

                    for (Annotation[] annotations : parameterAnnotations) {

                        for (Annotation annotation : annotations) {

                            if (annotationClass.equals(annotation.annotationType())) {

                                annotationsList.add((A) annotation);
                            }
                        }
                    }

                    break;

                case METHOD:
                    A annotation = findAnnotation(method, annotationClass);

                    if (annotation != null) {

                        annotationsList.add(annotation);
                    }

                    break;

                case TYPE:
                    Class<?> beanType = method.getDeclaringClass();

                    A annotation2 = findAnnotation(beanType, annotationClass);

                    if (annotation2 != null) {

                        annotationsList.add(annotation2);
                    }

                    break;

                default:

                    // NOTHING
                    break;
            }

            if (!annotationsList.isEmpty()) {

                annotationsMap.put(elementType, annotationsList);
            }
        }

        return Collections.unmodifiableMap(annotationsMap);
    }

    /**
     * Get the {@link Annotation} attributes
     *
     * @param annotation           specified {@link Annotation}
     * @param ignoreDefaultValue   whether ignore default value or not
     * @param ignoreAttributeNames the attribute names of annotation should be ignored
     * @return non-null
     * @since 1.0.2
     */
    public static Map<String, Object> getAttributes(
            Annotation annotation, boolean ignoreDefaultValue, String... ignoreAttributeNames) {
        return getAttributes(annotation, null, ignoreDefaultValue, ignoreAttributeNames);
    }

    /**
     * Get the {@link Annotation} attributes
     *
     * @param annotation           specified {@link Annotation}
     * @param propertyResolver     {@link PropertyResolver} instance, e.g {@link Environment}
     * @param ignoreDefaultValue   whether ignore default value or not
     * @param ignoreAttributeNames the attribute names of annotation should be ignored
     * @return non-null
     * @since 1.0.2
     */
    public static Map<String, Object> getAttributes(
            Annotation annotation,
            PropertyResolver propertyResolver,
            boolean ignoreDefaultValue,
            String... ignoreAttributeNames) {

        Map<String, Object> annotationAttributes =
                org.springframework.core.annotation.AnnotationUtils.getAnnotationAttributes(annotation);

        String[] actualIgnoreAttributeNames = ignoreAttributeNames;

        if (ignoreDefaultValue && !isEmpty(annotationAttributes)) {

            List<String> attributeNamesToIgnore = new LinkedList<String>(asList(ignoreAttributeNames));

            for (Map.Entry<String, Object> annotationAttribute : annotationAttributes.entrySet()) {
                String attributeName = annotationAttribute.getKey();
                Object attributeValue = annotationAttribute.getValue();
                if (nullSafeEquals(attributeValue, getDefaultValue(annotation, attributeName))) {
                    attributeNamesToIgnore.add(attributeName);
                }
            }
            // extends the ignored list
            actualIgnoreAttributeNames =
                    attributeNamesToIgnore.toArray(new String[attributeNamesToIgnore.size()]);
        }

        return getAttributes(annotationAttributes, propertyResolver, actualIgnoreAttributeNames);
    }

    /**
     * Get the {@link Annotation} attributes
     *
     * @param annotationAttributes the attributes of specified {@link Annotation}
     * @param propertyResolver     {@link PropertyResolver} instance, e.g {@link Environment}
     * @param ignoreAttributeNames the attribute names of annotation should be ignored
     * @return non-null
     * @since 1.0.4
     */
    public static Map<String, Object> getAttributes(
            Map<String, Object> annotationAttributes,
            PropertyResolver propertyResolver,
            String... ignoreAttributeNames) {

        Set<String> ignoreAttributeNamesSet = new HashSet<String>(CollectionUtil.toList(ignoreAttributeNames));

        Map<String, Object> actualAttributes = new LinkedHashMap<String, Object>();

        for (Map.Entry<String, Object> annotationAttribute : annotationAttributes.entrySet()) {

            String attributeName = annotationAttribute.getKey();
            Object attributeValue = annotationAttribute.getValue();

            // ignore attribute name
            if (ignoreAttributeNamesSet.contains(attributeName)) {
                continue;
            }

            if (attributeValue instanceof String) {
                attributeValue = resolvePlaceholders(valueOf(attributeValue), propertyResolver);
            } else if (attributeValue instanceof String[]) {
                String[] values = (String[]) attributeValue;
                for (int i = 0; i < values.length; i++) {
                    values[i] = resolvePlaceholders(values[i], propertyResolver);
                }
                attributeValue = values;
            }
            actualAttributes.put(attributeName, attributeValue);
        }
        return actualAttributes;
    }

    private static String resolvePlaceholders(
            String attributeValue, PropertyResolver propertyResolver) {
        String resolvedValue = attributeValue;
        if (propertyResolver != null) {
            resolvedValue = propertyResolver.resolvePlaceholders(resolvedValue);
            resolvedValue = trimWhitespace(resolvedValue);
        }
        return resolvedValue;
    }

    /**
     * Get the attribute value
     *
     * @param annotation    {@link Annotation annotation}
     * @param attributeName the name of attribute
     * @param <T>           the type of attribute value
     * @return the attribute value if found
     * @since 1.0.3
     */
    public static <T> T getAttribute(Annotation annotation, String attributeName) {
        return getAttribute(
                org.springframework.core.annotation.AnnotationUtils.getAnnotationAttributes(annotation),
                attributeName);
    }

    /**
     * Get the attribute value
     *
     * @param attributes    {@link Map the annotation attributes} or {@link AnnotationAttributes}
     * @param attributeName the name of attribute
     * @param <T>           the type of attribute value
     * @return the attribute value if found
     * @since 1.0.3
     */
    public static <T> T getAttribute(Map<String, Object> attributes, String attributeName) {
        return (T) attributes.get(attributeName);
    }

    /**
     * Get the {@link AnnotationAttributes}
     *
     * @param annotation           specified {@link Annotation}
     * @param ignoreDefaultValue   whether ignore default value or not
     * @param ignoreAttributeNames the attribute names of annotation should be ignored
     * @return non-null
     * @see #getAnnotationAttributes(Annotation, PropertyResolver, boolean, String...)
     * @since 1.0.3
     */
    public static AnnotationAttributes getAnnotationAttributes(
            Annotation annotation, boolean ignoreDefaultValue, String... ignoreAttributeNames) {
        return getAnnotationAttributes(annotation, null, ignoreDefaultValue, ignoreAttributeNames);
    }

    /**
     * Get the {@link AnnotationAttributes}
     *
     * @param annotation           specified {@link Annotation}
     * @param propertyResolver     {@link PropertyResolver} instance, e.g {@link Environment}
     * @param ignoreDefaultValue   whether ignore default value or not
     * @param ignoreAttributeNames the attribute names of annotation should be ignored
     * @return non-null
     * @see #getAttributes(Annotation, PropertyResolver, boolean, String...)
     * @see #getAnnotationAttributes(AnnotatedElement, Class, PropertyResolver, boolean, String...)
     * @since 1.0.3
     */
    public static AnnotationAttributes getAnnotationAttributes(
            Annotation annotation,
            PropertyResolver propertyResolver,
            boolean ignoreDefaultValue,
            String... ignoreAttributeNames) {
        return fromMap(
                getAttributes(annotation, propertyResolver, ignoreDefaultValue, ignoreAttributeNames));
    }

    /**
     * Get the {@link AnnotationAttributes}
     *
     * @param annotatedElement     {@link AnnotatedElement the annotated element}
     * @param annotationType       the {@link Class tyoe} pf {@link Annotation annotation}
     * @param propertyResolver     {@link PropertyResolver} instance, e.g {@link Environment}
     * @param ignoreDefaultValue   whether ignore default value or not
     * @param ignoreAttributeNames the attribute names of annotation should be ignored
     * @return if <code>annotatedElement</code> can't be found in <code>annotatedElement</code>,
     * return <code>null</code>
     * @since 1.0.3
     */
    public static AnnotationAttributes getAnnotationAttributes(
            AnnotatedElement annotatedElement,
            Class<? extends Annotation> annotationType,
            PropertyResolver propertyResolver,
            boolean ignoreDefaultValue,
            String... ignoreAttributeNames) {
        Annotation annotation = annotatedElement.getAnnotation(annotationType);
        return annotation == null
                ? null
                : getAnnotationAttributes(
                annotation, propertyResolver, ignoreDefaultValue, ignoreAttributeNames);
    }

    /**
     * Get the {@link AnnotationAttributes}, if the argument <code>tryMergedAnnotation</code> is
     * <code>true</code>, the {@link AnnotationAttributes} will be got from {@link
     * #tryGetMergedAnnotationAttributes(AnnotatedElement, Class, PropertyResolver, boolean,
     * String...) merged annotation} first, if failed, and then to get from {@link
     * #getAnnotationAttributes(AnnotatedElement, Class, PropertyResolver, boolean, boolean,
     * String...) normal one}
     *
     * @param annotatedElement     {@link AnnotatedElement the annotated element}
     * @param annotationType       the {@link Class tyoe} pf {@link Annotation annotation}
     * @param propertyResolver     {@link PropertyResolver} instance, e.g {@link Environment}
     * @param ignoreDefaultValue   whether ignore default value or not
     * @param tryMergedAnnotation  whether try merged annotation or not
     * @param ignoreAttributeNames the attribute names of annotation should be ignored
     * @return if <code>annotatedElement</code> can't be found in <code>annotatedElement</code>,
     * return <code>null</code>
     * @since 1.0.3
     */
    public static AnnotationAttributes getAnnotationAttributes(
            AnnotatedElement annotatedElement,
            Class<? extends Annotation> annotationType,
            PropertyResolver propertyResolver,
            boolean ignoreDefaultValue,
            boolean tryMergedAnnotation,
            String... ignoreAttributeNames) {
        AnnotationAttributes attributes = null;

        if (tryMergedAnnotation) {
            attributes =
                    tryGetMergedAnnotationAttributes(
                            annotatedElement,
                            annotationType,
                            propertyResolver,
                            ignoreDefaultValue,
                            ignoreAttributeNames);
        }

        if (attributes == null) {
            attributes =
                    getAnnotationAttributes(
                            annotatedElement,
                            annotationType,
                            propertyResolver,
                            ignoreDefaultValue,
                            ignoreAttributeNames);
        }

        return attributes;
    }

    /**
     * Try to get the merged {@link Annotation annotation}
     *
     * @param annotatedElement {@link AnnotatedElement the annotated element}
     * @param annotationType   the {@link Class tyoe} pf {@link Annotation annotation}
     * @return If current version of Spring Framework is below 4.2, return <code>null</code>
     * @since 1.0.3
     */
    public static Annotation tryGetMergedAnnotation(
            AnnotatedElement annotatedElement, Class<? extends Annotation> annotationType) {

        Annotation mergedAnnotation = null;

        ClassLoader classLoader = annotationType.getClassLoader();

        if (ClassUtils.isPresent(ANNOTATED_ELEMENT_UTILS_CLASS_NAME, classLoader)) {
            Class<?> annotatedElementUtilsClass =
                    resolveClassName(ANNOTATED_ELEMENT_UTILS_CLASS_NAME, classLoader);
            // getMergedAnnotation method appears in the Spring Framework 4.2
            Method getMergedAnnotationMethod =
                    findMethod(
                            annotatedElementUtilsClass,
                            "getMergedAnnotation",
                            AnnotatedElement.class,
                            Class.class);
            if (getMergedAnnotationMethod != null) {
                mergedAnnotation =
                        (Annotation)
                                invokeMethod(getMergedAnnotationMethod, null, annotatedElement, annotationType);
            }
        }

        return mergedAnnotation;
    }

    /**
     * Try to get {@link AnnotationAttributes the annotation attributes} after merging and resolving
     * the placeholders
     *
     * @param annotatedElement     {@link AnnotatedElement the annotated element}
     * @param annotationType       the {@link Class tyoe} pf {@link Annotation annotation}
     * @param propertyResolver     {@link PropertyResolver} instance, e.g {@link Environment}
     * @param ignoreDefaultValue   whether ignore default value or not
     * @param ignoreAttributeNames the attribute names of annotation should be ignored
     * @return If the specified annotation type is not found, return <code>null</code>
     * @since 1.0.3
     */
    public static AnnotationAttributes tryGetMergedAnnotationAttributes(
            AnnotatedElement annotatedElement,
            Class<? extends Annotation> annotationType,
            PropertyResolver propertyResolver,
            boolean ignoreDefaultValue,
            String... ignoreAttributeNames) {
        Annotation annotation = tryGetMergedAnnotation(annotatedElement, annotationType);
        return annotation == null
                ? null
                : getAnnotationAttributes(
                annotation, propertyResolver, ignoreDefaultValue, ignoreAttributeNames);
    }
}
