package com.skrstop.framework.components.core.common.response;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IListResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * response code with list data result entity
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
@Getter
@Setter
public class ListResult<T> extends AbstractResult implements IListResult<T> {

    private static final long serialVersionUID = 8864512560499081986L;
    private List<T> data;

    public ListResult() {
        this.data = new ArrayList<>();
    }

    public ListResult(IResult IResult) {
        super(IResult);
    }

    public ListResult(IListResult<T> iListResult) {
        super(iListResult);
        this.data = iListResult.getData();
    }

    public ListResult(IResult IResult, List<T> data) {
        super(IResult);
        this.data = data;
    }

    public static class Builder {

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static ListResult success() {
            return new ListResult(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，携带数据
         *
         * @param data
         * @return Result<T>
         */
        public static <T> ListResult<T> success(List<T> data) {
            return new ListResult<T>(CommonResultCode.SUCCESS, data);
        }

        /**
         * 默认请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static ListResult error() {
            return new ListResult(CommonResultCode.FAIL);
        }

        /**
         * 默认请求失败返回值, 携带数据
         *
         * @return Result
         */
        public static <T> ListResult<T> error(List<T> data) {
            return new ListResult<T>(CommonResultCode.FAIL, data);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> ListResult<T> result(IListResult<T> iListResult) {
            return new ListResult(iListResult);
        }

        /**
         * 请求返回值, 携带数据
         *
         * @return Result
         */
        public static <T> ListResult<T> result(IResult IResult, List<T> data) {
            return new ListResult<T>(IResult, data);
        }

        /**
         * 请求返回值, 携带数据
         *
         * @return Result
         */
        public static <T> ListResult<T> result(IResult IResult) {
            return new ListResult<T>(IResult);
        }

    }

}
