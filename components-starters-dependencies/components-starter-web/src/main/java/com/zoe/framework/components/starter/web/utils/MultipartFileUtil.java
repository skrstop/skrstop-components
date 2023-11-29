package com.zoe.framework.components.starter.web.utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

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
     * 获取传输的multipartFile，将输入流+文件名转成multipartFile文件
     *
     * @param inputStream
     * @param fileName
     * @return
     */
    public static MultipartFile getMulFile(InputStream inputStream, String fileName) {
        FileItem fileItem = createFileItem(inputStream, fileName);
        // CommonsMultipartFile是feign对multipartFile的封装，但是要FileItem类对象
        MultipartFile mfile = new CommonsMultipartFile(fileItem);
        return mfile;
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
