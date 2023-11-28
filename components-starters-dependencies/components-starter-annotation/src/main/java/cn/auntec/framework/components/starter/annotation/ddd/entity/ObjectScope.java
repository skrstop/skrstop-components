package cn.auntec.framework.components.starter.annotation.ddd.entity;

/**
 * 对象作用域
 *
 * @author 蒋时华
 * @date 2021-03-22 18:12:51
 */
public enum ObjectScope {

    /*** 普通 */
    NOMAL,
    /*** 请求 */
    REQUEST,
    /*** 相应 */
    RESPONSE,
    /*** 转换 */
    CONVERSION,
    /*** 临时 */
    TEMP,

    ;

}
