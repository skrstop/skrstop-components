package cn.auntec.framework.components.core.common.response;

import cn.auntec.framework.components.core.common.response.abstracts.AbstractResult;
import cn.auntec.framework.components.core.common.response.common.CommonResultCode;
import cn.auntec.framework.components.core.common.response.core.IResult;
import cn.auntec.framework.components.core.common.response.core.ISetResult;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * response code with list data result entity
 *
 * @author 蒋时华
 * @date 2018/7/19
 */
@Getter
@Setter
public class SetResult<T> extends AbstractResult implements ISetResult<T> {

    private static final long serialVersionUID = 8864512560499081986L;
    private Set<T> data;

    public SetResult() {
        this.data = new LinkedHashSet<>();
    }

    public SetResult(IResult IResult) {
        super(IResult);
    }

    public SetResult(ISetResult<T> iSetResult) {
        super(iSetResult);
        this.data = iSetResult.getData();
    }

    public SetResult(IResult IResult, Set<T> data) {
        super(IResult);
        this.data = data;
    }

    public static class Builder {

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static SetResult success() {
            return new SetResult(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，携带数据
         *
         * @param data
         * @return Result<T>
         */
        public static <T> SetResult<T> success(Set<T> data) {
            return new SetResult<T>(CommonResultCode.SUCCESS, data);
        }

        /**
         * 默认请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static SetResult error() {
            return new SetResult(CommonResultCode.FAIL);
        }

        /**
         * 默认请求失败返回值, 携带数据
         *
         * @return Result
         */
        public static <T> SetResult<T> error(Set<T> data) {
            return new SetResult<T>(CommonResultCode.FAIL, data);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> SetResult<T> result(ISetResult<T> iListResult) {
            return new SetResult(iListResult);
        }

        /**
         * 请求返回值, 携带数据
         *
         * @return Result
         */
        public static <T> SetResult<T> result(IResult IResult, Set<T> data) {
            return new SetResult<T>(IResult, data);
        }

        /**
         * 请求返回值, 携带数据
         *
         * @return Result
         */
        public static <T> SetResult<T> result(IResult IResult) {
            return new SetResult<T>(IResult);
        }

    }

}
