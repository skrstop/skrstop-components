package com.skrstop.framework.components.starter.annotation.handle.function.accessLimit;

import com.google.common.util.concurrent.RateLimiter;
import com.skrstop.framework.components.util.enums.CharSetEnum;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 蒋时华
 * @date 2024-05-10 21:01:04
 * @since 1.0.0
 */
@Slf4j
public class DefaultAccessLimitRule implements AccessLimitRule {

    /**
     * 用来存放不同接口的RateLimiter(key为接口名称，value为RateLimiter)
     * 每秒只发出 limit 个令牌，此处是单进程服务的限流,内部采用令牌捅算法实现
     */
    private final ConcurrentHashMap<String, RateLimiter> limitMap = new ConcurrentHashMap<>();

    @Override
    public boolean canContinue(MethodInvocation invocation, double limitNum) {
        String methodStr = invocation.getMethod().toString();
        String functionName;
        try {
            functionName = DigestUtils.md5DigestAsHex(methodStr.getBytes(CharSetEnum.UTF8.getCharSet()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        // 获取注解每秒加入桶中的token
        log.debug("function:{}, limit:{}, 开启限流", methodStr, limitNum);
        RateLimiter rateLimiter;
        // 获取rateLimiter
        if (limitMap.containsKey(functionName)) {
            rateLimiter = limitMap.get(functionName);
            double rate = rateLimiter.getRate();
            if (rate != limitNum) {
                limitMap.put(functionName, RateLimiter.create(limitNum));
                rateLimiter = limitMap.get(functionName);
            }
        } else {
            limitMap.put(functionName, RateLimiter.create(limitNum));
            rateLimiter = limitMap.get(functionName);
        }
        return rateLimiter.tryAcquire();
    }

}
