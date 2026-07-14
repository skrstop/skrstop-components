package com.skrstop.framework.components.core.common.response;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractPageResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IDataPageLinkedSetResult;
import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.SimplePageData;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
public class PageLinkedSetResult<T> extends AbstractPageResult<LinkedHashSet<T>, SimplePageData<LinkedHashSet<T>>> implements IDataPageLinkedSetResult<T, SimplePageData<LinkedHashSet<T>>> {

    public PageLinkedSetResult() {
    }

    public PageLinkedSetResult(IResult iResult) {
        super(iResult);
    }

    public PageLinkedSetResult(IResult iResult, SimplePageData<LinkedHashSet<T>> pageData) {
        super(iResult, pageData);
    }

    public PageLinkedSetResult(IPageResult<LinkedHashSet<T>, SimplePageData<LinkedHashSet<T>>> iPageResult) {
        super(iPageResult);
    }

    public PageLinkedSetResult(IPageResult<LinkedHashSet<T>, SimplePageData<LinkedHashSet<T>>> iPageResult, LinkedHashSet<T> list) {
        super(iPageResult, list);
    }

    public PageLinkedSetResult(IResult iResult, SimplePageData<LinkedHashSet<T>> pageData, LinkedHashSet<T> list) {
        this(iResult, pageData);
        if (this.data != null) {
            this.data.setRows(list);
        }
    }

    public PageLinkedSetResult(IResult iResult, long pageNumber, long pageSize) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize));
    }

    public PageLinkedSetResult(IResult iResult, long pageNumber, long pageSize, LinkedHashSet<T> list) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize));
        this.data.setPageNumber(pageNumber);
        this.data.setPageSize(pageSize);
        this.data.setRows(list);
    }

    public PageLinkedSetResult(IResult iResult, long pageNumber, long pageSize, long total) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize, total));
        this.data.setPageNumber(pageNumber);
        this.data.setPageSize(pageSize);
        this.data.setTotal(total);
    }

    public PageLinkedSetResult(IResult iResult, long pageNumber, long pageSize, long total, LinkedHashSet<T> list) {
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
        public static PageLinkedSetResult success() {
            return new PageLinkedSetResult(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static PageLinkedSetResult success(long pageNumber, long pageSize) {
            return new PageLinkedSetResult(CommonResultCode.SUCCESS, pageNumber, pageSize);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> success(long pageNumber, long pageSize, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, set);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> success(long pageNumber, long pageSize, long total) {
            return new PageLinkedSetResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, total);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> success(long pageNumber, long pageSize, long total, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, total, set);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult success(SimplePageData<LinkedHashSet<T>> pageData) {
            return new PageLinkedSetResult(CommonResultCode.SUCCESS, pageData);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> success(SimplePageData<LinkedHashSet<T>> pageData, LinkedHashSet<T> set) {
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
        public static PageLinkedSetResult error(long pageNumber, long pageSize) {
            return new PageLinkedSetResult(CommonResultCode.FAIL, pageNumber, pageSize);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> error(long pageNumber, long pageSize, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(CommonResultCode.FAIL, pageNumber, pageSize, set);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageLinkedSetResult error(long pageNumber, long pageSize, long total) {
            return new PageLinkedSetResult(CommonResultCode.FAIL, pageNumber, pageSize, total);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> error(long pageNumber, long pageSize, long total, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(CommonResultCode.FAIL, pageNumber, pageSize, total, set);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult error(SimplePageData<LinkedHashSet<T>> pageData) {
            return new PageLinkedSetResult(CommonResultCode.FAIL, pageData);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> error(SimplePageData<LinkedHashSet<T>> pageData, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(CommonResultCode.FAIL, pageData, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult result(IPageResult<LinkedHashSet<T>, SimplePageData<LinkedHashSet<T>>> iPageResult) {
            return new PageLinkedSetResult(iPageResult);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> result(IPageResult<LinkedHashSet<T>, SimplePageData<LinkedHashSet<T>>> iPageResult, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(iPageResult, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> result(IResult iResult, long pageNumber, long pageSize, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(iResult, pageNumber, pageSize, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageLinkedSetResult result(IResult iResult, long pageNumber, long pageSize, long total) {
            return new PageLinkedSetResult(iResult, pageNumber, pageSize, total);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> result(IResult iResult, long pageNumber, long pageSize, long total, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(iResult, pageNumber, pageSize, total, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult result(IResult iResult, SimplePageData<LinkedHashSet<T>> pageData) {
            return new PageLinkedSetResult(iResult, pageData);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> result(IResult iResult, SimplePageData<LinkedHashSet<T>> pageData, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(iResult, pageData, set);
        }

    }

}
