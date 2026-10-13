package com.skrstop.framework.components.starter.web.response;

import com.skrstop.framework.components.starter.web.configuration.GlobalResponseProperties;
import com.skrstop.framework.components.starter.web.response.core.ResponseHandleChainPattern;
import com.skrstop.framework.components.starter.web.response.webmvc.ReturnResponseAutoConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

public class WebmvcReturnResponseAutoConfigurationTest {

    @Test
    public void testAfterPropertiesSet() {
        RequestMappingHandlerAdapter adapter = Mockito.mock(RequestMappingHandlerAdapter.class);
        Mockito.when(adapter.getReturnValueHandlers()).thenReturn(null);
        ReturnResponseAutoConfiguration config = new ReturnResponseAutoConfiguration(
                adapter, new GlobalResponseProperties(), new ResponseHandleChainPattern(new ArrayList<>()));
        config.afterPropertiesSet();
        Mockito.verify(adapter).setReturnValueHandlers(Mockito.anyList());

        RequestResponseBodyMethodProcessor processor =
                new RequestResponseBodyMethodProcessor(
                        java.util.Collections.singletonList(new MappingJackson2HttpMessageConverter()));
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();
        handlers.add(processor);
        Mockito.when(adapter.getReturnValueHandlers()).thenReturn(handlers);
        config.afterPropertiesSet();
        org.mockito.ArgumentCaptor<List<HandlerMethodReturnValueHandler>> captor =
                org.mockito.ArgumentCaptor.forClass((Class) List.class);
        Mockito.verify(adapter, Mockito.times(2)).setReturnValueHandlers(captor.capture());
        Assert.assertTrue(captor.getAllValues().get(1).get(0)
                instanceof com.skrstop.framework.components.starter.web.response.webmvc.ReturnResponseHandler);
    }
}
