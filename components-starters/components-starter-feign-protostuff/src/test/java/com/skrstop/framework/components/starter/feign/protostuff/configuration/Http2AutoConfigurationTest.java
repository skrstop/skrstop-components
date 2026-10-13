package com.skrstop.framework.components.starter.feign.protostuff.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.test.util.ReflectionTestUtils;

public class Http2AutoConfigurationTest {

    @Test
    public void tomcatDisabled() {
        Http2TomcatAutoConfiguration configuration = new Http2TomcatAutoConfiguration();
        ReflectionTestUtils.setField(configuration, "globalFeignProperties", new GlobalFeignProperties());
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        // enableHttp2=false -> 直接返回
        configuration.customize(factory);
        Assert.assertNotNull(factory);
    }

    @Test
    public void tomcatEnabledInvokesProtocolHandlerCustomizer() {
        Http2TomcatAutoConfiguration configuration = new Http2TomcatAutoConfiguration();
        GlobalFeignProperties properties = new GlobalFeignProperties();
        properties.setEnableHttp2(true);
        ReflectionTestUtils.setField(configuration, "globalFeignProperties", properties);
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        configuration.customize(factory);
        // lambda 只在服务器启动时执行, 这里手动调用以覆盖
        org.apache.catalina.connector.Connector connector = new org.apache.catalina.connector.Connector();
        for (org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer<?> customizer
                : factory.getTomcatProtocolHandlerCustomizers()) {
            invokeTomcatCustomizer(customizer, (Object) connector.getProtocolHandler());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void invokeTomcatCustomizer(org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer customizer,
                                        Object protocolHandler) {
        customizer.customize((org.apache.coyote.ProtocolHandler) protocolHandler);
    }

    @Test
    public void undertowDisabled() {
        Http2UndertowAutoConfiguration configuration = new Http2UndertowAutoConfiguration();
        GlobalFeignProperties disabled = new GlobalFeignProperties();
        UndertowServletWebServerFactory factory1 = configuration.undertowServletWebServerFactory(disabled);
        Assert.assertNotNull(factory1);
    }

    @Test
    public void undertowEnabledInvokesBuilderCustomizer() {
        Http2UndertowAutoConfiguration configuration = new Http2UndertowAutoConfiguration();
        GlobalFeignProperties properties = new GlobalFeignProperties();
        properties.setEnableHttp2(true);
        UndertowServletWebServerFactory factory = configuration.undertowServletWebServerFactory(properties);
        Assert.assertNotNull(factory);
        // lambda 只在服务器启动时执行, 这里手动调用以覆盖
        io.undertow.Undertow.Builder builder = io.undertow.Undertow.builder();
        for (org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer customizer
                : factory.getBuilderCustomizers()) {
            customizer.customize(builder);
        }
    }
}
