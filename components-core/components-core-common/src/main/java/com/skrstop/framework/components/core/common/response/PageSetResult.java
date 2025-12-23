package com.skrstop.framework.components.core.common.response;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractPageResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IDataPageSetResult;
import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.SimplePageData;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
public class PageSetResult<T> extends AbstractPageResult<Set<T>, SimplePageData<Set<T>>> implements IDataPageSetResult<T, SimplePageData<Set<T>>> {

    public PageSetResult() {
    }

    public PageSetResult(IResult iResult) {
        super(iResult);
    }

    public PageSetResult(IResult iResult, SimplePageData<Set<T>> pageData) {
        super(iResult, pageData);
    }

    public PageSetResult(IPageResult<Set<T>, SimplePageData<Set<T>>> iPageResult) {
        super(iPageResult);
    }

    public PageSetResult(IPageResult<Set<T>, SimplePageData<Set<T>>> iPageResult, Set<T> list) {
        super(iPageResult, list);
    }

    public PageSetResult(IResult iResult, SimplePageData<Set<T>> pageData, Set<T> list) {
        this(iResult, pageData);
        if (this.data != null) {
            this.data.setRows(list);
        }
    }

    public PageSetResult(IResult iResult, long pageNumber, long pageSize) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize));
    }

    public PageSetResult(IResult iResult, long pageNumber, long pageSize, Set<T> list) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize));
        this.data.setPageNumber(pageNumber);
        this.data.setPageSize(pageSize);
        this.data.setRows(list);
    }

    public PageSetResult(IResult iResult, long pageNumber, long pageSize, long total) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize, total));
        this.data.setPageNumber(pageNumber);
        this.data.setPageSize(pageSize);
        this.data.setTotal(total);
    }

    public PageSetResult(IResult iResult, long pageNumber, long pageSize, long total, Set<T> list) {
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
        public static PageSetResult success() {
            return new PageSetResult(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static PageSetResult success(long pageNumber, long pageSize) {
            return new PageSetResult(CommonResultCode.SUCCESS, pageNumber, pageSize);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> success(long pageNumber, long pageSize, Set<T> set) {
            return new PageSetResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, set);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> success(long pageNumber, long pageSize, long total) {
            return new PageSetResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, total);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> success(long pageNumber, long pageSize, long total, Set<T> set) {
            return new PageSetResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, total, set);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult success(SimplePageData<Set<T>> pageData) {
            return new PageSetResult(CommonResultCode.SUCCESS, pageData);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> success(SimplePageData<Set<T>> pageData, Set<T> set) {
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
        public static PageSetResult error(long pageNumber, long pageSize) {
            return new PageSetResult(CommonResultCode.FAIL, pageNumber, pageSize);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> error(long pageNumber, long pageSize, Set<T> set) {
            return new PageSetResult<T>(CommonResultCode.FAIL, pageNumber, pageSize, set);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageSetResult error(long pageNumber, long pageSize, long total) {
            return new PageSetResult(CommonResultCode.FAIL, pageNumber, pageSize, total);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> error(long pageNumber, long pageSize, long total, Set<T> set) {
            return new PageSetResult<T>(CommonResultCode.FAIL, pageNumber, pageSize, total, set);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult error(SimplePageData<Set<T>> pageData) {
            return new PageSetResult(CommonResultCode.FAIL, pageData);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> error(SimplePageData<Set<T>> pageData, Set<T> set) {
            return new PageSetResult<T>(CommonResultCode.FAIL, pageData, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult result(IPageResult<Set<T>, SimplePageData<Set<T>>> iPageResult) {
            return new PageSetResult(iPageResult);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> result(IPageResult<Set<T>, SimplePageData<Set<T>>> iPageResult, Set<T> set) {
            return new PageSetResult<T>(iPageResult, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> result(IResult iResult, long pageNumber, long pageSize, Set<T> set) {
            return new PageSetResult<T>(iResult, pageNumber, pageSize, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageSetResult result(IResult iResult, long pageNumber, long pageSize, long total) {
            return new PageSetResult(iResult, pageNumber, pageSize, total);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> result(IResult iResult, long pageNumber, long pageSize, long total, Set<T> set) {
            return new PageSetResult<T>(iResult, pageNumber, pageSize, total, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult result(IResult iResult, SimplePageData<Set<T>> pageData) {
            return new PageSetResult(iResult, pageData);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> result(IResult iResult, SimplePageData<Set<T>> pageData, Set<T> set) {
            return new PageSetResult<T>(iResult, pageData, set);
        }

    }

}
