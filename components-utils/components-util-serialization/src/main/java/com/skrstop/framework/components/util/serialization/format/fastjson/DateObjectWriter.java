package com.skrstop.framework.components.util.serialization.format.fastjson;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.skrstop.framework.components.util.value.data.DateUtil;
import lombok.AllArgsConstructor;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author 蒋时华
 * @date 2025-03-28 09:59:13
 * @since 1.0.0
 */
@AllArgsConstructor
public class DateObjectWriter implements ObjectWriter<Date> {

    private String format;

    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
        jsonWriter.writeString(DateUtil.format((Date) object, format));
    }
}
