package com.jphoebe.framework.components.util.extra.pinyin;

import cn.hutool.extra.pinyin.PinyinEngine;
import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum;
import com.github.houbb.pinyin.util.PinyinHelper;

/**
 * @author 蒋时华
 * @date 2020-10-23 22:34:08
 */
public class DoubbPinyinEngine implements PinyinEngine {
    @Override
    public String getPinyin(char c) {
        return PinyinHelper.toPinyin(String.valueOf(c));
    }

    @Override
    public String getPinyin(String str, String separator) {
        return PinyinHelper.toPinyin(str, PinyinStyleEnum.DEFAULT, separator);
    }

}
