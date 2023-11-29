package com.zoe.framework.components.starter.redis.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2020-10-06 17:49:08
 */
public interface ValueProcessorConst {

    /*** 二进制 */
    byte BINARY = 1;

    /*** 字符串 */
    byte STRING = 2;

    /*** fastjson */
    byte FAST_JSON = 3;

    List<Byte> VALUES = new ArrayList() {{
        add(BINARY);
        add(STRING);
        add(FAST_JSON);
    }};

}
