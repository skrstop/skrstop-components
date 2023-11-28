package cn.auntec.framework.components.util.constant;

/**
 * @author 蒋时华
 * @date 2021-04-21 10:55:05
 */
public interface DataInputTypeConst {

    // 无数据类型
    byte NONE = -1;

    // 动态获取
    byte DYNAMIC = 0;

    // 固定值
    byte FIXED_VALUE = 1;
    // 整形数字
    byte INTEGER_VALUE = 2;
    // 浮点型数据
    byte FLOAT_VALUE = 3;
    // 文本
    byte TEXT_VALUE = 4;
    // 文件上传
    byte FILE_VALUE = 5;
    // list
    byte LIST_VALUE = 6;
    // obj
    byte OBJECT_VALUE = 7;

    // 固定可选值（单选）
    byte FIXED_SINGLE_OPTIONAL = 8;
    // 固定可选值（多选）
    byte FIXED_MULTI_OPTIONAL = 9;

    // 单日期时间
    byte SINGLE_DATE_TIME = 10;
    // 单日期
    byte SINGLE_DATE = 11;
    // 单时间
    byte SINGLE_TIME = 12;
    // 日期时间区间
    byte RANGE_DATE_TIME = 13;
    // 日期区间
    byte RANGE_DATE = 14;
    // 时间区间
    byte RANGE_TIME = 15;


}
