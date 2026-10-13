package com.skrstop.framework.components.starter.annotation.configuration;

import com.skrstop.framework.components.starter.annotation.handle.function.privacyInfo.PrivacyInfoTypeRule;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.Collections;
import java.util.Set;

public class AnnotationAutoConfigurationTest {

    public static class EmptyRule implements PrivacyInfoTypeRule {
        @Override
        public Set<String> supportType() {
            return Collections.emptySet();
        }

        @Override
        public String handle(String originVal) {
            return originVal;
        }
    }

    private final AnnotationAutoConfiguration configuration = new AnnotationAutoConfiguration();

    @Test
    public void testBeans() {
        Assert.assertNotNull(configuration.trimAnnotationFormatterFactory());
        Assert.assertNotNull(configuration.processorContainerConfiguration());
        AnnotationProperties properties = new AnnotationProperties();
        Assert.assertNotNull(configuration.executeTimeLog(properties));
        Assert.assertNotNull(configuration.accessControl(properties));
        Assert.assertNotNull(configuration.intranetLimit(properties));
        Assert.assertNotNull(configuration.accessLimit(properties));
        Assert.assertNotNull(configuration.serviceLock(properties));
        org.springframework.aop.Advisor privacyAdvisor = configuration.privacyInfo(properties, Collections.singletonList(new EmptyRule()));
        Assert.assertNotNull(privacyAdvisor);
        Assert.assertEquals(5, ((org.springframework.core.Ordered) privacyAdvisor).getOrder());
    }

    @Test
    public void testWebMvcRegistrations() {
        RequestMappingHandlerAdapter adapter = configuration.webMvcRegistrations()
                .getRequestMappingHandlerAdapter();
        Assert.assertNotNull(adapter);
        Assert.assertTrue(adapter instanceof com.skrstop.framework.components.starter.annotation.handle.paramAlias.AliasRequestMappingHandlerAdapter);
    }
}
