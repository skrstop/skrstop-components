package com.skrstop.framework.components.util.serialization.format.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.skrstop.framework.components.util.value.data.DateUtil;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.Date;

/**
 * @author 蒋时华
 * @date 2025-03-28 09:46:29
 * @since 1.0.0
 */
@AllArgsConstructor
public class DateDeserializer extends JsonDeserializer<Date> {

    private final String format;

    @Override
    public Date deserialize(JsonParser p, DeserializationContext deserializationContext)
            throws IOException {
        return DateUtil.format(p.getValueAsString(), format);
    }
}
