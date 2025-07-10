package com.skrstop.framework.components.starter.web.exception.global.webmvc;

import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.core.exception.core.BusinessThrowable;
import com.skrstop.framework.components.core.exception.core.SkrstopThrowable;
import com.skrstop.framework.components.core.exception.defined.illegal.ParameterException;
import com.skrstop.framework.components.starter.web.exception.core.NotShowHttpStatusException;
import com.skrstop.framework.components.starter.web.exception.core.ShowHtmlMessageException;
import com.skrstop.framework.components.starter.web.exception.core.ShowJsonMessageException;
import com.skrstop.framework.components.util.constant.HttpStatusConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class DefaultErrorController extends AbstractErrorController {

    protected ErrorProperties errorProperties;
    protected ErrorAttributes errorAttributes;

    private final String FORWARD_REQUEST_URL = "jakarta.servlet.forward.request_uri";

    /**
     * Create a new {@link DefaultErrorController} instance.
     *
     * @param errorAttributes the error attributes
     * @param errorProperties configuration properties
     */
    public DefaultErrorController(ErrorAttributes errorAttributes,
                                  ErrorProperties errorProperties) {
        this(errorAttributes, errorProperties, Collections.emptyList());
    }

    /**
     * Create a new {@link DefaultErrorController} instance.
     *
     * @param errorAttributes    the error attributes
     * @param errorProperties    configuration properties
     * @param errorViewResolvers error view resolvers
     */
    public DefaultErrorController(ErrorAttributes errorAttributes,
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
        response.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        this.error(request, response, ex, req);
        return null;
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND
                    , "404请求，源请求地址 -- " + request.getAttribute(FORWARD_REQUEST_URL));
        } else {
            if (error instanceof NotShowHttpStatusException || error instanceof BusinessThrowable) {
                response.setStatus(HttpStatusConst.HTTP_OK);
            } else if (error instanceof ParameterException) {
                response.setStatus(HttpStatusConst.HTTP_BAD_REQUEST);
            } else {
                response.setStatus(HttpStatusConst.HTTP_INTERNAL_ERROR);
            }
            if (error instanceof SkrstopThrowable) {
//                YnwThrowable ynwThrowable = (YnwThrowable) error;
//                throw new YnwRuntimeException(ynwThrowable.getIResult());
                throw error;
            }
        }
        throw new SkrstopRuntimeException(CommonResultCode.FAIL, ex);
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
