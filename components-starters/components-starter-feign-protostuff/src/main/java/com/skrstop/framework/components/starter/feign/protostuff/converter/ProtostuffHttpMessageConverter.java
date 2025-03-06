package com.skrstop.framework.components.starter.feign.protostuff.converter;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.skrstop.framework.components.core.common.response.*;
import com.skrstop.framework.components.core.common.response.core.IDataResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.PageData;
import com.skrstop.framework.components.starter.feign.protostuff.configuration.GlobalFeignProperties;
import com.skrstop.framework.components.starter.feign.protostuff.configuration.interceptor.DynamicFeignClientMethodContextHolder;
import com.skrstop.framework.components.starter.spring.support.bean.SpringUtil;
import com.skrstop.framework.components.starter.web.configuration.GlobalResponseProperties;
import com.skrstop.framework.components.util.constant.FeignConst;
import com.skrstop.framework.components.util.serialization.protobuf.ProtostuffUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    public static final MediaType PROTOBUF_MEDIA_TYPE = new MediaType("application", "x-protobuf", StandardCharsets.UTF_8);
    private GlobalFeignProperties globalFeignProperties;
    private boolean webResponseFeignSupport = true;

    public ProtostuffHttpMessageConverter(GlobalFeignProperties globalFeignProperties) {
//		super(PROTOBUF_MEDIA_TYPE, MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML);
        super(PROTOBUF_MEDIA_TYPE);
        this.globalFeignProperties = globalFeignProperties;
        try {
            GlobalResponseProperties globalResponseProperties = SpringUtil.getBean(GlobalResponseProperties.class);
            webResponseFeignSupport = globalResponseProperties.isSupportFeign();
        } catch (Exception | Error e) {
            // 未使用starter-web包，无法使用自适应类型转换
            webResponseFeignSupport = false;
        }
    }


    @Override
    protected boolean supports(Class<?> clazz) {
        return Object.class.isAssignableFrom(clazz);
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException {
        return ProtostuffUtil.deserializeWithUncompress(inputMessage.getBody(), clazz, this.globalFeignProperties.getGzipCompress());
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException {
        FileCopyUtils.copy(ProtostuffUtil.serializeWithCompress(object, this.globalFeignProperties.getGzipCompress()), outputMessage.getBody());
    }

    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        boolean feignProtobufType = mediaType != null && PROTOBUF_MEDIA_TYPE.isCompatibleWith(mediaType);
        return feignProtobufType || this.isUseFign();
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
        if (ObjectUtil.isNotNull(contextClass)) {
            // 可能是controller
            return ProtostuffUtil.deserializeWithUncompress(inputMessage.getBody(), c, this.globalFeignProperties.getGzipCompress());
        }
        // 关闭自动类型处理
        if (globalFeignProperties.isStopAutoRemoveGlobalResponse()) {
            if (!IResult.class.isAssignableFrom(c)) {
                log.error("当前已禁用自动类型转换，请查看是否设置了：skrstop.feign.config.stop-auto-remove-global-response = true 或 使用IResult类型接收数据");
                return ProtostuffUtil.deserializeWithUncompress(inputMessage.getBody(), Result.class, this.globalFeignProperties.getGzipCompress());
            }
            return ProtostuffUtil.deserializeWithUncompress(inputMessage.getBody(), c, this.globalFeignProperties.getGzipCompress());
        }
        if (!webResponseFeignSupport) {
            log.error("当前web已禁用feign包装支持，请查看是否设置了：skrstop.response.config.support-feign = false");
            return ProtostuffUtil.deserializeWithUncompress(inputMessage.getBody(), c, this.globalFeignProperties.getGzipCompress());
        }
        // 自动类型转换
        if (IResult.class.isAssignableFrom(c)) {
            // IResult
            return ProtostuffUtil.deserializeWithUncompress(inputMessage.getBody(), c, this.globalFeignProperties.getGzipCompress());
        }
        if (Void.class.isAssignableFrom(c)) {
            return null;
        }
        Class<? extends IResult> iResultClass = null;
        if (PageData.class.isAssignableFrom(c)) {
            PageListResult deserialize = ProtostuffUtil.deserializeWithUncompress(inputMessage.getBody(), PageListResult.class, this.globalFeignProperties.getGzipCompress());
            return deserialize.getData();
        }
        IDataResult deserialize;
        if (List.class.isAssignableFrom(c)) {
            deserialize = ProtostuffUtil.deserializeWithUncompress(inputMessage.getBody(), ListResult.class, this.globalFeignProperties.getGzipCompress());
        } else if (HashSet.class.isAssignableFrom(c)) {
            deserialize = ProtostuffUtil.deserializeWithUncompress(inputMessage.getBody(), HashSetResult.class, this.globalFeignProperties.getGzipCompress());
        } else if (LinkedHashSet.class.isAssignableFrom(c)) {
            deserialize = ProtostuffUtil.deserializeWithUncompress(inputMessage.getBody(), LinkedSetResult.class, this.globalFeignProperties.getGzipCompress());
        } else if (LinkedHashMap.class.isAssignableFrom(c)) {
            deserialize = ProtostuffUtil.deserializeWithUncompress(inputMessage.getBody(), LinkedMapResult.class, this.globalFeignProperties.getGzipCompress());
        } else if (HashMap.class.isAssignableFrom(c)) {
            deserialize = ProtostuffUtil.deserializeWithUncompress(inputMessage.getBody(), MapResult.class, this.globalFeignProperties.getGzipCompress());
        } else if (Collection.class.isAssignableFrom(c)) {
            deserialize = ProtostuffUtil.deserializeWithUncompress(inputMessage.getBody(), CollectionResult.class, this.globalFeignProperties.getGzipCompress());
        } else if (Set.class.isAssignableFrom(c)) {
            deserialize = ProtostuffUtil.deserializeWithUncompress(inputMessage.getBody(), LinkedSetResult.class, this.globalFeignProperties.getGzipCompress());
        } else if (Map.class.isAssignableFrom(c)) {
            deserialize = ProtostuffUtil.deserializeWithUncompress(inputMessage.getBody(), LinkedMapResult.class, this.globalFeignProperties.getGzipCompress());
        } else {
            deserialize = ProtostuffUtil.deserializeWithUncompress(inputMessage.getBody(), Result.class, this.globalFeignProperties.getGzipCompress());
        }
        if (deserialize.isFailed() && globalFeignProperties.isLogErrorAutoRemoveGlobalResponse()) {
            log.error("feign调用失败, 错误响应：{}", deserialize);
        }
        return deserialize.getData();
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        boolean feignProtobufType = mediaType != null && PROTOBUF_MEDIA_TYPE.isCompatibleWith(mediaType);
        return feignProtobufType || this.isUseFign() || DynamicFeignClientMethodContextHolder.peek();
    }

    private boolean isUseFign() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            String header = request.getHeader(FeignConst.USE_FEIGN_NAME);
            return BooleanUtil.toBoolean(header);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void write(Object t, Type type, MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        FileCopyUtils.copy(ProtostuffUtil.serializeWithCompress(t, globalFeignProperties.getGzipCompress()), outputMessage.getBody());
    }

}
