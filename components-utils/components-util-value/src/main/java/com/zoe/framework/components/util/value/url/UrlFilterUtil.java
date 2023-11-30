package com.zoe.framework.components.util.value.url;

import com.zoe.framework.components.util.value.data.CollectionUtil;
import com.zoe.framework.components.util.value.data.StrUtil;
import lombok.experimental.UtilityClass;

import java.util.Collection;

/**
 * @author 蒋时华
 * @date 2020-10-21 15:35:57
 */
@UtilityClass
public class UrlFilterUtil {

    private final static String PATTERN = "**";

    /**
     * @param url        配置url
     * @param targetPath 目标url path
     * @return
     */
    public static boolean valid(String url, String targetPath) {
        String temp;
        if (PATTERN.equals(url)) {
            // 全部放开
            return true;
        } else if (StrUtil.startWith(url, PATTERN) && !StrUtil.endWith(url, PATTERN)) {
            // 模糊匹配  **xxx
            temp = StrUtil.removePrefix(url, PATTERN);
            if (StrUtil.endWith(targetPath, temp)) {
                return true;
            }
        } else if (!StrUtil.startWith(url, PATTERN) && StrUtil.endWith(url, PATTERN)) {
            // 模糊匹配  xxx**
            temp = StrUtil.removeSuffix(url, PATTERN);
            if (StrUtil.startWith(targetPath, temp)) {
                return true;
            }
        } else if (StrUtil.startWith(url, PATTERN) && StrUtil.endWith(url, PATTERN)) {
            // 模糊匹配  **xxx**
            temp = StrUtil.removePrefix(url, PATTERN);
            temp = StrUtil.removeSuffix(temp, PATTERN);
            if (StrUtil.contains(targetPath, temp)) {
                return true;
            }
        } else if (url.equals(targetPath)) {
            // 相等
            return true;
        }
        return false;
    }

    /**
     * 校验url过滤器
     *
     * @param urlList    配置url列表
     * @param targetPath 目标url path
     * @return
     */
    public static boolean valid(Collection<String> urlList, String targetPath) {
        if (CollectionUtil.isEmpty(urlList) || StrUtil.isEmpty(targetPath)) {
            return false;
        }
        for (String url : urlList) {
            boolean valid = valid(url, targetPath);
            if (valid) {
                return true;
            }
        }
        return false;
    }

}
