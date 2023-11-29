package com.zoe.framework.components.util.http;

import cn.hutool.http.HttpRequest;

/**
 * HttpClient
 *
 * @author 蒋时华
 * @date 2019/9/11
 */
public class HttpClientUtil {

    /**
     * 使用jdkHttpClient get请求
     *
     * @param url
     * @return
     */
    public static HttpRequest getJdk(String url) {
        return HttpRequest.get(url);
    }

    /**
     * 使用jdkHttpClient post请求
     *
     * @param url
     * @return
     */
    public static HttpRequest postJdk(String url) {
        return HttpRequest.post(url);
    }

    /**
     * 使用jdkHttpClient head请求
     *
     * @param url
     * @return
     */
    public static HttpRequest headJdk(String url) {
        return HttpRequest.head(url);
    }

    /**
     * 使用jdkHttpClient delete请求
     *
     * @param url
     * @return
     */
    public static HttpRequest deleteJdk(String url) {
        return HttpRequest.delete(url);
    }

    /**
     * 使用jdkHttpClient patch请求
     *
     * @param url
     * @return
     */
    public static HttpRequest patchJdk(String url) {
        return HttpRequest.patch(url);
    }

    /**
     * 使用jdkHttpClient put请求
     *
     * @param url
     * @return
     */
    public static HttpRequest putJdk(String url) {
        return HttpRequest.put(url);
    }

    /**
     * 使用jdkHttpClient options请求
     *
     * @param url
     * @return
     */
    public static HttpRequest optionsJdk(String url) {
        return HttpRequest.options(url);
    }

    /**
     * 使用jdkHttpClient trace请求
     *
     * @param url
     * @return
     */
    public static HttpRequest traceJdk(String url) {
        return HttpRequest.trace(url);
    }

    /**
     * 使用OKHttpClient get请求
     *
     * @param url
     * @return
     */
    public static net.dreamlu.mica.http.HttpRequest getOkHttp(String url) {
        return net.dreamlu.mica.http.HttpRequest.get(url);
    }

    /**
     * 使用OKHttpClient delete请求
     *
     * @param url
     * @return
     */
    public static net.dreamlu.mica.http.HttpRequest deleteOkHttp(String url) {
        return net.dreamlu.mica.http.HttpRequest.delete(url);
    }

    /**
     * 使用OKHttpClient patch请求
     *
     * @param url
     * @return
     */
    public static net.dreamlu.mica.http.HttpRequest patchOkHttp(String url) {
        return net.dreamlu.mica.http.HttpRequest.patch(url);
    }

    /**
     * 使用OKHttpClient post请求
     *
     * @param url
     * @return
     */
    public static net.dreamlu.mica.http.HttpRequest postOkHttp(String url) {
        return net.dreamlu.mica.http.HttpRequest.post(url);
    }

    /**
     * 使用OKHttpClient put请求
     *
     * @param url
     * @return
     */
    public static net.dreamlu.mica.http.HttpRequest putOkHttp(String url) {
        return net.dreamlu.mica.http.HttpRequest.put(url);
    }

}
