package com.skrstop.framework.components.util.serialization.format.jackson;

import com.fasterxml.jackson.databind.deser.std.DateDeserializers;

import java.text.SimpleDateFormat;

/**
 * @author 蒋时华
 * @date 2025-03-28 09:46:29
 * @since 1.0.0
 */
public class CalendarDeserializer extends DateDeserializers.CalendarDeserializer {

    private final String format;

    public CalendarDeserializer(String format) {
        super(new DateDeserializers.CalendarDeserializer(), new SimpleDateFormat(format), format);
        this.format = format;
    }

}
