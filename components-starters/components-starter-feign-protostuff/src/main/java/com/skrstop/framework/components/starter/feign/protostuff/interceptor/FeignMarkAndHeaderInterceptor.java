package com.skrstop.framework.components.starter.feign.protostuff.interceptor;

import com.skrstop.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;
import com.skrstop.framework.components.starter.feign.protostuff.configuration.GlobalFeignProperties;
import com.skrstop.framework.components.util.constant.FeignConst;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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

    private static final Map<String, ProtostuffFeignClient> PROTOSTUFF_FEIGN_CLIENT_CACHE = new ConcurrentHashMap<>();

    private GlobalFeignProperties globalFeignProperties;

    public FeignMarkAndHeaderInterceptor(GlobalFeignProperties globalFeignProperties) {
        this.globalFeignProperties = globalFeignProperties;
    }

    @Override
    public void apply(RequestTemplate template) {

//		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        try {
            String name = template.feignTarget().type().getName();
            ProtostuffFeignClient protostuffFeignClient = PROTOSTUFF_FEIGN_CLIENT_CACHE.get(name);
            if (protostuffFeignClient == null) {
                protostuffFeignClient = Class.forName(name).getAnnotation(ProtostuffFeignClient.class);
                PROTOSTUFF_FEIGN_CLIENT_CACHE.put(name, protostuffFeignClient);
            }
            if (ObjectUtil.isNotNull(protostuffFeignClient)) {
                // 添加feignMark 标记为protostuff
                template.header(FeignConst.USE_FEIGN_NAME, FeignConst.USE_FEIGN_VALUE);
                template.header(FeignConst.FEIGN_PROTOCOL_NAME, FeignConst.FEIGN_PROTOCOL_VALUE_PROTOSTUFF);
            }
            // 请求头传递
            if (globalFeignProperties.getTransferHeader()) {
                this.transferHeader(template);
            }
        } catch (ClassNotFoundException e) {
            // do nothing
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
