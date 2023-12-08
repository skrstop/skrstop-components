//package com.zoe.framework.components.starter.redis.configuration.dynamic.selector;
//
//import org.aopalliance.intercept.MethodInvocation;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//
//public class DsHeaderProcessor extends DsSelector {
//
//    /**
//     * header prefix
//     */
//    private static final String HEADER_PREFIX = "#header";
//
//    @Override
//    public boolean matches(String key) {
//        return key.startsWith(HEADER_PREFIX);
//    }
//
//    @Override
//    public String doDetermineConnectionFactory(MethodInvocation invocation, String key) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        return request.getHeader(key.substring(8));
//    }
//}