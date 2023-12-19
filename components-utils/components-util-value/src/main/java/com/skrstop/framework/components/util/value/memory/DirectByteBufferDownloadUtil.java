package com.skrstop.framework.components.util.value.memory;

import cn.hutool.core.util.IdUtil;
import com.skrstop.framework.components.core.annotation.source.Experimental;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * 堆外内存下载工具
 *
 * @author 蒋时华
 * @date 2021-06-02 17:23:50
 */
@Experimental
@UtilityClass
public class DirectByteBufferDownloadUtil {

    private static int DEFAULT_READ_SIZE = 1024 * 1024 * 10;

    /**
     * 输出到指定文件
     *
     * @param source
     * @param target
     * @throws IOException
     */
    public static void downloadToFile(InputStream source, File target, int readSize) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        ByteBuffer byteBuffer = null;
        FileChannel targetFileChannel = new FileOutputStream(target).getChannel();
        try {
            ReadableByteChannel readableByteChannel = Channels.newChannel(source);
            // 字节缓冲区
            byteBuffer = MappedByteBuffer.allocateDirect(readSize);
            // 从输出流获得通道
            while (readableByteChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                targetFileChannel.write(byteBuffer);
                byteBuffer.clear();
            }
        } finally {
            targetFileChannel.close();
            DirectByteBufferDownloadUtil.close(byteBuffer);
        }
    }

    /**
     * 输出到指定文件
     *
     * @param source
     * @param target
     * @throws IOException
     */
    public static void downloadToFile(InputStream source, File target) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        DirectByteBufferDownloadUtil.downloadToFile(source, target, DEFAULT_READ_SIZE);
    }

    /**
     * 输出到临时文件
     *
     * @param source
     * @return
     * @throws IOException
     */
    public static File downloadToTempFile(InputStream source, int readSize) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        File tempFile = File.createTempFile(IdUtil.fastSimpleUUID(), ".temp");
        DirectByteBufferDownloadUtil.downloadToFile(source, tempFile, readSize);
        return tempFile;
    }

    /**
     * 输出到临时文件
     *
     * @param source
     * @return
     * @throws IOException
     */
    public static File downloadToTempFile(InputStream source) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        File tempFile = File.createTempFile(IdUtil.fastSimpleUUID(), ".temp");
        DirectByteBufferDownloadUtil.downloadToFile(source, tempFile, DEFAULT_READ_SIZE);
        return tempFile;
    }

    /**
     * 释放直接内存
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public static void close(ByteBuffer byteBuffer) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (byteBuffer == null) {
            return;
        }
        if (!byteBuffer.isDirect()) {
            return;
        }
//        Assert.isTrue(byteBuffer.isDirect(), "toBeDestroyed isn't direct!");
        byteBuffer.clear();
        Method cleanerMethod = byteBuffer.getClass().getMethod("cleaner");
        cleanerMethod.setAccessible(true);
        Object cleaner = cleanerMethod.invoke(byteBuffer);
        Method cleanMethod = cleaner.getClass().getMethod("clean");
        cleanMethod.setAccessible(true);
        cleanMethod.invoke(cleaner);
    }
}
