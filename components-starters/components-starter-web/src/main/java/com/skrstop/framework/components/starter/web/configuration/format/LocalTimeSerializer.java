package com.skrstop.framework.components.starter.web.configuration.format;

import cn.hutool.core.date.TemporalAccessorUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalTime;

/**
 * @author 蒋时华
 * @date 2023-11-30 15:56:31
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
