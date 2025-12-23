package com.skrstop.framework.components.core.common.response;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractPageResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IDataPageLinkedListResult;
import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.SimplePageData;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
public class PageLinkedListResult<T> extends AbstractPageResult<LinkedList<T>, SimplePageData<LinkedList<T>>> implements IDataPageLinkedListResult<T, SimplePageData<LinkedList<T>>> {

    public PageLinkedListResult() {
    }

    public PageLinkedListResult(IResult iResult) {
        super(iResult);
    }

    public PageLinkedListResult(IResult iResult, SimplePageData<LinkedList<T>> pageData) {
        super(iResult, pageData);
    }

    public PageLinkedListResult(IPageResult<LinkedList<T>, SimplePageData<LinkedList<T>>> iPageResult) {
        super(iPageResult);
    }

    public PageLinkedListResult(IPageResult<LinkedList<T>, SimplePageData<LinkedList<T>>> iPageResult, LinkedList<T> list) {
        super(iPageResult, list);
    }

    public PageLinkedListResult(IResult iResult, SimplePageData<LinkedList<T>> pageData, LinkedList<T> list) {
        this(iResult, pageData);
        if (this.data != null) {
            this.data.setRows(list);
        }
    }

    public PageLinkedListResult(IResult iResult, long pageNumber, long pageSize) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize));
    }

    public PageLinkedListResult(IResult iResult, long pageNumber, long pageSize, LinkedList<T> list) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize));
        this.data.setPageNumber(pageNumber);
        this.data.setPageSize(pageSize);
        this.data.setRows(list);
    }

    public PageLinkedListResult(IResult iResult, long pageNumber, long pageSize, long total) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize, total));
        this.data.setPageNumber(pageNumber);
        this.data.setPageSize(pageSize);
        this.data.setTotal(total);
    }

    public PageLinkedListResult(IResult iResult, long pageNumber, long pageSize, long total, LinkedList<T> list) {
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
        public static PageLinkedListResult success() {
            return new PageLinkedListResult(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static PageLinkedListResult success(long pageNumber, long pageSize) {
            return new PageLinkedListResult(CommonResultCode.SUCCESS, pageNumber, pageSize);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedListResult<T> success(long pageNumber, long pageSize, LinkedList<T> list) {
            return new PageLinkedListResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, list);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedListResult<T> success(long pageNumber, long pageSize, long total) {
            return new PageLinkedListResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, total);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedListResult<T> success(long pageNumber, long pageSize, long total, LinkedList<T> list) {
            return new PageLinkedListResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, total, list);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedListResult success(SimplePageData<LinkedList<T>> pageData) {
            return new PageLinkedListResult(CommonResultCode.SUCCESS, pageData);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedListResult<T> success(SimplePageData<LinkedList<T>> pageData, LinkedList<T> list) {
            return new PageLinkedListResult<T>(CommonResultCode.SUCCESS, pageData, list);
        }

        /**
         * 默认请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static PageLinkedListResult error() {
            return new PageLinkedListResult(CommonResultCode.FAIL);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageLinkedListResult error(long pageNumber, long pageSize) {
            return new PageLinkedListResult(CommonResultCode.FAIL, pageNumber, pageSize);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedListResult<T> error(long pageNumber, long pageSize, LinkedList<T> list) {
            return new PageLinkedListResult<T>(CommonResultCode.FAIL, pageNumber, pageSize, list);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageLinkedListResult error(long pageNumber, long pageSize, long total) {
            return new PageLinkedListResult(CommonResultCode.FAIL, pageNumber, pageSize, total);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedListResult<T> error(long pageNumber, long pageSize, long total, LinkedList<T> list) {
            return new PageLinkedListResult<T>(CommonResultCode.FAIL, pageNumber, pageSize, total, list);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedListResult error(SimplePageData<LinkedList<T>> pageData) {
            return new PageLinkedListResult(CommonResultCode.FAIL, pageData);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedListResult<T> error(SimplePageData<LinkedList<T>> pageData, LinkedList<T> list) {
            return new PageLinkedListResult<T>(CommonResultCode.FAIL, pageData, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedListResult result(IPageResult<LinkedList<T>, SimplePageData<LinkedList<T>>> iPageResult) {
            return new PageLinkedListResult(iPageResult);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedListResult<T> result(IPageResult<LinkedList<T>, SimplePageData<LinkedList<T>>> iPageResult, LinkedList<T> list) {
            return new PageLinkedListResult<T>(iPageResult, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedListResult<T> result(IResult iResult, long pageNumber, long pageSize, LinkedList<T> list) {
            return new PageLinkedListResult<T>(iResult, pageNumber, pageSize, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageLinkedListResult result(IResult iResult, long pageNumber, long pageSize, long total) {
            return new PageLinkedListResult(iResult, pageNumber, pageSize, total);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedListResult<T> result(IResult iResult, long pageNumber, long pageSize, long total, LinkedList<T> list) {
            return new PageLinkedListResult<T>(iResult, pageNumber, pageSize, total, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedListResult result(IResult iResult, SimplePageData<LinkedList<T>> pageData) {
            return new PageLinkedListResult(iResult, pageData);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedListResult<T> result(IResult iResult, SimplePageData<LinkedList<T>> pageData, LinkedList<T> list) {
            return new PageLinkedListResult<T>(iResult, pageData, list);
        }

    }

}
