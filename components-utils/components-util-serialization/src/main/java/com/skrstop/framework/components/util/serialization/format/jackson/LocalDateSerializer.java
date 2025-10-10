package com.skrstop.framework.components.util.serialization.format.jackson;

import java.time.format.DateTimeFormatter;

/**
 * @author 蒋时华
 * @date 2025-03-28 09:43:11
 * @since 1.0.0
 */
public class LocalDateSerializer extends com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer {

    private final String format;

    public LocalDateSerializer(String format) {
        super(DateTimeFormatter.ofPattern(format));
        this.format = format;
    }

}
