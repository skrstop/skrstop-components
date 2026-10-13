package com.skrstop.framework.components.util.value.net;

import org.junit.Assert;
import org.junit.Test;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @author 蒋时华
 */
public class IPUtilTest {

    @Test
    public void testGetLocalAddress() {
        Assert.assertNotNull(IPUtil.getLocalAddress());
        // 缓存路径
        Assert.assertSame(IPUtil.getLocalAddress(), IPUtil.getLocalAddress());
        Assert.assertNotNull(IPUtil.getIp());
        Assert.assertNotNull(IPUtil.getIpPort(8080));
    }

    @Test
    public void testIpPort() {
        Assert.assertEquals("127.0.0.1:80", IPUtil.getIpPort("127.0.0.1", 80));
        Assert.assertNull(IPUtil.getIpPort(null, 80));
    }

    @Test
    public void testParseIpPort() {
        Object[] result = IPUtil.parseIpPort("127.0.0.1:8080");
        Assert.assertEquals("127.0.0.1", result[0]);
        Assert.assertEquals(8080, result[1]);
        try {
            IPUtil.parseIpPort("127.0.0.1");
            Assert.fail();
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            IPUtil.parseIpPort(null);
            Assert.fail();
        } catch (NullPointerException ignored) {
        }
    }

    private static Enumeration<InetAddress> enumOf(InetAddress... addresses) {
        return new Vector<>(java.util.Arrays.asList(addresses)).elements();
    }

    @Test
    public void testPickValid() throws Exception {
        // v4 环回/anyhost → null；v4 合法 → 返回
        Assert.assertNull(IPUtil.pickValid(enumOf(InetAddress.getByName("127.0.0.1"))));
        Assert.assertNull(IPUtil.pickValid(enumOf(InetAddress.getByName("0.0.0.0"))));
        Assert.assertEquals(InetAddress.getByName("192.0.2.1"), IPUtil.pickValid(enumOf(InetAddress.getByName("192.0.2.1"))));
        // 混合：先环回后合法
        Assert.assertEquals(InetAddress.getByName("192.0.2.1"),
                IPUtil.pickValid(enumOf(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("192.0.2.1"))));
        // v6 关闭 preferIPv6 → null
        Assert.assertNull(IPUtil.pickValid(enumOf((Inet6Address) Inet6Address.getByName("::1"))));
        // 空
        Assert.assertNull(IPUtil.pickValid(enumOf()));
    }

    @Test
    public void testIsValidV6Address() throws Exception {
        // 默认 preferIPv6Addresses=false → false
        Assert.assertFalse(invokeIsValidV6((Inet6Address) Inet6Address.getByName("::1")));
        // 打开属性后：环回可达 → true
        System.setProperty("java.net.preferIPv6Addresses", "true");
        try {
            Assert.assertTrue(invokeIsValidV6((Inet6Address) Inet6Address.getByName("::1")));
            // 不可达 v6 → false
            Assert.assertFalse(invokeIsValidV6((Inet6Address) Inet6Address.getByName("2001:db8::1")));
        } finally {
            System.clearProperty("java.net.preferIPv6Addresses");
        }
    }

    @Test
    public void testNormalizeV6Address() throws Exception {
        // 带 scope 的 link-local 地址
        Inet6Address scoped = (Inet6Address) Inet6Address.getByName("fe80::1%lo0");
        InetAddress normalized = invokeNormalize(scoped);
        Assert.assertNotNull(normalized);
        // 无 scope → 原样返回
        InetAddress plain = (Inet6Address) Inet6Address.getByName("::1");
        Assert.assertSame(plain, invokeNormalize((Inet6Address) plain));
    }

    private static boolean invokeIsValidAddress(InetAddress address) throws Exception {
        java.lang.reflect.Method m = IPUtil.class.getDeclaredMethod("isValidAddress", InetAddress.class);
        m.setAccessible(true);
        return (boolean) m.invoke(null, address);
    }

    private static boolean invokeIsValidV6(Inet6Address address) throws Exception {
        java.lang.reflect.Method m = IPUtil.class.getDeclaredMethod("isValidV6Address", Inet6Address.class);
        m.setAccessible(true);
        return (boolean) m.invoke(null, address);
    }

    private static InetAddress invokeNormalize(Inet6Address address) throws Exception {
        java.lang.reflect.Method m = IPUtil.class.getDeclaredMethod("normalizeV6Address", Inet6Address.class);
        m.setAccessible(true);
        return (InetAddress) m.invoke(null, address);
    }

    @Test
    public void testGetLocalAddress0Branches() throws Exception {
        // localHost 抛异常 → catch 日志 → 走 interfaces
        IPUtil.ThrowingSupplier<InetAddress> broken = () -> {
            throw new RuntimeException("boom");
        };
        IPUtil.ThrowingSupplier<Enumeration<NetworkInterface>> real = NetworkInterface::getNetworkInterfaces;
        Assert.assertNotNull(IPUtil.getLocalAddress0(broken, real));
        // preferIPv6Addresses=true 时 loopback 的 v6 地址有效 → 循环内 v6 命中分支
        System.setProperty("java.net.preferIPv6Addresses", "true");
        try {
            Assert.assertNotNull(IPUtil.getLocalAddress0(() -> null, real));
        } finally {
            System.clearProperty("java.net.preferIPv6Addresses");
        }
        // interfaces 为 null → 返回 localHost 结果
        InetAddress lh = InetAddress.getByName("127.0.0.1");
        Assert.assertSame(lh, IPUtil.getLocalAddress0(() -> lh, () -> null));
        // interfaces 供应商抛异常 → 外层 catch → 返回 localHost
        Assert.assertSame(lh, IPUtil.getLocalAddress0(() -> lh, () -> {
            throw new RuntimeException("boom");
        }));
        // localHost 无效 + interfaces 中 nextElement 抛异常 → 内层 catch
        Assert.assertSame(lh, IPUtil.getLocalAddress0(() -> lh, () -> new Enumeration<NetworkInterface>() {
            private boolean first = true;

            @Override
            public boolean hasMoreElements() {return first;}

            @Override
            public NetworkInterface nextElement() {
                first = false;
                throw new RuntimeException("boom");
            }
        }));
        // localHost 有效且 interfaces 为 null → 直接返回 localHost
        InetAddress valid = InetAddress.getByName("192.0.2.1");
        Assert.assertSame(valid, IPUtil.getLocalAddress0(() -> valid, () -> null));
        // null 地址直接判定无效
        Assert.assertFalse(invokeIsValidAddress(null));
        // localHost 无效 + 正常 interfaces → 找到有效地址
        Assert.assertNotNull(IPUtil.getLocalAddress0(() -> lh, real));
        // localHost 无效 + 空 interfaces → 返回 localHost
        Assert.assertSame(lh, IPUtil.getLocalAddress0(() -> lh, () -> new Vector<NetworkInterface>().elements()));
    }

}