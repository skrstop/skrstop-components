package com.skrstop.framework.components.starter.web.configuration;

import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.DateUtil;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateTimeFormatAnnotationFormatterFactory;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * WebMvcConfig class
 *
 * @author 蒋时华
 * @date 2018/11/22
 */
@Configuration
@ConditionalOnClass(WebMvcConfigurer.class)
@EnableConfigurationProperties({GlobalResponseProperties.class})
@SuppressWarnings("all")
public class DateTimeFormatAutoConfiguration implements WebMvcConfigurer {

    @Autowired
    private GlobalResponseProperties globalResponseProperties;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        if (ObjectUtil.isNull(globalResponseProperties)) {
            return;
        }
        if (StrUtil.isBlank(globalResponseProperties.getDateTimeFormat())
                && StrUtil.isBlank(globalResponseProperties.getDateFormat())
                && StrUtil.isBlank(globalResponseProperties.getTimeFormat())) {
            return;
        }
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        if (StrUtil.isNotBlank(globalResponseProperties.getDateTimeFormat())) {
            registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(globalResponseProperties.getDateTimeFormat()));
            // 删除默认的配置
            registry.removeConvertible(String.class, Date.class);
            // 作为兜底的策略
            registry.addConverter(new StringToDateConverter(globalResponseProperties.getDateTimeFormat()));
            // 自带的注解策略
            registry.addFormatterForFieldAnnotation(new DateTimeFormatAnnotationFormatterFactory());
            // 注解增强策略
            registry.addFormatterForFieldAnnotation(new CustomDateTimeFormatAnnotationFormatterFactory(globalResponseProperties.getDateTimeFormat()));
        }
        if (StrUtil.isNotBlank(globalResponseProperties.getDateFormat())) {
            registrar.setDateFormatter(DateTimeFormatter.ofPattern(globalResponseProperties.getDateFormat()));
        }
        if (StrUtil.isNotBlank(globalResponseProperties.getTimeFormat())) {
            registrar.setTimeFormatter(DateTimeFormatter.ofPattern(globalResponseProperties.getTimeFormat()));
        }
        registrar.registerFormatters(registry);
    }

    @AllArgsConstructor
    @Slf4j
    public static class StringToDateConverter implements Converter<String, Date> {

        private String format;

        @Override
        public Date convert(String source) {
            if (source == null || source.isEmpty()) {
                return null;
            }
            try {
                return DateUtil.format(source, format);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.error("resolve date format error: source = {}, format = {}", source, format);
                throw e;
            }
        }
    }

    @AllArgsConstructor
    public static class CustomDateTimeFormatAnnotationFormatterFactory extends DateTimeFormatAnnotationFormatterFactory {

        private final String defaultPattern;

        @Override
        public Set<Class<?>> getFieldTypes() {
            return CollectionUtil.newHashSet(Date.class);
        }

        protected Formatter<Date> getFormatter(DateTimeFormat annotation, Class<?> fieldType) {
            DateFormatter formatter = new DateFormatter();
            formatter.setSource(annotation);
            formatter.setIso(annotation.iso());

            String style = resolveEmbeddedValue(annotation.style());
            if (StringUtils.hasLength(style)) {
                formatter.setStylePattern(style);
            }

            String pattern = resolveEmbeddedValue(annotation.pattern());
            if (StrUtil.isBlank(pattern)) {
                pattern = defaultPattern;
            }
            if (StringUtils.hasLength(pattern)) {
                formatter.setPattern(pattern);
            }

            List<String> resolvedFallbackPatterns = new ArrayList<>();
            for (String fallbackPattern : annotation.fallbackPatterns()) {
                String resolvedFallbackPattern = resolveEmbeddedValue(fallbackPattern);
                if (StringUtils.hasLength(resolvedFallbackPattern)) {
                    resolvedFallbackPatterns.add(resolvedFallbackPattern);
                }
            }
            if (!resolvedFallbackPatterns.isEmpty()) {
                formatter.setFallbackPatterns(resolvedFallbackPatterns.toArray(new String[0]));
            }

            return formatter;
        }
    }

}