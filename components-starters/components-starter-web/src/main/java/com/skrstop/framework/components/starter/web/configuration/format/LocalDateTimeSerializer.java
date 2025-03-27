package com.skrstop.framework.components.starter.web.configuration.format;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author 蒋时华
 * @date 2023-11-30 15:56:59
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
