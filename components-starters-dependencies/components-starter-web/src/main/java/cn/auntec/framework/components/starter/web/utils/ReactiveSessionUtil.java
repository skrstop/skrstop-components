package cn.auntec.framework.components.starter.web.utils;

import cn.auntec.framework.components.util.constant.CharSetEnum;
import cn.auntec.framework.components.util.constant.StringPoolConst;
import cn.auntec.framework.components.util.value.data.CollectionUtil;
import cn.auntec.framework.components.util.value.data.NumberUtil;
import cn.auntec.framework.components.util.value.data.ObjectUtil;
import cn.auntec.framework.components.util.value.data.StrUtil;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.net.URLEncoder;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author 蒋时华
 * @date 2021-01-19 13:44:56
 */
public class ReactiveSessionUtil {

    public static String getCookie(ServerHttpRequest request, String cookieName) {
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        for (Map.Entry<String, List<HttpCookie>> entry : cookies.entrySet()) {
            if (entry.getKey().equals(cookieName)) {
                List<HttpCookie> value = entry.getValue();
                if (CollectionUtil.isNotEmpty(value)) {
                    return value.get(0).getValue();
                }
            }
        }
        return null;
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
     * 从 header -> cookie -> parameter 中顺序获取相应的数据
     *
     * @param request
     * @return
     */
    public static String getValueStr(ServerHttpRequest request, String name) {
        String value;
        // header
        value = request.getHeaders().getFirst(name);
        if (StrUtil.isNotBlank(value)) {
            return ReactiveSessionUtil.decode(value);
        }
        // cookie
        value = ReactiveSessionUtil.getCookie(request, name);
        if (StrUtil.isNotBlank(value)) {
            return ReactiveSessionUtil.decode(value);
        }
        // parameter
        value = request.getQueryParams().getFirst(name);
        if (ObjectUtil.isNotNull(value)) {
            return ReactiveSessionUtil.decode(value);
        }
        return null;
    }

    public static Long getValueLong(ServerHttpRequest request, String name) {
        return NumberUtil.toLong(getValueStr(request, name));
    }

    public static Integer getValueInteger(ServerHttpRequest request, String name) {
        return NumberUtil.toInt(getValueStr(request, name));
    }

    public static Byte getValueByte(ServerHttpRequest request, String name) {
        return NumberUtil.toByte(getValueStr(request, name));
    }

    public static Short getValueShort(ServerHttpRequest request, String name) {
        return NumberUtil.toShort(getValueStr(request, name));
    }

    public static Float getValueFloat(ServerHttpRequest request, String name) {
        return NumberUtil.toFloat(getValueStr(request, name));
    }

    public static Double getValueDouble(ServerHttpRequest request, String name) {
        return NumberUtil.toDouble(getValueStr(request, name));
    }

    public static BigDecimal getValueBigDecimal(ServerHttpRequest request, String name) {
        return NumberUtil.toBigDecimal(getValueStr(request, name));
    }

}
