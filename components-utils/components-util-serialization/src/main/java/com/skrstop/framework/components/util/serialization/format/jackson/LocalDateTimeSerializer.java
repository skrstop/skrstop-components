package com.skrstop.framework.components.util.serialization.format.jackson;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author 蒋时华
 * @date 2025-03-28 09:43:51
 * @since 1.0.0
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    private final String format;

    public LocalDateTimeSerializer(String format) {
        this.format = format;
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(LocalDateTimeUtil.format(value, format));
    }
}
