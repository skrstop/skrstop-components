package com.skrstop.framework.components.starter.web.exception.global.webmvc;

import com.skrstop.framework.components.core.exception.SkrstopBusinessException;
import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.core.exception.defined.illegal.ParameterException;
import com.skrstop.framework.components.starter.web.configuration.GlobalExceptionProperties;
import com.skrstop.framework.components.starter.web.exception.core.ShowHtmlMessageException;
import com.skrstop.framework.components.starter.web.exception.core.ShowJsonMessageException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

public class ErrorControllerTest {

    private ErrorAttributes errorAttributes(Throwable t) {
        ErrorAttributes attrs = Mockito.mock(ErrorAttributes.class);
        Mockito.when(attrs.getError(Mockito.any())).thenReturn(t);
        return attrs;
    }

    @Test
    public void testDefaultErrorController() throws Throwable {
        GlobalExceptionProperties props = new GlobalExceptionProperties();
        org.springframework.boot.autoconfigure.web.ErrorProperties errorProperties = new org.springframework.boot.autoconfigure.web.ErrorProperties();
        DefaultErrorController controller = new DefaultErrorController(errorAttributes(null), errorProperties, Collections.emptyList(), props);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/error");
        MockHttpServletResponse response = new MockHttpServletResponse();
        WebRequest webRequest = new org.springframework.web.context.request.ServletWebRequest(request, response);

        // plain exception → default 500 + SkrstopRuntimeException
        try {
            controller.error(request, response, new Exception("x"), webRequest);
            Assert.fail();
        } catch (SkrstopRuntimeException expected) {
        }
        Assert.assertEquals(500, response.getStatus());

        // parameter exception → 400
        DefaultErrorController paramController = new DefaultErrorController(
                errorAttributes(new ParameterException("p")), errorProperties, Collections.emptyList(), props);
        response = new MockHttpServletResponse();
        webRequest = new org.springframework.web.context.request.ServletWebRequest(request, response);
        try {
            paramController.error(request, response, new Exception("x"), webRequest);
            Assert.fail();
        } catch (Exception expected) {
        }
        Assert.assertEquals(400, response.getStatus());

        // always return http ok
        props.setAlwaysReturnHttpOk(true);
        response = new MockHttpServletResponse();
        webRequest = new org.springframework.web.context.request.ServletWebRequest(request, response);
        try {
            controller.error(request, response, new Exception("x"), webRequest);
            Assert.fail();
        } catch (SkrstopRuntimeException expected) {
        }
        Assert.assertEquals(200, response.getStatus());
        props.setAlwaysReturnHttpOk(false);

        // business throwable → 200 and rethrow skrstop throwable
        // business exception error
        response = new MockHttpServletResponse();
        webRequest = new org.springframework.web.context.request.ServletWebRequest(request, response);
        Mockito.reset(errorAttributes(null));
        ErrorAttributes attrs2 = errorAttributes(new SkrstopBusinessException("biz"));
        DefaultErrorController controller2 = new DefaultErrorController(attrs2, errorProperties, Collections.emptyList(), props);
        try {
            controller2.error(request, response, new Exception("x"), webRequest);
            Assert.fail();
        } catch (SkrstopBusinessException expected) {
        }
        Assert.assertEquals(200, response.getStatus());

        // not show http status
        class NotShowThrowable extends Throwable implements com.skrstop.framework.components.starter.web.exception.core.NotShowHttpStatusException {
        }
        DefaultErrorController controller3 = new DefaultErrorController(errorAttributes(new NotShowThrowable()), errorProperties, Collections.emptyList(), props);
        response = new MockHttpServletResponse();
        webRequest = new org.springframework.web.context.request.ServletWebRequest(request, response);
        try {
            controller3.error(request, response, new Exception("x"), webRequest);
            Assert.fail();
        } catch (SkrstopRuntimeException expected) {
        }
        Assert.assertEquals(200, response.getStatus());

        // show html message → errorHtml path
        class HtmlThrowable extends Throwable implements ShowHtmlMessageException {
        }
        DefaultErrorController controller4 = new DefaultErrorController(
                errorAttributes(new HtmlThrowable()), errorProperties, Collections.emptyList(), props);
        response = new MockHttpServletResponse();
        webRequest = new org.springframework.web.context.request.ServletWebRequest(request, response);
        try {
            controller4.error(request, response, new Exception("x"), webRequest);
            Assert.fail();
        } catch (StackOverflowError expected) {
            // ShowHtmlMessage-only error causes infinite error() -> errorHtml() -> error() recursion by design
        }
    }

    @Test
    public void testError404() throws Throwable {
        GlobalExceptionProperties props = new GlobalExceptionProperties();
        org.springframework.boot.autoconfigure.web.ErrorProperties errorProperties = new org.springframework.boot.autoconfigure.web.ErrorProperties();
        DefaultErrorController controller = new DefaultErrorController(errorAttributes(null), errorProperties, Collections.emptyList(), props);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/error");
        request.addHeader("h1", "v1");
        request.setAttribute("jakarta.servlet.forward.request_uri", "/orig");
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(404);
        WebRequest webRequest = new org.springframework.web.context.request.ServletWebRequest(request, response);
        try {
            controller.error(request, response, new Exception("x"), webRequest);
            Assert.fail();
        } catch (org.springframework.web.server.ResponseStatusException expected) {
            Assert.assertEquals(HttpStatus.NOT_FOUND, expected.getStatusCode());
        }
    }

    @Test
    public void testErrorHtml() throws Throwable {
        GlobalExceptionProperties props = new GlobalExceptionProperties();
        org.springframework.boot.autoconfigure.web.ErrorProperties errorProperties = new org.springframework.boot.autoconfigure.web.ErrorProperties();
        DefaultErrorController controller = new DefaultErrorController(errorAttributes(null), errorProperties, Collections.emptyList(), props);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/error");
        MockHttpServletResponse response = new MockHttpServletResponse();
        WebRequest webRequest = new org.springframework.web.context.request.ServletWebRequest(request, response);
        try {
            Assert.assertNull(controller.errorHtml(request, response, new Exception("x"), webRequest));
        } catch (Exception expected) {
        }
        Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeader(org.springframework.http.HttpHeaders.ACCEPT));
    }

    @Test
    public void testErrorAttributeOptions() throws Exception {
        org.springframework.boot.autoconfigure.web.ErrorProperties errorProperties = new org.springframework.boot.autoconfigure.web.ErrorProperties();
        DefaultErrorController controller = new DefaultErrorController(errorAttributes(null), errorProperties);
        MockHttpServletRequest request = new MockHttpServletRequest();
        var options = controller.getErrorAttributeOptions(request, MediaType.TEXT_HTML);
        Assert.assertNotNull(options);

        errorProperties.setIncludeException(true);
        request = new MockHttpServletRequest();
        var options2 = controller.getErrorAttributeOptions(request, MediaType.TEXT_HTML);
        Assert.assertTrue(options2.getIncludes().contains(org.springframework.boot.web.error.ErrorAttributeOptions.Include.EXCEPTION));

        errorProperties.setIncludeStacktrace(org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeAttribute.ALWAYS);
        Assert.assertTrue(controller.isIncludeStackTrace(new MockHttpServletRequest(), MediaType.TEXT_HTML));
        errorProperties.setIncludeStacktrace(org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeAttribute.ON_PARAM);
        Assert.assertFalse(controller.isIncludeStackTrace(new MockHttpServletRequest(), MediaType.TEXT_HTML));
        MockHttpServletRequest withParam = new MockHttpServletRequest();
        withParam.setParameter("trace", "true");
        Assert.assertTrue(controller.isIncludeStackTrace(withParam, MediaType.TEXT_HTML));
        errorProperties.setIncludeStacktrace(org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeAttribute.NEVER);
        Assert.assertFalse(controller.isIncludeStackTrace(withParam, MediaType.TEXT_HTML));

        errorProperties.setIncludeMessage(org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeAttribute.ALWAYS);
        Assert.assertTrue(controller.isIncludeMessage(new MockHttpServletRequest(), MediaType.TEXT_HTML));
        errorProperties.setIncludeMessage(org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeAttribute.ON_PARAM);
        MockHttpServletRequest withMsg = new MockHttpServletRequest();
        withMsg.setParameter("message", "true");
        Assert.assertTrue(controller.isIncludeMessage(withMsg, MediaType.TEXT_HTML));
        Assert.assertFalse(controller.isIncludeMessage(new MockHttpServletRequest(), MediaType.TEXT_HTML));

        errorProperties.setIncludeBindingErrors(org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeAttribute.ALWAYS);
        Assert.assertTrue(controller.isIncludeBindingErrors(new MockHttpServletRequest(), MediaType.TEXT_HTML));
        errorProperties.setIncludeBindingErrors(org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeAttribute.ON_PARAM);
        MockHttpServletRequest withErrors = new MockHttpServletRequest();
        withErrors.setParameter("errors", "true");
        Assert.assertTrue(controller.isIncludeBindingErrors(withErrors, MediaType.TEXT_HTML));
        Assert.assertFalse(controller.isIncludeBindingErrors(new MockHttpServletRequest(), MediaType.TEXT_HTML));

        Assert.assertNotNull(controller.getErrorProperties());
    }

    @Test
    public void testErrorHtmlController() throws Throwable {
        GlobalExceptionProperties props = new GlobalExceptionProperties();
        org.springframework.boot.autoconfigure.web.ErrorProperties errorProperties = new org.springframework.boot.autoconfigure.web.ErrorProperties();
        DefaultErrorHtmlController controller = new DefaultErrorHtmlController(errorAttributes(null), errorProperties, Collections.emptyList(), props);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/error");
        MockHttpServletResponse response = new MockHttpServletResponse();
        WebRequest webRequest = new org.springframework.web.context.request.ServletWebRequest(request, response);

        ModelAndView mv = controller.errorHtml(request, response, new Exception("x"), webRequest);
        Assert.assertNotNull(mv);
        Assert.assertEquals("error", mv.getViewName());
        Assert.assertNotNull(mv.getModel().get("message"));

        // status attribute
        request = new MockHttpServletRequest("GET", "/error");
        request.setAttribute("jakarta.servlet.error.status_code", 404);
        response = new MockHttpServletResponse();
        webRequest = new org.springframework.web.context.request.ServletWebRequest(request, response);
        controller = new DefaultErrorHtmlController(errorAttributes(null), errorProperties, Collections.emptyList(), props);
        ModelAndView mv2 = controller.errorHtml(request, response, new Exception("x"), webRequest);
        Assert.assertEquals(404, response.getStatus());

        // always ok
        props.setAlwaysReturnHttpOk(true);
        response = new MockHttpServletResponse();
        webRequest = new org.springframework.web.context.request.ServletWebRequest(request, response);
        controller.errorHtml(request, response, new Exception("x"), webRequest);
        Assert.assertEquals(200, response.getStatus());
        props.setAlwaysReturnHttpOk(false);

        // business throwable
        DefaultErrorHtmlController controller2 = new DefaultErrorHtmlController(errorAttributes(new SkrstopBusinessException("biz")), errorProperties, Collections.emptyList(), props);
        response = new MockHttpServletResponse();
        webRequest = new org.springframework.web.context.request.ServletWebRequest(request, response);
        controller2.errorHtml(request, response, new Exception("x"), webRequest);
        Assert.assertEquals(200, response.getStatus());

        // show json message → error() path throws
        class JsonThrowable extends Throwable implements ShowJsonMessageException {
        }
        DefaultErrorHtmlController controller3 = new DefaultErrorHtmlController(
                errorAttributes(new JsonThrowable()), errorProperties, Collections.emptyList(), props);
        response = new MockHttpServletResponse();
        webRequest = new org.springframework.web.context.request.ServletWebRequest(request, response);
        try {
            controller3.errorHtml(request, response, new Exception("x"), webRequest);
            Assert.fail();
        } catch (org.springframework.web.server.ResponseStatusException expected) {
            Assert.assertEquals(404, expected.getStatusCode().value());
        }
        Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeader(org.springframework.http.HttpHeaders.ACCEPT));
    }

    @Test
    public void testHtmlControllerOldCtors() {
        org.springframework.boot.autoconfigure.web.ErrorProperties errorProperties = new org.springframework.boot.autoconfigure.web.ErrorProperties();
        Assert.assertNotNull(new DefaultErrorHtmlController(errorAttributes(null), errorProperties));
        Assert.assertNotNull(new DefaultErrorHtmlController(errorAttributes(null), errorProperties, Collections.emptyList()));
    }

    @Test
    public void testErrorMvcAutoConfiguration() {
        GlobalExceptionProperties props = new GlobalExceptionProperties();
        org.springframework.boot.autoconfigure.web.ServerProperties serverProperties = new org.springframework.boot.autoconfigure.web.ServerProperties();
        org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties webMvcProperties = new org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties();
        org.springframework.beans.factory.ObjectProvider<org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder> unused = null;
        org.springframework.beans.factory.ObjectProvider<org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver> resolvers =
                Mockito.mock(org.springframework.beans.factory.ObjectProvider.class);
        Mockito.when(resolvers.orderedStream()).thenReturn(java.util.stream.Stream.empty());
        org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath path =
                Mockito.mock(org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath.class);
        Mockito.when(path.getPath()).thenReturn("/");
        com.skrstop.framework.components.starter.web.exception.global.webmvc.ErrorMvcAutoConfiguration config =
                new com.skrstop.framework.components.starter.web.exception.global.webmvc.ErrorMvcAutoConfiguration(
                        serverProperties, path, resolvers, props);
        Assert.assertNotNull(config.errorAttributes());
        Assert.assertNotNull(config.basicErrorController(config.errorAttributes()));
        props.setHasHtmlError(true);
        Assert.assertNotNull(config.basicErrorHtmlController(config.errorAttributes()));
    }
}
