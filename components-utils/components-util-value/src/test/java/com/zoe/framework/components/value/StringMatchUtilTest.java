package com.zoe.framework.components.value;

import com.zoe.framework.components.util.value.data.StringMatchUtil;
import org.junit.Test;

/**
 * @author 蒋时华
 * @date 2023-11-30 14:12:42
 */
public class StringMatchUtilTest {

    @Test
    public void test() {
        long current = System.nanoTime();
        String source = "有限公司;2346;HGJ";
        String target = "J";
        boolean res = StringMatchUtil.kmp(source, target);
        System.out.println("kmp结果:" + res + " 耗时:" + (System.nanoTime() - current));
        boolean res1 = StringMatchUtil.plain(source, target);
        System.out.println("朴素结果:" + res1 + " 耗时:" + (System.nanoTime() - current));
    }

}
