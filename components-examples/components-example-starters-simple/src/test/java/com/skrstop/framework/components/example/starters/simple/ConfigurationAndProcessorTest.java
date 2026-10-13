package com.skrstop.framework.components.example.starters.simple;

import com.skrstop.framework.components.example.starters.simple.configuration.AccessLimitConfiguration;
import com.skrstop.framework.components.example.starters.simple.configuration.PrivacyInfoConfiguration;
import com.skrstop.framework.components.example.starters.simple.constant.ProcessorContainerNameConst;
import com.skrstop.framework.components.example.starters.simple.exception.CustomErrorCode;
import com.skrstop.framework.components.example.starters.simple.service.processor.ExampleProcessor1Service;
import com.skrstop.framework.components.example.starters.simple.service.processor.ExampleProcessor2Service;
import com.skrstop.framework.components.example.starters.simple.service.processor.ExampleProcessor3Service;
import com.skrstop.framework.components.example.starters.simple.service.processor.ExampleProcessorDefaultService;
import com.skrstop.framework.components.example.starters.simple.service.processor.asserts.ExampleProcessorAssert;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class ConfigurationAndProcessorTest {

    @Test
    public void testAccessLimitConfiguration() {
        AccessLimitConfiguration configuration = new AccessLimitConfiguration();
        Assert.assertNotNull(configuration.accessLimitRule1());
        AccessLimitConfiguration.CustomAccessLimitRule rule = configuration.accessLimitRule2();
        Assert.assertNotNull(rule);
        Assert.assertNotNull(new AccessLimitConfiguration.CustomAccessLimitRule());
    }

    @Test
    public void testPrivacyInfoConfiguration() {
        PrivacyInfoConfiguration configuration = new PrivacyInfoConfiguration();
        PrivacyInfoConfiguration.CustomPrivacyInfoTypeRule rule = configuration.accessLimitRule1();
        Assert.assertEquals(Set.of("test"), rule.supportType());
        Assert.assertEquals("处理后的结果", rule.handle("origin"));
    }

    @Test
    public void testCustomErrorCode() {
        CustomErrorCode code = CustomErrorCode.CUSTOM_ERROR;
        // @AllArgsConstructor 字段顺序为 (message, code)
        Assert.assertEquals("自定义异常code", code.getCode());
        Assert.assertEquals("custom_error", code.getMessage());
        code.setCode("x");
        code.setMessage("y");
        Assert.assertEquals("x", code.getCode());
        Assert.assertEquals("y", code.getMessage());
    }

    @Test
    public void testProcessors() {
        ExampleProcessor1Service p1 = new ExampleProcessor1Service();
        Assert.assertEquals("processor1 print", p1.print());
        ExampleProcessor1Service.AssertProcessor assert1 = new ExampleProcessor1Service.AssertProcessor();
        Assert.assertTrue(assert1.isSupported("processor1"));
        Assert.assertFalse(assert1.isSupported("other"));

        ExampleProcessor2Service p2 = new ExampleProcessor2Service();
        Assert.assertEquals("processor2 print", p2.print());
        Assert.assertTrue(p2.isSupported("processor2"));
        Assert.assertFalse(p2.isSupported("other"));

        ExampleProcessor3Service p3 = new ExampleProcessor3Service();
        Assert.assertEquals("processor3 print", p3.print());
        Assert.assertTrue(p3.isSupported("processor3"));
        Assert.assertFalse(p3.isSupported("other"));

        ExampleProcessorDefaultService pd = new ExampleProcessorDefaultService();
        Assert.assertEquals("processor default print", pd.print());
        Assert.assertTrue(pd.isSupported("anything"));

        Assert.assertFalse(new ExampleProcessorAssert().isSupported("anything"));

        Assert.assertEquals("processorPool", ProcessorContainerNameConst.PROCESSOR_POOL);
    }
}
