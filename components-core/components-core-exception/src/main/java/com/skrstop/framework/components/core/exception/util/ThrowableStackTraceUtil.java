package com.skrstop.framework.components.core.exception.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 获得异常栈信息
 * Created on 2018/11/06
 *
 * @author 蒋时华
 */
@Slf4j
@UtilityClass
public class ThrowableStackTraceUtil {

    public static String getStackTraceStr(Throwable e) {
        try (StringWriter sw = new StringWriter();
             PrintWriter printWriter = new PrintWriter(sw, true)) {
            e.printStackTrace(printWriter);
            return sw.toString();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }
}
