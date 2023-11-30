package com.zoe.framework.components.starter.feign.protostuff.interceptor;

import com.zoe.framework.components.util.constant.FeignConst;
import com.zoe.framework.components.util.enums.CharSetEnum;
import com.zoe.framework.components.util.enums.HttpContentTypeEnum;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * feign 请求拦截，添加protobuf content-type
 *
 * @author 蒋时华
 * @date 2019-04-21
 */
public class FeignHttpFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        // feign protobuf content-type设置
        processProtostuffContentType(servletRequest, servletResponse);
        chain.doFilter(servletRequest, servletResponse);

    }

    private void processProtostuffContentType(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (FeignConst.USE_FEIGN_VALUE.equals(request.getHeader(FeignConst.USE_FEIGN_NAME))) {
            servletResponse.setContentType(HttpContentTypeEnum.PROTOBUF.getContentType());
            servletResponse.setCharacterEncoding(CharSetEnum.UTF8.getCharSet());
        }
    }

}
