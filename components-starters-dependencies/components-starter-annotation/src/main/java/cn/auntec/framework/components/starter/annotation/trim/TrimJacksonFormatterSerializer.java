package cn.auntec.framework.components.starter.annotation.trim;

import cn.auntec.framework.components.core.exception.defined.illegal.NotSupportedException;
import cn.auntec.framework.components.starter.annotation.Trim;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * @author 蒋时华
 * @date 2021-02-24 16:50:44
 */
public class TrimJacksonFormatterSerializer extends StdSerializer<String> implements ContextualSerializer {

    private static final long serialVersionUID = -8984143986763270381L;
    private Trim annotation;

    public TrimJacksonFormatterSerializer() {
        super(String.class);
    }

    public TrimJacksonFormatterSerializer(Trim annotation) {
        super(String.class);
        this.annotation = annotation;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty beanProperty) throws JsonMappingException {
        if (!String.class.equals(beanProperty.getType().getRawClass())) {
            throw new NotSupportedException("@Trim only support String");
        }
        Trim annotation = beanProperty.getAnnotation(Trim.class);
        return new TrimJacksonFormatterSerializer(annotation);
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(TrimParse.parse(annotation, value));
    }
}
