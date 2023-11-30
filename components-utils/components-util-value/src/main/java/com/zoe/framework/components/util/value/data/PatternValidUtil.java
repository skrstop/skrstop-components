package com.zoe.framework.components.util.value.data;


import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.zoe.framework.components.util.constant.RegularExpressionConst;
import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 正则工具类
 * @author: 蒋时华
 * @date: 2018/5/17
 */
@UtilityClass
public class PatternValidUtil extends ReUtil {


    /**
     * 正则匹配方法,不区分大小写
     *
     * @param content 需匹配的字符串
     * @param regex   正则匹配规则
     * @return
     */
    public static boolean isMatchCaseInsensitive(String regex, CharSequence content) {

        if (content == null) {
            // 提供null的字符串为不匹配
            return false;
        }

        if (StrUtil.isEmpty(regex)) {
            // 正则不存在则为全匹配
            return true;
        }

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        return matcher.matches();
    }

    /**
     * 正则匹配手机号
     *
     * @param content
     * @return
     */
    public static boolean matchPhone(String content) {
        return isMatchCaseInsensitive(RegularExpressionConst.PHONE, content);
    }

    /**
     * 正则匹配邮箱
     *
     * @param content
     * @return
     */
    public static boolean matchEmail(String content) {

        return isMatchCaseInsensitive(RegularExpressionConst.EMAIL, content);
    }

    /**
     * 正则匹配车牌号
     *
     * @param content
     * @return
     */
    public static boolean matchVehicle(String content) {

        return isMatchCaseInsensitive(RegularExpressionConst.VCL_NO, content);
    }

    /**
     * 匹配日期时间   yyyy-MM-dd hh:mm:ss
     *
     * @param content
     * @return
     */
    public static boolean matchDateTime(String content) {

        return isMatchCaseInsensitive(RegularExpressionConst.DATE_TIME, content);
    }

    /**
     * 匹配日期   yyyy-MM-dd
     *
     * @param content
     * @return
     */
    public static boolean matchDate(String content) {

        return isMatchCaseInsensitive(RegularExpressionConst.DATE, content);
    }

    /**
     * 匹配时间  hh:mm:ss
     *
     * @param content
     * @return
     */
    public static boolean matchTime(String content) {

        return isMatchCaseInsensitive(RegularExpressionConst.TIME, content);
    }

    /**
     * 匹配http url
     *
     * @param content
     * @return
     */
    public static boolean matchHttpUrl(String content) {
        return isMatchCaseInsensitive(RegularExpressionConst.HTTP_URL, content);
    }

    /**
     * 匹配ftp url
     *
     * @param content
     * @return
     */
    public static boolean matchFtpUrl(String content) {
        return isMatchCaseInsensitive(RegularExpressionConst.FTP_URL, content);
    }

}
