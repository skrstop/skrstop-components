package com.skrstop.framework.components.core.common.response;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractPageResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IDataPageCollectionResult;
import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.PageData;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
public class PageCollectionResult<T> extends AbstractPageResult<Collection<T>> implements IDataPageCollectionResult<T> {

    public PageCollectionResult() {
    }

    public PageCollectionResult(IResult IResult) {
        super(IResult);
    }

    public PageCollectionResult(IResult IResult, PageData<Collection<T>> pageData) {
        super(IResult, pageData);
    }

    public PageCollectionResult(IPageResult<Collection<T>> IPageResult) {
        super(IPageResult);
    }

    public PageCollectionResult(IPageResult<Collection<T>> IPageResult, Collection<T> list) {
        super(IPageResult, list);
    }

    public PageCollectionResult(IResult IResult, PageData<Collection<T>> pageData, Collection<T> list) {
        super(IResult, pageData, list);
    }

    public PageCollectionResult(IResult IResult, long pageNum, long pageSize) {
        super(IResult, pageNum, pageSize);
    }

    public PageCollectionResult(IResult IResult, long pageNum, long pageSize, Collection<T> list) {
        super(IResult, pageNum, pageSize, list);
    }

    public PageCollectionResult(IResult IResult, long pageNum, long pageSize, long total) {
        super(IResult, pageNum, pageSize, total);
    }

    public PageCollectionResult(IResult IResult, long pageNum, long pageSize, long total, Collection<T> list) {
        super(IResult, pageNum, pageSize, total, list);
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
        public static PageCollectionResult success(long pageNum, long pageSize) {
            return new PageCollectionResult(CommonResultCode.SUCCESS, pageNum, pageSize);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> success(long pageNum, long pageSize, Collection<T> list) {
            return new PageCollectionResult<T>(CommonResultCode.SUCCESS, pageNum, pageSize, list);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> success(long pageNum, long pageSize, long total) {
            return new PageCollectionResult<T>(CommonResultCode.SUCCESS, pageNum, pageSize, total);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> success(long pageNum, long pageSize, long total, Collection<T> list) {
            return new PageCollectionResult<T>(CommonResultCode.SUCCESS, pageNum, pageSize, total, list);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static PageCollectionResult success(PageData pageData) {
            return new PageCollectionResult(CommonResultCode.SUCCESS, pageData);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> success(PageData pageData, Collection<T> list) {
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
        public static PageCollectionResult error(long pageNum, long pageSize) {
            return new PageCollectionResult(CommonResultCode.FAIL, pageNum, pageSize);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> error(long pageNum, long pageSize, Collection<T> list) {
            return new PageCollectionResult<T>(CommonResultCode.FAIL, pageNum, pageSize, list);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageCollectionResult error(long pageNum, long pageSize, long total) {
            return new PageCollectionResult(CommonResultCode.FAIL, pageNum, pageSize, total);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> error(long pageNum, long pageSize, long total, Collection<T> list) {
            return new PageCollectionResult<T>(CommonResultCode.FAIL, pageNum, pageSize, total, list);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageCollectionResult error(PageData pageData) {
            return new PageCollectionResult(CommonResultCode.FAIL, pageData);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> error(PageData pageData, Collection<T> list) {
            return new PageCollectionResult<T>(CommonResultCode.FAIL, pageData, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageCollectionResult result(IPageResult IPageResult) {
            return new PageCollectionResult(IPageResult);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> result(IPageResult IPageResult, Collection<T> list) {
            return new PageCollectionResult<T>(IPageResult, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> result(IResult IResult, long pageNum, long pageSize, Collection<T> list) {
            return new PageCollectionResult<T>(IResult, pageNum, pageSize, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageCollectionResult result(IResult IResult, long pageNum, long pageSize, long total) {
            return new PageCollectionResult(IResult, pageNum, pageSize, total);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> result(IResult IResult, long pageNum, long pageSize, long total, Collection<T> list) {
            return new PageCollectionResult<T>(IResult, pageNum, pageSize, total, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageCollectionResult result(IResult IResult, PageData pageData) {
            return new PageCollectionResult(IResult, pageData);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> result(IResult IResult, PageData pageData, Collection<T> list) {
            return new PageCollectionResult<T>(IResult, pageData, list);
        }

    }

}
