package cn.auntec.framework.components.core.common.response;

import cn.auntec.framework.components.core.common.response.abstracts.AbstractPageResult;
import cn.auntec.framework.components.core.common.response.common.CommonResultCode;
import cn.auntec.framework.components.core.common.response.core.IDataPageHashSetResult;
import cn.auntec.framework.components.core.common.response.core.IPageResult;
import cn.auntec.framework.components.core.common.response.core.IResult;
import cn.auntec.framework.components.core.common.response.page.PageInfo;
import cn.auntec.framework.components.core.common.response.page.SimplePageInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
public class PageHashSetResult<T> extends AbstractPageResult implements IDataPageHashSetResult<T> {

    HashSet<T> data;

    public PageHashSetResult() {
        this.data = new HashSet<>();
    }

    public PageHashSetResult(IResult IResult) {
        super(IResult);
    }

    public PageHashSetResult(IPageResult IPageResult) {
        super(IPageResult);
    }

    public PageHashSetResult(IPageResult IPageResult, HashSet<T> set) {
        super(IPageResult);
        this.data = set;
    }

    public PageHashSetResult(IResult IResult, PageInfo pageInfo) {
        super(IResult, pageInfo);
    }

    public PageHashSetResult(IResult IResult, PageInfo pageInfo, HashSet<T> set) {
        super(IResult, pageInfo);
        this.data = set;
    }

    public PageHashSetResult(IResult IResult, long pageNum, long pageSize) {
        super(IResult, new SimplePageInfo(pageNum, pageSize));
    }

    public PageHashSetResult(IResult IResult, long pageNum, long pageSize, HashSet<T> set) {
        super(IResult, new SimplePageInfo(pageNum, pageSize));
        this.data = set;
    }

    public PageHashSetResult(IResult IResult, long pageNum, long pageSize, long total) {
        super(IResult, new SimplePageInfo(pageNum, pageSize, total));
    }

    public PageHashSetResult(IResult IResult, long pageNum, long pageSize, long total, HashSet<T> set) {
        super(IResult, new SimplePageInfo(pageNum, pageSize, total));
        this.data = set;
    }


    public static class Builder {

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static PageHashSetResult success() {
            return new PageHashSetResult(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static PageHashSetResult success(long pageNum, long pageSize) {
            return new PageHashSetResult(CommonResultCode.SUCCESS, pageNum, pageSize);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> success(long pageNum, long pageSize, HashSet<T> set) {
            return new PageHashSetResult<T>(CommonResultCode.SUCCESS, pageNum, pageSize, set);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> success(long pageNum, long pageSize, long total) {
            return new PageHashSetResult<T>(CommonResultCode.SUCCESS, pageNum, pageSize, total);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> success(long pageNum, long pageSize, long total, HashSet<T> set) {
            return new PageHashSetResult<T>(CommonResultCode.SUCCESS, pageNum, pageSize, total, set);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static PageHashSetResult success(PageInfo pageInfo) {
            return new PageHashSetResult(CommonResultCode.SUCCESS, pageInfo);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> success(PageInfo pageInfo, HashSet<T> set) {
            return new PageHashSetResult<T>(CommonResultCode.SUCCESS, pageInfo, set);
        }

        /**
         * 默认请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static PageHashSetResult error() {
            return new PageHashSetResult(CommonResultCode.FAIL);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageHashSetResult error(long pageNum, long pageSize) {
            return new PageHashSetResult(CommonResultCode.FAIL, pageNum, pageSize);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> error(long pageNum, long pageSize, HashSet<T> set) {
            return new PageHashSetResult<T>(CommonResultCode.FAIL, pageNum, pageSize, set);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageHashSetResult error(long pageNum, long pageSize, long total) {
            return new PageHashSetResult(CommonResultCode.FAIL, pageNum, pageSize, total);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> error(long pageNum, long pageSize, long total, HashSet<T> set) {
            return new PageHashSetResult<T>(CommonResultCode.FAIL, pageNum, pageSize, total, set);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageHashSetResult error(PageInfo pageInfo) {
            return new PageHashSetResult(CommonResultCode.FAIL, pageInfo);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> error(PageInfo pageInfo, HashSet<T> set) {
            return new PageHashSetResult<T>(CommonResultCode.FAIL, pageInfo, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageHashSetResult result(IPageResult IPageResult) {
            return new PageHashSetResult(IPageResult);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> result(IPageResult IPageResult, HashSet<T> set) {
            return new PageHashSetResult<T>(IPageResult, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> result(IResult IResult, long pageNum, long pageSize, HashSet<T> set) {
            return new PageHashSetResult<T>(IResult, pageNum, pageSize, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageHashSetResult result(IResult IResult, long pageNum, long pageSize, long total) {
            return new PageHashSetResult(IResult, pageNum, pageSize, total);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> result(IResult IResult, long pageNum, long pageSize, long total, HashSet<T> set) {
            return new PageHashSetResult<T>(IResult, pageNum, pageSize, total, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageHashSetResult result(IResult IResult, PageInfo pageInfo) {
            return new PageHashSetResult(IResult, pageInfo);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> result(IResult IResult, PageInfo pageInfo, HashSet<T> set) {
            return new PageHashSetResult<T>(IResult, pageInfo, set);
        }

    }

}
