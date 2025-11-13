package com.skrstop.framework.components.util.serialization.format.jackson;

import com.fasterxml.jackson.databind.deser.std.DateDeserializers;

import java.text.SimpleDateFormat;

/**
 * @author 蒋时华
 * @date 2025-03-28 09:46:29
 * @since 1.0.0
 */
public class DateDeserializer extends DateDeserializers.DateDeserializer {

    private final String format;

    public DateDeserializer(String format) {
        super(new DateDeserializers.DateDeserializer(), new SimpleDateFormat(format), format);
        this.format = format;
    }

}
