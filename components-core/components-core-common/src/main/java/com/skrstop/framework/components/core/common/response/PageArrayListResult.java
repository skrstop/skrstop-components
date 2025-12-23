package com.skrstop.framework.components.core.common.response;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractPageResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IDataPageArrayListResult;
import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.SimplePageData;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
public class PageArrayListResult<T> extends AbstractPageResult<ArrayList<T>, SimplePageData<ArrayList<T>>> implements IDataPageArrayListResult<T, SimplePageData<ArrayList<T>>> {

    public PageArrayListResult() {
    }

    public PageArrayListResult(IResult iResult) {
        super(iResult);
    }

    public PageArrayListResult(IResult iResult, SimplePageData<ArrayList<T>> pageData) {
        super(iResult, pageData);
    }

    public PageArrayListResult(IPageResult<ArrayList<T>, SimplePageData<ArrayList<T>>> iPageResult) {
        super(iPageResult);
    }

    public PageArrayListResult(IPageResult<ArrayList<T>, SimplePageData<ArrayList<T>>> iPageResult, ArrayList<T> list) {
        super(iPageResult, list);
    }

    public PageArrayListResult(IResult iResult, SimplePageData<ArrayList<T>> pageData, ArrayList<T> list) {
        this(iResult, pageData);
        if (this.data != null) {
            this.data.setRows(list);
        }
    }

    public PageArrayListResult(IResult iResult, long pageNumber, long pageSize) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize));
    }

    public PageArrayListResult(IResult iResult, long pageNumber, long pageSize, ArrayList<T> list) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize));
        this.data.setPageNumber(pageNumber);
        this.data.setPageSize(pageSize);
        this.data.setRows(list);
    }

    public PageArrayListResult(IResult iResult, long pageNumber, long pageSize, long total) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize, total));
        this.data.setPageNumber(pageNumber);
        this.data.setPageSize(pageSize);
        this.data.setTotal(total);
    }

    public PageArrayListResult(IResult iResult, long pageNumber, long pageSize, long total, ArrayList<T> list) {
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
        public static PageArrayListResult success() {
            return new PageArrayListResult(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static PageArrayListResult success(long pageNumber, long pageSize) {
            return new PageArrayListResult(CommonResultCode.SUCCESS, pageNumber, pageSize);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageArrayListResult<T> success(long pageNumber, long pageSize, ArrayList<T> list) {
            return new PageArrayListResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, list);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageArrayListResult<T> success(long pageNumber, long pageSize, long total) {
            return new PageArrayListResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, total);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageArrayListResult<T> success(long pageNumber, long pageSize, long total, ArrayList<T> list) {
            return new PageArrayListResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, total, list);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageArrayListResult success(SimplePageData<ArrayList<T>> pageData) {
            return new PageArrayListResult(CommonResultCode.SUCCESS, pageData);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageArrayListResult<T> success(SimplePageData<ArrayList<T>> pageData, ArrayList<T> list) {
            return new PageArrayListResult<T>(CommonResultCode.SUCCESS, pageData, list);
        }

        /**
         * 默认请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static PageArrayListResult error() {
            return new PageArrayListResult(CommonResultCode.FAIL);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageArrayListResult error(long pageNumber, long pageSize) {
            return new PageArrayListResult(CommonResultCode.FAIL, pageNumber, pageSize);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageArrayListResult<T> error(long pageNumber, long pageSize, ArrayList<T> list) {
            return new PageArrayListResult<T>(CommonResultCode.FAIL, pageNumber, pageSize, list);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageArrayListResult error(long pageNumber, long pageSize, long total) {
            return new PageArrayListResult(CommonResultCode.FAIL, pageNumber, pageSize, total);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageArrayListResult<T> error(long pageNumber, long pageSize, long total, ArrayList<T> list) {
            return new PageArrayListResult<T>(CommonResultCode.FAIL, pageNumber, pageSize, total, list);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageArrayListResult error(SimplePageData<ArrayList<T>> pageData) {
            return new PageArrayListResult(CommonResultCode.FAIL, pageData);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageArrayListResult<T> error(SimplePageData<ArrayList<T>> pageData, ArrayList<T> list) {
            return new PageArrayListResult<T>(CommonResultCode.FAIL, pageData, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageArrayListResult result(IPageResult<ArrayList<T>, SimplePageData<ArrayList<T>>> iPageResult) {
            return new PageArrayListResult(iPageResult);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageArrayListResult<T> result(IPageResult<ArrayList<T>, SimplePageData<ArrayList<T>>> iPageResult, ArrayList<T> list) {
            return new PageArrayListResult<T>(iPageResult, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageArrayListResult<T> result(IResult iResult, long pageNumber, long pageSize, ArrayList<T> list) {
            return new PageArrayListResult<T>(iResult, pageNumber, pageSize, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageArrayListResult result(IResult iResult, long pageNumber, long pageSize, long total) {
            return new PageArrayListResult(iResult, pageNumber, pageSize, total);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageArrayListResult<T> result(IResult iResult, long pageNumber, long pageSize, long total, ArrayList<T> list) {
            return new PageArrayListResult<T>(iResult, pageNumber, pageSize, total, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageArrayListResult result(IResult iResult, SimplePageData<ArrayList<T>> pageData) {
            return new PageArrayListResult(iResult, pageData);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageArrayListResult<T> result(IResult iResult, SimplePageData<ArrayList<T>> pageData, ArrayList<T> list) {
            return new PageArrayListResult<T>(iResult, pageData, list);
        }

    }

}
