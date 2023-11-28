package cn.auntec.framework.components.util.compression.processor;

import cn.auntec.framework.components.util.compression.bean.BinaryFile;
import cn.auntec.framework.components.util.compression.compressor.CompressProcessor;
import cn.auntec.framework.components.util.compression.compressor.FileCompressor;
import cn.auntec.framework.components.util.compression.utils.CompressLoggerKits;
import cn.auntec.framework.components.util.compression.utils.FileKits;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipOutputStream;

/**
 * jar processor
 *
 * @author 蒋时华
 */
@SuppressWarnings("Duplicates")
public class JarProcessor implements CompressProcessor {

    @Override
    public byte[] compressData(FileCompressor fileCompressor) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JarOutputStream jos = new JarOutputStream(baos);
        try {
            jos.setLevel(fileCompressor.getLevel().getValue());
            jos.setMethod(ZipOutputStream.DEFLATED);
            jos.setComment(fileCompressor.getComment());
            for (BinaryFile binaryFile : fileCompressor.getMapBinaryFile().values()) {
                jos.putNextEntry(new JarEntry(binaryFile.getDesPath()));
                jos.write(binaryFile.getData());
                jos.closeEntry();
            }
            jos.flush();
            jos.finish();
        } catch (Exception e) {
            FileCompressor.LOGGER.error("Error on compress data", e);
        } finally {
            jos.close();
            baos.close();
        }
        return baos.toByteArray();
    }

    @Override
    public void read(String srcPath, FileCompressor fileCompressor) throws Exception {
        long t1 = System.currentTimeMillis();
        byte[] data = FileKits.convertFileToByte(srcPath);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        JarInputStream zis = new JarInputStream(bais);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int readByte;
            JarEntry entry = zis.getNextJarEntry();
            while (entry != null) {
                long t2 = System.currentTimeMillis();
                baos = new ByteArrayOutputStream();
                readByte = zis.read(buffer);
                while (readByte != -1) {
                    baos.write(buffer, 0, readByte);
                    readByte = zis.read(buffer);
                }
                zis.closeEntry();
                BinaryFile binaryFile = new BinaryFile(entry.getName(), baos.toByteArray());
                fileCompressor.addBinaryFile(binaryFile);
                CompressLoggerKits.createAddFileLog(
                        fileCompressor, binaryFile, t2, System.currentTimeMillis());
                entry = zis.getNextJarEntry();
            }
        } catch (Exception e) {
            FileCompressor.LOGGER.error("Error on get compressor file", e);
        } finally {
            baos.close();
            zis.close();
            bais.close();
        }
        CompressLoggerKits.createReadLog(
                fileCompressor, srcPath, data.length, t1, System.currentTimeMillis());
    }
}
