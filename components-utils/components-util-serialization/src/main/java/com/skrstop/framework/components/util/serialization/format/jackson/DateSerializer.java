package com.skrstop.framework.components.util.serialization.format.jackson;

import java.text.SimpleDateFormat;

/**
 * @author 蒋时华
 * @date 2025-03-28 09:43:31
 * @since 1.0.0
 */
public class DateSerializer extends com.fasterxml.jackson.databind.ser.std.DateSerializer {

    private final String format;

    public DateSerializer(String format) {
        super(null, new SimpleDateFormat(format));
        this.format = format;
    }

}
