package com.skrstop.framework.components.core.common.response;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractPageResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IDataPageLinkedSetResult;
import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.PageData;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
public class PageLinkedSetResult<T> extends AbstractPageResult<LinkedHashSet<T>> implements IDataPageLinkedSetResult<T> {

    public PageLinkedSetResult() {
    }

    public PageLinkedSetResult(IResult IResult) {
        super(IResult);
    }

    public PageLinkedSetResult(IResult IResult, PageData pageData) {
        super(IResult, pageData);
    }

    public PageLinkedSetResult(IPageResult IPageResult) {
        super(IPageResult);
    }

    public PageLinkedSetResult(IPageResult IPageResult, LinkedHashSet<T> list) {
        super(IPageResult, list);
    }

    public PageLinkedSetResult(IResult IResult, PageData pageData, LinkedHashSet<T> list) {
        super(IResult, pageData, list);
    }

    public PageLinkedSetResult(IResult IResult, long pageNum, long pageSize) {
        super(IResult, pageNum, pageSize);
    }

    public PageLinkedSetResult(IResult IResult, long pageNum, long pageSize, LinkedHashSet<T> list) {
        super(IResult, pageNum, pageSize, list);
    }

    public PageLinkedSetResult(IResult IResult, long pageNum, long pageSize, long total) {
        super(IResult, pageNum, pageSize, total);
    }

    public PageLinkedSetResult(IResult IResult, long pageNum, long pageSize, long total, LinkedHashSet<T> list) {
        super(IResult, pageNum, pageSize, total, list);
    }

    public static class Builder {

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static PageLinkedSetResult success() {
            return new PageLinkedSetResult(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static PageLinkedSetResult success(long pageNum, long pageSize) {
            return new PageLinkedSetResult(CommonResultCode.SUCCESS, pageNum, pageSize);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> success(long pageNum, long pageSize, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(CommonResultCode.SUCCESS, pageNum, pageSize, set);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> success(long pageNum, long pageSize, long total) {
            return new PageLinkedSetResult<T>(CommonResultCode.SUCCESS, pageNum, pageSize, total);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> success(long pageNum, long pageSize, long total, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(CommonResultCode.SUCCESS, pageNum, pageSize, total, set);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static PageLinkedSetResult success(PageData pageData) {
            return new PageLinkedSetResult(CommonResultCode.SUCCESS, pageData);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> success(PageData pageData, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(CommonResultCode.SUCCESS, pageData, set);
        }

        /**
         * 默认请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static PageLinkedSetResult error() {
            return new PageLinkedSetResult(CommonResultCode.FAIL);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageLinkedSetResult error(long pageNum, long pageSize) {
            return new PageLinkedSetResult(CommonResultCode.FAIL, pageNum, pageSize);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> error(long pageNum, long pageSize, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(CommonResultCode.FAIL, pageNum, pageSize, set);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageLinkedSetResult error(long pageNum, long pageSize, long total) {
            return new PageLinkedSetResult(CommonResultCode.FAIL, pageNum, pageSize, total);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> error(long pageNum, long pageSize, long total, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(CommonResultCode.FAIL, pageNum, pageSize, total, set);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageLinkedSetResult error(PageData pageData) {
            return new PageLinkedSetResult(CommonResultCode.FAIL, pageData);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> error(PageData pageData, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(CommonResultCode.FAIL, pageData, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageLinkedSetResult result(IPageResult IPageResult) {
            return new PageLinkedSetResult(IPageResult);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> result(IPageResult IPageResult, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(IPageResult, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> result(IResult IResult, long pageNum, long pageSize, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(IResult, pageNum, pageSize, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageLinkedSetResult result(IResult IResult, long pageNum, long pageSize, long total) {
            return new PageLinkedSetResult(IResult, pageNum, pageSize, total);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> result(IResult IResult, long pageNum, long pageSize, long total, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(IResult, pageNum, pageSize, total, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageLinkedSetResult result(IResult IResult, PageData pageData) {
            return new PageLinkedSetResult(IResult, pageData);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> result(IResult IResult, PageData pageData, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(IResult, pageData, set);
        }

    }

}
