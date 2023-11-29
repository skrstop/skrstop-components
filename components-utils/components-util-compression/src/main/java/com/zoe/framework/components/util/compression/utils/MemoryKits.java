package com.zoe.framework.components.util.compression.utils;

/**
 * Memory Kits
 *
 * @author 蒋时华
 */
public class MemoryKits {

    static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    static long getUsedMemory() {
        return (getMaxMemory() - getFreeMemory());
    }
}
