package com.skrstop.framework.components.util.http;

import cn.hutool.http.Method;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class HttpClientUtilTest {

    private static final String URL = "http://localhost:18080/test";

    @Test
    public void testJdkRequests() {
        Assert.assertEquals(Method.GET, HttpClientUtil.getJdk(URL).getMethod());
        Assert.assertEquals(Method.POST, HttpClientUtil.postJdk(URL).getMethod());
        Assert.assertEquals(Method.HEAD, HttpClientUtil.headJdk(URL).getMethod());
        Assert.assertEquals(Method.DELETE, HttpClientUtil.deleteJdk(URL).getMethod());
        Assert.assertEquals(Method.PATCH, HttpClientUtil.patchJdk(URL).getMethod());
        Assert.assertEquals(Method.PUT, HttpClientUtil.putJdk(URL).getMethod());
        Assert.assertEquals(Method.OPTIONS, HttpClientUtil.optionsJdk(URL).getMethod());
        Assert.assertEquals(Method.TRACE, HttpClientUtil.traceJdk(URL).getMethod());
    }

    @Test
    public void testOkHttpRequests() {
        Assert.assertNotNull(HttpClientUtil.getOkHttp(URL));
        Assert.assertNotNull(HttpClientUtil.deleteOkHttp(URL));
        Assert.assertNotNull(HttpClientUtil.patchOkHttp(URL));
        Assert.assertNotNull(HttpClientUtil.postOkHttp(URL));
        Assert.assertNotNull(HttpClientUtil.putOkHttp(URL));
    }

}
