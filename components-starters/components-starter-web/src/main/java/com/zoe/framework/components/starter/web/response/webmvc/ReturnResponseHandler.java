package com.zoe.framework.components.starter.web.response.webmvc;

import com.zoe.framework.components.starter.web.configuration.GlobalResponseProperties;
import com.zoe.framework.components.starter.web.response.DisableGlobalResponse;
import com.zoe.framework.components.starter.web.response.DisableTransResultTypeResponse;
import com.zoe.framework.components.starter.web.response.core.ResponseHandleChainPattern;
import com.zoe.framework.components.util.constant.FeignConst;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import com.zoe.framework.components.util.value.url.UrlFilterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * 全局统一返回值格式
 *
 * @author 蒋时华
 * @date 2018/11/22
 */
@Slf4j
public class ReturnResponseHandler implements HandlerMethodReturnValueHandler {

    private final HandlerMethodReturnValueHandler delegate;
    private final GlobalResponseProperties globalResponseProperties;
    private final ResponseHandleChainPattern responseHandleChainPattern;

    public ReturnResponseHandler(HandlerMethodReturnValueHandler delegate, GlobalResponseProperties globalResponseProperties, ResponseHandleChainPattern responseHandleChainPattern) {
        this.delegate = delegate;
        this.globalResponseProperties = globalResponseProperties;
        this.responseHandleChainPattern = responseHandleChainPattern;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return delegate.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue,
                                  MethodParameter returnType,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {
        if (globalResponseProperties == null || globalResponseProperties.getEnable()) {
            boolean validPath = false;
            try {
                // 原request
                HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);
                // 当前路径包含在NotTransResultList的集合中，不进行全局替换。
                validPath = UrlFilterUtil.valid(ObjectUtil.isNull(globalResponseProperties) ? new ArrayList<>() : globalResponseProperties.getDisableGlobalResponseList()
                        , nativeRequest.getServletPath());
            } catch (Exception e) {
                log.error("返回值处理异常：", e);
            }
            // 使用 全局返回值
            DisableGlobalResponse disableGlobalResponseMethod = returnType.getMethod().getAnnotation(DisableGlobalResponse.class);
            DisableGlobalResponse disableGlobalResponseClass = returnType.getMethod().getClass().getAnnotation(DisableGlobalResponse.class);
            if (!validPath && disableGlobalResponseMethod == null && disableGlobalResponseClass == null) {
                // 判断是否支持feign
                String useFeign = webRequest.getHeader(FeignConst.USE_FEIGN_NAME);
                if (FeignConst.USE_FEIGN_VALUE.equals(useFeign)
                        && ObjectUtil.isNotNull(globalResponseProperties)
                        && !globalResponseProperties.getSupportFeign()) {
                    // 不支持feign调用返回值的封装
                } else {
                    DisableTransResultTypeResponse disableTransResultTypeResponseMethod = returnType.getMethod().getAnnotation(DisableTransResultTypeResponse.class);
                    DisableTransResultTypeResponse disableTransResultTypeResponseClass = returnType.getMethod().getClass().getAnnotation(DisableTransResultTypeResponse.class);
                    boolean transResultResponse = disableTransResultTypeResponseMethod == null && disableTransResultTypeResponseClass == null;
                    returnValue = responseHandleChainPattern.execute(returnValue, transResultResponse);
                }
            }
        }
        delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }
}
