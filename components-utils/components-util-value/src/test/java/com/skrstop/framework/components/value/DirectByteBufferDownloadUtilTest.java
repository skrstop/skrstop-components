package com.skrstop.framework.components.value;

import com.skrstop.framework.components.util.value.memory.DirectByteBufferDownloadUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author 蒋时华
 * @date 2023-11-30 14:03:59
 */
public class DirectByteBufferDownloadUtilTest {

    @Test
    @Ignore
    public void test() throws InterruptedException {
        String httpUrl = "url";

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

}
