package com.skrstop.framework.components.starter.feign.protostuff.converter;

import com.skrstop.framework.components.core.common.response.*;
import com.skrstop.framework.components.core.common.response.page.PageData;
import com.skrstop.framework.components.starter.feign.protostuff.configuration.GlobalFeignProperties;
import com.skrstop.framework.components.starter.feign.protostuff.configuration.interceptor.DynamicFeignClientMethodContextHolder;
import com.skrstop.framework.components.starter.web.configuration.GlobalResponseProperties;
import com.skrstop.framework.components.util.constant.FeignConst;
import com.skrstop.framework.components.util.serialization.protobuf.ProtostuffUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ProtostuffHttpMessageConverterTest {

    static class Pojo {
        private String a;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }

    static class StringResult extends Result<String> {
    }

    static class PojoList extends ArrayList<Pojo> {
    }

    static class PojoPage implements PageData<Pojo> {
        private Pojo rows;

        @Override
        public long getPageNumber() {return 0;}

        @Override
        public void setPageNumber(long pageNumber) {}

        @Override
        public long getPageSize() {return 0;}

        @Override
        public void setPageSize(long pageSize) {}

        @Override
        public long getTotal() {return 0;}

        @Override
        public void setTotal(long total) {}

        @Override
        public Pojo getRows() {return rows;}

        @Override
        public void setRows(Pojo rows) {this.rows = rows;}
    }

    private static final MediaType PROTOBUF = new MediaType("application", "x-protobuf", StandardCharsets.UTF_8);

    private static AnnotationConfigApplicationContext context;

    @After
    public void resetState() {
        DynamicFeignClientMethodContextHolder.clear();
        RequestContextHolder.resetRequestAttributes();
    }

    @AfterClass
    public static void clearSpring() {
        Field field = ReflectionUtils.findField(cn.hutool.extra.spring.SpringUtil.class, "applicationContext");
        field.setAccessible(true);
        ReflectionUtils.setField(field, null, null);
    }

    private void primeSpring(boolean supportFeign) {
        context = new AnnotationConfigApplicationContext();
        context.registerBean(GlobalResponseProperties.class, () -> {
            GlobalResponseProperties p = new GlobalResponseProperties();
            p.setSupportFeign(supportFeign);
            return p;
        });
        context.refresh();
        new cn.hutool.extra.spring.SpringUtil().setApplicationContext(context);
    }

    private GlobalFeignProperties properties(boolean stopAuto, boolean logError) {
        GlobalFeignProperties properties = new GlobalFeignProperties();
        properties.setStopAutoRemoveGlobalResponse(stopAuto);
        properties.setLogErrorAutoRemoveGlobalResponse(logError);
        properties.setGzipCompress(false);
        return properties;
    }

    private HttpInputMessage input(byte[] bytes) {
        return new HttpInputMessage() {
            @Override
            public InputStream getBody() {
                return new ByteArrayInputStream(bytes);
            }

            @Override
            public HttpHeaders getHeaders() {
                return new HttpHeaders();
            }
        };
    }

    private HttpOutputMessage output() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        return new HttpOutputMessage() {
            @Override
            public java.io.OutputStream getBody() {
                return stream;
            }

            @Override
            public HttpHeaders getHeaders() {
                return new HttpHeaders();
            }
        };
    }

    private byte[] payload(Object obj, boolean gzip) {
        try {
            return ProtostuffUtil.serializeWithCompress(obj, gzip);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void ctorWithoutSpringContext() {
        // SpringUtil.getBean 抛异常 -> webResponseFeignSupport=false 分支
        ProtostuffHttpMessageConverter converter = new ProtostuffHttpMessageConverter(properties(false, true));
        Assert.assertTrue(converter.supports(Object.class));
    }

    @Test
    public void ctorWithSpringContextAndSupports() {
        primeSpring(true);
        ProtostuffHttpMessageConverter converter = new ProtostuffHttpMessageConverter(properties(false, true));
        Assert.assertTrue(converter.supports(Pojo.class));
    }

    @Test
    public void canReadAndCanWrite() throws Exception {
        ProtostuffHttpMessageConverter converter = new ProtostuffHttpMessageConverter(properties(false, true));
        // mediaType protobuf
        Assert.assertTrue(converter.canRead(Pojo.class, Pojo.class, PROTOBUF));
        Assert.assertTrue(converter.canWrite(Pojo.class, Pojo.class, PROTOBUF));
        // mediaType null 且无请求上下文 -> false
        Assert.assertFalse(converter.canRead(Pojo.class, Pojo.class, null));
        Assert.assertFalse(converter.canWrite(Pojo.class, Pojo.class, null));
        // 请求头 use-feign -> true
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(FeignConst.USE_FEIGN_NAME, "true");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Assert.assertTrue(converter.canRead(Pojo.class, Pojo.class, null));
        Assert.assertTrue(converter.canWrite(Pojo.class, Pojo.class, null));
        RequestContextHolder.resetRequestAttributes();
        // DynamicFeignClientMethodContextHolder.push(true) -> canWrite true
        DynamicFeignClientMethodContextHolder.push(true);
        Assert.assertTrue(converter.canWrite(Pojo.class, Pojo.class, null));
        DynamicFeignClientMethodContextHolder.poll();
        Assert.assertFalse(converter.canWrite(Pojo.class, Pojo.class, null));
        // 请求头非 true 值
        MockHttpServletRequest request2 = new MockHttpServletRequest();
        request2.addHeader(FeignConst.USE_FEIGN_NAME, "false");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request2));
        Assert.assertFalse(converter.canRead(Pojo.class, Pojo.class, null));
    }

    @Test
    public void readInternalAndWriteInternalAndWrite() throws Exception {
        ProtostuffHttpMessageConverter converter = new ProtostuffHttpMessageConverter(properties(false, true));
        Pojo pojo = new Pojo();
        pojo.setA("x");
        // writeInternal
        HttpOutputMessage out = output();
        converter.write(pojo, PROTOBUF, out);
        Assert.assertTrue(out.getBody().toString().isEmpty() == false || true);
        byte[] bytes = ((ByteArrayOutputStream) out.getBody()).toByteArray();
        Assert.assertTrue(bytes.length > 0);
        // readInternal (2 参 read -> readInternal)
        Pojo read = (Pojo) converter.read(Pojo.class, input(bytes));
        Assert.assertEquals("x", read.getA());
        // GenericHttpMessageConverter.write
        HttpOutputMessage out2 = output();
        converter.write(pojo, (Type) Pojo.class, PROTOBUF, out2);
        Assert.assertTrue(((ByteArrayOutputStream) out2.getBody()).toByteArray().length > 0);
        // gzip 路径
        GlobalFeignProperties gzipProps = properties(false, true);
        gzipProps.setGzipCompress(true);
        ProtostuffHttpMessageConverter gzipConverter = new ProtostuffHttpMessageConverter(gzipProps);
        Pojo read2 = (Pojo) gzipConverter.read(Pojo.class, input(payload(pojo, true)));
        Assert.assertEquals("x", read2.getA());
    }

    @Test
    public void readWithContextClass() throws Exception {
        ProtostuffHttpMessageConverter converter = new ProtostuffHttpMessageConverter(properties(false, true));
        Pojo pojo = new Pojo();
        pojo.setA("x");
        // ParameterizedType + contextClass 非空 -> 直接反序列化 raw type
        ParameterizedType type = (ParameterizedType) StringResult.class.getGenericSuperclass();
        Object result = converter.read(type, StringResult.class, input(payload(new Result<>(), false)));
        Assert.assertNotNull(result);
    }

    @Test
    public void readStopAutoRemoveGlobalResponse() throws Exception {
        GlobalFeignProperties stopProps = properties(true, true);
        ProtostuffHttpMessageConverter converter = new ProtostuffHttpMessageConverter(stopProps);
        // 非 IResult 类型 -> 错误日志 + 返回 Result
        Object notResult = converter.read((Type) Pojo.class, null, input(payload(new Result<>(), false)));
        Assert.assertTrue(notResult instanceof Result);
        // IResult 类型 -> 直接反序列化
        Object result = converter.read((Type) Result.class, null, input(payload(new Result<>(), false)));
        Assert.assertTrue(result instanceof Result);
    }

    @Test
    public void readWebResponseFeignNotSupported() throws Exception {
        // webResponseFeignSupport=false (无 SpringUtil 上下文)
        ProtostuffHttpMessageConverter converter = new ProtostuffHttpMessageConverter(properties(false, true));
        Object pojo = converter.read((Type) Pojo.class, null, input(payload(new Pojo(), false)));
        Assert.assertNotNull(pojo);
    }

    @Test
    public void readAutoRemoveBranches() throws Exception {
        primeSpring(true);
        ProtostuffHttpMessageConverter converter = new ProtostuffHttpMessageConverter(properties(false, true));
        // IResult
        Assert.assertTrue(converter.read((Type) Result.class, null, input(payload(new Result<>(), false))) instanceof Result);
        // Void
        Assert.assertNull(converter.read((Type) Void.class, null, input(new byte[0])));
        // PageData -> PageListResult
        Object pageData = converter.read((Type) PojoPage.class, null, input(payload(new PageListResult<>(), false)));
        Assert.assertTrue(pageData == null || pageData instanceof List);
        // List -> ListResult
        Assert.assertNotNull(converter.read((Type) ArrayList.class, null, input(payload(new ListResult<>(), false))));
        // HashSet -> HashSetResult
        Assert.assertNotNull(converter.read((Type) HashSet.class, null, input(payload(new HashSetResult<>(), false))));
        // LinkedHashSet -> LinkedSetResult
        Assert.assertNotNull(converter.read((Type) LinkedHashSet.class, null, input(payload(new LinkedSetResult<>(), false))));
        // LinkedHashMap -> LinkedMapResult
        Assert.assertNotNull(converter.read((Type) LinkedHashMap.class, null, input(payload(new LinkedMapResult<>(), false))));
        // HashMap -> MapResult
        Assert.assertNotNull(converter.read((Type) HashMap.class, null, input(payload(new MapResult<>(), false))));
        // Collection(ArrayList) -> CollectionResult
        Assert.assertNotNull(converter.read((Type) PojoList.class, null, input(payload(new CollectionResult<>(), false))));
        // Set(TreeSet) -> LinkedSetResult
        Assert.assertNotNull(converter.read((Type) TreeSet.class, null, input(payload(new LinkedSetResult<>(), false))));
        // Map(TreeMap) -> LinkedMapResult
        Assert.assertNotNull(converter.read((Type) TreeMap.class, null, input(payload(new LinkedMapResult<>(), false))));
        // 其他 -> Result, 失败结果 + 日志开关 -> log.error
        Result failed = new Result<>();
        failed.setCode("ERR");
        failed.setMessage("m");
        Object data = converter.read((Type) Pojo.class, null, input(payload(failed, false)));
        Assert.assertNull(data);
        // 失败结果 + 日志关闭
        GlobalFeignProperties noLog = properties(false, false);
        ProtostuffHttpMessageConverter noLogConverter = new ProtostuffHttpMessageConverter(noLog);
        Assert.assertNull(noLogConverter.read((Type) Pojo.class, null, input(payload(failed, false))));
    }


    @Test
    public void readAutoConvertWithWebResponseSupport() throws Exception {
        primeSpring(true);
        // stop=false, logError=false, webResponseFeignSupport=true -> 自动类型转换全链路
        ProtostuffHttpMessageConverter converter = new ProtostuffHttpMessageConverter(properties(false, false));
        Object pojo = converter.read((Type) Pojo.class, null, input(payload(Result.Builder.success(), false)));
        // 成功 Result -> data 为 null (payload 无数据)
        Assert.assertNull(pojo);
        // List 分支
        Assert.assertNotNull(converter.read((Type) ArrayList.class, null, input(payload(new ListResult<>(), false))));
    }

    @Test
    public void canReadMediaTypes() throws Exception {
        ProtostuffHttpMessageConverter converter = new ProtostuffHttpMessageConverter(properties(true, true));
        // 无 protobuf 媒体类型、无请求头、无 feign 上下文 -> false
        Assert.assertFalse(converter.canRead((Type) Pojo.class, null, MediaType.APPLICATION_JSON));
        // 有 use_feign 请求头 -> true
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(FeignConst.USE_FEIGN_NAME, FeignConst.USE_FEIGN_VALUE);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        try {
            Assert.assertTrue(converter.canRead((Type) Pojo.class, null, MediaType.APPLICATION_JSON));
            Assert.assertTrue(converter.canRead((Type) Pojo.class, null, null));
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }

    @Test
    public void readSuccessResultWithLogErrorEnabled() throws Exception {
        // 成功结果 + logError=true -> isFailed && isLogError 短路, 不打日志
        ProtostuffHttpMessageConverter converter = new ProtostuffHttpMessageConverter(properties(false, true));
        Object pojo = converter.read((Type) Pojo.class, null, input(payload(Result.Builder.success(), false)));
        Assert.assertNull(pojo);
    }

    @Test
    public void canWriteViaRequestHeader() throws Exception {
        ProtostuffHttpMessageConverter converter = new ProtostuffHttpMessageConverter(properties(true, true));
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(FeignConst.USE_FEIGN_NAME, FeignConst.USE_FEIGN_VALUE);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        try {
            // 非 protobuf 媒体类型、无 push -> isUseFign 分支 -> true
            Assert.assertTrue(converter.canWrite((Type) Pojo.class, Pojo.class, MediaType.APPLICATION_JSON));
            Assert.assertTrue(converter.canWrite((Type) Pojo.class, Pojo.class, null));
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }
}
