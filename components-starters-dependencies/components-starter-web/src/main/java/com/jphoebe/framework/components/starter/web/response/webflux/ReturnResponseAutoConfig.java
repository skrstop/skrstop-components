package com.jphoebe.framework.components.starter.web.response.webflux;

import com.jphoebe.framework.components.core.common.response.DefaultResult;
import com.jphoebe.framework.components.core.common.response.core.IResult;
import com.jphoebe.framework.components.starter.web.config.GlobalResponseConfig;
import com.jphoebe.framework.components.starter.web.response.DisableTransResultResponse;
import com.jphoebe.framework.components.starter.web.response.NoGlobalResponse;
import com.jphoebe.framework.components.starter.web.response.core.ResponseHandleChainPattern;
import com.jphoebe.framework.components.util.constant.FeignConst;
import com.jphoebe.framework.components.util.constant.StringPoolConst;
import com.jphoebe.framework.components.util.value.data.CollectionUtil;
import com.jphoebe.framework.components.util.value.data.ObjectUtil;
import com.jphoebe.framework.components.util.value.url.UrlFilterUtil;
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
@EnableConfigurationProperties(GlobalResponseConfig.class)
@ConditionalOnClass(ResponseBodyResultHandler.class)
@Slf4j
public class ReturnResponseAutoConfig {

    private final GlobalResponseConfig globalResponseConfig;
    private final ResponseHandleChainPattern responseHandleChainPattern;

    public ReturnResponseAutoConfig(GlobalResponseConfig globalResponseConfig, ResponseHandleChainPattern responseHandleChainPattern) {
        this.globalResponseConfig = globalResponseConfig;
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
                if (globalResponseConfig == null || globalResponseConfig.getEnable()) {
                    // 使用 全局返回值
                    Method method = returnType.getMethod();
                    boolean validPath = false;
                    try {
                        // 当前路径包含在NotTransResultList的集合中，不进行全局替换。
                        validPath = UrlFilterUtil.valid(ObjectUtil.isNull(globalResponseConfig) ? new ArrayList<>() : globalResponseConfig.getNotTransResultList()
                                , request.getURI().getRawPath());
                    } catch (Exception e) {
                        log.error("返回值处理异常：", e);
                    }
                    NoGlobalResponse noGlobalResponseMethod = method.getAnnotation(NoGlobalResponse.class);
                    NoGlobalResponse noGlobalResponseClass = method.getClass().getAnnotation(NoGlobalResponse.class);
                    if (!validPath && noGlobalResponseMethod == null && noGlobalResponseClass == null) {
                        // 判断是否支持feign
                        List<String> str = headers.get(FeignConst.USE_FEIGN_NAME);
                        String useFeign = StringPoolConst.EMPTY;
                        if (CollectionUtil.isNotEmpty(str)) {
                            useFeign = str.get(0);
                        }
                        if (FeignConst.USE_FEIGN_VALUE.equals(useFeign)
                                && ObjectUtil.isNotNull(globalResponseConfig)
                                && !globalResponseConfig.getSupportFeign()) {
                            // 不支持feign调用返回值的封装
                        } else {
                            // returnType parameterType
                            returnType = new IResultMethodParameter(returnType);
                            // mono
                            // // FIXME: 2021/1/11 目前先这样写，后面在完善，目前暂未使用，临时写法
                            // 是否关闭result类型的自动转换
                            DisableTransResultResponse disableTransResultResponseMethod = method.getAnnotation(DisableTransResultResponse.class);
                            DisableTransResultResponse disableTransResultResponseClass = method.getClass().getAnnotation(DisableTransResultResponse.class);
                            boolean transResultResponse = disableTransResultResponseMethod == null && disableTransResultResponseClass == null;
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
