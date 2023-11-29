package com.zoe.framework.components.util.compression.bean;

import lombok.Getter;

/**
 * Zip Level
 *
 * @author 蒋时华
 */
public enum ZipLevel implements CompressionLevel {

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

    ZipLevel(int value) {
        this.value = value;
    }
}
