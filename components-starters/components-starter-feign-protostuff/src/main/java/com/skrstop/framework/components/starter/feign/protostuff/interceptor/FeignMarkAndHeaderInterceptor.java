package com.skrstop.framework.components.starter.feign.protostuff.interceptor;

import com.skrstop.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;
import com.skrstop.framework.components.starter.feign.protostuff.configuration.GlobalFeignProperties;
import com.skrstop.framework.components.util.constant.FeignConst;
import com.skrstop.framework.components.util.enums.ContentTypeEnum;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * feign 请求拦截，添加protostuff标记
 *
 * @author 蒋时华
 * @date 2019-04-21
 */
@Slf4j
public class FeignMarkAndHeaderInterceptor implements RequestInterceptor {

    private static final Map<String, Boolean> PROTOSTUFF_FEIGN_CLIENT_CACHE = new ConcurrentHashMap<>();

    private GlobalFeignProperties globalFeignProperties;

    public FeignMarkAndHeaderInterceptor(GlobalFeignProperties globalFeignProperties) {
        this.globalFeignProperties = globalFeignProperties;
    }

    @Override
    public void apply(RequestTemplate template) {

//		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 直接使用 feignTarget().type() 获取 Class(原 Class.forName(name) 中 name 即 type.getName(),
        // type 已加载, CNFE 不可达), try/catch 等价删除
        Class<?> type = template.feignTarget().type();
        String name = type.getName();
        Boolean isProtostuffFeignClient = PROTOSTUFF_FEIGN_CLIENT_CACHE.get(name);
        if (isProtostuffFeignClient == null) {
            ProtostuffFeignClient protostuffFeignClient = type.getAnnotation(ProtostuffFeignClient.class);
            // 缓存未命中时 isProtostuffFeignClient 已被赋值, 恒非 null
            isProtostuffFeignClient = protostuffFeignClient != null;
            PROTOSTUFF_FEIGN_CLIENT_CACHE.put(name, isProtostuffFeignClient);
        }
        if (isProtostuffFeignClient) {
            // 添加feignMark 标记为protostuff
            template.header(FeignConst.USE_FEIGN_NAME, FeignConst.USE_FEIGN_VALUE);
            template.header(FeignConst.FEIGN_PROTOCOL_NAME, FeignConst.FEIGN_PROTOCOL_VALUE_PROTOSTUFF);
            template.header("Content-Type", ContentTypeEnum.PROTOBUF.getContentType());
        }
        // 请求头传递
        if (globalFeignProperties.getTransferHeader()) {
            this.transferHeader(template);
        }
    }

    private void transferHeader(RequestTemplate template) {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return;
        }
        Enumeration<String> headerNames = request.getHeaderNames();
        if (CollectionUtil.isEmpty(headerNames)) {
            return;
        }
        HashSet<Object> ignoreHashHeader = CollectionUtil.newHashSet();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            if (globalFeignProperties.getTransferHeaderIgnore().contains(key.toLowerCase())) {
                continue;
            }
            String value = request.getHeader(key);
            template.header(key, value);
        }
    }

    private HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

}
