package cn.auntec.framework.components.starter.database.utils;

import cn.auntec.framework.components.util.constant.StringPoolConst;
import cn.auntec.framework.components.util.value.data.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: wangzh
 * @date: 2021/6/11 17:35
 * @description: desc...
 */
public class SuperParamsUtil {

    private SuperParamsUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取定义参数和mbp生成参数规则对应关系
     *
     * @param sqlSet
     * @return
     */
    public static Map<String, String> getParamMap(String sqlSet) {
        Map<String, String> paramsMap = new HashMap<>();
        StrUtil.splitTrim(sqlSet, StringPool.COMMA)
                .forEach(item -> {
                    String[] split = item.split(StringPool.EQUALS);
                    String selfParams = split[0];
                    String replaceValue = split[1].replace("#{", StringPoolConst.EMPTY).replace("}", StringPoolConst.EMPTY);
                    List<String> mbpParamsList = StrUtil.splitTrim(replaceValue, StringPool.DOT);
                    String mbpParams = mbpParamsList.get(mbpParamsList.size() - 1);
                    paramsMap.put(selfParams, mbpParams);
                });
        return paramsMap;
    }

}
