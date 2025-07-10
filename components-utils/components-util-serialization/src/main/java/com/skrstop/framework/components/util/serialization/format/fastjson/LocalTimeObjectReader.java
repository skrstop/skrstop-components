package com.skrstop.framework.components.util.serialization.format.fastjson;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.skrstop.framework.components.util.value.data.StrUtil;
import lombok.AllArgsConstructor;

import java.lang.reflect.Type;
import java.time.LocalTime;

/**
 * @author 蒋时华
 * @date 2025-03-28 10:03:26
 * @since 1.0.0
 */
@AllArgsConstructor
public class LocalTimeObjectReader implements ObjectReader<LocalTime> {
    private String format;

    @Override
    public LocalTime readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        String val = jsonReader.getString();
        if (StrUtil.isBlank(val)) {
            return null;
        }
        return LocalDateTimeUtil.parse(val, format).toLocalTime();
    }
}
