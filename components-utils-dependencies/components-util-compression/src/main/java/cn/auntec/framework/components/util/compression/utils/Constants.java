package cn.auntec.framework.components.util.compression.utils;

import cn.auntec.framework.components.util.compression.bean.CompressionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Compress Constants
 *
 * @author 蒋时华
 */
public class Constants {

    // Delete Source
    public static class SOURCE {
        public static final boolean DELETE = true;
        public static final boolean KEEP = false;
    }

    // Allow file types
    public static class FILE_TYPES_ALLOW {
        public static final List<CompressionType> COMPRESSING = new ArrayList<CompressionType>();
        public static final List<CompressionType> DECOMPRESSING = new ArrayList<CompressionType>();

        static {
            // Compressing
            COMPRESSING.add(CompressionType.ZIP);
            COMPRESSING.add(CompressionType.GZ);
            COMPRESSING.add(CompressionType.TAR);
            COMPRESSING.add(CompressionType.TAR_GZ);
            COMPRESSING.add(CompressionType.JAR);
            COMPRESSING.add(CompressionType.BZ2);
            COMPRESSING.add(CompressionType.TAR_BZ2);
            COMPRESSING.add(CompressionType.AR);
            COMPRESSING.add(CompressionType.CPIO);
            COMPRESSING.add(CompressionType.XZ);
            // Deompressing
            DECOMPRESSING.add(CompressionType.ZIP);
            DECOMPRESSING.add(CompressionType.GZ);
            DECOMPRESSING.add(CompressionType.TAR);
            DECOMPRESSING.add(CompressionType.TAR_GZ);
            DECOMPRESSING.add(CompressionType.JAR);
            DECOMPRESSING.add(CompressionType.BZ2);
            DECOMPRESSING.add(CompressionType.TAR_BZ2);
            DECOMPRESSING.add(CompressionType.AR);
            DECOMPRESSING.add(CompressionType.CPIO);
            DECOMPRESSING.add(CompressionType.SEVENZIP);
            DECOMPRESSING.add(CompressionType.XZ);
        }
    }
}
