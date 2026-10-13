package com.skrstop.framework.components.starter.web.response;

import cn.hutool.core.lang.Pair;
import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.starter.web.entity.InterceptorResult;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class ResponseChainTest {

    @Test
    public void testChainInitAndExecute() {
        List<com.skrstop.framework.components.starter.web.response.core.ResponseHandlerInterceptor> list = new ArrayList<>();
        com.skrstop.framework.components.starter.web.response.core.ResponseHandleChainPattern chain =
                new com.skrstop.framework.components.starter.web.response.core.ResponseHandleChainPattern(list);
        org.springframework.test.util.ReflectionTestUtils.invokeMethod(chain, "initChainPattern");
        Assert.assertEquals(3, chain.getResponseHandlerInterceptors().size());

        // plain object → DefaultResponseInterceptor
        Pair<Object, Integer> p = chain.execute("hello", false);
        Assert.assertNotNull(p.getKey());
        Assert.assertEquals(HttpStatus.OK.value(), p.getValue().intValue());

        // IPageResult
        IPageResult pageResult = Mockito.mock(IPageResult.class);
        Mockito.when(pageResult.getData()).thenReturn(new com.skrstop.framework.components.core.common.response.page.ListSimplePageData<>());
        Pair<Object, Integer> p2 = chain.execute(pageResult, false);
        Assert.assertNotNull(p2.getKey());

        // PageData
        com.skrstop.framework.components.core.common.response.page.ListSimplePageData<String> pageData =
                new com.skrstop.framework.components.core.common.response.page.ListSimplePageData<>();
        pageData.setRows(java.util.Collections.singletonList("row"));
        Pair<Object, Integer> p3 = chain.execute(pageData, false);
        Assert.assertNotNull(p3.getKey());

        // disable trans + IResult
        IResult result = Mockito.mock(IResult.class);
        Pair<Object, Integer> p4 = chain.execute(result, true);
        Assert.assertSame(result, ((com.skrstop.framework.components.core.common.response.Result) p4.getKey()).getData());

        // no interceptor supports (custom chain)
        com.skrstop.framework.components.starter.web.response.core.ResponseHandleChainPattern emptyChain =
                new com.skrstop.framework.components.starter.web.response.core.ResponseHandleChainPattern(new ArrayList<>());
        Pair<Object, Integer> p5 = emptyChain.execute("x", false);
        Assert.assertNull(p5.getKey());

        // interceptor returns null / next=true / next=false with null result
        com.skrstop.framework.components.starter.web.response.core.ResponseHandlerInterceptor nullReturn =
                new com.skrstop.framework.components.starter.web.response.core.ResponseHandlerInterceptor() {
                    @Override
                    public boolean support(Object returnValue) {
                        return "nullCase".equals(returnValue);
                    }

                    @Override
                    public int order() {
                        return 0;
                    }

                    @Override
                    public InterceptorResult execute(Object returnValue) {
                        return null;
                    }
                };
        com.skrstop.framework.components.starter.web.response.core.ResponseHandlerInterceptor nextTrue =
                new com.skrstop.framework.components.starter.web.response.core.ResponseHandlerInterceptor() {
                    @Override
                    public boolean support(Object returnValue) {
                        return "nextTrue".equals(returnValue);
                    }

                    @Override
                    public int order() {
                        return 1;
                    }

                    @Override
                    public InterceptorResult execute(Object returnValue) {
                        return InterceptorResult.builder().next(true).responseStatus(201).build();
                    }
                };
        com.skrstop.framework.components.starter.web.response.core.ResponseHandlerInterceptor resultNull =
                new com.skrstop.framework.components.starter.web.response.core.ResponseHandlerInterceptor() {
                    @Override
                    public boolean support(Object returnValue) {
                        return "resultNull".equals(returnValue);
                    }

                    @Override
                    public int order() {
                        return 2;
                    }

                    @Override
                    public InterceptorResult execute(Object returnValue) {
                        return InterceptorResult.builder().next(false).build();
                    }
                };
        com.skrstop.framework.components.starter.web.response.core.ResponseHandlerInterceptor withResult =
                new com.skrstop.framework.components.starter.web.response.core.ResponseHandlerInterceptor() {
                    @Override
                    public boolean support(Object returnValue) {
                        return "withResult".equals(returnValue);
                    }

                    @Override
                    public int order() {
                        return 3;
                    }

                    @Override
                    public InterceptorResult execute(Object returnValue) {
                        return InterceptorResult.builder().next(true).result(result).responseStatus(202).build();
                    }
                };
        List<com.skrstop.framework.components.starter.web.response.core.ResponseHandlerInterceptor> custom = new ArrayList<>();
        custom.add(nullReturn);
        custom.add(nextTrue);
        custom.add(resultNull);
        custom.add(withResult);
        com.skrstop.framework.components.starter.web.response.core.ResponseHandleChainPattern customChain =
                new com.skrstop.framework.components.starter.web.response.core.ResponseHandleChainPattern(custom);

        Pair<Object, Integer> r1 = customChain.execute("nullCase", false);
        Assert.assertNotNull(r1.getKey());
        Pair<Object, Integer> r2 = customChain.execute("nextTrue", false);
        Assert.assertNull(r2.getKey());
        Assert.assertEquals(201, r2.getValue().intValue());
        Pair<Object, Integer> r3 = customChain.execute("resultNull", false);
        Assert.assertNotNull(r3.getKey());
        Pair<Object, Integer> r4 = customChain.execute("withResult", false);
        Assert.assertSame(result, r4.getKey());
        Assert.assertEquals(202, r4.getValue().intValue());
    }
}
