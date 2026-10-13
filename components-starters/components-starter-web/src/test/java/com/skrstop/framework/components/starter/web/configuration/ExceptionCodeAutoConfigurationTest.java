package com.skrstop.framework.components.starter.web.configuration;

import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.starter.web.exception.code.WebStarterExceptionCode;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class ExceptionCodeAutoConfigurationTest {

    @Test
    public void testDefaultExceptionCodeAutoConfiguration() {
        DefaultExceptionCodeAutoConfiguration config = new DefaultExceptionCodeAutoConfiguration();
        // null props → no-op
        config.init();

        GlobalResponseProperties props = new GlobalResponseProperties();
        ReflectionTestUtils.setField(config, "globalResponseProperties", props);
        // default code with all nulls → no-op
        config.init();

        GlobalResponseProperties.DefaultExceptionCode code = new GlobalResponseProperties.DefaultExceptionCode();
        code.setSuccess("s");
        code.setFail("f");
        code.setNotFound("n");
        code.setForbidden("fo");
        code.setBusy("b");
        code.setNotSupported("ns");
        code.setDecoding("d");
        code.setEncoding("e");
        code.setDeserialization("ds");
        code.setSerialization("se");
        code.setIllegalArgument("ia");
        code.setIllegalAccess("il");
        code.setHttpHandle("hh");
        code.setRpcHandle("rh");
        code.setParameter("p");
        code.setNullPointer("np");
        code.setIndexOutOfBounds("io");
        code.setIoStreaming("io2");
        code.setMatchParameter("mp");
        code.setNotSupportMediaType("nm");
        code.setNotAcceptedMediaType("na");
        code.setRequestTimeout("rt");
        code.setRequestMethodNotAllowed("rm");
        code.setMissParameter("ms");
        code.setFileUpload("fu");
        code.setFileNotPoint("fn");
        props.setDefaultExceptionCode(code);
        config.init();
        Assert.assertEquals("s", CommonResultCode.SUCCESS.getCode());
        Assert.assertEquals("mp", WebStarterExceptionCode.MATCH_PARAMETER.getCode());

        // null DefaultExceptionCode → no-op
        props.setDefaultExceptionCode(null);
        config.init();
    }

    @Test
    public void testAnnoExceptionCodeAutoConfiguration() {
        AnnoExceptionCodeAutoConfiguration config = new AnnoExceptionCodeAutoConfiguration();
        config.init();

        GlobalResponseProperties props = new GlobalResponseProperties();
        ReflectionTestUtils.setField(config, "globalResponseProperties", props);
        config.init();

        GlobalResponseProperties.DefaultExceptionCode code = new GlobalResponseProperties.DefaultExceptionCode();
        code.setAccessLimit("al");
        code.setControllerDeprecated("cd");
        code.setIntranetLimit("il");
        code.setPrivacyInfo("pi");
        code.setNoProcessor("np");
        props.setDefaultExceptionCode(code);
        config.init();

        props.setDefaultExceptionCode(null);
        config.init();
    }
}
