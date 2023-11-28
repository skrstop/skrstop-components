package com.jphoebe.framework.components.util.compression.processor;

import com.jphoebe.framework.components.util.compression.bean.BinaryFile;
import com.jphoebe.framework.components.util.compression.compressor.CompressProcessor;
import com.jphoebe.framework.components.util.compression.compressor.FileCompressor;
import com.jphoebe.framework.components.util.compression.utils.CompressLoggerKits;
import com.jphoebe.framework.components.util.compression.utils.FileKits;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * tar processor
 *
 * @author 蒋时华
 */
@SuppressWarnings("Duplicates")
public class TarProcessor implements CompressProcessor {

    @Override
    public byte[] compressData(FileCompressor fileCompressor) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream aos = new TarArchiveOutputStream(baos);
        try {
            for (BinaryFile binaryFile : fileCompressor.getMapBinaryFile().values()) {
                TarArchiveEntry entry = new TarArchiveEntry(binaryFile.getDesPath());
                entry.setSize(binaryFile.getActualSize());
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

    @Override
    public void read(String srcPath, FileCompressor fileCompressor) throws Exception {
        long t1 = System.currentTimeMillis();
        byte[] data = FileKits.convertFileToByte(srcPath);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        TarArchiveInputStream ais = new TarArchiveInputStream(bais);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int readByte;
            TarArchiveEntry entry = ais.getNextTarEntry();
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
                entry = ais.getNextTarEntry();
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
