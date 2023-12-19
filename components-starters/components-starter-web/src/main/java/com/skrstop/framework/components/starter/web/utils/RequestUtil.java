package com.skrstop.framework.components.starter.web.utils;

import com.skrstop.framework.components.core.exception.util.ThrowableStackTraceUtil;
import com.skrstop.framework.components.util.enums.CharSetEnum;
import com.skrstop.framework.components.util.value.data.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * 请求参数工具类
 *
 * @author 蒋时华
 */
@Slf4j
public class RequestUtil {

    public static String getRequestParam(HttpServletRequest request, String param) {
        Map<String, String> reqMap = getRequestParams(request);
        return reqMap.get(param);
    }

    public static String getRequestQueryPath(HttpServletRequest request) {
        if (ObjectUtil.isNull(request)) {
            return null;
        }
        return request.getQueryString();
    }

    public static String getRequestBody(HttpServletRequest request) {
        // 从输入流中取出body串, 如果为空，直接返回
        String reqBodyStr = null;
        try {
            reqBodyStr = IOUtils.toString(request.getInputStream(), CharSetEnum.UTF8.getCharSet());
        } catch (IOException e) {
            log.error(ThrowableStackTraceUtil.getStackTraceStr(e));
            return "获取请求body体内容失败";
        }
        return reqBodyStr;
    }

    public static Map<String, String> getRequestParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>(32);
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }

    public static String getRequestContent(Map<String, String> params) {
        if (params == null) {
            return null;
        } else {
            StringBuffer content = new StringBuffer();
            List<String> keys = new ArrayList(params.keySet());
            Collections.sort(keys);
            for (int i = 0; i < keys.size(); ++i) {
                String key = keys.get(i);
                String value = params.get(key);
                content.append((i == 0 ? "" : "&") + key + "=" + value);
            }
            return content.toString();
        }
    }

}
