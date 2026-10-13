package com.skrstop.framework.components.starter.objectStorage.service;

import com.skrstop.framework.components.starter.objectStorage.entity.StorageTemplateSign;
import com.skrstop.framework.components.starter.objectStorage.entity.TemporaryAccessExtraParam;
import com.skrstop.framework.components.starter.objectStorage.entity.UploadLimit;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 覆盖 ObjectStorageService / DynamicObjectStorageService 接口的全部 default 方法。
 */
public class InterfaceDefaultsTest {

    /**
     * 最小实现: 抽象方法全部委托给 mock
     */
    static class StubService implements ObjectStorageService {
        final ObjectStorageService mock = Mockito.mock(ObjectStorageService.class);

        @Override
        public String getBasePath() {
            return mock.getBasePath();
        }

        @Override
        public Object getClient() {
            return mock.getClient();
        }

        @Override
        public String getDefaultBucketName() {
            return mock.getDefaultBucketName();
        }

        @Override
        public boolean upload(String bucketName, String targetPath, File file) {
            return mock.upload(bucketName, targetPath, file);
        }

        @Override
        public boolean upload(String bucketName, String targetPath, byte[] bytes) {
            return mock.upload(bucketName, targetPath, bytes);
        }

        @Override
        public boolean upload(String bucketName, String targetPath, InputStream inputStream) {
            return mock.upload(bucketName, targetPath, inputStream);
        }

        @Override
        public boolean download(String bucketName, String targetPath, String localPath) {
            return mock.download(bucketName, targetPath, localPath);
        }

        @Override
        public boolean download(String bucketName, String targetPath, File localFile) {
            return mock.download(bucketName, targetPath, localFile);
        }

        @Override
        public InputStream downloadInputStream(String bucketName, String targetPath) {
            return mock.downloadInputStream(bucketName, targetPath);
        }

        @Override
        public boolean exists(String bucketName, String targetPath) {
            return mock.exists(bucketName, targetPath);
        }

        @Override
        public boolean delete(String bucketName, String targetPath) {
            return mock.delete(bucketName, targetPath);
        }

        @Override
        public boolean delete(String bucketName, java.util.List<String> targetPaths) {
            return mock.delete(bucketName, targetPaths);
        }

        @Override
        public boolean copy(String bucketName, String sourcePath, String targetPath) {
            return mock.copy(bucketName, sourcePath, targetPath);
        }

        @Override
        public boolean copy(String sourceBucketName, String sourcePath, String targetBucketName, String targetPath) {
            return mock.copy(sourceBucketName, sourcePath, targetBucketName, targetPath);
        }

        @Override
        public boolean move(String bucketName, String sourcePath, String targetPath) {
            return mock.move(bucketName, sourcePath, targetPath);
        }

        @Override
        public boolean move(String sourceBucketName, String sourcePath, String targetBucketName, String targetPath) {
            return mock.move(sourceBucketName, sourcePath, targetBucketName, targetPath);
        }

        @Override
        public String getTemporaryAccessUrl(String bucketName, String targetPath, TemporaryAccessExtraParam extraParam) {
            return mock.getTemporaryAccessUrl(bucketName, targetPath, extraParam);
        }

        @Override
        public java.util.Map<String, String> getTemporaryAccessUrl(String bucketName, java.util.List<String> targetPath, TemporaryAccessExtraParam extraParam) {
            return mock.getTemporaryAccessUrl(bucketName, targetPath, extraParam);
        }

        @Override
        public String getPublicAccessUrl(String bucketName, String targetPath, boolean useOriginHost) {
            return mock.getPublicAccessUrl(bucketName, targetPath, useOriginHost);
        }

        @Override
        public java.util.Map<String, String> getPublicAccessUrl(String bucketName, java.util.List<String> targetPath, boolean useOriginHost) {
            return mock.getPublicAccessUrl(bucketName, targetPath, useOriginHost);
        }

        @Override
        public <T extends StorageTemplateSign> T getTemporaryUploadSign(String bucketName, String targetPath, UploadLimit uploadLimit) {
            return mock.getTemporaryUploadSign(bucketName, targetPath, uploadLimit);
        }

        @Override
        public boolean createSymlink(String bucketName, String linkPath, String targetPath) {
            return mock.createSymlink(bucketName, linkPath, targetPath);
        }

        @Override
        public void close() throws java.io.IOException {
            // 委托给 mock, mock 默认不抛异常
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
        if (type == String.class) return "k";
        if (type == long.class) return 1L;
        if (type == Long.class) return 1L;
        if (type == boolean.class) return false;
        if (type == int.class) return 1;
        if (type == File.class) return new File("f");
        if (type == byte[].class) return new byte[0];
        if (type == InputStream.class) return new java.io.ByteArrayInputStream(new byte[0]);
        if (type == java.util.List.class) return new ArrayList<>();
        if (type == java.util.Map.class) return new HashMap<>();
        if (type == TemporaryAccessExtraParam.class) return TemporaryAccessExtraParam.builder().build();
        if (type == UploadLimit.class) return UploadLimit.builder().build();
        return null;
    }

    @Test
    public void objectStorageServiceDefaults() throws Exception {
        StubService stub = new StubService();
        for (Method m : ObjectStorageService.class.getDeclaredMethods()) {
            if (!m.isDefault() || m.isSynthetic()) continue;
            m.setAccessible(true);
            try {
                m.invoke(stub, args(m));
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw new AssertionError("default method " + m.getName() + " threw " + e.getCause(), e.getCause());
            }
        }
    }

    @Test
    public void dynamicObjectStorageServiceDefaults() throws Exception {
        ObjectStorageService storageService = Mockito.mock(ObjectStorageService.class);
        com.skrstop.framework.components.starter.objectStorage.service.impl.DynamicObjectStorageServiceImpl service =
                new com.skrstop.framework.components.starter.objectStorage.service.impl.DynamicObjectStorageServiceImpl(storageService);
        for (Method m : com.skrstop.framework.components.starter.objectStorage.service.DynamicObjectStorageService.class.getDeclaredMethods()) {
            if (!m.isDefault() || m.isSynthetic()) continue;
            m.setAccessible(true);
            try {
                m.invoke(service, args(m));
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw new AssertionError("default method " + m.getName() + " threw " + e.getCause(), e.getCause());
            }
        }
    }
}
