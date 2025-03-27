package com.skrstop.framework.components.starter.web.configuration.format;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalTime;

/**
 * @author 蒋时华
 * @date 2023-11-30 15:56:14
 */
public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {

    private final String format;

    public LocalTimeDeserializer(String format) {
        this.format = format;
    }

    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext deserializationContext)
            throws IOException {
        return LocalDateTimeUtil.parse(p.getValueAsString(), format).toLocalTime();
    }
}
