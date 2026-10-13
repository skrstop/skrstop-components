package com.skrstop.framework.components.starter.spring.support.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author 蒋时华
 */
public class AnnotationUtilTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
    @interface MyAnno {
        String value() default "default";
    }

    @Retention(RetentionPolicy.CLASS)
    @interface ClassOnly {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface FieldOnly {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.PARAMETER})
    @interface OtherAnno {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @interface ArrayAnno {
        String[] values() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @interface IntAnno {
        int num() default 0;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface NoAttrs {
    }

    @MyAnno
    public static class AnnotatedBean {

        public void method(@MyAnno("param") String param) {
        }

        @MyAnno("method")
        public void annotatedMethod() {
        }

        public void plainMethod() {
        }
    }

    public static class ParameterAnnotatedHolder {
        public void multi(@MyAnno("a") String a, @MyAnno("b") String b) {
        }

        public void mixed(@MyAnno("x") String x, String plain) {
        }

        // 参数带其他注解 → annotationClass.equals false 分支
        public void otherParam(@OtherAnno String x, @MyAnno("y") String y) {
        }

        @ArrayAnno(values = {"${a1}", "plain"})
        public void arrayMethod() {
        }

        @MyAnno("${method}")
        public void placeholderMethod() {
        }
    }

    @Test
    public void testIsPresent() throws Exception {
        Assert.assertTrue(AnnotationUtil.isPresent(
                AnnotatedBean.class.getMethod("annotatedMethod"), MyAnno.class));
        Assert.assertTrue(AnnotationUtil.isPresent(
                AnnotatedBean.class.getMethod("plainMethod"), MyAnno.class));
        // 类上有注解
        Assert.assertTrue(AnnotationUtil.isPresent(
                AnnotatedBean.class.getMethod("plainMethod"), MyAnno.class));
        Assert.assertFalse(AnnotationUtil.isPresent(
                AnnotatedBean.class.getMethod("plainMethod"), FieldOnly.class));
        // 非 RUNTIME retention → emptyMap
        Method m = AnnotatedBean.class.getMethod("plainMethod");
        Assert.assertTrue(AnnotationUtil.findAnnotations(m, ClassOnly.class).isEmpty());
    }

    @Test
    public void testFindAnnotations() throws Exception {
        // 非 RUNTIME → emptyMap
        Assert.assertTrue(AnnotationUtil.findAnnotations(
                AnnotatedBean.class.getMethod("plainMethod"), ClassOnly.class).isEmpty());

        // TYPE 元素
        Map<ElementType, java.util.List<MyAnno>> typeResult =
                AnnotationUtil.findAnnotations(AnnotatedBean.class.getMethod("plainMethod"), MyAnno.class);
        Assert.assertEquals(1, typeResult.size());
        Assert.assertEquals(ElementType.TYPE, typeResult.keySet().iterator().next());

        // METHOD 元素
        Map<ElementType, java.util.List<MyAnno>> methodResult =
                AnnotationUtil.findAnnotations(AnnotatedBean.class.getMethod("annotatedMethod"), MyAnno.class);
        Assert.assertEquals(2, methodResult.size());
        Assert.assertEquals("method", methodResult.get(ElementType.METHOD).get(0).value());

        // PARAMETER 元素（单个）
        Map<ElementType, java.util.List<MyAnno>> paramResult =
                AnnotationUtil.findAnnotations(AnnotatedBean.class.getMethod("method", String.class), MyAnno.class);
        Assert.assertEquals(2, paramResult.size());
        Assert.assertEquals("param", paramResult.get(ElementType.PARAMETER).get(0).value());
        // TYPE + PARAMETER
        Assert.assertTrue(paramResult.containsKey(ElementType.TYPE));

        // PARAMETER 元素（多个）
        Map<ElementType, java.util.List<MyAnno>> multiResult =
                AnnotationUtil.findAnnotations(ParameterAnnotatedHolder.class.getMethod("multi", String.class, String.class), MyAnno.class);
        Assert.assertEquals(2, multiResult.get(ElementType.PARAMETER).size());
        // 混合参数
        Map<ElementType, java.util.List<MyAnno>> mixedResult =
                AnnotationUtil.findAnnotations(ParameterAnnotatedHolder.class.getMethod("mixed", String.class, String.class), MyAnno.class);
        Assert.assertEquals(1, mixedResult.get(ElementType.PARAMETER).size());
    }

    @Test
    public void testGetAttributes() throws Exception {
        MyAnno annotation = AnnotatedBean.class.getAnnotation(MyAnno.class);
        // 默认值忽略
        Map<String, Object> attrs = AnnotationUtil.getAttributes(annotation, true);
        Assert.assertEquals(0, attrs.size());
        // 默认值不忽略
        Map<String, Object> attrs2 = AnnotationUtil.getAttributes(annotation, false);
        Assert.assertEquals("default", attrs2.get("value"));
        // ignoreAttributeNames
        Map<String, Object> attrs3 = AnnotationUtil.getAttributes(annotation, false, "value");
        Assert.assertEquals(0, attrs3.size());
        // propertyResolver
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("t",
                java.util.Collections.singletonMap("key", "vv")));
        MyAnno holder = ParameterAnnotatedHolder.class.getAnnotation(MyAnno.class);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @NoAttrs
    @interface Holder {
    }

    @NoAttrs
    @IntAnno(num = 5)
    static class MixedBean {
    }

    @Test
    public void testGetAttributesEdge() throws Exception {
        // 无属性的注解 + ignoreDefaultValue=true → isEmpty 短路
        NoAttrs noAttrs = MixedBean.class.getAnnotation(NoAttrs.class);
        Assert.assertEquals(0, AnnotationUtil.getAttributes(noAttrs, true).size());
        // 属性为非默认值 + ignoreDefaultValue=true → nullSafeEquals false 分支
        MyAnno methodAnno = AnnotatedBean.class.getMethod("annotatedMethod").getAnnotation(MyAnno.class);
        Map<String, Object> attrs = AnnotationUtil.getAttributes(methodAnno, true);
        Assert.assertEquals("method", attrs.get("value"));
        // 属性为非 String / String[] 类型 → 两个 instanceof 都为 false
        IntAnno intAnno = MixedBean.class.getAnnotation(IntAnno.class);
        Map<String, Object> intAttrs = AnnotationUtil.getAttributes(intAnno, null, false);
        Assert.assertEquals(5, intAttrs.get("num"));
    }

    @Test
    public void testTryMergedNull() throws Exception {
        // 方法本身未直接标注注解（仅类上有）→ merged null → 返回 null
        java.lang.reflect.AnnotatedElement plainMethod = AnnotatedBean.class.getMethod("plainMethod");
        Assert.assertNull(AnnotationUtil.tryGetMergedAnnotationAttributes(plainMethod, MyAnno.class, null, false));
        Assert.assertNull(AnnotationUtil.getAnnotationAttributes(plainMethod, MyAnno.class, null, false, true));
    }

    @Test
    public void testGetAttributesStringArray() throws Exception {
        ArrayAnno annotation = ParameterAnnotatedHolder.class.getMethod("arrayMethod").getAnnotation(ArrayAnno.class);
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("t",
                java.util.Collections.singletonMap("a1", "R1")));
        Map<String, Object> attrs = AnnotationUtil.getAttributes(annotation, env, false);
        String[] values = (String[]) attrs.get("values");
        Assert.assertEquals("R1", values[0]);
        Assert.assertEquals("plain", values[1]);
        // 无 resolver → 原样
        Map<String, Object> attrs2 = AnnotationUtil.getAttributes(annotation, null, false);
        Assert.assertEquals("${a1}", ((String[]) attrs2.get("values"))[0]);
    }

    @Test
    public void testFindAnnotationsOtherType() throws Exception {
        // 参数注解类型不匹配 → equals false
        Map<ElementType, java.util.List<MyAnno>> result = AnnotationUtil.findAnnotations(
                ParameterAnnotatedHolder.class.getMethod("otherParam", String.class, String.class), MyAnno.class);
        Assert.assertEquals(1, result.get(ElementType.PARAMETER).size());
        Assert.assertEquals("y", result.get(ElementType.PARAMETER).get(0).value());
        // OtherAnno 不在 TYPE 上 → 只有 PARAMETER
        Map<ElementType, java.util.List<OtherAnno>> otherResult = AnnotationUtil.findAnnotations(
                ParameterAnnotatedHolder.class.getMethod("otherParam", String.class, String.class), OtherAnno.class);
        Assert.assertEquals(1, otherResult.size());
    }

    @Test
    public void testGetAttributesWithResolver() throws Exception {
        // 用方法上的注解（value=method）
        MyAnno annotation = AnnotatedBean.class.getMethod("annotatedMethod").getAnnotation(MyAnno.class);
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("t",
                java.util.Collections.singletonMap("method", "resolved")));
        MyAnno placeholder = ParameterAnnotatedHolder.class.getMethod("placeholderMethod").getAnnotation(MyAnno.class);
        Map<String, Object> attrs = AnnotationUtil.getAttributes(placeholder, env, false);
        Assert.assertEquals("resolved", attrs.get("value"));
        Map<String, Object> attrsPlain = AnnotationUtil.getAttributes(annotation, env, false);
        Assert.assertEquals("method", attrsPlain.get("value"));
        // 无 resolver → 原样
        Map<String, Object> attrs2 = AnnotationUtil.getAttributes(annotation, null, false);
        Assert.assertEquals("method", attrs2.get("value"));
    }

    @Test
    public void testGetAttribute() throws Exception {
        MyAnno annotation = AnnotatedBean.class.getMethod("annotatedMethod").getAnnotation(MyAnno.class);
        Assert.assertEquals("method", AnnotationUtil.getAttribute(annotation, "value"));
        Assert.assertNull(AnnotationUtil.getAttribute(annotation, "missing"));
        Map<String, Object> attrs = AnnotationUtil.getAttributes(annotation, false);
        Assert.assertEquals("method", AnnotationUtil.getAttribute(attrs, "value"));
        Assert.assertNull(AnnotationUtil.getAttribute(attrs, "missing"));
    }

    @Test
    public void testGetAnnotationAttributes() throws Exception {
        MyAnno annotation = AnnotatedBean.class.getMethod("annotatedMethod").getAnnotation(MyAnno.class);
        Assert.assertEquals("method",
                AnnotationUtil.getAnnotationAttributes(annotation, false).get("value"));
        Assert.assertEquals("method",
                AnnotationUtil.getAnnotationAttributes(annotation, null, false).get("value"));
        // 默认值注解 + ignoreDefaultValue → 空
        MyAnno classAnno = AnnotatedBean.class.getAnnotation(MyAnno.class);
        Assert.assertTrue(
                AnnotationUtil.getAnnotationAttributes(classAnno, true).isEmpty());

        // AnnotatedElement 版本
        Assert.assertEquals("method", AnnotationUtil.getAnnotationAttributes(
                (java.lang.reflect.AnnotatedElement) AnnotatedBean.class.getMethod("annotatedMethod"),
                MyAnno.class, null, false).get("value"));
        Assert.assertNull(AnnotationUtil.getAnnotationAttributes(
                (java.lang.reflect.AnnotatedElement) AnnotatedBean.class.getMethod("plainMethod"),
                FieldOnly.class, null, false));
    }

    @Test
    public void testGetAnnotationAttributesTryMerged() throws Exception {
        java.lang.reflect.AnnotatedElement element = AnnotatedBean.class.getMethod("annotatedMethod");
        // tryMergedAnnotation=true：merged 失败回退普通
        Assert.assertEquals("method", AnnotationUtil.getAnnotationAttributes(
                element, MyAnno.class, null, false, true).get("value"));
        // tryMergedAnnotation=false
        Assert.assertEquals("method", AnnotationUtil.getAnnotationAttributes(
                element, MyAnno.class, null, false, false).get("value"));
        // 类上
        Assert.assertEquals("default", AnnotationUtil.getAnnotationAttributes(
                (java.lang.reflect.AnnotatedElement) AnnotatedBean.class, MyAnno.class, null, false, true).get("value"));
    }

    @Test
    public void testTryGetMergedAnnotation() throws Exception {
        java.lang.reflect.AnnotatedElement element = AnnotatedBean.class.getMethod("annotatedMethod");
        // Spring 6 中 getMergedAnnotation 已移除 → findMethod 返回 null → null
        // Spring 6 的 AnnotatedElementUtils.getMergedAnnotation 仍可用 → 返回注解
        Assert.assertNotNull(AnnotationUtil.tryGetMergedAnnotation(element, MyAnno.class));
        Assert.assertEquals("method",
                AnnotationUtil.tryGetMergedAnnotationAttributes(element, MyAnno.class, null, false).get("value"));
    }

    @Test
    public void testConstant() {
        Assert.assertEquals("org.springframework.core.annotation.AnnotatedElementUtils",
                AnnotationUtil.ANNOTATED_ELEMENT_UTILS_CLASS_NAME);
    }

}