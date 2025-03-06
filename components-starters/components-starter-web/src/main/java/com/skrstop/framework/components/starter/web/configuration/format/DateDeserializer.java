package com.skrstop.framework.components.starter.web.configuration.format;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author 蒋时华
 * @date 2023-11-30 15:56:44
 */
public class DateDeserializer extends JsonDeserializer<Date> {

    private SimpleDateFormat dateFormat;

    public DateDeserializer(String dateFormat) {
        this.dateFormat = new SimpleDateFormat(dateFormat);
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext deserializationContext)
            throws IOException {
        try {
            return dateFormat.parse(p.getValueAsString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
