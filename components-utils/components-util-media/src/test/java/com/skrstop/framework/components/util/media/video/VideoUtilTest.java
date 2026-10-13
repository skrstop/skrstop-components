package com.skrstop.framework.components.util.media.video;

import com.skrstop.framework.components.util.media.exception.image.ImageHandleException;
import com.skrstop.framework.components.util.media.exception.video.VideoHandleException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;

/**
 * 视频处理测试：使用 ffmpeg 生成测试视频
 *
 * @author 蒋时华
 */
public class VideoUtilTest {

    private static byte[] videoBytes;

    @BeforeClass
    public static void setUp() throws Exception {
        File temp = File.createTempFile("video-test", ".mp4");
        temp.deleteOnExit();
        Process p = new ProcessBuilder("ffmpeg", "-y",
                "-f", "lavfi", "-i", "testsrc=duration=1:size=64x64:rate=10",
                "-pix_fmt", "yuv420p", temp.getAbsolutePath())
                .redirectErrorStream(true)
                .start();
        p.getInputStream().readAllBytes();
        Assert.assertEquals(0, p.waitFor());
        videoBytes = Files.readAllBytes(temp.toPath());
    }

    @Test
    public void testConstructors() {
        Assert.assertNotNull(VideoUtil.Builder.of(videoBytes));
        Assert.assertNotNull(VideoUtil.Builder.of(new ByteArrayInputStream(videoBytes)));
    }

    @Test
    public void testSettersChain() {
        VideoUtil util = VideoUtil.Builder.of(videoBytes);
        Assert.assertSame(util, util.coverFrameIndex(1));
        Assert.assertSame(util, util.gifFrameRate(5));
        Assert.assertSame(util, util.gifStartFrame(0));
        Assert.assertSame(util, util.gifMargin(1));
        Assert.assertSame(util, util.gifFrameCount(3));
        Assert.assertSame(util, util.format("jpg"));
        Assert.assertSame(util, util.width(32));
        Assert.assertSame(util, util.height(32));
        Assert.assertSame(util, util.p1080());
        Assert.assertSame(util, util.p720());
        Assert.assertSame(util, util.p360());
        Assert.assertSame(util, util.aspectRatio(1.33));
        Assert.assertSame(util, util.metadata("k", "v"));
        Assert.assertSame(util, util.audioMetadata("k", "v"));
        Assert.assertSame(util, util.videoMetadata("k", "v"));
        Assert.assertSame(util, util.options("k", "v"));
        Assert.assertSame(util, util.videoOptions("k", "v"));
        Assert.assertSame(util, util.audioOptions("k", "v"));
        Assert.assertSame(util, util.frameRate(10.0));
        Assert.assertSame(util, util.simpleRate(44100));
        Assert.assertSame(util, util.audioChannels(0));
        Assert.assertSame(util, util.avCodecId(null));
        Assert.assertSame(util, util.videoQuality(50.0));
        Assert.assertSame(util, util.audioQuality(50.0));

        VideoUtil.VideoSnapshotValue snapshot = util.getVideoSnapshot();
        Assert.assertNotNull(snapshot);
        Assert.assertNull(util.getVideoSnapshot());
        Assert.assertNotNull(snapshot.getMetadata());
        Assert.assertNotNull(snapshot.getAudioMetadata());
        Assert.assertNotNull(snapshot.getVideoMetadata());
        Assert.assertNotNull(snapshot.getOptions());
        Assert.assertNotNull(snapshot.getVideoOptions());
        Assert.assertNotNull(snapshot.getAudioOptions());
    }

    @Test
    public void testExecuteVideoCover() throws IOException {
        // toFile(String)
        VideoUtil util = VideoUtil.Builder.of(videoBytes);
        util.format("jpg").coverFrameIndex(1).executeVideoCover();
        File target = File.createTempFile("cover-out", ".jpg");
        File copied = util.toFile(target.getAbsolutePath());
        Assert.assertNotNull(copied);
        Assert.assertTrue(copied.exists());

        // 输入流
        VideoUtil util2 = VideoUtil.Builder.of(videoBytes);
        util2.format("png").coverFrameIndex(1).executeVideoCover();
        try (FileInputStream fis = util2.toInputStream()) {
            Assert.assertNotNull(fis);
        }

        // 输出流
        VideoUtil util3 = VideoUtil.Builder.of(videoBytes);
        util3.format("png").coverFrameIndex(1).executeVideoCover();
        try (FileOutputStream fos = util3.toOutputStream()) {
            Assert.assertNotNull(fos);
        }

        // toFile(File)
        VideoUtil util4 = VideoUtil.Builder.of(videoBytes);
        util4.coverFrameIndex(1).executeVideoCover();
        File target4 = File.createTempFile("cover-out4", ".jpg");
        Assert.assertNotNull(util4.toFile(target4));
    }

    @Test
    public void testExecuteVideo() {
        VideoUtil util = VideoUtil.Builder.of(videoBytes);
        util.format("mp4").videoQuality(35.0).audioQuality(35.0).executeVideoRecorderFrame();
        VideoUtil.VideoSnapshotValue snapshot = util.getVideoSnapshot();
        Assert.assertTrue(snapshot.isExecute());
        Assert.assertTrue(snapshot.getOut().length() > 0);
    }

    @Test
    public void testExecuteVideoWithOptions() {
        // 带 p360、帧率、采样率等配置
        VideoUtil util = VideoUtil.Builder.of(videoBytes);
        util.p360().frameRate(10.0).simpleRate(44100).audioChannels(0)
                .metadata("title", "t").videoMetadata("vm", "v").audioMetadata("am", "v")
                .options("o", "v").videoOptions("vo", "v").audioOptions("ao", "v")
                .videoQuality(50.0).audioQuality(50.0)
                .executeVideoRecorderFrame();
        Assert.assertTrue(util.getVideoSnapshot().isExecute());
    }

    @Test
    public void testExecuteVideoGif() {
        VideoUtil util = VideoUtil.Builder.of(videoBytes);
        util.gifStartFrame(0).gifFrameCount(5).gifMargin(1).gifFrameRate(5).executeVideoGif();
        VideoUtil.VideoSnapshotValue snapshot = util.getVideoSnapshot();
        Assert.assertTrue(snapshot.isExecute());
        Assert.assertTrue(snapshot.getOut().length() > 0);
    }

    @Test
    public void testGifVideoTooShort() {
        // gif 起始帧超过视频总帧数 → 视频太短
        VideoUtil util = VideoUtil.Builder.of(videoBytes);
        try {
            util.gifStartFrame(1000).gifFrameCount(5).executeVideoGif();
            Assert.fail("should throw");
        } catch (VideoHandleException ignored) {
        }
    }

    @Test
    public void testCorruptVideoThrows() {
        // 非法视频字节 → VideoHandleException
        VideoUtil util = VideoUtil.Builder.of("not-a-video".getBytes());
        try {
            util.executeVideoCover();
            Assert.fail("should throw");
        } catch (VideoHandleException ignored) {
        }
        VideoUtil util2 = VideoUtil.Builder.of("not-a-video".getBytes());
        try {
            util2.executeVideoRecorderFrame();
            Assert.fail("should throw");
        } catch (VideoHandleException ignored) {
        }
        VideoUtil util3 = VideoUtil.Builder.of("not-a-video".getBytes());
        try {
            util3.executeVideoGif();
            Assert.fail("should throw");
        } catch (VideoHandleException ignored) {
        }
    }

    @Test
    public void testNotExecuteReturnsNull() throws Exception {
        // 未执行 → toFile/toInputStream/toOutputStream 返回 null（每次使用新的实例）
        Assert.assertNull(VideoUtil.Builder.of(videoBytes).toFile(File.createTempFile("aaa", ".b").getAbsolutePath()));
        Assert.assertNull(VideoUtil.Builder.of(videoBytes).toFile(File.createTempFile("bbb", ".b")));
        Assert.assertNull(VideoUtil.Builder.of(videoBytes).toInputStream());
        Assert.assertNull(VideoUtil.Builder.of(videoBytes).toOutputStream());
    }

    @Test
    public void testOfFileNotFound() {
        try {
            VideoUtil.Builder.ofFile("/not/exist/video.mp4");
            Assert.fail("should throw");
        } catch (ImageHandleException ignored) {
        }
    }

    @Test
    public void testVideoSnapshotValueSetters() throws IOException {
        VideoUtil.VideoSnapshotValue value = new VideoUtil.VideoSnapshotValue();
        value.setSourceBytes(new byte[1]);
        value.setBuffedStream(new ByteArrayInputStream(new byte[1]));
        File out = File.createTempFile("vvv", ".tmp");
        value.setOut(out);
        value.setFormat("mp4");
        value.setWidth(1);
        value.setHeight(1);
        value.setFrameRate(1.0);
        value.setSampleRate(1);
        value.setAudioChannels(1);
        value.setAvCodecId(1);
        value.setVideoQuality(1.0);
        value.setAudioQuality(1.0);
        value.setAspectRatio(1.0);
        value.setMetadata(null);
        value.setAudioMetadata(null);
        value.setVideoMetadata(null);
        value.setOptions(null);
        value.setVideoOptions(null);
        value.setAudioOptions(null);
        value.setCoverFrameIndex(1);
        value.setGifStartFrame(1);
        value.setGifFrameCount(1);
        value.setGifMargin(1);
        value.setGifFrameRate(1);
        value.setExecute(true);
        Assert.assertTrue(value.isExecute());
        Assert.assertNotNull(value.getSourceBytes());
        Assert.assertNotNull(value.getBuffedStream());
        Assert.assertEquals(out, value.getOut());
        Assert.assertEquals("mp4", value.getFormat());
        Assert.assertNotNull(value.getAvCodecId());
    }

    @Test
    public void testOfUrlFileProtocol() throws IOException {
        File temp = File.createTempFile("video-test2", ".mp4");
        temp.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(temp)) {
            fos.write(videoBytes);
        }
        com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(new java.net.InetSocketAddress(0), 0);
        byte[] data = Files.readAllBytes(temp.toPath());
        server.createContext("/video", exchange -> {
            exchange.sendResponseHeaders(200, data.length);
            try (java.io.OutputStream os = exchange.getResponseBody()) {
                os.write(data);
            }
        });
        server.start();
        try {
            VideoUtil util = VideoUtil.Builder.ofUrl(new java.net.URL("http://127.0.0.1:" + server.getAddress().getPort() + "/video"));
            Assert.assertNotNull(util);
        } finally {
            server.stop(0);
        }
        Assert.assertNull(VideoUtil.Builder.ofUrl((java.net.URL) null));
    }


    @Test
    public void testCreateTempOutFailure() {
        // 创建临时文件失败 → IllegalArgumentException（构造函数内捕获 IOException）
        try {
            new VideoUtil(videoBytes) {
                @Override
                File createTempOut() throws IOException {
                    throw new IOException("no temp");
                }
            };
            Assert.fail("should throw");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testExecuteVideoFullOptions() {
        // avCodecId / aspectRatio 非空分支
        VideoUtil util = VideoUtil.Builder.of(videoBytes);
        util.format("mp4")
                .avCodecId(org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264)
                .aspectRatio(1.0)
                .executeVideoRecorderFrame();
        Assert.assertTrue(util.getVideoSnapshot().isExecute());
    }

    @Test
    public void testExecuteVideoNoQuality() {
        // videoQuality/audioQuality 为空分支
        VideoUtil util = VideoUtil.Builder.of(videoBytes);
        util.format("mp4").executeVideoRecorderFrame();
        Assert.assertTrue(util.getVideoSnapshot().isExecute());
    }

    @Test
    public void testCloseHelpers() {
        // close 抛异常被静默处理
        org.bytedeco.javacv.FFmpegFrameRecorder recorder =
                org.mockito.Mockito.mock(org.bytedeco.javacv.FFmpegFrameRecorder.class);
        try {
            org.mockito.Mockito.doThrow(new org.bytedeco.javacv.FrameRecorder.Exception("close fail"))
                    .when(recorder).close();
        } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
            throw new AssertionError(e);
        }
        VideoUtil.closeRecorder(recorder);

        org.bytedeco.javacv.FFmpegFrameGrabber grabber =
                org.mockito.Mockito.mock(org.bytedeco.javacv.FFmpegFrameGrabber.class);
        try {
            org.mockito.Mockito.doThrow(new org.bytedeco.javacv.FrameGrabber.Exception("close fail"))
                    .when(grabber).close();
        } catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
            throw new AssertionError(e);
        }
        VideoUtil.closeGrabber(grabber);
    }


}
