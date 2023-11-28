package cn.auntec.framework.components.starter.web.response.webmvc;

import cn.auntec.framework.components.starter.web.config.GlobalResponseConfig;
import cn.auntec.framework.components.starter.web.response.core.ResponseHandleChainPattern;
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
@EnableConfigurationProperties(GlobalResponseConfig.class)
@ConditionalOnClass(RequestMappingHandlerAdapter.class)
@ConditionalOnBean(RequestMappingHandlerAdapter.class)
public class ReturnResponseAutoConfig implements InitializingBean {

    private final RequestMappingHandlerAdapter adapter;
    private final GlobalResponseConfig globalResponseConfig;
    private final ResponseHandleChainPattern responseHandleChainPattern;

    public ReturnResponseAutoConfig(RequestMappingHandlerAdapter adapter, GlobalResponseConfig globalResponseConfig, ResponseHandleChainPattern responseHandleChainPattern) {
        this.adapter = adapter;
        this.globalResponseConfig = globalResponseConfig;
        this.responseHandleChainPattern = responseHandleChainPattern;
    }


    @Override
    public void afterPropertiesSet() {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList(returnValueHandlers);
        decorateHandlers(handlers);
        adapter.setReturnValueHandlers(handlers);
    }

    private void decorateHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                ReturnResponseHandler decorator = new ReturnResponseHandler(handler, globalResponseConfig, responseHandleChainPattern);
                int index = handlers.indexOf(handler);
                handlers.set(index, decorator);
                break;
            }
        }
    }

}
