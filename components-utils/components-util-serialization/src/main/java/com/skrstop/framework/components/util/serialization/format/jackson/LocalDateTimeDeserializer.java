package com.skrstop.framework.components.util.serialization.format.jackson;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author 蒋时华
 * @date 2025-03-28 09:46:08
 * @since 1.0.0
 */
@AllArgsConstructor
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private final String format;

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext)
            throws IOException {
        return LocalDateTimeUtil.parse(p.getValueAsString(), format);
    }
}
