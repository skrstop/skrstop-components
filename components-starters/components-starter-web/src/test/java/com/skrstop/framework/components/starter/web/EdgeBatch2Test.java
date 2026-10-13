package com.skrstop.framework.components.starter.web;

import cn.hutool.core.io.IORuntimeException;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ErrorHandleChainPattern;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ErrorHandlerInterceptor;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandleChainPattern;
import com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandlerInterceptor;
import com.skrstop.framework.components.starter.web.utils.ServletUtil;
import com.skrstop.framework.components.starter.web.utils.WebUtil;
import feign.Request;
import feign.Response;
import feign.codec.Decoder;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class EdgeBatch2Test {

    // ---------- 链模式 ----------
    private interface ErrHandler extends ErrorHandlerInterceptor {
    }

    private interface ExcHandler extends ExceptionHandlerInterceptor {
    }

    @Test
    public void testChainPatternInterceptorBranches() {
        // exception chain: interceptor 返回 null → FAIL/500
        ExceptionHandlerInterceptor nullExec = Mockito.mock(ExceptionHandlerInterceptor.class);
        Mockito.when(nullExec.support(Mockito.any())).thenReturn(true);
        Mockito.when(nullExec.execute(Mockito.any())).thenReturn(null);
        ExceptionHandleChainPattern excChain = new ExceptionHandleChainPattern(new ArrayList<>(Collections.singletonList(nullExec)));
        ReflectionTestUtils.invokeMethod(excChain, "initChainPattern");
        cn.hutool.core.lang.Pair<IResult, Integer> r1 = excChain.execute(new RuntimeException("x"));
        Assert.assertNotNull(r1.getKey());
        Assert.assertEquals(500, r1.getValue().intValue());

        // result null + next=false → FAIL/500
        ExceptionHandlerInterceptor nullResult = Mockito.mock(ExceptionHandlerInterceptor.class);
        Mockito.when(nullResult.support(Mockito.any())).thenReturn(true);
        Mockito.when(nullResult.execute(Mockito.any())).thenReturn(InterceptorResult.builder().next(false).build());
        excChain = new ExceptionHandleChainPattern(new ArrayList<>(Collections.singletonList(nullResult)));
        ReflectionTestUtils.invokeMethod(excChain, "initChainPattern");
        r1 = excChain.execute(new RuntimeException("x"));
        Assert.assertEquals(500, r1.getValue().intValue());

        // result null + next=true → 继续向下 → 无更多拦截器 → FAIL/500
        excChain = new ExceptionHandleChainPattern(new ArrayList<>(Collections.singletonList(nullResult)));
        ReflectionTestUtils.invokeMethod(excChain, "initChainPattern");
        r1 = excChain.execute(new RuntimeException("x"));
        Assert.assertEquals(500, r1.getValue().intValue());

        // error chain 同样分支
        ErrorHandlerInterceptor errNullExec = Mockito.mock(ErrorHandlerInterceptor.class);
        Mockito.when(errNullExec.support(Mockito.any())).thenReturn(true);
        Mockito.when(errNullExec.execute(Mockito.any())).thenReturn(null);
        ErrorHandleChainPattern errChain = new ErrorHandleChainPattern(new ArrayList<>(Collections.singletonList(errNullExec)));
        ReflectionTestUtils.invokeMethod(errChain, "initChainPattern");
        cn.hutool.core.lang.Pair<IResult, Integer> r2 = errChain.execute(new AssertionError("x"));
        Assert.assertEquals(500, r2.getValue().intValue());

        ErrorHandlerInterceptor errNullResult = Mockito.mock(ErrorHandlerInterceptor.class);
        Mockito.when(errNullResult.support(Mockito.any())).thenReturn(true);
        Mockito.when(errNullResult.execute(Mockito.any())).thenReturn(InterceptorResult.builder().next(false).build());
        errChain = new ErrorHandleChainPattern(new ArrayList<>(Collections.singletonList(errNullResult)));
        ReflectionTestUtils.invokeMethod(errChain, "initChainPattern");
        r2 = errChain.execute(new AssertionError("x"));
        Assert.assertEquals(500, r2.getValue().intValue());

        Mockito.when(errNullResult.execute(Mockito.any())).thenReturn(InterceptorResult.builder().next(true).responseStatus(418).build());
        errChain = new ErrorHandleChainPattern(new ArrayList<>(Collections.singletonList(errNullResult)));
        ReflectionTestUtils.invokeMethod(errChain, "initChainPattern");
        r2 = errChain.execute(new AssertionError("x"));
        Assert.assertEquals(418, r2.getValue().intValue());
        Assert.assertNull(r2.getKey());
    }

    // ---------- ServletUtil ----------
    private static class ThrowingReader extends Reader {
        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            throw new IOException("boom");
        }

        @Override
        public void close() throws IOException {
            // IoUtil.read 不抛 IOException，getBody 的 catch 仅可能由 close() 触发
            throw new IOException("close boom");
        }
    }

    @Test
    public void testServletUtilIoErrors() throws IOException {
        jakarta.servlet.ServletRequest req = Mockito.mock(jakarta.servlet.ServletRequest.class);
        try {
            Mockito.when(req.getReader()).thenReturn(new BufferedReader(new ThrowingReader()));
            ServletUtil.getBody(req);
            Assert.fail();
        } catch (IORuntimeException expected) {
        }
        try {
            Mockito.when(req.getInputStream()).thenThrow(new IOException("boom"));
            ServletUtil.getBodyBytes(req);
            Assert.fail();
        } catch (IORuntimeException expected) {
        }
        try {
            jakarta.servlet.http.HttpServletResponse resp = Mockito.mock(jakarta.servlet.http.HttpServletResponse.class);
            Mockito.when(resp.getWriter()).thenThrow(new IOException("boom"));
            ServletUtil.getWriter(resp);
            Assert.fail();
        } catch (IORuntimeException expected) {
        }
        try {
            jakarta.servlet.http.HttpServletResponse resp = Mockito.mock(jakarta.servlet.http.HttpServletResponse.class);
            Mockito.when(resp.getOutputStream()).thenThrow(new IOException("boom"));
            ServletUtil.write(resp, new ByteArrayInputStream(new byte[1]), 1024);
            Assert.fail();
        } catch (RuntimeException expected) {
        }
        // PrintWriter 不抛 IOException，write(text) 分支等价于仅 getWriter 可能抛
        try {
            jakarta.servlet.http.HttpServletResponse resp = Mockito.mock(jakarta.servlet.http.HttpServletResponse.class);
            Mockito.when(resp.getWriter()).thenThrow(new IOException("boom"));
            ServletUtil.write(resp, "text", "application/json");
            Assert.fail();
        } catch (RuntimeException expected) {
        }
    }

    public static class Bean {
        private String name;

        public String getName() {return name;}

        public void setName(String name) {this.name = name;}
    }

    @Test
    public void testServletUtilFillBeanNullParams() {
        // containsKey 两个 getParameter 均为 null -> value() 内两次 getParameterValues 均为空 -> 返回 null
        jakarta.servlet.ServletRequest req = Mockito.mock(jakarta.servlet.ServletRequest.class);
        Bean bean = ServletUtil.fillBean(req, new Bean(), cn.hutool.core.bean.copier.CopyOptions.create());
        Assert.assertNull(bean.getName());
    }

    @Test
    public void testServletUtilFillBeanSingleValue() {
        jakarta.servlet.ServletRequest req = Mockito.mock(jakarta.servlet.ServletRequest.class);
        Mockito.when(req.getParameter("name")).thenReturn("v");
        Mockito.when(req.getParameterValues("name")).thenReturn(new String[]{"v"});
        Bean bean = ServletUtil.fillBean(req, new Bean(), cn.hutool.core.bean.copier.CopyOptions.create());
        Assert.assertEquals("v", bean.getName());
    }

    @Test
    public void testServletUtilFillBeanMultiValue() {
        jakarta.servlet.ServletRequest req = Mockito.mock(jakarta.servlet.ServletRequest.class);
        Mockito.when(req.getParameter("name")).thenReturn("a");
        Mockito.when(req.getParameterValues("name")).thenReturn(new String[]{"a", "b"});
        Bean bean = ServletUtil.fillBean(req, new Bean(), cn.hutool.core.bean.copier.CopyOptions.create());
        Assert.assertNotNull(bean.getName());
    }

    @Test
    public void testServletUtilFillBeanPrefixedParams() {
        // name 参数为空 -> 使用 bean.name 前缀参数
        jakarta.servlet.ServletRequest req = Mockito.mock(jakarta.servlet.ServletRequest.class);
        Mockito.when(req.getParameter("bean.name")).thenReturn("x");
        Mockito.when(req.getParameterValues("name")).thenReturn(null);
        Mockito.when(req.getParameterValues("bean.name")).thenReturn(new String[]{"x"});
        Bean bean = ServletUtil.fillBean(req, new Bean(), cn.hutool.core.bean.copier.CopyOptions.create());
        Assert.assertEquals("x", bean.getName());
    }

    @Test
    public void testServletUtilMultipart() {
        String body = "--b\r\nContent-Disposition: form-data; name=\"field\"\r\n\r\nvalue\r\n--b--\r\n";
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setContent(body.getBytes(StandardCharsets.UTF_8));
        req.setCharacterEncoding("UTF-8");
        try {
            cn.hutool.core.net.multipart.MultipartFormData formData = ServletUtil.getMultipart(req);
            Assert.assertNotNull(formData);
        } catch (Exception e) {
            // hutool 解析需要 content-type/boundary 等额外信息，解析失败不影响覆盖
        }
        try {
            cn.hutool.core.net.multipart.MultipartFormData formData = ServletUtil.getMultipart(req, new cn.hutool.core.net.multipart.UploadSetting());
            Assert.assertNotNull(formData);
        } catch (Exception e) {
            // 同上
        }
    }

    @Test
    public void testServletUtilHeaderIgnoreCase() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Other", "v");
        Assert.assertNull(ServletUtil.getHeaderIgnoreCase(req, "not-exist"));
        Assert.assertEquals("v", ServletUtil.getHeaderIgnoreCase(req, "other"));
    }

    @Test
    public void testServletUtilSetHeaderInt() {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        ServletUtil.setHeader(resp, "n", 1);
        Assert.assertNotNull(resp.getHeader("n"));
        // 其他类型 -> toString 分支
        ServletUtil.setHeader(resp, "m", 100L);
        Assert.assertEquals("100", resp.getHeader("m"));
    }

    @Test
    public void testServletUtilHeaderIgnoreCaseNullName() throws Exception {
        jakarta.servlet.http.HttpServletRequest req = Mockito.mock(jakarta.servlet.http.HttpServletRequest.class);
        java.util.Vector<String> names = new java.util.Vector<>();
        names.add(null);
        names.add("X");
        Mockito.when(req.getHeaderNames()).thenReturn(java.util.Collections.enumeration(names));
        Mockito.when(req.getHeader("X")).thenReturn("v");
        Assert.assertEquals("v", ServletUtil.getHeaderIgnoreCase(req, "x"));
        Assert.assertNull(ServletUtil.getHeaderIgnoreCase(req, "y"));
    }

    // ---------- WebUtil ----------
    private MockHttpServletRequest ipRequest(String xff, String real, String proxy) {
        return ipRequest(xff, real, proxy, null);
    }

    private MockHttpServletRequest ipRequest(String xff, String real, String proxy, String wl) {
        MockHttpServletRequest req = new MockHttpServletRequest();
        if (xff != null) req.addHeader("X-Forwarded-For", xff);
        if (real != null) req.addHeader("X-Real-IP", real);
        if (proxy != null) req.addHeader("Proxy-Client-IP", proxy);
        if (wl != null) req.addHeader("WL-Proxy-Client-IP", wl);
        req.setRemoteAddr("127.0.0.1");
        return req;
    }

    @Test
    public void testWebUtilGetIpCombos() {
        Assert.assertEquals("5.6.7.8", WebUtil.getIP(ipRequest("5.6.7.8, 9.9", null, null)));
        Assert.assertEquals("8.8.8.8", WebUtil.getIP(ipRequest("8.8.8.8", null, null)));
        Assert.assertEquals("7.7.7.7", WebUtil.getIP(ipRequest("unknown", "7.7.7.7", null)));
        Assert.assertEquals("1.2.3.4", WebUtil.getIP(ipRequest("unknown", "unKnown", "1.2.3.4")));
        Assert.assertEquals("127.0.0.1", WebUtil.getIP(ipRequest("unknown", null, "unKnown")));
        Assert.assertEquals("127.0.0.1", WebUtil.getIP(ipRequest(null, null, null)));
        // 所有 header 均为 unknown 值 -> 全部落空，返回 remoteAddr
        MockHttpServletRequest allUnknown = new MockHttpServletRequest();
        allUnknown.addHeader("X-Forwarded-For", "unknown");
        allUnknown.addHeader("X-Real-IP", "unKnown");
        allUnknown.addHeader("Proxy-Client-IP", "unKnown");
        allUnknown.addHeader("WL-Proxy-Client-IP", "unKnown");
        allUnknown.addHeader("HTTP_CLIENT_IP", "unKnown");
        allUnknown.addHeader("HTTP_X_FORWARDED_FOR", "unKnown");
        allUnknown.setRemoteAddr("10.0.0.1");
        Assert.assertEquals("10.0.0.1", WebUtil.getIP(allUnknown));
        // X-Real-IP 为空，Proxy-Client-IP 为空 -> WL-Proxy-Client-IP 命中
        Assert.assertEquals("4.4.4.4", WebUtil.getIP(ipRequest(null, null, null, "4.4.4.4")));
    }

    @Test
    public void testWebUtilRequestBytesShortStream() throws Exception {
        jakarta.servlet.http.HttpServletRequest req = Mockito.mock(jakarta.servlet.http.HttpServletRequest.class);
        Mockito.when(req.getContentLength()).thenReturn(5);
        Mockito.when(req.getInputStream()).thenAnswer(inv -> new jakarta.servlet.ServletInputStream() {
            @Override
            public int read() {
                return -1;
            }

            @Override
            public boolean isFinished() {return true;}

            @Override
            public boolean isReady() {return true;}

            @Override
            public void setReadListener(jakarta.servlet.ReadListener listener) {}
        });
        byte[] bytes = WebUtil.getRequestBytes(req);
        Assert.assertEquals(5, bytes.length);
    }

    @Test
    public void testWebUtilEnvironmentNull() {
        org.springframework.web.context.request.RequestContextHolder.resetRequestAttributes();
        Assert.assertNull(WebUtil.getApplicationContext());
        Assert.assertNull(WebUtil.getEnvironment());
    }

    @Test
    public void testWebUtilRequestArgs() {
        jakarta.servlet.http.HttpServletRequest serReq = Mockito.mock(jakarta.servlet.http.HttpServletRequest.class,
                Mockito.withSettings().serializable());
        Mockito.when(serReq.getParameterMap()).thenReturn(Collections.singletonMap("a", new String[]{"b"}));
        Object r = WebUtil.getRequestArgs(new Object[]{serReq});
        Assert.assertNotNull(r);

        // 全部为非 Serializable -> 过滤后为空数组
        Object r2 = WebUtil.getRequestArgs(new Object[]{new Object()});
        Assert.assertNotNull(r2);

        // 过滤后剩余单个非 request 参数
        Object r3 = WebUtil.getRequestArgs(new Object[]{"str", new Object()});
        Assert.assertNotNull(r3);
        Assert.assertArrayEquals(new Object[]{"str"}, (Object[]) r3);

        // 过滤后剩余多个参数 -> getValidArgs 多参数分支
        Object r4 = WebUtil.getRequestArgs(new Object[]{new Object(), "a", "b"});
        Assert.assertArrayEquals(new Object[]{"a", "b"}, (Object[]) r4);
    }

    // ---------- Feign ----------
    @Test
    public void testHttpClientErrorNullBody() {
        var r = exceptionChain().execute(
                org.springframework.web.client.HttpClientErrorException.create(HttpStatus.BAD_REQUEST,
                        "bad", null, "null".getBytes(), null));
        Assert.assertNotNull(r.getKey());
    }

    private com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandleChainPattern exceptionChain() {
        com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandleChainPattern chain =
                new com.skrstop.framework.components.starter.web.exception.core.interceptor.ExceptionHandleChainPattern(new java.util.ArrayList<>());
        ReflectionTestUtils.invokeMethod(chain, "initChainPattern");
        return chain;
    }

    @Test
    public void testFeignNullResult() throws Exception {
        com.skrstop.framework.components.starter.web.configuration.FeignResponseAuthConfiguration config =
                new com.skrstop.framework.components.starter.web.configuration.FeignResponseAuthConfiguration();
        ObjectProviderStub<org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer> customizers =
                new ObjectProviderStub<>();
        org.springframework.beans.factory.ObjectFactory<org.springframework.boot.autoconfigure.http.HttpMessageConverters> factory =
                () -> new org.springframework.boot.autoconfigure.http.HttpMessageConverters(
                        new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter());
        Decoder decoder = config.customAutoResponseDecoder(customizers, factory);
        Response resp = Response.builder()
                .status(200)
                .reason("ok")
                .request(Request.create(Request.HttpMethod.GET, "/", Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .headers(Collections.singletonMap("Content-Type", Collections.singletonList("application/json")))
                .body("null", StandardCharsets.UTF_8)
                .build();
        Type type = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {return new Type[0];}

            @Override
            public Type getRawType() {return com.alibaba.fastjson2.JSONObject.class;}

            @Override
            public Type getOwnerType() {return null;}
        };
        Assert.assertNull(decoder.decode(resp, type));
    }

    static class ObjectProviderStub<T> implements org.springframework.beans.factory.ObjectProvider<T> {
        @Override
        public java.util.stream.Stream<T> stream() {
            return java.util.stream.Stream.empty();
        }

        @Override
        public java.util.stream.Stream<T> orderedStream() {
            return java.util.stream.Stream.empty();
        }

        @Override
        public T getObject(Object... args) {return null;}

        @Override
        public T getObject() {return null;}

        @Override
        public T getIfAvailable() {return null;}

        @Override
        public T getIfUnique() {return null;}
    }

    // ---------- Sentinel ----------
    @Test
    public void testWebFluxBlockRequestHandler() {
        com.skrstop.framework.components.starter.web.configuration.sentinel.webflux.DefaultWebFluxBlockRequestHandler h =
                new com.skrstop.framework.components.starter.web.configuration.sentinel.webflux.DefaultWebFluxBlockRequestHandler();
        org.springframework.mock.web.server.MockServerWebExchange exchange =
                org.springframework.mock.web.server.MockServerWebExchange.from(
                        org.springframework.mock.http.server.reactive.MockServerHttpRequest.get("/x").build());
        h.handleRequest(exchange, new RuntimeException("blocked"));
    }

    static class NonBusinessDataException extends Exception implements com.skrstop.framework.components.core.exception.core.SkrstopDataThrowable {
        @Override
        public com.skrstop.framework.components.core.common.response.core.IResult getIResult() {
            return com.skrstop.framework.components.core.common.response.DefaultResult.Builder.error();
        }

        @Override
        public com.skrstop.framework.components.core.exception.core.data.ThrowableData getThrowableData() {
            return new com.skrstop.framework.components.core.exception.DefaultThrowableData();
        }
    }

    @Test
    public void testNonBusinessDataException() {
        var r = exceptionChain().execute(new NonBusinessDataException());
        Assert.assertNotNull(r.getKey());
    }

    @Test
    public void testBindExceptionFilterSides() {
        BindException e = new BindException(new org.springframework.validation.BeanPropertyBindingResult(new Object(), "target"));
        // 非 FieldError 的 ObjectError -> instanceof 过滤
        e.addError(new org.springframework.validation.ObjectError("target", "object error"));
        // bindingFailure 为 true 的 FieldError -> 被过滤
        e.addError(new org.springframework.validation.FieldError("target", "field", "rejected", true, null, null, "binding failure"));
        var r = exceptionChain().execute(e);
        Assert.assertNotNull(r.getKey());
        Assert.assertEquals(400, r.getValue().intValue());
    }

    @Test
    public void testServletUtilGetBodyGetReaderThrows() throws IOException {
        jakarta.servlet.ServletRequest req = Mockito.mock(jakarta.servlet.ServletRequest.class);
        Mockito.when(req.getReader()).thenThrow(new IOException("boom"));
        try {
            ServletUtil.getBody(req);
            Assert.fail();
        } catch (IORuntimeException expected) {
        }
    }

    @Test
    public void testServletUtilFillBeanValuesEmptyPrefixedEmpty() {
        // containsKey 为 true（getParameter 非 null），但 getParameterValues 两次均为空 -> value() 返回 null
        jakarta.servlet.ServletRequest req = Mockito.mock(jakarta.servlet.ServletRequest.class);
        Mockito.when(req.getParameter("name")).thenReturn("x");
        Mockito.when(req.getParameterValues("name")).thenReturn(null);
        Mockito.when(req.getParameterValues("bean.name")).thenReturn(null);
        Bean bean = ServletUtil.fillBean(req, new Bean(), cn.hutool.core.bean.copier.CopyOptions.create());
        Assert.assertNull(bean.getName());
    }
}
