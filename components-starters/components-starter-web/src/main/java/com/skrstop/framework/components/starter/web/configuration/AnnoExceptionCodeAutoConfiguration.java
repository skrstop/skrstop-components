package com.skrstop.framework.components.starter.web.configuration;

import com.skrstop.framework.components.starter.annotation.exception.code.WebAnnotationExceptionCode;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author 蒋时华
 * @date 2019-08-17 16:27:11
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(name = "com.skrstop.framework.components.starter.annotation.configuration.AnnotationAutoConfiguration")
public class AnnoExceptionCodeAutoConfiguration {

    @Autowired
    private GlobalResponseProperties globalResponseProperties;

    @PostConstruct
    public void init() {
        if (ObjectUtil.isNull(globalResponseProperties) || ObjectUtil.isNull(globalResponseProperties.getDefaultExceptionCode())) {
            return;
        }
        GlobalResponseProperties.DefaultExceptionCode defaultExceptionCode = globalResponseProperties.getDefaultExceptionCode();
        if (ObjectUtil.isNotNull(defaultExceptionCode.getAccessLimit())) {
            WebAnnotationExceptionCode.ACCESS_LIMIT.setCode(defaultExceptionCode.getAccessLimit());
        }
        if (ObjectUtil.isNotNull(defaultExceptionCode.getControllerDeprecated())) {
            WebAnnotationExceptionCode.CONTROLLER_DEPRECATED.setCode(defaultExceptionCode.getControllerDeprecated());
        }
        if (ObjectUtil.isNotNull(defaultExceptionCode.getIntranetLimit())) {
            WebAnnotationExceptionCode.INTRANET_LIMIT.setCode(defaultExceptionCode.getIntranetLimit());
        }
        if (ObjectUtil.isNotNull(defaultExceptionCode.getPrivacyInfo())) {
            WebAnnotationExceptionCode.PRIVACY_INFO.setCode(defaultExceptionCode.getPrivacyInfo());
        }
        if (ObjectUtil.isNotNull(defaultExceptionCode.getNoProcessor())) {
            WebAnnotationExceptionCode.NO_PROCESSOR.setCode(defaultExceptionCode.getNoProcessor());
        }
    }

}
