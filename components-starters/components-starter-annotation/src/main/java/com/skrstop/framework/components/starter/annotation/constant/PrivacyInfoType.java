package com.skrstop.framework.components.starter.annotation.constant;

/**
 * @author 蒋时华
 * @date 2023-12-04 10:16:08
 */
public interface PrivacyInfoType {

    /*** 默认处理：显示前1/3和后1/3，其他*号代替 */
    String DEFAULT = "DEFAULT";
    /*** 置为null */
    String SET_NULL = "SET_NULL";
    /*** 默认处理：银行卡类型 */
    String DEFAULT_BANK_CARD = "DEFAULT_BANK_CARD";
    /*** 默认处理：身份证号 */
    String DEFAULT_ID_CARD = "DEFAULT_ID_CARD";
    /*** 默认处理：电话号码 */
    String DEFAULT_PHONE = "DEFAULT_PHONE";
    /*** 默认处理：邮件 */
    String DEFAULT_EMAIL = "DEFAULT_EMAIL";

}
