package com.skrstop.framework.components.util.extra;

import com.skrstop.framework.components.util.extra.document.PdfBoxUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class PdfBoxUtilTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private byte[] pdfBytes() throws IOException {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);
            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                cs.newLineAtOffset(50, 700);
                cs.showText("hello pdf");
                cs.endText();
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.save(out);
            return out.toByteArray();
        }
    }

    @Test
    public void testLoadBytesAndGetTextAndClose() throws IOException {
        byte[] bytes = pdfBytes();
        try (PdfBoxUtil util = PdfBoxUtil.load(bytes)) {
            String text = util.getText();
            Assert.assertNotNull(text);
            Assert.assertTrue(text.contains("hello pdf"));
        }
    }

    @Test
    public void testLoadBytesWithPassword() throws IOException {
        byte[] bytes = pdfBytes();
        try (PdfBoxUtil util = PdfBoxUtil.load(bytes, null)) {
            Assert.assertTrue(util.getText().contains("hello pdf"));
        }
    }

    @Test
    public void testLoadFileAndGetTextAndClose() throws IOException {
        File file = folder.newFile("test.pdf");
        java.nio.file.Files.write(file.toPath(), pdfBytes());
        try (PdfBoxUtil util = PdfBoxUtil.load(file)) {
            Assert.assertTrue(util.getText().contains("hello pdf"));
        }
    }

    @Test
    public void testLoadFileWithPassword() throws IOException {
        File file = folder.newFile("test2.pdf");
        java.nio.file.Files.write(file.toPath(), pdfBytes());
        try (PdfBoxUtil util = PdfBoxUtil.load(file, null)) {
            Assert.assertTrue(util.getText().contains("hello pdf"));
        }
    }
}
