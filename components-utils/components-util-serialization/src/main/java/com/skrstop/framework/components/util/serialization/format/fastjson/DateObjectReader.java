package com.skrstop.framework.components.util.serialization.format.fastjson;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.skrstop.framework.components.util.value.data.DateUtil;
import lombok.AllArgsConstructor;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author 蒋时华
 * @date 2025-03-28 10:03:11
 * @since 1.0.0
 */
@AllArgsConstructor
public class DateObjectReader implements ObjectReader<Date> {
    private String format;

    @Override
    public Date readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        String val = jsonReader.getString();
        return DateUtil.format(val, format);
    }
}
