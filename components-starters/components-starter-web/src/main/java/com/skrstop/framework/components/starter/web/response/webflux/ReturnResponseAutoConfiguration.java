package com.skrstop.framework.components.starter.web.response.webflux;

import com.skrstop.framework.components.core.common.response.DefaultResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.starter.common.util.AnnoFindUtil;
import com.skrstop.framework.components.starter.web.configuration.GlobalResponseProperties;
import com.skrstop.framework.components.starter.web.response.DisableGlobalResponse;
import com.skrstop.framework.components.starter.web.response.DisableTransResultTypeResponse;
import com.skrstop.framework.components.starter.web.response.core.ResponseHandleChainPattern;
import com.skrstop.framework.components.util.constant.FeignConst;
import com.skrstop.framework.components.util.constant.StringPoolConst;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.url.UrlFilterUtil;
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
                if (globalResponseProperties != null && !globalResponseProperties.isEnable()) {
                    return writeBody(returnValue, returnType, exchange);
                }
                // 使用 全局返回值
                boolean validPath = false;
                try {
                    // 当前路径包含在NotTransResultList的集合中，不进行全局替换。
                    validPath = UrlFilterUtil.valid(ObjectUtil.isNull(globalResponseProperties) ? new ArrayList<>() : globalResponseProperties.getDisableGlobalResponseList()
                            , request.getURI().getRawPath());
                } catch (Exception e) {
                    log.error("返回值处理异常：", e);
                }
                if (validPath) {
                    return writeBody(returnValue, returnType, exchange);
                }
                boolean disableGlobalResponse = AnnoFindUtil.has(returnType.getMethod(), DisableGlobalResponse.class);
                if (disableGlobalResponse) {
                    return writeBody(returnValue, returnType, exchange);
                }
                // 判断是否支持feign
                List<String> str = headers.get(FeignConst.USE_FEIGN_NAME);
                String useFeign = StringPoolConst.EMPTY;
                if (CollectionUtil.isNotEmpty(str)) {
                    useFeign = str.get(0);
                }
                if (FeignConst.USE_FEIGN_VALUE.equals(useFeign)
                        && ObjectUtil.isNotNull(globalResponseProperties)
                        && !globalResponseProperties.isSupportFeign()) {
                    // 不支持feign调用返回值的封装
                    return writeBody(returnValue, returnType, exchange);
                }
                // 针对IResult返回类型，是否需要处理
                returnType = new IResultMethodParameter(returnType);
                // 是否关闭result类型的自动转换
                boolean disableTransResultTypeResponse = AnnoFindUtil.has(returnType.getMethod(), DisableTransResultTypeResponse.class)
                        || globalResponseProperties.isDisableGlobalTransResultTypeResponse();
                if (returnValue instanceof Mono) {
                    Mono returnValueMono = (Mono) returnValue;
                    returnValue = returnValueMono
                            .defaultIfEmpty(DefaultResult.Builder.success())
                            .map(item -> responseHandleChainPattern.execute(item, disableTransResultTypeResponse));
                } else if (returnValue instanceof Flux) {
                    Flux returnValueFlux = (Flux) returnValue;
                    returnValue = returnValueFlux
                            .defaultIfEmpty(DefaultResult.Builder.success())
                            .map(item -> responseHandleChainPattern.execute(item, disableTransResultTypeResponse));
                } else {
                    // other
                    returnValue = responseHandleChainPattern.execute(returnValue, disableTransResultTypeResponse);
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
