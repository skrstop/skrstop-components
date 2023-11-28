package com.jphoebe.framework.components.util.compression.bean;

import lombok.Getter;

/**
 * Bzip Level
 *
 * @author 蒋时华
 */
public enum Bzip2Level implements CompressionLevel {

    /**
     * no compress
     */
    NO_COMPRESS(0),
    LOWEST(1),
    LOW(2),
    NORMAL(3),
    HIGH(4),
    HIGHEST(5),
    ;

    @Getter
    private final int value;

    Bzip2Level(int value) {
        this.value = value;
    }
}
