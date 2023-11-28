package com.jphoebe.framework.components.util.compression.utils;

import com.jphoebe.framework.components.util.compression.bean.BinaryFile;
import com.jphoebe.framework.components.util.compression.compressor.FileCompressor;

/**
 * Compress Logger Kits
 *
 * @author 蒋时华
 */
public class CompressLoggerKits {

    public static void createFileLog(FileCompressor fileCompressor) {
        String compressedPath = fileCompressor.getCompressedPath();
        String s =
                fileCompressor.hashCode()
                        + " Created file"
                        + ((compressedPath == null || compressedPath.isEmpty())
                        ? ""
                        : (" <" + compressedPath.trim() + ">"));
        FileCompressor.LOGGER.info(s);
    }

    public static void createStartCompressingLog(FileCompressor fileCompressor) {
        FileCompressor.LOGGER.info(
                fileCompressor.hashCode()
                        + " Start compressing... <"
                        + fileCompressor.getCompressedPath()
                        + ">");
    }

    public static void createFinishCompressingLog(
            FileCompressor fileCompressor, long startTime, long endTime) {
        long actualSize = fileCompressor.getActualSize();
        long compressedSize = fileCompressor.getCompressedSize();
        double rate = (actualSize - compressedSize) * 1.0 / actualSize;
        StringBuilder s =
                new StringBuilder(
                        fileCompressor.hashCode()
                                + " Finish compressing <"
                                + fileCompressor.getCompressedPath()
                                + ">");
        s.append("\n    Type: ").append(fileCompressor.getCompressedTypeName());
        s.append("\n    Level: ").append(fileCompressor.getLevel());
        s.append("\n    Actual size: ").append(FormatterKits.formatSizeInfo(actualSize));
        s.append("\n    Compressed size: ").append(FormatterKits.formatSizeInfo(compressedSize));
        s.append("\n    Rate: ").append(FormatterKits.formatPercent(rate));
        s.append("\n    Time: ")
                .append(endTime - startTime)
                .append(" (")
                .append(FormatterKits.formatTimeInfo(endTime - startTime))
                .append(")");
        FileCompressor.LOGGER.info(s.toString());
    }

    public static void createStartDecompressingLog(FileCompressor fileCompressor) {
        FileCompressor.LOGGER.info(
                fileCompressor.hashCode()
                        + " Start decompressing... <"
                        + fileCompressor.getCompressedPath()
                        + ">");
    }

    public static void createFinishDecompressingLog(
            FileCompressor fileCompressor, long startTime, long endTime) {
        FileCompressor.LOGGER.info(
                fileCompressor.hashCode()
                        + " Finish decompressing <"
                        + fileCompressor.getCompressedPath()
                        + "> in "
                        + (endTime - startTime)
                        + " ("
                        + FormatterKits.formatTimeInfo(endTime - startTime)
                        + ") Size: "
                        + FormatterKits.formatSizeInfo(fileCompressor.getActualSize()));
    }

    public static void createDeleteAllSourcesLog(
            FileCompressor fileCompressor, long startTime, long endTime) {
        FileCompressor.LOGGER.info(
                fileCompressor.hashCode()
                        + " Delete all sources in "
                        + (endTime - startTime)
                        + " ("
                        + FormatterKits.formatTimeInfo(endTime - startTime)
                        + ")");
    }

    public static void createAddFileLog(
            FileCompressor fileCompressor, BinaryFile binaryFile, long startTime, long endTime) {
        FileCompressor.LOGGER.info(
                fileCompressor.hashCode()
                        + " Added file <"
                        + binaryFile.getSrcPath()
                        + "> to <"
                        + binaryFile.getDesPath()
                        + "> in "
                        + (endTime - startTime)
                        + " ("
                        + FormatterKits.formatTimeInfo(endTime - startTime)
                        + ") Size: "
                        + FormatterKits.formatSizeInfo(binaryFile.getActualSize()));
    }

    public static void createRemoveFileLog(
            FileCompressor fileCompressor, BinaryFile binaryFile, long startTime, long endTime) {
        FileCompressor.LOGGER.info(
                fileCompressor.hashCode()
                        + " Removed file <"
                        + binaryFile.getDesPath()
                        + "> in "
                        + (endTime - startTime)
                        + " ("
                        + FormatterKits.formatTimeInfo(endTime - startTime)
                        + ") Size: "
                        + FormatterKits.formatSizeInfo(binaryFile.getActualSize()));
    }

    public static void createExtractLog(
            FileCompressor fileCompressor, BinaryFile binaryFile, long startTime, long endTime) {
        FileCompressor.LOGGER.info(
                fileCompressor.hashCode()
                        + " Extracted file <"
                        + binaryFile.getDesPath()
                        + "> in "
                        + (endTime - startTime)
                        + " ("
                        + FormatterKits.formatTimeInfo(endTime - startTime)
                        + ") Size: "
                        + FormatterKits.formatSizeInfo(binaryFile.getActualSize()));
    }

    public static void createReadLog(
            FileCompressor fileCompressor, String srcPath, long size, long startTime, long endTime) {
        FileCompressor.LOGGER.info(
                fileCompressor.hashCode()
                        + " Read from file <"
                        + srcPath
                        + "> in "
                        + (endTime - startTime)
                        + " ("
                        + FormatterKits.formatTimeInfo(endTime - startTime)
                        + ") Size: "
                        + FormatterKits.formatSizeInfo(size));
    }

    public static void createFileNotFoundLog(
            String prefix, FileCompressor fileCompressor, String srcPath) {
        FileCompressor.LOGGER.error(
                fileCompressor.hashCode()
                        + (prefix == null ? "" : (" " + prefix))
                        + " File not found <"
                        + srcPath
                        + ">");
    }

    public static void createMemoryLog() {
        StringBuilder s = new StringBuilder();
        s.append("JVM Memory Info");
        s.append("\n    Heap size: ").append(FormatterKits.formatSizeInfo(MemoryKits.getTotalMemory()));
        s.append("\n    Max heap size: ")
                .append(FormatterKits.formatSizeInfo(MemoryKits.getMaxMemory()));
        s.append("\n    Free: ").append(FormatterKits.formatSizeInfo(MemoryKits.getFreeMemory()));
        s.append("\n    Used: ").append(FormatterKits.formatSizeInfo(MemoryKits.getUsedMemory()));
        FileCompressor.LOGGER.info(s.toString());
    }

    public static void createFileTypeNotSupportLog(String fileName) {
        FileCompressor.LOGGER.error("File type not supported. <" + fileName + ">");
    }
}
