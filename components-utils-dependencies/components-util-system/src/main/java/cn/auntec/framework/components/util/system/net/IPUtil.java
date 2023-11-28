package cn.auntec.framework.components.util.system.net;


import cn.auntec.framework.components.util.value.data.StrUtil;
import cn.auntec.framework.components.util.value.validate.Assert;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;

/**
 * @author 蒋时华
 */
public class IPUtil {

    /*** 当前机器内网IP */
    private static String INTRANET_IP = null;
    /*** 前机器外网IP */
    private static String INTERNET_IP = null;

    /**
     * 获得当前机器内网IP
     *
     * @return 内网IP
     */
    public static String getIntranetIp() {
        if (StrUtil.isNotBlank(INTRANET_IP)) {
            return INTRANET_IP;
        }
        Enumeration<NetworkInterface> networks = null;
        try {
            networks = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new RuntimeException("获取当前机器内网IP失败");
        }
        InetAddress ip = null;
        Enumeration<InetAddress> addrs;
        while (networks.hasMoreElements()) {
            addrs = networks.nextElement().getInetAddresses();
            while (addrs.hasMoreElements()) {
                ip = addrs.nextElement();
                if (ip != null
                        && ip instanceof Inet4Address
                        && ip.isSiteLocalAddress()) {
                    INTRANET_IP = ip.getHostAddress();
                    return INTRANET_IP;
                }
            }
        }
        // 如果没有外网IP，就返回内网IP
        throw new RuntimeException("获取当前机器内网IP失败");

    }

    /**
     * 获得当前机器外网IP
     *
     * @return 外网IP
     */
    public static String getInternetIp() {
        if (StrUtil.isNotBlank(INTERNET_IP)) {
            return INTERNET_IP;
        }
        BufferedReader sc = null;
        try {
            URL url = new URL("http://bot.whatismyipaddress.com");
            sc = new BufferedReader(new InputStreamReader(url.openStream()));
            INTERNET_IP = sc.readLine().trim();
            return INTERNET_IP;
        } catch (Exception e) {
            throw new RuntimeException("获取当前机器外网IP失败");
        } finally {
            if (sc != null) {
                try {
                    sc.close();
                } catch (Exception e) {
                    throw new RuntimeException("获取当前机器外网IP失败");
                }
            }
        }
    }

    /**
     * 获取当前请求的Ip地址
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        Assert.notNull(request, "HttpServletRequest must not null");
        String XFor = request.getHeader("X-Forwarded-For");
        if (StrUtil.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if (index != -1) {
                return XFor.substring(0, index);
            } else {
                return XFor;
            }
        }
        XFor = request.getHeader("X-Real-IP");
        if (StrUtil.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            return XFor;
        }
        if (StrUtil.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StrUtil.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StrUtil.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StrUtil.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }

    /**
     * 获取当前请求的Ip地址
     *
     * @param request
     * @return
     */
    public static String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String XFor = headers.getFirst("X-Forwarded-For");
        if (StrUtil.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            int index = XFor.indexOf(",");
            return index != -1 ? XFor.substring(0, index) : XFor;
        } else {
            XFor = headers.getFirst("X-Real-IP");
            if (StrUtil.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
                return XFor;
            } else {
                if (StrUtil.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
                    XFor = headers.getFirst("Proxy-Client-IP");
                }
                if (StrUtil.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
                    XFor = headers.getFirst("WL-Proxy-Client-IP");
                }
                if (StrUtil.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
                    XFor = headers.getFirst("HTTP_CLIENT_IP");
                }
                if (StrUtil.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
                    XFor = headers.getFirst("HTTP_X_FORWARDED_FOR");
                }

                if (StrUtil.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
                    XFor = request.getRemoteAddress().getAddress().getHostAddress();
                }
                return XFor;
            }
        }
    }
}
