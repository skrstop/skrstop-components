package com.skrstop.framework.components.starter.web.response;

import com.skrstop.framework.components.starter.web.configuration.GlobalResponseProperties;
import com.skrstop.framework.components.starter.web.response.core.ResponseHandleChainPattern;
import com.skrstop.framework.components.starter.web.response.webmvc.ReturnResponseHandler;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;

public class ReturnResponseHandlerTest {

    public static class Controller {
        @com.skrstop.framework.components.starter.web.response.DisableGlobalResponse
        public String annotated() {
            return "";
        }

        public String plain() {
            return "";
        }

        @com.skrstop.framework.components.starter.web.response.DisableTransResultTypeResponse
        public String annotatedTrans() {
            return "";
        }
    }

    private final ModelAndViewContainer mav = new ModelAndViewContainer();

    private ResponseHandleChainPattern chain() {
        ResponseHandleChainPattern chain = new ResponseHandleChainPattern(new ArrayList<>());
        org.springframework.test.util.ReflectionTestUtils.invokeMethod(chain, "initChainPattern");
        return chain;
    }

    @Test
    public void testSupportsReturnType() throws Exception {
        HandlerMethodReturnValueHandler delegate = Mockito.mock(HandlerMethodReturnValueHandler.class);
        ReturnResponseHandler handler = new ReturnResponseHandler(delegate, new GlobalResponseProperties(), chain());
        MethodParameterHolder holder = new MethodParameterHolder();
        org.springframework.core.MethodParameter mp = new org.springframework.core.MethodParameter(
                MethodParameterHolder.class.getMethod("m", String.class), 0);
        handler.supportsReturnType(mp);
        Mockito.verify(delegate).supportsReturnType(mp);
    }

    public static class MethodParameterHolder {
        public void m(String param) {
        }
    }

    @Test
    public void testDisabled() throws Exception {
        HandlerMethodReturnValueHandler delegate = Mockito.mock(HandlerMethodReturnValueHandler.class);
        GlobalResponseProperties props = new GlobalResponseProperties();
        props.setEnable(false);
        ReturnResponseHandler handler = new ReturnResponseHandler(delegate, props, chain());
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resp = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(req, resp);
        org.springframework.core.MethodParameter mp = new org.springframework.core.MethodParameter(
                Controller.class.getMethod("plain"), -1);
        handler.handleReturnValue("v", mp, mav, webRequest);
        Mockito.verify(delegate).handleReturnValue(Mockito.eq("v"), Mockito.eq(mp), Mockito.eq(mav), Mockito.eq(webRequest));
        Assert.assertEquals(200, resp.getStatus());
    }

    @Test
    public void testValidPath() throws Exception {
        HandlerMethodReturnValueHandler delegate = Mockito.mock(HandlerMethodReturnValueHandler.class);
        ReturnResponseHandler handler = new ReturnResponseHandler(delegate, new GlobalResponseProperties(), chain());
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setServletPath("/actuator");
        ServletWebRequest webRequest = new ServletWebRequest(req, new MockHttpServletResponse());
        org.springframework.core.MethodParameter mp = new org.springframework.core.MethodParameter(
                Controller.class.getMethod("plain"), -1);
        handler.handleReturnValue("v", mp, mav, webRequest);
        Mockito.verify(delegate).handleReturnValue(Mockito.eq("v"), Mockito.eq(mp), Mockito.eq(mav), Mockito.eq(webRequest));
    }

    @Test
    public void testDisableGlobalResponseAnnotation() throws Exception {
        HandlerMethodReturnValueHandler delegate = Mockito.mock(HandlerMethodReturnValueHandler.class);
        ReturnResponseHandler handler = new ReturnResponseHandler(delegate, new GlobalResponseProperties(), chain());
        MockHttpServletRequest req = new MockHttpServletRequest();
        ServletWebRequest webRequest = new ServletWebRequest(req, new MockHttpServletResponse());
        org.springframework.core.MethodParameter mp = new org.springframework.core.MethodParameter(
                Controller.class.getMethod("annotated"), -1);
        handler.handleReturnValue("v", mp, mav, webRequest);
        Mockito.verify(delegate).handleReturnValue(Mockito.eq("v"), Mockito.eq(mp), Mockito.eq(mav), Mockito.eq(webRequest));
    }

    @Test
    public void testFeignNotSupported() throws Exception {
        HandlerMethodReturnValueHandler delegate = Mockito.mock(HandlerMethodReturnValueHandler.class);
        GlobalResponseProperties props = new GlobalResponseProperties();
        props.setSupportFeign(false);
        ReturnResponseHandler handler = new ReturnResponseHandler(delegate, props, chain());
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("use_feign", "true");
        ServletWebRequest webRequest = new ServletWebRequest(req, new MockHttpServletResponse());
        org.springframework.core.MethodParameter mp = new org.springframework.core.MethodParameter(
                Controller.class.getMethod("plain"), -1);
        handler.handleReturnValue("v", mp, mav, webRequest);
        Mockito.verify(delegate).handleReturnValue(Mockito.eq("v"), Mockito.eq(mp), Mockito.eq(mav), Mockito.eq(webRequest));
    }

    @Test
    public void testNormalWrap() throws Exception {
        HandlerMethodReturnValueHandler delegate = Mockito.mock(HandlerMethodReturnValueHandler.class);
        ReturnResponseHandler handler = new ReturnResponseHandler(delegate, new GlobalResponseProperties(), chain());
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resp = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(req, resp);
        org.springframework.core.MethodParameter mp = new org.springframework.core.MethodParameter(
                Controller.class.getMethod("plain"), -1);
        handler.handleReturnValue("v", mp, mav, webRequest);
        Mockito.verify(delegate).handleReturnValue(Mockito.any(), Mockito.eq(mp), Mockito.eq(mav), Mockito.eq(webRequest));
        Assert.assertEquals(200, resp.getStatus());
    }

    @Test
    public void testNormalWrapNoResponse() throws Exception {
        HandlerMethodReturnValueHandler delegate = Mockito.mock(HandlerMethodReturnValueHandler.class);
        ReturnResponseHandler handler = new ReturnResponseHandler(delegate, new GlobalResponseProperties(), chain());
        MockHttpServletRequest req = new MockHttpServletRequest();
        ServletWebRequest webRequest = new ServletWebRequest(req);
        org.springframework.core.MethodParameter mp = new org.springframework.core.MethodParameter(
                Controller.class.getMethod("plain"), -1);
        handler.handleReturnValue("v", mp, mav, webRequest);
        Mockito.verify(delegate).handleReturnValue(Mockito.any(), Mockito.eq(mp), Mockito.eq(mav), Mockito.eq(webRequest));
    }

    @Test
    public void testValidPathException() throws Exception {
        HandlerMethodReturnValueHandler delegate = Mockito.mock(HandlerMethodReturnValueHandler.class);
        ReturnResponseHandler handler = new ReturnResponseHandler(delegate, new GlobalResponseProperties(), chain());
        NativeWebRequest badRequest = Mockito.mock(NativeWebRequest.class);
        Mockito.when(badRequest.getNativeRequest(Mockito.any())).thenThrow(new RuntimeException("boom"));
        Mockito.when(badRequest.getHeader(Mockito.anyString())).thenReturn(null);
        org.springframework.core.MethodParameter mp = new org.springframework.core.MethodParameter(
                Controller.class.getMethod("plain"), -1);
        handler.handleReturnValue("v", mp, mav, badRequest);
        Mockito.verify(delegate).handleReturnValue(Mockito.any(), Mockito.eq(mp), Mockito.eq(mav), Mockito.eq(badRequest));
    }

    @Test
    public void testNullProps() throws Exception {
        HandlerMethodReturnValueHandler delegate = Mockito.mock(HandlerMethodReturnValueHandler.class);
        ReturnResponseHandler handler = new ReturnResponseHandler(delegate, null, chain());
        MockHttpServletRequest req = new MockHttpServletRequest();
        ServletWebRequest webRequest = new ServletWebRequest(req, new MockHttpServletResponse());
        org.springframework.core.MethodParameter mp = new org.springframework.core.MethodParameter(
                Controller.class.getMethod("plain"), -1);
        try {
            handler.handleReturnValue("v", mp, mav, webRequest);
            Assert.fail();
        } catch (NullPointerException expected) {
        }

        // annotation present → short-circuits before null props dereference
        org.springframework.core.MethodParameter mpAnno = new org.springframework.core.MethodParameter(
                Controller.class.getMethod("annotatedTrans"), -1);
        handler.handleReturnValue("v", mpAnno, mav, webRequest);
        Mockito.verify(delegate).handleReturnValue(Mockito.any(), Mockito.eq(mpAnno), Mockito.eq(mav), Mockito.eq(webRequest));
    }
}
