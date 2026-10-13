package com.skrstop.framework.components.util.value.security;

import org.junit.Assert;
import org.junit.Test;

/**
 * SensitiveDataUtil 单元测试
 *
 * @author 蒋时华
 */
public class SensitiveDataUtilTest {

    @Test
    public void testNeedHideAndSetHideFlag() {
        try {
            SensitiveDataUtil.setHideFlag(true);
            Assert.assertTrue(SensitiveDataUtil.needHide());
            SensitiveDataUtil.setHideFlag(false);
            Assert.assertFalse(SensitiveDataUtil.needHide());
        } finally {
            SensitiveDataUtil.setHideFlag(true);
        }
    }

    @Test
    public void testHideFlagOffPassthrough() {
        try {
            SensitiveDataUtil.setHideFlag(false);
            Assert.assertEquals("abc", SensitiveDataUtil.alipayLogonIdHide("abc"));
            Assert.assertEquals("abc", SensitiveDataUtil.alipayLogonIdHideSMS("abc"));
            Assert.assertEquals("张三", SensitiveDataUtil.nameHide("张三"));
            Assert.assertEquals("123", SensitiveDataUtil.idCardNoHide("123", true));
            Assert.assertEquals("123", SensitiveDataUtil.idCardNoHide("123"));
            Assert.assertEquals("123", SensitiveDataUtil.bankCardNoHide("123", true));
            Assert.assertEquals("123", SensitiveDataUtil.bankCardNoHide("123"));
            Assert.assertEquals("123", SensitiveDataUtil.phoneOrTelNoHide("123", true));
            Assert.assertEquals("123", SensitiveDataUtil.phoneOrTelNoHide("123"));
            Assert.assertEquals("123", SensitiveDataUtil.cellphoneHide("123"));
            Assert.assertEquals("123", SensitiveDataUtil.cellphoneHideSMS("123"));
            Assert.assertEquals("a@b.c", SensitiveDataUtil.emailHide("a@b.c", true));
            Assert.assertEquals("a@b.c", SensitiveDataUtil.emailHide("a@b.c"));
            Assert.assertEquals("a@b.c", SensitiveDataUtil.emailHideSMS("a@b.c", true));
            Assert.assertEquals("a@b.c", SensitiveDataUtil.emailHideSMS("a@b.c"));
            Assert.assertEquals("x", SensitiveDataUtil.filterHide("x", "<a>", "</a>", 0));
            Assert.assertEquals("abc", SensitiveDataUtil.taobaoNickHide("abc"));
            Assert.assertEquals("abc", SensitiveDataUtil.defaultHide("abc"));
        } finally {
            SensitiveDataUtil.setHideFlag(true);
        }
    }

    @Test
    public void testBlankInputs() {
        Assert.assertNull(SensitiveDataUtil.alipayLogonIdHide(null));
        Assert.assertEquals("", SensitiveDataUtil.alipayLogonIdHide(""));
        Assert.assertEquals("  ", SensitiveDataUtil.alipayLogonIdHide("  "));
        Assert.assertNull(SensitiveDataUtil.alipayLogonIdHideSMS(null));
        Assert.assertEquals("", SensitiveDataUtil.alipayLogonIdHideSMS(""));
        Assert.assertNull(SensitiveDataUtil.nameHide(null));
        Assert.assertEquals("", SensitiveDataUtil.nameHide(""));
        Assert.assertNull(SensitiveDataUtil.idCardNoHide(null, true));
        Assert.assertEquals("", SensitiveDataUtil.idCardNoHide("", false));
        Assert.assertNull(SensitiveDataUtil.bankCardNoHide(null, true));
        Assert.assertNull(SensitiveDataUtil.phoneOrTelNoHide(null, true));
        Assert.assertEquals(" ", SensitiveDataUtil.phoneOrTelNoHide(" ", true));
        Assert.assertNull(SensitiveDataUtil.cellphoneHide(null));
        Assert.assertNull(SensitiveDataUtil.cellphoneHideSMS(null));
        Assert.assertNull(SensitiveDataUtil.emailHide(null, true));
        Assert.assertNull(SensitiveDataUtil.emailHideSMS(null, true));
        Assert.assertNull(SensitiveDataUtil.filterHide(null, "<a>", "</a>", 0));
        Assert.assertEquals(" ", SensitiveDataUtil.filterHide(" ", "<a>", "</a>", 0));
        Assert.assertNull(SensitiveDataUtil.taobaoNickHide(null));
        Assert.assertEquals("", SensitiveDataUtil.taobaoNickHide(""));
        Assert.assertNull(SensitiveDataUtil.defaultHide(null));
        Assert.assertEquals("  ", SensitiveDataUtil.defaultHide("  "));
        // customizeHide 不做 hideFlag 检查
        Assert.assertNull(SensitiveDataUtil.customizeHide(null, 1, 1, 1));
        Assert.assertEquals("", SensitiveDataUtil.customizeHide("", 1, 1, 1));
    }

    @Test
    public void testAlipayLogonIdHide() {
        // email → emailHide
        Assert.assertEquals("yaa***@163.com", SensitiveDataUtil.alipayLogonIdHide("yaaachaa@163.com"));
        // 手机号 → cellphoneHide
        Assert.assertEquals("150******59", SensitiveDataUtil.alipayLogonIdHide("15087653459"));
        // 非 email（@ 在首位不算 email）→ cellphoneHide
        Assert.assertEquals("@163.com".length() > 0 ? SensitiveDataUtil.cellphoneHide("@163.com") : "",
                SensitiveDataUtil.alipayLogonIdHide("@163.com"));
    }

    @Test
    public void testAlipayLogonIdHideSMS() {
        Assert.assertEquals("yaa*@163.*", SensitiveDataUtil.alipayLogonIdHideSMS("yaaachaa@163.com"));
        Assert.assertEquals("130*58", SensitiveDataUtil.alipayLogonIdHideSMS("13071835358"));
    }

    @Test
    public void testNameHide() {
        // 长度<=3 隐藏姓
        Assert.assertEquals("*明", SensitiveDataUtil.nameHide("李明"));
        Assert.assertEquals("*", SensitiveDataUtil.nameHide("李"));
        // 4-6
        Assert.assertEquals("***娜子", SensitiveDataUtil.nameHide("欧阳娜娜子"));
        // >6
        Assert.assertEquals("欧****啦啦", SensitiveDataUtil.nameHide("欧阳娜娜子啦啦"));
    }

    @Test
    public void testIdCardNoHide() {
        // 不校验
        Assert.assertEquals("3****************4", SensitiveDataUtil.idCardNoHide("362397201201012384"));
        // 校验通过
        Assert.assertEquals("3****************4", SensitiveDataUtil.idCardNoHide("362397201201012384", true));
        // 校验失败 → defaultHide
        String invalid = "abc-def-ghi";
        Assert.assertEquals(SensitiveDataUtil.defaultHide(invalid), SensitiveDataUtil.idCardNoHide(invalid, true));
        // 15 位合法
        Assert.assertEquals("1*************5", SensitiveDataUtil.idCardNoHide("123456789012345", true));
        // 14 位 + X
        Assert.assertEquals("12345*****123X", SensitiveDataUtil.idCardNoHide("1234567890123X", true));
    }

    @Test
    public void testBankCardNoHide() {
        // >=16 位
        Assert.assertEquals("622202*********0123", SensitiveDataUtil.bankCardNoHide("6222021234567890123"));
        // <16 位 → defaultHide
        Assert.assertEquals(SensitiveDataUtil.defaultHide("12345678"), SensitiveDataUtil.bankCardNoHide("12345678"));
        // 校验失败 → defaultHide
        Assert.assertEquals(SensitiveDataUtil.defaultHide("abc12345"),
                SensitiveDataUtil.bankCardNoHide("abc12345", true));
        // 校验通过且 <16 → defaultHide
        Assert.assertEquals(SensitiveDataUtil.defaultHide("12345678"), SensitiveDataUtil.bankCardNoHide("12345678", true));
        Assert.assertEquals("622202*********0123", SensitiveDataUtil.bankCardNoHide("6222021234567890123", true));
    }

    @Test
    public void testPhoneOrTelNoHide() {
        // 手机号
        Assert.assertEquals("150******59", SensitiveDataUtil.phoneOrTelNoHide("15087653459"));
        // 固话带 -
        Assert.assertEquals("0796-***4567", SensitiveDataUtil.phoneOrTelNoHide("0796-1234567"));
        // 无 - 非 1 开头 → defaultHide
        Assert.assertEquals(SensitiveDataUtil.defaultHide("36852045"), SensitiveDataUtil.phoneOrTelNoHide("36852045"));
        // 校验失败 → defaultHide
        Assert.assertEquals(SensitiveDataUtil.defaultHide("abc"), SensitiveDataUtil.phoneOrTelNoHide("abc", true));
        // trim 后合法
        Assert.assertEquals("150******59", SensitiveDataUtil.phoneOrTelNoHide(" 15087653459 ", true));
    }

    @Test
    public void testCellphoneHide() {
        Assert.assertEquals("130******58", SensitiveDataUtil.cellphoneHide("13071835358"));
        Assert.assertEquals("307****358", SensitiveDataUtil.cellphoneHide("3071835358"));
        Assert.assertEquals("07****358", SensitiveDataUtil.cellphoneHide("071835358"));
        Assert.assertEquals("8****8", SensitiveDataUtil.cellphoneHide("835358"));
        Assert.assertEquals("****", SensitiveDataUtil.cellphoneHide("1234"));
        // trim 生效
        Assert.assertEquals("8****8", SensitiveDataUtil.cellphoneHide(" 835358 "));
    }

    @Test
    public void testCellphoneHideSMS() {
        Assert.assertEquals("130*58", SensitiveDataUtil.cellphoneHideSMS("13071835358"));
        Assert.assertEquals("307*358", SensitiveDataUtil.cellphoneHideSMS("3071835358"));
        Assert.assertEquals("07*358", SensitiveDataUtil.cellphoneHideSMS("071835358"));
        Assert.assertEquals("8*8", SensitiveDataUtil.cellphoneHideSMS("835358"));
    }

    @Test
    public void testEmailHide() {
        Assert.assertEquals("yaa***@163.com", SensitiveDataUtil.emailHide("yaaachaa@163.com"));
        // 用户名不足 3 位
        Assert.assertEquals("d***@163.com", SensitiveDataUtil.emailHide("d@163.com"));
        // 校验失败 → defaultHide
        Assert.assertEquals(SensitiveDataUtil.defaultHide("noemail"), SensitiveDataUtil.emailHide("noemail", true));
        // @ 在首位 → 非 email
        Assert.assertEquals(SensitiveDataUtil.defaultHide("@163.com"), SensitiveDataUtil.emailHide("@163.com", true));
        // trim
        Assert.assertEquals("yaa***@163.com", SensitiveDataUtil.emailHide(" yaaachaa@163.com ", true));
    }

    @Test
    public void testEmailHideSMS() {
        // 域名短 → .*
        Assert.assertEquals("ddd*@hide.*", SensitiveDataUtil.emailHideSMS("ddddddd@hide.com"));
        // 域名长 → netviga*
        Assert.assertEquals("ddd*@netviga*", SensitiveDataUtil.emailHideSMS("ddddddd@netvigator.com"));
        // 用户名不足 3 位
        Assert.assertEquals("d*@netviga*", SensitiveDataUtil.emailHideSMS("d@netvigator.com"));
        // 正好 7 位域名点
        Assert.assertEquals("ddd*@outlook.*", SensitiveDataUtil.emailHideSMS("ddddddd@outlook.com"));
        // 校验失败
        Assert.assertEquals(SensitiveDataUtil.defaultHide("noemail"), SensitiveDataUtil.emailHideSMS("noemail", true));
        // 校验通过
        Assert.assertEquals("ddd*@163.*", SensitiveDataUtil.emailHideSMS("ddddddd@163.com", true));
    }

    @Test
    public void testTaobaoNickHide() {
        Assert.assertEquals("中**华", SensitiveDataUtil.taobaoNickHide("中华"));
        Assert.assertEquals("c**a", SensitiveDataUtil.taobaoNickHide("china"));
        Assert.assertEquals("c**a", SensitiveDataUtil.taobaoNickHide(" china "));
    }

    @Test
    public void testDefaultHide() {
        Assert.assertEquals("t*t", SensitiveDataUtil.defaultHide("ttt"));
        Assert.assertEquals("ttt***tt", SensitiveDataUtil.defaultHide("tttttttt"));
        // 长度 4：head=2 tail=1
        Assert.assertEquals("tt*t", SensitiveDataUtil.defaultHide("tttt"));
        Assert.assertEquals("t*t", SensitiveDataUtil.defaultHide(" ttt "));
    }

    @Test
    public void testCustomizeHide() {
        Assert.assertEquals("135****4561", SensitiveDataUtil.customizeHide("13568794561", 3, 4, 4));
        Assert.assertEquals("****4561", SensitiveDataUtil.customizeHide("13568794561", 0, 4, 4));
        Assert.assertEquals("135****", SensitiveDataUtil.customizeHide("13568794561", 3, 0, 4));
        Assert.assertEquals("135********", SensitiveDataUtil.customizeHide("13568794561", 3, 0, 8));
        // 参数不合法 → 返回源数据
        Assert.assertEquals("13568794561", SensitiveDataUtil.customizeHide("13568794561", -1, 4, 4));
        Assert.assertEquals("13568794561", SensitiveDataUtil.customizeHide("13568794561", 3, -1, 4));
        Assert.assertEquals("13568794561", SensitiveDataUtil.customizeHide("13568794561", 3, 4, -1));
        Assert.assertEquals("13568794561", SensitiveDataUtil.customizeHide("13568794561", 6, 6, 4));
        // tail = length → endIndex=0，尾部为全部
        Assert.assertEquals("***13568794561", SensitiveDataUtil.customizeHide("13568794561", 0, 11, 3));
        // trim
        Assert.assertEquals("135****4561", SensitiveDataUtil.customizeHide(" 13568794561 ", 3, 4, 4));
    }

    @Test
    public void testFilterHide() {
        String source = "<GovtID>432926198110191188</GovtID>";
        // ID card
        Assert.assertEquals("<GovtID>4****************8</GovtID>",
                SensitiveDataUtil.filterHide(source, "<GovtID>", "</GovtID>", SensitiveDataUtil.IDCARDNO_DATA));
        // bank card
        String bank = "<Card>6222021234567890</Card>";
        Assert.assertEquals("<Card>622202******7890</Card>",
                SensitiveDataUtil.filterHide(bank, "<Card>", "</Card>", SensitiveDataUtil.BANKCARDNO_DATA));
        // phone
        String phone = "<Tel>0796-1234567</Tel>";
        Assert.assertEquals("<Tel>0796-***4567</Tel>",
                SensitiveDataUtil.filterHide(phone, "<Tel>", "</Tel>", SensitiveDataUtil.PHONENO_DATA));
        // email
        String email = "<Mail>yaaachaa@163.com</Mail>";
        Assert.assertEquals("<Mail>yaa***@163.com</Mail>",
                SensitiveDataUtil.filterHide(email, "<Mail>", "</Mail>", SensitiveDataUtil.EMAIL_DATA));
        // default type
        Assert.assertEquals("<X>t*t</X>",
                SensitiveDataUtil.filterHide("<X>ttt</X>", "<X>", "</X>", 99));
        // 无匹配标签 → 原样返回
        Assert.assertEquals(source, SensitiveDataUtil.filterHide(source, "<No>", "</No>", 99));
        // 有起始标签无结束标签 → 原样返回
        Assert.assertEquals("<a>xxx", SensitiveDataUtil.filterHide("<a>xxx", "<a>", "</a>", 99));
        // 多个节点
        String multi = "<a>123456789012345</a>mid<a>123456789012345</a>";
        Assert.assertEquals("<a>1*************5</a>mid<a>1*************5</a>",
                SensitiveDataUtil.filterHide(multi, "<a>", "</a>", SensitiveDataUtil.IDCARDNO_DATA));
    }
}
