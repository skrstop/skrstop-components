package com.skrstop.framework.components.starter.web.exception.global.webflux;

import cn.hutool.core.lang.Pair;
import com.skrstop.framework.components.core.common.response.DefaultResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.exception.core.BusinessThrowable;
import com.skrstop.framework.components.core.exception.util.ThrowableStackTraceUtil;
import com.skrstop.framework.components.starter.web.configuration.GlobalExceptionProperties;
import com.skrstop.framework.components.starter.web.constant.RequestConst;
import com.skrstop.framework.components.starter.web.exception.core.NotShowHttpStatusException;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ErrorHandleChainPattern;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandleChainPattern;
import com.skrstop.framework.components.util.constant.FeignConst;
import com.skrstop.framework.components.util.constant.HttpStatusConst;
import com.skrstop.framework.components.util.constant.StringPoolConst;
import com.skrstop.framework.components.util.enums.ContentTypeEnum;
import com.skrstop.framework.components.util.serialization.json.FastJsonUtil;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import com.skrstop.framework.components.util.value.validate.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ExceptionHandler class
 *
 * @author 蒋时华
 * @date 2019/1/22
 */
@ControllerAdvice
@Slf4j
public class RequestExceptionHandler implements ErrorWebExceptionHandler {

    private final ExceptionHandleChainPattern exceptionHandleChainPattern;
    private final ErrorHandleChainPattern errorHandleChainPattern;
    private final GlobalExceptionProperties globalExceptionProperties;

    private final String errorPath;

    public RequestExceptionHandler(ExceptionHandleChainPattern exceptionHandleChainPattern
            , ErrorHandleChainPattern errorHandleChainPattern
            , GlobalExceptionProperties globalExceptionProperties, String errorPath) {
        this.exceptionHandleChainPattern = exceptionHandleChainPattern;
        this.errorHandleChainPattern = errorHandleChainPattern;
        this.globalExceptionProperties = globalExceptionProperties;
        this.errorPath = errorPath;
    }

    /**
     * MessageReader
     */
    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();

    /**
     * MessageWriter
     */
    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();

    /**
     * ViewResolvers
     */
    private List<ViewResolver> viewResolvers = Collections.emptyList();

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    public void setMessageReaders(List<HttpMessageReader<?>> messageReaders) {
        Assert.notNull(messageReaders, "'messageReaders' must not be null");
        this.messageReaders = messageReaders;
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    public void setMessageWriters(List<HttpMessageWriter<?>> messageWriters) {
        Assert.notNull(messageWriters, "'messageWriters' must not be null");
        this.messageWriters = messageWriters;
    }

    @InitBinder
    public void bodyCache(WebDataBinder webDataBinder, ServerWebExchange exchange) {
        if (this.skipErrorPath(exchange)) {
            return;
        }
        Map<String, Object> attributes = exchange.getAttributes();
        Map<String, Object> model = webDataBinder.getBindingResult().getModel();
        if (ObjectUtil.isNotNull(model) && model.size() > 1) {
            Iterator<Map.Entry<String, Object>> iterator = model.entrySet().iterator();
            if (iterator.hasNext()) {
                Map.Entry<String, Object> bodyMap = iterator.next();
                attributes.put(RequestConst.BODY_CACHE_NAME, bodyMap.getKey());
                String content = FastJsonUtil.toJson(bodyMap.getValue());
                if (ObjectUtil.isNull(content)) {
                    content = StringPoolConst.EMPTY;
                }
                attributes.put(RequestConst.BODY_CACHE, content);
            }
        }
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable e) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        if (globalExceptionProperties != null
                && e instanceof BusinessThrowable
                && !globalExceptionProperties.isLogBusinessServiceException()) {
            // 不需要打印日志的业务异常信息
        } else if (ObjectUtil.isNotNull(exchange) && !this.skipErrorPath(exchange)) {
            String requestPath, requestQueryPath, requestBodyName, requestBody;
            // 获取请求信息
            if (ObjectUtil.isNull(request)) {
                requestPath = "无法获取请求地址";
                requestQueryPath = "无法获取请求参数";
                requestBodyName = "无法获取请求body形参名";
                requestBody = "无法获取请求body体";
            } else {
                requestPath = request.getURI().getPath();
                requestQueryPath = request.getQueryParams().toString();
                requestBodyName = ObjectUtil.isNotNull(exchange.getAttribute(RequestConst.BODY_CACHE_NAME)) ? exchange.getAttribute(RequestConst.BODY_CACHE_NAME).toString() : StringPoolConst.EMPTY;
                requestBody = ObjectUtil.isNotNull(exchange.getAttribute(RequestConst.BODY_CACHE)) ? exchange.getAttribute(RequestConst.BODY_CACHE).toString() : StringPoolConst.EMPTY;
            }
            log.error("http请求报错 ==>\n请求地址：{} \n请求参数：{} \n请求body形参名：{} \n请求body体：{} \n异常栈：\n\n{}", requestPath, requestQueryPath, requestBodyName, requestBody, ThrowableStackTraceUtil.getStackTraceStr(e));
        } else {
            log.error("异常栈：\n\n{}", ThrowableStackTraceUtil.getStackTraceStr(e));
        }

        IResult result;
        if (e instanceof Exception) {
            Pair<IResult, Integer> execute = exceptionHandleChainPattern.execute((Exception) e);
            result = execute.getKey();
            response.setRawStatusCode(execute.getValue());
        } else if (e instanceof Error) {
            result = errorHandleChainPattern.execute((Error) e);
            response.setRawStatusCode(HttpStatusConst.HTTP_INTERNAL_ERROR);
        } else {
            result = DefaultResult.Builder.error();
            response.setRawStatusCode(HttpStatusConst.HTTP_INTERNAL_ERROR);
        }
        if (e instanceof NotShowHttpStatusException || e instanceof BusinessThrowable) {
            response.setRawStatusCode(HttpStatusConst.HTTP_OK);
        }
        // 参考AbstractErrorWebExceptionHandler
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(e);
        }
        ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
        return RouterFunctions.route(RequestPredicates.all(), req -> this.renderErrorResponse(req, response.getStatusCode(), result))
                .route(newRequest)
                .switchIfEmpty(Mono.error(e))
                .flatMap((handler) -> handler.handle(newRequest))
                .flatMap((flatResponse) -> write(exchange, flatResponse));
    }

    /**
     * 参考DefaultErrorWebExceptionHandler
     */
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request, HttpStatus httpStatus, IResult result) {
        ServerRequest.Headers headers = request.headers();
        MediaType contentType = MediaType.APPLICATION_JSON;
        List<String> feignValues = headers.header(FeignConst.USE_FEIGN_NAME);
        List<String> contentTypeValues = headers.header(HttpHeaders.CONTENT_TYPE);
        if (CollectionUtil.isNotEmpty(feignValues)) {
            contentType = MediaType.parseMediaType(ContentTypeEnum.PROTOBUF.getContentType());
        } else if (CollectionUtil.isNotEmpty(contentTypeValues)) {
            String str = contentTypeValues.get(0);
            if (ContentTypeEnum.PROTOBUF.getContentType().equals(str) || MediaType.APPLICATION_JSON_VALUE.equals(str)) {
                contentType = MediaType.parseMediaType(str);
            }
        }
        return ServerResponse.status(httpStatus)
                .contentType(contentType)
                .body(BodyInserters.fromValue(result));
    }

    private boolean skipErrorPath(ServerWebExchange exchange) {
        if (ObjectUtil.isNull(exchange)) {
            return false;
        }
        ServerHttpRequest request = exchange.getRequest();
        if (StrUtil.equals(request.getURI().getPath(), this.errorPath)) {
            return true;
        }
        return false;
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    private Mono<? extends Void> write(ServerWebExchange exchange,
                                       ServerResponse response) {
        exchange.getResponse().getHeaders()
                .setContentType(response.headers().getContentType());
        return response.writeTo(exchange, new ResponseContext());
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    private class ResponseContext implements ServerResponse.Context {

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return RequestExceptionHandler.this.messageWriters;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return RequestExceptionHandler.this.viewResolvers;
        }

    }
}
