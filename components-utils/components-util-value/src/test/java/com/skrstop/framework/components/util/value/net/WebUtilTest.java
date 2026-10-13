package com.skrstop.framework.components.util.value.net;

import com.sun.net.httpserver.HttpServer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.InetSocketAddress;

/**
 * @author 蒋时华
 */
public class WebUtilTest {

    private static HttpServer server;

    @BeforeClass
    public static void setUp() throws Exception {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext(
                "/no",
                exchange -> {
                    exchange.sendResponseHeaders(404, -1);
                    exchange.close();
                });
        server.createContext(
                "/ok",
                exchange -> {
                    exchange.sendResponseHeaders(200, 2);
                    exchange.getResponseBody().write("ok".getBytes());
                    exchange.close();
                });
        server.start();
    }

    @AfterClass
    public static void tearDown() {
        server.stop(0);
    }

    @Test
    public void testGetLocalIP() throws Exception {
        Assert.assertNotNull(WebUtil.getLocalIP());
    }

    @Test
    public void testIpCanPing() {
        Assert.assertFalse(WebUtil.ipCanPing("", 100));
        Assert.assertFalse(WebUtil.ipCanPing(null, 100));
        // localhost 可达
        Assert.assertTrue(WebUtil.ipCanPing("127.0.0.1", 500));
        // 不可达地址
        Assert.assertFalse(WebUtil.ipCanPing("10.255.255.1", 100));
    }

    @Test
    public void testAddrCanConnect() {
        String url = "http://127.0.0.1:" + server.getAddress().getPort() + "/ok";
        Assert.assertTrue(WebUtil.addrCanConnect(url, 1000));
        Assert.assertFalse(WebUtil.addrCanConnect("", 100));
        Assert.assertFalse(WebUtil.addrCanConnect(null, 100));
        Assert.assertFalse(WebUtil.addrCanConnect("http://127.0.0.1:1/", 300));
        // 非 200 响应 → false
        Assert.assertFalse(WebUtil.addrCanConnect("http://127.0.0.1:" + server.getAddress().getPort() + "/no", 1000));
        // 未知主机 → UnknownHostException → catch 日志后返回 false
        Assert.assertFalse(WebUtil.ipCanPing("no-such-host.invalid", 100));
    }

}
