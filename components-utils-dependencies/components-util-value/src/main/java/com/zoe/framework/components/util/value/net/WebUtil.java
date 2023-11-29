package com.zoe.framework.components.util.value.net;

import com.zoe.framework.components.util.value.data.StrUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * web相关工具类
 *
 * @author 蒋时华
 * @date 2019-04-03
 */
@UtilityClass
@Slf4j
public class WebUtil {

    /**
     * 获取本机ip地址
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getLocalIP() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }


    /**
     * ip地址是否可以ping通
     *
     * @param ipAddress ip地址
     * @param timeout   超时，单位毫秒
     * @return true | false
     */
    public static boolean ipCanPing(String ipAddress, int timeout) {
        if (StrUtil.isEmpty(ipAddress)) {
            return false;
        }
        boolean canPing = false;
        try {
            canPing = InetAddress.getByName(ipAddress).isReachable(timeout);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return canPing;
    }

    /**
     * 链接是否可以被请求
     *
     * @param url     地址
     * @param timeout 超时，单位毫秒
     * @return true | false
     */
    public static boolean addrCanConnect(String url, int timeout) {
        if (StrUtil.isEmpty(url)) {
            return false;
        }

        boolean canConnect = false;
        try {
            URL urlConnection = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setUseCaches(false);
            connection.setConnectTimeout(timeout);
            int state = connection.getResponseCode();
            canConnect = (state == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return canConnect;
    }

}
