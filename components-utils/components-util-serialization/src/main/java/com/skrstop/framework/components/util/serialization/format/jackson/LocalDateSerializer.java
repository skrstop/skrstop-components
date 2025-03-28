package com.skrstop.framework.components.util.serialization.format.jackson;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @author 蒋时华
 * @date 2025-03-28 09:43:11
 * @since 1.0.0
 */
public class LocalDateSerializer extends JsonSerializer<LocalDate> {

    private final String format;

    public LocalDateSerializer(String format) {
        this.format = format;
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(LocalDateTimeUtil.format(value, format));
    }
}
