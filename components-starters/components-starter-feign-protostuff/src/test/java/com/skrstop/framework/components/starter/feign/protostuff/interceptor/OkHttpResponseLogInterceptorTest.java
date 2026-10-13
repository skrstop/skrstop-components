package com.skrstop.framework.components.starter.feign.protostuff.interceptor;

import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class OkHttpResponseLogInterceptorTest {

    private final Request request = new Request.Builder().url("http://localhost/p").build();

    private Response response(int code) {
        return new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(code)
                .message("m")
                .build();
    }

    @Test
    public void successAndFailure() throws IOException {
        for (boolean logInfo : new boolean[]{true, false}) {
            OkHttpResponseLogInterceptor interceptor = new OkHttpResponseLogInterceptor(logInfo);
            Interceptor.Chain chain = Mockito.mock(Interceptor.Chain.class);
            Mockito.when(chain.request()).thenReturn(request);
            Mockito.when(chain.proceed(request)).thenReturn(response(200));
            Assert.assertSame(chain.proceed(request), interceptor.intercept(chain));

            Mockito.when(chain.proceed(request)).thenReturn(response(500));
            interceptor.intercept(chain);
        }
    }

    @Test(expected = IOException.class)
    public void proceedThrows() throws IOException {
        OkHttpResponseLogInterceptor interceptor = new OkHttpResponseLogInterceptor(true);
        Interceptor.Chain chain = Mockito.mock(Interceptor.Chain.class);
        Mockito.when(chain.request()).thenReturn(request);
        Mockito.when(chain.proceed(request)).thenThrow(new IOException("boom"));
        interceptor.intercept(chain);
    }
}
