package com.skrstop.framework.components.starter.feign.protostuff.converter;

import com.skrstop.framework.components.core.common.response.*;
import com.skrstop.framework.components.core.common.response.core.IDataResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.CommonPageData;
import com.skrstop.framework.components.starter.feign.protostuff.configuration.GlobalFeignProperties;
import com.skrstop.framework.components.util.serialization.protobuf.ProtostuffUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author 蒋时华
 * @desc protostuff格式转换
 * @date 2019/12/2
 */
@SuppressWarnings("all")
@Slf4j
public class ProtostuffHttpMessageConverter extends AbstractHttpMessageConverter<Object>
        implements GenericHttpMessageConverter<Object> {

    public static final MediaType PROTOBUF_MEDIA_TYPE = new MediaType("application", "x-protobuf",
            StandardCharsets.UTF_8);
    private GlobalFeignProperties globalFeignProperties;

    public ProtostuffHttpMessageConverter(GlobalFeignProperties globalFeignProperties) {
//		super(PROTOBUF_MEDIA_TYPE, MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML);
        super(PROTOBUF_MEDIA_TYPE);
        this.globalFeignProperties = globalFeignProperties;
    }


    @Override
    protected boolean supports(Class<?> clazz) {
        return Object.class.isAssignableFrom(clazz);
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException {
        return ProtostuffUtil.deserialize(inputMessage.getBody(), clazz);
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException {
        FileCopyUtils.copy(ProtostuffUtil.serialize(object), outputMessage.getBody());
    }

    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return mediaType != null && PROTOBUF_MEDIA_TYPE.isCompatibleWith(mediaType);
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        Class<?> c;
        Object returnValue;
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            c = (Class<?>) parameterizedType.getRawType();
        } else {
            c = (Class<?>) type;
        }
        if (globalFeignProperties.getStopAutoRemoveGlobalResponse()) {
            // 关闭自动类型处理
            returnValue = ProtostuffUtil.deserialize(inputMessage.getBody(), c);
            return returnValue;
        }
        // 自动类型转换
        if (IResult.class.isAssignableFrom(c)) {
            // IResult
            return ProtostuffUtil.deserialize(inputMessage.getBody(), c);
        }
        if (Void.class.isAssignableFrom(c)) {
            return null;
        }
        Class<? extends IResult> iResultClass = null;
        if (CommonPageData.class.isAssignableFrom(c)) {
            PageListResult deserialize = ProtostuffUtil.deserialize(inputMessage.getBody(), PageListResult.class);
            return CommonPageData.builder()
                    .pageInfo(deserialize.getPageInfo())
                    .data(deserialize.getData())
                    .build();
        }
        IDataResult deserialize;
        if (List.class.isAssignableFrom(c)) {
            deserialize = ProtostuffUtil.deserialize(inputMessage.getBody(), ListResult.class);
        } else if (HashSet.class.isAssignableFrom(c)) {
            deserialize = ProtostuffUtil.deserialize(inputMessage.getBody(), HashSetResult.class);
        } else if (LinkedHashSet.class.isAssignableFrom(c)) {
            deserialize = ProtostuffUtil.deserialize(inputMessage.getBody(), LinkedSetResult.class);
        } else if (LinkedHashMap.class.isAssignableFrom(c)) {
            deserialize = ProtostuffUtil.deserialize(inputMessage.getBody(), LinkedMapResult.class);
        } else if (HashMap.class.isAssignableFrom(c)) {
            deserialize = ProtostuffUtil.deserialize(inputMessage.getBody(), MapResult.class);
        } else if (Collection.class.isAssignableFrom(c)) {
            deserialize = ProtostuffUtil.deserialize(inputMessage.getBody(), CollectionResult.class);
        } else if (Set.class.isAssignableFrom(c)) {
            deserialize = ProtostuffUtil.deserialize(inputMessage.getBody(), LinkedSetResult.class);
        } else if (Map.class.isAssignableFrom(c)) {
            deserialize = ProtostuffUtil.deserialize(inputMessage.getBody(), LinkedMapResult.class);
        } else {
            deserialize = ProtostuffUtil.deserialize(inputMessage.getBody(), Result.class);
        }
        return deserialize.getData();
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return mediaType != null && PROTOBUF_MEDIA_TYPE.isCompatibleWith(mediaType);
    }

    @Override
    public void write(Object t, Type type, MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        FileCopyUtils.copy(ProtostuffUtil.serialize(t), outputMessage.getBody());
    }

}
