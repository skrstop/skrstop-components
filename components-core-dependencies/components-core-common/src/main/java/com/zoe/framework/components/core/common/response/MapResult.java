package com.zoe.framework.components.core.common.response;

import com.zoe.framework.components.core.common.response.abstracts.AbstractResult;
import com.zoe.framework.components.core.common.response.common.CommonResultCode;
import com.zoe.framework.components.core.common.response.core.IMapResult;
import com.zoe.framework.components.core.common.response.core.IResult;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * response code with list data result entity
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
@Getter
@Setter
public class MapResult<T, R> extends AbstractResult implements IMapResult<T, R> {

    private static final long serialVersionUID = 8864512560499081986L;
    private Map<T, R> data;

    public MapResult() {
        this.data = new HashMap<T, R>();
    }

    public MapResult(IResult IResult) {
        super(IResult);
    }

    public MapResult(IMapResult<T, R> iMapResult) {
        super(iMapResult);
        this.data = iMapResult.getData();
    }

    public MapResult(IResult IResult, Map<T, R> data) {
        super(IResult);
        this.data = data;
    }

    public static class Builder {

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static MapResult success() {
            return new MapResult(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，携带数据
         *
         * @param data
         * @return Result<T>
         */
        public static <T, R> MapResult<T, R> success(Map<T, R> data) {
            return new MapResult<T, R>(CommonResultCode.SUCCESS, data);
        }

        /**
         * 默认请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static MapResult error() {
            return new MapResult(CommonResultCode.FAIL);
        }

        /**
         * 默认请求失败返回值, 携带数据
         *
         * @return Result
         */
        public static <T, R> MapResult<T, R> error(Map<T, R> data) {
            return new MapResult<T, R>(CommonResultCode.FAIL, data);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T, R> MapResult<T, R> result(IMapResult<T, R> iListResult) {
            return new MapResult(iListResult);
        }

        /**
         * 请求返回值, 携带数据
         *
         * @return Result
         */
        public static <T, R> MapResult<T, R> result(IResult IResult, Map<T, R> data) {
            return new MapResult<T, R>(IResult, data);
        }

        /**
         * 请求返回值, 携带数据
         *
         * @return Result
         */
        public static <T, R> MapResult<T, R> result(IResult IResult) {
            return new MapResult<T, R>(IResult);
        }

    }

}
