package com.skrstop.framework.components.util.setting.properties;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.setting.Setting;
import lombok.experimental.UtilityClass;

/**
 * PropertiesUtil class
 *
 * @author 蒋时华
 * @date 2019/6/17
 */
@UtilityClass
public class PropertiesUtil {

    /**
     * setting 接收 properties属性文件
     *
     * @param classPath
     * @return
     */
    public static Setting getSetting(String classPath) {
        ClassPathResource classPathResource = new ClassPathResource(classPath, PropertiesUtil.class.getClassLoader());
        Setting setting = new Setting();
        setting.init(classPathResource, Setting.DEFAULT_CHARSET, false);
        return setting;
    }

}
