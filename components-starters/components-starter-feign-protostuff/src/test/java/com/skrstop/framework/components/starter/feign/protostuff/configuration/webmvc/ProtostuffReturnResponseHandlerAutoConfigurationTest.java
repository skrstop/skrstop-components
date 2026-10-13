package com.skrstop.framework.components.starter.feign.protostuff.configuration.webmvc;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

public class ProtostuffReturnResponseHandlerAutoConfigurationTest {

    @Test
    public void afterPropertiesSetReplacesHandler() throws Exception {
        RequestMappingHandlerAdapter adapter = Mockito.mock(RequestMappingHandlerAdapter.class);
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new StringHttpMessageConverter());
        RequestResponseBodyMethodProcessor processor = new RequestResponseBodyMethodProcessor(converters);
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();
        // 第一个非目标 handler, 覆盖迭代继续分支
        handlers.add(Mockito.mock(HandlerMethodReturnValueHandler.class));
        handlers.add(processor);
        Mockito.when(adapter.getReturnValueHandlers()).thenReturn(handlers);
        Mockito.when(adapter.getMessageConverters()).thenReturn(converters);
        Mockito.when(adapter.getMessageConverters()).thenReturn(converters);

        ContentNegotiationManager manager = new ContentNegotiationManager();
        ProtostuffReturnResponseHandlerAutoConfiguration configuration =
                new ProtostuffReturnResponseHandlerAutoConfiguration(adapter, new ArrayList<>(), manager);
        configuration.afterPropertiesSet();

        Mockito.verify(adapter).setReturnValueHandlers(Mockito.argThat(list -> {
            for (HandlerMethodReturnValueHandler handler : list) {
                if (handler instanceof RequestResponseBodyMethodProcessor && !(handler instanceof com.skrstop.framework.components.starter.feign.protostuff.converter.WebMvcProtostuffReturnResponseHandler)) {
                    return false;
                }
            }
            return list.stream().anyMatch(h -> h instanceof com.skrstop.framework.components.starter.feign.protostuff.converter.WebMvcProtostuffReturnResponseHandler);
        }));
    }


    @Test
    public void afterPropertiesSetWithoutProcessor() throws Exception {
        RequestMappingHandlerAdapter adapter = Mockito.mock(RequestMappingHandlerAdapter.class);
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();
        handlers.add(Mockito.mock(HandlerMethodReturnValueHandler.class));
        Mockito.when(adapter.getReturnValueHandlers()).thenReturn(handlers);
        Mockito.when(adapter.getMessageConverters()).thenReturn(new ArrayList<>());
        ProtostuffReturnResponseHandlerAutoConfiguration configuration =
                new ProtostuffReturnResponseHandlerAutoConfiguration(adapter, new ArrayList<>(), new ContentNegotiationManager());
        configuration.afterPropertiesSet();
        // 无 RequestResponseBodyMethodProcessor -> 原样回写
        Mockito.verify(adapter).setReturnValueHandlers(handlers);
    }
}
