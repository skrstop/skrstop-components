package com.skrstop.framework.components.starter.web.configuration;

import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;

/**
 * WebMvcConfig class
 *
 * @author 蒋时华
 * @date 2018/11/22
 */
@Configuration
@EnableConfigurationProperties({GlobalResponseProperties.class})
public class DateTimeFormatAutoConfiguration implements WebMvcConfigurer {

    @Autowired
    private GlobalResponseProperties globalResponseProperties;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        if (ObjectUtil.isNotNull(globalResponseProperties)
                && StrUtil.isBlank(globalResponseProperties.getDateTimeFormat())
                && StrUtil.isBlank(globalResponseProperties.getDateFormat())
                && StrUtil.isBlank(globalResponseProperties.getTimeFormat())) {
            return;
        }
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        if (StrUtil.isNotBlank(globalResponseProperties.getDateFormat())) {
            registrar.setDateFormatter(DateTimeFormatter.ofPattern(globalResponseProperties.getDateFormat()));
        }
        if (StrUtil.isNotBlank(globalResponseProperties.getDateTimeFormat())) {
            registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(globalResponseProperties.getDateTimeFormat()));
        }
        if (StrUtil.isNotBlank(globalResponseProperties.getTimeFormat())) {
            registrar.setTimeFormatter(DateTimeFormatter.ofPattern(globalResponseProperties.getTimeFormat()));
        }
        registrar.registerFormatters(registry);
    }
}
