package com.skrstop.framework.components.core.common.response;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractPageResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IDataPageSetResult;
import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.PageData;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
public class PageSetResult<T> extends AbstractPageResult<Set<T>> implements IDataPageSetResult<T> {

    public PageSetResult() {
    }

    public PageSetResult(IResult IResult) {
        super(IResult);
    }

    public PageSetResult(IResult IResult, PageData pageData) {
        super(IResult, pageData);
    }

    public PageSetResult(IPageResult IPageResult) {
        super(IPageResult);
    }

    public PageSetResult(IPageResult IPageResult, Set<T> list) {
        super(IPageResult, list);
    }

    public PageSetResult(IResult IResult, PageData pageData, Set<T> list) {
        super(IResult, pageData, list);
    }

    public PageSetResult(IResult IResult, long pageNum, long pageSize) {
        super(IResult, pageNum, pageSize);
    }

    public PageSetResult(IResult IResult, long pageNum, long pageSize, Set<T> list) {
        super(IResult, pageNum, pageSize, list);
    }

    public PageSetResult(IResult IResult, long pageNum, long pageSize, long total) {
        super(IResult, pageNum, pageSize, total);
    }

    public PageSetResult(IResult IResult, long pageNum, long pageSize, long total, Set<T> list) {
        super(IResult, pageNum, pageSize, total, list);
    }

    public static class Builder {

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static PageSetResult success() {
            return new PageSetResult(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static PageSetResult success(long pageNum, long pageSize) {
            return new PageSetResult(CommonResultCode.SUCCESS, pageNum, pageSize);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> success(long pageNum, long pageSize, Set<T> set) {
            return new PageSetResult<T>(CommonResultCode.SUCCESS, pageNum, pageSize, set);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> success(long pageNum, long pageSize, long total) {
            return new PageSetResult<T>(CommonResultCode.SUCCESS, pageNum, pageSize, total);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> success(long pageNum, long pageSize, long total, Set<T> set) {
            return new PageSetResult<T>(CommonResultCode.SUCCESS, pageNum, pageSize, total, set);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static PageSetResult success(PageData pageData) {
            return new PageSetResult(CommonResultCode.SUCCESS, pageData);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> success(PageData pageData, Set<T> set) {
            return new PageSetResult<T>(CommonResultCode.SUCCESS, pageData, set);
        }

        /**
         * 默认请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static PageSetResult error() {
            return new PageSetResult(CommonResultCode.FAIL);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageSetResult error(long pageNum, long pageSize) {
            return new PageSetResult(CommonResultCode.FAIL, pageNum, pageSize);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> error(long pageNum, long pageSize, Set<T> set) {
            return new PageSetResult<T>(CommonResultCode.FAIL, pageNum, pageSize, set);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageSetResult error(long pageNum, long pageSize, long total) {
            return new PageSetResult(CommonResultCode.FAIL, pageNum, pageSize, total);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> error(long pageNum, long pageSize, long total, Set<T> set) {
            return new PageSetResult<T>(CommonResultCode.FAIL, pageNum, pageSize, total, set);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageSetResult error(PageData pageData) {
            return new PageSetResult(CommonResultCode.FAIL, pageData);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> error(PageData pageData, Set<T> set) {
            return new PageSetResult<T>(CommonResultCode.FAIL, pageData, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageSetResult result(IPageResult IPageResult) {
            return new PageSetResult(IPageResult);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> result(IPageResult IPageResult, Set<T> set) {
            return new PageSetResult<T>(IPageResult, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> result(IResult IResult, long pageNum, long pageSize, Set<T> set) {
            return new PageSetResult<T>(IResult, pageNum, pageSize, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageSetResult result(IResult IResult, long pageNum, long pageSize, long total) {
            return new PageSetResult(IResult, pageNum, pageSize, total);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> result(IResult IResult, long pageNum, long pageSize, long total, Set<T> set) {
            return new PageSetResult<T>(IResult, pageNum, pageSize, total, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageSetResult result(IResult IResult, PageData pageData) {
            return new PageSetResult(IResult, pageData);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> result(IResult IResult, PageData pageData, Set<T> set) {
            return new PageSetResult<T>(IResult, pageData, set);
        }

    }

}
