package com.skrstop.framework.components.util.serialization.format.fastjson;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import lombok.AllArgsConstructor;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * @author 蒋时华
 * @date 2025-03-28 10:03:16
 * @since 1.0.0
 */
@AllArgsConstructor
public class LocalDateTimeObjectReader implements ObjectReader<LocalDateTime> {
    private String format;

    @Override
    public LocalDateTime readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        String val = jsonReader.getString();
        return LocalDateTimeUtil.parse(val, format);
    }
}
