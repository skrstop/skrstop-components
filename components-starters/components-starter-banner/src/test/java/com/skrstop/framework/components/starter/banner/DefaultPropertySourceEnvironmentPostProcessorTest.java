package com.skrstop.framework.components.starter.banner;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.StandardEnvironment;

/**
 * DefaultPropertySourceEnvironmentPostProcessor 单元测试
 */
public class DefaultPropertySourceEnvironmentPostProcessorTest {

    @Test
    public void testPostProcessEnvironment() {
        DefaultPropertySourceEnvironmentPostProcessor processor = new DefaultPropertySourceEnvironmentPostProcessor();
        StandardEnvironment environment = new StandardEnvironment();
        SpringApplication application = new SpringApplication();
        processor.postProcessEnvironment(environment, application);

        Assert.assertNotNull(environment.getPropertySources().get(DefaultPropertySourceEnvironmentPostProcessor.PROPERTY_SOURCE_NAME));
        // 添加在最后（最低优先级）
        java.util.List<org.springframework.core.env.PropertySource<?>> all = new java.util.ArrayList<>();
        environment.getPropertySources().iterator().forEachRemaining(all::add);
        Assert.assertEquals(DefaultPropertySourceEnvironmentPostProcessor.PROPERTY_SOURCE_NAME, all.get(all.size() - 1).getName());
        // banner 模块自带默认配置文件，应加载其中内容
        CompositePropertySource ps = (CompositePropertySource)
                environment.getPropertySources().get(DefaultPropertySourceEnvironmentPostProcessor.PROPERTY_SOURCE_NAME);
        Assert.assertFalse(ps.getPropertySources().isEmpty());
    }

    @Test
    public void testCustomResourceLoader() {
        DefaultPropertySourceEnvironmentPostProcessor processor = new DefaultPropertySourceEnvironmentPostProcessor();
        StandardEnvironment environment = new StandardEnvironment();
        SpringApplication application = new SpringApplication();
        application.setResourceLoader(new org.springframework.core.io.DefaultResourceLoader());
        processor.postProcessEnvironment(environment, application);
        Assert.assertNotNull(environment.getPropertySources().get(DefaultPropertySourceEnvironmentPostProcessor.PROPERTY_SOURCE_NAME));
    }

    @Test
    public void testIOExceptionWrapped() {
        DefaultPropertySourceEnvironmentPostProcessor processor = new DefaultPropertySourceEnvironmentPostProcessor() {
            @Override
            protected org.springframework.core.env.PropertySource buildPropertySource(
                    org.springframework.core.io.ResourceLoader resourceLoader) throws java.io.IOException {
                throw new java.io.IOException("boom");
            }
        };
        try {
            processor.postProcessEnvironment(new StandardEnvironment(), new SpringApplication());
            Assert.fail("should throw");
        } catch (IllegalStateException e) {
            Assert.assertEquals("boom", e.getMessage());
        }
    }

    @Test
    public void testOrder() {
        Assert.assertEquals(Integer.MAX_VALUE, new DefaultPropertySourceEnvironmentPostProcessor().getOrder());
        Assert.assertEquals("spring-context-default", DefaultPropertySourceEnvironmentPostProcessor.PROPERTY_SOURCE_NAME);
        Assert.assertEquals("classpath*:META-INF/spring-context-default.properties",
                DefaultPropertySourceEnvironmentPostProcessor.RESOURCE_LOCATION_PATTERN);
    }
}
