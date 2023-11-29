package com.zoe.framework.components.starter.web.response.interceptor;

import com.zoe.framework.components.core.common.response.*;
import com.zoe.framework.components.core.common.response.core.*;
import com.zoe.framework.components.starter.web.response.core.ResponseHandlerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:50:54
 */
@Configuration
@Slf4j
public class ICollectionResultResponseInterceptor implements ResponseHandlerInterceptor {

    @Override
    @SuppressWarnings("unchecked")
    public IResult execute(Object returnValue, boolean transResultResponse) {
        if (returnValue instanceof IListResult) {
            return ListResult.Builder.result((IListResult) returnValue);
        } else if (returnValue instanceof ILinkedMapResult) {
            return LinkedMapResult.Builder.result((ILinkedMapResult) returnValue);
        } else if (returnValue instanceof IMapResult) {
            return MapResult.Builder.result((IMapResult) returnValue);
        } else if (returnValue instanceof ILinkedSetResult) {
            return LinkedSetResult.Builder.result((ILinkedSetResult) returnValue);
        } else if (returnValue instanceof IHashSetResult) {
            return HashSetResult.Builder.result((IHashSetResult) returnValue);
        } else if (returnValue instanceof ISetResult) {
            return SetResult.Builder.result((ISetResult) returnValue);
        } else if (returnValue instanceof ICollectionResult) {
            return CollectionResult.Builder.result((ICollectionResult) returnValue);
        } else if (returnValue instanceof List && transResultResponse) {
            return ListResult.Builder.success((List) returnValue);
        } else if (returnValue instanceof LinkedHashMap && transResultResponse) {
            return LinkedMapResult.Builder.success((LinkedHashMap) returnValue);
        } else if (returnValue instanceof Map && transResultResponse) {
            return MapResult.Builder.success((Map) returnValue);
        } else if (returnValue instanceof LinkedHashSet && transResultResponse) {
            return LinkedSetResult.Builder.success((LinkedHashSet) returnValue);
        } else if (returnValue instanceof HashSet && transResultResponse) {
            return HashSetResult.Builder.success((HashSet) returnValue);
        } else if (returnValue instanceof Set && transResultResponse) {
            return SetResult.Builder.success((Set) returnValue);
        } else if (returnValue instanceof Collection && transResultResponse) {
            return CollectionResult.Builder.success((Collection) returnValue);
        }
        return null;
    }
}
