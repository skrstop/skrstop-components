package com.skrstop.framework.components.util.compression.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class CompressExceptionTest {

    @Test
    public void testConstructors() {
        Assert.assertNotNull(new CompressException());
        RuntimeException cause = new RuntimeException("c");
        CompressException e1 = new CompressException(cause);
        Assert.assertNotNull(e1);
        CompressException e2 = new CompressException("msg");
        Assert.assertNotNull(e2);
        CompressException e3 = new CompressException("msg", cause);
        Assert.assertNotNull(e3);
    }

}
