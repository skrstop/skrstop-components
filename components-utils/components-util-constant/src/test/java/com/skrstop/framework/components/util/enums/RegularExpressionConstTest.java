package com.skrstop.framework.components.util.enums;

import com.skrstop.framework.components.util.constant.RegularExpressionConst;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class RegularExpressionConstTest {

    @Test
    public void testReKeys() {
        // 触发接口常量初始化
        Assert.assertEquals(14, RegularExpressionConst.RE_KEYS.length);
        Assert.assertNotNull(RegularExpressionConst.RE_CHINESE);
        Assert.assertNotNull(RegularExpressionConst.RE_CHINESES);
    }

}
