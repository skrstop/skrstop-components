package com.jphoebe.framework.components.starter.feign.protostuff.autoconfigure.webmvc;

import com.jphoebe.framework.components.starter.feign.protostuff.converter.WebMvcProtostuffReturnResponseHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 全局统一返回值格式
 *
 * @author 蒋时华
 * @date 2018/11/22
 */
@Configuration
@ConditionalOnClass(RequestMappingHandlerAdapter.class)
@ConditionalOnBean(RequestMappingHandlerAdapter.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ProtostuffReturnResponseHandlerAutoConfiguration implements InitializingBean {


    private final RequestMappingHandlerAdapter adapter;
    private List<Object> responseBodyAdvice = new ArrayList<>();
    private final ContentNegotiationManager contentNegotiationManager;

    public ProtostuffReturnResponseHandlerAutoConfiguration(RequestMappingHandlerAdapter adapter
            , List<ResponseBodyAdvice<?>> responseBodyAdviceBean
            , @Qualifier("mvcContentNegotiationManager") ContentNegotiationManager contentNegotiationManager) {
        this.adapter = adapter;
        this.responseBodyAdvice.add(Collections.singletonList(new JsonViewRequestBodyAdvice()));
        this.responseBodyAdvice.add(Collections.singletonList(new JsonViewResponseBodyAdvice()));
        this.responseBodyAdvice.addAll(responseBodyAdviceBean);
        this.contentNegotiationManager = contentNegotiationManager;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList(returnValueHandlers);
        decorateHandlers(handlers);
        adapter.setReturnValueHandlers(handlers);
    }

    private void decorateHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        Iterator<HandlerMethodReturnValueHandler> iterator = handlers.iterator();
        while (iterator.hasNext()) {
            HandlerMethodReturnValueHandler handler = iterator.next();
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                RequestResponseBodyMethodProcessor oldHandle = (RequestResponseBodyMethodProcessor) handler;
                int index = handlers.indexOf(handler);
                WebMvcProtostuffReturnResponseHandler webMvcProtostuffReturnResponseHandler =
                        new WebMvcProtostuffReturnResponseHandler(adapter.getMessageConverters(), this.contentNegotiationManager, responseBodyAdvice);
                handlers.set(index, webMvcProtostuffReturnResponseHandler);
                handlers.remove(handler);
                break;
            }
        }
    }

}
