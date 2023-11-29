package com.zoe.framework.components.util.stress.result;

import java.io.Writer;

/**
 * @author 蒋时华
 * @date 2018-02-15
 **/
public interface StressResultFormater {

    void format(StressResult result, Writer writer);
}
