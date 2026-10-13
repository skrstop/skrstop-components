package com.skrstop.framework.components.starter.annotation.handle.paramAlias;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;

import java.util.Collections;

/**
 * @author 蒋时华
 */
public class AliasRequestMappingHandlerAdapterTest {

    @Test
    public void testCreateDataBinderFactory() throws Exception {
        AliasRequestMappingHandlerAdapter adapter = new AliasRequestMappingHandlerAdapter();
        adapter.setWebBindingInitializer(new ConfigurableWebBindingInitializer());
        org.springframework.web.method.annotation.InitBinderDataBinderFactory factory =
                adapter.createDataBinderFactory(Collections.emptyList());
        Assert.assertTrue(factory instanceof AliasServletRequestDataBinderFactory);
    }

}