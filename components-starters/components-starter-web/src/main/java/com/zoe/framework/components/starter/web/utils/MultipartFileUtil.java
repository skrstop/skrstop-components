package com.zoe.framework.components.starter.web.utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author 蒋时华
 * @date 2021-03-15 17:33:43
 */
public class MultipartFileUtil {

    private MultipartFileUtil() {
    }

    /**
     * FileItem类对象创建
     *
     * @param fis
     * @param fileName
     * @return
     */
    public static FileItem createFileItem(InputStream fis, String fileName) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "file";
        FileItem item = factory.createItem(textFieldName, "multipart/form-data", true, fileName);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        // 使用输出流输出输入流的字节
        try {
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

}
