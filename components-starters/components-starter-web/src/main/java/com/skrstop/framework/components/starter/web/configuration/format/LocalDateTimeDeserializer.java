package com.skrstop.framework.components.starter.web.configuration.format;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author 蒋时华
 * @date 2023-11-30 15:57:25
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private String format;

    public LocalDateTimeDeserializer(String format) {
        this.format = format;
    }

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext)
            throws IOException {
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        return LocalDateTime.parse(p.getValueAsString(), DateTimeFormatter
                .ofPattern(format)
                .withZone(zone.toZoneId())
        );
    }
}
