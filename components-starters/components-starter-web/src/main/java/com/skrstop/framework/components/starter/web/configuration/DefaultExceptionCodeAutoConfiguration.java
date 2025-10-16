package com.skrstop.framework.components.starter.web.configuration;

import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.starter.web.exception.code.WebStarterExceptionCode;
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
        if (ObjectUtil.isNotNull(defaultExceptionCode.getNotFound())) {
            CommonResultCode.NOT_FOUND.setCode(defaultExceptionCode.getNotFound());
        }
        if (ObjectUtil.isNotNull(defaultExceptionCode.getForbidden())) {
            CommonResultCode.FORBIDDEN.setCode(defaultExceptionCode.getForbidden());
        }
        if (ObjectUtil.isNotNull(defaultExceptionCode.getBusy())) {
            CommonResultCode.BUSY.setCode(defaultExceptionCode.getBusy());
        }
        if (ObjectUtil.isNotNull(defaultExceptionCode.getMatchParameter())) {
            WebStarterExceptionCode.MATCH_PARAMETER.setCode(defaultExceptionCode.getMatchParameter());
        }
        if (ObjectUtil.isNotNull(defaultExceptionCode.getNotSupportMediaType())) {
            WebStarterExceptionCode.NOT_SUPPORT_MEDIA_TYPE.setCode(defaultExceptionCode.getNotSupportMediaType());
        }
        if (ObjectUtil.isNotNull(defaultExceptionCode.getNotAcceptedMediaType())) {
            WebStarterExceptionCode.NOT_ACCEPTED_MEDIA_TYPE.setCode(defaultExceptionCode.getNotAcceptedMediaType());
        }
        if (ObjectUtil.isNotNull(defaultExceptionCode.getRequestTimeout())) {
            WebStarterExceptionCode.REQUEST_TIMEOUT.setCode(defaultExceptionCode.getRequestTimeout());
        }
        if (ObjectUtil.isNotNull(defaultExceptionCode.getRequestMethodNotAllowed())) {
            WebStarterExceptionCode.REQUEST_METHOD_NOT_ALLOWED.setCode(defaultExceptionCode.getRequestMethodNotAllowed());
        }
        if (ObjectUtil.isNotNull(defaultExceptionCode.getMissParameter())) {
            WebStarterExceptionCode.MISS_PARAMETER.setCode(defaultExceptionCode.getMissParameter());
        }
        if (ObjectUtil.isNotNull(defaultExceptionCode.getFileUpload())) {
            WebStarterExceptionCode.FILE_UPLOAD.setCode(defaultExceptionCode.getFileUpload());
        }
        if (ObjectUtil.isNotNull(defaultExceptionCode.getFileNotPoint())) {
            WebStarterExceptionCode.FILE_NOT_POINT.setCode(defaultExceptionCode.getFileNotPoint());
        }
    }

}
