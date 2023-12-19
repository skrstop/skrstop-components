package com.skrstop.framework.components.core.common.response;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.ICollectionResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * response code with list data result entity
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
@Getter
@Setter
public class CollectionResult<T> extends AbstractResult implements ICollectionResult<T> {

    private static final long serialVersionUID = 8864512560499081986L;
    private Collection<T> data;

    public CollectionResult() {
        this.data = new ArrayList<>();
    }

    public CollectionResult(IResult IResult) {
        super(IResult);
    }

    public CollectionResult(ICollectionResult<T> iCollectionResult) {
        super(iCollectionResult);
        this.data = iCollectionResult.getData();
    }

    public CollectionResult(IResult IResult, Collection<T> data) {
        super(IResult);
        this.data = data;
    }

    public static class Builder {

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static CollectionResult success() {
            return new CollectionResult(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，携带数据
         *
         * @param data
         * @return Result<T>
         */
        public static <T> CollectionResult<T> success(Collection<T> data) {
            return new CollectionResult<T>(CommonResultCode.SUCCESS, data);
        }

        /**
         * 默认请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static CollectionResult error() {
            return new CollectionResult(CommonResultCode.FAIL);
        }

        /**
         * 默认请求失败返回值, 携带数据
         *
         * @return Result
         */
        public static <T> CollectionResult<T> error(Collection<T> data) {
            return new CollectionResult<T>(CommonResultCode.FAIL, data);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> CollectionResult<T> result(ICollectionResult<T> iListResult) {
            return new CollectionResult(iListResult);
        }

        /**
         * 请求返回值, 携带数据
         *
         * @return Result
         */
        public static <T> CollectionResult<T> result(IResult IResult, Collection<T> data) {
            return new CollectionResult<T>(IResult, data);
        }

        /**
         * 请求返回值, 携带数据
         *
         * @return Result
         */
        public static <T> CollectionResult<T> result(IResult IResult) {
            return new CollectionResult<T>(IResult);
        }

    }

}
