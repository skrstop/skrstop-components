package com.zoe.framework.components.util.compression.processor;

import com.zoe.framework.components.util.compression.bean.BinaryFile;
import com.zoe.framework.components.util.compression.compressor.CompressProcessor;
import com.zoe.framework.components.util.compression.compressor.FileCompressor;
import com.zoe.framework.components.util.compression.utils.CompressLoggerKits;
import com.zoe.framework.components.util.compression.utils.FileKits;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Ar Processor
 *
 * @author 蒋时华
 */
@SuppressWarnings("Duplicates")
public class ArProcessor implements CompressProcessor {

    /**
     * Compress data
     *
     * @param fileCompressor FileCompressor object
     * @return byte array
     * @throws Exception exception
     */
    @Override
    public byte[] compressData(FileCompressor fileCompressor) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ArArchiveOutputStream aos = new ArArchiveOutputStream(baos);
        try {
            for (BinaryFile binaryFile : fileCompressor.getMapBinaryFile().values()) {
                ArArchiveEntry entry =
                        new ArArchiveEntry(binaryFile.getDesPath(), binaryFile.getActualSize());
                aos.putArchiveEntry(entry);
                aos.write(binaryFile.getData());
                aos.closeArchiveEntry();
            }
            aos.flush();
            aos.finish();
        } catch (Exception e) {
            FileCompressor.LOGGER.error("Error on compress data", e);
        } finally {
            aos.close();
            baos.close();
        }
        return baos.toByteArray();
    }

    /**
     * Read from compressed file
     *
     * @param srcPath        path of compressed file
     * @param fileCompressor FileCompressor object
     * @throws Exception
     */
    @Override
    public void read(String srcPath, FileCompressor fileCompressor) throws Exception {
        long t1 = System.currentTimeMillis();
        byte[] data = FileKits.convertFileToByte(srcPath);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ArArchiveInputStream ais = new ArArchiveInputStream(bais);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int readByte;
            ArArchiveEntry entry = ais.getNextArEntry();
            while (entry != null && entry.getSize() > 0) {
                long t2 = System.currentTimeMillis();
                baos = new ByteArrayOutputStream();
                readByte = ais.read(buffer);
                while (readByte != -1) {
                    baos.write(buffer, 0, readByte);
                    readByte = ais.read(buffer);
                }
                BinaryFile binaryFile = new BinaryFile(entry.getName(), baos.toByteArray());
                fileCompressor.addBinaryFile(binaryFile);
                CompressLoggerKits.createAddFileLog(
                        fileCompressor, binaryFile, t2, System.currentTimeMillis());
                entry = ais.getNextArEntry();
            }
        } catch (Exception e) {
            FileCompressor.LOGGER.error("Error on get compressor file", e);
        } finally {
            baos.close();
            ais.close();
            bais.close();
        }
        CompressLoggerKits.createReadLog(
                fileCompressor, srcPath, data.length, t1, System.currentTimeMillis());
    }
}
