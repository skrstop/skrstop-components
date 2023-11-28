package com.jphoebe.framework.components.starter.web.exception.global.webmvc;

import com.jphoebe.framework.components.core.annotation.source.Snapshot;
import com.jphoebe.framework.components.core.common.response.common.CommonResultCode;
import com.jphoebe.framework.components.core.exception.AuntecRuntimeException;
import com.jphoebe.framework.components.core.exception.core.AuntecThrowable;
import com.jphoebe.framework.components.starter.web.exception.core.NotShowHttpStatusException;
import com.jphoebe.framework.components.starter.web.exception.core.ShowHtmlMessageException;
import com.jphoebe.framework.components.starter.web.exception.core.ShowJsonMessageException;
import com.jphoebe.framework.components.util.constant.HttpStatusConst;
import com.jphoebe.framework.components.util.value.data.ObjectUtil;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
@Snapshot(message = "新版本未测试")
public class AuntecErrorController extends AbstractErrorController {

    protected ErrorProperties errorProperties;
    protected ErrorAttributes errorAttributes;

    private final String FORWARD_REQUEST_URL = "javax.servlet.forward.request_uri";

    /**
     * Create a new {@link AuntecErrorController} instance.
     *
     * @param errorAttributes the error attributes
     * @param errorProperties configuration properties
     */
    public AuntecErrorController(ErrorAttributes errorAttributes,
                                 ErrorProperties errorProperties) {
        this(errorAttributes, errorProperties, Collections.emptyList());
    }

    /**
     * Create a new {@link AuntecErrorController} instance.
     *
     * @param errorAttributes    the error attributes
     * @param errorProperties    configuration properties
     * @param errorViewResolvers error view resolvers
     */
    public AuntecErrorController(ErrorAttributes errorAttributes,
                                 ErrorProperties errorProperties
            , List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorViewResolvers);
        Assert.notNull(errorProperties, "ErrorProperties must not be null");
        this.errorProperties = errorProperties;
        this.errorAttributes = errorAttributes;
    }


    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    protected ModelAndView errorHtml(HttpServletRequest request,
                                     HttpServletResponse response,
                                     final Exception ex,
                                     final WebRequest req) throws Throwable {
        HttpStatus status = getStatus(request);
        Map<String, Object> model = Collections
                .unmodifiableMap(getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML)));
        Throwable error = this.errorAttributes.getError(req);
        if (ObjectUtil.isNotNull(error)) {
            model.put("message", error.getMessage());
        }
        if (error instanceof ShowJsonMessageException && !(error instanceof ShowHtmlMessageException)) {
            response.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);
//            return Result.Builder.error(this.error(request, response, ex, req));
//            model = BeanUtil.beanToMap(Result.Builder.error(this.error(request, response, ex, req)));
            this.error(request, response, ex, req);
        } else if (error instanceof NotShowHttpStatusException) {
            response.setStatus(HttpStatusConst.HTTP_OK);
        } else {
            response.setStatus(status.value());
        }
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        return (modelAndView != null) ? modelAndView : new ModelAndView("error", model);
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object error(HttpServletRequest request,
                        HttpServletResponse response,
                        final Exception ex,
                        final WebRequest req) throws Throwable {
        Throwable error = this.errorAttributes.getError(req);
        if (error instanceof ShowHtmlMessageException && !(error instanceof ShowJsonMessageException)) {
            response.setHeader(HttpHeaders.ACCEPT, MediaType.TEXT_HTML_VALUE);
            return this.errorHtml(request, response, ex, req);
        }
        int status = response.getStatus();
        if (HttpStatusConst.HTTP_NOT_FOUND == status) {
            AuntecRuntimeException auntecRuntimeException = new AuntecRuntimeException(CommonResultCode.NOT_FOUND, ex);
            auntecRuntimeException.setExceptionMessage("源请求地址 -- " + request.getAttribute(FORWARD_REQUEST_URL));
            throw auntecRuntimeException;
        } else {
            if (error instanceof NotShowHttpStatusException) {
                response.setStatus(HttpStatusConst.HTTP_OK);
            }
            if (error instanceof AuntecThrowable) {
//                AuntecThrowable auntecThrowable = (AuntecThrowable) error;
//                throw new AuntecRuntimeException(auntecThrowable.getIResult());
                throw error;
            }
        }
        throw new AuntecRuntimeException(CommonResultCode.FAIL, ex);
    }

    protected ErrorAttributeOptions getErrorAttributeOptions(HttpServletRequest request, MediaType mediaType) {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        if (this.errorProperties.isIncludeException()) {
            options = options.including(ErrorAttributeOptions.Include.EXCEPTION);
        }
        if (isIncludeStackTrace(request, mediaType)) {
            options = options.including(ErrorAttributeOptions.Include.STACK_TRACE);
        }
        if (isIncludeMessage(request, mediaType)) {
            options = options.including(ErrorAttributeOptions.Include.MESSAGE);
        }
        if (isIncludeBindingErrors(request, mediaType)) {
            options = options.including(ErrorAttributeOptions.Include.BINDING_ERRORS);
        }
        return options;
    }

    /**
     * Determine if the stacktrace attribute should be included.
     *
     * @param request  the source request
     * @param produces the media type produced (or {@code MediaType.ALL})
     * @return if the stacktrace attribute should be included
     */
    protected boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces) {
        switch (getErrorProperties().getIncludeStacktrace()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
                return getTraceParameter(request);
            default:
                return false;
        }
    }

    /**
     * Determine if the message attribute should be included.
     *
     * @param request  the source request
     * @param produces the media type produced (or {@code MediaType.ALL})
     * @return if the message attribute should be included
     */
    protected boolean isIncludeMessage(HttpServletRequest request, MediaType produces) {
        switch (getErrorProperties().getIncludeMessage()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
                return getMessageParameter(request);
            default:
                return false;
        }
    }

    /**
     * Determine if the errors attribute should be included.
     *
     * @param request  the source request
     * @param produces the media type produced (or {@code MediaType.ALL})
     * @return if the errors attribute should be included
     */
    protected boolean isIncludeBindingErrors(HttpServletRequest request, MediaType produces) {
        switch (getErrorProperties().getIncludeBindingErrors()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
                return getErrorsParameter(request);
            default:
                return false;
        }
    }

    /**
     * Provide access to the error properties.
     *
     * @return the error properties
     */
    protected ErrorProperties getErrorProperties() {
        return this.errorProperties;
    }


}
