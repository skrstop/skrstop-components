package com.skrstop.framework.components.core.exception;

import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.exception.common.CommonExceptionCode;
import com.skrstop.framework.components.core.exception.core.BusinessThrowable;
import com.skrstop.framework.components.core.exception.core.SkrstopDataThrowable;
import com.skrstop.framework.components.core.exception.core.SkrstopThrowable;
import com.skrstop.framework.components.core.exception.util.ThrowableStackTraceUtil;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;

/**
 * 覆盖异常基类、错误码与工具类
 *
 * @author 蒋时华
 */
public class ExceptionsTest {

    private final RuntimeException cause = new RuntimeException("cause");

    @Test
    public void testSkrstopRuntimeException() {
        SkrstopRuntimeException e0 = new SkrstopRuntimeException();
        Assert.assertEquals(CommonResultCode.FAIL.getMessage(), e0.getMessage());
        Assert.assertEquals(CommonResultCode.FAIL.getCode(), e0.getIResult().getCode());

        SkrstopRuntimeException e1 = new SkrstopRuntimeException("msg1");
        Assert.assertEquals("msg1", e1.getMessage());
        Assert.assertEquals("msg1", e1.getIResult().getMessage());

        SkrstopRuntimeException e2 = new SkrstopRuntimeException(cause);
        Assert.assertEquals("cause", e2.getMessage());
        Assert.assertEquals("cause", e2.getExceptionMessage());
        Assert.assertSame(cause, e2.getThrowable());

        SkrstopRuntimeException e3 = new SkrstopRuntimeException("msg3", cause);
        Assert.assertEquals("msg3", e3.getIResult().getMessage());
        Assert.assertEquals("cause", e3.getExceptionMessage());
        Assert.assertSame(cause, e3.getThrowable());

        SkrstopRuntimeException e4 = new SkrstopRuntimeException((IResult) CommonExceptionCode.ENCODING);
        Assert.assertEquals(CommonExceptionCode.ENCODING.getCode(), e4.getIResult().getCode());
        // enum 会转换为 DefaultResult
        Assert.assertFalse(e4.getIResult() instanceof CommonExceptionCode);

        SkrstopRuntimeException e5 = new SkrstopRuntimeException((IResult) CommonExceptionCode.ENCODING, cause);
        Assert.assertSame(cause, e5.getThrowable());

        SkrstopRuntimeException e6 = new SkrstopRuntimeException((IResult) CommonExceptionCode.ENCODING, "msg6");
        Assert.assertEquals("msg6", e6.getIResult().getMessage());
        Assert.assertEquals("msg6", e6.getMessage());

        SkrstopRuntimeException e7 = new SkrstopRuntimeException((IResult) CommonExceptionCode.ENCODING, "msg7", cause);
        Assert.assertEquals("msg7", e7.getIResult().getMessage());
        Assert.assertSame(cause, e7.getThrowable());

        // toString
        String ts = e7.toString();
        Assert.assertTrue(ts.contains("错误信息"));
        Assert.assertTrue(ts.contains("自定义错误信息"));
        Assert.assertTrue(ts.contains("result结果"));

        // setter
        e7.setThrowable(cause);
        e7.setExceptionMessage("em");
        e7.setIResult(DefaultResultOfTest.result());
        Assert.assertEquals("em", e7.getExceptionMessage());
    }

    @Test
    public void testSkrstopError() {
        SkrstopError e0 = new SkrstopError();
        Assert.assertEquals(CommonResultCode.FAIL.getMessage(), e0.getMessage());

        SkrstopError e1 = new SkrstopError("msg1");
        Assert.assertEquals("msg1", e1.getMessage());

        SkrstopError e2 = new SkrstopError(cause);
        Assert.assertSame(cause, e2.getThrowable());
        Assert.assertEquals("cause", e2.getErrorMessage());

        SkrstopError e3 = new SkrstopError("msg3", cause);
        Assert.assertSame(cause, e3.getThrowable());

        SkrstopError e4 = new SkrstopError((IResult) CommonExceptionCode.ENCODING);
        Assert.assertEquals(CommonExceptionCode.ENCODING.getCode(), e4.getIResult().getCode());

        SkrstopError e5 = new SkrstopError((IResult) CommonExceptionCode.ENCODING, cause);
        Assert.assertSame(cause, e5.getThrowable());

        SkrstopError e6 = new SkrstopError((IResult) CommonExceptionCode.ENCODING, "msg6");
        Assert.assertEquals("msg6", e6.getIResult().getMessage());

        SkrstopError e7 = new SkrstopError((IResult) CommonExceptionCode.ENCODING, "msg7", cause);
        Assert.assertSame(cause, e7.getThrowable());

        Assert.assertTrue(e7.toString().contains("错误信息"));

        e7.setThrowable(cause);
        e7.setErrorMessage("em");
        Assert.assertEquals("em", e7.getErrorMessage());
    }

    @Test
    public void testSkrstopBusinessException() {
        SkrstopBusinessException e = new SkrstopBusinessException();
        Assert.assertTrue(e instanceof BusinessThrowable);
        Assert.assertEquals(CommonResultCode.FAIL.getMessage(), e.getMessage());

        Assert.assertNotNull(new SkrstopBusinessException("msg"));
        Assert.assertNotNull(new SkrstopBusinessException(cause));
        Assert.assertNotNull(new SkrstopBusinessException("msg", cause));
        Assert.assertNotNull(new SkrstopBusinessException((IResult) CommonExceptionCode.ENCODING));
        Assert.assertNotNull(new SkrstopBusinessException((IResult) CommonExceptionCode.ENCODING, cause));
        Assert.assertNotNull(new SkrstopBusinessException((IResult) CommonExceptionCode.ENCODING, "msg"));
        Assert.assertNotNull(new SkrstopBusinessException((IResult) CommonExceptionCode.ENCODING, "msg", cause));
    }

    @Test
    public void testSkrstopDataRuntimeException() {
        DefaultThrowableData data = new DefaultThrowableData();
        data.setData("detail");

        SkrstopDataRuntimeException e0 = new SkrstopDataRuntimeException(data);
        Assert.assertSame(data, e0.getThrowableData());
        Assert.assertTrue(e0 instanceof SkrstopDataThrowable);

        SkrstopDataRuntimeException e1 = new SkrstopDataRuntimeException("msg", data);
        Assert.assertEquals("msg", e1.getIResult().getMessage());

        SkrstopDataRuntimeException e2 = new SkrstopDataRuntimeException(cause, data);
        Assert.assertSame(cause, e2.getThrowable());

        SkrstopDataRuntimeException e3 = new SkrstopDataRuntimeException("msg", cause, data);
        Assert.assertSame(cause, e3.getThrowable());

        SkrstopDataRuntimeException e4 = new SkrstopDataRuntimeException((IResult) CommonExceptionCode.ENCODING, data);
        Assert.assertEquals(CommonExceptionCode.ENCODING.getCode(), e4.getIResult().getCode());

        SkrstopDataRuntimeException e5 = new SkrstopDataRuntimeException((IResult) CommonExceptionCode.ENCODING, cause, data);
        Assert.assertSame(cause, e5.getThrowable());

        SkrstopDataRuntimeException e6 = new SkrstopDataRuntimeException((IResult) CommonExceptionCode.ENCODING, "msg", data);
        Assert.assertEquals("msg", e6.getIResult().getMessage());

        SkrstopDataRuntimeException e7 = new SkrstopDataRuntimeException((IResult) CommonExceptionCode.ENCODING, "msg", cause, data);
        Assert.assertSame(cause, e7.getThrowable());

        String ts = e7.toString();
        Assert.assertTrue(ts.contains("异常数据信息"));
        Assert.assertTrue(ts.contains("detail"));

        e7.setThrowableData(data);
        Assert.assertSame(data, e7.getThrowableData());

        // throwableData 为 null 时的 toString 分支
        SkrstopDataRuntimeException e8 = new SkrstopDataRuntimeException((IResult) CommonExceptionCode.ENCODING, null);
        Assert.assertFalse(e8.toString().contains("异常数据信息"));
    }

    @Test
    public void testSkrstopBusinessDataException() {
        DefaultThrowableData data = new DefaultThrowableData();
        data.setData("d");

        SkrstopBusinessDataException e0 = new SkrstopBusinessDataException(data);
        Assert.assertTrue(e0 instanceof BusinessThrowable);

        Assert.assertNotNull(new SkrstopBusinessDataException("msg", data));
        Assert.assertNotNull(new SkrstopBusinessDataException(cause, data));
        Assert.assertNotNull(new SkrstopBusinessDataException("msg", cause, data));
        Assert.assertNotNull(new SkrstopBusinessDataException((IResult) CommonExceptionCode.ENCODING, data));
        Assert.assertNotNull(new SkrstopBusinessDataException((IResult) CommonExceptionCode.ENCODING, cause, data));
        Assert.assertNotNull(new SkrstopBusinessDataException((IResult) CommonExceptionCode.ENCODING, "msg", data));
        Assert.assertNotNull(new SkrstopBusinessDataException((IResult) CommonExceptionCode.ENCODING, "msg", cause, data));
    }

    @Test
    public void testSkrstopDataError() {
        DefaultThrowableData data = new DefaultThrowableData();
        data.setData("d");

        SkrstopDataError e0 = new SkrstopDataError(data);
        Assert.assertSame(data, e0.getThrowableData());
        Assert.assertTrue(e0 instanceof SkrstopDataThrowable);

        Assert.assertNotNull(new SkrstopDataError("msg", data));
        Assert.assertNotNull(new SkrstopDataError(cause, data));
        Assert.assertNotNull(new SkrstopDataError("msg", cause, data));
        Assert.assertNotNull(new SkrstopDataError((IResult) CommonExceptionCode.ENCODING, data));
        Assert.assertNotNull(new SkrstopDataError((IResult) CommonExceptionCode.ENCODING, cause, data));
        Assert.assertNotNull(new SkrstopDataError((IResult) CommonExceptionCode.ENCODING, "msg", data));
        Assert.assertNotNull(new SkrstopDataError((IResult) CommonExceptionCode.ENCODING, "msg", cause, data));

        SkrstopDataError e7 = new SkrstopDataError((IResult) CommonExceptionCode.ENCODING, "msg", cause, data);
        Assert.assertTrue(e7.toString().contains("异常数据信息"));
        e7.setThrowableData(data);
        Assert.assertSame(data, e7.getThrowableData());

        SkrstopDataError e8 = new SkrstopDataError((IResult) CommonExceptionCode.ENCODING, null);
        Assert.assertFalse(e8.toString().contains("异常数据信息"));
    }

    @Test
    public void testDefaultThrowableData() {
        DefaultThrowableData data = new DefaultThrowableData();
        Assert.assertNull(data.getData());
        data.setData("v");
        Assert.assertEquals("v", data.getData());
    }

    @Test
    public void testCommonExceptionCode() {
        for (CommonExceptionCode code : CommonExceptionCode.values()) {
            Assert.assertNotNull(code.getCode());
            Assert.assertNotNull(code.getMessage());
            Assert.assertEquals(code, CommonExceptionCode.valueOf(code.name()));
        }
        CommonExceptionCode.ENCODING.setCode("x");
        Assert.assertEquals("x", CommonExceptionCode.ENCODING.getCode());
        CommonExceptionCode.ENCODING.setCode("server.encoding");
    }

    @Test
    public void testThrowableStackTraceUtil() {
        RuntimeException e = new RuntimeException("boom");
        String stack = ThrowableStackTraceUtil.getStackTraceStr(e);
        Assert.assertTrue(stack.contains("java.lang.RuntimeException: boom"));
    }

    @Test
    public void testSkrstopThrowableDefaults() {
        SkrstopThrowable throwable = new SkrstopRuntimeException((IResult) CommonExceptionCode.ENCODING);
        Assert.assertEquals(CommonExceptionCode.ENCODING.getMessage(), throwable.getMessage());
        Assert.assertEquals(CommonExceptionCode.ENCODING.getCode(), throwable.getCode());
        Assert.assertNull(throwable.getException());

        // 未覆写 default 方法的实现类
        SkrstopThrowable plain = new SkrstopThrowable() {
            @Override
            public IResult getIResult() {
                return new com.skrstop.framework.components.core.common.response.DefaultResult(CommonResultCode.SUCCESS);
            }
        };
        Assert.assertEquals(CommonResultCode.SUCCESS.getMessage(), plain.getMessage());
        Assert.assertEquals(CommonResultCode.SUCCESS.getCode(), plain.getCode());
        Assert.assertNull(plain.getException());
    }

    @Test
    public void testThrowableStackTraceUtilIOException() throws Exception {
        // 模拟 StringWriter.close() 抛出 IOException，覆盖 finally 中的异常分支
        try (MockedConstruction<StringWriter> mocked = Mockito.mockConstruction(StringWriter.class,
                (mock, context) -> Mockito.doThrow(new IOException("close failed")).when(mock).close())) {
            RuntimeException e = new RuntimeException("boom");
            String stack = ThrowableStackTraceUtil.getStackTraceStr(e);
            Assert.assertNull(stack);
            Assert.assertEquals(1, mocked.constructed().size());
        }
    }

    static class DefaultResultOfTest {
        static IResult result() {
            return new com.skrstop.framework.components.core.common.response.DefaultResult(CommonResultCode.SUCCESS);
        }
    }

}
