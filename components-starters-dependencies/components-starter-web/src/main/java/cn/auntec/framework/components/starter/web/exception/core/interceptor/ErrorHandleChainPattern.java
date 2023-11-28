package cn.auntec.framework.components.starter.web.exception.core.interceptor;

import cn.auntec.framework.components.core.common.response.core.IResult;
import cn.auntec.framework.components.starter.web.exception.global.interceptor.error.AuntecErrorInterceptor;
import cn.auntec.framework.components.starter.web.exception.global.interceptor.error.DefaultErrorInterceptor;
import cn.auntec.framework.components.util.value.data.ObjectUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2020-05-08 13:14:38
 */
@Configuration
@Getter
@Setter
public class ErrorHandleChainPattern {

    private final List<ErrorHandlerInterceptor> errorHandleChainPatterns;

    public ErrorHandleChainPattern(List<ErrorHandlerInterceptor> errorHandleChainPatterns) {
        this.errorHandleChainPatterns = errorHandleChainPatterns;
    }

    @PostConstruct
    private void initChainPattern() {
        Collections.sort(errorHandleChainPatterns, AnnotationAwareOrderComparator.INSTANCE);
        errorHandleChainPatterns.add(new AuntecErrorInterceptor());
        errorHandleChainPatterns.add(new DefaultErrorInterceptor());
    }

    public IResult execute(Error e) {
        for (ErrorHandlerInterceptor errorHandlerInterceptor : errorHandleChainPatterns) {
            IResult IResult = errorHandlerInterceptor.execute(e);
            if (ObjectUtil.isNotNull(IResult)) {
                return IResult;
            }
        }
        return null;
    }

}
