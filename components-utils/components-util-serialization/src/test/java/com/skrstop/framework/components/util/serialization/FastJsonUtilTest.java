package com.skrstop.framework.components.util.serialization;

import com.skrstop.framework.components.core.common.response.DefaultResult;
import com.skrstop.framework.components.core.common.response.Result;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.util.serialization.json.FastJsonUtil;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author 蒋时华
 * @date 2023-11-30 14:00:53
 */
public class FastJsonUtilTest {

    @Test
    public void test() {
        //        CommonResultCode success = CommonResultCode.SUCCESS;
        IResult success = CommonResultCode.BUSY;
        Result<DefaultResult> result = Result.Builder.result(success, DefaultResult.Builder.result(success));
        String json = FastJsonUtil.toJson(result);
        System.out.println(json);

        Result<DefaultResult> defaultResultResult = FastJsonUtil.toBeanForResult(json, DefaultResult.class);
        System.out.println(defaultResultResult);

        final HashMap<String, Object> stringObjectHashMap = FastJsonUtil.toBeanForHashMap("");
        final HashMap<String, String> stringStringHashMap = FastJsonUtil.toBeanForHashMap("", String.class);
    }

}
