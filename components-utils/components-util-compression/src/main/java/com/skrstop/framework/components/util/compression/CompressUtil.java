package com.skrstop.framework.components.util.compression;

import com.google.common.collect.Lists;
import com.skrstop.framework.components.util.compression.bean.CompressionType;
import com.skrstop.framework.components.util.compression.bean.ZipLevel;
import com.skrstop.framework.components.util.compression.compressor.FileCompressor;
import com.skrstop.framework.components.util.compression.exception.CompressException;
import com.skrstop.framework.components.util.compression.tar.TarUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

/**
 * Compress Kit
 *
 * @author 蒋时华
 * @version v1.0 - 07/09/2018.
 */
@UtilityClass
public final class CompressUtil {

    private static final Logger log = LoggerFactory.getLogger(CompressUtil.class);

    /**
     * 将文件夹打包成 *.tar
     *
     * @param directory   文件夹
     * @param destTarPath Tar 包存储路径
     * @throws Exception compress exception
     */
    public static void compressTar(String directory, String destTarPath) throws Exception {
        TarUtils.tarFolder(directory, destTarPath);
    }

    /**
     * 压缩 ZIP 文件
     *
     * @param destZipFilePath zip存储路径
     * @param zipFileParams   待压缩文件列表
     */
    public static void compressZip(String destZipFilePath, List<ZipFileParams> zipFileParams) {
        try {
            if (zipFileParams != null && zipFileParams.size() > 0) {
                FileCompressor fileCompressor = new FileCompressor();
                for (ZipFileParams zipFileParam : zipFileParams) {
                    fileCompressor.add(
                            zipFileParam.getOriginalFilePath(), zipFileParam.getZipInnerRelativePath());
                }
                fileCompressor.setType(CompressionType.ZIP);
                fileCompressor.setLevel(ZipLevel.NORMAL);
                fileCompressor.setCompressedPath(destZipFilePath);
                fileCompressor.compress();
            }
        } catch (Exception e) {
            log.error("文件压缩异常", e);
            throw new CompressException(e);
        }
    }

    /**
     * 压缩 Zip 文件
     *
     * @param destZipFilePath     zip 文件存储路径
     * @param sourceFileDirectory 源文件跟文件夹
     */
    public static void compressZip(String destZipFilePath, String sourceFileDirectory) {
        compressZip(destZipFilePath, parseDirectory(sourceFileDirectory, null));
    }

    /**
     * 压缩 Zip 文件
     *
     * @param destZipFilePath     zip 文件存储路径
     * @param sourceFileDirectory 源文件跟文件夹
     * @param fileExtension       文件后缀
     */
    public static void compressZip(
            String destZipFilePath, String sourceFileDirectory, String fileExtension) {
        compressZip(destZipFilePath, parseDirectory(sourceFileDirectory, fileExtension));
    }

    /**
     * 解析文件夹
     *
     * @param baseDir 文件夹
     * @return list
     */
    public static List<ZipFileParams> parseDirectory(String baseDir, String fileExtension) {

        List<ZipFileParams> params = Lists.newArrayList();
        if (StringUtils.isNoneBlank(baseDir)) {
            if (!StringUtils.endsWith(baseDir, "/")) {
                baseDir = baseDir + "/";
            }
            if (Files.isDirectory(Paths.get(baseDir))) {
                Collection collection = null;
                if (StringUtils.isNoneBlank(fileExtension)) {
                    collection =
                            FileUtils.listFiles(Paths.get(baseDir).toFile(), new String[]{fileExtension}, true);
                } else {
                    collection = FileUtils.listFiles(Paths.get(baseDir).toFile(), null, true);
                }
                for (Object o : collection) {
                    if (o instanceof File) {
                        File item = (File) o;
                        if (!item.isHidden()) {
                            if (!item.isDirectory()) {
                                params.add(
                                        ZipFileParams.builder()
                                                .originalFilePath(item.getAbsolutePath())
                                                .zipInnerRelativePath(item.getAbsolutePath().replace(baseDir, ""))
                                                .build());
                            }
                        }
                    }
                }
            }
        }
        return params;
    }

    /**
     * 待压缩文件的参数
     *
     * @author 蒋时华
     */
    @Getter
    @Setter
    public static class ZipFileParams implements Serializable {
        @Serial
        private static final long serialVersionUID = -4509945208814944712L;

        /**
         * 文件原始路径
         *
         * <pre>
         *
         * </pre>
         */
        private String originalFilePath;

        /**
         * 压缩文件内的目录结构,相对位置
         *
         * <pre>
         *
         *
         * </pre>
         */
        private String zipInnerRelativePath;

        @Builder
        public ZipFileParams(String originalFilePath, String zipInnerRelativePath) {
            this.originalFilePath = originalFilePath;
            this.zipInnerRelativePath = zipInnerRelativePath;
        }
    }
}
