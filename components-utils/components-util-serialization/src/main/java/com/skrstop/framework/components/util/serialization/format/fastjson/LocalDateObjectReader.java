package com.skrstop.framework.components.util.serialization.format.fastjson;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import lombok.AllArgsConstructor;

import java.lang.reflect.Type;
import java.time.LocalDate;

/**
 * @author 蒋时华
 * @date 2025-03-28 10:03:21
 * @since 1.0.0
 */
@AllArgsConstructor
public class LocalDateObjectReader implements ObjectReader<LocalDate> {
    private String format;

    @Override
    public LocalDate readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        String val = jsonReader.getString();
        return LocalDateTimeUtil.parseDate(val, format);
    }
}
