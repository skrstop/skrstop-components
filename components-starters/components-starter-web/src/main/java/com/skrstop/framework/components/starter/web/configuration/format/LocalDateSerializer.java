package com.skrstop.framework.components.starter.web.configuration.format;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @author 蒋时华
 * @date 2023-11-30 15:57:14
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
