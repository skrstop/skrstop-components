package com.skrstop.framework.components.util.media;

import cn.hutool.core.io.FileUtil;
import com.skrstop.framework.components.util.media.image.ImageUtil;
import net.coobird.thumbnailator.geometry.Positions;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.skrstop.framework.components.util.constant.ImageTypeConst.JPG_FORMAT_NAME;

/**
 * @author 蒋时华
 * @date 2023-11-30 13:56:37
 */
public class ImageUtilTest {

    @Test
    @Ignore
    public void test() throws IOException {
        File file = new File("/Users/skrstop/test/abcdefg.png");
        File out = new File("/Users/skrstop/test/test.jpg");

        ImageUtil.ImageSnapshotValue imageSnapshot = ImageUtil.Builder.ofUrl("http://xxx.xxx.com/storage/xxx/xxx/xxx.jpg")
                .rotate(-45)
                .scale(1.2)
                .getImageSnapshot();


        ImageUtil.Builder.ofFile(file)
                .scale(0.2)
                .waterMarkImage(Positions.CENTER, imageSnapshot.getTargetBuffedImage(), 0.3F)
                .toFile(file);

        InputStream inputStream = ImageUtil.Builder.ofFile(file).toInputStream(0.05, JPG_FORMAT_NAME);
        FileUtil.writeFromStream(inputStream, out);

        ImageUtil.Builder.ofFile(file).rotate(90).scale(0.5).compress(720).toFile(out, 0.5, JPG_FORMAT_NAME);

//        InputStream inputStream = ImageUtil.Builder.ofFile(file).rotate(90).scale(0.5).compress(720).toInputStream(0.5, JPG_FORMAT_NAME);
//        FileUtil.writeFromStream(inputStream, out);
    }

}
