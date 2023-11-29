package com.zoe.framework.components.util.value.data;


import cn.hutool.core.util.StrUtil;

import java.util.Optional;

/**
 * @description: 枚举工具类
 * @author: 蒋时华
 * @date: 2018/10/17
 */
public class EnumUtil extends cn.hutool.core.util.EnumUtil {

    /**
     * 通过枚举名获取枚举项
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends Enum<T>> Optional<T> getInstance(String name, Class<T> clazz) {
        if (StrUtil.isNotEmpty(name) || clazz != null) {
            T[] enumConstants = clazz.getEnumConstants();
            for (int i = 0; i < enumConstants.length; i++) {
                if (enumConstants[i].name().equals(name)) {
                    return Optional.ofNullable(enumConstants[i]);
                }
            }
        }
        return Optional.empty();
    }

}
