package com.skrstop.framework.components.util.media.video;

import cn.hutool.core.img.gif.AnimatedGifEncoder;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.skrstop.framework.components.util.constant.ImageTypeConst;
import com.skrstop.framework.components.util.constant.VideoTypeConst;
import com.skrstop.framework.components.util.media.exception.image.ImageHandleException;
import com.skrstop.framework.components.util.media.exception.video.VideoHandleException;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import com.skrstop.framework.components.util.value.stream.StreamUtil;
import lombok.Getter;
import lombok.Setter;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 视频处理工具类
 *
 * @author 蒋时华
 * @date 2019/9/25
 */
public class VideoUtil {

    private final String defaultVideoFormat = VideoTypeConst.FORMAT_MP4;
    private final String defaultCoverFormat = ImageTypeConst.JPG_FORMAT_NAME;

    @Setter
    @Getter
    public static class VideoSnapshotValue {
        /*** 原 */
        private byte[] sourceBytes;
        /*** 处理后 */
        private InputStream buffedStream;

        private File out;
        private String format;
        private Integer width;
        private Integer height;
        private Double frameRate;
        private Integer sampleRate;
        private Integer audioChannels;
        private Integer avCodecId;
        // 0 ~ 100
        private Double videoQuality;
        // 0 ~ 100
        private Double audioQuality;
        // 宽高比
        private Double aspectRatio;
        private Map<String, String> metadata;
        private Map<String, String> audioMetadata;
        private Map<String, String> videoMetadata;
        private Map<String, String> options;
        private Map<String, String> videoOptions;
        private Map<String, String> audioOptions;

        // 封面帧数地址
        private Integer coverFrameIndex = 5;

        // gif 开始帧数
        private Integer gifStartFrame = 5;
        // gif 截取帧数
        private Integer gifFrameCount = 15;
        // gif 每截取一次跳过多少帧
        private Integer gifMargin = 3;
        // 帧频率
        private Integer gifFrameRate = 10;

        private boolean execute;
    }

    private ThreadLocal<VideoSnapshotValue> videoSnapshot = new ThreadLocal<>();

    /**
     * 获取文件快照，只能获取一次，再次获取为null
     *
     * @return
     */
    public VideoSnapshotValue getVideoSnapshot() {
        VideoSnapshotValue imageSnapshot = this.videoSnapshot.get();
        this.videoSnapshot.remove();
        return imageSnapshot;
    }

    public VideoUtil(InputStream targetInStream) {
        this(StreamUtil.inputStreamToByte(targetInStream));
    }

    public VideoUtil(byte[] bytes) {
        VideoSnapshotValue videoSnapshotValue = new VideoSnapshotValue();
        videoSnapshotValue.setSourceBytes(bytes);
        videoSnapshotValue.setBuffedStream(StreamUtil.byteToInputStream(videoSnapshotValue.getSourceBytes()));
        try {
            videoSnapshotValue.setOut(File.createTempFile(IdUtil.fastSimpleUUID(), null));
        } catch (IOException e) {
            throw new IllegalArgumentException("创建临时文件失败");
        }
        videoSnapshotValue.setMetadata(new HashMap<>());
        videoSnapshotValue.setAudioMetadata(new HashMap<>());
        videoSnapshotValue.setVideoMetadata(new HashMap<>());
        videoSnapshotValue.setOptions(new HashMap<>());
        videoSnapshotValue.setVideoOptions(new HashMap<>());
        videoSnapshotValue.setAudioOptions(new HashMap<>());
        this.videoSnapshot.set(videoSnapshotValue);
    }

    /**
     * 封面去第几帧
     *
     * @param coverFrameIndex
     * @return
     */
    public VideoUtil coverFrameIndex(int coverFrameIndex) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.setCoverFrameIndex(coverFrameIndex);
        return this;
    }

    /**
     * gif 开始帧数
     *
     * @param gifFrameRate
     * @return
     */
    public VideoUtil gifFrameRate(int gifFrameRate) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.setGifFrameRate(gifFrameRate);
        return this;
    }

    /**
     * gif 开始帧数
     *
     * @param gifStartFrameIndex
     * @return
     */
    public VideoUtil gifStartFrame(int gifStartFrameIndex) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.setGifStartFrame(gifStartFrameIndex);
        return this;
    }

    /**
     * gif 每截取一次跳过多少帧
     *
     * @param gifMargin
     * @return
     */
    public VideoUtil gifMargin(int gifMargin) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.setGifMargin(gifMargin);
        return this;
    }

    /**
     * gif 截取帧数
     *
     * @param gifStartFrameCount
     * @return
     */
    public VideoUtil gifFrameCount(int gifStartFrameCount) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.setGifFrameCount(gifStartFrameCount);
        return this;
    }

    /**
     * 设置视频格式
     *
     * @param format
     * @return
     */
    public VideoUtil format(String format) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.setFormat(format);
        return this;
    }

    /**
     * 设置视频宽
     *
     * @param width
     * @return
     */
    public VideoUtil width(int width) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.setWidth(width);
        return this;
    }

    /**
     * 设置视频高
     *
     * @param height
     * @return
     */
    public VideoUtil height(int height) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.setHeight(height);
        return this;
    }

    /**
     * 设置视频1080P
     *
     * @return
     */
    public VideoUtil p1080() {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.setWidth(1920);
        videoSnapshotValue.setHeight(1080);
        return this;
    }

    /**
     * 设置视频720P
     *
     * @return
     */
    public VideoUtil p720() {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.setWidth(1280);
        videoSnapshotValue.setHeight(720);
        return this;
    }

    /**
     * 设置视频360P
     *
     * @return
     */
    public VideoUtil p360() {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.setWidth(480);
        videoSnapshotValue.setHeight(360);
        return this;
    }

    /**
     * 设置视频宽高比
     *
     * @param aspectRatio
     * @return
     */
    public VideoUtil aspectRatio(Double aspectRatio) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.setAspectRatio(aspectRatio);
        return this;
    }

    /**
     * 设置元数据
     *
     * @param key
     * @param value
     * @return
     */
    public VideoUtil metadata(String key, String value) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.getMetadata().put(key, value);
        return this;
    }

    /**
     * 设置音频元数据
     *
     * @param key
     * @param value
     * @return
     */
    public VideoUtil audioMetadata(String key, String value) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.getAudioMetadata().put(key, value);
        return this;
    }

    /**
     * 设置视频元数据
     *
     * @param key
     * @param value
     * @return
     */
    public VideoUtil videoMetadata(String key, String value) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.getVideoMetadata().put(key, value);
        return this;
    }

    /**
     * 设置额外数据
     *
     * @param key
     * @param value
     * @return
     */
    public VideoUtil options(String key, String value) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.getOptions().put(key, value);
        return this;
    }

    /**
     * 设置额外视频数据
     *
     * @param key
     * @param value
     * @return
     */
    public VideoUtil videoOptions(String key, String value) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.getVideoOptions().put(key, value);
        return this;
    }

    /**
     * 设置额外音频数据
     *
     * @param key
     * @param value
     * @return
     */
    public VideoUtil audioOptions(String key, String value) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.getAudioOptions().put(key, value);
        return this;
    }

    /**
     * 设置视频帧率
     *
     * @param frameRate
     * @return
     */
    public VideoUtil frameRate(double frameRate) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.setFrameRate(frameRate);
        return this;
    }

    /**
     * 设置视频采样率
     *
     * @param sampleRate
     * @return
     */
    public VideoUtil simpleRate(int sampleRate) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.setSampleRate(sampleRate);
        return this;
    }

    /**
     * 设置音频通道
     *
     * @param audioChannels
     * @return
     * @see avcodec
     */
    public VideoUtil audioChannels(int audioChannels) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.setAudioChannels(audioChannels);
        return this;
    }

    /**
     * 设置音频编解码器
     *
     * @param avCodecId
     * @return
     */
    public VideoUtil avCodecId(Integer avCodecId) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.setAvCodecId(avCodecId);
        return this;
    }

    /**
     * 设置视频质量 0 ~ 100
     *
     * @param videoQuality
     * @return
     */
    public VideoUtil videoQuality(Double videoQuality) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.setVideoQuality(videoQuality);
        return this;
    }

    /**
     * 设置音频质量 0 ~ 100
     *
     * @param audioQuality
     * @return
     */
    public VideoUtil audioQuality(Double audioQuality) {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        videoSnapshotValue.setAudioQuality(audioQuality);
        return this;
    }

    /**
     * 执行操作
     *
     * @return
     */
    public VideoUtil executeVideoRecorderFrame() {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        FFmpegFrameRecorder recorder = null;
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoSnapshotValue.getBuffedStream());
        try {
            frameGrabber.start();
            recorder = new FFmpegFrameRecorder(videoSnapshotValue.getOut(),
                    ObjectUtil.isNull(videoSnapshotValue.getWidth()) ? frameGrabber.getImageWidth() : videoSnapshotValue.getWidth(),
                    ObjectUtil.isNull(videoSnapshotValue.getHeight()) ? frameGrabber.getImageHeight() : videoSnapshotValue.getHeight(),
                    ObjectUtil.isNull(videoSnapshotValue.getAudioChannels()) ? frameGrabber.getAudioChannels() : videoSnapshotValue.getAudioChannels());
            recorder.setVideoCodec(ObjectUtil.isNull(videoSnapshotValue.getAvCodecId()) ? avcodec.AV_CODEC_ID_H264 : videoSnapshotValue.getAvCodecId());
            recorder.setFormat(ObjectUtil.isNull(videoSnapshotValue.getFormat()) ? this.defaultVideoFormat : videoSnapshotValue.getFormat());
            recorder.setFrameRate(ObjectUtil.isNull(videoSnapshotValue.getFrameRate()) ? frameGrabber.getFrameRate() : videoSnapshotValue.getFrameRate());
            recorder.setSampleRate(ObjectUtil.isNull(videoSnapshotValue.getSampleRate()) ? frameGrabber.getSampleRate() : videoSnapshotValue.getSampleRate());

            if (ObjectUtil.isNotNull(videoSnapshotValue.getAspectRatio())) {
                recorder.setAspectRatio(videoSnapshotValue.getAspectRatio());
            }
            if (ObjectUtil.isNotNull(videoSnapshotValue.getVideoQuality())) {
                recorder.setVideoQuality(videoSnapshotValue.getVideoQuality());
            }
            if (ObjectUtil.isNotNull(videoSnapshotValue.getAudioQuality())) {
                recorder.setAudioQuality(videoSnapshotValue.getAudioQuality());
            }

            Map<String, String> oldMetadata = frameGrabber.getMetadata();
            oldMetadata.putAll(videoSnapshotValue.getMetadata());
            recorder.setMetadata(oldMetadata);
            Map<String, String> oldAudioMetadata = frameGrabber.getAudioMetadata();
            oldAudioMetadata.putAll(videoSnapshotValue.getAudioMetadata());
            recorder.setAudioMetadata(oldAudioMetadata);
            Map<String, String> oldVideoMetadata = frameGrabber.getVideoMetadata();
            oldVideoMetadata.putAll(videoSnapshotValue.getVideoMetadata());
            recorder.setVideoMetadata(oldVideoMetadata);

            Map<String, String> oldOptions = frameGrabber.getOptions();
            oldOptions.putAll(videoSnapshotValue.getOptions());
            recorder.setOptions(oldOptions);
            Map<String, String> oldAudioOptions = frameGrabber.getAudioOptions();
            oldAudioOptions.putAll(videoSnapshotValue.getAudioOptions());
            recorder.setAudioOptions(oldAudioOptions);
            Map<String, String> oldVideoOptions = frameGrabber.getVideoOptions();
            oldVideoOptions.putAll(videoSnapshotValue.getVideoOptions());
            recorder.setVideoOptions(oldVideoOptions);

            recorder.start();
            while (true) {
                Frame frame = frameGrabber.grabFrame();
                if (ObjectUtil.isNull(frame)) {
                    break;
                }
                recorder.setTimestamp(frameGrabber.getTimestamp());
                recorder.record(frame);
            }
            videoSnapshotValue.setExecute(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new VideoHandleException(e);
        } finally {
            if (ObjectUtil.isNotNull(recorder)) {
                try {
                    recorder.close();
                } catch (FrameRecorder.Exception e) {
                    e.printStackTrace();
                }
            }
            if (ObjectUtil.isNotNull(frameGrabber)) {
                try {
                    frameGrabber.close();
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    /**
     * 获取视频封面
     *
     * @return
     */
    public VideoUtil executeVideoCover() {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        FFmpegFrameGrabber frameGrabber = null;
        try {
            frameGrabber = new FFmpegFrameGrabber(videoSnapshotValue.getBuffedStream());
            frameGrabber.start();
            Frame frame = null;
            // 截取封面
            for (int i = 0; i < videoSnapshotValue.getCoverFrameIndex(); i++) {
                frame = frameGrabber.grabImage();
            }
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage bi = converter.getBufferedImage(frame);
            ImageIO.write(bi
                    , ObjectUtil.isNull(videoSnapshotValue.getFormat()) ? this.defaultCoverFormat : videoSnapshotValue.getFormat()
                    , videoSnapshotValue.getOut());
            videoSnapshotValue.setExecute(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new VideoHandleException(e);
        } finally {
            if (ObjectUtil.isNotNull(frameGrabber)) {
                try {
                    frameGrabber.close();
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    /**
     * 视频转gif
     *
     * @return
     */
    public VideoUtil executeVideoGif() {
        VideoSnapshotValue videoSnapshotValue = this.videoSnapshot.get();
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoSnapshotValue.getBuffedStream());
        Java2DFrameConverter converter = new Java2DFrameConverter();
        try {
            frameGrabber.start();
            if (videoSnapshotValue.getGifStartFrame() > frameGrabber.getLengthInFrames()
                    & (videoSnapshotValue.getGifStartFrame() + videoSnapshotValue.getGifFrameCount()) > frameGrabber.getLengthInFrames()) {
                throw new RuntimeException("视频太短");
            }
            FileOutputStream targetFile = new FileOutputStream(videoSnapshotValue.getOut());
            frameGrabber.setFrameNumber(videoSnapshotValue.getGifStartFrame());
            AnimatedGifEncoder en = new AnimatedGifEncoder();
            en.setFrameRate(videoSnapshotValue.getGifFrameRate());
            en.start(targetFile);
            int frameNumber = videoSnapshotValue.getGifStartFrame();
            for (int i = 0; i < videoSnapshotValue.getGifFrameCount(); i++) {
                en.addFrame(converter.convert(frameGrabber.grabImage()));
                frameNumber = frameNumber + videoSnapshotValue.getGifMargin();
                frameGrabber.setFrameNumber(frameNumber);
            }
            en.finish();
            videoSnapshotValue.setExecute(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new VideoHandleException(e);
        } finally {
            if (ObjectUtil.isNotNull(frameGrabber)) {
                try {
                    frameGrabber.close();
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    /**
     * 输出到文本
     *
     * @param fullNamePath
     * @return
     */
    public File toFile(String fullNamePath) {
        VideoSnapshotValue videoSnapshotValue = getVideoSnapshot();
        if (videoSnapshotValue.isExecute()) {
            return FileUtil.copy(videoSnapshotValue.getOut(), new File(fullNamePath), true);
        }
        return null;
    }

    /**
     * 输出到文本
     *
     * @param outFile
     * @return
     */
    public File toFile(File outFile) {
        VideoSnapshotValue videoSnapshotValue = getVideoSnapshot();
        if (videoSnapshotValue.isExecute()) {
            return FileUtil.copy(videoSnapshotValue.getOut(), outFile, true);
        }
        return null;
    }

    /**
     * 输出到inputStream
     *
     * @return
     */
    public FileInputStream toInputStream() throws FileNotFoundException {
        VideoSnapshotValue videoSnapshotValue = getVideoSnapshot();
        if (videoSnapshotValue.isExecute()) {
            return new FileInputStream(videoSnapshotValue.getOut());
        }
        return null;
    }

    /**
     * 输出到OutputStream
     *
     * @return
     */
    public FileOutputStream toOutputStream() throws FileNotFoundException {
        VideoSnapshotValue videoSnapshotValue = getVideoSnapshot();
        if (videoSnapshotValue.isExecute()) {
            return new FileOutputStream(videoSnapshotValue.getOut());
        }
        return null;
    }

    public static class Builder {

        /**
         * 从文本读入
         *
         * @param file
         * @return
         */
        public static VideoUtil ofFile(File file) {
            try {
                return new VideoUtil(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                throw new ImageHandleException(e);
            }
        }

        /**
         * 从文本读入
         *
         * @param filePath
         * @return
         */
        public static VideoUtil ofFile(String filePath) {
            try {
                return new VideoUtil(new FileInputStream(filePath));
            } catch (FileNotFoundException e) {
                throw new ImageHandleException(e);
            }
        }

        /**
         * 从InputStream读入
         *
         * @param inStream
         * @return
         */
        public static VideoUtil of(InputStream inStream) {
            return new VideoUtil(inStream);
        }

        /**
         * 从二进制数组读入
         *
         * @param bytes
         * @return
         */
        public static VideoUtil of(byte[] bytes) {
            return new VideoUtil(bytes);
        }

        /**
         * 从http url读入
         *
         * @param url
         * @return
         */
        public static VideoUtil ofUrl(URL url) throws IOException {
            if (ObjectUtil.isNotNull(url) && StrUtil.isNotBlank(url.toString())) {
                InputStream inStream = StreamUtil.getInputStream(url);
                if (ObjectUtil.isNotNull(inStream)) {
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = inStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    inStream.close();
                    return new VideoUtil(outStream.toByteArray());
                }
            }
            return null;
        }

        /**
         * 从http url读入
         *
         * @param url
         * @return
         */
        public static VideoUtil ofUrl(String url) throws IOException {
            return ofUrl(new URL(url));
        }

    }

}
