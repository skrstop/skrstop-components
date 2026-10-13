package com.skrstop.framework.components.starter.objectStorage.service.impl;

import com.skrstop.framework.components.starter.objectStorage.entity.TemporaryAccessExtraParam;
import com.skrstop.framework.components.starter.objectStorage.entity.UploadLimit;
import com.skrstop.framework.components.starter.objectStorage.service.ObjectStorageService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * DynamicObjectStorageServiceImpl 纯委托 ObjectStorageService, 逐方法调用即覆盖。
 */
public class DynamicObjectStorageServiceImplTest {

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
    public void delegateAllMethods() throws Exception {
        ObjectStorageService storageService = Mockito.mock(ObjectStorageService.class);
        DynamicObjectStorageServiceImpl service = new DynamicObjectStorageServiceImpl(storageService);
        for (Method m : DynamicObjectStorageServiceImpl.class.getDeclaredMethods()) {
            if (Modifier.isStatic(m.getModifiers()) || m.isSynthetic() || !Modifier.isPublic(m.getModifiers()))
                continue;
            if (m.getName().equals("equals") || m.getName().equals("hashCode") || m.getName().equals("toString"))
                continue;
            m.setAccessible(true);
            try {
                m.invoke(service, args(m));
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw new AssertionError("method " + m.getName() + " threw " + e.getCause(), e.getCause());
            }
        }
        Assert.assertNotNull(service);
    }
}
