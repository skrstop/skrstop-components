package com.skrstop.framework.components.starter.web.configuration;

import com.skrstop.framework.components.starter.web.constant.GlobalConfigConst;
import com.skrstop.framework.components.starter.web.constant.RequestPathConst;
import com.skrstop.framework.components.util.constant.DateFormatConst;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * GlobalResponseConfig class
 *
 * @author 蒋时华
 * @date 2019/6/4
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(GlobalConfigConst.RESPONSE_PREFIX)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalResponseProperties {

    /*** 是否开启统一返回值 */
    private boolean enable = true;

    /*** 是否显示null字段节点 */
    private boolean showNullValue = false;

    /*** long 类型转换成string */
    private boolean longToString = true;

    /*** 时间格式化 */
    private String dateTimeFormat = DateFormatConst.NORM_DATETIME_PATTERN;
    private String dateFormat = DateFormatConst.NORM_DATE_PATTERN;
    private String timeFormat = DateFormatConst.NORM_TIME_PATTERN;
    private String yearFormat = DateFormatConst.NORM_YEAR_PATTERN;
    private String monthDayFormat = DateFormatConst.NORM_MONTH_DAY_PATTERN;
    private String yearMonthFormat = DateFormatConst.NORM_YEAR_MONTH_PATTERN;

    /*** 全局关闭IResult返回值自适应处理 */
    private boolean disableGlobalTransResultTypeResponse = false;

    /*** 不自动包装Result的接口列表 */
    private List<String> disableGlobalResponseList = new ArrayList<>(RequestPathConst.DEFAULT_NOT_GLOBAL_RESPONSE_RESULT_LIST);

    /*** 是否开启feign调用的自动去除包装结构，用户controller直接集成feignClient的情况，默认：false */
    private boolean enableFeignTransResultTypeResponse = false;

}
