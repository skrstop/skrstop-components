package com.zoe.framework.components.starter.web.response.webflux;

import com.zoe.framework.components.core.common.response.DefaultResult;
import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.starter.web.configuration.GlobalResponseProperties;
import com.zoe.framework.components.starter.web.response.DisableGlobalResponse;
import com.zoe.framework.components.starter.web.response.DisableTransResultTypeResponse;
import com.zoe.framework.components.starter.web.response.core.ResponseHandleChainPattern;
import com.zoe.framework.components.util.constant.FeignConst;
import com.zoe.framework.components.util.constant.StringPoolConst;
import com.zoe.framework.components.util.value.data.CollectionUtil;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import com.zoe.framework.components.util.value.url.UrlFilterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * WebMvcConfig class
 *
 * @author 蒋时华
 * @date 2018/11/22
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@EnableConfigurationProperties(GlobalResponseProperties.class)
@ConditionalOnClass(ResponseBodyResultHandler.class)
@Slf4j
public class ReturnResponseAutoConfiguration {

    private final GlobalResponseProperties globalResponseProperties;
    private final ResponseHandleChainPattern responseHandleChainPattern;

    public ReturnResponseAutoConfiguration(GlobalResponseProperties globalResponseProperties, ResponseHandleChainPattern responseHandleChainPattern) {
        this.globalResponseProperties = globalResponseProperties;
        this.responseHandleChainPattern = responseHandleChainPattern;
    }

    @Bean
    @Primary
    public ResponseBodyResultHandler responseBodyResultHandler(
            @Qualifier("webFluxAdapterRegistry") ReactiveAdapterRegistry reactiveAdapterRegistry,
            ServerCodecConfigurer serverCodecConfigurer,
            @Qualifier("webFluxContentTypeResolver") RequestedContentTypeResolver contentTypeResolver) {
        return new ResponseBodyResultHandler(serverCodecConfigurer.getWriters(),
                contentTypeResolver, reactiveAdapterRegistry) {
            @Override
            public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
                Object returnValue = result.getReturnValue();
                MethodParameter returnType = result.getReturnTypeSource();
                ServerHttpRequest request = exchange.getRequest();
                HttpHeaders headers = request.getHeaders();
                if (globalResponseProperties == null || globalResponseProperties.getEnable()) {
                    // 使用 全局返回值
                    Method method = returnType.getMethod();
                    boolean validPath = false;
                    try {
                        // 当前路径包含在NotTransResultList的集合中，不进行全局替换。
                        validPath = UrlFilterUtil.valid(ObjectUtil.isNull(globalResponseProperties) ? new ArrayList<>() : globalResponseProperties.getDisableGlobalResponseList()
                                , request.getURI().getRawPath());
                    } catch (Exception e) {
                        log.error("返回值处理异常：", e);
                    }
                    DisableGlobalResponse disableGlobalResponseMethod = method.getAnnotation(DisableGlobalResponse.class);
                    DisableGlobalResponse disableGlobalResponseClass = method.getClass().getAnnotation(DisableGlobalResponse.class);
                    if (!validPath && disableGlobalResponseMethod == null && disableGlobalResponseClass == null) {
                        // 判断是否支持feign
                        List<String> str = headers.get(FeignConst.USE_FEIGN_NAME);
                        String useFeign = StringPoolConst.EMPTY;
                        if (CollectionUtil.isNotEmpty(str)) {
                            useFeign = str.get(0);
                        }
                        if (FeignConst.USE_FEIGN_VALUE.equals(useFeign)
                                && ObjectUtil.isNotNull(globalResponseProperties)
                                && !globalResponseProperties.getSupportFeign()) {
                            // 不支持feign调用返回值的封装
                        } else {
                            // returnType parameterType
                            returnType = new IResultMethodParameter(returnType);
                            // mono
                            // // FIXME: 2021/1/11 目前先这样写，后面在完善，目前暂未使用，临时写法
                            // 是否关闭result类型的自动转换
                            DisableTransResultTypeResponse disableTransResultTypeResponseMethod = method.getAnnotation(DisableTransResultTypeResponse.class);
                            DisableTransResultTypeResponse disableTransResultTypeResponseClass = method.getClass().getAnnotation(DisableTransResultTypeResponse.class);
                            boolean transResultResponse = disableTransResultTypeResponseMethod == null && disableTransResultTypeResponseClass == null;
                            if (returnValue instanceof Mono) {
                                Mono returnValueMono = (Mono) returnValue;
                                returnValue = returnValueMono
                                        .defaultIfEmpty(DefaultResult.Builder.success())
                                        .map(item -> responseHandleChainPattern.execute(item, transResultResponse));
                            } else if (returnValue instanceof Flux) {
                                Flux returnValueFlux = (Flux) returnValue;
                                returnValue = returnValueFlux
                                        .defaultIfEmpty(DefaultResult.Builder.success())
                                        .map(item -> responseHandleChainPattern.execute(item, transResultResponse));
                            } else {
                                // other
                                returnValue = responseHandleChainPattern.execute(returnValue, transResultResponse);
                            }
                        }
                    }
                }
                return writeBody(returnValue, returnType, exchange);
            }
        };
    }

    /**
     * 重写{@link MethodParameter}, 用户返回值类型的校验
     */
    static final class IResultMethodParameter extends MethodParameter {

        public IResultMethodParameter(MethodParameter original) {
            super(original);
        }

        @Override
        public Class<?> getParameterType() {
            return IResult.class;
        }

        @Override
        public Type getGenericParameterType() {
            return IResult.class;
        }
    }

}
