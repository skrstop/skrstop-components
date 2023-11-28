package cn.auntec.framework.components.util.extra.document;

import cn.auntec.framework.components.util.value.data.ObjectUtil;
import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author 蒋时华
 * @date 2021-01-12 13:02:39
 */
@Slf4j
public class PdfBoxUtil implements Closeable {

    private PDDocument document;
    private PDFTextStripper textStripper;

    private PdfBoxUtil(PDDocument document) throws IOException {
        this.document = document;
        textStripper = new PDFTextStripper();
    }

    @Override
    public void close() throws IOException {
        if (ObjectUtil.isNull(this.document)) {
            return;
        }
        this.document.close();
    }

    /**
     * 加载pdf文件
     *
     * @param file
     * @param password
     * @return
     * @throws IOException
     */
    public static PdfBoxUtil load(File file, String password) throws IOException {
        return new PdfBoxUtil(PDDocument.load(file, password));
    }

    /**
     * 加载pdf文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static PdfBoxUtil load(File file) throws IOException {
        return new PdfBoxUtil(PDDocument.load(file));
    }

    /**
     * 加载pdf文件
     *
     * @param in
     * @param password
     * @return
     * @throws IOException
     */
    public static PdfBoxUtil load(InputStream in, String password) throws IOException {
        return new PdfBoxUtil(PDDocument.load(in, password));
    }

    /**
     * 加载pdf文件
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static PdfBoxUtil load(InputStream in) throws IOException {
        return new PdfBoxUtil(PDDocument.load(in));
    }

    /**
     * 加载pdf文件
     *
     * @param bytes
     * @param password
     * @return
     * @throws IOException
     */
    public static PdfBoxUtil load(byte[] bytes, String password) throws IOException {
        return new PdfBoxUtil(PDDocument.load(bytes, password));
    }

    /**
     * 加载pdf文件
     *
     * @param bytes
     * @return
     * @throws IOException
     */
    public static PdfBoxUtil load(byte[] bytes) throws IOException {
        return new PdfBoxUtil(PDDocument.load(bytes));
    }

    /**
     * 加载pdf文件
     *
     * @param url
     * @param password
     * @return
     * @throws IOException
     */
    public static PdfBoxUtil load(String url, String password) throws IOException {
        return new PdfBoxUtil(PDDocument.load(URLUtil.getStream(URLUtil.url(url)), password));
    }

    /**
     * 加载pdf文件
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static PdfBoxUtil load(String url) throws IOException {
        return new PdfBoxUtil(PDDocument.load(URLUtil.getStream(URLUtil.url(url))));
    }

    /**
     * 加载pdf文件
     *
     * @param url
     * @param password
     * @return
     * @throws IOException
     */
    public static PdfBoxUtil load(URL url, String password) throws IOException {
        return new PdfBoxUtil(PDDocument.load(URLUtil.getStream(url), password));
    }

    /**
     * 加载pdf文件
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static PdfBoxUtil load(URL url) throws IOException {
        return new PdfBoxUtil(PDDocument.load(URLUtil.getStream(url)));
    }

    /**
     * 读取 pdf 文件内容，转化成 string
     *
     * @return
     * @throws IOException
     */
    public String getText() throws IOException {
        String text;
        text = this.textStripper.getText(this.document);
        return text;
    }

}
