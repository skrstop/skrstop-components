package com.skrstop.framework.components.util.system.net;

import org.junit.Assert;
import org.junit.Test;

import java.net.ServerSocket;

/**
 * @author 蒋时华
 */
public class PortUtilTest {

    @Test
    public void testGetServerSocketFromBasePort() throws Exception {
        int port = PortUtil.getServerSocketFromBasePort(20000);
        Assert.assertTrue(port >= 20000 && port <= 65535);

        int port2 = PortUtil.getServerSocketFromBasePort(20000, 19000, 21000);
        Assert.assertTrue(port2 >= 19000 && port2 <= 21000);
    }

    @Test
    public void testIsPortUsed() throws Exception {
        int freePort = PortUtil.getServerSocketFromBasePort(22000);
        Assert.assertFalse(PortUtil.isPortUsed(freePort));

        try (ServerSocket socket = new ServerSocket(freePort)) {
            Assert.assertTrue(PortUtil.isPortUsed(freePort));
        }
        Assert.assertFalse(PortUtil.isPortUsed(freePort));
    }

    @Test
    public void testNoAvailablePort() {
        try (ServerSocket s1 = new ServerSocket(23001);
             ServerSocket s2 = new ServerSocket(23002)) {
            try {
                PortUtil.getServerSocketFromBasePort(23001, 23001, 23002);
                Assert.fail("should throw");
            } catch (IllegalArgumentException e) {
                Assert.assertTrue(e.getMessage().contains("no available port"));
            }
        } catch (Exception e) {
            Assert.fail("bind failed: " + e.getMessage());
        }
    }

    @Test
    public void testBackwardSearch() throws Exception {
        // 默认端口被占用，向前查找
        try (ServerSocket s = new ServerSocket(23100)) {
            int port = PortUtil.getServerSocketFromBasePort(23100, 22000, 23101);
            Assert.assertTrue(port != 23100);
        }
    }

}
