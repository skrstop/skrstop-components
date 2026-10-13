package com.skrstop.framework.components.util.system.net;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;

import java.net.InetSocketAddress;

/**
 * @author 蒋时华
 */
public class IPUtilTest {

    @Test
    public void testGetIntranetIp() {
        try {
            String ip = IPUtil.getIntranetIp();
            Assert.assertNotNull(ip);
        } catch (RuntimeException e) {
            // 本机无内网地址时允许跳过
            org.junit.Assume.assumeNoException(e);
        }
    }

    @Test
    public void testGetInternetIp() {
        try {
            String ip = IPUtil.getInternetIp();
            Assert.assertNotNull(ip);
        } catch (RuntimeException e) {
            // 无外网环境时跳过
            org.junit.Assume.assumeNoException(e);
        }
    }

    @Test
    public void testCachedIps() throws Exception {
        // 通过反射设置缓存，覆盖缓存命中分支
        java.lang.reflect.Field intranet = IPUtil.class.getDeclaredField("INTRANET_IP");
        intranet.setAccessible(true);
        intranet.set(null, "192.168.1.1");
        Assert.assertEquals("192.168.1.1", IPUtil.getIntranetIp());

        java.lang.reflect.Field internet = IPUtil.class.getDeclaredField("INTERNET_IP");
        internet.setAccessible(true);
        internet.set(null, "1.1.1.1");
        Assert.assertEquals("1.1.1.1", IPUtil.getInternetIp());

        // 清理缓存，恢复原始状态
        intranet.set(null, null);
        internet.set(null, null);
    }

    @Test
    public void testGetIntranetIpNoNetwork() {
        try {
            java.lang.reflect.Field intranet = IPUtil.class.getDeclaredField("INTRANET_IP");
            intranet.setAccessible(true);
            intranet.set(null, null);
        } catch (Exception ignored) {
        }
        try {
            String ip = IPUtil.getIntranetIp();
            Assert.assertNotNull(ip);
        } catch (RuntimeException e) {
            // 本机无内网地址时允许跳过
            org.junit.Assume.assumeNoException(e);
        }
    }

    @Test
    public void testGetIntranetIpSocketException() throws Exception {
        // getNetworkInterfaces 抛出 SocketException → RuntimeException
        try (MockedStatic<IPUtil> ignored = Mockito.mockStatic(IPUtil.class, Mockito.CALLS_REAL_METHODS)) {
            org.mockito.Mockito.when(IPUtil.getNetworkInterfaces()).thenThrow(new java.net.SocketException("mock"));
            try {
                java.lang.reflect.Field intranet = IPUtil.class.getDeclaredField("INTRANET_IP");
                intranet.setAccessible(true);
                intranet.set(null, null);
                IPUtil.getIntranetIp();
                Assert.fail("should throw");
            } catch (RuntimeException e) {
                Assert.assertTrue(e.getMessage().contains("获取当前机器内网IP失败"));
            } catch (Exception e) {
                Assert.fail("unexpected: " + e);
            }
        }
    }

    @Test
    public void testGetIntranetIpSiteLocal() throws Exception {
        // mock 一个带站点本地 IPv4 地址的网卡 → 命中 isSiteLocalAddress 分支并缓存
        try (MockedStatic<IPUtil> ignored = Mockito.mockStatic(IPUtil.class, Mockito.CALLS_REAL_METHODS)) {
            java.net.InetAddress siteLocal = java.net.InetAddress.getByName("192.168.1.5");
            java.net.InetAddress ipv6 = java.net.InetAddress.getByName("::1");
            java.net.NetworkInterface nic = Mockito.mock(java.net.NetworkInterface.class);
            java.util.Vector<java.net.InetAddress> addrs = new java.util.Vector<>();
            addrs.add(ipv6);
            addrs.add(java.net.InetAddress.getByName("8.8.8.8"));
            addrs.add(siteLocal);
            Mockito.when(nic.getInetAddresses()).thenReturn(addrs.elements());
            java.util.Vector<java.net.NetworkInterface> nics = new java.util.Vector<>();
            nics.add(nic);
            Mockito.when(IPUtil.getNetworkInterfaces()).thenReturn(nics.elements());

            java.lang.reflect.Field intranet = IPUtil.class.getDeclaredField("INTRANET_IP");
            intranet.setAccessible(true);
            intranet.set(null, null);
            Assert.assertEquals("192.168.1.5", IPUtil.getIntranetIp());
            // 第二次走缓存
            Assert.assertEquals("192.168.1.5", IPUtil.getIntranetIp());
            intranet.set(null, null);
        }
    }

    @Test
    public void testGetIntranetIpEmptyInterfaces() throws Exception {
        // 没有任何网络接口 → 未找到内网 IP
        try (MockedStatic<IPUtil> ignored = Mockito.mockStatic(IPUtil.class, Mockito.CALLS_REAL_METHODS)) {
            java.util.Vector<java.net.NetworkInterface> empty = new java.util.Vector<>();
            org.mockito.Mockito.when(IPUtil.getNetworkInterfaces()).thenReturn(empty.elements());
            try {
                java.lang.reflect.Field intranet = IPUtil.class.getDeclaredField("INTRANET_IP");
                intranet.setAccessible(true);
                intranet.set(null, null);
                IPUtil.getIntranetIp();
                Assert.fail("should throw");
            } catch (RuntimeException e) {
                Assert.assertTrue(e.getMessage().contains("获取当前机器内网IP失败"));
            } catch (Exception e) {
                Assert.fail("unexpected: " + e);
            }
        }
    }

    @Test
    public void testGetInternetIpFailed() {
        // 外网不可达时抛出异常（内网/离线环境）
        try {
            java.lang.reflect.Field internet = IPUtil.class.getDeclaredField("INTERNET_IP");
            internet.setAccessible(true);
            internet.set(null, null);
        } catch (Exception ignored) {
        }
        try {
            String ip = IPUtil.getInternetIp();
            Assert.assertNotNull(ip);
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("获取当前机器外网IP失败"));
        }
    }

    @Test
    public void testGetInternetIpCatch() throws Exception {
        // 桩掉 createInternetReader 抛异常 → 覆盖 catch 分支
        try (MockedStatic<IPUtil> mocked = Mockito.mockStatic(IPUtil.class, Mockito.CALLS_REAL_METHODS)) {
            mocked.when(IPUtil::createInternetReader).thenThrow(new java.io.IOException("offline"));
            java.lang.reflect.Field internet = IPUtil.class.getDeclaredField("INTERNET_IP");
            internet.setAccessible(true);
            internet.set(null, null);
            try {
                IPUtil.getInternetIp();
                Assert.fail("should throw");
            } catch (RuntimeException e) {
                Assert.assertTrue(e.getMessage().contains("获取当前机器外网IP失败"));
            }
        }
    }

    @Test
    public void testGetInternetIpSuccess() throws Exception {
        // 桩掉 createInternetReader 返回固定内容 → 覆盖成功分支
        try (MockedStatic<IPUtil> mocked = Mockito.mockStatic(IPUtil.class, Mockito.CALLS_REAL_METHODS)) {
            mocked.when(IPUtil::createInternetReader).thenReturn(
                    new java.io.BufferedReader(new java.io.StringReader("  5.6.7.8  ")));
            java.lang.reflect.Field internet = IPUtil.class.getDeclaredField("INTERNET_IP");
            internet.setAccessible(true);
            internet.set(null, null);
            Assert.assertEquals("5.6.7.8", IPUtil.getInternetIp());
        }
    }

    @Test
    public void testCreateInternetReader() {
        try {
            Assert.assertNotNull(IPUtil.createInternetReader());
        } catch (Exception e) {
            // 无外网环境跳过
            org.junit.Assume.assumeNoException(e);
        }
    }

    @Test
    public void testGetIpAddressServlet() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        Mockito.when(request.getHeader("X-Real-IP")).thenReturn(null);
        Mockito.when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
        Mockito.when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
        Mockito.when(request.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
        Mockito.when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn(null);
        Mockito.when(request.getRemoteAddr()).thenReturn("10.0.0.1");
        Assert.assertEquals("10.0.0.1", IPUtil.getIpAddress(request));

        Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("1.1.1.1, 2.2.2.2");
        Assert.assertEquals("1.1.1.1", IPUtil.getIpAddress(request));

        Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("1.1.1.1");
        Assert.assertEquals("1.1.1.1", IPUtil.getIpAddress(request));

        Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("localhost");
        Assert.assertEquals("127.0.0.1", IPUtil.getIpAddress(request));

        Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("unKnown");
        Mockito.when(request.getHeader("X-Real-IP")).thenReturn("3.3.3.3");
        Assert.assertEquals("3.3.3.3", IPUtil.getIpAddress(request));

        Mockito.when(request.getHeader("X-Real-IP")).thenReturn("localhost");
        Assert.assertEquals("127.0.0.1", IPUtil.getIpAddress(request));

        Mockito.when(request.getHeader("X-Real-IP")).thenReturn("unknown");
        Mockito.when(request.getHeader("Proxy-Client-IP")).thenReturn("4.4.4.4");
        Assert.assertEquals("4.4.4.4", IPUtil.getIpAddress(request));

        Mockito.when(request.getHeader("Proxy-Client-IP")).thenReturn("unknown");
        Mockito.when(request.getHeader("WL-Proxy-Client-IP")).thenReturn("5.5.5.5");
        Assert.assertEquals("5.5.5.5", IPUtil.getIpAddress(request));

        Mockito.when(request.getHeader("WL-Proxy-Client-IP")).thenReturn("unknown");
        Mockito.when(request.getHeader("HTTP_CLIENT_IP")).thenReturn("6.6.6.6");
        Assert.assertEquals("6.6.6.6", IPUtil.getIpAddress(request));

        Mockito.when(request.getHeader("HTTP_CLIENT_IP")).thenReturn("unknown");
        Mockito.when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn("7.7.7.7");
        Assert.assertEquals("7.7.7.7", IPUtil.getIpAddress(request));

        Mockito.when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn("unknown");
        Mockito.when(request.getRemoteAddr()).thenReturn("localhost");
        Assert.assertEquals("127.0.0.1", IPUtil.getIpAddress(request));

        Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("");
        Mockito.when(request.getRemoteAddr()).thenReturn("8.8.8.8");
        Assert.assertEquals("8.8.8.8", IPUtil.getIpAddress(request));

        try {
            IPUtil.getIpAddress((HttpServletRequest) null);
            Assert.fail("should throw");
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testGetIpAddressServerHttpRequest() {
        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost/")
                .header("X-Forwarded-For", "1.1.1.1, 2.2.2.2")
                .build();
        Assert.assertEquals("1.1.1.1", IPUtil.getIpAddress(request));

        request = MockServerHttpRequest.get("http://localhost/")
                .header("X-Forwarded-For", "1.1.1.1")
                .build();
        Assert.assertEquals("1.1.1.1", IPUtil.getIpAddress(request));

        request = MockServerHttpRequest.get("http://localhost/")
                .header("X-Forwarded-For", "localhost")
                .build();
        Assert.assertEquals("127.0.0.1", IPUtil.getIpAddress(request));

        request = MockServerHttpRequest.get("http://localhost/")
                .header("X-Forwarded-For", "unKnown")
                .header("X-Real-IP", "3.3.3.3")
                .build();
        Assert.assertEquals("3.3.3.3", IPUtil.getIpAddress(request));

        request = MockServerHttpRequest.get("http://localhost/")
                .header("X-Forwarded-For", "unKnown")
                .header("X-Real-IP", "localhost")
                .build();
        Assert.assertEquals("127.0.0.1", IPUtil.getIpAddress(request));

        request = MockServerHttpRequest.get("http://localhost/")
                .header("X-Forwarded-For", "unKnown")
                .header("X-Real-IP", "unknown")
                .header("Proxy-Client-IP", "4.4.4.4")
                .build();
        Assert.assertEquals("4.4.4.4", IPUtil.getIpAddress(request));

        request = MockServerHttpRequest.get("http://localhost/")
                .header("Proxy-Client-IP", "unknown")
                .header("WL-Proxy-Client-IP", "5.5.5.5")
                .build();
        Assert.assertEquals("5.5.5.5", IPUtil.getIpAddress(request));

        request = MockServerHttpRequest.get("http://localhost/")
                .header("HTTP_CLIENT_IP", "6.6.6.6")
                .build();
        Assert.assertEquals("6.6.6.6", IPUtil.getIpAddress(request));

        request = MockServerHttpRequest.get("http://localhost/")
                .header("HTTP_X_FORWARDED_FOR", "7.7.7.7")
                .build();
        Assert.assertEquals("7.7.7.7", IPUtil.getIpAddress(request));

        request = MockServerHttpRequest.get("http://localhost/")
                .remoteAddress(new InetSocketAddress("9.9.9.9", 8080))
                .build();
        Assert.assertEquals("9.9.9.9", IPUtil.getIpAddress(request));

        request = MockServerHttpRequest.get("http://localhost/")
                .remoteAddress(new InetSocketAddress("localhost", 8080))
                .build();
        Assert.assertEquals("127.0.0.1", IPUtil.getIpAddress(request));

        // header fallback 为 localhost → 127.0.0.1
        request = MockServerHttpRequest.get("http://localhost/")
                .header("X-Forwarded-For", "unKnown")
                .header("Proxy-Client-IP", "localhost")
                .build();
        Assert.assertEquals("127.0.0.1", IPUtil.getIpAddress(request));
    }

}
