package com.skrstop.framework.components.starter.portShift;

import com.skrstop.framework.components.starter.portShift.configuration.GlobalPortProperties;
import com.skrstop.framework.components.starter.portShift.configuration.PortShiftAutoConfiguration;
import com.skrstop.framework.components.starter.portShift.constant.GlobalConfigConst;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.server.AbstractConfigurableWebServerFactory;

/**
 * port shift starter 单元测试
 */
public class PortShiftTest {

    private static class TestWebServerFactory extends AbstractConfigurableWebServerFactory {
        int getPortValue() {
            return getPort();
        }
    }

    @Test
    public void testGlobalPortProperties() {
        GlobalPortProperties properties = new GlobalPortProperties();
        Assert.assertTrue(properties.isEnable());
        Assert.assertEquals("", properties.getPortArea());
        properties.setEnable(false);
        properties.setPortArea("8080-9090");
        Assert.assertFalse(properties.isEnable());
        Assert.assertEquals("8080-9090", properties.getPortArea());
        Assert.assertEquals("skrstop.http.port.config", GlobalConfigConst.HTTP_PREFIX);
    }

    @Test
    public void testDisable() {
        PortShiftAutoConfiguration configuration = new PortShiftAutoConfiguration();
        ServerProperties serverProperties = new ServerProperties();
        serverProperties.setPort(8080);
        GlobalPortProperties properties = new GlobalPortProperties();
        properties.setEnable(false);
        TestWebServerFactory factory = new TestWebServerFactory();
        factory.setPort(7070);
        configuration.webServerFactoryCustomizer(serverProperties, properties).customize(factory);
        Assert.assertEquals(7070, factory.getPortValue());
    }

    @Test
    public void testBlankPortArea() {
        PortShiftAutoConfiguration configuration = new PortShiftAutoConfiguration();
        ServerProperties serverProperties = new ServerProperties();
        serverProperties.setPort(18080);
        GlobalPortProperties properties = new GlobalPortProperties();
        TestWebServerFactory factory = new TestWebServerFactory();
        configuration.webServerFactoryCustomizer(serverProperties, properties).customize(factory);
        // 无 portArea 时在 basePort 起查找可用端口，结果 >= basePort
        Assert.assertTrue(factory.getPortValue() >= 18080);
    }

    @Test
    public void testPortAreaRange() {
        PortShiftAutoConfiguration configuration = new PortShiftAutoConfiguration();
        ServerProperties serverProperties = new ServerProperties();
        serverProperties.setPort(8080);
        GlobalPortProperties properties = new GlobalPortProperties();
        properties.setPortArea("29090-29191");
        TestWebServerFactory factory = new TestWebServerFactory();
        configuration.webServerFactoryCustomizer(serverProperties, properties).customize(factory);
        // basePort 不在范围内 → 从 minPort 起查找可用端口
        Assert.assertTrue(factory.getPortValue() >= 29090);
        Assert.assertTrue(factory.getPortValue() <= 29191);
    }

    @Test
    public void testPortAreaContainsBase() {
        PortShiftAutoConfiguration configuration = new PortShiftAutoConfiguration();
        ServerProperties serverProperties = new ServerProperties();
        serverProperties.setPort(28080);
        GlobalPortProperties properties = new GlobalPortProperties();
        properties.setPortArea("28080-29090");
        TestWebServerFactory factory = new TestWebServerFactory();
        configuration.webServerFactoryCustomizer(serverProperties, properties).customize(factory);
        Assert.assertTrue(factory.getPortValue() >= 28080);
        Assert.assertTrue(factory.getPortValue() <= 29090);
    }

    @Test
    public void testPortAreaBaseAboveRange() {
        PortShiftAutoConfiguration configuration = new PortShiftAutoConfiguration();
        ServerProperties serverProperties = new ServerProperties();
        serverProperties.setPort(28510);
        GlobalPortProperties properties = new GlobalPortProperties();
        properties.setPortArea("28501-28505");
        TestWebServerFactory factory = new TestWebServerFactory();
        configuration.webServerFactoryCustomizer(serverProperties, properties).customize(factory);
        Assert.assertTrue(factory.getPortValue() >= 28501);
        Assert.assertTrue(factory.getPortValue() <= 28510);
    }

    @Test
    public void testInvalidPortArea() {
        PortShiftAutoConfiguration configuration = new PortShiftAutoConfiguration();
        // 非数字
        ServerProperties serverProperties = new ServerProperties();
        serverProperties.setPort(18081);
        GlobalPortProperties properties = new GlobalPortProperties();
        properties.setPortArea("abc-def");
        TestWebServerFactory factory = new TestWebServerFactory();
        configuration.webServerFactoryCustomizer(serverProperties, properties).customize(factory);
        Assert.assertTrue(factory.getPortValue() >= 18081);

        // min > max
        ServerProperties serverProperties2 = new ServerProperties();
        serverProperties2.setPort(18082);
        GlobalPortProperties properties2 = new GlobalPortProperties();
        properties2.setPortArea("29091-29090");
        TestWebServerFactory factory2 = new TestWebServerFactory();
        configuration.webServerFactoryCustomizer(serverProperties2, properties2).customize(factory2);
        Assert.assertTrue(factory2.getPortValue() >= 18082);

        // 单值（无 -）
        ServerProperties serverProperties3 = new ServerProperties();
        serverProperties3.setPort(18083);
        GlobalPortProperties properties3 = new GlobalPortProperties();
        properties3.setPortArea("29090");
        TestWebServerFactory factory3 = new TestWebServerFactory();
        configuration.webServerFactoryCustomizer(serverProperties3, properties3).customize(factory3);
        Assert.assertTrue(factory3.getPortValue() >= 18083);

        // min 为数字、max 非数字
        ServerProperties serverProperties4 = new ServerProperties();
        serverProperties4.setPort(18084);
        GlobalPortProperties properties4 = new GlobalPortProperties();
        properties4.setPortArea("29091-abc");
        TestWebServerFactory factory4 = new TestWebServerFactory();
        configuration.webServerFactoryCustomizer(serverProperties4, properties4).customize(factory4);
        Assert.assertTrue(factory4.getPortValue() >= 18084);
    }

    @Test
    public void testBasePortOccupiedShift() throws Exception {
        // 占用 basePort，默认路径下 PortUtil 会偏移端口并打印日志
        java.net.ServerSocket socket = new java.net.ServerSocket(28555);
        try {
            PortShiftAutoConfiguration configuration = new PortShiftAutoConfiguration();
            ServerProperties serverProperties = new ServerProperties();
            serverProperties.setPort(28555);
            GlobalPortProperties properties = new GlobalPortProperties();
            TestWebServerFactory factory = new TestWebServerFactory();
            configuration.webServerFactoryCustomizer(serverProperties, properties).customize(factory);
            Assert.assertTrue(factory.getPortValue() > 28555);
        } finally {
            socket.close();
        }
    }
}
