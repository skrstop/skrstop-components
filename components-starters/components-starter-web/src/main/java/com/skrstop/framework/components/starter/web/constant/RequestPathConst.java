package com.skrstop.framework.components.starter.web.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author: wangzh
 * @date: 2021/9/2 10:46
 * @description: desc.
 */
public interface RequestPathConst {

    /*** 监控相关 */
    String ACTUATOR_PATH_1 = "/actuator";
    String ACTUATOR_PATH_2 = "/actuator/**";
    String ACTUATOR_PATH_3 = "**/actuator";
    String ACTUATOR_PATH_4 = "**/actuator/**";

    String METRICS_PATH_1 = "metrics";
    String METRICS_PATH_2 = "/metrics/**";
    String METRICS_PATH_3 = "**/metrics";
    String METRICS_PATH_4 = "**/metrics/**";

    String DRUID_PATH_1 = "druid";
    String DRUID_PATH_2 = "/druid/**";
    String DRUID_PATH_3 = "**/druid";
    String DRUID_PATH_4 = "**/druid/**";

    List<String> DEFAULT_NOT_GLOBAL_RESPONSE_RESULT_LIST = Arrays.asList(
            RequestPathConst.ACTUATOR_PATH_1, RequestPathConst.ACTUATOR_PATH_2, RequestPathConst.ACTUATOR_PATH_3, RequestPathConst.ACTUATOR_PATH_4,
            RequestPathConst.METRICS_PATH_1, RequestPathConst.METRICS_PATH_2, RequestPathConst.METRICS_PATH_3, RequestPathConst.METRICS_PATH_4,
            RequestPathConst.DRUID_PATH_1, RequestPathConst.DRUID_PATH_2, RequestPathConst.DRUID_PATH_3, RequestPathConst.DRUID_PATH_4
    );


}
