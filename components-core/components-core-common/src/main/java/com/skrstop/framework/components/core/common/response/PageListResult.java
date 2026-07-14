package com.skrstop.framework.components.core.common.response;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractPageResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IDataPageListResult;
import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.SimplePageData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
public class PageListResult<T> extends AbstractPageResult<List<T>, SimplePageData<List<T>>> implements IDataPageListResult<T, SimplePageData<List<T>>> {

    public PageListResult() {
    }

    public PageListResult(IResult iResult) {
        super(iResult);
    }

    public PageListResult(IResult iResult, SimplePageData<List<T>> pageData) {
        super(iResult, pageData);
    }

    public PageListResult(IPageResult<List<T>, SimplePageData<List<T>>> iPageResult) {
        super(iPageResult);
    }

    public PageListResult(IPageResult<List<T>, SimplePageData<List<T>>> iPageResult, List<T> list) {
        super(iPageResult, list);
    }

    public PageListResult(IResult iResult, SimplePageData<List<T>> pageData, List<T> list) {
        this(iResult, pageData);
        if (this.data != null) {
            this.data.setRows(list);
        }
    }

    public PageListResult(IResult iResult, long pageNumber, long pageSize) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize));
    }

    public PageListResult(IResult iResult, long pageNumber, long pageSize, List<T> list) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize));
        this.data.setPageNumber(pageNumber);
        this.data.setPageSize(pageSize);
        this.data.setRows(list);
    }

    public PageListResult(IResult iResult, long pageNumber, long pageSize, long total) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize, total));
        this.data.setPageNumber(pageNumber);
        this.data.setPageSize(pageSize);
        this.data.setTotal(total);
    }

    public PageListResult(IResult iResult, long pageNumber, long pageSize, long total, List<T> list) {
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
        public static PageListResult success() {
            return new PageListResult(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static PageListResult success(long pageNumber, long pageSize) {
            return new PageListResult(CommonResultCode.SUCCESS, pageNumber, pageSize);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> success(long pageNumber, long pageSize, List<T> list) {
            return new PageListResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, list);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> success(long pageNumber, long pageSize, long total) {
            return new PageListResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, total);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> success(long pageNumber, long pageSize, long total, List<T> list) {
            return new PageListResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, total, list);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult success(SimplePageData<List<T>> pageData) {
            return new PageListResult(CommonResultCode.SUCCESS, pageData);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> success(SimplePageData<List<T>> pageData, List<T> list) {
            return new PageListResult<T>(CommonResultCode.SUCCESS, pageData, list);
        }

        /**
         * 默认请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static PageListResult error() {
            return new PageListResult(CommonResultCode.FAIL);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageListResult error(long pageNumber, long pageSize) {
            return new PageListResult(CommonResultCode.FAIL, pageNumber, pageSize);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> error(long pageNumber, long pageSize, List<T> list) {
            return new PageListResult<T>(CommonResultCode.FAIL, pageNumber, pageSize, list);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageListResult error(long pageNumber, long pageSize, long total) {
            return new PageListResult(CommonResultCode.FAIL, pageNumber, pageSize, total);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> error(long pageNumber, long pageSize, long total, List<T> list) {
            return new PageListResult<T>(CommonResultCode.FAIL, pageNumber, pageSize, total, list);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult error(SimplePageData<List<T>> pageData) {
            return new PageListResult(CommonResultCode.FAIL, pageData);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> error(SimplePageData<List<T>> pageData, List<T> list) {
            return new PageListResult<T>(CommonResultCode.FAIL, pageData, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult result(IPageResult<List<T>, SimplePageData<List<T>>> iPageResult) {
            return new PageListResult(iPageResult);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> result(IPageResult<List<T>, SimplePageData<List<T>>> iPageResult, List<T> list) {
            return new PageListResult<T>(iPageResult, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> result(IResult iResult, long pageNumber, long pageSize, List<T> list) {
            return new PageListResult<T>(iResult, pageNumber, pageSize, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageListResult result(IResult iResult, long pageNumber, long pageSize, long total) {
            return new PageListResult(iResult, pageNumber, pageSize, total);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> result(IResult iResult, long pageNumber, long pageSize, long total, List<T> list) {
            return new PageListResult<T>(iResult, pageNumber, pageSize, total, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult result(IResult iResult, SimplePageData<List<T>> pageData) {
            return new PageListResult(iResult, pageData);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> result(IResult iResult, SimplePageData<List<T>> pageData, List<T> list) {
            return new PageListResult<T>(iResult, pageData, list);
        }

    }

}
