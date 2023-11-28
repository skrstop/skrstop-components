/*
 * Copyright 1999-2019 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.auntec.framework.components.starter.web.config.sentinel.webflux;

import cn.auntec.framework.components.core.common.response.DefaultResult;
import cn.auntec.framework.components.core.common.response.common.CommonResultCode;
import cn.auntec.framework.components.util.serialization.json.FastJsonUtil;
import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.BlockRequestHandler;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

/**
 * The default implementation of {@link BlockRequestHandler}.
 *
 * @author Eric Zhao
 * @since 1.5.0
 */
public class DefaultWebFluxBlockRequestHandler implements BlockRequestHandler {

    private static final String DEFAULT_BLOCK_MSG_PREFIX = "Blocked by Sentinel: ";

    @SneakyThrows
    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable ex) {
//        if (acceptsHtml(exchange)) {
//            return htmlErrorResponse(ex);
//        }
        // JSON result by default.
        return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(buildErrorResult(ex, exchange)));
    }

    private Mono<ServerResponse> htmlErrorResponse(Throwable ex) {
        return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                .contentType(MediaType.TEXT_PLAIN)
                .syncBody(DEFAULT_BLOCK_MSG_PREFIX + ex.getClass().getSimpleName());
    }

    private String buildErrorResult(Throwable ex, ServerWebExchange exchange) throws IOException {
        return FastJsonUtil.toJson(DefaultResult.Builder.result(CommonResultCode.BUSY));
    }

    /**
     * Reference from {@code DefaultErrorWebExceptionHandler} of Spring Boot.
     */
    private boolean acceptsHtml(ServerWebExchange exchange) {
        try {
            List<MediaType> acceptedMediaTypes = exchange.getRequest().getHeaders().getAccept();
            acceptedMediaTypes.remove(MediaType.ALL);
            MediaType.sortBySpecificityAndQuality(acceptedMediaTypes);
            return acceptedMediaTypes.stream()
                    .anyMatch(MediaType.TEXT_HTML::isCompatibleWith);
        } catch (InvalidMediaTypeException ex) {
            return false;
        }
    }
}
