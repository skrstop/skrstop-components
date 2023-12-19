package com.skrstop.framework.components.core.common.response;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IHashSetResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.core.ISetResult;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

/**
 * response code with list data result entity
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
@Getter
@Setter
public class HashSetResult<T> extends AbstractResult implements IHashSetResult<T> {

    private static final long serialVersionUID = 8864512560499081986L;
    private HashSet<T> data;

    public HashSetResult() {
        this.data = new HashSet<>();
    }

    public HashSetResult(IResult IResult) {
        super(IResult);
    }

    public HashSetResult(IHashSetResult<T> iHashSetResult) {
        super(iHashSetResult);
        this.data = iHashSetResult.getData();
    }

    public HashSetResult(IResult IResult, HashSet<T> data) {
        super(IResult);
        this.data = data;
    }

    public static class Builder {

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static HashSetResult success() {
            return new HashSetResult(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，携带数据
         *
         * @param data
         * @return Result<T>
         */
        public static <T> HashSetResult<T> success(HashSet<T> data) {
            return new HashSetResult<T>(CommonResultCode.SUCCESS, data);
        }

        /**
         * 默认请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static HashSetResult error() {
            return new HashSetResult(CommonResultCode.FAIL);
        }

        /**
         * 默认请求失败返回值, 携带数据
         *
         * @return Result
         */
        public static <T> HashSetResult<T> error(HashSet<T> data) {
            return new HashSetResult<T>(CommonResultCode.FAIL, data);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> HashSetResult<T> result(ISetResult<T> iListResult) {
            return new HashSetResult(iListResult);
        }

        /**
         * 请求返回值, 携带数据
         *
         * @return Result
         */
        public static <T> HashSetResult<T> result(IResult IResult, HashSet<T> data) {
            return new HashSetResult<T>(IResult, data);
        }

        /**
         * 请求返回值, 携带数据
         *
         * @return Result
         */
        public static <T> HashSetResult<T> result(IResult IResult) {
            return new HashSetResult<T>(IResult);
        }

    }

}
