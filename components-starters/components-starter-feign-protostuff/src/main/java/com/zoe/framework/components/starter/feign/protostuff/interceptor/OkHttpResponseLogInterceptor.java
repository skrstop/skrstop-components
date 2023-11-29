package com.zoe.framework.components.starter.feign.protostuff.interceptor;

import feign.RequestTemplate;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author 蒋时华
 * @date 2020-05-13 19:04:25
 */
@Slf4j
public class OkHttpResponseLogInterceptor implements Interceptor {

    /**
     * ˚
     * 这里记录所有，根据实际情况选择合适的日志level
     * 注意：使用了http2，但是feign还是显示http1, 不是http2没有使用，而是feign日志打印默认写死Http/1.1
     * 具体日志代码，请看：
     *
     * @see feign.SynchronousMethodHandler#executeAndDecode(RequestTemplate template, feign.Request.Options options)
     * @see feign.Request#toString()
     */
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        try {
            log.debug("okhttp请求值日志打印: {}", request.toString());
            Response response = chain.proceed(request);
            log.debug("okhttp返回值日志打印: {}", response.toString());
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

}
