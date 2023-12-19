package com.skrstop.framework.components.util.media.image;

import com.skrstop.framework.components.util.constant.ImageTypeConst;
import com.skrstop.framework.components.util.media.exception.image.ImageHandleException;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import com.skrstop.framework.components.util.value.stream.StreamUtil;
import lombok.Getter;
import lombok.Setter;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.List;

/**
 * 图片处理工具类
 *
 * @author 蒋时华
 * @date 2019/1/21
 */
@Deprecated
public class ImageUtil {

    public static List<String> SUPPORT_FORMAT = CollectionUtil.newArrayList(
            ImageTypeConst.JPG_FORMAT_NAME,
            ImageTypeConst.JPEG_FORMAT_NAME,
            ImageTypeConst.PNG_FORMAT_NAME,
            ImageTypeConst.BMP_FORMAT_NAME,
            ImageTypeConst.WBMP_FORMAT_NAME,
            ImageTypeConst.GIF_FORMAT_NAME
    );
    public static String DEFAULT_FORMAT_NAME = ImageTypeConst.JPG_FORMAT_NAME;
    private ThreadLocal<ImageSnapshotValue> imageSnapshot = new ThreadLocal<>();

    public ImageUtil(InputStream targetInStream) {
        this(StreamUtil.inputStreamToByte(targetInStream));
    }

    public ImageUtil(byte[] bytes) {
        ImageSnapshotValue imageSnapshotValue = new ImageSnapshotValue();
        imageSnapshotValue.setSourceBytes(bytes);
        try {
            imageSnapshotValue.setTargetBuffedImage(ImageIO.read(new ByteArrayInputStream(bytes)));
        } catch (IOException e) {
            throw new ImageHandleException(e);
        }
        this.imageSnapshot.set(imageSnapshotValue);
    }

    /**
     * 1080P 分辨率处理
     *
     * @return
     * @throws IOException
     */
    public ImageUtil compress1080() {
        return compress(1080);
    }

    /**
     * 720P 分辨率处理
     *
     * @return
     */
    public ImageUtil compress720() {
        return compress(720);
    }

    public ImageUtil compress(int size) {
        try {
            ImageSnapshotValue imageSnapshot = this.imageSnapshot.get();
            int width = imageSnapshot.getTargetBuffedImage().getWidth();
            int height = imageSnapshot.getTargetBuffedImage().getHeight();
            if (width >= height) {
                return this.compressProportionWidth(size);
            } else {
                return this.compressProportionHeight(size);
            }
        } catch (Exception e) {
            throw new ImageHandleException(e);
        }
    }

    /**
     * 按宽分辨率比例缩放
     */
    public ImageUtil compressProportionWidth(Integer width) {
        try {
            ImageSnapshotValue imageSnapshot = this.imageSnapshot.get();
            BufferedImage bufferedImage = Thumbnails.of(imageSnapshot.getTargetBuffedImage())
                    .width(width)
                    .keepAspectRatio(true)
                    .asBufferedImage();
            imageSnapshot.setTargetBuffedImage(bufferedImage);
            return this;
        } catch (Exception e) {
            throw new ImageHandleException(e);
        }
    }

    /**
     * 按高分辨率比例缩放
     */
    public ImageUtil compressProportionHeight(Integer height) {
        try {
            ImageSnapshotValue imageSnapshot = this.imageSnapshot.get();
            BufferedImage bufferedImage = Thumbnails.of(imageSnapshot.getTargetBuffedImage())
                    .height(height)
                    .keepAspectRatio(true)
                    .asBufferedImage();
            imageSnapshot.setTargetBuffedImage(bufferedImage);
            return this;
        } catch (Exception e) {
            throw new ImageHandleException(e);
        }
    }

    /**
     * 旋转图片
     *
     * @param totate + 顺时针旋转角度 - 逆时针旋转角度
     * @return
     */
    public ImageUtil rotate(double totate) {
        try {
            ImageSnapshotValue imageSnapshot = this.imageSnapshot.get();
            BufferedImage bufferedImage = Thumbnails.of(imageSnapshot.getTargetBuffedImage())
                    .scale(1D)
                    .rotate(totate)
                    .asBufferedImage();
            imageSnapshot.setTargetBuffedImage(bufferedImage);
            return this;
        } catch (Exception e) {
            throw new ImageHandleException(e);
        }
    }

    /**
     * 按照指定的尺寸进行缩放，不遵循原图大小
     *
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    public ImageUtil size(Integer width, Integer height) {
        try {
            ImageSnapshotValue imageSnapshot = this.imageSnapshot.get();
            BufferedImage bufferedImage = Thumbnails.of(imageSnapshot.getTargetBuffedImage())
                    .size(width, height)
                    .keepAspectRatio(false)
                    .asBufferedImage();
            imageSnapshot.setTargetBuffedImage(bufferedImage);
            return this;
        } catch (Exception e) {
            throw new ImageHandleException(e);
        }
    }

    /**
     * 按比例缩放
     *
     * @param scale
     * @return
     * @throws IOException
     */
    public ImageUtil scale(double scale) {
        try {
            ImageSnapshotValue imageSnapshot = this.imageSnapshot.get();
            BufferedImage bufferedImage = Thumbnails.of(imageSnapshot.getTargetBuffedImage())
                    .scale(scale)
                    .asBufferedImage();
            imageSnapshot.setTargetBuffedImage(bufferedImage);
            return this;
        } catch (Exception e) {
            throw new ImageHandleException(e);
        }
    }

    /**
     * 相对裁剪
     *
     * @param position 中心点：Positions.CENTER, Positions.TOP_CENTER ... ...
     * @param width    裁剪的宽
     * @param height   裁剪的高
     * @return
     * @throws IOException
     */
    public ImageUtil sourceRegion(Position position, Integer width, Integer height) {
        try {
            ImageSnapshotValue imageSnapshot = this.imageSnapshot.get();
            BufferedImage bufferedImage = Thumbnails.of(imageSnapshot.getTargetBuffedImage())
                    .sourceRegion(position, width, height)
                    .scale(1D)
                    .asBufferedImage();
            imageSnapshot.setTargetBuffedImage(bufferedImage);
            return this;
        } catch (Exception e) {
            throw new ImageHandleException(e);
        }
    }

    /**
     * 绝对裁剪
     *
     * @param leftTopX
     * @param leftTopY
     * @param rightBottomX
     * @param rightBottomY
     * @return
     * @throws IOException
     */
    public ImageUtil sourceRegion(Integer leftTopX, Integer leftTopY, Integer rightBottomX, Integer rightBottomY) {
        try {
            ImageSnapshotValue imageSnapshot = this.imageSnapshot.get();
            BufferedImage bufferedImage = Thumbnails.of(imageSnapshot.getTargetBuffedImage())
                    .sourceRegion(leftTopX, leftTopY, rightBottomX, rightBottomY)
                    .scale(1D)
                    .asBufferedImage();
            imageSnapshot.setTargetBuffedImage(bufferedImage);
            return this;
        } catch (Exception e) {
            throw new ImageHandleException(e);
        }
    }

    /**
     * 添加图片水印
     *
     * @param positions      位值：Positions.CENTER, Positions.TOP_CENTER ... ...
     * @param waterImagePath 水印图片路径
     * @param opacity        水印图片透明度
     * @return
     * @throws IOException
     */
    public ImageUtil waterMarkImage(Positions positions, String waterImagePath, float opacity) {
        try {
            ImageSnapshotValue imageSnapshot = this.imageSnapshot.get();
            BufferedImage bufferedImage = Thumbnails.of(imageSnapshot.getTargetBuffedImage())
                    .scale(1D)
                    .watermark(positions, ImageIO.read(new File(waterImagePath)), opacity)
                    .asBufferedImage();
            imageSnapshot.setTargetBuffedImage(bufferedImage);
            return this;
        } catch (Exception e) {
            throw new ImageHandleException(e);
        }
    }

    /**
     * 添加图片水印
     *
     * @param positions 位值：Positions.CENTER, Positions.TOP_CENTER ... ...
     * @param url       水印图片url
     * @param opacity   水印图片透明度
     * @return
     */
    public ImageUtil waterMarkImageUrl(Positions positions, String url, float opacity) {
        try {
            ImageSnapshotValue imageSnapshot = this.imageSnapshot.get();
            BufferedImage bufferedImage = Thumbnails.of(imageSnapshot.getTargetBuffedImage())
                    .scale(1D)
                    .watermark(positions, ImageIO.read(new URL(url)), opacity)
                    .asBufferedImage();
            imageSnapshot.setTargetBuffedImage(bufferedImage);
            return this;
        } catch (Exception e) {
            throw new ImageHandleException(e);
        }
    }

    /**
     * 添加图片水印
     *
     * @param positions  位值：Positions.CENTER, Positions.TOP_CENTER ... ...
     * @param waterImage 水印图片流
     * @param opacity    水印图片透明度
     * @return
     * @throws IOException
     */
    public ImageUtil waterMarkImage(Positions positions, InputStream waterImage, float opacity) {
        try {
            ImageSnapshotValue imageSnapshot = this.imageSnapshot.get();
            BufferedImage bufferedImage = Thumbnails.of(imageSnapshot.getTargetBuffedImage())
                    .scale(1D)
                    .watermark(positions, ImageIO.read(waterImage), opacity)
                    .asBufferedImage();
            imageSnapshot.setTargetBuffedImage(bufferedImage);
            return this;
        } catch (Exception e) {
            throw new ImageHandleException(e);
        }
    }

    /**
     * 添加图片水印
     *
     * @param positions  位值：Positions.CENTER, Positions.TOP_CENTER ... ...
     * @param waterImage 水印图片
     * @param opacity    水印图片透明度
     * @return
     * @throws IOException
     */
    public ImageUtil waterMarkImage(Positions positions, File waterImage, float opacity) {
        try {
            ImageSnapshotValue imageSnapshot = this.imageSnapshot.get();
            BufferedImage bufferedImage = Thumbnails.of(imageSnapshot.getTargetBuffedImage())
                    .scale(1D)
                    .watermark(positions, ImageIO.read(waterImage), opacity)
                    .asBufferedImage();
            imageSnapshot.setTargetBuffedImage(bufferedImage);
            return this;
        } catch (Exception e) {
            throw new ImageHandleException(e);
        }
    }

    /**
     * 添加图片水印
     *
     * @param positions  位值：Positions.CENTER, Positions.TOP_CENTER ... ...
     * @param waterImage 水印图片
     * @param opacity    水印图片透明度
     * @return
     */
    public ImageUtil waterMarkImage(Positions positions, BufferedImage waterImage, float opacity) {
        try {
            ImageSnapshotValue imageSnapshot = this.imageSnapshot.get();
            BufferedImage bufferedImage = Thumbnails.of(imageSnapshot.getTargetBuffedImage())
                    .scale(1D)
                    .watermark(positions, waterImage, opacity)
                    .asBufferedImage();
            imageSnapshot.setTargetBuffedImage(bufferedImage);
            return this;
        } catch (Exception e) {
            throw new ImageHandleException(e);
        }
    }

    /**
     * 获取文件快照，只能获取一次，再次获取为null
     *
     * @return
     */
    public ImageSnapshotValue getImageSnapshot() {
        ImageSnapshotValue imageSnapshot = this.imageSnapshot.get();
        this.imageSnapshot.remove();
        return imageSnapshot;
    }

    /**
     * 指定格式和质量，输出InputStream
     *
     * @param outputQuality
     * @param outFormat
     * @return
     */
    public InputStream toInputStream(Double outputQuality, String outFormat) {
        OutputStream outputStream = toOutputStream(outputQuality, outFormat);
        return StreamUtil.parse(outputStream);
    }

    /**
     * 指定质量，输出InputStream
     *
     * @param outputQuality
     * @return
     */
    public InputStream toInputStream(Double outputQuality) {
        OutputStream outputStream = toOutputStream(outputQuality);
        return StreamUtil.parse(outputStream);
    }

    /**
     * 指定格式，输出InputStream
     *
     * @param outFormat
     * @return
     */
    public InputStream toInputStream(String outFormat) {
        OutputStream outputStream = toOutputStream(outFormat);
        return StreamUtil.parse(outputStream);
    }

    /**
     * 输出InputStream
     *
     * @return
     */
    public InputStream toInputStream() {
        OutputStream outputStream = toOutputStream();
        return StreamUtil.parse(outputStream);
    }

    /**
     * 指定格式和质量，输出OutputStream
     *
     * @param outputQuality
     * @param outFormat
     * @return
     */
    public OutputStream toOutputStream(Double outputQuality, String outFormat) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageSnapshotValue imageSnapshot = this.getImageSnapshot();
            Thumbnails.Builder<BufferedImage> bufferedImageBuilder = Thumbnails.of(imageSnapshot.getTargetBuffedImage())
                    .width(imageSnapshot.getTargetBuffedImage().getWidth())
                    .height(imageSnapshot.getTargetBuffedImage().getHeight());
            if (ObjectUtil.isNotNull(outputQuality)) {
                bufferedImageBuilder.outputQuality(outputQuality);
            }
            bufferedImageBuilder.outputFormat(outFormat);
            bufferedImageBuilder.toOutputStream(byteArrayOutputStream);
            return byteArrayOutputStream;
        } catch (IOException e) {
            throw new ImageHandleException(e);
        }
    }

    /**
     * 指定质量，输出OutputStream
     *
     * @param outputQuality
     * @return
     */
    public OutputStream toOutputStream(Double outputQuality) {
        return toOutputStream(outputQuality, DEFAULT_FORMAT_NAME);
    }

    /**
     * 指定格式，输出OutputStream
     *
     * @param outFormat
     * @return
     */
    public OutputStream toOutputStream(String outFormat) {
        return toOutputStream(null, outFormat);
    }

    /**
     * 输出OutputStream
     *
     * @return
     */
    public OutputStream toOutputStream() {
        return toOutputStream(null, DEFAULT_FORMAT_NAME);
    }

    /**
     * 指定格式和质量，输出文件
     *
     * @param outputQuality
     * @param outFormat
     * @return
     */
    public void toFile(String fileFullPath, Double outputQuality, String outFormat) {
        try {
            ImageSnapshotValue imageSnapshot = this.getImageSnapshot();
            Thumbnails.Builder<BufferedImage> bufferedImageBuilder = Thumbnails.of(imageSnapshot.getTargetBuffedImage())
                    .width(imageSnapshot.getTargetBuffedImage().getWidth())
                    .height(imageSnapshot.getTargetBuffedImage().getHeight());
            if (ObjectUtil.isNotNull(outputQuality)) {
                bufferedImageBuilder.outputQuality(outputQuality);
            }
            bufferedImageBuilder.outputFormat(outFormat);
            bufferedImageBuilder.toFile(fileFullPath);
        } catch (Exception e) {
            throw new ImageHandleException(e);
        }
    }

    /**
     * 指定格式和质量，输出文件
     *
     * @param outputQuality
     * @param outFormat
     * @return
     */
    public void toFile(File file, Double outputQuality, String outFormat) {
        try {
            ImageSnapshotValue imageSnapshot = this.getImageSnapshot();
            Thumbnails.Builder<BufferedImage> bufferedImageBuilder = Thumbnails.of(imageSnapshot.getTargetBuffedImage())
                    .width(imageSnapshot.getTargetBuffedImage().getWidth())
                    .height(imageSnapshot.getTargetBuffedImage().getHeight());
            if (ObjectUtil.isNotNull(outputQuality)) {
                bufferedImageBuilder.outputQuality(outputQuality);
            }
            bufferedImageBuilder.outputFormat(outFormat);
            bufferedImageBuilder.toFile(file);
        } catch (Exception e) {
            throw new ImageHandleException(e);
        }
    }

    /**
     * 输出文件
     *
     * @return
     */
    public void toFile(String fileFullPath) {
        toFile(fileFullPath, null, DEFAULT_FORMAT_NAME);
    }

    /**
     * 输出文件
     *
     * @return
     */
    public void toFile(File file) {
        toFile(file, null, DEFAULT_FORMAT_NAME);
    }

    /**
     * 指定质量，输出文件
     *
     * @param outputQuality
     * @return
     */
    public void toFile(String fileFullPath, double outputQuality) {
        toFile(fileFullPath, outputQuality, DEFAULT_FORMAT_NAME);
    }

    /**
     * 指定质量，输出文件
     *
     * @param outputQuality
     * @return
     */
    public void toFile(File file, double outputQuality) {
        toFile(file, outputQuality, DEFAULT_FORMAT_NAME);
    }

    /**
     * 指定格式，输出文件
     *
     * @param outFormat
     * @return
     */
    public void toFile(String fileFullPath, String outFormat) {
        toFile(fileFullPath, null, outFormat);
    }

    /**
     * 指定格式，输出文件
     *
     * @param outFormat
     * @return
     */
    public void toFile(File file, String outFormat) {
        toFile(file, null, outFormat);
    }

    /**
     * ImageUtil 建造器
     */
    public static class Builder {

        /**
         * 从文件读入
         *
         * @param file
         * @return
         */
        public static ImageUtil ofFile(File file) {
            try {
                return new ImageUtil(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                throw new ImageHandleException(e);
            }
        }

        /**
         * 从文件读入
         *
         * @param filePath
         * @return
         */
        public static ImageUtil ofFile(String filePath) {
            try {
                return new ImageUtil(new FileInputStream(filePath));
            } catch (FileNotFoundException e) {
                throw new ImageHandleException(e);
            }
        }

        /**
         * 从输入流读入
         *
         * @param inStream
         * @return
         */
        public static ImageUtil of(InputStream inStream) {
            return new ImageUtil(inStream);
        }

        /**
         * 从二进制数组读入
         *
         * @param bytes
         * @return
         */
        public static ImageUtil of(byte[] bytes) {
            return new ImageUtil(bytes);
        }

        /**
         * 从http url读入
         *
         * @param url
         * @return
         * @throws IOException
         */
        public static ImageUtil ofUrl(URL url) throws IOException {
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
                    return new ImageUtil(outStream.toByteArray());
                }
            }
            return null;
        }

        /**
         * 从http url读入
         *
         * @param url
         * @return
         * @throws IOException
         */
        public static ImageUtil ofUrl(String url) throws IOException {
            return ofUrl(new URL(url));
        }

    }

    @Setter
    @Getter
    public static class ImageSnapshotValue {
        /*** 原图 */
        private byte[] sourceBytes;
        /*** 处理后的图片 */
        private BufferedImage targetBuffedImage;
    }

}
