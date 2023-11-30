package com.zoe.framework.components.util.system.net;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * PortUtil class
 *
 * @author 蒋时华
 * @date 2019/8/26
 */
@UtilityClass
public class PortUtil {

    /**
     * 基于默认端口，默认范围(0, 65535), 自动寻找为使用的端口，先后序查找，后前序查找
     *
     * @param defaultPort default port to start
     * @return new socket with available port
     */
    public static int getServerSocketFromBasePort(int defaultPort) {
        return getServerSocketFromBasePort(defaultPort, 0, 65535);
    }

    /**
     * 基于默认端口，指定范围(minPort, 65maxPort535), 自动寻找为使用的端口，先后序查找，后前序查找
     *
     * @param defaultPort
     * @param minPort
     * @param maxPort
     * @return
     */
    public static int getServerSocketFromBasePort(int defaultPort, int minPort, int maxPort) {
        int portTmp = defaultPort;
        while (portTmp < maxPort) {
            if (!isPortUsed(portTmp)) {
                return portTmp;
            } else {
                portTmp++;
            }
        }
        portTmp = defaultPort--;
        while (portTmp > minPort) {
            if (!isPortUsed(portTmp)) {
                return portTmp;
            } else {
                portTmp--;
            }
        }
        throw new IllegalArgumentException("no available port.");
    }

    /**
     * 判断端口是否使用
     *
     * @param port
     * @return
     */
    public static boolean isPortUsed(int port) {
        boolean used = false;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            used = false;
        } catch (IOException e) {
            used = true;
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                }
            }
        }
        return used;
    }

}
