package com.skrstop.framework.components.value;

import com.skrstop.framework.components.util.value.data.RandomValueUtil;
import org.junit.Test;

/**
 * @author 蒋时华
 * @date 2023-11-30 14:09:16
 */
public class RandomValueUtilTest {

    @Test
    public void testName() {
        for (int i = 0; i < 100; i++) {
            System.out.println(RandomValueUtil.getChineseName());
        }
    }


}
