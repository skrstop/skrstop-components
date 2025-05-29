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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

/**
 * Response 输出内容
 *
 * @author 蒋时华
 * @date 2018/11/8
 */
public class ResponseOutUtil extends ServletUtil {

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

    public static void out(HttpServletResponse response, IResult IResult, HttpStatus httpStatus) throws IOException {
        out(response, IResult, CharSetEnum.UTF8, ContentTypeEnum.APPLICATION_JSON_UTF8, httpStatus);
    }

    public static void out(HttpServletResponse response, IResult IResult) throws IOException {
        out(response, IResult, CharSetEnum.UTF8, ContentTypeEnum.APPLICATION_JSON_UTF8, HttpStatus.OK);
    }

    public static void out(HttpServletResponse response, IResult IResult, CharSetEnum charSetEnum, ContentTypeEnum contentTypeEnum, HttpStatus httpStatus)
            throws IOException {
        response.setCharacterEncoding(charSetEnum.toString());
        response.setContentType(contentTypeEnum.toString());
        response.setStatus(httpStatus.value());

        try (PrintWriter writer = response.getWriter()) {
            writer.print(FastJsonUtil.toJson(new Result<>(IResult)));
        }
    }
}
