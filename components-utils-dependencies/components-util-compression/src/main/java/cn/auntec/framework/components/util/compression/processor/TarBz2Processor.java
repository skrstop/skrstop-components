package cn.auntec.framework.components.util.compression.processor;

import cn.auntec.framework.components.util.compression.bean.BinaryFile;
import cn.auntec.framework.components.util.compression.compressor.CompressProcessor;
import cn.auntec.framework.components.util.compression.compressor.FileCompressor;
import cn.auntec.framework.components.util.compression.utils.CompressLoggerKits;
import cn.auntec.framework.components.util.compression.utils.FileKits;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * tar bz2 processor
 *
 * @author 蒋时华
 */
@SuppressWarnings("Duplicates")
public class TarBz2Processor implements CompressProcessor {

    @Override
    public byte[] compressData(FileCompressor fileCompressor) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BZip2CompressorOutputStream cos = new BZip2CompressorOutputStream(baos);
        TarArchiveOutputStream aos = new TarArchiveOutputStream(cos);
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
            cos.close();
            baos.close();
        }
        return baos.toByteArray();
    }

    @Override
    public void read(String srcPath, FileCompressor fileCompressor) throws Exception {
        long t1 = System.currentTimeMillis();
        byte[] data = FileKits.convertFileToByte(srcPath);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        BZip2CompressorInputStream cis = new BZip2CompressorInputStream(bais);
        TarArchiveInputStream ais = new TarArchiveInputStream(cis);
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
            cis.close();
            bais.close();
        }
        CompressLoggerKits.createReadLog(
                fileCompressor, srcPath, data.length, t1, System.currentTimeMillis());
    }
}
