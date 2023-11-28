package cn.auntec.framework.components.starter.feign.protostuff.autoconfigure;

import cn.auntec.framework.components.util.serialization.protostuff.ProtostuffUtil;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

public class ProtostuffFeignDecoder implements Decoder {

    @Override
    public Object decode(final Response response, Type type) throws IOException, FeignException {
        if (type instanceof Class || type instanceof ParameterizedType || type instanceof WildcardType) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class<?> c = (Class<?>) parameterizedType.getRawType();
                return ProtostuffUtil.deserialize(response.body().asInputStream(), c);
            }
            return ProtostuffUtil.deserialize(response.body().asInputStream(), type.getClass());
        }
        throw new DecodeException(response.status(), "type is not an instance of Class or ParameterizedType: " + type,
                response.request());
    }

}
