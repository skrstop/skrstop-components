package com.zoe.framework.components.starter.annotation.handle.trim;

import com.zoe.framework.components.starter.annotation.anno.trim.Trim;
import lombok.Setter;
import org.springframework.context.support.EmbeddedValueResolutionSupport;
import org.springframework.format.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 蒋时华
 * @date 2021-02-24 14:44:32
 */
public class TrimAnnotationFormatterFactory extends EmbeddedValueResolutionSupport
        implements AnnotationFormatterFactory<Trim>, WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldAnnotation(this);
    }

    private static final Map<Class<?>, TrimFormatter<?>> TRIMMER_FORMATTER_MAP;

    static {
        TRIMMER_FORMATTER_MAP = new ConcurrentHashMap<>();
        TRIMMER_FORMATTER_MAP.put(String.class, new TrimmerStringFormatter());
    }

    @Override
    public Set<Class<?>> getFieldTypes() {
        Set<Class<?>> fieldTypes = new HashSet<Class<?>>(1);
        fieldTypes.add(String.class);
        return fieldTypes;
    }

    @Override
    public Parser<?> getParser(Trim annotation, Class<?> fieldType) {
        TrimFormatter<?> trimFormatter = TRIMMER_FORMATTER_MAP.get(fieldType);
        trimFormatter.setAnnotation(annotation);
        return trimFormatter;
    }

    @Override
    public Printer<?> getPrinter(Trim annotation, Class<?> fieldType) {
        TrimFormatter<?> trimFormatter = TRIMMER_FORMATTER_MAP.get(fieldType);
        trimFormatter.setAnnotation(annotation);
        return trimFormatter;
    }

    private interface TrimFormatter<T> extends Formatter<T> {
        /**
         * 自定义注解参数
         *
         * @param annotation
         */
        void setAnnotation(Trim annotation);
    }

    private static class TrimmerStringFormatter implements TrimFormatter<String> {
        @Setter
        private Trim annotation;

        @Override
        public String print(String object, Locale locale) {
            return object;
        }

        @Override
        public String parse(String text, Locale locale) {
            return TrimParse.parse(annotation, text);
        }

    }
}
