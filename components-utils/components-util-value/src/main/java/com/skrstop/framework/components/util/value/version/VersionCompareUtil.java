package com.skrstop.framework.components.util.value.version;

import cn.hutool.core.comparator.VersionComparator;
import lombok.experimental.UtilityClass;

/**
 * @author 蒋时华
 * @date 2020-06-17 17:44:25
 */
@UtilityClass
public class VersionCompareUtil extends VersionComparator {

    // 大于
    public final static byte GTR = 1;
    // 小于
    public final static byte LSS = -1;
    // 等于
    public final static byte EQU = 0;

    /**
     * 对比字符串版本号的大小，返回1则v1大于v2，返回-1则v1小于v2，返回0则v1等于v2
     *
     * @param {string} version1 版本号1
     * @param {string} version2 要进行比较的版本号2
     */
    public static int compareVersion(String version1, String version2) {
        //通过\\将.进行转义
        String[] s1 = version1.split("\\.");
        String[] s2 = version2.split("\\.");
        int len1 = s1.length;
        int len2 = s2.length;
        int i, j;
        for (i = 0, j = 0; i < len1 && j < len2; i++, j++) {
            if (Integer.parseInt(s1[i]) > Integer.parseInt(s2[j])) {
                return GTR;
            } else if (Integer.parseInt(s1[i]) < Integer.parseInt(s2[j])) {
                return LSS;
            }
        }
        while (i < len1) {
            if (Integer.parseInt(s1[i]) != 0) {
                return GTR;
            }
            i++;
        }
        while (j < len2) {
            if (Integer.parseInt(s2[j]) != 0) {
                return LSS;
            }
            j++;
        }
        return EQU;
    }

    public static boolean compareVersion(String version1, String version2, int result) {
        int i = VersionCompareUtil.compareVersion(version1, version2);
        return i == result;
    }

}
