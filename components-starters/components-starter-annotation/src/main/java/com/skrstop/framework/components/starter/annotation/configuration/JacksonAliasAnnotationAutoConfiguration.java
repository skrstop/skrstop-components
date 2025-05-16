package com.skrstop.framework.components.starter.annotation.configuration;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skrstop.framework.components.starter.annotation.handle.paramAlias.AliasJacksonAnnotationIntrospector;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

/**
 * @author 蒋时华
 * @date 2023-12-14 12:08:38
 */
@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
public class JacksonAliasAnnotationAutoConfiguration {

    public JacksonAliasAnnotationAutoConfiguration(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                ObjectMapper mapper = ((MappingJackson2HttpMessageConverter) converter).getObjectMapper();
                AnnotationIntrospector annotationIntrospector = mapper.getSerializationConfig().getAnnotationIntrospector();
                AliasJacksonAnnotationIntrospector aliasJacksonAnnotationIntrospector = new AliasJacksonAnnotationIntrospector();
                AnnotationIntrospector pair = AnnotationIntrospector.pair(annotationIntrospector, aliasJacksonAnnotationIntrospector);
                mapper.setAnnotationIntrospector(pair);
            }
        }
    }

}
