package com.skrstop.framework.components.starter.feign.protostuff.converter;

import com.skrstop.framework.components.util.constant.FeignConst;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 全局统一返回值格式
 *
 * @author 蒋时华
 * @date 2018/11/22
 */
public class WebMvcProtostuffReturnResponseHandler extends RequestResponseBodyMethodProcessor {

    public WebMvcProtostuffReturnResponseHandler(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    public WebMvcProtostuffReturnResponseHandler(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager) {
        super(converters, manager);
    }

    public WebMvcProtostuffReturnResponseHandler(List<HttpMessageConverter<?>> converters, List<Object> requestResponseBodyAdvice) {
        super(converters, requestResponseBodyAdvice);
    }

    public WebMvcProtostuffReturnResponseHandler(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager, List<Object> requestResponseBodyAdvice) {
        super(converters, manager, requestResponseBodyAdvice);
    }

    @Override
    protected ServletServerHttpRequest createInputMessage(NativeWebRequest webRequest) {
        return super.createInputMessage(webRequest);
    }

    @Override
    protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
        return super.createOutputMessage(webRequest);
    }

    @Override
    public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
            throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {

        mavContainer.setRequestHandled(true);
        ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
        ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);

        if (inputMessage.getHeaders().containsKey(FeignConst.USE_FEIGN_NAME)) {
            List<String> header = inputMessage.getHeaders().get(FeignConst.USE_FEIGN_NAME);
            if (header != null && FeignConst.USE_FEIGN_VALUE.equals(header.get(0))) {
                MediaType application = new MediaType("application", "x-protobuf", StandardCharsets.UTF_8);
                outputMessage.getHeaders().setContentType(application);
            }
        } else {
            outputMessage.getHeaders().setContentType(MediaType.parseMediaType("application/json;charset=UTF-8"));
        }
        boolean committed = outputMessage.getServletResponse().isCommitted();
        if (committed) {
            return;
        }
        writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
    }

}
