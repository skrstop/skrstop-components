package com.skrstop.framework.components.util.setting.properties;

import cn.hutool.setting.Setting;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class PropertiesUtilTest {

    @Test
    public void testGetSetting() {
        Setting setting = PropertiesUtil.getSetting("config/test.properties");
        Assert.assertNotNull(setting);
        Assert.assertEquals("value1", setting.get("key1"));
    }

    @Test(expected = cn.hutool.core.io.IORuntimeException.class)
    public void testGetSettingNotFound() {
        PropertiesUtil.getSetting("config/not-exists.properties");
    }

}
