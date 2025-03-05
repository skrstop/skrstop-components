package com.skrstop.framework.components.starter.web.configuration.format;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author 蒋时华
 * @date 2023-11-30 15:57:14
 */
public class DateSerializer extends JsonSerializer<Date> {

    private SimpleDateFormat dateFormat;

    public DateSerializer(String dateFormat) {
        this.dateFormat = new SimpleDateFormat(dateFormat);
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(dateFormat.format(value));
    }
}
