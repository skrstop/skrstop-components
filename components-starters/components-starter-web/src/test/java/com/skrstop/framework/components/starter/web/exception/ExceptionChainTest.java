package com.skrstop.framework.components.starter.web.exception;

import com.skrstop.framework.components.core.exception.*;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ErrorHandleChainPattern;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandleChainPattern;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;

import java.util.HashSet;
import java.util.Set;

public class ExceptionChainTest {

    private ExceptionHandleChainPattern exceptionChain() {
        ExceptionHandleChainPattern chain = new ExceptionHandleChainPattern(new java.util.ArrayList<>());
        ReflectionTestUtils.invokeMethod(chain, "initChainPattern");
        return chain;
    }

    private ErrorHandleChainPattern errorChain() {
        ErrorHandleChainPattern chain = new ErrorHandleChainPattern(new java.util.ArrayList<>());
        ReflectionTestUtils.invokeMethod(chain, "initChainPattern");
        return chain;
    }

    @Test
    public void testParameterException() {
        ExceptionHandleChainPattern chain = exceptionChain();
        var result = chain.execute(new com.skrstop.framework.components.core.exception.defined.illegal.ParameterException("bad"));
        Assert.assertNotNull(result.getKey());
        Assert.assertEquals(400, result.getValue().intValue());
    }

    @Test
    public void testMissingServletRequestParameter() {
        // covered by ParameterException path? MissingServletRequestParameterException is not ParameterException
        var result = exceptionChain().execute(new MissingServletRequestParameterException("p", "String"));
        Assert.assertNotNull(result.getKey());
        Assert.assertEquals(500, result.getValue().intValue());
    }

    @Test
    public void testServerWebInput() {
        ExceptionHandleChainPattern chain = exceptionChain();
        // no cause → interceptor returns null → chain FAIL/500
        var r1 = chain.execute(new ServerWebInputException("msg"));
        Assert.assertNotNull(r1.getKey());
        Assert.assertEquals(500, r1.getValue().intValue());

        // cause TypeMismatchException
        ServerWebInputException e2 = new ServerWebInputException("msg", null,
                new TypeMismatchException("val", Integer.class));
        var r2 = chain.execute(e2);
        Assert.assertNotNull(r2.getKey());
        Assert.assertEquals(400, r2.getValue().intValue());

        // cause other type → next=true, result null → chain FAIL/500
        ServerWebInputException e3 = new ServerWebInputException("msg", null, new RuntimeException("x"));
        var r3 = chain.execute(e3);
        Assert.assertNull(r3.getKey());
        Assert.assertEquals(400, r3.getValue().intValue());
    }

    @Test
    public void testNotFound() {
        ExceptionHandleChainPattern chain = exceptionChain();
        var r1 = chain.execute(new ResponseStatusException(HttpStatus.NOT_FOUND));
        Assert.assertNotNull(r1.getKey());
        Assert.assertEquals(404, r1.getValue().intValue());

        var r2 = chain.execute(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        Assert.assertNull(r2.getKey());
        Assert.assertEquals(500, r2.getValue().intValue());
    }

    @Test
    public void testBindException() {
        ExceptionHandleChainPattern chain = exceptionChain();
        BindException e = new BindException(new BeanPropertyBindingResult(new Object(), "target"));
        e.addError(new FieldError("target", "field", "must not be null"));
        var r = chain.execute(e);
        Assert.assertNotNull(r.getKey());
        Assert.assertEquals(400, r.getValue().intValue());

        BindException e2 = new BindException(new BeanPropertyBindingResult(new Object(), "target"));
        var r2 = chain.execute(e2);
        Assert.assertNotNull(r2.getKey());
        Assert.assertEquals(400, r2.getValue().intValue());
    }

    @Test
    public void testConstraintViolation() {
        ExceptionHandleChainPattern chain = exceptionChain();
        var r1 = chain.execute(new ConstraintViolationException(new HashSet<>()));
        Assert.assertNotNull(r1.getKey());
        Assert.assertEquals(400, r1.getValue().intValue());

        ConstraintViolation<?> violation = org.mockito.Mockito.mock(ConstraintViolation.class);
        org.mockito.Mockito.when(violation.getMessage()).thenReturn("violation message");
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);
        var r2 = chain.execute(new ConstraintViolationException(violations));
        Assert.assertNotNull(r2.getKey());
        Assert.assertEquals(400, r2.getValue().intValue());
    }

    @Test
    public void testHttpClientError() {
        var r = exceptionChain().execute(
                org.springframework.web.client.HttpClientErrorException.create(HttpStatus.BAD_REQUEST,
                        "bad", null, "{\"message\":\"custom\"}".getBytes(), null));
        Assert.assertNotNull(r.getKey());
        Assert.assertEquals(500, r.getValue().intValue());
    }

    @Test
    public void testSkrstopExceptions() {
        ExceptionHandleChainPattern chain = exceptionChain();
        // business data exception
        SkrstopBusinessDataException bde = new SkrstopBusinessDataException("biz", new DefaultThrowableData());
        var r1 = chain.execute(bde);
        Assert.assertNotNull(r1.getKey());
        Assert.assertEquals(500, r1.getValue().intValue());

        // non-business skrstop runtime exception
        var r2 = chain.execute(new SkrstopRuntimeException("oops"));
        Assert.assertNotNull(r2.getKey());
        Assert.assertEquals(500, r2.getValue().intValue());

        // default
        var r3 = chain.execute(new RuntimeException("plain"));
        Assert.assertNotNull(r3.getKey());
        Assert.assertEquals(500, r3.getValue().intValue());
    }

    @Test
    public void testErrors() {
        ErrorHandleChainPattern chain = errorChain();
        var r1 = chain.execute(new SkrstopDataError("data error", new DefaultThrowableData()));
        Assert.assertNotNull(r1.getKey());

        var r2 = chain.execute(new SkrstopError("error"));
        Assert.assertNotNull(r2.getKey());

        var r3 = chain.execute(new AssertionError("plain"));
        Assert.assertNotNull(r3.getKey());
        Assert.assertEquals(500, r3.getValue().intValue());
    }

    @Test
    public void testEmptyChains() {
        ExceptionHandleChainPattern empty = new ExceptionHandleChainPattern(new java.util.ArrayList<>());
        var r = empty.execute(new RuntimeException("x"));
        Assert.assertNotNull(r.getKey());
        Assert.assertEquals(500, r.getValue().intValue());

        ErrorHandleChainPattern emptyE = new ErrorHandleChainPattern(new java.util.ArrayList<>());
        var r2 = emptyE.execute(new AssertionError("x"));
        Assert.assertNotNull(r2.getKey());
    }
}
