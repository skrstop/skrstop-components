package com.skrstop.framework.components.util.serialization.format.fastjson;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import lombok.AllArgsConstructor;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * @author 蒋时华
 * @date 2025-03-28 09:59:57
 * @since 1.0.0
 */
@AllArgsConstructor
public class LocalDateTimeObjectWriter implements ObjectWriter<LocalDateTime> {

    private String format;

    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
        jsonWriter.writeString(LocalDateTimeUtil.format((LocalDateTime) object, format));
    }
}
