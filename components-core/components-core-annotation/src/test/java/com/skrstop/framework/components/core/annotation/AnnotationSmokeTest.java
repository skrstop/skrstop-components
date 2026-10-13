package com.skrstop.framework.components.core.annotation;

import com.skrstop.framework.components.core.annotation.source.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 蒋时华
 */
@Compatible
@Experimental
@Optional
@Snapshot
@Version
public class AnnotationSmokeTest {

    @Test
    public void testAnnotationsRetentionAndTarget() {
        Class[] annotations = {Compatible.class, Experimental.class, Optional.class, Snapshot.class, Version.class};
        for (Class<?> annotation : annotations) {
            Retention retention = annotation.getAnnotation(Retention.class);
            Assert.assertNotNull(retention);
            Assert.assertEquals(RetentionPolicy.SOURCE, retention.value());
            Assert.assertNotNull(annotation.getAnnotation(Target.class));
        }
        // SOURCE 保留策略在运行时不可见
        Assert.assertNull(AnnotationSmokeTest.class.getAnnotation(Compatible.class));
    }
}
