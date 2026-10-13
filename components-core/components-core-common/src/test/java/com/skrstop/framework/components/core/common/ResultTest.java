package com.skrstop.framework.components.core.common;

import com.skrstop.framework.components.core.common.response.CollectionResult;
import com.skrstop.framework.components.core.common.response.DefaultResult;
import com.skrstop.framework.components.core.common.response.Result;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.ICollectionResult;
import com.skrstop.framework.components.core.common.response.core.IDataResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.util.EnumCodeUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

/**
 * @author 蒋时华
 */
public class ResultTest {

    @Test
    public void testCommonResultCode() {
        for (CommonResultCode code : CommonResultCode.values()) {
            Assert.assertNotNull(code.getCode());
            Assert.assertNotNull(code.getMessage());
            Assert.assertEquals(code, CommonResultCode.valueOf(code.name()));
        }
        Assert.assertEquals("server.success", CommonResultCode.SUCCESS.getCode());
        Assert.assertEquals("请求成功", CommonResultCode.SUCCESS.getMessage());

        CommonResultCode.SUCCESS.setCode("x");
        Assert.assertEquals("x", CommonResultCode.SUCCESS.getCode());
        CommonResultCode.SUCCESS.setCode("server.success");
        CommonResultCode.SUCCESS.setMessage("请求成功2");
        Assert.assertEquals("请求成功2", CommonResultCode.SUCCESS.getMessage());
    }

    @Test
    public void testDefaultResult() {
        DefaultResult success = DefaultResult.Builder.success();
        Assert.assertEquals(CommonResultCode.SUCCESS.getCode(), success.getCode());
        Assert.assertNotNull(success.getTimestamp());

        DefaultResult error = DefaultResult.Builder.error();
        Assert.assertEquals(CommonResultCode.FAIL.getCode(), error.getCode());

        DefaultResult copy = DefaultResult.Builder.result(success);
        Assert.assertEquals(success.getCode(), copy.getCode());
        Assert.assertEquals(success.getMessage(), copy.getMessage());

        DefaultResult empty = new DefaultResult();
        empty.setCode("c");
        empty.setMessage("m");
        empty.setTimestamp(1L);
        Assert.assertEquals("c", empty.getCode());
        Assert.assertEquals("m", empty.getMessage());
        Assert.assertEquals(Long.valueOf(1L), empty.getTimestamp());
    }

    @Test
    public void testResult() {
        Result success = Result.Builder.success();
        Assert.assertEquals(CommonResultCode.SUCCESS.getCode(), success.getCode());
        Assert.assertNull(success.getData());

        Result<String> withData = Result.Builder.success("data");
        Assert.assertEquals("data", withData.getData());

        Result error = Result.Builder.error();
        Assert.assertEquals(CommonResultCode.FAIL.getCode(), error.getCode());

        Result<String> errorData = Result.Builder.error("err");
        Assert.assertEquals("err", errorData.getData());

        Result<String> copy = Result.Builder.result(withData);
        Assert.assertEquals("data", copy.getData());
        Assert.assertEquals(withData.getCode(), copy.getCode());

        Result<String> fromIResult = Result.Builder.result((IResult) success);
        Assert.assertEquals(success.getCode(), fromIResult.getCode());

        Result<String> merged = Result.Builder.result((IResult) success, "d");
        Assert.assertEquals("d", merged.getData());

        Result empty = new Result();
        empty.setData("d1");
        Assert.assertEquals("d1", empty.getData());
    }

    @Test
    public void testEnumCodeUtil() {
        IResult fromEnum = EnumCodeUtil.transferEnumCode((IResult) CommonResultCode.SUCCESS);
        Assert.assertTrue(fromEnum instanceof DefaultResult);
        Assert.assertEquals(CommonResultCode.SUCCESS.getCode(), fromEnum.getCode());

        DefaultResult nonEnum = DefaultResult.Builder.success();
        IResult same = EnumCodeUtil.transferEnumCode((IResult) nonEnum);
        Assert.assertSame(nonEnum, same);

        IDataResult<String> dataFromEnum = EnumCodeUtil.transferEnumCode((IDataResult<String>) EnumDataResult.INSTANCE);
        Assert.assertTrue(dataFromEnum instanceof Result);
        Assert.assertEquals("d", dataFromEnum.getData());

        // 非 enum 的 IDataResult 直接返回
        IDataResult<String> dataNonEnum = Result.Builder.success("d");
        Assert.assertSame(dataNonEnum, EnumCodeUtil.transferEnumCode(dataNonEnum));

        // enum 的 ICollectionResult 转为 CollectionResult
        ICollectionResult<String> listFromEnum = EnumCodeUtil.transferEnumCode((ICollectionResult<String>) EnumCollectionResult.INSTANCE);
        Assert.assertTrue(listFromEnum instanceof CollectionResult);

        // 非 enum 的 ICollectionResult 直接返回
        CollectionResult<String> listNonEnum = CollectionResult.Builder.success();
        Assert.assertSame(listNonEnum, EnumCodeUtil.transferEnumCode((ICollectionResult<String>) listNonEnum));
    }

    public enum EnumDataResult implements IDataResult<String> {
        INSTANCE;

        @Override
        public String getData() {
            return "d";
        }

        @Override
        public Object getCode() {
            return "c";
        }

        @Override
        public String getMessage() {
            return "m";
        }

        @Override
        public void setData(String data) {
        }

        @Override
        public void setCode(Object code) {
        }

        @Override
        public void setMessage(String message) {
        }
    }

    public enum EnumCollectionResult implements ICollectionResult<String> {
        INSTANCE;

        @Override
        public Collection<String> getData() {
            return null;
        }

        @Override
        public void setData(Collection<String> data) {
        }

        @Override
        public Object getCode() {
            return "c";
        }

        @Override
        public String getMessage() {
            return "m";
        }

        @Override
        public void setCode(Object code) {
        }

        @Override
        public void setMessage(String message) {
        }
    }

}
