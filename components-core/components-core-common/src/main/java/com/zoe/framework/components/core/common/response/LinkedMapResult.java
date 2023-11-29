package com.zoe.framework.components.core.common.response;

import com.zoe.framework.components.core.common.response.abstracts.AbstractResult;
import com.zoe.framework.components.core.common.response.common.CommonResultCode;
import com.zoe.framework.components.core.common.response.core.ILinkedMapResult;
import com.zoe.framework.components.core.common.response.core.IResult;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

/**
 * response code with list data result entity
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
@Getter
@Setter
public class LinkedMapResult<T, R> extends AbstractResult implements ILinkedMapResult<T, R> {

    private static final long serialVersionUID = 8864512560499081986L;
    private LinkedHashMap<T, R> data;

    public LinkedMapResult() {
        this.data = new LinkedHashMap<T, R>();
    }

    public LinkedMapResult(IResult IResult) {
        super(IResult);
    }

    public LinkedMapResult(ILinkedMapResult<T, R> iMapResult) {
        super(iMapResult);
        this.data = iMapResult.getData();
    }

    public LinkedMapResult(IResult IResult, LinkedHashMap<T, R> data) {
        super(IResult);
        this.data = data;
    }

    public static class Builder {

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static LinkedMapResult success() {
            return new LinkedMapResult(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，携带数据
         *
         * @param data
         * @return Result<T>
         */
        public static <T, R> LinkedMapResult<T, R> success(LinkedHashMap<T, R> data) {
            return new LinkedMapResult<T, R>(CommonResultCode.SUCCESS, data);
        }

        /**
         * 默认请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static LinkedMapResult error() {
            return new LinkedMapResult(CommonResultCode.FAIL);
        }

        /**
         * 默认请求失败返回值, 携带数据
         *
         * @return Result
         */
        public static <T, R> LinkedMapResult<T, R> error(LinkedHashMap<T, R> data) {
            return new LinkedMapResult<T, R>(CommonResultCode.FAIL, data);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T, R> LinkedMapResult<T, R> result(ILinkedMapResult<T, R> iListResult) {
            return new LinkedMapResult(iListResult);
        }

        /**
         * 请求返回值, 携带数据
         *
         * @return Result
         */
        public static <T, R> LinkedMapResult<T, R> result(IResult IResult, LinkedHashMap<T, R> data) {
            return new LinkedMapResult<T, R>(IResult, data);
        }

        /**
         * 请求返回值, 携带数据
         *
         * @return Result
         */
        public static <T, R> LinkedMapResult<T, R> result(IResult IResult) {
            return new LinkedMapResult<T, R>(IResult);
        }

    }

}
