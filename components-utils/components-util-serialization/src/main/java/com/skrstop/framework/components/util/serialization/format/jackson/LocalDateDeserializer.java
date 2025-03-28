package com.skrstop.framework.components.util.serialization.format.jackson;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @author 蒋时华
 * @date 2025-03-28 09:45:59
 * @since 1.0.0
 */
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    private final String format;

    public LocalDateDeserializer(String format) {
        this.format = format;
    }

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext deserializationContext)
            throws IOException {
        return LocalDateTimeUtil.parseDate(p.getValueAsString(), format);
    }
}
