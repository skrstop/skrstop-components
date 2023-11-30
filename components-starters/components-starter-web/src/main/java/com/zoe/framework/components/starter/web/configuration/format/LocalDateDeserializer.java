package com.zoe.framework.components.starter.web.configuration.format;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author 蒋时华
 * @date 2023-11-30 15:56:44
 */
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    private String format;

    public LocalDateDeserializer(String format) {
        this.format = format;
    }

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext deserializationContext)
            throws IOException {
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        return LocalDate.parse(p.getValueAsString(), DateTimeFormatter
                .ofPattern(format)
                .withZone(zone.toZoneId())
        );
    }
}
