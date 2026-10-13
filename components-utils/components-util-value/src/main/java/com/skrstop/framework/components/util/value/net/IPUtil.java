package com.skrstop.framework.components.util.value.net;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * IpUtil class
 *
 * @author 蒋时华
 * @date 2019/8/30
 */
@Slf4j
@UtilityClass
public class IPUtil {

    private static final String ANYHOST = "0.0.0.0";

    private static volatile InetAddress LOCAL_ADDRESS = null;

    // ---------------------- valid ----------------------

    /**
     * 验证ipv4地址
     *
     * @param address
     * @return
     */
    private static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress()) {
            return false;
        }
        String name = address.getHostAddress();
        // isValidAddress 仅对 Inet4Address 调用，getHostAddress 必然是点分十进制，无需正则校验
        return !ANYHOST.equals(name);
    }

    /**
     * 验证ipv6地址
     *
     * @param address
     * @return
     */
    private static boolean isValidV6Address(Inet6Address address) {
        boolean preferIpv6 = Boolean.getBoolean("java.net.preferIPv6Addresses");
        if (!preferIpv6) {
            return false;
        }
        try {
            return address.isReachable(100);
        } catch (IOException e) {
            // ignore
        }
        return false;
    }

    /**
     * normalize the ipv6 Address, convert scope name to scope id.
     * <p>
     * e.g.
     * convert
     * fe80:0:0:0:894:aeec:f37d:23e1%en0
     * to
     * fe80:0:0:0:894:aeec:f37d:23e1%5
     * <p>
     * The %5 after ipv6 address is called scope id.
     * see java doc of {@link Inet6Address} for more details.
     *
     * @param address the input address
     * @return the normalized address, with scope id converted to int
     */
    private static InetAddress normalizeV6Address(Inet6Address address) throws UnknownHostException {
        String addr = address.getHostAddress();
        int i = addr.lastIndexOf('%');
        if (i > 0) {
            return InetAddress.getByName(addr.substring(0, i) + '%' + address.getScopeId());
        }
        return address;
    }

    // ---------------------- find ip ----------------------


    interface ThrowingSupplier<T> {
        T get() throws Throwable;
    }

    private static InetAddress getLocalAddress0() {
        return getLocalAddress0(InetAddress::getLocalHost, NetworkInterface::getNetworkInterfaces);
    }

    static <T> T supply(ThrowingSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    static InetAddress getLocalAddress0(ThrowingSupplier<InetAddress> localHostSupplier,
                                        ThrowingSupplier<Enumeration<NetworkInterface>> interfacesSupplier) {
        InetAddress localAddress = null;
        try {
            localAddress = supply(localHostSupplier);
            InetAddress found = pickValid(java.util.Collections.enumeration(java.util.Collections.singletonList(localAddress)));
            if (found != null) {
                return found;
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
        try {
            Enumeration<NetworkInterface> interfaces = supply(interfacesSupplier);
            if (null == interfaces) {
                return localAddress;
            }
            while (interfaces.hasMoreElements()) {
                try {
                    InetAddress found = pickValid(interfaces.nextElement().getInetAddresses());
                    if (found != null) {
                        return found;
                    }
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                }
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
        return localAddress;
    }

    static InetAddress pickValid(Enumeration<InetAddress> addresses) throws UnknownHostException {
        while (addresses.hasMoreElements()) {
            InetAddress address = addresses.nextElement();
            if (address instanceof Inet6Address) {
                Inet6Address v6Address = (Inet6Address) address;
                if (isValidV6Address(v6Address)) {
                    return normalizeV6Address(v6Address);
                }
            } else if (isValidAddress(address)) {
                return address;
            }
        }
        return null;
    }


    // ---------------------- tool ----------------------

    /**
     * Find first valid IP from local network card
     *
     * @return first valid local IP
     */
    public static InetAddress getLocalAddress() {
        if (LOCAL_ADDRESS != null) {
            return LOCAL_ADDRESS;
        }
        InetAddress localAddress = getLocalAddress0();
        LOCAL_ADDRESS = localAddress;
        return localAddress;
    }

    /**
     * get ip address
     *
     * @return String
     */
    public static String getIp() {
        return getLocalAddress().getHostAddress();
    }

    /**
     * get ip:port
     *
     * @param port
     * @return String
     */
    public static String getIpPort(int port) {
        String ip = getIp();
        return getIpPort(ip, port);
    }

    /**
     * 获取ip:port 格式字符串
     *
     * @param ip
     * @param port
     * @return
     */
    public static String getIpPort(String ip, int port) {
        if (ip == null) {
            return null;
        }
        return ip.concat(":").concat(String.valueOf(port));
    }

    /**
     * 解析ip:port 格式字符串
     *
     * @param address
     * @return
     */
    public static Object[] parseIpPort(String address) {
        String[] array = address.split(":");

        String host = array[0];
        int port = Integer.parseInt(array[1]);

        return new Object[]{host, port};
    }

}
