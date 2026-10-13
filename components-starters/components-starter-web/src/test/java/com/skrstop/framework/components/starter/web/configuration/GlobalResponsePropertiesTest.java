package com.skrstop.framework.components.starter.web.configuration;

import com.skrstop.framework.components.starter.web.configuration.GlobalResponseProperties.DefaultExceptionCode;
import com.skrstop.framework.components.util.constant.DateFormatConst;
import org.junit.Assert;
import org.junit.Test;

public class GlobalResponsePropertiesTest {

    @Test
    public void testDefaults() {
        GlobalResponseProperties p = new GlobalResponseProperties();
        Assert.assertTrue(p.isEnable());
        Assert.assertFalse(p.isShowNullValue());
        Assert.assertTrue(p.isSupportFeign());
        Assert.assertTrue(p.isLongToString());
        Assert.assertEquals(DateFormatConst.NORM_DATETIME_PATTERN, p.getDateTimeFormat());
        Assert.assertEquals(DateFormatConst.NORM_DATE_PATTERN, p.getDateFormat());
        Assert.assertEquals(DateFormatConst.NORM_TIME_PATTERN, p.getTimeFormat());
        Assert.assertEquals(DateFormatConst.NORM_YEAR_PATTERN, p.getYearFormat());
        Assert.assertEquals(DateFormatConst.NORM_MONTH_DAY_PATTERN, p.getMonthDayFormat());
        Assert.assertEquals(DateFormatConst.NORM_YEAR_MONTH_PATTERN, p.getYearMonthFormat());
        Assert.assertFalse(p.isDisableGlobalTransResultTypeResponse());
        Assert.assertFalse(p.getDisableGlobalResponseList().isEmpty());
        Assert.assertFalse(p.isEnableFeignTransResultTypeResponse());
        Assert.assertNotNull(p.getDefaultExceptionCode());
    }

    @Test
    public void testSettersAndBuilder() {
        GlobalResponseProperties p = new GlobalResponseProperties();
        p.setEnable(false);
        p.setShowNullValue(true);
        p.setSupportFeign(false);
        p.setLongToString(false);
        p.setDateTimeFormat("dt");
        p.setDateFormat("d");
        p.setTimeFormat("t");
        p.setYearFormat("y");
        p.setMonthDayFormat("md");
        p.setYearMonthFormat("ym");
        p.setDisableGlobalTransResultTypeResponse(true);
        p.setDisableGlobalResponseList(java.util.Collections.singletonList("a"));
        p.setEnableFeignTransResultTypeResponse(true);
        Assert.assertFalse(p.isEnable());
        Assert.assertTrue(p.isShowNullValue());
        Assert.assertFalse(p.isSupportFeign());
        Assert.assertFalse(p.isLongToString());
        Assert.assertEquals("dt", p.getDateTimeFormat());
        Assert.assertEquals("d", p.getDateFormat());
        Assert.assertEquals("t", p.getTimeFormat());
        Assert.assertEquals("y", p.getYearFormat());
        Assert.assertEquals("md", p.getMonthDayFormat());
        Assert.assertEquals("ym", p.getYearMonthFormat());
        Assert.assertTrue(p.isDisableGlobalTransResultTypeResponse());
        Assert.assertEquals(java.util.Collections.singletonList("a"), p.getDisableGlobalResponseList());
        Assert.assertTrue(p.isEnableFeignTransResultTypeResponse());
    }

    @Test
    public void testDefaultExceptionCode() {
        DefaultExceptionCode c = new DefaultExceptionCode();
        Assert.assertNotNull(new DefaultExceptionCode(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
                15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31));
        DefaultExceptionCode built = DefaultExceptionCode.builder()
                .success(0).fail(1).notFound(2).forbidden(3).busy(4)
                .matchParameter(5).notSupportMediaType(6).notAcceptedMediaType(7)
                .requestTimeout(8).requestMethodNotAllowed(9).missParameter(10)
                .fileUpload(11).fileNotPoint(12).notSupported(13).decoding(14)
                .encoding(15).deserialization(16).serialization(17).illegalArgument(18)
                .illegalAccess(19).httpHandle(20).rpcHandle(21).parameter(22)
                .nullPointer(23).indexOutOfBounds(24).ioStreaming(25).accessLimit(26)
                .controllerDeprecated(27).intranetLimit(28).privacyInfo(29).noProcessor(30)
                .build();
        Assert.assertNotNull(c);
        Assert.assertEquals(0, built.getSuccess());
        Assert.assertEquals(15, built.getEncoding());
        Assert.assertEquals(30, built.getNoProcessor());
        built.setSuccess(100);
        Assert.assertEquals(100, built.getSuccess());
        DefaultExceptionCode chained = new DefaultExceptionCode().setSuccess(1);
        Assert.assertEquals(1, chained.getSuccess());
    }
}
