package com.skrstop.framework.components.starter.web.utils;

import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.util.enums.CharSetEnum;
import com.skrstop.framework.components.util.enums.ContentTypeEnum;
import com.skrstop.framework.components.util.serialization.json.FastJsonUtil;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public static String DEFAULT_CHARSET = CharSetEnum.UTF8.toString();
    public static String DEFAULT_APPLICATION = ContentTypeEnum.APPLICATION_JSON_UTF8.toString();

    public static Mono<Void> fluxOut(ServerHttpResponse response, IResult IResult, Class errorCodeCls) throws IOException {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.getHeaders()
                .setAcceptCharset(CollectionUtil.newArrayList(Charset.forName(CharSetEnum.UTF8.toString())));
        String body = FastJsonUtil.toJson(errorCodeCls);
        DataBuffer wrap = response.bufferFactory().wrap(body.getBytes(CharSetEnum.UTF8.toString()));
        return response.writeWith(Flux.just(wrap));
    }

    public static Mono<Void> fluxOut(ServerHttpResponse response, IResult IResult, Class errorCodeCls, CharSetEnum charSetEnum,
                                     ContentTypeEnum contentTypeEnum) throws IOException {
        response.getHeaders().setContentType(MediaType.parseMediaType(contentTypeEnum.getContentType()));
        response.getHeaders()
                .setAcceptCharset(CollectionUtil.newArrayList(Charset.forName(charSetEnum.getCharSet())));
        String body = FastJsonUtil.toJson(errorCodeCls);
        DataBuffer wrap = response.bufferFactory().wrap(body.getBytes(charSetEnum.getCharSet()));
        return response.writeWith(Flux.just(wrap));
    }

    public static void out(HttpServletResponse response, IResult IResult, Class errorCodeCls) throws IOException {
        response.setCharacterEncoding(DEFAULT_CHARSET);
        response.setContentType(DEFAULT_APPLICATION);

        try (PrintWriter writer = response.getWriter()) {
            writer.print(FastJsonUtil.toJson(errorCodeCls));
        }
    }

    public static void out(HttpServletResponse response, IResult IResult, Class errorCodeCls, CharSetEnum charSetEnum,
                           ContentTypeEnum contentTypeEnum)
            throws IOException {
        response.setCharacterEncoding(charSetEnum.toString());
        response.setContentType(contentTypeEnum.toString());

        try (PrintWriter writer = response.getWriter()) {
            writer.print(FastJsonUtil.toJson(errorCodeCls));
        }
    }
}
