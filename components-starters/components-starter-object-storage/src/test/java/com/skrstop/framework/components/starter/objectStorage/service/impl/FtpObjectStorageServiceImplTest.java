package com.skrstop.framework.components.starter.objectStorage.service.impl;

import cn.hutool.extra.ftp.Ftp;
import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.skrstop.framework.components.starter.objectStorage.configuration.FtpProperties;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * FtpObjectStorageServiceImpl: 真实 new Ftp 会连接 FTP 服务器,
 * 用 mockConstruction 拦截构造(不执行真实构造代码), 从 constructed() 取出 mock 后打桩。
 */
public class FtpObjectStorageServiceImplTest {

    private FtpProperties properties() {
        FtpProperties properties = new FtpProperties();
        properties.setHost("h");
        properties.setUsername("u");
        properties.setPassword("p");
        properties.setBasePath("");
        return properties;
    }

    @Test
    public void ctorAndSimpleMethods() throws Exception {
        try (MockedConstruction<Ftp> mocked = Mockito.mockConstruction(Ftp.class)) {
            // basePath 默认 "/" 分支
            FtpObjectStorageServiceImpl service = new FtpObjectStorageServiceImpl(properties());
            Ftp ftp = mocked.constructed().get(0);
            Assert.assertSame(ftp, service.getClient());
            Assert.assertNull(service.getDefaultBucketName());
            Assert.assertEquals("/", service.getBasePath());
            Mockito.verify(ftp).setMode(properties().getMode());

            // basePath 非空分支
            FtpProperties withBase = properties();
            withBase.setBasePath("bp");
            FtpObjectStorageServiceImpl service2 = new FtpObjectStorageServiceImpl(withBase);
            Assert.assertEquals("bp", service2.getBasePath());

            // upload(File)
            Mockito.when(ftp.upload(anyString(), anyString(), any(File.class))).thenReturn(true);
            Assert.assertTrue(service.upload("b", "t/f.txt", new File("f")));

            // upload(InputStream) 成功
            Mockito.when(ftp.upload(anyString(), anyString(), any(InputStream.class))).thenReturn(true);
            Assert.assertTrue(service.upload("b", "t/f.txt", new ByteArrayInputStream(new byte[0])));

            // upload(InputStream) 抛异常 -> false
            Mockito.when(ftp.upload(anyString(), anyString(), any(InputStream.class)))
                    .thenThrow(new RuntimeException("x"));
            Assert.assertFalse(service.upload("b", "t/f.txt", new ByteArrayInputStream(new byte[0])));

            // upload(byte[]) -> 走 InputStream 分支
            Mockito.when(ftp.upload(anyString(), anyString(), any(InputStream.class))).thenReturn(true);
            Assert.assertTrue(service.upload("b", "t/f.txt", new byte[0]));

            // download(File) 成功/异常
            Mockito.doNothing().when(ftp).download(anyString(), anyString(), any(File.class));
            Assert.assertTrue(service.download("b", "t/f.txt", new File("f")));
            Mockito.doThrow(new RuntimeException("x")).when(ftp).download(anyString(), anyString(), any(File.class));
            Assert.assertFalse(service.download("b", "t/f.txt", new File("f")));

            // download(String) 委托
            Mockito.doNothing().when(ftp).download(anyString(), anyString(), any(File.class));
            Assert.assertTrue(service.download("b", "t/f.txt", "local"));

            // exists
            Mockito.when(ftp.exist(anyString())).thenReturn(true);
            Assert.assertTrue(service.exists("b", "t"));

            // delete: 目录 / 文件
            Mockito.when(ftp.isDir(anyString())).thenReturn(true);
            Mockito.when(ftp.delDir(anyString())).thenReturn(true);
            Assert.assertTrue(service.delete("b", "t"));
            Mockito.when(ftp.isDir(anyString())).thenReturn(false);
            Mockito.when(ftp.delFile(anyString())).thenReturn(false);
            Assert.assertFalse(service.delete("b", "t"));

            // delete list
            Mockito.when(ftp.isDir(anyString())).thenReturn(true);
            Mockito.when(ftp.delDir(anyString())).thenReturn(true);
            Assert.assertTrue(service.delete("b", Arrays.asList("a", "b")));

            // close
            Mockito.doNothing().when(ftp).close();
            service.close();

            // 构造失败: 非法 charset (Charset.forName 在 try 内抛出)
            FtpProperties badCharset = properties();
            badCharset.setCharset("bad-charset-xx");
            try {
                new FtpObjectStorageServiceImpl(badCharset);
                Assert.fail("should throw");
            } catch (IllegalArgumentException e) {
                Assert.assertEquals("FTP连接失败", e.getMessage());
            }
        }
    }

    @Test
    public void notSupported() throws Exception {
        try (MockedConstruction<Ftp> mocked = Mockito.mockConstruction(Ftp.class)) {
            FtpObjectStorageServiceImpl service = new FtpObjectStorageServiceImpl(properties());
            int i = 0;
            try {
                service.downloadInputStream("b", "t");
            } catch (NotSupportedException e) {
                i++;
            }
            try {
                service.copy("b", "s", "t");
            } catch (NotSupportedException e) {
                i++;
            }
            try {
                service.copy("b", "s", "b2", "t");
            } catch (NotSupportedException e) {
                i++;
            }
            try {
                service.move("b", "s", "t");
            } catch (NotSupportedException e) {
                i++;
            }
            try {
                service.move("b", "s", "b2", "t");
            } catch (NotSupportedException e) {
                i++;
            }
            try {
                service.getTemporaryAccessUrl("b", "t", null);
            } catch (NotSupportedException e) {
                i++;
            }
            try {
                service.getTemporaryAccessUrl("b", Arrays.asList("t"), null);
            } catch (NotSupportedException e) {
                i++;
            }
            try {
                service.getPublicAccessUrl("b", Arrays.asList("t"), false);
            } catch (NotSupportedException e) {
                i++;
            }
            try {
                service.getPublicAccessUrl("b", "t", false);
            } catch (NotSupportedException e) {
                i++;
            }
            try {
                service.getTemporaryUploadSign("b", "t", null);
            } catch (NotSupportedException e) {
                i++;
            }
            try {
                service.createSymlink("b", "l", "t");
            } catch (NotSupportedException e) {
                i++;
            }
            Assert.assertEquals(11, i);
        }
    }

    @Test
    public void uploadInputStreamCloseThrows() throws Exception {
        try (MockedConstruction<Ftp> mocked = Mockito.mockConstruction(Ftp.class)) {
            FtpObjectStorageServiceImpl service = new FtpObjectStorageServiceImpl(properties());
            Ftp ftp = mocked.constructed().get(0);
            Mockito.when(ftp.upload(anyString(), anyString(), any(InputStream.class))).thenReturn(true);
            InputStream badClose = new InputStream() {
                @Override
                public int read() {
                    return -1;
                }

                @Override
                public void close() throws IOException {
                    throw new IOException("close failed");
                }
            };
            Assert.assertTrue(service.upload("b", "t/f.txt", badClose));
        }
    }
}
