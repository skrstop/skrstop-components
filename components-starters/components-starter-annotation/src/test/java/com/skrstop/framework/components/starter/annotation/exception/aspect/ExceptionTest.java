package com.skrstop.framework.components.starter.annotation.exception.aspect;

import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class ExceptionTest {

    @Test
    public void testExceptions() {
        Assert.assertTrue(new ProcessorException() instanceof SkrstopRuntimeException);
        Assert.assertTrue(new IntranetLimitException() instanceof SkrstopRuntimeException);
        Assert.assertTrue(new PrivacyInfoException() instanceof SkrstopRuntimeException);

        AccessLimitException accessLimitException = new AccessLimitException();
        Assert.assertTrue(accessLimitException instanceof SkrstopRuntimeException);
        Assert.assertNotNull(new AccessLimitException("msg").getMessage());

        ControllerDeprecatedException deprecated = new ControllerDeprecatedException();
        Assert.assertTrue(deprecated instanceof SkrstopRuntimeException);
        Assert.assertNotNull(new ControllerDeprecatedException("msg").getMessage());
    }

}