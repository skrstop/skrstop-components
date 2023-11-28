package com.jphoebe.framework.components.util.compression.processor;

import com.jphoebe.framework.components.util.compression.bean.BinaryFile;
import com.jphoebe.framework.components.util.compression.compressor.CompressProcessor;
import com.jphoebe.framework.components.util.compression.compressor.FileCompressor;
import com.jphoebe.framework.components.util.compression.utils.CompressLoggerKits;
import com.jphoebe.framework.components.util.compression.utils.FileKits;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 7z processor
 *
 * @author 蒋时华
 */
@SuppressWarnings("Duplicates")
public class SevenZipProcessor implements CompressProcessor {

    @Override
    public byte[] compressData(FileCompressor fileCompressor) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SevenZOutputFile sevenZOutput =
                new SevenZOutputFile(new File(fileCompressor.getCompressedPath()));
        try {
            for (BinaryFile binaryFile : fileCompressor.getMapBinaryFile().values()) {
                SevenZArchiveEntry entry =
                        sevenZOutput.createArchiveEntry(
                                new File(binaryFile.getSrcPath()), binaryFile.getDesPath());
                entry.setSize(binaryFile.getActualSize());
                sevenZOutput.putArchiveEntry(entry);
                sevenZOutput.write(binaryFile.getData());
                sevenZOutput.closeArchiveEntry();
            }
            sevenZOutput.finish();
        } catch (Exception e) {
            FileCompressor.LOGGER.error("Error on compress data", e);
        } finally {
            sevenZOutput.close();
            baos.close();
        }
        return baos.toByteArray();
    }

    @Override
    public void read(String srcPath, FileCompressor fileCompressor) throws Exception {
        long t1 = System.currentTimeMillis();
        byte[] data = FileKits.convertFileToByte(srcPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SevenZFile sevenZFile = new SevenZFile(new File(srcPath));
        try {
            byte[] buffer = new byte[1024];
            int readByte;
            SevenZArchiveEntry entry = sevenZFile.getNextEntry();
            while (entry != null && entry.getSize() > 0) {
                long t2 = System.currentTimeMillis();
                baos = new ByteArrayOutputStream();
                readByte = sevenZFile.read(buffer);
                while (readByte != -1) {
                    baos.write(buffer, 0, readByte);
                    readByte = sevenZFile.read(buffer);
                }
                BinaryFile binaryFile = new BinaryFile(entry.getName(), baos.toByteArray());
                fileCompressor.addBinaryFile(binaryFile);
                CompressLoggerKits.createAddFileLog(
                        fileCompressor, binaryFile, t2, System.currentTimeMillis());
                entry = sevenZFile.getNextEntry();
            }
        } catch (Exception e) {
            FileCompressor.LOGGER.error("Error on get compressor file", e);
        } finally {
            sevenZFile.close();
            baos.close();
        }
        CompressLoggerKits.createReadLog(
                fileCompressor, srcPath, data.length, t1, System.currentTimeMillis());
    }
}
