package com.skrstop.framework.components.util.media.image;

import com.skrstop.framework.components.util.media.exception.image.ImageHandleException;
import net.coobird.thumbnailator.geometry.Positions;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

/**
 * @author 蒋时华
 */
public class ImageUtilTest {

    private byte[] pngBytes() throws IOException {
        BufferedImage image = new BufferedImage(100, 50, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bos);
        return bos.toByteArray();
    }

    private File pngFile() throws IOException {
        File f = File.createTempFile("img-test", ".png");
        try (FileOutputStream fos = new FileOutputStream(f)) {
            fos.write(pngBytes());
        }
        return f;
    }

    @Test
    public void testConstructors() throws IOException {
        byte[] bytes = pngBytes();
        Assert.assertNotNull(ImageUtil.Builder.of(bytes));
        Assert.assertNotNull(ImageUtil.Builder.of(new ByteArrayInputStream(bytes)));
        Assert.assertNotNull(ImageUtil.Builder.ofFile(pngFile()));
        Assert.assertNotNull(ImageUtil.Builder.ofFile(pngFile().getAbsolutePath()));
        // 非法 JPEG 数据 → ImageIO.read 抛 IIOException(IOException) → ImageHandleException
        byte[] corruptJpeg = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, 0x00, 0x01, 0x02, 0x03, 0x04};
        try {
            ImageUtil.Builder.of(corruptJpeg);
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
        try {
            ImageUtil.Builder.ofFile("/not/exist/file.png");
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
    }

    @Test
    public void testTransformations() throws IOException {
        ImageUtil util = ImageUtil.Builder.of(pngBytes());
        util.compress1080();
        util.compress720();
        util.compressProportionWidth(30);
        util.compressProportionHeight(20);
        util.rotate(90);
        util.size(20, 10);
        util.scale(0.5);
        util.sourceRegion(Positions.CENTER, 10, 10);
        util.sourceRegion(0, 0, 20, 20);

        File water = pngFile();
        BufferedImage waterImage = ImageIO.read(water);
        util.waterMarkImage(Positions.CENTER, water.getAbsolutePath(), 0.5F);
        util.waterMarkImage(Positions.CENTER, new FileInputStream(water), 0.5F);
        util.waterMarkImage(Positions.CENTER, water, 0.5F);
        util.waterMarkImage(Positions.CENTER, waterImage, 0.5F);

        ImageUtil.ImageSnapshotValue snapshot = util.getImageSnapshot();
        Assert.assertNotNull(snapshot);
        Assert.assertNotNull(snapshot.getTargetBuffedImage());
        // 第二次获取为 null
        Assert.assertNull(util.getImageSnapshot());
    }

    @Test
    public void testWaterMarkUrl() throws IOException {
        // 水印 URL：使用文件协议
        File water = pngFile();
        ImageUtil util = ImageUtil.Builder.of(pngBytes());
        util.waterMarkImageUrl(Positions.CENTER, water.toURI().toURL().toString(), 0.5F);
        Assert.assertNotNull(util.getImageSnapshot());
    }

    @Test
    public void testOutputs() throws IOException {
        byte[] bytes = pngBytes();

        // InputStream 系列
        Assert.assertNotNull(ImageUtil.Builder.of(bytes).toInputStream(0.5, "jpg"));
        Assert.assertNotNull(ImageUtil.Builder.of(bytes).toInputStream(0.5));
        Assert.assertNotNull(ImageUtil.Builder.of(bytes).toInputStream("png"));
        Assert.assertNotNull(ImageUtil.Builder.of(bytes).toInputStream());

        // OutputStream 系列
        Assert.assertNotNull(ImageUtil.Builder.of(bytes).toOutputStream(0.5, "jpg"));
        Assert.assertNotNull(ImageUtil.Builder.of(bytes).toOutputStream(0.5));
        Assert.assertNotNull(ImageUtil.Builder.of(bytes).toOutputStream("png"));
        Assert.assertNotNull(ImageUtil.Builder.of(bytes).toOutputStream());

        // File 系列
        File out1 = File.createTempFile("img-out1", ".jpg");
        ImageUtil.Builder.of(bytes).toFile(out1.getAbsolutePath(), 0.5, "jpg");
        Assert.assertTrue(out1.length() > 0);

        File out2 = File.createTempFile("img-out2", ".jpg");
        ImageUtil.Builder.of(bytes).toFile(out2, 0.5, "jpg");
        Assert.assertTrue(out2.length() > 0);

        File out3 = File.createTempFile("img-out3", ".jpg");
        ImageUtil.Builder.of(bytes).toFile(out3.getAbsolutePath());
        Assert.assertTrue(out3.length() > 0);

        File out4 = File.createTempFile("img-out4", ".jpg");
        ImageUtil.Builder.of(bytes).toFile(out4);
        Assert.assertTrue(out4.length() > 0);

        File out5 = File.createTempFile("img-out5", ".jpg");
        ImageUtil.Builder.of(bytes).toFile(out5.getAbsolutePath(), 0.5);
        Assert.assertTrue(out5.length() > 0);

        File out6 = File.createTempFile("img-out6", ".jpg");
        ImageUtil.Builder.of(bytes).toFile(out6, 0.5);
        Assert.assertTrue(out6.length() > 0);

        File out7 = File.createTempFile("img-out7", ".png");
        ImageUtil.Builder.of(bytes).toFile(out7.getAbsolutePath(), "png");
        Assert.assertTrue(out7.length() > 0);

        File out8 = File.createTempFile("img-out8", ".png");
        ImageUtil.Builder.of(bytes).toFile(out8, "png");
        Assert.assertTrue(out8.length() > 0);
    }

    @Test
    public void testOfUrlHttp() throws Exception {
        // 本地 http 服务返回图片字节
        com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(new java.net.InetSocketAddress(0), 0);
        byte[] data = pngBytes();
        server.createContext("/img", exchange -> {
            exchange.sendResponseHeaders(200, data.length);
            try (java.io.OutputStream os = exchange.getResponseBody()) {
                os.write(data);
            }
        });
        server.start();
        try {
            URL url = new URL("http://127.0.0.1:" + server.getAddress().getPort() + "/img");
            ImageUtil util = ImageUtil.Builder.ofUrl(url);
            Assert.assertNotNull(util);
            ImageUtil util2 = ImageUtil.Builder.ofUrl(url.toString());
            Assert.assertNotNull(util2);
        } finally {
            server.stop(0);
        }

        // null → null
        Assert.assertNull(ImageUtil.Builder.ofUrl((URL) null));
        try {
            ImageUtil.Builder.ofUrl("  ");
            Assert.fail("should throw");
        } catch (IOException ignored) {
        }
    }

    @Test
    public void testCompressWidthHeightBranches() throws IOException {
        // 宽 >= 高 → compressProportionWidth
        ImageUtil wide = ImageUtil.Builder.of(pngBytes());
        wide.compress(30);
        // 高 > 宽 → compressProportionHeight
        BufferedImage tall = new BufferedImage(50, 100, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(tall, "png", bos);
        ImageUtil tallUtil = ImageUtil.Builder.of(bos.toByteArray());
        tallUtil.compress(30);
    }

    @Test
    public void testInvalidOutputFormat() throws IOException {
        // 非法输出格式 → IOException → ImageHandleException
        try {
            ImageUtil.Builder.of(pngBytes()).toOutputStream("invalid-format-xyz");
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
    }

    @Test
    public void testSpentInstanceThrows() throws IOException {
        // snapshot 被取走后再次操作 → 异常
        ImageUtil util = ImageUtil.Builder.of(pngBytes());
        util.getImageSnapshot();
        try {
            util.compress(30);
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
        try {
            util.compressProportionWidth(30);
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
        try {
            util.compressProportionHeight(30);
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
        try {
            util.rotate(90);
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
        try {
            util.size(10, 10);
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
        try {
            util.scale(0.5);
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
        try {
            util.sourceRegion(Positions.CENTER, 10, 10);
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
        try {
            util.sourceRegion(0, 0, 10, 10);
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
        try {
            util.waterMarkImage(Positions.CENTER, "water", 0.5F);
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
        try {
            util.waterMarkImageUrl(Positions.CENTER, "http://127.0.0.1:1/x.png", 0.5F);
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
        try {
            util.waterMarkImage(Positions.CENTER, new ByteArrayInputStream(new byte[0]), 0.5F);
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
        try {
            util.waterMarkImage(Positions.CENTER, File.createTempFile("wmk", ".png"), 0.5F);
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
        try {
            util.waterMarkImage(Positions.CENTER, new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB), 0.5F);
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
        try {
            util.toFile(File.createTempFile("xxx", ".jpg").getAbsolutePath(), 0.5, "jpg");
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
        try {
            util.toFile(File.createTempFile("xxx", ".jpg"), 0.5, "jpg");
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
    }

    @Test
    public void testSnapshotValueSetters() throws IOException {
        ImageUtil.ImageSnapshotValue value = new ImageUtil.ImageSnapshotValue();
        value.setSourceBytes(new byte[1]);
        value.setTargetBuffedImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
        Assert.assertNotNull(value.getSourceBytes());
        Assert.assertNotNull(value.getTargetBuffedImage());
    }

}
