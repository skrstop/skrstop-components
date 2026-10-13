package com.skrstop.framework.components.starter.feign.protostuff.interceptor;

import com.skrstop.framework.components.util.constant.FeignConst;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class FeignHttpFilterTest {

    private final FeignHttpFilter filter = new FeignHttpFilter();

    @Test
    public void feignRequestSetsProtostuffContentType() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(FeignConst.USE_FEIGN_NAME, FeignConst.USE_FEIGN_VALUE);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        filter.doFilter(request, response, chain);
        Assert.assertEquals("application/x-protobuf;charset=UTF-8", response.getContentType());
        Assert.assertNotNull(chain.getRequest());
    }

    @Test
    public void normalRequestUntouched() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, new MockFilterChain());
        Assert.assertNull(response.getContentType());
    }
}
