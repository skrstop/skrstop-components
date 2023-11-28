package com.jphoebe.framework.components.util.value.stream;

import com.jphoebe.framework.components.core.exception.defined.io.IOStreamingException;
import com.jphoebe.framework.components.util.constant.CharSetEnum;
import com.jphoebe.framework.components.util.constant.HttpMethodEnum;
import com.jphoebe.framework.components.util.value.data.ObjectUtil;
import com.jphoebe.framework.components.util.value.data.StrUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author 蒋时华
 */
public class StreamUtil {

    private static final String DEFAULT_CHARSET = CharSetEnum.UTF8.getCharSet();

    /**
     * inputStream 转 outputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static ByteArrayOutputStream parse(final InputStream in) {
        try {
            final ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            int ch;
            while ((ch = in.read()) != -1) {
                swapStream.write(ch);
            }
            return swapStream;
        } catch (Exception e) {
            throw new IOStreamingException(e);
        }
    }

    /**
     * outputStream to inputStream
     *
     * @param out
     * @return
     * @throws Exception
     */
    public static ByteArrayInputStream parse(final OutputStream out) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = (ByteArrayOutputStream) out;
        final ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream;
    }

    /**
     * Output Stream to String
     *
     * @param out
     * @return
     * @throws Exception
     */
    public static String parseString(final OutputStream out) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = (ByteArrayOutputStream) out;
        final ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream.toString();
    }

    /**
     * String to inputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static ByteArrayInputStream parseInputStream(final String in) {
        final ByteArrayInputStream input = new ByteArrayInputStream(in.getBytes());
        return input;
    }

    /**
     * String to outputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static ByteArrayOutputStream parseOutputStream(final String in) {
        return parse(parseInputStream(in));
    }

    /**
     * input stream to String
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String parseString(InputStream inputStream) {
        try {
            return inputStreamToString(inputStream, DEFAULT_CHARSET);
        } catch (Exception e) {
            throw new IOStreamingException(e);
        }
    }

    /**
     * inputStream 转为字符串
     *
     * @param inputStream 输入流
     * @param charset     编码
     * @return
     * @throws IOException
     */
    public static String inputStreamToString(InputStream inputStream, String charset) {
        try {
            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            final StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(inputStream, charset);
            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0) {
                    break;
                }
                out.append(buffer, 0, rsz);
            }
            return out.toString();
        } catch (Exception e) {
            throw new IOStreamingException(e);
        }
    }

    /**
     * inputstream to byte[]
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] inputStreamToByte(InputStream inputStream) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new IOStreamingException(e);
        }
    }

    /**
     * bete[] to inputStream
     *
     * @param bytes
     * @return
     */
    public static InputStream byteToInputStream(byte[] bytes) {
        try {
            return new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            throw new IOStreamingException(e);
        }
    }

    /**
     * 通过http url 获取inputStream
     *
     * @param urlStr
     * @return
     * @throws IOException
     */
    public static InputStream getInputStream(String urlStr) throws IOException {
        if (StrUtil.isNotBlank(urlStr)) {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(HttpMethodEnum.GET.getMethod());
            con.setConnectTimeout(4 * 1000);
            //通过输入流获取数据
            return con.getInputStream();
        }
        return null;
    }

    /**
     * 通过http url 获取inputStream
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static InputStream getInputStream(URL url) throws IOException {
        if (ObjectUtil.isNotNull(url) && StrUtil.isNotBlank(url.toString())) {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(HttpMethodEnum.GET.getMethod());
            con.setConnectTimeout(4 * 1000);
            //通过输入流获取数据
            return con.getInputStream();
        }
        return null;
    }

    /**
     * 通过http url 获取 byte[]
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static byte[] getBytes(URL url) throws IOException {
        if (ObjectUtil.isNotNull(url) && StrUtil.isNotBlank(url.toString())) {
            InputStream inputStream = getInputStream(url);
            if (ObjectUtil.isNotNull(inputStream)) {
                return inputStreamToByte(inputStream);
            }
        }
        return null;
    }

}
