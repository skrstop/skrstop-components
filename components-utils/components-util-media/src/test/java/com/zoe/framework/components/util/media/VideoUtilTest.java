package com.zoe.framework.components.util.media;

import com.zoe.framework.components.util.constant.ImageTypeConst;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

/**
 * @author 蒋时华
 * @date 2023-11-30 13:59:48
 */
public class VideoUtilTest {

    @Test
    @Ignore
    public void test() {
        File file = new File("/Users/zoe/Downloads/1.mp4");
        File out = new File("/Users/zoe/Downloads/11.gif");
        long start = System.currentTimeMillis();
        com.zoe.framework.components.util.media.video.VideoUtil.Builder.ofFile(file)
                .format(ImageTypeConst.JPG_FORMAT_NAME)
                .p720()
                .audioQuality(35.0)
                .videoQuality(35.0)
//                .executeVideo()
//                .executeVideoCover()
//                .gifStartFrame(5)
//                .gifFrameCount(15)
//                .gifFrameRate(3)
                .executeVideoGif()
                .toFile(out);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

}
