package com.skrstop.framework.components.starter.web.configuration;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.skrstop.framework.components.core.common.response.DefaultResult;
import com.skrstop.framework.components.core.common.response.Result;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.util.value.data.StrUtil;
import com.skrstop.framework.components.util.value.stream.StreamUtil;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author 蒋时华
 * @date 2019-08-17 16:27:11
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass({FeignClient.class, Decoder.class})
@ConditionalOnProperty(value = "skrstop.response.config.enable-feign-trans-result-type-response", havingValue = "true", matchIfMissing = false)
public class FeignResponseAuthConfiguration {

    @Bean
    public Decoder customAutoResponseDecoder(ObjectProvider<HttpMessageConverterCustomizer> customizers
            , ObjectFactory<HttpMessageConverters> messageConverters) {
        return new Decoder() {
            private final OptionalDecoder defaultDelegate = new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters, customizers)));

            @Override
            public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
                Class<?> targetClass = (Class<?>) ((ParameterizedType) type).getRawType();
                if (DefaultResult.class.isAssignableFrom(targetClass)) {
                    // 无返回值
                    return null;
                }
                if (IResult.class.isAssignableFrom(targetClass)) {
                    // 本身带有外包装，无需特殊处理
                    return defaultDelegate.decode(response, type);
                }
                String responseJson = StreamUtil.inputStreamToString(response.body().asInputStream(), StandardCharsets.UTF_8.name());
                if (StrUtil.isBlank(responseJson)) {
                    return null;
                }
                Result<?> result = JSON.parseObject(responseJson, Result.class);
                if (ObjectUtil.isNull(result)
                        || ObjectUtil.isNull(result.getData())) {
                    return null;
                }
                return JSON.parseObject(result.getData().toString(), type);
            }
        };
    }
}
