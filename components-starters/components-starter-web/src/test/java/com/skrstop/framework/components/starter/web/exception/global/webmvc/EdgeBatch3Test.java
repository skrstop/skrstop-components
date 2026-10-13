package com.skrstop.framework.components.starter.web.exception.global.webmvc;

import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.exception.SkrstopBusinessException;
import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.starter.web.configuration.GlobalExceptionProperties;
import com.skrstop.framework.components.starter.web.configuration.GlobalResponseProperties;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ErrorHandleChainPattern;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandleChainPattern;
import com.skrstop.framework.components.starter.web.response.core.ResponseHandleChainPattern;
import com.skrstop.framework.components.starter.web.response.webmvc.ReturnResponseHandler;
import com.skrstop.framework.components.util.constant.FeignConst;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.Collections;

public class EdgeBatch3Test {

    @Test
    public void testWebmvcHandlerNoResourcePlainWebRequest() throws Exception {
        ExceptionHandleChainPattern excChain = new ExceptionHandleChainPattern(new ArrayList<>());
        ReflectionTestUtils.invokeMethod(excChain, "initChainPattern");
        ErrorHandleChainPattern errChain = new ErrorHandleChainPattern(new ArrayList<>());
        ReflectionTestUtils.invokeMethod(errChain, "initChainPattern");
        RequestExceptionHandler handler =
                new RequestExceptionHandler(
                        excChain, errChain, null);
        ReflectionTestUtils.setField(handler, "errorPath", "/error");

        // 非 ServletWebRequest
        WebRequest plainReq = Mockito.mock(WebRequest.class);
        org.springframework.http.ResponseEntity<Object> r = handler.handleExceptionInternal(
                new NoResourceFoundException(HttpMethod.GET, "/nope"), null,
                new org.springframework.http.HttpHeaders(), org.springframework.http.HttpStatus.NOT_FOUND, plainReq);
        Assert.assertNotNull(r.getBody());

        // ServletWebRequest 且带 header（覆盖 header 遍历 lambda）
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/nope");
        req.addHeader("h1", "v1");
        org.springframework.http.ResponseEntity<Object> r2 = handler.handleExceptionInternal(
                new NoResourceFoundException(HttpMethod.GET, "/nope"), null,
                new org.springframework.http.HttpHeaders(), org.springframework.http.HttpStatus.NOT_FOUND,
                new ServletWebRequest(req, new MockHttpServletResponse()));
        Assert.assertNotNull(r2.getBody());

        // null props handler 的 handleExceptionInternal（getFinalStatusCode props-null 分支）
        org.springframework.http.ResponseEntity<Object> r3 = handler.handleExceptionInternal(
                new RuntimeException("x"), null, new org.springframework.http.HttpHeaders(),
                org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                new ServletWebRequest(new MockHttpServletRequest(), new MockHttpServletResponse()));
        Assert.assertEquals(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, r3.getStatusCode());
    }

    // ---------- webmvc ReturnResponseHandler 补充 ----------
    @Test
    public void testWebmvcReturnResponseHandlerFeignSupportTrue() throws Exception {
        org.springframework.web.method.support.HandlerMethodReturnValueHandler delegate =
                Mockito.mock(org.springframework.web.method.support.HandlerMethodReturnValueHandler.class);
        GlobalResponseProperties props = new GlobalResponseProperties();
        props.setSupportFeign(true);
        ResponseHandleChainPattern chain = new ResponseHandleChainPattern(new ArrayList<>());
        ReflectionTestUtils.invokeMethod(chain, "initChainPattern");
        ReturnResponseHandler handler =
                new ReturnResponseHandler(delegate, props, chain);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(FeignConst.USE_FEIGN_NAME, FeignConst.USE_FEIGN_VALUE);
        MockHttpServletResponse resp = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(req, resp);
        org.springframework.core.MethodParameter mp = new org.springframework.core.MethodParameter(
                AnnoHolder.class.getMethod("plain"), -1);
        org.springframework.web.method.support.ModelAndViewContainer mav = new org.springframework.web.method.support.ModelAndViewContainer();
        handler.handleReturnValue("v", mp, mav, webRequest);
        Mockito.verify(delegate).handleReturnValue(Mockito.any(), Mockito.eq(mp), Mockito.eq(mav), Mockito.eq(webRequest));
    }

    public static class AnnoHolder {
        public String plain() {return "v";}

        @com.skrstop.framework.components.starter.web.response.DisableTransResultTypeResponse
        public String transDisabled() {return "v";}
    }

    @Test
    public void testWebmvcReturnResponseHandlerPropsNullAndTransDisable() throws Exception {
        org.springframework.web.method.support.HandlerMethodReturnValueHandler delegate =
                Mockito.mock(org.springframework.web.method.support.HandlerMethodReturnValueHandler.class);
        ResponseHandleChainPattern chain = new ResponseHandleChainPattern(new ArrayList<>());
        ReflectionTestUtils.invokeMethod(chain, "initChainPattern");
        // globalResponseProperties 为 null + feign header → operand2 false
        ReturnResponseHandler handler =
                new ReturnResponseHandler(delegate, null, chain);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(FeignConst.USE_FEIGN_NAME, FeignConst.USE_FEIGN_VALUE);
        MockHttpServletResponse resp = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(req, resp);
        org.springframework.core.MethodParameter mp = new org.springframework.core.MethodParameter(
                AnnoHolder.class.getMethod("plain"), -1);
        org.springframework.web.method.support.ModelAndViewContainer mav = new org.springframework.web.method.support.ModelAndViewContainer();
        try {
            handler.handleReturnValue("v", mp, mav, webRequest);
            Assert.fail("expect NPE when props is null");
        } catch (NullPointerException expected) {
        }
        Mockito.verify(delegate, Mockito.never()).handleReturnValue(Mockito.any(), Mockito.eq(mp), Mockito.eq(mav), Mockito.eq(webRequest));
        GlobalResponseProperties props = new GlobalResponseProperties();
        ReturnResponseHandler handler2 =
                new ReturnResponseHandler(delegate, props, chain);
        ServletWebRequest webRequest2 = new ServletWebRequest(new MockHttpServletRequest(), new MockHttpServletResponse());
        org.springframework.core.MethodParameter mp2 = new org.springframework.core.MethodParameter(
                AnnoHolder.class.getMethod("transDisabled"), -1);
        org.springframework.web.method.support.ModelAndViewContainer mav2 = new org.springframework.web.method.support.ModelAndViewContainer();
        handler2.handleReturnValue("v", mp2, mav2, webRequest2);
        Mockito.verify(delegate).handleReturnValue(Mockito.any(), Mockito.eq(mp2), Mockito.eq(mav2), Mockito.eq(webRequest2));

        // props.setDisableGlobalTransResultTypeResponse → operand2 true
        props.setDisableGlobalTransResultTypeResponse(true);
        ServletWebRequest webRequest3 = new ServletWebRequest(new MockHttpServletRequest(), new MockHttpServletResponse());
        org.springframework.core.MethodParameter mp3 = new org.springframework.core.MethodParameter(
                AnnoHolder.class.getMethod("plain"), -1);
        org.springframework.web.method.support.ModelAndViewContainer mav3 = new org.springframework.web.method.support.ModelAndViewContainer();
        handler2.handleReturnValue("v", mp3, mav3, webRequest3);
        Mockito.verify(delegate).handleReturnValue(Mockito.any(), Mockito.eq(mp3), Mockito.eq(mav3), Mockito.eq(webRequest3));
    }

    @Test
    public void testWebmvcReturnResponseAutoConfigurationDecorate() {
        GlobalResponseProperties props = new GlobalResponseProperties();
        ResponseHandleChainPattern chain = new ResponseHandleChainPattern(new ArrayList<>());
        ReflectionTestUtils.invokeMethod(chain, "initChainPattern");
        org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter adapter =
                new org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter();
        com.skrstop.framework.components.starter.web.response.webmvc.ReturnResponseAutoConfiguration config =
                new com.skrstop.framework.components.starter.web.response.webmvc.ReturnResponseAutoConfiguration(adapter, props, chain);
        // 非 RequestResponseBodyMethodProcessor → 不替换
        org.springframework.web.method.support.HandlerMethodReturnValueHandler dummy =
                Mockito.mock(org.springframework.web.method.support.HandlerMethodReturnValueHandler.class);
        java.util.List<org.springframework.web.method.support.HandlerMethodReturnValueHandler> handlers =
                new ArrayList<>(Collections.singletonList(dummy));
        ReflectionTestUtils.invokeMethod(config, "decorateHandlers", handlers);
        Assert.assertSame(dummy, handlers.get(0));

        // 含 RequestResponseBodyMethodProcessor → 替换
        org.springframework.web.method.support.HandlerMethodReturnValueHandler rrmp =
                new RequestResponseBodyMethodProcessor(new ArrayList<>(Collections.singletonList(
                        new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter())));
        java.util.List<org.springframework.web.method.support.HandlerMethodReturnValueHandler> handlers2 =
                new ArrayList<>(Collections.singletonList(rrmp));
        ReflectionTestUtils.invokeMethod(config, "decorateHandlers", handlers2);
        Assert.assertNotSame(rrmp, handlers2.get(0));
    }

    // ---------- Error controllers ----------
    private ErrorAttributes errorAttrs(Throwable first, Throwable... rest) {
        ErrorAttributes attrs = Mockito.mock(ErrorAttributes.class);
        Mockito.when(attrs.getError(Mockito.any())).thenReturn(first, rest);
        return attrs;
    }

    static class HtmlThrowable extends Throwable implements com.skrstop.framework.components.starter.web.exception.core.ShowHtmlMessageException {
    }

    static class JsonOnlyThrowable extends Throwable implements com.skrstop.framework.components.starter.web.exception.core.ShowJsonMessageException {
    }

    static class HtmlJsonThrowable extends Throwable implements
            com.skrstop.framework.components.starter.web.exception.core.ShowHtmlMessageException,
            com.skrstop.framework.components.starter.web.exception.core.ShowJsonMessageException {
    }

    static class NotShowThrowable extends Throwable implements com.skrstop.framework.components.starter.web.exception.core.NotShowHttpStatusException {
    }

    @Test
    public void testErrorHtmlShowHtmlSequence() throws Throwable {
        ErrorProperties errorProperties = new ErrorProperties();
        DefaultErrorController controller = new DefaultErrorController(
                errorAttrs(new HtmlThrowable(), new SkrstopBusinessException("biz")),
                errorProperties, Collections.emptyList(), new GlobalExceptionProperties());
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/error");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        try {
            controller.errorHtml(request, response, new Exception("x"), webRequest);
            Assert.fail();
        } catch (SkrstopBusinessException expected) {
        }
        Assert.assertNotNull(response.getHeader("Accept"));
    }

    @Test
    public void testErrorHtmlShowHtmlAndJson() throws Throwable {
        ErrorProperties errorProperties = new ErrorProperties();
        DefaultErrorController controller = new DefaultErrorController(
                errorAttrs(new HtmlJsonThrowable()), errorProperties, Collections.emptyList(), new GlobalExceptionProperties());
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/error");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        try {
            controller.error(request, response, new Exception("x"), webRequest);
            Assert.fail();
        } catch (SkrstopRuntimeException expected) {
        }
    }

    @Test
    public void testGetErrorAttributeOptionsAll() {
        ErrorProperties errorProperties = new ErrorProperties();
        errorProperties.setIncludeException(true);
        errorProperties.setIncludeStacktrace(ErrorProperties.IncludeAttribute.ALWAYS);
        errorProperties.setIncludeMessage(ErrorProperties.IncludeAttribute.ALWAYS);
        errorProperties.setIncludeBindingErrors(ErrorProperties.IncludeAttribute.ALWAYS);
        DefaultErrorController controller = new DefaultErrorController(errorAttrs(null), errorProperties);
        MockHttpServletRequest request = new MockHttpServletRequest();
        ErrorAttributeOptions options = controller.getErrorAttributeOptions(request, MediaType.TEXT_HTML);
        Assert.assertTrue(options.getIncludes().contains(ErrorAttributeOptions.Include.EXCEPTION));
        Assert.assertTrue(options.getIncludes().contains(ErrorAttributeOptions.Include.STACK_TRACE));
        Assert.assertTrue(options.getIncludes().contains(ErrorAttributeOptions.Include.MESSAGE));
        Assert.assertTrue(options.getIncludes().contains(ErrorAttributeOptions.Include.BINDING_ERRORS));
    }

    @Test
    public void testHtmlControllerEdgeCases() throws Throwable {
        ErrorProperties errorProperties = new ErrorProperties();
        GlobalExceptionProperties props = new GlobalExceptionProperties();

        DefaultErrorHtmlController controller = new DefaultErrorHtmlController(
                errorAttrs(new NotShowThrowable()), errorProperties, Collections.emptyList(), props);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/error");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        controller.errorHtml(request, response, new Exception("x"), webRequest);
        Assert.assertEquals(200, response.getStatus());

        controller = new DefaultErrorHtmlController(errorAttrs(null), errorProperties, Collections.emptyList(), props);
        request = new MockHttpServletRequest("GET", "/error");
        response = new MockHttpServletResponse();
        webRequest = new ServletWebRequest(request, response);
        ModelAndView mv = controller.errorHtml(request, response, new Exception("x"), webRequest);
        Assert.assertEquals(CommonResultCode.FAIL.getMessage(), mv.getModel().get("message"));

        ErrorViewResolver resolver = Mockito.mock(ErrorViewResolver.class);
        Mockito.when(resolver.resolveErrorView(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new ModelAndView("custom"));
        controller = new DefaultErrorHtmlController(errorAttrs(null), errorProperties,
                Collections.singletonList(resolver), props);
        request = new MockHttpServletRequest("GET", "/error");
        response = new MockHttpServletResponse();
        webRequest = new ServletWebRequest(request, response);
        mv = controller.errorHtml(request, response, new Exception("x"), webRequest);
        Assert.assertEquals("custom", mv.getViewName());
    }

    @Test
    public void testErrorAlwaysOk() throws Throwable {
        ErrorProperties errorProperties = new ErrorProperties();
        GlobalExceptionProperties props = new GlobalExceptionProperties();
        props.setAlwaysReturnHttpOk(true);
        DefaultErrorController controller = new DefaultErrorController(errorAttrs(null), errorProperties, Collections.emptyList(), props);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/error");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        try {
            controller.error(request, response, new Exception("x"), webRequest);
            Assert.fail();
        } catch (SkrstopRuntimeException expected) {
        }
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void testErrorHtmlNormalReturnViaSubclass() throws Throwable {
        // 子类覆盖 error() 使其正常返回，从而覆盖 errorHtml 的返回语句
        DefaultErrorController controller = new DefaultErrorController(
                errorAttrs(null), new ErrorProperties(), Collections.emptyList(), new GlobalExceptionProperties()) {
            @Override
            public Object error(jakarta.servlet.http.HttpServletRequest request,
                                jakarta.servlet.http.HttpServletResponse response,
                                Exception ex,
                                WebRequest req) {
                return new ModelAndView("never");
            }
        };
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/error");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        Object mv = controller.errorHtml(request, response, new Exception("x"), webRequest);
        Assert.assertNotNull(mv);
    }

    @Test
    public void testErrorShowHtmlReturnsViaSubclass() throws Throwable {
        // 子类的 errorHtml 正常返回 -> error() 的 ShowHtml 递归分支返回值被返回
        DefaultErrorController controller = new DefaultErrorController(
                errorAttrs(new HtmlThrowable()), new ErrorProperties(), Collections.emptyList(), new GlobalExceptionProperties()) {
            @Override
            protected ModelAndView errorHtml(jakarta.servlet.http.HttpServletRequest request,
                                             jakarta.servlet.http.HttpServletResponse response,
                                             Exception ex,
                                             WebRequest req) {
                return new ModelAndView("never");
            }
        };
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/error");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        Object result = controller.error(request, response, new Exception("x"), webRequest);
        Assert.assertNotNull(result);
        Assert.assertEquals(MediaType.TEXT_HTML_VALUE, response.getHeader("Accept"));
    }

    @Test
    public void testHtmlControllerShowJson() throws Throwable {
        class JsonOnlyThrowable extends Throwable implements com.skrstop.framework.components.starter.web.exception.core.ShowJsonMessageException {
        }
        DefaultErrorHtmlController controller = new DefaultErrorHtmlController(
                errorAttrs(new JsonOnlyThrowable()), new ErrorProperties(), Collections.emptyList(), new GlobalExceptionProperties());
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/error");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        try {
            controller.errorHtml(request, response, new Exception("x"), webRequest);
            Assert.fail();
        } catch (Exception expected) {
        }
        Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeader("Accept"));
    }

    @Test
    public void testHtmlControllerNullProps() throws Throwable {
        DefaultErrorHtmlController controller = new DefaultErrorHtmlController(
                errorAttrs(null), new ErrorProperties(), Collections.emptyList(), null);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/error");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        ModelAndView mv = controller.errorHtml(request, response, new Exception("x"), webRequest);
        Assert.assertNotNull(mv);
    }

    @Test
    public void testErrorNullProps() throws Throwable {
        DefaultErrorController controller = new DefaultErrorController(
                errorAttrs(null), new ErrorProperties(), Collections.emptyList(), null);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/error");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        try {
            controller.error(request, response, new Exception("x"), webRequest);
            Assert.fail();
        } catch (SkrstopRuntimeException expected) {
        }
        Assert.assertEquals(500, response.getStatus());
    }

    @Test
    public void testHtmlControllerShowJsonNormalReturnViaSubclass() throws Throwable {
        // error() 正常抛出会使调用行无法到达探针点；子类覆盖 error() 正常返回以完整执行该行
        DefaultErrorHtmlController controller = new DefaultErrorHtmlController(
                errorAttrs(new JsonOnlyThrowable()), new ErrorProperties(), Collections.emptyList(), new GlobalExceptionProperties()) {
            @Override
            public Object error(jakarta.servlet.http.HttpServletRequest request,
                                jakarta.servlet.http.HttpServletResponse response,
                                Exception ex,
                                WebRequest req) {
                return null;
            }
        };
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/error");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        controller.errorHtml(request, response, new Exception("x"), webRequest);
        Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeader("Accept"));
    }

    @Test
    public void testHtmlControllerPlainError() throws Throwable {
        // 普通 Throwable（非 NotShow/Business）-> else 分支 setStatus + message 使用 error.getMessage()
        DefaultErrorHtmlController controller = new DefaultErrorHtmlController(
                errorAttrs(new Exception("plain")), new ErrorProperties(), Collections.emptyList(), new GlobalExceptionProperties());
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/error");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        ModelAndView mv = controller.errorHtml(request, response, new Exception("x"), webRequest);
        Assert.assertNotNull(mv);
        Assert.assertNotEquals(200, response.getStatus());
    }

    @Test
    public void testHtmlControllerShowJsonAndShowHtml() throws Throwable {
        // ShowJson && ShowHtml 同时满足 -> && 整体为 false，不设置 JSON header
        DefaultErrorHtmlController controller = new DefaultErrorHtmlController(
                errorAttrs(new HtmlJsonThrowable()), new ErrorProperties(), Collections.emptyList(), new GlobalExceptionProperties());
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/error");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        ModelAndView mv = controller.errorHtml(request, response, new Exception("x"), webRequest);
        Assert.assertNotNull(mv);
        Assert.assertNull(response.getHeader("Accept"));
    }
}
