package com.skrstop.framework.components.starter.feign.protostuff.configuration;

import com.skrstop.framework.components.starter.feign.protostuff.constant.GlobalConfigConst;
import com.skrstop.framework.components.util.constant.StringPoolConst;
import com.skrstop.framework.components.util.value.data.StrUtil;
import feign.Request;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cloud.openfeign.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * @author 蒋时华
 * @date 2023-12-04 17:45:33
 */
public class FeignAutoScanConfiguration implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    // patterned after Spring Integration IntegrationComponentScanRegistrar
    // and RibbonClientsConfigurationRegistgrar

    private ResourceLoader resourceLoader;

    private Environment environment;

    private String scanPackage;

    static void validateFallback(final Class clazz) {
        Assert.isTrue(!clazz.isInterface(), "Fallback class must implement the interface annotated by @FeignClient");
    }

    static void validateFallbackFactory(final Class clazz) {
        Assert.isTrue(!clazz.isInterface(), "Fallback factory must produce instances "
                + "of fallback classes that implement the interface annotated by @FeignClient");
    }

    static String getName(String name) {
        if (!StringUtils.hasText(name)) {
            return "";
        }

        String host = null;
        try {
            String url;
            if (!name.startsWith("http://") && !name.startsWith("https://")) {
                url = "http://" + name;
            } else {
                url = name;
            }
            host = new URI(url).getHost();

        } catch (URISyntaxException e) {
        }
        Assert.state(host != null, "Service id not legal hostname (" + name + ")");
        return name;
    }

    static String getUrl(String url) {
        if (StringUtils.hasText(url) && !(url.startsWith("#{") && url.contains("}"))) {
            if (!url.contains("://")) {
                url = "http://" + url;
            }
            try {
                new URL(url);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(url + " is malformed", e);
            }
        }
        return url;
    }

    static String getPath(String path) {
        if (StringUtils.hasText(path)) {
            path = path.trim();
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
        }
        return path;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        if (StrUtil.isBlank(scanPackage)) {
            return;
        }
        registerFeignClients(metadata, registry);
    }

    public void registerFeignClients(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        LinkedHashSet<BeanDefinition> candidateComponents = new LinkedHashSet<>();
        // 扫包
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter(FeignClient.class));
        Set<String> basePackages = new LinkedHashSet<>(StrUtil.splitTrim(this.scanPackage, StringPoolConst.COMMA));
        for (String basePackage : basePackages) {
            candidateComponents.addAll(scanner.findCandidateComponents(basePackage));
        }
        for (BeanDefinition candidateComponent : candidateComponents) {
            if (candidateComponent instanceof AnnotatedBeanDefinition beanDefinition) {
                // verify annotated class is an interface
                AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                Assert.isTrue(annotationMetadata.isInterface(), "@FeignClient can only be specified on an interface");
                Map<String, Object> attributes = annotationMetadata
                        .getAnnotationAttributes(FeignClient.class.getCanonicalName());
                String name = getClientName(attributes);
                String className = annotationMetadata.getClassName();
                registerClientConfiguration(registry, name, className, attributes.get("configuration"));
                registerFeignClient(registry, annotationMetadata, attributes);
            }
        }
    }

    private void registerFeignClient(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata,
                                     Map<String, Object> attributes) {
        String className = annotationMetadata.getClassName();
        if (String.valueOf(false).equals(environment.getProperty("spring.cloud.openfeign.lazy-attributes-resolution", String.valueOf(false)))) {
            eagerlyRegisterFeignClientBeanDefinition(className, attributes, registry);
        } else {
            lazilyRegisterFeignClientBeanDefinition(className, attributes, registry);
        }
    }

    private void eagerlyRegisterFeignClientBeanDefinition(String className, Map<String, Object> attributes,
                                                          BeanDefinitionRegistry registry) {
        validate(attributes);
        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(FeignClientFactoryBean.class);
        definition.addPropertyValue("url", getUrl(null, attributes));
        definition.addPropertyValue("path", getPath(null, attributes));
        String name = getName(attributes);
        definition.addPropertyValue("name", name);
        String contextId = getContextId(null, attributes);
        definition.addPropertyValue("contextId", contextId);
        definition.addPropertyValue("type", className);
        definition.addPropertyValue("dismiss404", Boolean.parseBoolean(String.valueOf(attributes.get("dismiss404"))));
        Object fallback = attributes.get("fallback");
        if (fallback != null) {
            definition.addPropertyValue("fallback",
                    (fallback instanceof Class ? fallback : ClassUtils.resolveClassName(fallback.toString(), null)));
        }
        Object fallbackFactory = attributes.get("fallbackFactory");
        if (fallbackFactory != null) {
            definition.addPropertyValue("fallbackFactory", fallbackFactory instanceof Class ? fallbackFactory
                    : ClassUtils.resolveClassName(fallbackFactory.toString(), null));
        }
        definition.addPropertyValue("fallbackFactory", attributes.get("fallbackFactory"));
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        definition.addPropertyValue("refreshableClient", isClientRefreshEnabled());
        String[] qualifiers = getQualifiers(attributes);
        if (ObjectUtils.isEmpty(qualifiers)) {
            qualifiers = new String[]{contextId + "FeignClient"};
        }
        // This is done so that there's a way to retrieve qualifiers while generating AOT
        // code
        definition.addPropertyValue("qualifiers", qualifiers);
        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        // 修复高版本不兼容问题
//        beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, className);
        // has a default, won't be null
        boolean primary = (Boolean) attributes.get("primary");
        beanDefinition.setPrimary(primary);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className, qualifiers);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
        registerRefreshableBeanDefinition(registry, contextId, Request.Options.class, OptionsFactoryBean.class);
        registerRefreshableBeanDefinition(registry, contextId, RefreshableUrl.class, RefreshableUrlFactoryBean.class);
    }

    private void lazilyRegisterFeignClientBeanDefinition(String className, Map<String, Object> attributes,
                                                         BeanDefinitionRegistry registry) {
        ConfigurableBeanFactory beanFactory = registry instanceof ConfigurableBeanFactory
                ? (ConfigurableBeanFactory) registry : null;
        Class clazz = ClassUtils.resolveClassName(className, null);
        String contextId = getContextId(beanFactory, attributes);
        String name = getName(attributes);
        FeignClientFactoryBean factoryBean = new FeignClientFactoryBean();
        factoryBean.setBeanFactory(beanFactory);
        factoryBean.setName(name);
        factoryBean.setContextId(contextId);
        factoryBean.setType(clazz);
        factoryBean.setRefreshableClient(isClientRefreshEnabled());
        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(clazz, () -> {
            factoryBean.setUrl(getUrl(beanFactory, attributes));
            factoryBean.setPath(getPath(beanFactory, attributes));
            factoryBean.setDismiss404(Boolean.parseBoolean(String.valueOf(attributes.get("dismiss404"))));
            Object fallback = attributes.get("fallback");
            if (fallback != null) {
                factoryBean.setFallback(fallback instanceof Class ? (Class<?>) fallback
                        : ClassUtils.resolveClassName(fallback.toString(), null));
            }
            Object fallbackFactory = attributes.get("fallbackFactory");
            if (fallbackFactory != null) {
                factoryBean.setFallbackFactory(fallbackFactory instanceof Class ? (Class<?>) fallbackFactory
                        : ClassUtils.resolveClassName(fallbackFactory.toString(), null));
            }
            return factoryBean.getObject();
        });
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        definition.setLazyInit(true);
        validate(attributes);

        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        // 修复高版本不兼容问题
//        beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, className);
        beanDefinition.setAttribute("feignClientsRegistrarFactoryBean", factoryBean);

        // has a default, won't be null
        boolean primary = (Boolean) attributes.get("primary");

        beanDefinition.setPrimary(primary);

        String[] qualifiers = getQualifiers(attributes);
        if (ObjectUtils.isEmpty(qualifiers)) {
            qualifiers = new String[]{contextId + "FeignClient"};
        }

        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className, qualifiers);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);

        registerRefreshableBeanDefinition(registry, contextId, Request.Options.class, OptionsFactoryBean.class);
        registerRefreshableBeanDefinition(registry, contextId, RefreshableUrl.class, RefreshableUrlFactoryBean.class);
    }

    private void registerRefreshableBeanDefinition(BeanDefinitionRegistry registry, String contextId, Class<?> beanType,
                                                   Class<?> factoryBeanType) {
        if (isClientRefreshEnabled()) {
            String beanName = beanType.getCanonicalName() + "-" + contextId;
            BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(factoryBeanType);
            definitionBuilder.setScope("refresh");
            definitionBuilder.addPropertyValue("contextId", contextId);
            BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(definitionBuilder.getBeanDefinition(),
                    beanName);
            definitionHolder = ScopedProxyUtils.createScopedProxy(definitionHolder, registry, true);
            BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
        }
    }

    private void validate(Map<String, Object> attributes) {
        AnnotationAttributes annotation = AnnotationAttributes.fromMap(attributes);
        // This blows up if an aliased property is overspecified
        // FIXME annotation.getAliasedString("name", FeignClient.class, null);
        validateFallback(annotation.getClass("fallback"));
        validateFallbackFactory(annotation.getClass("fallbackFactory"));
    }

    /* for testing */ String getName(Map<String, Object> attributes) {
        return getName(null, attributes);
    }

    String getName(ConfigurableBeanFactory beanFactory, Map<String, Object> attributes) {
        String name = (String) attributes.get("serviceId");
        if (!StringUtils.hasText(name)) {
            name = (String) attributes.get("name");
        }
        if (!StringUtils.hasText(name)) {
            name = (String) attributes.get("value");
        }
        name = resolve(beanFactory, name);
        return getName(name);
    }

    private String getContextId(ConfigurableBeanFactory beanFactory, Map<String, Object> attributes) {
        String contextId = (String) attributes.get("contextId");
        if (!StringUtils.hasText(contextId)) {
            return getName(attributes);
        }

        contextId = resolve(beanFactory, contextId);
        return getName(contextId);
    }

    private String resolve(ConfigurableBeanFactory beanFactory, String value) {
        if (StringUtils.hasText(value)) {
            if (beanFactory == null) {
                return this.environment.resolvePlaceholders(value);
            }
            BeanExpressionResolver resolver = beanFactory.getBeanExpressionResolver();
            String resolved = beanFactory.resolveEmbeddedValue(value);
            if (resolver == null) {
                return resolved;
            }
            Object evaluateValue = resolver.evaluate(resolved, new BeanExpressionContext(beanFactory, null));
            if (evaluateValue != null) {
                return String.valueOf(evaluateValue);
            }
            return null;
        }
        return value;
    }

    private String getUrl(ConfigurableBeanFactory beanFactory, Map<String, Object> attributes) {
        String url = resolve(beanFactory, (String) attributes.get("url"));
        return getUrl(url);
    }

    private String getPath(ConfigurableBeanFactory beanFactory, Map<String, Object> attributes) {
        String path = resolve(beanFactory, (String) attributes.get("path"));
        return getPath(path);
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }

    private String getQualifier(Map<String, Object> client) {
        if (client == null) {
            return null;
        }
        String qualifier = (String) client.get("qualifier");
        if (StringUtils.hasText(qualifier)) {
            return qualifier;
        }
        return null;
    }

    private String[] getQualifiers(Map<String, Object> client) {
        if (client == null) {
            return null;
        }
        List<String> qualifierList = new ArrayList<>(Arrays.asList((String[]) client.get("qualifiers")));
        qualifierList.removeIf(qualifier -> !StringUtils.hasText(qualifier));
        if (qualifierList.isEmpty() && getQualifier(client) != null) {
            qualifierList = Collections.singletonList(getQualifier(client));
        }
        return !qualifierList.isEmpty() ? qualifierList.toArray(new String[0]) : null;
    }

    private String getClientName(Map<String, Object> client) {
        if (client == null) {
            return null;
        }
        String value = (String) client.get("contextId");
        if (!StringUtils.hasText(value)) {
            value = (String) client.get("value");
        }
        if (!StringUtils.hasText(value)) {
            value = (String) client.get("name");
        }
        if (!StringUtils.hasText(value)) {
            value = (String) client.get("serviceId");
        }
        if (StringUtils.hasText(value)) {
            return value;
        }

        throw new IllegalStateException(
                "Either 'name' or 'value' must be provided in @" + FeignClient.class.getSimpleName());
    }

    private void registerClientConfiguration(BeanDefinitionRegistry registry, Object name, Object className,
                                             Object configuration) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(FeignClientSpecification.class);
        builder.addConstructorArgValue(name);
        builder.addConstructorArgValue(className);
        builder.addConstructorArgValue(configuration);
        registry.registerBeanDefinition(name + "." + FeignClientSpecification.class.getSimpleName(),
                builder.getBeanDefinition());
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        this.scanPackage = environment.getProperty(GlobalConfigConst.FEIGN_CONFIG_SCAN_PACKAGE);
    }

    private boolean isClientRefreshEnabled() {
        return environment.getProperty("spring.cloud.openfeign.client.refresh-enabled", Boolean.class, false);
    }

}
