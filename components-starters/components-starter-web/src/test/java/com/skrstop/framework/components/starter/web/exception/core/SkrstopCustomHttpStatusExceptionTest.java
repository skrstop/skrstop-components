package com.skrstop.framework.components.starter.web.exception.core;

import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.exception.DefaultThrowableData;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

public class SkrstopCustomHttpStatusExceptionTest {

    private final DefaultThrowableData data = new DefaultThrowableData();
    private final IResult iResult = Mockito.mock(IResult.class);

    @Test
    public void testBusinessException() {
        HttpStatus status = HttpStatus.NOT_FOUND;
        Assert.assertEquals(status, new SkrstopCustomHttpStatusBusinessException("msg", status).getHttpStatus());
        Assert.assertEquals(status, new SkrstopCustomHttpStatusBusinessException(new RuntimeException(), status).getHttpStatus());
        Assert.assertEquals(status, new SkrstopCustomHttpStatusBusinessException("msg", status, new RuntimeException()).getHttpStatus());
        Assert.assertEquals(status, new SkrstopCustomHttpStatusBusinessException(iResult, status).getHttpStatus());
        Assert.assertEquals(HttpStatus.OK, new SkrstopCustomHttpStatusBusinessException(iResult, new RuntimeException()).getHttpStatus());
        Assert.assertEquals(status, new SkrstopCustomHttpStatusBusinessException(iResult, "msg", status).getHttpStatus());
        Assert.assertEquals(status, new SkrstopCustomHttpStatusBusinessException(iResult, "msg", status, new RuntimeException()).getHttpStatus());
        Assert.assertEquals("msg", new SkrstopCustomHttpStatusBusinessException("msg", status).getMessage());
    }

    @Test
    public void testBusinessDataException() {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Assert.assertEquals(status, new SkrstopCustomHttpStatusBusinessDataException(data, status).getHttpStatus());
        Assert.assertEquals(status, new SkrstopCustomHttpStatusBusinessDataException("msg", status, data).getHttpStatus());
        Assert.assertEquals(HttpStatus.OK, new SkrstopCustomHttpStatusBusinessDataException(new RuntimeException(), data).getHttpStatus());
        Assert.assertEquals(status, new SkrstopCustomHttpStatusBusinessDataException("msg", new RuntimeException(), data, status).getHttpStatus());
        Assert.assertEquals(status, new SkrstopCustomHttpStatusBusinessDataException(iResult, data, status).getHttpStatus());
        Assert.assertEquals(status, new SkrstopCustomHttpStatusBusinessDataException(iResult, new RuntimeException(), data, status).getHttpStatus());
        Assert.assertEquals(status, new SkrstopCustomHttpStatusBusinessDataException(iResult, "msg", data, status).getHttpStatus());
        Assert.assertEquals(status, new SkrstopCustomHttpStatusBusinessDataException(iResult, "msg", new RuntimeException(), data, status).getHttpStatus());
    }

    @Test
    public void testInterfaces() {
        Assert.assertTrue(new NotHandleException() {
        } instanceof NotHandleException);
        Assert.assertTrue(new NotShowHttpStatusException() {
        } instanceof NotShowHttpStatusException);
        Assert.assertTrue(new ShowHtmlMessageException() {
        } instanceof ShowHtmlMessageException);
        Assert.assertTrue(new ShowJsonMessageException() {
        } instanceof ShowJsonMessageException);
    }
}
