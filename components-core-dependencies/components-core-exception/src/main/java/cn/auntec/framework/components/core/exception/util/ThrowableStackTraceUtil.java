package cn.auntec.framework.components.core.exception.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 获得异常栈信息
 * Created on 2018/11/06
 *
 * @author 蒋时华
 */
@Slf4j
public class ThrowableStackTraceUtil {

    public static String getStackTraceStr(Throwable e) {
        StringWriter sw = null;
        PrintWriter printWriter = null;
        try {
            sw = new StringWriter();
            printWriter = new PrintWriter(sw, true);
            e.printStackTrace(printWriter);
            return sw.toString();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
            if (printWriter != null) {
                printWriter.close();
            }
        }

    }
}
