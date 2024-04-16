package com.skrstop.framework.components.starter.feign.protostuff.interceptor;

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

    private boolean logInfoLevelForRequest = false;

    public OkHttpResponseLogInterceptor(boolean logInfoLevelForRequest) {
        this.logInfoLevelForRequest = logInfoLevelForRequest;
    }

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
            if (this.logInfoLevelForRequest) {
                log.info("okhttp请求值日志打印: {}", request);
            } else {
                log.debug("okhttp请求值日志打印: {}", request);
            }
            Response response = chain.proceed(request);
            if (this.logInfoLevelForRequest) {
                log.info("okhttp返回值日志打印: {}", response);
            } else {
                log.debug("okhttp返回值日志打印: {}", response);
            }
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

}
