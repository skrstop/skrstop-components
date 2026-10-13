package com.skrstop.framework.components.util.media;

import com.skrstop.framework.components.util.media.exception.image.ImageHandleException;
import com.skrstop.framework.components.util.media.image.ImageUtil;
import com.skrstop.framework.components.util.media.video.VideoUtil;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.net.URL;

/**
 * Builder 隐式构造器与遗漏分支补覆盖
 */
public class BuilderMediaTest {

    @Test
    public void testVideoBuilderInitAndOfFile() {
        Assert.assertNotNull(new VideoUtil.Builder());
        // ofFile(File) 成功路径（构造仅读取字节，不涉及编解码）
        VideoUtil util = VideoUtil.Builder.ofFile(new File("pom.xml"));
        Assert.assertNotNull(util);
        // ofFile(String) 成功路径
        Assert.assertNotNull(VideoUtil.Builder.ofFile(new File("pom.xml").getAbsolutePath()));
    }

    @Test
    public void testVideoOfUrlBlankToString() throws Exception {
        // URL 为 final 类，使用 inline mock 构造 toString 为空白的 URL，覆盖 isNotBlank 的 false 侧
        URL blankUrl = Mockito.mock(URL.class);
        Mockito.when(blankUrl.toString()).thenReturn("   ");
        Assert.assertNull(VideoUtil.Builder.ofUrl(blankUrl));
    }

    @Test
    public void testVideoOfUrlString() {
        // ofUrl(String) 委托行执行；本机端口拒绝连接，StreamUtil 返回 null 或抛 IOException 均可
        try {
            VideoUtil result = VideoUtil.Builder.ofUrl("http://127.0.0.1:1/video.mp4");
            Assert.assertNull(result);
        } catch (java.io.IOException expected) {
            // 连接被拒绝时抛出 IOException，属预期
        }
    }

    @Test
    public void testVideoOfFileNotFound() {
        try {
            VideoUtil.Builder.ofFile(new File("/not/exist/video.mp4"));
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
    }

    @Test
    public void testVideoOfUrlStringSuccess() throws Exception {
        // 本机 HttpServer，使 ofUrl(String) 委托调用完整返回
        com.sun.net.httpserver.HttpServer server =
                com.sun.net.httpserver.HttpServer.create(new java.net.InetSocketAddress(0), 0);
        byte[] data = new byte[]{1, 2, 3};
        server.createContext("/video.mp4", exchange -> {
            exchange.sendResponseHeaders(200, data.length);
            try (java.io.OutputStream os = exchange.getResponseBody()) {
                os.write(data);
            }
        });
        server.start();
        try {
            VideoUtil util = VideoUtil.Builder.ofUrl("http://127.0.0.1:" + server.getAddress().getPort() + "/video.mp4");
            Assert.assertNotNull(util);
        } finally {
            server.stop(0);
        }
    }

    @Test
    public void testImageBuilderInitAndOfFile() {
        Assert.assertNotNull(new ImageUtil.Builder());
        // ofFile(File) 未找到文件 -> catch 分支
        try {
            ImageUtil.Builder.ofFile(new File("/not/exist/file.png"));
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
    }

    @Test
    public void testImageOfUrlBlankToString() throws Exception {
        URL blankUrl = Mockito.mock(URL.class);
        Mockito.when(blankUrl.toString()).thenReturn("   ");
        Assert.assertNull(ImageUtil.Builder.ofUrl(blankUrl));
    }
}
