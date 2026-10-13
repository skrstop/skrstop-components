package com.skrstop.framework.components.util.compression.utils;

import com.skrstop.framework.components.util.compression.bean.BinaryFile;
import com.skrstop.framework.components.util.compression.bean.CompressionType;
import com.skrstop.framework.components.util.compression.compressor.FileCompressor;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

/**
 * @author 蒋时华
 */
public class CompressLoggerKitsTest {

    @Test
    public void testAllLogs() throws Exception {
        File tmp = Files.createTempDirectory("logkit").toFile();
        File src = new File(tmp, "s.txt");
        Files.write(src.toPath(), "x".getBytes());
        File zip = new File(tmp, "o.zip");

        FileCompressor compressor = new FileCompressor();
        compressor.setType(CompressionType.ZIP);
        compressor.setCompressedPath(zip.getAbsolutePath());

        CompressLoggerKits.createFileLog(compressor);
        // 空 path 分支
        compressor.setCompressedPath("");
        CompressLoggerKits.createFileLog(compressor);
        CompressLoggerKits.createStartCompressingLog(compressor);
        CompressLoggerKits.createFinishCompressingLog(compressor, 1L, 2L);
        CompressLoggerKits.createStartDecompressingLog(compressor);
        CompressLoggerKits.createFinishDecompressingLog(compressor, 1L, 2L);
        CompressLoggerKits.createDeleteAllSourcesLog(compressor, 1L, 2L);

        BinaryFile bf = new BinaryFile(src.getAbsolutePath(), "s.txt", false);
        CompressLoggerKits.createAddFileLog(compressor, bf, 1L, 2L);
        CompressLoggerKits.createRemoveFileLog(compressor, bf, 1L, 2L);
        CompressLoggerKits.createExtractLog(compressor, bf, 1L, 2L);
        CompressLoggerKits.createReadLog(compressor, "s.txt", 1L, 1L, 2L);
        CompressLoggerKits.createFileNotFoundLog(null, compressor, "s.txt");
        CompressLoggerKits.createMemoryLog();
        CompressLoggerKits.createFileTypeNotSupportLog("a.unknown");

        Assert.assertNotNull(compressor);
        delete(tmp);
    }

    private static void delete(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
        file.delete();
    }

}
