package com.skrstop.framework.components.util.media.exception.video;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class VideoHandleExceptionTest {

    @Test
    public void testConstructors() {
        Assert.assertNotNull(new VideoHandleException());
        IllegalStateException ise = new IllegalStateException("ise");
        VideoHandleException e1 = new VideoHandleException(ise);
        Assert.assertSame(ise, e1.getThrowable());
        VideoHandleException e2 = new VideoHandleException("msg");
        Assert.assertEquals("msg", e2.getIResult().getMessage());
        VideoHandleException e3 = new VideoHandleException("msg", ise);
        Assert.assertSame(ise, e3.getThrowable());
        Assert.assertEquals("msg", e3.getIResult().getMessage());
    }

}
