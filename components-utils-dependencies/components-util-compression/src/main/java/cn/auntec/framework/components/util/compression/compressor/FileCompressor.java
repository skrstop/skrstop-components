package cn.auntec.framework.components.util.compression.compressor;

import cn.auntec.framework.components.util.compression.bean.BinaryFile;
import cn.auntec.framework.components.util.compression.bean.CompressionLevel;
import cn.auntec.framework.components.util.compression.bean.CompressionType;
import cn.auntec.framework.components.util.compression.utils.CompressLoggerKits;
import cn.auntec.framework.components.util.compression.utils.Constants.FILE_TYPES_ALLOW;
import cn.auntec.framework.components.util.compression.utils.Constants.SOURCE;
import cn.auntec.framework.components.util.compression.utils.FileKits;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * File Compressor
 *
 * @author 蒋时华
 */
@Getter
@Setter
public class FileCompressor implements Serializable {
    public static final Logger LOGGER = LoggerFactory.getLogger(FileCompressor.class);
    private static final long serialVersionUID = 1L;
    private Map<String, BinaryFile> mapBinaryFile = new HashMap<String, BinaryFile>();
    private CompressionType type = null;
    private CompressionLevel level = null;
    private long actualSize = 0;
    private String compressedPath;
    private long compressedSize = -1;
    private CompressionType compressedType = null;
    private String comment;

    /**
     * Create new FileCompressor
     */
    public FileCompressor() {
        CompressLoggerKits.createFileLog(this);
    }

    /*
     * --------------------APIs--------------------
     */

    /**
     * Create new FileCompressor from compressed file
     *
     * @param compressedPath path of compressed file including name
     * @return
     */
    public static FileCompressor read(String compressedPath) throws Exception {
        return read(compressedPath, getFileType(compressedPath));
    }

    /**
     * Create new FileCompressor from compressed file
     *
     * @param compressedPath path of compressed file including name
     * @param type           compressed type
     * @return
     */
    public static FileCompressor read(String compressedPath, CompressionType type) throws Exception {
        compressedPath = FileKits.getSafePath(compressedPath);
        validateCompressedPath(compressedPath);
        validateType(type);
        FileCompressor fileCompressor = new FileCompressor();
        fileCompressor.setCompressedPath(compressedPath);
        fileCompressor.setType(type);
        try {
            getCompressProcessor(type).read(compressedPath, fileCompressor);
            CompressLoggerKits.createFileLog(fileCompressor);
            return fileCompressor;
        } catch (Exception e) {
            LOGGER.error("Error on reading compressed file", e);
        }
        return null;
    }

    /**
     * Create new FileCompressor from compressed file and delete it
     *
     * @param compressedPath path of compressed file including name
     * @return
     */
    public static FileCompressor readAndDelete(String compressedPath) throws Exception {
        return readAndDelete(compressedPath, getFileType(compressedPath));
    }

    /**
     * Create new FileCompressor from compressed file and delete it
     *
     * @param compressedPath path of compressed file including name
     * @param type           compressed type
     * @return
     */
    public static FileCompressor readAndDelete(String compressedPath, CompressionType type)
            throws Exception {
        compressedPath = FileKits.getSafePath(compressedPath);
        FileCompressor fileCompressor = read(compressedPath, type);
        try {
            FileKits.delete(compressedPath);
        } catch (Exception e) {
            CompressLoggerKits.createFileNotFoundLog(
                    "Delete after reading compressed file...", fileCompressor, compressedPath);
        }
        return fileCompressor;
    }

    /**
     * Get file type
     *
     * @param compressedPath path of compressed file
     * @return
     */
    public static CompressionType getFileType(String compressedPath) throws Exception {
        compressedPath = FileKits.getSafePath(compressedPath);
        CompressionType type = null;
        CompressionType[] compressorTypes = CompressionType.values();
        for (CompressionType compressorType : compressorTypes) {
            if (FileKits.isExtension(compressedPath, compressorType.getExtension())) {
                type = compressorType;
                break;
            }
        }
        if (type == null) {
            throw new IllegalArgumentException("File type not supported. File: " + compressedPath);
        }
        return type;
    }

    /**
     * Check file type allowed for compressing
     *
     * @param type compressed type
     * @throws Exception
     */
    public static void validateAllowCompressing(CompressionType type) throws Exception {
        if (FILE_TYPES_ALLOW.COMPRESSING.contains(type)) {
            return;
        }
        throw new IllegalArgumentException("Type not support for compressing.");
    }

    /**
     * Check file type allowed for decompressing
     *
     * @param type compressed type
     * @throws Exception
     */
    public static void validateAllowDecompressing(CompressionType type) throws Exception {
        if (FILE_TYPES_ALLOW.DECOMPRESSING.contains(type)) {
            return;
        }
        throw new IllegalArgumentException("Type not support for decompressing.");
    }

    /**
     * Get CompressProcessor
     *
     * @param type compressed type
     * @return
     * @throws Exception
     */
    public static CompressProcessor getCompressProcessor(CompressionType type) throws Exception {
        CompressionType[] compressorTypes = CompressionType.values();
        for (CompressionType compressorType : compressorTypes) {
            if (compressorType.equals(type)) {
                return compressorType.getCompressProcessor();
            }
        }
        throw new IllegalArgumentException("Compress Processor not found.");
    }

    /*
     *  --------------------Base API--------------------
     */

    /**
     * Validate type
     *
     * @param type compressed type
     * @throws Exception
     */
    public static void validateType(CompressionType type) throws Exception {
        if (type == null) {
            throw new IllegalArgumentException("Type not set.");
        }
        CompressionType[] compressorTypes = CompressionType.values();
        for (CompressionType compressorType : compressorTypes) {
            if (compressorType.equals(type)) {
                return;
            }
        }
        throw new IllegalArgumentException("Invalid type.");
    }

    /**
     * Validate level
     *
     * @param type  compressed type
     * @param level compressed level
     * @throws Exception
     */
    public static void validateLevel(CompressionType type, CompressionLevel level) throws Exception {
        validateType(type);
        if (level == null) {
            throw new IllegalArgumentException("Level not set.");
        }
        int lv = level.getValue();
        if (CompressionType.ZIP.equals(type)) {
            if ((lv < -1) || (lv > 9)) {
                throw new IllegalArgumentException("Invalid compression level: " + level);
            }
        } else if (CompressionType.JAR.equals(type)) {
            if ((lv < -1) || (lv > 9)) {
                throw new IllegalArgumentException("Invalid compression level: " + level);
            }
        } else if (CompressionType.GZ.equals(type)) {
            if ((lv < -1) || (lv > 9)) {
                throw new IllegalArgumentException("Invalid compression level: " + level);
            }
        } else if (CompressionType.BZ2.equals(type)) {
            if ((lv < -1) || (lv > 9)) {
                throw new IllegalArgumentException("Invalid compression level: " + level);
            }
        }
    }

    /**
     * Validate compressed path
     *
     * @param compressedPath path of compressed file
     * @throws Exception
     */
    public static void validateCompressedPath(String compressedPath) throws Exception {
        if (compressedPath == null || compressedPath.isEmpty()) {
            throw new IllegalArgumentException("Compressed path not set.");
        }
    }

    /**
     * Validate map BinaryFile
     *
     * @param mapBinaryFile map of BinaryFiles
     * @throws Exception
     */
    public static void validateMapFile(Map<String, BinaryFile> mapBinaryFile) throws Exception {
        if (mapBinaryFile == null || mapBinaryFile.isEmpty()) {
            throw new IllegalArgumentException("No file found.");
        }
    }

    /**
     * Validate before compressing
     *
     * @throws Exception
     */
    public void validateCompress() throws Exception {
        validateCompressedPath(compressedPath);
        validateType(type);
        validateAllowCompressing(type);
        validateLevel(type, level);
        validateMapFile(mapBinaryFile);
    }

    /*
     * TODO --------------------Base functions--------------------
     */

    /**
     * Compress file
     *
     * @throws Exception
     */
    public void compress() throws Exception {
        validateCompress();
        long t1 = System.currentTimeMillis();
        CompressLoggerKits.createStartCompressingLog(this);
        CompressProcessor compressProcessor = getCompressProcessor(type);
        byte[] compressedData = compressProcessor.compressData(this);
        FileKits.convertByteToFile(compressedPath, compressedData);
        compressedSize = compressedData.length;
        compressedType = type;
        CompressLoggerKits.createFinishCompressingLog(this, t1, System.currentTimeMillis());
    }

    /**
     * Compress file and delete all sources
     *
     * @throws Exception
     */
    public void compressAndDelete() throws Exception {
        compress();
        deleteAllSources();
    }

    /**
     * Decompress
     *
     * @param decompressedPath path of decompressed folder
     * @throws Exception
     */
    public void decompress(String decompressedPath) throws Exception {
        long t1 = System.currentTimeMillis();
        CompressLoggerKits.createStartDecompressingLog(this);
        decompressedPath = FileKits.getSafePath(decompressedPath);
        File file = new File(decompressedPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        for (BinaryFile binFile : this.getMapBinaryFile().values()) {
            FileKits.convertByteToFile(
                    FileKits.getSafePath(decompressedPath + "/" + binFile.getDesPath()), binFile.getData());
        }
        CompressLoggerKits.createFinishDecompressingLog(this, t1, System.currentTimeMillis());
    }

    /**
     * Add file or folder
     *
     * @param srcPath source path or file or folder
     * @param desPath destination path of file or folder
     * @throws Exception
     */
    public void add(String srcPath, String desPath) throws Exception {
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) {
            return;
        }
        File[] files;
        if (srcFile.isFile()) {
            addFile(srcPath, desPath);
        } else {
            files = srcFile.listFiles();
            for (File file : files) {
                add(file.getPath(), desPath + "/" + file.getName());
            }
        }
    }

    /**
     * Add and delete file or folder
     *
     * @param srcPath source path or file or folder
     * @param desPath destination path of file or folder
     * @throws Exception
     */
    public void addAndDelete(String srcPath, String desPath) throws Exception {
        File srcFile = new File(srcPath);
        File[] listFiles;
        if (srcFile.exists() && srcFile.isFile()) {
            addFileAndDelete(srcPath, desPath);
        } else if (srcFile.exists() && srcFile.isDirectory()) {
            listFiles = srcFile.listFiles();
            for (File file : listFiles) {
                addAndDelete(file.getPath(), desPath + "/" + file.getName());
            }
        }
        if (srcFile.exists()) {
            srcFile.delete();
        }
    }

    /**
     * Delete all sources
     *
     * @throws Exception
     */
    public void deleteAllSources() throws Exception {
        long t1 = System.currentTimeMillis();
        for (BinaryFile binFile : mapBinaryFile.values()) {
            try {
                FileKits.deleteParent(binFile.getSrcPath());
            } catch (Exception e) {
                CompressLoggerKits.createFileNotFoundLog(
                        "Delete all sources...", this, binFile.getSrcPath());
            }
        }
        CompressLoggerKits.createDeleteAllSourcesLog(this, t1, System.currentTimeMillis());
    }

    /*
     * TODO --------------------Static methods--------------------
     */

    /**
     * Remove file
     *
     * @param srcPath path of binary file
     * @throws Exception
     */
    public void remove(String srcPath) throws Exception {
        removeFile(srcPath);
    }

    /**
     * Extract file
     *
     * @param srcPath path of file in compressed file
     * @param desPath path to extract folder
     * @throws Exception
     */
    public void extract(String srcPath, String desPath) throws Exception {
        long t1 = System.currentTimeMillis();
        srcPath = FileKits.getSafePath(srcPath);
        desPath = FileKits.getSafePath(desPath);
        File file = new File(desPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        BinaryFile binaryFile = getBinaryFile(srcPath);
        if (binaryFile == null) {
            CompressLoggerKits.createFileNotFoundLog("Extract file...", this, srcPath);
            return;
        }
        FileKits.convertByteToFile(
                FileKits.getSafePath(desPath + "/" + binaryFile.getDesPath()), binaryFile.getData());
        CompressLoggerKits.createExtractLog(this, binaryFile, t1, System.currentTimeMillis());
    }

    /**
     * Add BinaryFile
     *
     * @param binaryFile BinaryFile object
     */
    public void addBinaryFile(BinaryFile binaryFile) {
        mapBinaryFile.put(binaryFile.getDesPath(), binaryFile);
        this.actualSize += binaryFile.getActualSize();
    }

    /**
     * Remove BinaryFile
     *
     * @param binaryFile BinaryFile object
     */
    public void removeBinaryFile(BinaryFile binaryFile) {
        mapBinaryFile.remove(binaryFile.getDesPath());
        this.actualSize -= binaryFile.getActualSize();
    }

    /**
     * Add file
     *
     * @param srcPath source file path
     * @param desPath destination file path
     * @param delSrc  delete source after adding
     * @throws Exception
     */
    public void addFile(String srcPath, String desPath, boolean delSrc) throws Exception {
        long t1 = System.currentTimeMillis();
        BinaryFile binaryFile = BinaryFile.newInstance(srcPath, desPath, delSrc);
        addBinaryFile(binaryFile);
        CompressLoggerKits.createAddFileLog(this, binaryFile, t1, System.currentTimeMillis());
    }

    /**
     * Add file
     *
     * @param srcPath source file path
     * @param desPath destination file path
     * @throws Exception
     */
    public void addFile(String srcPath, String desPath) throws Exception {
        addFile(srcPath, desPath, SOURCE.KEEP);
    }

    /**
     * Add file and delete source
     *
     * @param srcPath source file path
     * @param desPath destination file path
     * @throws Exception
     */
    public void addFileAndDelete(String srcPath, String desPath) throws Exception {
        addFile(srcPath, desPath, SOURCE.DELETE);
    }

    /**
     * Remove file
     *
     * @param srcPath path of binary file
     * @throws Exception
     */
    public void removeFile(String srcPath) throws Exception {
        long t1 = System.currentTimeMillis();
        srcPath = FileKits.getSafePath(srcPath);
        BinaryFile binaryFile = mapBinaryFile.get(srcPath);
        if (binaryFile == null) {
            CompressLoggerKits.createFileNotFoundLog("Remove file...", this, srcPath);
            CompressLoggerKits.createRemoveFileLog(this, binaryFile, t1, System.currentTimeMillis());
            return;
        }
        removeBinaryFile(binaryFile);
        CompressLoggerKits.createRemoveFileLog(this, binaryFile, t1, System.currentTimeMillis());
    }

    public BinaryFile getBinaryFile(String fileName) {
        return mapBinaryFile.get(fileName);
    }

    public String getCompressedTypeName() {
        if (compressedType == null) {
            return "";
        }
        return compressedType.getName();
    }
}
