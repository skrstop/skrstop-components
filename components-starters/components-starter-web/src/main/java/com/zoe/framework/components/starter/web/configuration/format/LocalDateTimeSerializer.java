package com.zoe.framework.components.starter.web.configuration.format;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author 蒋时华
 * @date 2023-11-30 15:56:59
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    private String format;

    public LocalDateTimeSerializer(String format) {
        this.format = format;
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        gen.writeString(value.format(DateTimeFormatter
                        .ofPattern(format)
                        .withZone(zone.toZoneId())
                )
        );
    }
}
