package com.zoe.framework.components.core.common.response;

import com.zoe.framework.components.core.common.response.abstracts.AbstractResult;
import com.zoe.framework.components.core.common.response.common.CommonResultCode;
import com.zoe.framework.components.core.common.response.core.IDataResult;
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
public class Result<T> extends AbstractResult implements IDataResult<T> {

    private static final long serialVersionUID = 4192152776182936563L;
    private T data;

    public Result() {

    }

    public Result(IResult IResult) {
        super(IResult);
    }

    public Result(IDataResult<T> IDataResult) {
        super(IDataResult);
        this.data = IDataResult.getData();
    }

    public Result(IResult IResult, T data) {
        super(IResult);
        this.data = data;
    }

    public static class Builder {

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static Result success() {
            return new Result(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，携带数据
         *
         * @param data
         * @return Result<T>
         */
        public static <T> Result<T> success(T data) {
            return new Result<T>(CommonResultCode.SUCCESS, data);
        }

        /**
         * 默认请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static Result error() {
            return new Result(CommonResultCode.FAIL);
        }

        /**
         * 默认请求失败返回值, 携带数据
         *
         * @return Result
         */
        public static <T> Result<T> error(T data) {
            return new Result<T>(CommonResultCode.FAIL, data);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> Result<T> result(IDataResult<T> IDataResult) {
            return new Result(IDataResult);
        }

        /**
         * 请求返回值, 携带数据
         *
         * @return Result
         */
        public static <T> Result<T> result(IResult IResult, T data) {
            return new Result<T>(IResult, data);
        }

        /**
         * 请求返回值, 携带数据
         *
         * @return Result
         */
        public static <T> Result<T> result(IResult IResult) {
            return new Result<T>(IResult);
        }

    }

}
