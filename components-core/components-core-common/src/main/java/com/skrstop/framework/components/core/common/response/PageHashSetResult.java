package com.skrstop.framework.components.core.common.response;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractPageResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IDataPageHashSetResult;
import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.PageData;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
public class PageHashSetResult<T> extends AbstractPageResult<HashSet<T>> implements IDataPageHashSetResult<T> {

    public PageHashSetResult() {
    }

    public PageHashSetResult(IResult IResult) {
        super(IResult);
    }

    public PageHashSetResult(IResult IResult, PageData<HashSet<T>> pageData) {
        super(IResult, pageData);
    }

    public PageHashSetResult(IPageResult<HashSet<T>> IPageResult) {
        super(IPageResult);
    }

    public PageHashSetResult(IPageResult<HashSet<T>> IPageResult, HashSet<T> list) {
        super(IPageResult, list);
    }

    public PageHashSetResult(IResult IResult, PageData<HashSet<T>> pageData, HashSet<T> list) {
        super(IResult, pageData, list);
    }

    public PageHashSetResult(IResult IResult, long pageNum, long pageSize) {
        super(IResult, pageNum, pageSize);
    }

    public PageHashSetResult(IResult IResult, long pageNum, long pageSize, HashSet<T> list) {
        super(IResult, pageNum, pageSize, list);
    }

    public PageHashSetResult(IResult IResult, long pageNum, long pageSize, long total) {
        super(IResult, pageNum, pageSize, total);
    }

    public PageHashSetResult(IResult IResult, long pageNum, long pageSize, long total, HashSet<T> list) {
        super(IResult, pageNum, pageSize, total, list);
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
        public static PageHashSetResult success(PageData pageData) {
            return new PageHashSetResult(CommonResultCode.SUCCESS, pageData);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> success(PageData pageData, HashSet<T> set) {
            return new PageHashSetResult<T>(CommonResultCode.SUCCESS, pageData, set);
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
        public static PageHashSetResult error(PageData pageData) {
            return new PageHashSetResult(CommonResultCode.FAIL, pageData);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> error(PageData pageData, HashSet<T> set) {
            return new PageHashSetResult<T>(CommonResultCode.FAIL, pageData, set);
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
        public static PageHashSetResult result(IResult IResult, PageData pageData) {
            return new PageHashSetResult(IResult, pageData);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> result(IResult IResult, PageData pageData, HashSet<T> set) {
            return new PageHashSetResult<T>(IResult, pageData, set);
        }

    }

}
