package com.skrstop.framework.components.starter.web.exception.global.webmvc;

import com.skrstop.framework.components.starter.web.configuration.GlobalExceptionProperties;
import jakarta.servlet.Servlet;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link EnableAutoConfiguration Auto-configuration} to render errors via an MVC error
 * controller.
 *
 * @author Dave Syer
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 * @author Brian Clozel
 */
@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnProperty(value = "skrstop.exception.config.enable", havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties({ServerProperties.class, WebMvcProperties.class, GlobalExceptionProperties.class})
public class ErrorMvcAutoConfiguration {

    private final ServerProperties serverProperties;
    private final DispatcherServletPath dispatcherServletPath;
    private final List<ErrorViewResolver> errorViewResolvers;
    private final GlobalExceptionProperties globalExceptionProperties;

    public ErrorMvcAutoConfiguration(ServerProperties serverProperties,
                                     DispatcherServletPath dispatcherServletPath,
                                     ObjectProvider<ErrorViewResolver> errorViewResolvers, GlobalExceptionProperties globalExceptionProperties) {
        this.serverProperties = serverProperties;
        this.dispatcherServletPath = dispatcherServletPath;
        this.errorViewResolvers = errorViewResolvers.orderedStream()
                .collect(Collectors.toList());
        this.globalExceptionProperties = globalExceptionProperties;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(value = DefaultErrorController.class,
            search = SearchStrategy.CURRENT)
    public DefaultErrorAttributes errorAttributes() {
//        this.serverProperties.getError().isIncludeException();
        return new DefaultErrorAttributes();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(value = DefaultErrorController.class,
            search = SearchStrategy.CURRENT)
    @ConditionalOnProperty(value = "skrstop.exception.config.has-html-error", havingValue = "false", matchIfMissing = true)
    public DefaultErrorController basicErrorController(ErrorAttributes errorAttributes) {
        return new DefaultErrorController(errorAttributes, this.serverProperties.getError(),
                this.errorViewResolvers);
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(value = DefaultErrorController.class,
            search = SearchStrategy.CURRENT)
    @ConditionalOnProperty(value = "skrstop.exception.config.has-html-error", havingValue = "true", matchIfMissing = false)
    public DefaultErrorController basicErrorHtmlController(ErrorAttributes errorAttributes) {
        return new DefaultErrorHtmlController(errorAttributes, this.serverProperties.getError(),
                this.errorViewResolvers);
    }

}
