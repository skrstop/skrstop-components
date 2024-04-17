package com.skrstop.framework.components.starter.web.utils;

import com.skrstop.framework.components.util.constant.StringPoolConst;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 蒋时华
 * @date 2020-06-28 16:07:29
 */
public abstract class CookieUtil {

    public static Map<String, String> getCookies() {
        HttpServletRequest request = WebUtil.getRequest();
        Map<String, String> cookieMap = new LinkedHashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }
        return cookieMap;
    }


    public static Cookie getCookie(String cookieName) {
        HttpServletRequest request = WebUtil.getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }


    public static String getValue(String name) {
        HttpServletRequest request = WebUtil.getRequest();
        Cookie cookie = CookieUtil.getCookie(name);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    public static void removeCookie(String name) {
        HttpServletResponse response = WebUtil.getResponse();
        Cookie cookie = new Cookie(name, null);
        cookie.setPath(StringPoolConst.SLASH);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static void removeCookie(String name, String cookieRootPath) {
        HttpServletResponse response = WebUtil.getResponse();
        Cookie cookie = new Cookie(name, null);
        cookie.setPath(cookieRootPath);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static void setCookie(String cookieRootPath, String name, String value, int maxAge) {
        HttpServletResponse response = WebUtil.getResponse();
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(cookieRootPath);
        if (maxAge > 0) {
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }

    public static void setCookie(String cookieRootPath, String name, String value, int maxAge, String domain) {
        HttpServletResponse response = WebUtil.getResponse();
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(cookieRootPath);
        if (maxAge > 0) {
            cookie.setMaxAge(maxAge);
        }
        cookie.setDomain(domain);
        response.addCookie(cookie);
    }

}
