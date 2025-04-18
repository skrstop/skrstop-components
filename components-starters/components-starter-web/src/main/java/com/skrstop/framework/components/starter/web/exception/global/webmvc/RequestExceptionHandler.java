package com.skrstop.framework.components.starter.web.exception.global.webmvc;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.skrstop.framework.components.core.common.response.Result;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.util.EnumCodeUtil;
import com.skrstop.framework.components.core.exception.common.CommonExceptionCode;
import com.skrstop.framework.components.core.exception.core.BusinessThrowable;
import com.skrstop.framework.components.core.exception.util.ThrowableStackTraceUtil;
import com.skrstop.framework.components.starter.web.configuration.GlobalExceptionProperties;
import com.skrstop.framework.components.starter.web.constant.RequestConst;
import com.skrstop.framework.components.starter.web.exception.code.WebStarterExceptionCode;
import com.skrstop.framework.components.starter.web.exception.core.NotShowHttpStatusException;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ErrorHandleChainPattern;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandleChainPattern;
import com.skrstop.framework.components.starter.web.exception.global.interceptor.exception.BindExceptionInterceptor;
import com.skrstop.framework.components.util.constant.FeignConst;
import com.skrstop.framework.components.util.constant.HttpStatusConst;
import com.skrstop.framework.components.util.constant.StringPoolConst;
import com.skrstop.framework.components.util.enums.ContentTypeEnum;
import com.skrstop.framework.components.util.serialization.json.FastJsonUtil;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import com.skrstop.framework.components.util.value.validate.ErrorMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by 蒋时华 on 2017/10/31.
 * request请求异常处理
 */
@ControllerAdvice
@ConditionalOnProperty(value = "skrstop.exception.config.enable", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcAutoConfiguration.class, ResponseEntityExceptionHandler.class})
@EnableConfigurationProperties({GlobalExceptionProperties.class})
@Slf4j
public class RequestExceptionHandler extends ResponseEntityExceptionHandler {

    private final ExceptionHandleChainPattern exceptionHandleChainPattern;
    private final ErrorHandleChainPattern errorHandleChainPattern;
    private final GlobalExceptionProperties globalExceptionProperties;

    @Value("${server.error.path:${error.path:/error}}")
    private String errorPath;

    public RequestExceptionHandler(ExceptionHandleChainPattern exceptionHandleChainPattern, ErrorHandleChainPattern errorHandleChainPattern, GlobalExceptionProperties globalExceptionProperties) {
        this.exceptionHandleChainPattern = exceptionHandleChainPattern;
        this.errorHandleChainPattern = errorHandleChainPattern;
        this.globalExceptionProperties = globalExceptionProperties;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ThrowableStackTraceUtil.getStackTraceStr(ex));
        if (ex instanceof BindException) {
            BindExceptionInterceptor bindExceptionInterceptor = new BindExceptionInterceptor();
            return new ResponseEntity(bindExceptionInterceptor.execute(ex, null, null), HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (ex instanceof HttpMessageNotReadableException) {
            // json格式转换错误
            Throwable cause = ex.getCause();
            if (ObjectUtil.isNotNull(cause) && cause instanceof JsonMappingException && ObjectUtil.isNotNull(cause.getCause())) {
                IResult iResult = EnumCodeUtil.transferEnumCode(CommonExceptionCode.PARAMETER);
                iResult.setMessage(cause.getCause().getMessage());
                return new ResponseEntity(iResult, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity(EnumCodeUtil.transferEnumCode(CommonExceptionCode.PARAMETER), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(Result.Builder.result(CommonResultCode.FAIL), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理@RequestParam异常, 即参数不足
     */
    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ThrowableStackTraceUtil.getStackTraceStr(ex));
        return new ResponseEntity(Result.Builder.result(WebStarterExceptionCode.MISS_PARAMETER), status);
    }

    /**
     * 处理@PathVariable异常, 即参数不足
     */
    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ThrowableStackTraceUtil.getStackTraceStr(ex));
        return new ResponseEntity(Result.Builder.result(WebStarterExceptionCode.MISS_PARAMETER), status);
    }

    /**
     * 处理参数类型转换失败
     *
     * @param request
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ThrowableStackTraceUtil.getStackTraceStr(ex));
        IResult iResult = EnumCodeUtil.transferEnumCode(WebStarterExceptionCode.MATCH_PARAMETER);
        String message = iResult.getMessage();
        message = new StringBuffer(message)
                .append(": ")
                .append(ex.getValue())
                .toString();
        iResult.setMessage(message);
        return new ResponseEntity(iResult, status);
    }

    /**
     * 媒体类型不支持异常
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ThrowableStackTraceUtil.getStackTraceStr(ex));
        return new ResponseEntity(Result.Builder.result(WebStarterExceptionCode.NOT_SUPPORT_MEDIA_TYPE), status);
    }

    /**
     * 媒体类型不可接受
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ThrowableStackTraceUtil.getStackTraceStr(ex));
        return new ResponseEntity(Result.Builder.result(WebStarterExceptionCode.NOT_ACCEPTED_MEDIA_TYPE), status);
    }

    /**
     * 请求超时
     *
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        log.error(ThrowableStackTraceUtil.getStackTraceStr(ex));
        return new ResponseEntity(Result.Builder.result(WebStarterExceptionCode.REQUEST_TIMEOUT), status);
    }

    /**
     * 请求方法类型错误异常
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ThrowableStackTraceUtil.getStackTraceStr(ex));
        return new ResponseEntity(Result.Builder.result(WebStarterExceptionCode.REQUEST_METHOD_NOT_ALLOWED), status);
    }

    /**
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     * @RequestBody 参数验证异常
     */
    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug(ThrowableStackTraceUtil.getStackTraceStr(ex));
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
//        StringBuffer errorMsg=new StringBuffer();
//        errors.stream().forEach(x -> errorMsg.append(x.getDefaultMessage()).append(";"));
//        ParameterException.ParameterErrorCode.PARAMETER_VALID_ERROR_CODE.setMessage(errorMsg.toString());
//        String defaultMessage = errors.get(0).getDefaultMessage();
        String defaultMessage = null;
        IResult paramError = EnumCodeUtil.transferEnumCode(CommonExceptionCode.PARAMETER);
        if (CollectionUtil.isNotEmpty(errors)) {
            defaultMessage = ErrorMessageUtil.getFirstErrorMessage(errors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList()));
        }
        paramError.setMessage(defaultMessage);
        return new ResponseEntity(paramError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 文件上传异常
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ThrowableStackTraceUtil.getStackTraceStr(ex));
        return new ResponseEntity(Result.Builder.result(WebStarterExceptionCode.FILE_UPLOAD), status);
    }

    private boolean skipErrorPath(HttpServletRequest request) {
        if (ObjectUtil.isNull(request)) {
            return false;
        }
        if (StrUtil.equals(request.getRequestURI(), this.errorPath)) {
            return true;
        }
        return false;
    }

    @InitBinder
    public void bodyCache(WebDataBinder webDataBinder, HttpServletRequest request) {
        if (this.skipErrorPath(request)) {
            return;
        }
        Map<String, Object> model = webDataBinder.getBindingResult().getModel();
        if (ObjectUtil.isNotNull(model) && model.size() > 1) {
            Iterator<Map.Entry<String, Object>> iterator = model.entrySet().iterator();
            if (iterator.hasNext()) {
                Map.Entry<String, Object> bodyMap = iterator.next();
                request.setAttribute(RequestConst.BODY_CACHE_NAME, bodyMap.getKey());
                request.setAttribute(RequestConst.BODY_CACHE, FastJsonUtil.toJson(bodyMap.getValue()));
            }
        }
    }

    /**
     * 进入到实际逻辑执行了，抛出的异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public IResult handlerException(HttpServletRequest request
            , HttpServletResponse response
            , Exception e) {
        if (globalExceptionProperties != null
                && e instanceof BusinessThrowable
                && !globalExceptionProperties.isLogBusinessServiceException()) {
            // 不需要打印日志的业务异常信息
        } else if (ObjectUtil.isNotNull(request) && !this.skipErrorPath(request)) {
            String requestPath, requestQueryPath, requestBodyName, requestBody;
            // 获取请求信息
            if (ObjectUtil.isNull(request)) {
                requestPath = "无法获取请求地址";
                requestQueryPath = "无法获取请求参数";
                requestBodyName = "无法获取请求body形参名";
                requestBody = "无法获取请求body体";
            } else {
                requestPath = request.getRequestURI();
                requestQueryPath = request.getQueryString();
                requestBodyName = ObjectUtil.isNotNull(request.getAttribute(RequestConst.BODY_CACHE_NAME)) ? request.getAttribute(RequestConst.BODY_CACHE_NAME).toString() : StringPoolConst.EMPTY;
                requestBody = ObjectUtil.isNotNull(request.getAttribute(RequestConst.BODY_CACHE)) ? request.getAttribute(RequestConst.BODY_CACHE).toString() : StringPoolConst.EMPTY;
            }
            log.error("http请求报错 ==>\n请求地址：{} \n请求参数：{} \n请求body形参名：{} \n请求body体：{} \n异常栈：\n\n{}", requestPath, requestQueryPath, requestBodyName, requestBody, ThrowableStackTraceUtil.getStackTraceStr(e));
        } else {
            log.error("异常栈：\n\n{}", ThrowableStackTraceUtil.getStackTraceStr(e));
        }
        this.setResponseContentType(request, response);
        IResult execute = exceptionHandleChainPattern.execute(e, response, null);
        if ((e instanceof NotShowHttpStatusException || e instanceof BusinessThrowable)) {
            response.setStatus(HttpStatusConst.HTTP_OK);
        } else if (response.getStatus() == HttpStatusConst.HTTP_OK) {
            response.setStatus(HttpStatusConst.HTTP_INTERNAL_ERROR);
        }
        return execute;
    }

    /**
     * 进入到实际逻辑执行了，抛出的异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = Error.class)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public IResult handleError(HttpServletRequest request, HttpServletResponse response, Error e) {
        log.error(ThrowableStackTraceUtil.getStackTraceStr(e));
        response.setStatus(HttpStatusConst.HTTP_INTERNAL_ERROR);
        this.setResponseContentType(request, response);
        return errorHandleChainPattern.execute(e);
    }

    public void setResponseContentType(HttpServletRequest request, HttpServletResponse response) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String element = headerNames.nextElement();
            if (element.equals(FeignConst.USE_FEIGN_NAME) ||
                    request.getHeader(element).startsWith(ContentTypeEnum.PROTOBUF.getContentType()) ||
                    request.getHeader(element).startsWith(MediaType.APPLICATION_JSON_VALUE)) {
                return;
            }
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

}
