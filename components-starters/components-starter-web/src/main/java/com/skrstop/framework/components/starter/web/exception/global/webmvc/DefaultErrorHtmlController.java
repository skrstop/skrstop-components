package com.skrstop.framework.components.starter.web.exception.global.webmvc;

import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.exception.core.BusinessThrowable;
import com.skrstop.framework.components.starter.web.exception.core.NotShowHttpStatusException;
import com.skrstop.framework.components.starter.web.exception.core.ShowHtmlMessageException;
import com.skrstop.framework.components.starter.web.exception.core.ShowJsonMessageException;
import com.skrstop.framework.components.util.constant.HttpStatusConst;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class DefaultErrorHtmlController extends DefaultErrorController {

    public DefaultErrorHtmlController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
        super(errorAttributes, errorProperties);
    }

    public DefaultErrorHtmlController(ErrorAttributes errorAttributes, ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorProperties, errorViewResolvers);
    }

    @Override
    @RequestMapping(consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request,
                                  HttpServletResponse response,
                                  final Exception ex,
                                  final WebRequest req) throws Throwable {
        HttpStatus status = getStatus(request);
        Throwable error = this.errorAttributes.getError(req);
        if (error instanceof NotShowHttpStatusException) {
            response.setStatus(HttpStatusConst.HTTP_OK);
        } else {
            response.setStatus(status.value());
        }
        if (error instanceof ShowJsonMessageException && !(error instanceof ShowHtmlMessageException)) {
            response.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            this.error(request, response, ex, req);
        }
        Map<String, Object> model = new java.util.HashMap<>(Collections
                .unmodifiableMap(getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML))));
        if (ObjectUtil.isNotNull(error) || error instanceof BusinessThrowable) {
            model.put("message", error.getMessage());
        } else {
            model.put("message", CommonResultCode.FAIL.getMessage());
        }
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        return (modelAndView != null) ? modelAndView : new ModelAndView("error", model);
    }

}
