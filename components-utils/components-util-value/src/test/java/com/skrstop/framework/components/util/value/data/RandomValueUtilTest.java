package com.skrstop.framework.components.util.value.data;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class RandomValueUtilTest {

    @Test
    public void testGetRandomAndGetStrAndGetNum() {
        String random = RandomValueUtil.getRandom(10);
        Assert.assertEquals(10, random.length());
        String str = RandomValueUtil.getStr(8);
        Assert.assertEquals(8, str.length());
        Assert.assertTrue(str.matches("[a-zA-Z]+"));
        String num = RandomValueUtil.getNum(6);
        Assert.assertEquals(6, num.length());
        Assert.assertTrue(num.matches("[0-9]+"));
        for (int i = 0; i < 100; i++) {
            int n = RandomValueUtil.getNum(5, 10);
            Assert.assertTrue(n >= 5 && n <= 10);
        }
    }

    @Test
    public void testEmailAndTel() {
        String email = RandomValueUtil.getEmail(5, 10);
        Assert.assertTrue(email.contains("@"));
        Assert.assertTrue(email.matches("[a-zA-Z0-9]+@.+"));
        String tel = RandomValueUtil.getTel();
        Assert.assertEquals(11, tel.length());
        Assert.assertTrue(tel.startsWith("1"));
    }

    @Test
    public void testChineseNameAndRoadAndSms() {
        String name = RandomValueUtil.getChineseName();
        Assert.assertFalse(name.isEmpty());
        String road = RandomValueUtil.getRoad();
        Assert.assertTrue(road.contains("号"));
        String sms = RandomValueUtil.getSMSCode();
        Assert.assertEquals(6, sms.length());
        int code = Integer.parseInt(sms);
        Assert.assertTrue(code >= 100000 && code <= 999999);
        // length<=0 直接返回空串
        Assert.assertEquals("", RandomValueUtil.getRandom(0));
        Assert.assertEquals("", RandomValueUtil.getStr(-1));
        Assert.assertEquals("", RandomValueUtil.getNum(-2));
        // 随机中文名覆盖两种性别与可选第三字
        for (int i = 0; i < 200; i++) {
            String cn = RandomValueUtil.getChineseName();
            Assert.assertTrue(cn.length() >= 2 && cn.length() <= 3);
        }
        // 边界闭区间
        int v = RandomValueUtil.getNum(5, 5);
        Assert.assertEquals(5, v);
    }

}
