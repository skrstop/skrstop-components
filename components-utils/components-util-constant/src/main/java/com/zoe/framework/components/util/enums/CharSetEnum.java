package com.zoe.framework.components.util.enums;

import lombok.Getter;

/**
 * 常用字符编码常量池
 *
 * @author 蒋时华
 * @date 2019/5/30
 */
public enum CharSetEnum {

    /*** 常用字符编码常量池 */
    UTF8("UTF-8"),
    GBK("GBK"),
    ISO_8859_1("iso-8859-1"),
    ;

    @Getter
    private String charSet;

    CharSetEnum(String charSet) {
        this.charSet = charSet;
    }

    @Override
    public String toString() {
        return this.getCharSet();
    }
}
