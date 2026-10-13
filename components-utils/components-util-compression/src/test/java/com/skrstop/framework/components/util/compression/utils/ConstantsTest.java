package com.skrstop.framework.components.util.compression.utils;

import com.skrstop.framework.components.util.compression.bean.CompressionType;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class ConstantsTest {

    @Test
    public void testFileTypesAllow() {
        Assert.assertTrue(Constants.FILE_TYPES_ALLOW.COMPRESSING.contains(CompressionType.ZIP));
        Assert.assertFalse(Constants.FILE_TYPES_ALLOW.COMPRESSING.contains(CompressionType.SEVENZIP));
        Assert.assertTrue(Constants.FILE_TYPES_ALLOW.DECOMPRESSING.contains(CompressionType.SEVENZIP));
        Assert.assertEquals(10, Constants.FILE_TYPES_ALLOW.COMPRESSING.size());
        Assert.assertEquals(11, Constants.FILE_TYPES_ALLOW.DECOMPRESSING.size());
        Assert.assertTrue(Constants.SOURCE.DELETE);
        Assert.assertFalse(Constants.SOURCE.KEEP);
    }

}
