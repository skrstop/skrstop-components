package com.skrstop.framework.components.starter.web.configuration.format;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.skrstop.framework.components.util.value.data.DateUtil;

import java.io.IOException;
import java.util.Date;

/**
 * @author 蒋时华
 * @date 2023-11-30 15:56:44
 */
public class DateDeserializer extends JsonDeserializer<Date> {

    private final String format;

    public DateDeserializer(String format) {
        this.format = format;
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext deserializationContext)
            throws IOException {
        return DateUtil.format(p.getValueAsString(), format);
    }
}
