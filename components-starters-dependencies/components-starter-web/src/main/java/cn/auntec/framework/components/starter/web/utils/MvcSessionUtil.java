package cn.auntec.framework.components.starter.web.utils;

import cn.auntec.framework.components.util.constant.CharSetEnum;
import cn.auntec.framework.components.util.constant.StringPoolConst;
import cn.auntec.framework.components.util.value.data.NumberUtil;
import cn.auntec.framework.components.util.value.data.ObjectUtil;
import cn.auntec.framework.components.util.value.data.StrUtil;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.net.URLEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.Charset;

/**
 * @author 蒋时华
 * @date 2021-01-19 13:29:07
 */
public class MvcSessionUtil {

    public static String getCookie(String cookieName) {
        return CookieUtil.getValue(cookieName);
    }

    public static String encode(String value) {
        if (StrUtil.isBlank(value)) {
            return StringPoolConst.EMPTY;
        }
        return URLEncoder.createDefault().encode(value, Charset.forName(CharSetEnum.UTF8.getCharSet()));
    }

    public static String decode(String encodeStr) {
        return URLDecoder.decode(encodeStr, Charset.forName(CharSetEnum.UTF8.getCharSet()));
    }

    /**
     * 从 attribute -> header -> cookie -> parameter 中顺序获取相应的数据
     *
     * @param name
     * @return
     */
    public static String getValueStr(String name) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtil.isNull(servletRequestAttributes) || ObjectUtil.isNull(servletRequestAttributes.getRequest())) {
            return null;
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String value;
        if (ObjectUtil.isNull(request)) {
            return null;
        }
        // attribute
        Object attribute = request.getAttribute(name);
        if (ObjectUtil.isNotNull(attribute)) {
            return attribute.toString();
        }
        // header
        value = request.getHeader(name);
        if (StrUtil.isNotBlank(value)) {
            return MvcSessionUtil.decode(value);
        }
        // cookie
        value = MvcSessionUtil.getCookie(name);
        if (StrUtil.isNotBlank(value)) {
            return MvcSessionUtil.decode(value);
        }
        // parameter
        value = request.getParameter(name);
        if (StrUtil.isNotBlank(value)) {
            return MvcSessionUtil.decode(value);
        }
        return null;
    }

    public static Long getValueLong(String name) {
        return NumberUtil.toLong(getValueStr(name));
    }

    public static Integer getValueInteger(String name) {
        return NumberUtil.toInt(getValueStr(name));
    }

    public static Byte getValueByte(String name) {
        return NumberUtil.toByte(getValueStr(name));
    }

    public static Short getValueShort(String name) {
        return NumberUtil.toShort(getValueStr(name));
    }

    public static Float getValueFloat(String name) {
        return NumberUtil.toFloat(getValueStr(name));
    }

    public static Double getValueDouble(String name) {
        return NumberUtil.toDouble(getValueStr(name));
    }

    public static BigDecimal getValueBigDecimal(String name) {
        return NumberUtil.toBigDecimal(getValueStr(name));
    }

}
