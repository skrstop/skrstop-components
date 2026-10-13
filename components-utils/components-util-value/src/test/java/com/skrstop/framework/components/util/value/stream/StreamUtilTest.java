package com.skrstop.framework.components.util.value.stream;

import com.skrstop.framework.components.core.exception.defined.io.IOStreamingException;
import com.sun.net.httpserver.HttpServer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author 蒋时华
 */
public class StreamUtilTest {

    private static HttpServer server;
    private static String baseUrl;

    @BeforeClass
    public static void setUp() throws Exception {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        byte[] body = "hello stream".getBytes(StandardCharsets.UTF_8);
        server.createContext(
                "/data",
                exchange -> {
                    exchange.sendResponseHeaders(200, body.length);
                    exchange.getResponseBody().write(body);
                    exchange.close();
                });
        server.start();
        baseUrl = "http://127.0.0.1:" + server.getAddress().getPort() + "/data";
    }

    @AfterClass
    public static void tearDown() {
        server.stop(0);
    }

    @Test
    public void testParse() throws Exception {
        ByteArrayOutputStream out = StreamUtil.parse(new ByteArrayInputStream("abc".getBytes()));
        Assert.assertEquals("abc", out.toString());

        InputStream in = StreamUtil.parse(out);
        byte[] bytes = new byte[3];
        Assert.assertEquals(3, in.read(bytes));
        Assert.assertEquals("abc", new String(bytes));

        Assert.assertEquals("abc", StreamUtil.parseString(out));
        Assert.assertEquals("abc", StreamUtil.parseString(StreamUtil.parseInputStream("abc")));
        Assert.assertEquals("abc", StreamUtil.parseOutputStream("abc").toString());
    }

    @Test
    public void testParseStringAndByte() throws Exception {
        Assert.assertEquals("abc", StreamUtil.parseString(new ByteArrayInputStream("abc".getBytes())));
        Assert.assertArrayEquals(
                "abc".getBytes(), StreamUtil.inputStreamToByte(new ByteArrayInputStream("abc".getBytes())));
        // 异常分支：读取即抛异常的流
        try {
            StreamUtil.parseString(new BrokenStream());
            Assert.fail();
        } catch (IOStreamingException ignored) {
        }
        try {
            StreamUtil.inputStreamToByte(new BrokenStream());
            Assert.fail();
        } catch (IOStreamingException ignored) {
        }
        try {
            StreamUtil.parse(new BrokenStream());
            Assert.fail();
        } catch (IOStreamingException ignored) {
        }
        try {
            StreamUtil.inputStreamToString(new BrokenStream(), "UTF-8");
            Assert.fail();
        } catch (IOStreamingException ignored) {
        }
    }

    @Test
    public void testByteToInputStream() throws Exception {
        InputStream in = StreamUtil.byteToInputStream("abc".getBytes());
        Assert.assertEquals('a', in.read());
    }

    @Test
    public void testGetInputStreamFromUrl() throws Exception {
        InputStream in = StreamUtil.getInputStream(baseUrl);
        Assert.assertNotNull(in);
        Assert.assertArrayEquals("hello stream".getBytes(), StreamUtil.inputStreamToByte(in));
        Assert.assertNull(StreamUtil.getInputStream(""));

        URL url = new URL(baseUrl);
        InputStream in2 = StreamUtil.getInputStream(url);
        Assert.assertNotNull(in2);
        Assert.assertArrayEquals("hello stream".getBytes(), StreamUtil.inputStreamToByte(in2));
        Assert.assertNull(StreamUtil.getInputStream((URL) null));

        byte[] bytes = StreamUtil.getBytes(url);
        Assert.assertArrayEquals("hello stream".getBytes(), bytes);
        Assert.assertNull(StreamUtil.getBytes(null));
    }

    @Test
    public void testGetInputStreamBlankAndUrl() throws Exception {
        Assert.assertNull(StreamUtil.getInputStream((String) null));
        Assert.assertNull(StreamUtil.getInputStream((java.net.URL) null));
        // 无效 URL
        try {
            StreamUtil.getInputStream("::::bad");
            Assert.fail();
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testByteToInputStreamParses() throws Exception {
        java.io.InputStream in = StreamUtil.byteToInputStream("abc".getBytes());
        Assert.assertEquals("abc", StreamUtil.parseString(in));
    }


    private static class BrokenStream extends InputStream {
        @Override
        public int read() throws java.io.IOException {
            throw new java.io.IOException("broken");
        }
    }

}
