package com.skrstop.framework.components.util.extra.document;

import com.skrstop.framework.components.util.value.data.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

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
        return new PdfBoxUtil(Loader.loadPDF(file, password));
    }

    /**
     * 加载pdf文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static PdfBoxUtil load(File file) throws IOException {
        return new PdfBoxUtil(Loader.loadPDF(file));
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
        return new PdfBoxUtil(Loader.loadPDF(bytes, password));
    }

    /**
     * 加载pdf文件
     *
     * @param bytes
     * @return
     * @throws IOException
     */
    public static PdfBoxUtil load(byte[] bytes) throws IOException {
        return new PdfBoxUtil(Loader.loadPDF(bytes));
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
