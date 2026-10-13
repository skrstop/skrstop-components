package com.skrstop.framework.components.starter.annotation.handle.function;

import com.skrstop.framework.components.starter.annotation.anno.function.PrivacyInfo;
import com.skrstop.framework.components.starter.annotation.anno.function.PrivacyInfoValue;
import com.skrstop.framework.components.starter.annotation.configuration.AnnotationProperties;
import com.skrstop.framework.components.starter.annotation.handle.function.privacyInfo.PrivacyInfoTypeRule;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author 蒋时华
 */
public class PrivacyInfoAnnotationInterceptorTest {

    @Data
    public static class UserVO {
        @PrivacyInfoValue(type = com.skrstop.framework.components.starter.annotation.constant.PrivacyInfoType.DEFAULT)
        private String name = "张三丰";
        @PrivacyInfoValue(type = com.skrstop.framework.components.starter.annotation.constant.PrivacyInfoType.SET_NULL)
        private String secret = "secret";
        @PrivacyInfoValue(type = com.skrstop.framework.components.starter.annotation.constant.PrivacyInfoType.DEFAULT_BANK_CARD)
        private String bankCard = "6222021234567890123";
        @PrivacyInfoValue(type = com.skrstop.framework.components.starter.annotation.constant.PrivacyInfoType.DEFAULT_ID_CARD)
        private String idCard = "110101199001011234";
        @PrivacyInfoValue(type = com.skrstop.framework.components.starter.annotation.constant.PrivacyInfoType.DEFAULT_PHONE)
        private String phone = "13800138000";
        @PrivacyInfoValue(type = com.skrstop.framework.components.starter.annotation.constant.PrivacyInfoType.DEFAULT_EMAIL)
        private String email = "yaaachaa@163.com";
        @PrivacyInfoValue(type = "custom")
        private String custom = "customValue";
        @PrivacyInfoValue(type = "missing-rule")
        private String missingRule = "mr";
        @PrivacyInfoValue(type = com.skrstop.framework.components.starter.annotation.constant.PrivacyInfoType.DEFAULT)
        private String blank = "";
        private String noAnno = "keep";
        private Integer num = 1;
        @PrivacyInfoValue(type = com.skrstop.framework.components.starter.annotation.constant.PrivacyInfoType.DEFAULT, limitIntranet = false)
        private String innerOnly = "inner";
        // 非 hutool 基本类型 → 走递归 setInfo
        private Object obj = new Object();
        private NestedVO nested = new NestedVO();
        // 覆盖 isPrimitive 各类型分支
        private java.sql.Timestamp ts = new java.sql.Timestamp(0);
        private java.sql.Time time = new java.sql.Time(0);
        private java.util.Date date = new java.util.Date();
        private java.time.LocalDateTime ldt = java.time.LocalDateTime.now();
        private java.time.LocalDate ld = java.time.LocalDate.now();
        private java.time.LocalTime lt = java.time.LocalTime.now();
        private java.math.BigDecimal bd = java.math.BigDecimal.ONE;

        // 无字段的计算属性 → ReflectUtil.getField 为 null
        public String getComputed() {
            return "computed";
        }
    }

    public static class NestedVO {
        private String nestedName = "nested";
        private Integer nestedNum = 2;
    }

    public static class ThrowBean {
        @PrivacyInfoValue(type = com.skrstop.framework.components.starter.annotation.constant.PrivacyInfoType.SET_NULL)
        private String val = "v";

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            throw new IllegalStateException("boom");
        }
    }

    public static class CustomRule implements PrivacyInfoTypeRule {
        @Override
        public Set<String> supportType() {
            return Collections.singleton("custom");
        }

        @Override
        public String handle(String originVal) {
            return "handled:" + originVal;
        }
    }

    public static class EmptyTypeRule implements PrivacyInfoTypeRule {
        @Override
        public Set<String> supportType() {
            return Collections.emptySet();
        }

        @Override
        public String handle(String originVal) {
            return originVal;
        }
    }

    public static class Service {
        @PrivacyInfo
        public UserVO annotated() {
            return new UserVO();
        }

        @PrivacyInfo
        public java.util.List<UserVO> annotatedList() {
            return Arrays.asList(new UserVO());
        }

        @PrivacyInfo
        public java.util.List<UserVO> emptyList() {
            return Collections.emptyList();
        }

        @PrivacyInfo
        public Map<String, UserVO> annotatedMap() {
            Map<String, UserVO> map = new HashMap<>();
            map.put("u", new UserVO());
            return map;
        }

        @PrivacyInfo
        public Map<String, UserVO> emptyMap() {
            return new HashMap<>();
        }

        @PrivacyInfo
        public Map<String, UserVO> mapWithNullValue() {
            Map<String, UserVO> map = new HashMap<>();
            map.put("n", null);
            return map;
        }

        public UserVO plain() {
            return new UserVO();
        }

        @PrivacyInfo
        public java.util.Map<String, Object> mapWithBrokenGet() {
            return new HashMap<String, Object>() {
                {
                    put("k", "v");
                }

                @Override
                public Object get(Object key) {
                    throw new IllegalStateException("boom");
                }
            };
        }

        @PrivacyInfo
        public java.util.Map<String, ThrowBean> mapWithThrowBean() {
            java.util.Map<String, ThrowBean> map = new HashMap<>();
            map.put("t", new ThrowBean());
            return map;
        }
    }

    private static Object invoke(Service service, String methodName,
                                 PrivacyInfoAnnotationInterceptor interceptor) throws Throwable {
        Method method = Service.class.getMethod(methodName);
        return interceptor.invoke(new ReflectiveMethodInvocation(
                service, service, method, new Object[0], null, Collections.emptyList()) {
            @Override
            public Object proceed() {
                try {
                    return method.invoke(service);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Test
    public void testRuleRegistration() {
        PrivacyInfoAnnotationInterceptor interceptor = new PrivacyInfoAnnotationInterceptor(
                new AnnotationProperties(), Arrays.asList(new CustomRule(), new EmptyTypeRule()));
        // 空规则列表
        new PrivacyInfoAnnotationInterceptor(new AnnotationProperties(), Collections.emptyList());
        // 通过 custom 规则生效验证注册
        PrivacyInfoAnnotationInterceptor withRule = new PrivacyInfoAnnotationInterceptor(
                new AnnotationProperties(), Collections.singletonList(new CustomRule()));
        Assert.assertNotNull(withRule);
        Assert.assertNotNull(interceptor);
    }

    @Test
    public void testNoAnnotationPassthrough() throws Throwable {
        PrivacyInfoAnnotationInterceptor interceptor = new PrivacyInfoAnnotationInterceptor(
                new AnnotationProperties(), Collections.emptyList());
        UserVO result = (UserVO) invoke(new Service(), "plain", interceptor);
        Assert.assertEquals("张三丰", result.getName());
    }

    @Test
    public void testAnnotatedObjectMasking() throws Throwable {
        PrivacyInfoAnnotationInterceptor interceptor = new PrivacyInfoAnnotationInterceptor(
                new AnnotationProperties(), Collections.singletonList(new CustomRule()));
        UserVO result = (UserVO) invoke(new Service(), "annotated", interceptor);
        Assert.assertNotEquals("张三丰", result.getName());
        Assert.assertNotEquals("6222021234567890123", result.getBankCard());
        Assert.assertNotEquals("110101199001011234", result.getIdCard());
        Assert.assertNotEquals("13800138000", result.getPhone());
        Assert.assertNotEquals("yaaachaa@163.com", result.getEmail());
        Assert.assertEquals("handled:customValue", result.getCustom());
        Assert.assertNull(result.getSecret());
        Assert.assertEquals("keep", result.getNoAnno());
        Assert.assertEquals(Integer.valueOf(1), result.getNum());
        // 无注解的 String 字段不受影响
        Assert.assertEquals("", result.getBlank());
        // 内网 + limitIntranet=false → 跳过
        Assert.assertEquals("inner", result.getInnerOnly());
        // missing-rule：规则不存在 → break，值不变
        Assert.assertEquals("mr", result.getMissingRule());
        // 非基本类型属性 → 递归处理
        Assert.assertNotNull(result.getNested());
        Assert.assertEquals("computed", result.getComputed());
    }

    @Test
    public void testMapGetThrowsIsSwallowed() throws Throwable {
        PrivacyInfoAnnotationInterceptor interceptor = new PrivacyInfoAnnotationInterceptor(
                new AnnotationProperties(), Collections.emptyList());
        Object result = invoke(new Service(), "mapWithBrokenGet", interceptor);
        Assert.assertNotNull(result);
    }

    @Test
    public void testMapValueThrowsIsSwallowed() throws Throwable {
        PrivacyInfoAnnotationInterceptor interceptor = new PrivacyInfoAnnotationInterceptor(
                new AnnotationProperties(), Collections.emptyList());
        Object result = invoke(new Service(), "mapWithThrowBean", interceptor);
        Assert.assertNotNull(result);
    }

    @Test
    public void testInnerIpFalseMasksEvenWhenLimitFalse() throws Throwable {
        PrivacyInfoAnnotationInterceptor interceptor = new PrivacyInfoAnnotationInterceptor(
                new AnnotationProperties(), Collections.emptyList());
        org.springframework.mock.web.MockHttpServletRequest request =
                new org.springframework.mock.web.MockHttpServletRequest();
        request.setRemoteAddr("8.8.8.8");
        org.springframework.web.context.request.RequestContextHolder.setRequestAttributes(
                new org.springframework.web.context.request.ServletRequestAttributes(request));
        try {
            UserVO result = (UserVO) invoke(new Service(), "annotated", interceptor);
            // 外网访问时 limitIntranet=false 的字段也会脱敏
            Assert.assertNotEquals("inner", result.getInnerOnly());
        } finally {
            org.springframework.web.context.request.RequestContextHolder.resetRequestAttributes();
        }
    }

    @Test
    public void testRequestNullAndBlankIpAndException() throws Throwable {
        PrivacyInfoAnnotationInterceptor interceptor = new PrivacyInfoAnnotationInterceptor(
                new AnnotationProperties(), Collections.emptyList());
        // request 为 null
        org.springframework.web.context.request.RequestAttributes ra =
                org.mockito.Mockito.mock(org.springframework.web.context.request.ServletRequestAttributes.class);
        org.springframework.web.context.request.RequestContextHolder.setRequestAttributes(ra);
        UserVO result = (UserVO) invoke(new Service(), "annotated", interceptor);
        Assert.assertNotNull(result);
        org.springframework.web.context.request.RequestContextHolder.resetRequestAttributes();
        // ip 为空 → innerIp false
        org.springframework.mock.web.MockHttpServletRequest blankRequest =
                new org.springframework.mock.web.MockHttpServletRequest();
        blankRequest.setRemoteAddr("");
        org.springframework.web.context.request.RequestContextHolder.setRequestAttributes(
                new org.springframework.web.context.request.ServletRequestAttributes(blankRequest));
        result = (UserVO) invoke(new Service(), "annotated", interceptor);
        Assert.assertNotNull(result);
        org.springframework.web.context.request.RequestContextHolder.resetRequestAttributes();
        // 获取 ip 抛异常 → catch → innerIp false
        jakarta.servlet.http.HttpServletRequest throwingRequest =
                (jakarta.servlet.http.HttpServletRequest) java.lang.reflect.Proxy.newProxyInstance(
                        jakarta.servlet.http.HttpServletRequest.class.getClassLoader(),
                        new Class[]{jakarta.servlet.http.HttpServletRequest.class},
                        (proxy, method, args) -> {
                            if ("getHeader".equals(method.getName())) {
                                throw new IllegalStateException("boom");
                            }
                            if ("getRemoteAddr".equals(method.getName())) {
                                return "";
                            }
                            if ("equals".equals(method.getName())) {
                                return proxy == args[0];
                            }
                            if ("hashCode".equals(method.getName())) {
                                return System.identityHashCode(proxy);
                            }
                            if ("toString".equals(method.getName())) {
                                return "throwingRequest";
                            }
                            return null;
                        });
        org.springframework.web.context.request.RequestContextHolder.setRequestAttributes(
                new org.springframework.web.context.request.ServletRequestAttributes(throwingRequest));
        result = (UserVO) invoke(new Service(), "annotated", interceptor);
        Assert.assertNotNull(result);
        org.springframework.web.context.request.RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testCollectionAndMap() throws Throwable {
        PrivacyInfoAnnotationInterceptor interceptor = new PrivacyInfoAnnotationInterceptor(
                new AnnotationProperties(), Collections.singletonList(new CustomRule()));
        java.util.List<UserVO> list = (java.util.List<UserVO>) invoke(new Service(), "annotatedList", interceptor);
        Assert.assertNotEquals("张三丰", list.get(0).getName());
        // 空集合
        java.util.List<UserVO> empty = (java.util.List<UserVO>) invoke(new Service(), "emptyList", interceptor);
        Assert.assertTrue(empty.isEmpty());
        // map
        Map<String, UserVO> map = (Map<String, UserVO>) invoke(new Service(), "annotatedMap", interceptor);
        Assert.assertNotEquals("张三丰", map.get("u").getName());
        // 空 map
        Map<String, UserVO> emptyMap = (Map<String, UserVO>) invoke(new Service(), "emptyMap", interceptor);
        Assert.assertTrue(emptyMap.isEmpty());
        // map 值为 null → catch 分支
        Map<String, UserVO> nullMap = (Map<String, UserVO>) invoke(new Service(), "mapWithNullValue", interceptor);
        Assert.assertNull(nullMap.get("n"));
    }

}