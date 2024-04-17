package com.skrstop.framework.components.starter.annotation.constant;

/**
 * @author 蒋时华
 * @date 2023-12-04 10:16:08
 */
public enum PrivacyInfoType {

    /*** 默认处理：显示前1/3和后1/3，其他*号代替 */
    DEFAULT,

    /*** 置为null */
    SET_NULL,

    /*** 银行卡类型 */
    BANK_CARD,

    /*** 身份证号 */
    ID_CARD,

    /*** 电话号码 */
    PHONE,

    /*** 邮件 */
    EMAIL,

    PrivacyInfoType() {
    }

}
