package com.skrstop.framework.components.util.value.validate;

import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.NumberUtil;
import com.skrstop.framework.components.util.value.data.PatternValidUtil;
import com.skrstop.framework.components.util.value.data.StrUtil;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 蒋时华
 * @date 2021-01-11 20:38:56
 */
@UtilityClass
public class ErrorMessageUtil {


    private final static String VALIDATE_SPLIT_CHAR = "##";

    /**
     * 按顺序获取序列消息第一个
     *
     * @param errors
     * @return
     */
    public static List<String> getErrorMessage(List<String> errors) {
        if (CollectionUtil.isEmpty(errors)) {
            return new ArrayList<>();
        }
        return errors.stream().sorted().sorted((o1, o2) -> {
            if (StrUtil.isBlank(o1) && StrUtil.isBlank(o2)) {
                return 0;
            } else if (StrUtil.isBlank(o1)) {
                return 1;
            } else if (StrUtil.isBlank(o2)) {
                return -1;
            }
            int o1Index = o1.indexOf(ErrorMessageUtil.VALIDATE_SPLIT_CHAR);
            int o2Index = o2.indexOf(ErrorMessageUtil.VALIDATE_SPLIT_CHAR);
            if (o1Index == -1 && o2Index == -1) {
                return 0;
            } else if (o1Index == -1) {
                return 1;
            } else if (o2Index == -1) {
                return -1;
            }
            Long o1Num = NumberUtil.toLong(o1.substring(0, o1Index), Long.MAX_VALUE);
            Long o2Num = NumberUtil.toLong(o2.substring(0, o2Index), Long.MAX_VALUE);
            return NumberUtil.compare(o1Num, o2Num);
        }).collect(Collectors.toList());
    }

    /**
     * 按顺序获取序列消息第一个
     *
     * @param errors
     * @return
     */
    public static String getFirstErrorMessage(List<String> errors) {
        List<String> errorMessage = getErrorMessage(errors);
        if (CollectionUtil.isEmpty(errorMessage)) {
            return null;
        }
        String str = errorMessage.get(0);
        if (!PatternValidUtil.isMatch("^[0-9]+##.*", str)) {
            return str;
        }
        int indexOf = str.indexOf(ErrorMessageUtil.VALIDATE_SPLIT_CHAR);
        if (indexOf == -1) {
            return str;
        }
        return str.substring(indexOf + 2);
    }

}
