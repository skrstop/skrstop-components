package com.skrstop.framework.components.starter.annotation.handle.paramAlias;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Collections;

/**
 * @author 蒋时华
 */
public class AliasServletRequestDataBinderFactoryTest {

    @Test
    public void testCreateBinderInstance() throws Exception {
        AliasServletRequestDataBinderFactory factory =
                new AliasServletRequestDataBinderFactory(Collections.emptyList(), null);
        Object target = new Object();
        org.springframework.web.bind.ServletRequestDataBinder binder =
                factory.createBinderInstance(target, "target",
                        new org.springframework.web.context.request.ServletWebRequest(new MockHttpServletRequest()));
        Assert.assertTrue(binder instanceof AliasExtendedServletRequestDataBinder);
        Assert.assertSame(target, binder.getTarget());
    }

    @Test
    public void testCreateBinderWithInitializer() throws Exception {
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        AliasServletRequestDataBinderFactory factory =
                new AliasServletRequestDataBinderFactory(Collections.emptyList(), initializer);
        org.springframework.web.bind.ServletRequestDataBinder binder =
                factory.createBinderInstance(new Object(), "t",
                        new ServletWebRequest(new MockHttpServletRequest()));
        Assert.assertNotNull(binder);
    }

}