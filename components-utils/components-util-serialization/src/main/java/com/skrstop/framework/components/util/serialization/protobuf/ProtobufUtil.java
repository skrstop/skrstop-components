package com.skrstop.framework.components.util.serialization.protobuf;

import com.skrstop.framework.components.core.common.response.DefaultPageResult;
import com.skrstop.framework.components.core.common.response.DefaultResult;
import com.skrstop.framework.components.core.common.response.PageListResult;
import com.skrstop.framework.components.core.common.response.Result;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;

/**
 * protobuf 序列化工具
 *
 * @author 蒋时华
 * @date 2020-05-09 17:19:55
 */
@UtilityClass
public class ProtobufUtil {

    /**
     * 序列化对象
     *
     * @param obj 需要序列化的对象
     * @return 序列化后的二进制数组
     */
    @SuppressWarnings("unchecked")
    public static byte[] serialize(Object obj) {
        Class<?> clazz = (Class<?>) obj.getClass();
        LinkedBuffer buffer = ProtostuffUtil.BUFFERS.get();
        //存储buffer到线程局部变量中，避免每次序列化操作都分配内存提高序列化性能
        if (buffer == null) {
            buffer = LinkedBuffer.allocate(512);
            ProtostuffUtil.BUFFERS.set(buffer);
        }
        try {
            Object serializeObject = obj;
            Schema schema = ProtostuffUtil.WRAPPER_SCHEMA;
            /**
             * @see ProtobufIOUtil
             */
            if (ProtostuffUtil.notSupported(clazz)) {
                serializeObject = ProtostuffUtil.SerializeDeserializeWrapper.builder(obj);
            } else {
                schema = ProtostuffUtil.getSchema(clazz);
            }
            return ProtobufIOUtil.toByteArray(serializeObject, schema, buffer);
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
            ProtostuffUtil.SerializeDeserializeWrapper<T> wrapper = new ProtostuffUtil.SerializeDeserializeWrapper<>();
            ProtobufIOUtil.mergeFrom(data, wrapper, ProtostuffUtil.WRAPPER_SCHEMA);
            return wrapper.getData();
        } else {
            Schema<T> schema = ProtostuffUtil.getSchema(clazz);
            T message = schema.newMessage();
            ProtobufIOUtil.mergeFrom(data, message, schema);
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
        return (Result<T>) ProtobufUtil.deserialize(data, Result.class);
    }

    /**
     * 反序列化DefaultResult
     *
     * @param data 需要反序列化的二进制数组
     * @return 反序列化后的实例对象
     */
    public static DefaultResult deserializeToDefaultResult(byte[] data) {
        return ProtobufUtil.deserialize(data, DefaultResult.class);
    }

    /**
     * 反序列化DefaultPageResult
     *
     * @param data 需要反序列化的二进制数组
     * @return 反序列化后的实例对象
     */
    public static DefaultPageResult deserializeToDefaultPageResult(byte[] data) {
        return ProtobufUtil.deserialize(data, DefaultPageResult.class);
    }

    /**
     * 反序列化PageResult<T>
     *
     * @param data 需要反序列化的二进制数组
     * @return 反序列化后的实例对象
     */
    public static <T> PageListResult<T> deserializeToPageResult(byte[] data, Class<T> resultData) {
        return (PageListResult<T>) ProtobufUtil.deserialize(data, PageListResult.class);
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
            ProtostuffUtil.SerializeDeserializeWrapper<T> wrapper = new ProtostuffUtil.SerializeDeserializeWrapper<>();
            ProtostuffIOUtil.mergeFrom(inputStream, wrapper, ProtostuffUtil.WRAPPER_SCHEMA);
            return wrapper.getData();
        } else {
            Schema<T> schema = ProtostuffUtil.getSchema(clazz);
            T message = schema.newMessage();
            ProtobufIOUtil.mergeFrom(inputStream, message, schema);
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
        return (Result<T>) ProtobufUtil.deserialize(inputStream, Result.class);
    }

    /**
     * 反序列化DefaultResult
     *
     * @param inputStream 需要反序列化的输入流
     * @return 反序列化后的实例对象
     */
    public static DefaultResult deserializeToDefaultResult(InputStream inputStream) throws IOException {
        return ProtobufUtil.deserialize(inputStream, DefaultResult.class);
    }

    /**
     * 反序列化DefaultPageResult
     *
     * @param inputStream 需要反序列化的输入流
     * @return 反序列化后的实例对象
     */
    public static DefaultPageResult deserializeToDefaultPageResult(InputStream inputStream) throws IOException {
        return ProtobufUtil.deserialize(inputStream, DefaultPageResult.class);
    }

    /**
     * 反序列化PageResult<T>
     *
     * @param inputStream 需要反序列化的输入流
     * @return 反序列化后的实例对象
     */
    public static <T> PageListResult<T> deserializeToPageListResult(InputStream inputStream, Class<T> resultData) throws IOException {
        return (PageListResult<T>) ProtobufUtil.deserialize(inputStream, PageListResult.class);
    }


}
