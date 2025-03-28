package com.skrstop.framework.components.util.serialization.format.fastjson;

import cn.hutool.core.date.TemporalAccessorUtil;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import lombok.AllArgsConstructor;

import java.lang.reflect.Type;
import java.time.LocalTime;

/**
 * @author 蒋时华
 * @date 2025-03-28 10:00:12
 * @since 1.0.0
 */
@AllArgsConstructor
public class LocalTimeObjectWriter implements ObjectWriter<LocalTime> {

    private String format;

    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
        jsonWriter.writeString(TemporalAccessorUtil.format((LocalTime) object, format));
    }
}
