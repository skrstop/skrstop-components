package com.jphoebe.framework.components.starter.annotation.paramAlias;

import com.jphoebe.framework.components.util.value.validate.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import javax.servlet.ServletRequest;

/**
 * @author: wangzh
 * @date: 2021/3/11 19:51
 * @description: desc...
 */
public class AliasModelAttributeMethodServletProcessor extends ServletModelAttributeMethodProcessor {

    public AliasModelAttributeMethodServletProcessor(final boolean annotationNotRequired) {
        super(annotationNotRequired);
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
        Assert.state(servletRequest != null, "No ServletRequest");
        new AliasDataBinder(binder.getTarget(), binder.getObjectName()).bind(servletRequest);
    }

}
