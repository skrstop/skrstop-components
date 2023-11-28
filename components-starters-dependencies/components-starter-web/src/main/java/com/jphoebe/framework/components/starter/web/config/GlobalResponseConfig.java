package com.jphoebe.framework.components.starter.web.config;

import com.jphoebe.framework.components.starter.web.constant.GlobalConfigConst;
import com.jphoebe.framework.components.starter.web.constant.RequestPathConst;
import com.jphoebe.framework.components.util.constant.DateFormatConst;
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
public class GlobalResponseConfig {

    /*** 是否开启统一返回值 */
    private Boolean enable;

    /*** 是否显示null字段节点 */
    private Boolean showNullValue;

    /*** 使用feign, 是否统一处理 */
    private Boolean supportFeign;

    /*** long 类型转换成string */
    private Boolean longToString;

    /*** 时间格式化 */
    private String dateTimeFormat;

    /*** 根据返回值类型自动转换Result */
    private Boolean transResultResponse;

    /*** 不自动包装Result的接口列表 */
    private List<String> notTransResultList;

    public GlobalResponseConfig() {
        this.enable = true;
        this.showNullValue = false;
        this.supportFeign = true;
        this.longToString = true;
        this.dateTimeFormat = DateFormatConst.NORM_DATETIME_PATTERN;
        this.transResultResponse = true;
        /*** 默认添加：不转换请求结果类型的路径 */
        this.notTransResultList = new ArrayList<>(RequestPathConst.DEFAULT_NOT_TRANS_RESULT_LIST);
    }

}
