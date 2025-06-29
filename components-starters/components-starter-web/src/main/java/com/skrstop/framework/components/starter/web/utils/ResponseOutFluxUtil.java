package com.skrstop.framework.components.starter.web.utils;

import com.skrstop.framework.components.core.common.response.Result;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.util.enums.CharSetEnum;
import com.skrstop.framework.components.util.enums.ContentTypeEnum;
import com.skrstop.framework.components.util.serialization.json.FastJsonUtil;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Response 输出内容
 *
 * @author 蒋时华
 * @date 2018/11/8
 */
public class ResponseOutFluxUtil {

    public static Mono<Void> fluxOut(ServerHttpResponse response, IResult IResult, HttpStatus httpStatus) throws IOException {
        response.setStatusCode(httpStatus);
        return fluxOut(response, IResult);
    }

    public static Mono<Void> fluxOut(ServerHttpResponse response, IResult IResult) throws IOException {
        return fluxOut(response, IResult, CharSetEnum.UTF8, ContentTypeEnum.APPLICATION_JSON_UTF8);
    }

    public static Mono<Void> fluxOut(ServerHttpResponse response, IResult IResult, CharSetEnum charSetEnum, ContentTypeEnum contentTypeEnum) throws IOException {
        response.getHeaders().setContentType(MediaType.parseMediaType(contentTypeEnum.getContentType()));
        response.getHeaders()
                .setAcceptCharset(CollectionUtil.newArrayList(Charset.forName(charSetEnum.getCharSet())));
        String body = FastJsonUtil.toJson(new Result<>(IResult));
        DataBuffer wrap = response.bufferFactory().wrap(body.getBytes(charSetEnum.getCharSet()));
        return response.writeWith(Flux.just(wrap));
    }

}
