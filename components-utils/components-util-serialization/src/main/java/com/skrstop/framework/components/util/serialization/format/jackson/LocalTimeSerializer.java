package com.skrstop.framework.components.util.serialization.format.jackson;

import cn.hutool.core.date.TemporalAccessorUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalTime;

/**
 * @author 蒋时华
 * @date 2025-03-28 09:44:06
 * @since 1.0.0
 */
public class LocalTimeSerializer extends JsonSerializer<LocalTime> {

    private final String format;

    public LocalTimeSerializer(String format) {
        this.format = format;
    }

    @Override
    public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(TemporalAccessorUtil.format(value, format));
    }
}
