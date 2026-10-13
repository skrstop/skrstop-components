package com.skrstop.framework.components.starter.feign.protostuff.interceptor;

import com.skrstop.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;
import com.skrstop.framework.components.starter.feign.protostuff.configuration.GlobalFeignProperties;
import com.skrstop.framework.components.util.constant.FeignConst;
import feign.RequestTemplate;
import feign.Target;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Collection;

public class FeignMarkAndHeaderInterceptorTest {

    @After
    public void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @ProtostuffFeignClient("mark-svc")
    interface MarkedClient {
    }

    interface PlainClient {
    }

    private RequestTemplate template(Class<?> type) {
        RequestTemplate template = new RequestTemplate();
        template.feignTarget(new Target.HardCodedTarget<>(type, type.getSimpleName(), "http://localhost"));
        return template;
    }

    @Test
    public void markedClientAddsHeaders() {
        GlobalFeignProperties properties = new GlobalFeignProperties();
        properties.setTransferHeader(false);
        FeignMarkAndHeaderInterceptor interceptor = new FeignMarkAndHeaderInterceptor(properties);
        RequestTemplate template = template(MarkedClient.class);
        interceptor.apply(template);
        Collection<String> feignMark = template.headers().get(FeignConst.USE_FEIGN_NAME);
        Assert.assertNotNull(feignMark);
        Assert.assertEquals(FeignConst.USE_FEIGN_VALUE, feignMark.iterator().next());
        Assert.assertTrue(template.headers().containsKey(FeignConst.FEIGN_PROTOCOL_NAME));
        Assert.assertTrue(template.headers().containsKey("Content-Type"));

        // 再次调用走缓存命中分支
        RequestTemplate template2 = template(MarkedClient.class);
        interceptor.apply(template2);
        Assert.assertNotNull(template2.headers().get(FeignConst.USE_FEIGN_NAME));
    }

    @Test
    public void plainClientAndNoRequestContext() {
        GlobalFeignProperties properties = new GlobalFeignProperties();
        properties.setTransferHeader(true);
        FeignMarkAndHeaderInterceptor interceptor = new FeignMarkAndHeaderInterceptor(properties);
        RequestTemplate template = template(PlainClient.class);
        // 无 request attributes -> getHttpServletRequest 返回 null
        interceptor.apply(template);
        Assert.assertFalse(template.headers().containsKey(FeignConst.USE_FEIGN_NAME));
    }

    @Test
    public void transferHeaderFromRequest() {
        GlobalFeignProperties properties = new GlobalFeignProperties();
        properties.setTransferHeader(true);
        FeignMarkAndHeaderInterceptor interceptor = new FeignMarkAndHeaderInterceptor(properties);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Custom", "cv");
        // 命中 ignore 列表
        request.addHeader("Content-Type", "application/json");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        RequestTemplate template = template(PlainClient.class);
        interceptor.apply(template);
        Assert.assertEquals("cv", template.headers().get("X-Custom").iterator().next());
        // ignore 列表中的请求头不传递
        Collection<String> contentType = template.headers().getOrDefault("Content-Type", new ArrayList<>());
        Assert.assertFalse(contentType.contains("application/json"));
    }

    @Test
    public void transferHeaderWithNoRequestHeaders() {
        GlobalFeignProperties properties = new GlobalFeignProperties();
        properties.setTransferHeader(true);
        FeignMarkAndHeaderInterceptor interceptor = new FeignMarkAndHeaderInterceptor(properties);
        // 请求无任何头 -> CollectionUtil.isEmpty -> return
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
        RequestTemplate template = template(PlainClient.class);
        interceptor.apply(template);
        Assert.assertFalse(template.headers().containsKey("X-Custom"));
        RequestContextHolder.resetRequestAttributes();
    }
}
