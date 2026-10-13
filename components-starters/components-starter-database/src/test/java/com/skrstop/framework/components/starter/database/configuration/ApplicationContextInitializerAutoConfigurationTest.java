package com.skrstop.framework.components.starter.database.configuration;

import com.skrstop.framework.components.starter.database.constant.GlobalConfigConst;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.env.MockEnvironment;

import java.lang.reflect.Field;

public class ApplicationContextInitializerAutoConfigurationTest {

    private void resetFlag() throws Exception {
        Field f = ApplicationContextInitializerAutoConfiguration.class.getDeclaredField("druidInfoSet");
        f.setAccessible(true);
        f.setBoolean(null, false);
    }

    @After
    public void after() throws Exception {
        resetFlag();
    }

    @Test
    public void initializeUsePingTrue() throws Exception {
        resetFlag();
        MockEnvironment env = new MockEnvironment();
        env.setProperty(GlobalConfigConst.DATABASE_CONFIG_CHECK_DRUID_USE_PING_METHOD, "true");
        org.springframework.context.ConfigurableApplicationContext ctx =
                new org.springframework.context.annotation.AnnotationConfigApplicationContext();
        ctx.setEnvironment(env);
        new ApplicationContextInitializerAutoConfiguration().initialize(ctx);
        Assert.assertEquals("true", System.getProperty("druid.mysql.usePingMethod"));
    }

    @Test
    public void initializeDruidPropertyOnly() throws Exception {
        resetFlag();
        System.clearProperty("druid.mysql.usePingMethod");
        MockEnvironment env = new MockEnvironment();
        env.setProperty("spring.datasource.druid.use-ping-method", "true");
        org.springframework.context.ConfigurableApplicationContext ctx =
                new org.springframework.context.annotation.AnnotationConfigApplicationContext();
        ctx.setEnvironment(env);
        new ApplicationContextInitializerAutoConfiguration().initialize(ctx);
        Assert.assertEquals("true", System.getProperty("druid.mysql.usePingMethod"));
    }

    @Test
    public void initializeDisabled() throws Exception {
        resetFlag();
        System.clearProperty("druid.mysql.usePingMethod");
        org.springframework.context.ConfigurableApplicationContext ctx =
                new org.springframework.context.annotation.AnnotationConfigApplicationContext();
        ctx.setEnvironment(new MockEnvironment());
        new ApplicationContextInitializerAutoConfiguration().initialize(ctx);
        Assert.assertEquals("false", System.getProperty("druid.mysql.usePingMethod"));
    }

    @Test
    public void initializeOnlyOnce() throws Exception {
        resetFlag();
        MockEnvironment env = new MockEnvironment();
        env.setProperty(GlobalConfigConst.DATABASE_CONFIG_CHECK_DRUID_USE_PING_METHOD, "true");
        org.springframework.context.ConfigurableApplicationContext ctx =
                new org.springframework.context.annotation.AnnotationConfigApplicationContext();
        ctx.setEnvironment(env);
        ApplicationContextInitializerAutoConfiguration initializer = new ApplicationContextInitializerAutoConfiguration();
        initializer.initialize(ctx);
        initializer.initialize(ctx);
        Assert.assertEquals("true", System.getProperty("druid.mysql.usePingMethod"));
    }
}
