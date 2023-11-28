package cn.auntec.framework.components.starter.web.exception.global.interceptor.exception;

import cn.auntec.framework.components.core.common.response.Result;
import cn.auntec.framework.components.core.common.response.common.CommonResultCode;
import cn.auntec.framework.components.core.common.response.core.IResult;
import cn.auntec.framework.components.core.common.util.EnumCodeUtil;
import cn.auntec.framework.components.core.exception.util.ThrowableStackTraceUtil;
import cn.auntec.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import cn.auntec.framework.components.util.serialization.json.FastJsonUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:08:48
 */
@Slf4j
@NoArgsConstructor
public class HttpClientErrorExceptionInterceptor implements ExceptionHandlerInterceptor {
    @Override
    public IResult execute(Exception e) {
        if (e instanceof HttpClientErrorException) {
            log.error(ThrowableStackTraceUtil.getStackTraceStr(e));
            HttpClientErrorException httpClientErrorException = (HttpClientErrorException) e;
            String httpResultStr = null;
            try {
                httpResultStr = new String(httpClientErrorException.getResponseBodyAsByteArray(), "UTF-8");
                HashMap<String, String> map = FastJsonUtil.toBeanForHashMap(httpResultStr);
                String message = null;
                if (map != null) {
                    message = map.get("message");
                }
                IResult error = EnumCodeUtil.transferEnumCode(CommonResultCode.FAIL);
                error.setMessage(message);
                return error;
            } catch (UnsupportedEncodingException ex) {
                return Result.Builder.result(CommonResultCode.FAIL, ex);
            }
        }
        return null;
    }
}
