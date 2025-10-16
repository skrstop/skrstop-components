package com.skrstop.framework.components.starter.web.configuration;

import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author 蒋时华
 * @date 2019-08-17 16:27:11
 * @since 1.0.0
 */
@Configuration
public class DefaultExceptionCodeAutoConfiguration {

    @Autowired
    private GlobalResponseProperties globalResponseProperties;

    @PostConstruct
    public void init() {
        if (ObjectUtil.isNull(globalResponseProperties) || ObjectUtil.isNull(globalResponseProperties.getDefaultExceptionCode())) {
            return;
        }
        GlobalResponseProperties.DefaultExceptionCode defaultExceptionCode = globalResponseProperties.getDefaultExceptionCode();
        if (ObjectUtil.isNotNull(defaultExceptionCode.getSuccess())) {
            CommonResultCode.SUCCESS.setCode(defaultExceptionCode.getSuccess());
        }
        if (ObjectUtil.isNotNull(defaultExceptionCode.getFail())) {
            CommonResultCode.FAIL.setCode(defaultExceptionCode.getFail());
        }
    }

}
