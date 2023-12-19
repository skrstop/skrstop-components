package com.skrstop.framework.components.starter.annotation.handle.trim;

import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.skrstop.framework.components.starter.annotation.anno.trim.Trim;
import com.skrstop.framework.components.util.constant.StringPoolConst;
import com.skrstop.framework.components.util.value.data.ObjectUtil;

/**
 * @author 蒋时华
 * @date 2021-02-24 17:00:58
 */
public class TrimParse {

    public static String parse(Trim annotation, String value) {
        if (ObjectUtil.isNull(annotation) || ObjectUtil.isNull(value)) {
            return value;
        }
        TrimmerType type = annotation.value();
        if (ObjectUtil.isNull(type)) {
            type = TrimmerType.LEFT_RIGHT;
        }
        switch (type) {
            case ALL:
                return value.replaceAll(StringPoolConst.SPACE, StringPoolConst.EMPTY);
            case LEFT_RIGHT:
                return value.trim();
            case LEFT:
                return value.replaceAll("^[　 ]+", StringPoolConst.EMPTY);
            case RIGHT:
                return value.replaceAll("[　 ]+$", StringPoolConst.EMPTY);
            default:
                throw new NotSupportedException("不支持该 Trim.TrimmerType");
        }
    }

}
