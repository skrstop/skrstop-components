package com.zoe.framework.components.core.common.response;

import com.zoe.framework.components.core.common.response.abstracts.AbstractResult;
import com.zoe.framework.components.core.common.response.common.CommonResultCode;
import com.zoe.framework.components.core.common.response.core.ILinkedSetResult;
import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.core.common.response.core.ISetResult;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;

/**
 * response code with list data result entity
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
@Getter
@Setter
public class LinkedSetResult<T> extends AbstractResult implements ILinkedSetResult<T> {

    private static final long serialVersionUID = 8864512560499081986L;
    private LinkedHashSet<T> data;

    public LinkedSetResult() {
        this.data = new LinkedHashSet<>();
    }

    public LinkedSetResult(IResult IResult) {
        super(IResult);
    }

    public LinkedSetResult(ILinkedSetResult<T> iLinkedSetResult) {
        super(iLinkedSetResult);
        this.data = iLinkedSetResult.getData();
    }

    public LinkedSetResult(IResult IResult, LinkedHashSet<T> data) {
        super(IResult);
        this.data = data;
    }

    public static class Builder {

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static LinkedSetResult success() {
            return new LinkedSetResult(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，携带数据
         *
         * @param data
         * @return Result<T>
         */
        public static <T> LinkedSetResult<T> success(LinkedHashSet<T> data) {
            return new LinkedSetResult<T>(CommonResultCode.SUCCESS, data);
        }

        /**
         * 默认请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static LinkedSetResult error() {
            return new LinkedSetResult(CommonResultCode.FAIL);
        }

        /**
         * 默认请求失败返回值, 携带数据
         *
         * @return Result
         */
        public static <T> LinkedSetResult<T> error(LinkedHashSet<T> data) {
            return new LinkedSetResult<T>(CommonResultCode.FAIL, data);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> LinkedSetResult<T> result(ISetResult<T> iListResult) {
            return new LinkedSetResult(iListResult);
        }

        /**
         * 请求返回值, 携带数据
         *
         * @return Result
         */
        public static <T> LinkedSetResult<T> result(IResult IResult, LinkedHashSet<T> data) {
            return new LinkedSetResult<T>(IResult, data);
        }

        /**
         * 请求返回值, 携带数据
         *
         * @return Result
         */
        public static <T> LinkedSetResult<T> result(IResult IResult) {
            return new LinkedSetResult<T>(IResult);
        }

    }

}
