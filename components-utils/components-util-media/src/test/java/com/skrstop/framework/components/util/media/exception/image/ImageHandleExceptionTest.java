package com.skrstop.framework.components.util.media.exception.image;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class ImageHandleExceptionTest {

    @Test
    public void testConstructors() {
        Assert.assertNotNull(new ImageHandleException());
        IllegalStateException ise = new IllegalStateException("ise");
        ImageHandleException e1 = new ImageHandleException(ise);
        Assert.assertSame(ise, e1.getThrowable());
        ImageHandleException e2 = new ImageHandleException("msg");
        Assert.assertEquals("msg", e2.getIResult().getMessage());
        ImageHandleException e3 = new ImageHandleException("msg", ise);
        Assert.assertSame(ise, e3.getThrowable());
        Assert.assertEquals("msg", e3.getIResult().getMessage());
    }

}
