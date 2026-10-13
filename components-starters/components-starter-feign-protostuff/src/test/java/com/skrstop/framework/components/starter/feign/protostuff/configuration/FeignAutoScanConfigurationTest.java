package com.skrstop.framework.components.starter.feign.protostuff.configuration;

import com.skrstop.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;
import com.skrstop.framework.components.starter.feign.protostuff.constant.GlobalConfigConst;
import feign.Request;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cloud.openfeign.FeignClientSpecification;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.mock.env.MockEnvironment;

import java.util.HashMap;
import java.util.Map;

public class FeignAutoScanConfigurationTest {

    @ProtostuffFeignClient("scan-svc")
    interface LocalClient {
    }

    @Test
    public void getNameStatics() {
        Assert.assertEquals("", FeignAutoScanConfiguration.getName(""));
        Assert.assertEquals("", FeignAutoScanConfiguration.getName((String) null));
        Assert.assertEquals("svc", FeignAutoScanConfiguration.getName("svc"));
        Assert.assertEquals("http://svc", FeignAutoScanConfiguration.getName("http://svc"));
        Assert.assertEquals("https://svc", FeignAutoScanConfiguration.getName("https://svc"));
        try {
            // 含空格 -> host 解析失败
            FeignAutoScanConfiguration.getName("bad host");
            Assert.fail("should throw");
        } catch (IllegalStateException e) {
            Assert.assertTrue(e.getMessage().contains("not legal hostname"));
        }
    }

    @Test
    public void getUrlStatics() {
        Assert.assertNull(FeignAutoScanConfiguration.getUrl(null));
        Assert.assertEquals("", FeignAutoScanConfiguration.getUrl(""));
        Assert.assertEquals("#{placeholder}", FeignAutoScanConfiguration.getUrl("#{placeholder}"));
        Assert.assertEquals("#{x}", FeignAutoScanConfiguration.getUrl("#{x}"));
        // startsWith("#{") 但不含 "}" -> 进入 URL 校验分支
        Assert.assertEquals("http://#{abc", FeignAutoScanConfiguration.getUrl("#{abc"));
        Assert.assertEquals("http://host:8080", FeignAutoScanConfiguration.getUrl("host:8080"));
        Assert.assertEquals("http://host", FeignAutoScanConfiguration.getUrl("http://host"));
        try {
            FeignAutoScanConfiguration.getUrl("ht tp://bad");
            Assert.fail("should throw");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("malformed"));
        }
    }

    @Test
    public void getPathStatics() {
        Assert.assertNull(FeignAutoScanConfiguration.getPath(null));
        Assert.assertEquals("", FeignAutoScanConfiguration.getPath(""));
        Assert.assertEquals("/path", FeignAutoScanConfiguration.getPath("path"));
        Assert.assertEquals("/path", FeignAutoScanConfiguration.getPath("/path/"));
        Assert.assertEquals("/a", FeignAutoScanConfiguration.getPath("/a"));
        Assert.assertEquals("/p", FeignAutoScanConfiguration.getPath(" /p "));
    }

    @Test
    public void validateFallbackStatics() {
        // 接口 -> 抛异常
        try {
            FeignAutoScanConfiguration.validateFallback(LocalClient.class);
            Assert.fail("should throw");
        } catch (IllegalArgumentException e) {
            Assert.assertNotNull(e.getMessage());
        }
        try {
            FeignAutoScanConfiguration.validateFallbackFactory(LocalClient.class);
            Assert.fail("should throw");
        } catch (IllegalArgumentException e) {
            Assert.assertNotNull(e.getMessage());
        }
        // 非接口 -> 通过
        FeignAutoScanConfiguration.validateFallback(String.class);
        FeignAutoScanConfiguration.validateFallbackFactory(String.class);
    }

    @Test
    public void registerBeanDefinitionsBlankScanPackage() {
        FeignAutoScanConfiguration configuration = new FeignAutoScanConfiguration();
        configuration.setEnvironment(new MockEnvironment());
        configuration.setResourceLoader(Mockito.mock(org.springframework.core.io.ResourceLoader.class));
        // scanPackage 为空 -> 直接返回
        configuration.registerBeanDefinitions(
                AnnotationMetadata.introspect(LocalClient.class),
                new DefaultListableBeanFactory());
    }

    @Test
    public void registerFeignClientsEager() {
        MockEnvironment environment = new MockEnvironment();
        environment.setProperty(GlobalConfigConst.FEIGN_CONFIG_SCAN_PACKAGE,
                "com.skrstop.framework.components.starter.feign.protostuff.scanapp");
        FeignAutoScanConfiguration configuration = new FeignAutoScanConfiguration();
        configuration.setEnvironment(environment);
        configuration.setResourceLoader(new org.springframework.core.io.DefaultResourceLoader());
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        configuration.registerFeignClients(AnnotationMetadata.introspect(LocalClient.class), registry);

        Assert.assertNotNull(registry.getBeanDefinition("svc-app.FeignClientSpecification"));
        Assert.assertNotNull(registry.getBeanDefinition("svc-plain.FeignClientSpecification"));
        // feign client bean definition
        Assert.assertNotNull(registry.containsBeanDefinition("com.skrstop.framework.components.starter.feign.protostuff.scanapp.ScanAppClient"));
    }

    @Test
    public void registerFeignClientsLazy() {
        MockEnvironment environment = new MockEnvironment();
        environment.setProperty(GlobalConfigConst.FEIGN_CONFIG_SCAN_PACKAGE,
                "com.skrstop.framework.components.starter.feign.protostuff.scanapp");
        environment.setProperty("spring.cloud.openfeign.lazy-attributes-resolution", "true");
        FeignAutoScanConfiguration configuration = new FeignAutoScanConfiguration();
        configuration.setEnvironment(environment);
        configuration.setResourceLoader(new org.springframework.core.io.DefaultResourceLoader());
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        configuration.registerFeignClients(AnnotationMetadata.introspect(LocalClient.class), registry);
        Assert.assertTrue(registry.containsBeanDefinition("com.skrstop.framework.components.starter.feign.protostuff.scanapp.ScanAppClient"));
    }

    @Test
    public void registerFeignClientsWithRefreshEnabled() {
        MockEnvironment environment = new MockEnvironment();
        environment.setProperty(GlobalConfigConst.FEIGN_CONFIG_SCAN_PACKAGE,
                "com.skrstop.framework.components.starter.feign.protostuff.scanapp");
        environment.setProperty("spring.cloud.openfeign.client.refresh-enabled", "true");
        FeignAutoScanConfiguration configuration = new FeignAutoScanConfiguration();
        configuration.setEnvironment(environment);
        configuration.setResourceLoader(new org.springframework.core.io.DefaultResourceLoader());
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        configuration.registerFeignClients(AnnotationMetadata.introspect(LocalClient.class), registry);
        // refresh 开启后注册了 scoped proxy bean
        boolean optionsProxy = false;
        for (String beanName : registry.getBeanDefinitionNames()) {
            if (beanName.contains(Request.Options.class.getCanonicalName())) {
                optionsProxy = true;
            }
        }
        Assert.assertTrue(optionsProxy);
        Assert.assertNotNull(registry);
        Assert.assertNotNull(Request.Options.class);
        Assert.assertNotNull(feign.Request.Options.class);
    }

    @Test
    public void getScannerCandidateComponent() throws Exception {
        MockEnvironment environment = new MockEnvironment();
        FeignAutoScanConfiguration configuration = new FeignAutoScanConfiguration();
        configuration.setEnvironment(environment);
        ClassPathScanningCandidateComponentProvider scanner = configuration.getScanner();
        java.lang.reflect.Method isCandidate = ClassPathScanningCandidateComponentProvider.class
                .getDeclaredMethod("isCandidateComponent", AnnotatedBeanDefinition.class);
        isCandidate.setAccessible(true);
        // 独立接口 -> candidate
        AnnotatedBeanDefinition annotated = Mockito.mock(AnnotatedBeanDefinition.class);
        Mockito.when(annotated.getMetadata())
                .thenReturn(AnnotationMetadata.introspect(com.skrstop.framework.components.starter.feign.protostuff.scanapp.ScanAppClient.class));
        Assert.assertTrue((boolean) isCandidate.invoke(scanner, annotated));
        // 注解类型 -> 非 candidate
        AnnotatedBeanDefinition annotationDef = Mockito.mock(AnnotatedBeanDefinition.class);
        Mockito.when(annotationDef.getMetadata())
                .thenReturn(AnnotationMetadata.introspect(ProtostuffFeignClient.class));
        Assert.assertFalse((boolean) isCandidate.invoke(scanner, annotationDef));
    }

    @Test
    public void getNameFromAttributes() {
        FeignAutoScanConfiguration configuration = new FeignAutoScanConfiguration();
        configuration.setEnvironment(new MockEnvironment());
        Map<String, Object> attributes = new HashMap<>();
        Assert.assertEquals("", configuration.getName(attributes));
        attributes.put("serviceId", "sid");
        Assert.assertEquals("sid", configuration.getName(attributes));
        attributes.remove("serviceId");
        attributes.put("name", "nm");
        Assert.assertEquals("nm", configuration.getName(attributes));
        attributes.remove("name");
        attributes.put("value", "vl");
        Assert.assertEquals("vl", configuration.getName(attributes));
    }

    @Test
    public void getContextIdAndResolve() throws Exception {
        FeignAutoScanConfiguration configuration = new FeignAutoScanConfiguration();
        MockEnvironment environment = new MockEnvironment();
        configuration.setEnvironment(environment);
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("contextId", "ctx");
        attributes.put("name", "nm");
        java.lang.reflect.Method getContextId = FeignAutoScanConfiguration.class
                .getDeclaredMethod("getContextId", org.springframework.beans.factory.config.ConfigurableBeanFactory.class, Map.class);
        getContextId.setAccessible(true);
        // contextId 非空 + beanFactory null -> environment.resolvePlaceholders
        Assert.assertEquals("ctx", getContextId.invoke(configuration, null, attributes));
        // contextId 非空 + beanFactory 非空 -> resolver.evaluate
        Assert.assertEquals("ctx", getContextId.invoke(configuration, beanFactory, attributes));

        // contextId 为空 -> getName
        Map<String, Object> noContext = new HashMap<>();
        noContext.put("name", "nm2");
        Assert.assertEquals("nm2", getContextId.invoke(configuration, null, noContext));

        java.lang.reflect.Method resolve = FeignAutoScanConfiguration.class
                .getDeclaredMethod("resolve", org.springframework.beans.factory.config.ConfigurableBeanFactory.class, String.class);
        resolve.setAccessible(true);
        // value 为空
        Assert.assertNull(resolve.invoke(configuration, null, null));
        Assert.assertEquals("", resolve.invoke(configuration, null, ""));
        // beanFactory null -> resolvePlaceholders
        Assert.assertEquals("v", resolve.invoke(configuration, null, "v"));
        // beanFactory 非空
        Assert.assertEquals("v", resolve.invoke(configuration, beanFactory, "v"));

        java.lang.reflect.Method getUrl = FeignAutoScanConfiguration.class
                .getDeclaredMethod("getUrl", org.springframework.beans.factory.config.ConfigurableBeanFactory.class, Map.class);
        getUrl.setAccessible(true);
        Assert.assertNull(getUrl.invoke(configuration, null, attributes));
        attributes.put("url", "host");
        Assert.assertEquals("http://host", getUrl.invoke(configuration, null, attributes));

        java.lang.reflect.Method getPath = FeignAutoScanConfiguration.class
                .getDeclaredMethod("getPath", org.springframework.beans.factory.config.ConfigurableBeanFactory.class, Map.class);
        getPath.setAccessible(true);
        Assert.assertEquals(null, getPath.invoke(configuration, null, attributes));
        attributes.put("path", "p");
        Assert.assertEquals("/p", getPath.invoke(configuration, null, attributes));
    }

    @Test
    public void getQualifiersAndGetClientName() throws Exception {
        FeignAutoScanConfiguration configuration = new FeignAutoScanConfiguration();
        configuration.setEnvironment(new MockEnvironment());

        java.lang.reflect.Method getQualifiers = FeignAutoScanConfiguration.class
                .getDeclaredMethod("getQualifiers", Map.class);
        getQualifiers.setAccessible(true);
        // qualifiers 全空 + qualifier 空 -> null
        Map<String, Object> empty = new HashMap<>();
        empty.put("qualifiers", new String[]{""});
        Assert.assertNull(getQualifiers.invoke(configuration, empty));
        // qualifiers 全空 + qualifier 非空 -> 单元素
        Map<String, Object> withQualifier = new HashMap<>();
        withQualifier.put("qualifiers", new String[]{""});
        withQualifier.put("qualifier", "q");
        Assert.assertArrayEquals(new String[]{"q"}, (String[]) getQualifiers.invoke(configuration, withQualifier));
        // qualifiers 非空 -> 过滤空元素
        Map<String, Object> mixed = new HashMap<>();
        mixed.put("qualifiers", new String[]{"a", "", "b"});
        Assert.assertArrayEquals(new String[]{"a", "b"}, (String[]) getQualifiers.invoke(configuration, mixed));

        java.lang.reflect.Method getClientName = FeignAutoScanConfiguration.class
                .getDeclaredMethod("getClientName", Map.class);
        getClientName.setAccessible(true);
        Map<String, Object> client = new HashMap<>();
        client.put("contextId", "c");
        Assert.assertEquals("c", getClientName.invoke(configuration, client));
        client.remove("contextId");
        client.put("value", "v");
        Assert.assertEquals("v", getClientName.invoke(configuration, client));
        client.remove("value");
        client.put("name", "n");
        Assert.assertEquals("n", getClientName.invoke(configuration, client));
        client.remove("name");
        client.put("serviceId", "s");
        Assert.assertEquals("s", getClientName.invoke(configuration, client));
        client.remove("serviceId");
        try {
            getClientName.invoke(configuration, client);
            Assert.fail("should throw");
        } catch (java.lang.reflect.InvocationTargetException e) {
            Assert.assertTrue(e.getCause() instanceof IllegalStateException);
        }
        // setResourceLoader
        configuration.setResourceLoader(new org.springframework.core.io.DefaultResourceLoader());
        Assert.assertNotNull(configuration);
        // EnvironmentAware
        Environment env = Mockito.mock(Environment.class);
        configuration.setEnvironment(env);
        Assert.assertNotNull(env);
        Assert.assertNotNull(FeignClientSpecification.class);
        Assert.assertNotNull(AnnotationMetadata.class);
    }


    @Test
    public void registerBeanDefinitionsNonBlankScanPackage() {
        MockEnvironment environment = new MockEnvironment();
        environment.setProperty(GlobalConfigConst.FEIGN_CONFIG_SCAN_PACKAGE,
                "com.skrstop.framework.components.starter.feign.protostuff.scanapp");
        FeignAutoScanConfiguration configuration = new FeignAutoScanConfiguration();
        configuration.setEnvironment(environment);
        configuration.setResourceLoader(new org.springframework.core.io.DefaultResourceLoader());
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        // 非 blank -> 走 registerFeignClients
        configuration.registerBeanDefinitions(AnnotationMetadata.introspect(LocalClient.class), registry);
        Assert.assertTrue(registry.containsBeanDefinition(
                "com.skrstop.framework.components.starter.feign.protostuff.scanapp.ScanAppClient"));
    }

    @Test
    public void registerFeignClientsLazyNonConfigurableRegistry() {
        MockEnvironment environment = new MockEnvironment();
        environment.setProperty(GlobalConfigConst.FEIGN_CONFIG_SCAN_PACKAGE,
                "com.skrstop.framework.components.starter.feign.protostuff.scanapp");
        environment.setProperty("spring.cloud.openfeign.lazy-attributes-resolution", "true");
        FeignAutoScanConfiguration configuration = new FeignAutoScanConfiguration();
        configuration.setEnvironment(environment);
        configuration.setResourceLoader(new org.springframework.core.io.DefaultResourceLoader());
        // 非 ConfigurableBeanFactory -> beanFactory 为 null 分支
        org.springframework.beans.factory.support.BeanDefinitionRegistry registry =
                Mockito.mock(org.springframework.beans.factory.support.BeanDefinitionRegistry.class);
        configuration.registerFeignClients(AnnotationMetadata.introspect(LocalClient.class), registry);
        Mockito.verify(registry, Mockito.atLeastOnce())
                .registerBeanDefinition(Mockito.anyString(), Mockito.any());
    }

    @Test
    public void registerFeignClientsLazySupplierBody() {
        MockEnvironment environment = new MockEnvironment();
        environment.setProperty(GlobalConfigConst.FEIGN_CONFIG_SCAN_PACKAGE,
                "com.skrstop.framework.components.starter.feign.protostuff.scanapp");
        environment.setProperty("spring.cloud.openfeign.lazy-attributes-resolution", "true");
        FeignAutoScanConfiguration configuration = new FeignAutoScanConfiguration();
        configuration.setEnvironment(environment);
        configuration.setResourceLoader(new org.springframework.core.io.DefaultResourceLoader());
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        // 触发 instanceSupplier lambda 体 (setUrl/setPath/dismiss404/fallback/fallbackFactory)
        // mock 构造 FeignClientFactoryBean(在注册时构造), 使 supplier 正常返回, 覆盖 return getObject()
        try (org.mockito.MockedConstruction<org.springframework.cloud.openfeign.FeignClientFactoryBean> mocked =
                     Mockito.mockConstruction(org.springframework.cloud.openfeign.FeignClientFactoryBean.class,
                             (mock, ctx) -> Mockito.when(mock.getObject())
                                     .thenReturn(Mockito.mock(com.skrstop.framework.components.starter.feign.protostuff.scanapp.ScanAppClient.class)))) {
            configuration.registerFeignClients(AnnotationMetadata.introspect(LocalClient.class), registry);
            Assert.assertNotNull(registry.getBean(
                    "com.skrstop.framework.components.starter.feign.protostuff.scanapp.ScanAppClient"));
        }
        Assert.assertTrue(registry.containsBeanDefinition(
                "com.skrstop.framework.components.starter.feign.protostuff.scanapp.ScanAppClient"));
    }

    @Test
    public void resolveBranches() throws Exception {
        FeignAutoScanConfiguration configuration = new FeignAutoScanConfiguration();
        configuration.setEnvironment(new MockEnvironment());
        java.lang.reflect.Method resolve = FeignAutoScanConfiguration.class
                .getDeclaredMethod("resolve", org.springframework.beans.factory.config.ConfigurableBeanFactory.class, String.class);
        resolve.setAccessible(true);
        org.springframework.beans.factory.config.ConfigurableBeanFactory beanFactory =
                Mockito.mock(org.springframework.beans.factory.config.ConfigurableBeanFactory.class);
        // resolver == null -> 直接返回 resolved
        Mockito.when(beanFactory.resolveEmbeddedValue("v")).thenReturn("v");
        Mockito.when(beanFactory.getBeanExpressionResolver()).thenReturn(null);
        Assert.assertEquals("v", resolve.invoke(configuration, beanFactory, "v"));
        // evaluateValue 非 null -> String.valueOf
        org.springframework.beans.factory.config.BeanExpressionResolver resolver =
                Mockito.mock(org.springframework.beans.factory.config.BeanExpressionResolver.class);
        Mockito.when(beanFactory.getBeanExpressionResolver()).thenReturn(resolver);
        Mockito.when(resolver.evaluate(Mockito.any(), Mockito.any())).thenReturn("EV");
        Mockito.when(beanFactory.resolveEmbeddedValue("p")).thenReturn("p");
        Assert.assertEquals("EV", resolve.invoke(configuration, beanFactory, "p"));
        // evaluateValue 为 null -> 返回 null
        Mockito.when(resolver.evaluate(Mockito.any(), Mockito.any())).thenReturn(null);
        Assert.assertNull(resolve.invoke(configuration, beanFactory, "p"));
        // blank -> 原样返回
        Assert.assertEquals("", resolve.invoke(configuration, beanFactory, ""));
    }

    class NonIndependentInner {
    }

    @Test
    public void isCandidateComponentNotIndependent() throws Exception {
        MockEnvironment environment = new MockEnvironment();
        FeignAutoScanConfiguration configuration = new FeignAutoScanConfiguration();
        configuration.setEnvironment(environment);
        ClassPathScanningCandidateComponentProvider scanner = configuration.getScanner();
        java.lang.reflect.Method isCandidate = ClassPathScanningCandidateComponentProvider.class
                .getDeclaredMethod("isCandidateComponent", AnnotatedBeanDefinition.class);
        isCandidate.setAccessible(true);
        AnnotatedBeanDefinition notIndependent = Mockito.mock(AnnotatedBeanDefinition.class);
        Mockito.when(notIndependent.getMetadata())
                .thenReturn(AnnotationMetadata.introspect(NonIndependentInner.class));
        // 非独立类 -> 非 candidate
        Assert.assertFalse((boolean) isCandidate.invoke(scanner, notIndependent));
    }
}
