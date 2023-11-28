package cn.auntec.framework.components.util.value.memory;

import cn.hutool.core.util.IdUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 半成品
 *
 * @author 蒋时华
 * @date 2021-06-02 17:23:50
 */
public class DirectByteBufferDownloadUtil implements Cloneable {

    private static int DEFAULT_READ_SIZE = 1024 * 1024 * 10;

    public static void main(String[] args) throws Exception {

        String httpUrl = "http://java-static-resource-prod.oss-cn-hangzhou.aliyuncs.com/application/soft_package/mac/17418237/software/1.0.0.21/cdUwld/install.dmg";

        TimeUnit.SECONDS.sleep(10);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < 15; i++) {

            new Thread(() -> {
                try {
                    URL url = new URL(httpUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "application/octet-stream");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    countDownLatch.await();

                    File file = DirectByteBufferDownloadUtil.downloadToTempFile(inputStream);
//
//                    HttpResponse response = HttpClientUtil.postJdk("http://127.0.0.1:18091/mq/test/upload")
////                            .form("file", file)
//                            .form("file", new UrlResource(URLUtil.url(httpUrl)))
//                            .execute();
//                    System.out.println("upload over: " + response.getStatus() + "    "  + response.body());

//                    AsyncHttpClient client = new DefaultAsyncHttpClient(new DefaultAsyncHttpClientConfig.Builder()
////                            .setReadTimeout(60 * 2)
////                            .setConnectTimeout(60 * 2)
////                            .setRequestTimeout(60 * 2)
//                            .build());
//                    ListenableFuture<Response> execute = client.preparePost("http://127.0.0.1:18091/mq/test/upload")
//                            .setBody(inputStream)
//                            .execute();
//                    Response response = execute.get();
//                    System.out.println("upload over: " + response.getStatusText() + "    " + response.getStatusText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        countDownLatch.countDown();
        System.out.println("end");
        TimeUnit.MINUTES.sleep(10);

    }

    /**
     * 输出到指定文件
     *
     * @param source
     * @param target
     * @throws IOException
     */
    public static void downloadToFile(InputStream source, File target, int readSize) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        ByteBuffer byteBuffer = null;
        try {
            ReadableByteChannel readableByteChannel = Channels.newChannel(source);
            // 字节缓冲区
            byteBuffer = MappedByteBuffer.allocateDirect(readSize);
            // 从输出流获得通道
            FileChannel targetFileChannel = new FileOutputStream(target).getChannel();
            // 传输
            while (readableByteChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                targetFileChannel.write(byteBuffer);
                byteBuffer.clear();
            }
        } finally {
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
