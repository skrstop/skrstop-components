package com.skrstop.framework.components.starter.web.utils;

import com.skrstop.framework.components.core.common.response.Result;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.util.enums.CharSetEnum;
import com.skrstop.framework.components.util.enums.ContentTypeEnum;
import com.skrstop.framework.components.util.serialization.json.FastJsonUtil;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Response 输出内容
 *
 * @author 蒋时华
 * @date 2018/11/8
 */
public class ResponseOutUtil extends ServletUtil {

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
