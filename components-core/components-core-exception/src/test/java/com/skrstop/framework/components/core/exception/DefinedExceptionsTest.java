package com.skrstop.framework.components.core.exception;

import com.skrstop.framework.components.core.exception.common.CommonExceptionCode;
import com.skrstop.framework.components.core.exception.defined.coding.DecodingException;
import com.skrstop.framework.components.core.exception.defined.coding.EncodingException;
import com.skrstop.framework.components.core.exception.defined.illegal.*;
import com.skrstop.framework.components.core.exception.defined.illegal.IllegalAccessException;
import com.skrstop.framework.components.core.exception.defined.illegal.IllegalArgumentException;
import com.skrstop.framework.components.core.exception.defined.illegal.IndexOutOfBoundsException;
import com.skrstop.framework.components.core.exception.defined.illegal.NullPointerException;
import com.skrstop.framework.components.core.exception.defined.io.IOStreamingException;
import com.skrstop.framework.components.core.exception.defined.rpc.HttpHandleException;
import com.skrstop.framework.components.core.exception.defined.rpc.RpcHandleException;
import com.skrstop.framework.components.core.exception.defined.serialization.DeserializationException;
import com.skrstop.framework.components.core.exception.defined.serialization.SerializationException;
import org.junit.Assert;
import org.junit.Test;

/**
 * 覆盖所有 defined 异常类的构造器
 *
 * @author 蒋时华
 */
public class DefinedExceptionsTest {

    @Test
    public void testDecodingException() {
        DecodingException e1 = new DecodingException();
        Assert.assertEquals(CommonExceptionCode.DECODING.getCode(), e1.getIResult().getCode());
        Assert.assertEquals(CommonExceptionCode.DECODING.getMessage(), e1.getIResult().getMessage());

        RuntimeException cause = new RuntimeException("cause");
        DecodingException e2 = new DecodingException(cause);
        Assert.assertEquals(CommonExceptionCode.DECODING.getCode(), e2.getIResult().getCode());
        Assert.assertEquals("cause", e2.getExceptionMessage());
        Assert.assertSame(cause, e2.getThrowable());

        DecodingException e3 = new DecodingException("msg");
        Assert.assertEquals("msg", e3.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.DECODING.getCode(), e3.getIResult().getCode());

        DecodingException e4 = new DecodingException("msg", cause);
        Assert.assertEquals("msg", e4.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.DECODING.getCode(), e4.getIResult().getCode());
        Assert.assertSame(cause, e4.getThrowable());
    }

    @Test
    public void testEncodingException() {
        EncodingException e1 = new EncodingException();
        Assert.assertEquals(CommonExceptionCode.ENCODING.getCode(), e1.getIResult().getCode());
        Assert.assertEquals(CommonExceptionCode.ENCODING.getMessage(), e1.getIResult().getMessage());

        RuntimeException cause = new RuntimeException("cause");
        EncodingException e2 = new EncodingException(cause);
        Assert.assertEquals(CommonExceptionCode.ENCODING.getCode(), e2.getIResult().getCode());
        Assert.assertEquals("cause", e2.getExceptionMessage());
        Assert.assertSame(cause, e2.getThrowable());

        EncodingException e3 = new EncodingException("msg");
        Assert.assertEquals("msg", e3.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.ENCODING.getCode(), e3.getIResult().getCode());

        EncodingException e4 = new EncodingException("msg", cause);
        Assert.assertEquals("msg", e4.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.ENCODING.getCode(), e4.getIResult().getCode());
        Assert.assertSame(cause, e4.getThrowable());
    }

    @Test
    public void testIllegalAccessException() {
        IllegalAccessException e1 = new IllegalAccessException();
        Assert.assertEquals(CommonExceptionCode.ILLEGAL_ACCESS.getCode(), e1.getIResult().getCode());
        Assert.assertEquals(CommonExceptionCode.ILLEGAL_ACCESS.getMessage(), e1.getIResult().getMessage());

        RuntimeException cause = new RuntimeException("cause");
        IllegalAccessException e2 = new IllegalAccessException(cause);
        Assert.assertEquals(CommonExceptionCode.ILLEGAL_ACCESS.getCode(), e2.getIResult().getCode());
        Assert.assertEquals("cause", e2.getExceptionMessage());
        Assert.assertSame(cause, e2.getThrowable());

        IllegalAccessException e3 = new IllegalAccessException("msg");
        Assert.assertEquals("msg", e3.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.ILLEGAL_ACCESS.getCode(), e3.getIResult().getCode());

        IllegalAccessException e4 = new IllegalAccessException("msg", cause);
        Assert.assertEquals("msg", e4.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.ILLEGAL_ACCESS.getCode(), e4.getIResult().getCode());
        Assert.assertSame(cause, e4.getThrowable());
    }

    @Test
    public void testIllegalArgumentException() {
        IllegalArgumentException e1 = new IllegalArgumentException();
        Assert.assertEquals(CommonExceptionCode.ILLEGAL_ARGUMENT.getCode(), e1.getIResult().getCode());
        Assert.assertEquals(CommonExceptionCode.ILLEGAL_ARGUMENT.getMessage(), e1.getIResult().getMessage());

        RuntimeException cause = new RuntimeException("cause");
        IllegalArgumentException e2 = new IllegalArgumentException(cause);
        Assert.assertEquals(CommonExceptionCode.ILLEGAL_ARGUMENT.getCode(), e2.getIResult().getCode());
        Assert.assertEquals("cause", e2.getExceptionMessage());
        Assert.assertSame(cause, e2.getThrowable());

        IllegalArgumentException e3 = new IllegalArgumentException("msg");
        Assert.assertEquals("msg", e3.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.ILLEGAL_ARGUMENT.getCode(), e3.getIResult().getCode());

        IllegalArgumentException e4 = new IllegalArgumentException("msg", cause);
        Assert.assertEquals("msg", e4.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.ILLEGAL_ARGUMENT.getCode(), e4.getIResult().getCode());
        Assert.assertSame(cause, e4.getThrowable());
    }

    @Test
    public void testIndexOutOfBoundsException() {
        IndexOutOfBoundsException e1 = new IndexOutOfBoundsException();
        Assert.assertEquals(CommonExceptionCode.INDEX_OUT_OF_BOUNDS.getCode(), e1.getIResult().getCode());
        Assert.assertEquals(CommonExceptionCode.INDEX_OUT_OF_BOUNDS.getMessage(), e1.getIResult().getMessage());

        RuntimeException cause = new RuntimeException("cause");
        IndexOutOfBoundsException e2 = new IndexOutOfBoundsException(cause);
        Assert.assertEquals(CommonExceptionCode.INDEX_OUT_OF_BOUNDS.getCode(), e2.getIResult().getCode());
        Assert.assertEquals("cause", e2.getExceptionMessage());
        Assert.assertSame(cause, e2.getThrowable());

        IndexOutOfBoundsException e3 = new IndexOutOfBoundsException("msg");
        Assert.assertEquals("msg", e3.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.INDEX_OUT_OF_BOUNDS.getCode(), e3.getIResult().getCode());

        IndexOutOfBoundsException e4 = new IndexOutOfBoundsException("msg", cause);
        Assert.assertEquals("msg", e4.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.INDEX_OUT_OF_BOUNDS.getCode(), e4.getIResult().getCode());
        Assert.assertSame(cause, e4.getThrowable());
    }

    @Test
    public void testNotSupportedException() {
        NotSupportedException e1 = new NotSupportedException();
        Assert.assertEquals(CommonExceptionCode.NOT_SUPPORTED.getCode(), e1.getIResult().getCode());
        Assert.assertEquals(CommonExceptionCode.NOT_SUPPORTED.getMessage(), e1.getIResult().getMessage());

        RuntimeException cause = new RuntimeException("cause");
        NotSupportedException e2 = new NotSupportedException(cause);
        Assert.assertEquals(CommonExceptionCode.NOT_SUPPORTED.getCode(), e2.getIResult().getCode());
        Assert.assertEquals("cause", e2.getExceptionMessage());
        Assert.assertSame(cause, e2.getThrowable());

        NotSupportedException e3 = new NotSupportedException("msg");
        Assert.assertEquals("msg", e3.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.NOT_SUPPORTED.getCode(), e3.getIResult().getCode());

        NotSupportedException e4 = new NotSupportedException("msg", cause);
        Assert.assertEquals("msg", e4.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.NOT_SUPPORTED.getCode(), e4.getIResult().getCode());
        Assert.assertSame(cause, e4.getThrowable());
    }

    @Test
    public void testNullPointerException() {
        NullPointerException e1 = new NullPointerException();
        Assert.assertEquals(CommonExceptionCode.NULL_POINTER.getCode(), e1.getIResult().getCode());
        Assert.assertEquals(CommonExceptionCode.NULL_POINTER.getMessage(), e1.getIResult().getMessage());

        RuntimeException cause = new RuntimeException("cause");
        NullPointerException e2 = new NullPointerException(cause);
        Assert.assertEquals(CommonExceptionCode.NULL_POINTER.getCode(), e2.getIResult().getCode());
        Assert.assertEquals("cause", e2.getExceptionMessage());
        Assert.assertSame(cause, e2.getThrowable());

        NullPointerException e3 = new NullPointerException("msg");
        Assert.assertEquals("msg", e3.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.NULL_POINTER.getCode(), e3.getIResult().getCode());

        NullPointerException e4 = new NullPointerException("msg", cause);
        Assert.assertEquals("msg", e4.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.NULL_POINTER.getCode(), e4.getIResult().getCode());
        Assert.assertSame(cause, e4.getThrowable());
    }

    @Test
    public void testParameterException() {
        ParameterException e1 = new ParameterException();
        Assert.assertEquals(CommonExceptionCode.PARAMETER.getCode(), e1.getIResult().getCode());
        Assert.assertEquals(CommonExceptionCode.PARAMETER.getMessage(), e1.getIResult().getMessage());

        RuntimeException cause = new RuntimeException("cause");
        ParameterException e2 = new ParameterException(cause);
        Assert.assertEquals(CommonExceptionCode.PARAMETER.getCode(), e2.getIResult().getCode());
        Assert.assertEquals("cause", e2.getExceptionMessage());
        Assert.assertSame(cause, e2.getThrowable());

        ParameterException e3 = new ParameterException("msg");
        Assert.assertEquals("msg", e3.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.PARAMETER.getCode(), e3.getIResult().getCode());

        ParameterException e4 = new ParameterException("msg", cause);
        Assert.assertEquals("msg", e4.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.PARAMETER.getCode(), e4.getIResult().getCode());
        Assert.assertSame(cause, e4.getThrowable());
    }

    @Test
    public void testIOStreamingException() {
        IOStreamingException e1 = new IOStreamingException();
        Assert.assertEquals(CommonExceptionCode.IO_STREAMING.getCode(), e1.getIResult().getCode());
        Assert.assertEquals(CommonExceptionCode.IO_STREAMING.getMessage(), e1.getIResult().getMessage());

        RuntimeException cause = new RuntimeException("cause");
        IOStreamingException e2 = new IOStreamingException(cause);
        Assert.assertEquals(CommonExceptionCode.IO_STREAMING.getCode(), e2.getIResult().getCode());
        Assert.assertEquals("cause", e2.getExceptionMessage());
        Assert.assertSame(cause, e2.getThrowable());

        IOStreamingException e3 = new IOStreamingException("msg");
        Assert.assertEquals("msg", e3.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.IO_STREAMING.getCode(), e3.getIResult().getCode());

        IOStreamingException e4 = new IOStreamingException("msg", cause);
        Assert.assertEquals("msg", e4.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.IO_STREAMING.getCode(), e4.getIResult().getCode());
        Assert.assertSame(cause, e4.getThrowable());
    }

    @Test
    public void testHttpHandleException() {
        HttpHandleException e1 = new HttpHandleException();
        Assert.assertEquals(CommonExceptionCode.HTTP_HANDLE.getCode(), e1.getIResult().getCode());
        Assert.assertEquals(CommonExceptionCode.HTTP_HANDLE.getMessage(), e1.getIResult().getMessage());

        RuntimeException cause = new RuntimeException("cause");
        HttpHandleException e2 = new HttpHandleException(cause);
        Assert.assertEquals(CommonExceptionCode.HTTP_HANDLE.getCode(), e2.getIResult().getCode());
        Assert.assertEquals("cause", e2.getExceptionMessage());
        Assert.assertSame(cause, e2.getThrowable());

        HttpHandleException e3 = new HttpHandleException("msg");
        Assert.assertEquals("msg", e3.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.HTTP_HANDLE.getCode(), e3.getIResult().getCode());

        HttpHandleException e4 = new HttpHandleException("msg", cause);
        Assert.assertEquals("msg", e4.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.HTTP_HANDLE.getCode(), e4.getIResult().getCode());
        Assert.assertSame(cause, e4.getThrowable());
    }

    @Test
    public void testRpcHandleException() {
        RpcHandleException e1 = new RpcHandleException();
        Assert.assertEquals(CommonExceptionCode.RPC_HANDLE.getCode(), e1.getIResult().getCode());
        Assert.assertEquals(CommonExceptionCode.RPC_HANDLE.getMessage(), e1.getIResult().getMessage());

        RuntimeException cause = new RuntimeException("cause");
        RpcHandleException e2 = new RpcHandleException(cause);
        Assert.assertEquals(CommonExceptionCode.RPC_HANDLE.getCode(), e2.getIResult().getCode());
        Assert.assertEquals("cause", e2.getExceptionMessage());
        Assert.assertSame(cause, e2.getThrowable());

        RpcHandleException e3 = new RpcHandleException("msg");
        Assert.assertEquals("msg", e3.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.RPC_HANDLE.getCode(), e3.getIResult().getCode());

        RpcHandleException e4 = new RpcHandleException("msg", cause);
        Assert.assertEquals("msg", e4.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.RPC_HANDLE.getCode(), e4.getIResult().getCode());
        Assert.assertSame(cause, e4.getThrowable());
    }

    @Test
    public void testDeserializationException() {
        DeserializationException e1 = new DeserializationException();
        Assert.assertEquals(CommonExceptionCode.DESERIALIZATION.getCode(), e1.getIResult().getCode());
        Assert.assertEquals(CommonExceptionCode.DESERIALIZATION.getMessage(), e1.getIResult().getMessage());

        RuntimeException cause = new RuntimeException("cause");
        DeserializationException e2 = new DeserializationException(cause);
        Assert.assertEquals(CommonExceptionCode.DESERIALIZATION.getCode(), e2.getIResult().getCode());
        Assert.assertEquals("cause", e2.getExceptionMessage());
        Assert.assertSame(cause, e2.getThrowable());

        DeserializationException e3 = new DeserializationException("msg");
        Assert.assertEquals("msg", e3.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.DESERIALIZATION.getCode(), e3.getIResult().getCode());

        DeserializationException e4 = new DeserializationException("msg", cause);
        Assert.assertEquals("msg", e4.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.DESERIALIZATION.getCode(), e4.getIResult().getCode());
        Assert.assertSame(cause, e4.getThrowable());
    }

    @Test
    public void testSerializationException() {
        SerializationException e1 = new SerializationException();
        Assert.assertEquals(CommonExceptionCode.SERIALIZATION.getCode(), e1.getIResult().getCode());
        Assert.assertEquals(CommonExceptionCode.SERIALIZATION.getMessage(), e1.getIResult().getMessage());

        RuntimeException cause = new RuntimeException("cause");
        SerializationException e2 = new SerializationException(cause);
        Assert.assertEquals(CommonExceptionCode.SERIALIZATION.getCode(), e2.getIResult().getCode());
        Assert.assertEquals("cause", e2.getExceptionMessage());
        Assert.assertSame(cause, e2.getThrowable());

        SerializationException e3 = new SerializationException("msg");
        Assert.assertEquals("msg", e3.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.SERIALIZATION.getCode(), e3.getIResult().getCode());

        SerializationException e4 = new SerializationException("msg", cause);
        Assert.assertEquals("msg", e4.getIResult().getMessage());
        Assert.assertEquals(CommonExceptionCode.SERIALIZATION.getCode(), e4.getIResult().getCode());
        Assert.assertSame(cause, e4.getThrowable());
    }
}
