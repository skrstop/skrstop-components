package com.skrstop.framework.components.util.media.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class CommonExceptionCodeTest {

    @Test
    public void testEnum() {
        Assert.assertEquals("server.image.error", CommonExceptionCode.IMAGE_HANLE.getCode());
        Assert.assertEquals("图片处理异常", CommonExceptionCode.IMAGE_HANLE.getMessage());
        Assert.assertEquals("server.video.error", CommonExceptionCode.VIDEO_HANLE.getCode());
        Assert.assertEquals("视频处理异常异常", CommonExceptionCode.VIDEO_HANLE.getMessage());
        CommonExceptionCode.IMAGE_HANLE.setCode("x");
        Assert.assertEquals("x", CommonExceptionCode.IMAGE_HANLE.getCode());
        CommonExceptionCode.IMAGE_HANLE.setMessage("y");
        Assert.assertEquals("y", CommonExceptionCode.IMAGE_HANLE.getMessage());
    }

}
