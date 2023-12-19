package com.skrstop.framework.components.util.serialization.protostuff;

import com.skrstop.framework.components.core.common.response.DefaultPageResult;
import com.skrstop.framework.components.core.common.response.DefaultResult;
import com.skrstop.framework.components.core.common.response.PageListResult;
import com.skrstop.framework.components.core.common.response.Result;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * protostuff 序列化工具
 *
 * @author 蒋时华
 * @date 2020-05-09 17:19:55
 */
@UtilityClass
public class ProtostuffUtil {

    /**
     * 线程局部变量
     */
    private static final ThreadLocal<LinkedBuffer> BUFFERS = new ThreadLocal();

    /**
     * schema缓存
     */
    private static Map<Class<?>, Schema<?>> SCHEMA_CACHED = new ConcurrentHashMap<>();

    /**
     * 序列化/反序列化包装类 Schema 对象
     */
    private static final Schema<SerializeDeserializeWrapper> WRAPPER_SCHEMA = ProtostuffUtil.getSchema(SerializeDeserializeWrapper.class);

    /**
     * 不支持直接序列化的类
     *
     * @param clazz
     * @return
     */
    public static boolean notSupported(Class<?> clazz) {
        // Protostuff 不支持序列化/反序列化数组、集合等对象,特殊处理
        return clazz.isArray()
                || Collection.class.isAssignableFrom(clazz)
                || Map.class.isAssignableFrom(clazz)
                || Set.class.isAssignableFrom(clazz);
    }

    /**
     * schema 缓存
     *
     * @param cls
     * @param <T>
     * @return
     */
    private static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) SCHEMA_CACHED.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            if (schema != null) {
                SCHEMA_CACHED.put(cls, schema);
            }
        }
        return schema;
    }

    /**
     * <p>
     * 序列化/反序列化对象包装类 专为基于 Protostuff 进行序列化/反序列化而定义。 Protostuff
     * 是基于POJO进行序列化和反序列化操作。 如果需要进行序列化/反序列化的对象不知道其类型，不能进行序列化/反序列化；
     * 比如Map、List、String、Enum等是不能进行正确的序列化/反序列化。
     * 因此需要映入一个包装类，把这些需要序列化/反序列化的对象放到这个包装类中。 这样每次 Protostuff
     * 都是对这个类进行序列化/反序列化,不会出现不能/不正常的操作出现
     * </p>
     *
     * @author butioy
     */
    static class SerializeDeserializeWrapper<T> {

        private T data;

        public static <T> SerializeDeserializeWrapper<T> builder(T data) {
            SerializeDeserializeWrapper<T> wrapper = new SerializeDeserializeWrapper<>();
            wrapper.setData(data);
            return wrapper;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

    }

    /**
     * 序列化对象
     *
     * @param obj 需要序列化的对象
     * @return 序列化后的二进制数组
     */
    @SuppressWarnings("unchecked")
    public static byte[] serialize(Object obj) {
        Class<?> clazz = (Class<?>) obj.getClass();
        LinkedBuffer buffer = BUFFERS.get();
        //存储buffer到线程局部变量中，避免每次序列化操作都分配内存提高序列化性能
        if (buffer == null) {
            buffer = LinkedBuffer.allocate(512);
            BUFFERS.set(buffer);
        }
        try {
            Object serializeObject = obj;
            Schema schema = WRAPPER_SCHEMA;
            if (ProtostuffUtil.notSupported(clazz)) {
                serializeObject = SerializeDeserializeWrapper.builder(obj);
            } else {
                schema = ProtostuffUtil.getSchema(clazz);
            }
            return ProtostuffIOUtil.toByteArray(serializeObject, schema, buffer);
        } finally {
            buffer.clear();
        }
    }

    /**
     * 反序列化对象
     *
     * @param data  需要反序列化的二进制数组
     * @param clazz 反序列化后的对象class
     * @param <T>   反序列化后的对象类型
     * @return 反序列化后的实例对象
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        if (ProtostuffUtil.notSupported(clazz)) {
            SerializeDeserializeWrapper<T> wrapper = new SerializeDeserializeWrapper<>();
            ProtostuffIOUtil.mergeFrom(data, wrapper, WRAPPER_SCHEMA);
            return wrapper.getData();
        } else {
            Schema<T> schema = ProtostuffUtil.getSchema(clazz);
            T message = schema.newMessage();
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        }
    }

    /**
     * 反序列化Result<T>
     *
     * @param data       需要反序列化的二进制数组
     * @param resultData Result中的data对象class
     * @param <T>        反序列化后的对象类型
     * @return 反序列化后的实例对象
     */
    public static <T> Result<T> deserializeToResult(byte[] data, Class<T> resultData) {
        return (Result<T>) ProtostuffUtil.deserialize(data, Result.class);
    }

    /**
     * 反序列化DefaultResult
     *
     * @param data 需要反序列化的二进制数组
     * @return 反序列化后的实例对象
     */
    public static DefaultResult deserializeToDefaultResult(byte[] data) {
        return ProtostuffUtil.deserialize(data, DefaultResult.class);
    }

    /**
     * 反序列化DefaultPageResult
     *
     * @param data 需要反序列化的二进制数组
     * @return 反序列化后的实例对象
     */
    public static DefaultPageResult deserializeToDefaultPageResult(byte[] data) {
        return ProtostuffUtil.deserialize(data, DefaultPageResult.class);
    }

    /**
     * 反序列化PageResult<T>
     *
     * @param data 需要反序列化的二进制数组
     * @return 反序列化后的实例对象
     */
    public static <T> PageListResult<T> deserializeToPageResult(byte[] data, Class<T> resultData) {
        return (PageListResult<T>) ProtostuffUtil.deserialize(data, PageListResult.class);
    }

    /**
     * 反序列化对象
     *
     * @param inputStream 需要反序列化的输入流
     * @param clazz       反序列化后的对象class
     * @param <T>         反序列化后的对象类型
     * @return 反序列化后的实例对象
     */
    public static <T> T deserialize(InputStream inputStream, Class<T> clazz) throws IOException {
        if (ProtostuffUtil.notSupported(clazz)) {
            SerializeDeserializeWrapper<T> wrapper = new SerializeDeserializeWrapper<>();
            ProtostuffIOUtil.mergeFrom(inputStream, wrapper, WRAPPER_SCHEMA);
            return wrapper.getData();
        } else {
            Schema<T> schema = ProtostuffUtil.getSchema(clazz);
            T message = schema.newMessage();
            ProtostuffIOUtil.mergeFrom(inputStream, message, schema);
            return message;
        }
    }

    /**
     * 反序列化Result<T>
     *
     * @param inputStream 需要反序列化的输入流
     * @param resultData  Result中的data对象class
     * @param <T>         反序列化后的对象类型
     * @return 反序列化后的实例对象
     */
    public static <T> Result<T> deserializeToResult(InputStream inputStream, Class<T> resultData) throws IOException {
        return (Result<T>) ProtostuffUtil.deserialize(inputStream, Result.class);
    }

    /**
     * 反序列化DefaultResult
     *
     * @param inputStream 需要反序列化的输入流
     * @return 反序列化后的实例对象
     */
    public static DefaultResult deserializeToDefaultResult(InputStream inputStream) throws IOException {
        return ProtostuffUtil.deserialize(inputStream, DefaultResult.class);
    }

    /**
     * 反序列化DefaultPageResult
     *
     * @param inputStream 需要反序列化的输入流
     * @return 反序列化后的实例对象
     */
    public static DefaultPageResult deserializeToDefaultPageResult(InputStream inputStream) throws IOException {
        return ProtostuffUtil.deserialize(inputStream, DefaultPageResult.class);
    }

    /**
     * 反序列化PageResult<T>
     *
     * @param inputStream 需要反序列化的输入流
     * @return 反序列化后的实例对象
     */
    public static <T> PageListResult<T> deserializeToPageListResult(InputStream inputStream, Class<T> resultData) throws IOException {
        return (PageListResult<T>) ProtostuffUtil.deserialize(inputStream, PageListResult.class);
    }


}
