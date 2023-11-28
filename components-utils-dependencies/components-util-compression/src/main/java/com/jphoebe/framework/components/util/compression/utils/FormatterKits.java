/*
 * Copyright (C) 2013-2015 蒋时华
 *
 *             https://espringtran.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jphoebe.framework.components.util.compression.utils;

import java.text.DecimalFormat;

/**
 * Formatter Kits
 *
 * @author 蒋时华
 */
public class FormatterKits {

    private static final DecimalFormat dfDouble = new DecimalFormat("0.##");
    private static final DecimalFormat dfPercent = new DecimalFormat("0.##%");

    /**
     * Format double
     *
     * @param d double value
     * @return
     */
    public static String formatDouble(Double d) {
        if (d == null) return "";
        return dfDouble.format(d);
    }

    /**
     * Format percent
     *
     * @param d percent value
     * @return
     */
    public static String formatPercent(Double d) {
        if (d == null) return "";
        return dfPercent.format(d);
    }

    /**
     * Get size info
     *
     * @param size size of file
     * @return
     */
    public static String formatSizeInfo(long size) {
        double value = size;
        String unit = "Byte(s)";
        if (value >= 1024) {
            value /= 1024;
            unit = "KB(s)";
        }
        if (value >= 1024) {
            value /= 1024;
            unit = "MB(s)";
        }
        if (value >= 1024) {
            value /= 1024;
            unit = "GB(s)";
        }
        if (value >= 1024) {
            value /= 1024;
            unit = "GB(s)";
        }
        return formatDouble(value) + " " + unit;
    }

    /**
     * Get size info
     *
     * @param ms size of file
     * @return
     */
    public static String formatTimeInfo(long time) {
        String info = "";
        long ms = (long) time;
        int i = 1000;
        int value = (int) ms % i;
        info = value + " millisecond(s)";
        ms /= i;
        if (ms > 0) {
            i = 60;
            value = (int) ms % i;
            info = value + " second(s) " + info;
            ms /= i;
        }
        if (ms > 0) {
            i = 60;
            value = (int) ms % i;
            info = value + " minute(s) " + info;
            ms /= i;
        }
        if (ms > 0) {
            i = 24;
            value = (int) ms % i;
            info = value + " hour(s) " + info;
            ms /= i;
        }
        if (ms > 0) {
            i = 30;
            value = (int) ms % i;
            info = value + " day(s) " + info;
            ms /= i;
        }
        if (ms > 0) {
            i = 12;
            value = (int) ms % i;
            info = value + " month(s) " + info;
            ms /= i;
        }
        if (ms > 0) {
            info = ms + " year(s) " + info;
        }
        return info;
    }
}
