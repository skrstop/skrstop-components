package com.skrstop.framework.components.util.serialization.format.jackson;

import java.time.format.DateTimeFormatter;

/**
 * @author 蒋时华
 * @date 2025-03-28 09:46:18
 * @since 1.0.0
 */
public class YearDeserializer extends com.fasterxml.jackson.datatype.jsr310.deser.YearDeserializer {

    private final String format;

    public YearDeserializer(String format) {
        super(DateTimeFormatter.ofPattern(format));
        this.format = format;
    }

}
