package com.skrstop.framework.components.core.common.response;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractPageResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IDataPageHashSetResult;
import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.SimplePageData;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
public class PageHashSetResult<T> extends AbstractPageResult<HashSet<T>, SimplePageData<HashSet<T>>> implements IDataPageHashSetResult<T, SimplePageData<HashSet<T>>> {

    public PageHashSetResult() {
    }

    public PageHashSetResult(IResult iResult) {
        super(iResult);
    }

    public PageHashSetResult(IResult iResult, SimplePageData<HashSet<T>> pageData) {
        super(iResult, pageData);
    }

    public PageHashSetResult(IPageResult<HashSet<T>, SimplePageData<HashSet<T>>> iPageResult) {
        super(iPageResult);
    }

    public PageHashSetResult(IPageResult<HashSet<T>, SimplePageData<HashSet<T>>> iPageResult, HashSet<T> list) {
        super(iPageResult, list);
    }

    public PageHashSetResult(IResult iResult, SimplePageData<HashSet<T>> pageData, HashSet<T> list) {
        this(iResult, pageData);
        if (this.data != null) {
            this.data.setRows(list);
        }
    }

    public PageHashSetResult(IResult iResult, long pageNumber, long pageSize) {
        this(iResult, new SimplePageData<HashSet<T>>(pageNumber, pageSize));
    }

    public PageHashSetResult(IResult iResult, long pageNumber, long pageSize, HashSet<T> list) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize));
        this.data.setPageNumber(pageNumber);
        this.data.setPageSize(pageSize);
        this.data.setRows(list);
    }

    public PageHashSetResult(IResult iResult, long pageNumber, long pageSize, long total) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize, total));
        this.data.setPageNumber(pageNumber);
        this.data.setPageSize(pageSize);
        this.data.setTotal(total);
    }

    public PageHashSetResult(IResult iResult, long pageNumber, long pageSize, long total, HashSet<T> list) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize, total));
        this.data.setPageNumber(pageNumber);
        this.data.setPageSize(pageSize);
        this.data.setTotal(total);
        this.data.setRows(list);
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
        public static PageHashSetResult success(long pageNumber, long pageSize) {
            return new PageHashSetResult(CommonResultCode.SUCCESS, pageNumber, pageSize);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> success(long pageNumber, long pageSize, HashSet<T> set) {
            return new PageHashSetResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, set);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> success(long pageNumber, long pageSize, long total) {
            return new PageHashSetResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, total);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> success(long pageNumber, long pageSize, long total, HashSet<T> set) {
            return new PageHashSetResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, total, set);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> success(SimplePageData<HashSet<T>> pageData) {
            return new PageHashSetResult(CommonResultCode.SUCCESS, pageData);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> success(SimplePageData<HashSet<T>> pageData, HashSet<T> set) {
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
        public static PageHashSetResult error(long pageNumber, long pageSize) {
            return new PageHashSetResult(CommonResultCode.FAIL, pageNumber, pageSize);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> error(long pageNumber, long pageSize, HashSet<T> set) {
            return new PageHashSetResult<T>(CommonResultCode.FAIL, pageNumber, pageSize, set);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageHashSetResult error(long pageNumber, long pageSize, long total) {
            return new PageHashSetResult(CommonResultCode.FAIL, pageNumber, pageSize, total);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> error(long pageNumber, long pageSize, long total, HashSet<T> set) {
            return new PageHashSetResult<T>(CommonResultCode.FAIL, pageNumber, pageSize, total, set);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult error(SimplePageData<HashSet<T>> pageData) {
            return new PageHashSetResult(CommonResultCode.FAIL, pageData);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> error(SimplePageData<HashSet<T>> pageData, HashSet<T> set) {
            return new PageHashSetResult<T>(CommonResultCode.FAIL, pageData, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult result(IPageResult<HashSet<T>, SimplePageData<HashSet<T>>> iPageResult) {
            return new PageHashSetResult(iPageResult);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> result(IPageResult<HashSet<T>, SimplePageData<HashSet<T>>> iPageResult, HashSet<T> set) {
            return new PageHashSetResult<T>(iPageResult, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> result(IResult iResult, long pageNumber, long pageSize, HashSet<T> set) {
            return new PageHashSetResult<T>(iResult, pageNumber, pageSize, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageHashSetResult result(IResult iResult, long pageNumber, long pageSize, long total) {
            return new PageHashSetResult(iResult, pageNumber, pageSize, total);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> result(IResult iResult, long pageNumber, long pageSize, long total, HashSet<T> set) {
            return new PageHashSetResult<T>(iResult, pageNumber, pageSize, total, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult result(IResult iResult, SimplePageData<HashSet<T>> pageData) {
            return new PageHashSetResult(iResult, pageData);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageHashSetResult<T> result(IResult iResult, SimplePageData<HashSet<T>> pageData, HashSet<T> set) {
            return new PageHashSetResult<T>(iResult, pageData, set);
        }

    }

}
