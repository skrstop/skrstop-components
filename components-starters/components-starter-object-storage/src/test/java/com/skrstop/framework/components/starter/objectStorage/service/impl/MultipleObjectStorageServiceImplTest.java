package com.skrstop.framework.components.starter.objectStorage.service.impl;

import cn.hutool.extra.ftp.Ftp;
import com.skrstop.framework.components.starter.objectStorage.configuration.CosProperties;
import com.skrstop.framework.components.starter.objectStorage.configuration.OssProperties;
import com.skrstop.framework.components.starter.objectStorage.configuration.dynamic.DynamicDatasourceContextHolder;
import com.skrstop.framework.components.starter.objectStorage.configuration.dynamic.DynamicObjectStorageProperties;
import com.skrstop.framework.components.starter.objectStorage.entity.TemporaryAccessExtraParam;
import com.skrstop.framework.components.starter.objectStorage.entity.UploadLimit;
import com.skrstop.framework.components.starter.objectStorage.service.ObjectStorageService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

/**
 * MultipleObjectStorageServiceImpl: 构造时会创建真实的 Cos/Oss service(不联网),
 * 测试委托方法前把内部 datasource map 替换为 mock, 避免真实 SDK 调用。
 */
public class MultipleObjectStorageServiceImplTest {

    @After
    public void tearDown() {
        DynamicDatasourceContextHolder.clear();
    }

    private DynamicObjectStorageProperties properties(boolean exceptionWhileNotFound, String primary) {
        DynamicObjectStorageProperties properties = new DynamicObjectStorageProperties();
        properties.setExceptionWhileNotFound(exceptionWhileNotFound);
        properties.setPrimary(primary);
        CosProperties cos = new CosProperties();
        cos.setSecretId("id");
        cos.setSecretKey("key");
        cos.setRegion("ap-beijing-1");
        properties.getCosDataSources().put("cosDs", cos);
        OssProperties oss = new OssProperties();
        oss.setEndpoint("oss-cn-beijing.aliyuncs.com");
        oss.setRegion("cn-beijing");
        properties.getOssDataSources().put("ossDs", oss);
        return properties;
    }

    private void injectMocks(MultipleObjectStorageServiceImpl service, ObjectStorageService... mocks) {
        Map<String, ObjectStorageService> map = new java.util.LinkedHashMap<>();
        String[] keys = {"cosDs", "ossDs"};
        for (int i = 0; i < mocks.length; i++) {
            map.put(keys[i], mocks[i]);
        }
        ReflectionTestUtils.setField(service, "objectStorageDatasourceMap", map);
    }

    @Test
    public void ctorBranches() {
        // 空数据源
        try {
            new MultipleObjectStorageServiceImpl(new DynamicObjectStorageProperties());
            Assert.fail("should throw");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("动态数据源为空"));
        }
        // cos 数据源名称重复 (ftp 先放入同名 key)
        try (MockedConstruction<Ftp> ignored = Mockito.mockConstructionWithAnswer(Ftp.class, inv -> Mockito.mock(Ftp.class))) {
            DynamicObjectStorageProperties properties = new DynamicObjectStorageProperties();
            com.skrstop.framework.components.starter.objectStorage.configuration.FtpProperties ftp =
                    new com.skrstop.framework.components.starter.objectStorage.configuration.FtpProperties();
            ftp.setHost("h");
            properties.getFtpDataSources().put("dup", ftp);
            CosProperties cos = new CosProperties();
            cos.setSecretId("id");
            cos.setSecretKey("key");
            cos.setRegion("r");
            properties.getCosDataSources().put("dup", cos);
            try {
                new MultipleObjectStorageServiceImpl(properties);
                Assert.fail("should throw");
            } catch (IllegalArgumentException e) {
                Assert.assertTrue(e.getMessage().contains("cos数据源名称重复"));
            }
            // oss 数据源名称重复
            DynamicObjectStorageProperties properties2 = new DynamicObjectStorageProperties();
            CosProperties cos2 = new CosProperties();
            cos2.setSecretId("id");
            cos2.setSecretKey("key");
            cos2.setRegion("r");
            properties2.getCosDataSources().put("dup", cos2);
            properties2.getOssDataSources().put("dup", new OssProperties());
            try {
                new MultipleObjectStorageServiceImpl(properties2);
                Assert.fail("should throw");
            } catch (IllegalArgumentException e) {
                Assert.assertTrue(e.getMessage().contains("oss数据源名称重复"));
            }
        }
    }

    private static Object[] args(Method m) {
        Class<?>[] types = m.getParameterTypes();
        Object[] result = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            boolean lastVarargs = m.isVarArgs() && i == types.length - 1;
            if (lastVarargs) {
                Object array = java.lang.reflect.Array.newInstance(types[i].getComponentType(), 1);
                result[i] = array;
            } else {
                result[i] = defaultArg(types[i]);
            }
        }
        return result;
    }

    private static Object defaultArg(Class<?> type) {
        if (type == String.class) return "cosDs";
        if (type == long.class) return 1L;
        if (type == Long.class) return 1L;
        if (type == boolean.class) return false;
        if (type == int.class) return 1;
        if (type == File.class) return new File("f");
        if (type == byte[].class) return new byte[0];
        if (type == InputStream.class) return new java.io.ByteArrayInputStream(new byte[0]);
        if (type == List.class) return new ArrayList<>();
        if (type == Map.class) return new java.util.HashMap<>();
        if (type == TemporaryAccessExtraParam.class) return TemporaryAccessExtraParam.builder().build();
        if (type == UploadLimit.class) return UploadLimit.builder().build();
        return null;
    }

    @Test
    public void delegateAllMethods() throws Exception {
        MultipleObjectStorageServiceImpl service =
                new MultipleObjectStorageServiceImpl(properties(false, "cosDs"));
        ObjectStorageService cosMock = mock(ObjectStorageService.class);
        ObjectStorageService ossMock = mock(ObjectStorageService.class);
        Mockito.when(cosMock.getBasePath()).thenReturn("cosDs");
        Mockito.when(ossMock.getBasePath()).thenReturn("ossDs");
        Mockito.when(ossMock.getClient()).thenReturn(new Object());
        injectMocks(service, cosMock, ossMock);

        DynamicDatasourceContextHolder.push("cosDs");
        for (Method m : ObjectStorageService.class.getDeclaredMethods()) {
            if (Modifier.isStatic(m.getModifiers()) || m.isSynthetic() || !Modifier.isPublic(m.getModifiers()))
                continue;
            if (m.getName().equals("equals") || m.getName().equals("hashCode") || m.getName().equals("toString"))
                continue;
            if (m.isDefault()) continue;
            m.setAccessible(true);
            try {
                m.invoke(service, args(m));
            } catch (InvocationTargetException e) {
                throw new AssertionError("method " + m.getName() + " threw " + e.getCause(), e.getCause());
            }
        }
        DynamicDatasourceContextHolder.clear();

        // primary 数据源兜底: 未 push 时使用 primary
        ObjectStorageService primaryMock = mock(ObjectStorageService.class);
        Map<String, ObjectStorageService> map = new java.util.LinkedHashMap<>();
        map.put("cosDs", cosMock);
        map.put("ossDs", ossMock);
        ReflectionTestUtils.setField(service, "objectStorageDatasourceMap", map);
        Assert.assertEquals("cosDs", service.getBasePath());
    }

    @Test
    public void notFoundBranches() {
        DynamicObjectStorageProperties properties = properties(true, "cosDs");
        MultipleObjectStorageServiceImpl service = new MultipleObjectStorageServiceImpl(properties);
        ObjectStorageService cosMock = mock(ObjectStorageService.class);
        ObjectStorageService ossMock = mock(ObjectStorageService.class);
        injectMocks(service, cosMock, ossMock);
        // exceptionWhileNotFound = true -> 数据源不存在
        DynamicDatasourceContextHolder.push("missing");
        try {
            service.getBasePath();
            Assert.fail("should throw");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("数据源不存在"));
        } finally {
            DynamicDatasourceContextHolder.clear();
        }
        // exceptionWhileNotFound = false, primary 不存在 -> 默认数据源不存在
        DynamicObjectStorageProperties properties2 = properties(false, "noPrimary");
        MultipleObjectStorageServiceImpl service2 = new MultipleObjectStorageServiceImpl(properties2);
        injectMocks(service2, cosMock, ossMock);
        DynamicDatasourceContextHolder.push("missing");
        try {
            service2.getBasePath();
            Assert.fail("should throw");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("默认数据源不存在"));
        } finally {
            DynamicDatasourceContextHolder.clear();
        }
        // exceptionWhileNotFound = false, primary 存在 -> 使用 primary
        DynamicObjectStorageProperties properties3 = properties(false, "ossDs");
        MultipleObjectStorageServiceImpl service3 = new MultipleObjectStorageServiceImpl(properties3);
        injectMocks(service3, cosMock, ossMock);
        DynamicDatasourceContextHolder.push("missing");
        try {
            service3.getClient();
        } finally {
            DynamicDatasourceContextHolder.clear();
        }
    }

    @Test
    public void closeWithIOException() throws Exception {
        MultipleObjectStorageServiceImpl service =
                new MultipleObjectStorageServiceImpl(properties(false, "cosDs"));
        ObjectStorageService normal = mock(ObjectStorageService.class);
        ObjectStorageService failing = mock(ObjectStorageService.class);
        doThrow(new IOException("close failed")).when(failing).close();
        Map<String, ObjectStorageService> map = new java.util.LinkedHashMap<>();
        map.put("cosDs", normal);
        map.put("ossDs", failing);
        ReflectionTestUtils.setField(service, "objectStorageDatasourceMap", map);
        service.close();
        Mockito.verify(normal).close();
        Mockito.verify(failing).close();
    }
}
