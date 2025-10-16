package com.skrstop.framework.components.starter.web.configuration;

import com.skrstop.framework.components.starter.web.constant.GlobalConfigConst;
import com.skrstop.framework.components.starter.web.constant.RequestPathConst;
import com.skrstop.framework.components.util.constant.DateFormatConst;
import lombok.*;
import lombok.experimental.Accessors;
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

    /*** 使用feign, 是否统一处理 */
    private boolean supportFeign = true;

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

    /*** 默认错误码code */
    private DefaultExceptionCode defaultExceptionCode = new DefaultExceptionCode();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class DefaultExceptionCode {
        /**
         * 成功
         */
        private Object success;
        /**
         * 失败
         */
        private Object fail;
        /**
         * 无资源
         */
        private Object notFound;
        /**
         * 禁止访问
         */
        private Object forbidden;
        /**
         * 系统繁忙
         */
        private Object busy;
        /**
         * 参数类型错误
         */
        private Object matchParameter;
        /**
         * 不支持该媒体类型
         */
        private Object notSupportMediaType;
        /**
         * 不可接受该媒体类型
         */
        private Object notAcceptedMediaType;
        /**
         * 请求超时
         */
        private Object requestTimeout;
        /**
         * 不支持该请求方法
         */
        private Object requestMethodNotAllowed;
        /**
         * 缺少参数
         */
        private Object missParameter;
        /**
         * 文件上传错误
         */
        private Object fileUpload;
        /**
         * 未指定上传文件
         */
        private Object fileNotPoint;

        /**
         * not supported
         */
        private Object notSupported;
        /**
         * 解码异常
         */
        private Object decoding;
        /**
         * 编码异常
         */
        private Object encoding;
        /**
         * 反序列化异常
         */
        private Object deserialization;
        /**
         * 序列化异常
         */
        private Object serialization;
        /**
         * 非法参数异常
         */
        private Object illegalArgument;
        /**
         * 非法访问异常
         */
        private Object illegalAccess;
        /**
         * HTTP访问异常
         */
        private Object httpHandle;
        /**
         * RPC访问异常
         */
        private Object rpcHandle;
        /**
         * 参数异常
         */
        private Object parameter;
        /**
         * 空指针异常
         */
        private Object nullPointer;
        /**
         * 指针越界异常
         */
        private Object indexOutOfBounds;
        /**
         * io流异常
         */
        private Object ioStreaming;

        /**
         * 访问过于频繁，请稍后重试
         */
        private Object accessLimit;
        /**
         * 该接口已失效
         */
        private Object controllerDeprecated;
        /**
         * 内网访问限制
         */
        private Object intranetLimit;
        /**
         * 隐私信息，不可访问
         */
        private Object privacyInfo;
        /**
         * 未找到相关处理器
         */
        private Object noProcessor;
    }

}
