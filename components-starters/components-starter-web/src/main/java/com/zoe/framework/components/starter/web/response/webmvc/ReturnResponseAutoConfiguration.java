package com.zoe.framework.components.starter.web.response.webmvc;

import com.zoe.framework.components.starter.web.configuration.GlobalResponseProperties;
import com.zoe.framework.components.starter.web.response.core.ResponseHandleChainPattern;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * WebMvcConfig class
 *
 * @author 蒋时华
 * @date 2018/11/22
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(GlobalResponseProperties.class)
@ConditionalOnClass(RequestMappingHandlerAdapter.class)
@ConditionalOnBean(RequestMappingHandlerAdapter.class)
public class ReturnResponseAutoConfiguration implements InitializingBean {

    private final RequestMappingHandlerAdapter adapter;
    private final GlobalResponseProperties globalResponseProperties;
    private final ResponseHandleChainPattern responseHandleChainPattern;

    public ReturnResponseAutoConfiguration(RequestMappingHandlerAdapter adapter, GlobalResponseProperties globalResponseProperties, ResponseHandleChainPattern responseHandleChainPattern) {
        this.adapter = adapter;
        this.globalResponseProperties = globalResponseProperties;
        this.responseHandleChainPattern = responseHandleChainPattern;
    }


    @Override
    public void afterPropertiesSet() {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();
        if (ObjectUtil.isNull(returnValueHandlers)) {
            returnValueHandlers = new ArrayList<>();
        }
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>(returnValueHandlers);
        decorateHandlers(handlers);
        adapter.setReturnValueHandlers(handlers);
    }

    private void decorateHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                ReturnResponseHandler decorator = new ReturnResponseHandler(handler, globalResponseProperties, responseHandleChainPattern);
                int index = handlers.indexOf(handler);
                handlers.set(index, decorator);
                break;
            }
        }
    }

}
