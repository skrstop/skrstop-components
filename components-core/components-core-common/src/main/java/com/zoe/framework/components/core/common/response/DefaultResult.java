package com.zoe.framework.components.core.common.response;

import com.zoe.framework.components.core.common.response.abstracts.AbstractResult;
import com.zoe.framework.components.core.common.response.common.CommonResultCode;
import com.zoe.framework.components.core.common.response.core.IResult;
import lombok.Getter;
import lombok.Setter;

/**
 * response code with data result entity
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
@Getter
@Setter
public class DefaultResult extends AbstractResult implements IResult {

    private static final long serialVersionUID = -6429956305851375791L;

    public DefaultResult() {

    }

    public DefaultResult(IResult IResult) {
        super(IResult);
    }

    public static class Builder {

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static DefaultResult success() {
            return new DefaultResult(CommonResultCode.SUCCESS);
        }

        /**
         * 默认请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static DefaultResult error() {
            return new DefaultResult(CommonResultCode.FAIL);
        }

        /**
         * 请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static DefaultResult result(IResult IResult) {
            return new DefaultResult(IResult);
        }


    }

}
