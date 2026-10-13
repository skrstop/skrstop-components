package com.skrstop.framework.components.starter.web.exception.global.webmvc;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.exception.*;
import com.skrstop.framework.components.starter.web.constant.RequestConst;
import com.skrstop.framework.components.starter.web.exception.core.SkrstopCustomHttpStatusBusinessException;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ErrorHandleChainPattern;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandleChainPattern;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

public class WebmvcRequestExceptionHandlerTest {

    private RequestExceptionHandler handler;
    private MockHttpServletResponse response;

    @Before
    public void setup() {
        ExceptionHandleChainPattern excChain = new ExceptionHandleChainPattern(new java.util.ArrayList<>());
        ReflectionTestUtils.invokeMethod(excChain, "initChainPattern");
        ErrorHandleChainPattern errChain = new ErrorHandleChainPattern(new java.util.ArrayList<>());
        ReflectionTestUtils.invokeMethod(errChain, "initChainPattern");
        handler = new RequestExceptionHandler(excChain, errChain, new com.skrstop.framework.components.starter.web.configuration.GlobalExceptionProperties());
        ReflectionTestUtils.setField(handler, "errorPath", "/error");
        response = new MockHttpServletResponse();
    }

    private IResult handle(Exception e) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
        return handler.handlerException(request, response, e);
    }

    @Test
    public void testHandlerException() {
        Assert.assertNotNull(handle(new SkrstopBusinessException("biz")));
        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals("application/json", response.getContentType());

        response = new MockHttpServletResponse();
        Assert.assertNotNull(handle(new SkrstopBusinessDataException("biz", new DefaultThrowableData())));

        response = new MockHttpServletResponse();
        Assert.assertNotNull(handle(new SkrstopCustomHttpStatusBusinessException("c", HttpStatus.NOT_FOUND)));
        Assert.assertEquals(404, response.getStatus());

        response = new MockHttpServletResponse();
        Assert.assertNotNull(handle(new com.skrstop.framework.components.core.exception.defined.illegal.ParameterException("p")));
        Assert.assertEquals(400, response.getStatus());

        response = new MockHttpServletResponse();
        Assert.assertNotNull(handle(new RuntimeException("plain")));
        Assert.assertEquals(500, response.getStatus());
    }

    @Test
    public void testHandlerExceptionBusinessLogSkipped() {
        com.skrstop.framework.components.starter.web.configuration.GlobalExceptionProperties props =
                new com.skrstop.framework.components.starter.web.configuration.GlobalExceptionProperties();
        props.setLogBusinessServiceException(true);
        ExceptionHandleChainPattern excChain = new ExceptionHandleChainPattern(new java.util.ArrayList<>());
        ReflectionTestUtils.invokeMethod(excChain, "initChainPattern");
        handler = new RequestExceptionHandler(excChain, null, props);
        ReflectionTestUtils.setField(handler, "errorPath", "/error");
        response = new MockHttpServletResponse();
        Assert.assertNotNull(handle(new SkrstopBusinessException("biz")));
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void testHandlerError() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
        Assert.assertNotNull(handler.handleError(request, response, new SkrstopError("err")));
        Assert.assertEquals(500, response.getStatus());

        Assert.assertNotNull(handler.handleError(request, response, new SkrstopDataError(new DefaultThrowableData())));

        Assert.assertNotNull(handler.handleError(request, response, new AssertionError("x")));
    }

    @Test
    public void testSetResponseContentType() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse resp = new MockHttpServletResponse();
        request.addHeader("use_feign", "true");
        handler.setResponseContentType(request, resp);
        Assert.assertEquals(0, resp.getHeaderNames().size());

        request = new MockHttpServletRequest();
        request.addHeader("x", "application/json");
        handler.setResponseContentType(request, resp);
        Assert.assertEquals(0, resp.getHeaderNames().size());

        request = new MockHttpServletRequest();
        request.addHeader("x", "text/plain");
        handler.setResponseContentType(request, resp);
        Assert.assertEquals("application/json", resp.getContentType());
    }

    @Test
    public void testBodyCache() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/normal");
        WebDataBinder binder = new WebDataBinder(new Object(), "target");
        handler.bodyCache(binder, request);
        Assert.assertEquals("target", request.getAttribute(RequestConst.BODY_CACHE_NAME));
        Assert.assertNotNull(request.getAttribute(RequestConst.BODY_CACHE));

        MockHttpServletRequest errReq = new MockHttpServletRequest("GET", "/error");
        WebDataBinder binder2 = new WebDataBinder(new Object(), "target");
        handler.bodyCache(binder2, errReq);
        Assert.assertNull(errReq.getAttribute(RequestConst.BODY_CACHE_NAME));
    }

    @Test
    public void testHandleExceptionInternalBind() {
        BindException e = new BindException(new BeanPropertyBindingResult(new Object(), "t"));
        e.addError(new FieldError("t", "f", "required"));
        ServletWebRequest req = new ServletWebRequest(new MockHttpServletRequest(), new MockHttpServletResponse());
        ResponseEntity<Object> resp = handler.handleExceptionInternal(e, null, new HttpHeaders(), HttpStatus.BAD_REQUEST, req);
        Assert.assertNotNull(resp.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
    }

    @Test
    public void testHandleExceptionInternalJson() {
        ServletWebRequest req = new ServletWebRequest(new MockHttpServletRequest(), new MockHttpServletResponse());
        ResponseEntity<Object> r1 = handler.handleExceptionInternal(new HttpMessageNotReadableException("bad"), null, new HttpHeaders(), HttpStatus.BAD_REQUEST, req);
        Assert.assertNotNull(r1.getBody());

        JsonMappingException jme = new JsonMappingException(new java.io.StringWriter(), "map fail", new RuntimeException("cause"));
        ResponseEntity<Object> r2 = handler.handleExceptionInternal(new HttpMessageNotReadableException("bad", jme), null, new HttpHeaders(), HttpStatus.BAD_REQUEST, req);
        Assert.assertNotNull(r2.getBody());
    }

    @Test
    public void testHandleExceptionInternalNoResource() {
        ServletWebRequest req = new ServletWebRequest(new MockHttpServletRequest("GET", "/nope"), new MockHttpServletResponse());
        ResponseEntity<Object> r = handler.handleExceptionInternal(
                new NoResourceFoundException(org.springframework.http.HttpMethod.GET, "/nope"),
                null, new HttpHeaders(), HttpStatus.NOT_FOUND, req);
        Assert.assertNotNull(r.getBody());
        Assert.assertEquals(HttpStatus.NOT_FOUND, r.getStatusCode());
    }

    @Test
    public void testHandleExceptionInternalDefault() {
        ServletWebRequest req = new ServletWebRequest(new MockHttpServletRequest(), new MockHttpServletResponse());
        ResponseEntity<Object> r = handler.handleExceptionInternal(new RuntimeException("x"), null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, req);
        Assert.assertNotNull(r.getBody());
    }

    @Test
    public void testSimpleHandlers() {
        ServletWebRequest req = new ServletWebRequest(new MockHttpServletRequest(), new MockHttpServletResponse());
        HttpHeaders h = new HttpHeaders();
        Assert.assertNotNull(handler.handleMissingServletRequestParameter(new MissingServletRequestParameterException("a", "b"), h, HttpStatus.BAD_REQUEST, req).getBody());
        Assert.assertNotNull(handler.handleMissingPathVariable(new org.springframework.web.bind.MissingPathVariableException("v", null), h, HttpStatus.BAD_REQUEST, req).getBody());
        Assert.assertNotNull(handler.handleTypeMismatch(new TypeMismatchException("v", Integer.class), h, HttpStatus.BAD_REQUEST, req).getBody());
        Assert.assertNotNull(handler.handleHttpMediaTypeNotSupported(new HttpMediaTypeNotSupportedException("x"), h, HttpStatus.UNSUPPORTED_MEDIA_TYPE, req).getBody());
        Assert.assertNotNull(handler.handleHttpMediaTypeNotAcceptable(new HttpMediaTypeNotAcceptableException("x"), h, HttpStatus.NOT_ACCEPTABLE, req).getBody());
        Assert.assertNotNull(handler.handleAsyncRequestTimeoutException(new AsyncRequestTimeoutException(), h, HttpStatus.SERVICE_UNAVAILABLE, req).getBody());
        Assert.assertNotNull(handler.handleHttpRequestMethodNotSupported(new HttpRequestMethodNotSupportedException("GET"), h, HttpStatus.METHOD_NOT_ALLOWED, req).getBody());
        Assert.assertNotNull(handler.handleConversionNotSupported(new ConversionNotSupportedException("v", Integer.class, null), h, HttpStatus.INTERNAL_SERVER_ERROR, req).getBody());
    }

    @Test
    public void testGetFinalStatusCodeAlwaysOk() {
        com.skrstop.framework.components.starter.web.configuration.GlobalExceptionProperties props =
                new com.skrstop.framework.components.starter.web.configuration.GlobalExceptionProperties();
        props.setAlwaysReturnHttpOk(true);
        ExceptionHandleChainPattern excChain = new ExceptionHandleChainPattern(new java.util.ArrayList<>());
        handler = new RequestExceptionHandler(excChain, null, props);
        ServletWebRequest req = new ServletWebRequest(new MockHttpServletRequest(), new MockHttpServletResponse());
        ResponseEntity<Object> r = handler.handleExceptionInternal(new RuntimeException("x"), null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, req);
        Assert.assertEquals(HttpStatus.OK, r.getStatusCode());
    }

    @Test
    public void testMethodArgumentNotValid() {
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(new Object(), "t");
        br.addError(new FieldError("t", "f", "required"));
        org.springframework.web.bind.MethodArgumentNotValidException e =
                new org.springframework.web.bind.MethodArgumentNotValidException(null, br);
        ServletWebRequest req = new ServletWebRequest(new MockHttpServletRequest(), new MockHttpServletResponse());
        ResponseEntity<Object> r = handler.handleMethodArgumentNotValid(e, new HttpHeaders(), HttpStatus.BAD_REQUEST, req);
        Assert.assertNotNull(r.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, r.getStatusCode());
    }

    private static class NotShowEx extends RuntimeException implements com.skrstop.framework.components.starter.web.exception.core.NotShowHttpStatusException {
    }

    @Test
    public void testHandleExceptionInternalEdgeCases() {
        // 非 ServletWebRequest 的 WebRequest
        org.springframework.web.context.request.WebRequest plainReq = Mockito.mock(org.springframework.web.context.request.WebRequest.class);
        Mockito.when(plainReq.getHeaderNames()).thenReturn(java.util.Collections.<String>emptyIterator());
        Mockito.when(plainReq.getHeader(Mockito.anyString())).thenReturn("v");
        ResponseEntity<Object> r = handler.handleExceptionInternal(new HttpMessageNotReadableException("bad"), null, new HttpHeaders(), HttpStatus.BAD_REQUEST, plainReq);
        Assert.assertNotNull(r.getBody());

        // JsonMappingException 无 cause
        ServletWebRequest req = new ServletWebRequest(new MockHttpServletRequest(), new MockHttpServletResponse());
        JsonMappingException noCause = new JsonMappingException(new java.io.StringWriter(), "m");
        ResponseEntity<Object> r2 = handler.handleExceptionInternal(new HttpMessageNotReadableException("bad", noCause), null, new HttpHeaders(), HttpStatus.BAD_REQUEST, req);
        Assert.assertNotNull(r2.getBody());

        // 非 JsonMappingException 的 cause
        ResponseEntity<Object> r3 = handler.handleExceptionInternal(new HttpMessageNotReadableException("bad", new RuntimeException("c")), null, new HttpHeaders(), HttpStatus.BAD_REQUEST, req);
        Assert.assertNotNull(r3.getBody());

        // ServletWebRequest 且带请求头，覆盖 header 遍历 lambda
        MockHttpServletRequest withHeaders = new MockHttpServletRequest();
        withHeaders.addHeader("Content-Type", "application/json");
        ResponseEntity<Object> r4 = handler.handleExceptionInternal(new HttpMessageNotReadableException("bad"), null, new HttpHeaders(), HttpStatus.BAD_REQUEST, new ServletWebRequest(withHeaders, new MockHttpServletResponse()));
        Assert.assertNotNull(r4.getBody());

        // NotShowHttpStatusException -> 200
        ResponseEntity<Object> r5 = handler.handleExceptionInternal(new NotShowEx(), null, new HttpHeaders(), HttpStatus.BAD_REQUEST, req);
        Assert.assertEquals(HttpStatus.OK, r5.getStatusCode());

        // BusinessThrowable -> 200
        ResponseEntity<Object> r6 = handler.handleExceptionInternal(new SkrstopBusinessException("biz"), null, new HttpHeaders(), HttpStatus.BAD_REQUEST, req);
        Assert.assertEquals(HttpStatus.OK, r6.getStatusCode());
    }

    @Test
    public void testHandlerExceptionNullProps() {
        ExceptionHandleChainPattern excChain = new ExceptionHandleChainPattern(new java.util.ArrayList<>());
        ReflectionTestUtils.invokeMethod(excChain, "initChainPattern");
        handler = new RequestExceptionHandler(excChain, null, null);
        ReflectionTestUtils.setField(handler, "errorPath", "/error");
        response = new MockHttpServletResponse();
        Assert.assertNotNull(handle(new RuntimeException("plain")));
        Assert.assertEquals(500, response.getStatus());
    }

    @Test
    public void testHandlerExceptionEdgeCases() {
        // skipErrorPath 命中 -> 只打印异常栈
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/error");
        Assert.assertNotNull(handler.handlerException(req, response, new RuntimeException("x")));

        // null request -> else 分支后 NPE
        try {
            handler.handlerException(null, response, new RuntimeException("x"));
            Assert.fail("expect NPE");
        } catch (NullPointerException expected) {
        }

        // NotShow -> 200
        response = new MockHttpServletResponse();
        MockHttpServletRequest req2 = new MockHttpServletRequest("GET", "/test");
        handler.handlerException(req2, response, new NotShowEx());
        Assert.assertEquals(200, response.getStatus());

        // protobuf 请求头 -> 不设置 content type
        MockHttpServletRequest req3 = new MockHttpServletRequest();
        req3.addHeader("x", "application/x-protobuf;charset=UTF-8");
        MockHttpServletResponse resp3 = new MockHttpServletResponse();
        handler.setResponseContentType(req3, resp3);
        Assert.assertNull(resp3.getContentType());

        // skipErrorPath(null) -> false
        Assert.assertFalse((Boolean) ReflectionTestUtils.invokeMethod(handler, "skipErrorPath", new Object[]{null}));
    }

    static class BizError extends Error implements com.skrstop.framework.components.core.exception.core.BusinessThrowable {
    }

    static class NotShowError extends Error implements com.skrstop.framework.components.starter.web.exception.core.NotShowHttpStatusException {
    }

    static class CustomError extends Error implements com.skrstop.framework.components.starter.web.exception.core.CustomHttpStatusException {
        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.I_AM_A_TEAPOT;
        }
    }

    @Test
    public void testHandlerExceptionBodyCacheAndAlwaysOk() {
        // 带 bodyCache attribute -> ternary 非空分支
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
        request.setAttribute(RequestConst.BODY_CACHE_NAME, "myBody");
        request.setAttribute(RequestConst.BODY_CACHE, "{\"a\":1}");
        Assert.assertNotNull(handler.handlerException(request, response, new RuntimeException("x")));

        // alwaysReturnHttpOk -> handlerException 最终 200
        com.skrstop.framework.components.starter.web.configuration.GlobalExceptionProperties props =
                new com.skrstop.framework.components.starter.web.configuration.GlobalExceptionProperties();
        props.setAlwaysReturnHttpOk(true);
        ExceptionHandleChainPattern excChain = new ExceptionHandleChainPattern(new java.util.ArrayList<>());
        handler = new RequestExceptionHandler(excChain, null, props);
        ReflectionTestUtils.setField(handler, "errorPath", "/error");
        response = new MockHttpServletResponse();
        Assert.assertNotNull(handle(new RuntimeException("x")));
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void testHandleErrorVariants() {
        // NotShow Error -> 200
        Assert.assertNotNull(handler.handleError(new MockHttpServletRequest(), response, new NotShowError()));
        Assert.assertEquals(200, response.getStatus());

        // Custom status Error -> 418
        response = new MockHttpServletResponse();
        Assert.assertNotNull(handler.handleError(new MockHttpServletRequest(), response, new CustomError()));
        Assert.assertEquals(418, response.getStatus());

        // Business Error -> 200
        response = new MockHttpServletResponse();
        Assert.assertNotNull(handler.handleError(new MockHttpServletRequest(), response, new BizError()));
        Assert.assertEquals(200, response.getStatus());

        // null props -> 500
        ExceptionHandleChainPattern excChain = new ExceptionHandleChainPattern(new java.util.ArrayList<>());
        ErrorHandleChainPattern errChain = new ErrorHandleChainPattern(new java.util.ArrayList<>());
        handler = new RequestExceptionHandler(excChain, errChain, null);
        ReflectionTestUtils.setField(handler, "errorPath", "/error");
        response = new MockHttpServletResponse();
        Assert.assertNotNull(handler.handleError(new MockHttpServletRequest(), response, new AssertionError("x")));
        Assert.assertEquals(500, response.getStatus());
    }

    @Test
    public void testHandleErrorAlwaysOk() {
        com.skrstop.framework.components.starter.web.configuration.GlobalExceptionProperties props =
                new com.skrstop.framework.components.starter.web.configuration.GlobalExceptionProperties();
        props.setAlwaysReturnHttpOk(true);
        ExceptionHandleChainPattern excChain = new ExceptionHandleChainPattern(new java.util.ArrayList<>());
        ErrorHandleChainPattern errChain = new ErrorHandleChainPattern(new java.util.ArrayList<>());
        handler = new RequestExceptionHandler(excChain, errChain, props);
        ReflectionTestUtils.setField(handler, "errorPath", "/error");
        response = new MockHttpServletResponse();
        Assert.assertNotNull(handler.handleError(new MockHttpServletRequest(), response, new AssertionError("x")));
        Assert.assertEquals(200, response.getStatus());
    }
}
