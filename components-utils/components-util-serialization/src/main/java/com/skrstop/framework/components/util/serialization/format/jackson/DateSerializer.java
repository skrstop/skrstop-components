package com.skrstop.framework.components.util.serialization.format.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.skrstop.framework.components.util.value.data.DateUtil;

import java.io.IOException;
import java.util.Date;

/**
 * @author 蒋时华
 * @date 2025-03-28 09:43:31
 * @since 1.0.0
 */
public class DateSerializer extends JsonSerializer<Date> {

    private final String format;

    public DateSerializer(String format) {
        this.format = format;
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(DateUtil.format(value, format));
    }
}
