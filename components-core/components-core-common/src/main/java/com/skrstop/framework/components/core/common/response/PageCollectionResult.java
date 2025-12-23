package com.skrstop.framework.components.core.common.response;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractPageResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IDataPageCollectionResult;
import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.SimplePageData;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
public class PageCollectionResult<T> extends AbstractPageResult<Collection<T>, SimplePageData<Collection<T>>> implements IDataPageCollectionResult<T, SimplePageData<Collection<T>>> {

    public PageCollectionResult() {
    }

    public PageCollectionResult(IResult iResult) {
        super(iResult);
    }

    public PageCollectionResult(IResult iResult, SimplePageData<Collection<T>> pageData) {
        super(iResult, pageData);
    }

    public PageCollectionResult(IPageResult<Collection<T>, SimplePageData<Collection<T>>> iPageResult) {
        super(iPageResult);
    }

    public PageCollectionResult(IPageResult<Collection<T>, SimplePageData<Collection<T>>> iPageResult, Collection<T> list) {
        super(iPageResult, list);
    }

    public PageCollectionResult(IResult iResult, SimplePageData<Collection<T>> pageData, Collection<T> list) {
        this(iResult, pageData);
        if (this.data != null) {
            this.data.setRows(list);
        }
    }

    public PageCollectionResult(IResult iResult, long pageNumber, long pageSize) {
        this(iResult, new SimplePageData<Collection<T>>(pageNumber, pageSize));
    }

    public PageCollectionResult(IResult iResult, long pageNumber, long pageSize, Collection<T> list) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize));
        this.data.setPageNumber(pageNumber);
        this.data.setPageSize(pageSize);
        this.data.setRows(list);
    }

    public PageCollectionResult(IResult iResult, long pageNumber, long pageSize, long total) {
        this(iResult, new SimplePageData<>(pageNumber, pageSize, total));
        this.data.setPageNumber(pageNumber);
        this.data.setPageSize(pageSize);
        this.data.setTotal(total);
    }

    public PageCollectionResult(IResult iResult, long pageNumber, long pageSize, long total, Collection<T> list) {
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
        public static PageCollectionResult success() {
            return new PageCollectionResult(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static PageCollectionResult success(long pageNumber, long pageSize) {
            return new PageCollectionResult(CommonResultCode.SUCCESS, pageNumber, pageSize);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> success(long pageNumber, long pageSize, Collection<T> list) {
            return new PageCollectionResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, list);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> success(long pageNumber, long pageSize, long total) {
            return new PageCollectionResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, total);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> success(long pageNumber, long pageSize, long total, Collection<T> list) {
            return new PageCollectionResult<T>(CommonResultCode.SUCCESS, pageNumber, pageSize, total, list);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> success(SimplePageData<Collection<T>> pageData) {
            return new PageCollectionResult(CommonResultCode.SUCCESS, pageData);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> success(SimplePageData<Collection<T>> pageData, Collection<T> list) {
            return new PageCollectionResult<T>(CommonResultCode.SUCCESS, pageData, list);
        }

        /**
         * 默认请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static PageCollectionResult error() {
            return new PageCollectionResult(CommonResultCode.FAIL);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageCollectionResult error(long pageNumber, long pageSize) {
            return new PageCollectionResult(CommonResultCode.FAIL, pageNumber, pageSize);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> error(long pageNumber, long pageSize, Collection<T> list) {
            return new PageCollectionResult<T>(CommonResultCode.FAIL, pageNumber, pageSize, list);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageCollectionResult error(long pageNumber, long pageSize, long total) {
            return new PageCollectionResult(CommonResultCode.FAIL, pageNumber, pageSize, total);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> error(long pageNumber, long pageSize, long total, Collection<T> list) {
            return new PageCollectionResult<T>(CommonResultCode.FAIL, pageNumber, pageSize, total, list);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> error(SimplePageData<Collection<T>> pageData) {
            return new PageCollectionResult(CommonResultCode.FAIL, pageData);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> error(SimplePageData<Collection<T>> pageData, Collection<T> list) {
            return new PageCollectionResult<T>(CommonResultCode.FAIL, pageData, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult result(IPageResult<Collection<T>, SimplePageData<Collection<T>>> iPageResult) {
            return new PageCollectionResult(iPageResult);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> result(IPageResult<Collection<T>, SimplePageData<Collection<T>>> iPageResult, Collection<T> list) {
            return new PageCollectionResult<T>(iPageResult, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> result(IResult iResult, long pageNumber, long pageSize, Collection<T> list) {
            return new PageCollectionResult<T>(iResult, pageNumber, pageSize, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageCollectionResult result(IResult iResult, long pageNumber, long pageSize, long total) {
            return new PageCollectionResult(iResult, pageNumber, pageSize, total);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> result(IResult iResult, long pageNumber, long pageSize, long total, Collection<T> list) {
            return new PageCollectionResult<T>(iResult, pageNumber, pageSize, total, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult result(IResult iResult, SimplePageData<Collection<T>> pageData) {
            return new PageCollectionResult(iResult, pageData);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> result(IResult iResult, SimplePageData<Collection<T>> pageData, Collection<T> list) {
            return new PageCollectionResult<T>(iResult, pageData, list);
        }

    }

}
