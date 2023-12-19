package com.skrstop.framework.components.starter.annotation.handle.trim;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.skrstop.framework.components.starter.annotation.anno.trim.Trim;

import java.io.IOException;

/**
 * @author 蒋时华
 * @date 2021-02-24 16:50:44
 */
public class TrimJacksonFormatterDeserializer extends StdDeserializer<Object> implements ContextualDeserializer {
    private static final long serialVersionUID = 6361739963166916386L;

    private Trim annotation;
    private BeanProperty beanProperty;

    public TrimJacksonFormatterDeserializer() {
        super(Object.class);
    }

    public TrimJacksonFormatterDeserializer(Trim annotation, BeanProperty beanProperty) {
        super(Object.class);
        this.annotation = annotation;
        this.beanProperty = beanProperty;
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return TrimParse.parse(annotation, p.getValueAsString());
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        if (!String.class.equals(property.getType().getRawClass())) {
            throw new NotSupportedException("@Trim only support String");
        }
        Trim annotation = property.getAnnotation(Trim.class);
        return new TrimJacksonFormatterDeserializer(annotation, property);
    }
}
