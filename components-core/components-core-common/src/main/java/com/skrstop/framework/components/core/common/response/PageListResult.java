package com.skrstop.framework.components.core.common.response;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractPageResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IDataPageListResult;
import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.PageData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
public class PageListResult<T> extends AbstractPageResult<List<T>> implements IDataPageListResult<T> {

    public PageListResult() {
    }

    public PageListResult(IResult IResult) {
        super(IResult);
    }

    public PageListResult(IResult IResult, PageData pageData) {
        super(IResult, pageData);
    }

    public PageListResult(IPageResult IPageResult) {
        super(IPageResult);
    }

    public PageListResult(IPageResult IPageResult, List<T> list) {
        super(IPageResult, list);
    }

    public PageListResult(IResult IResult, PageData pageData, List<T> list) {
        super(IResult, pageData, list);
    }

    public PageListResult(IResult IResult, long pageNum, long pageSize) {
        super(IResult, pageNum, pageSize);
    }

    public PageListResult(IResult IResult, long pageNum, long pageSize, List<T> list) {
        super(IResult, pageNum, pageSize, list);
    }

    public PageListResult(IResult IResult, long pageNum, long pageSize, long total) {
        super(IResult, pageNum, pageSize, total);
    }

    public PageListResult(IResult IResult, long pageNum, long pageSize, long total, List<T> list) {
        super(IResult, pageNum, pageSize, total, list);
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
        public static PageListResult success(long pageNum, long pageSize) {
            return new PageListResult(CommonResultCode.SUCCESS, pageNum, pageSize);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> success(long pageNum, long pageSize, List<T> list) {
            return new PageListResult<T>(CommonResultCode.SUCCESS, pageNum, pageSize, list);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> success(long pageNum, long pageSize, long total) {
            return new PageListResult<T>(CommonResultCode.SUCCESS, pageNum, pageSize, total);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> success(long pageNum, long pageSize, long total, List<T> list) {
            return new PageListResult<T>(CommonResultCode.SUCCESS, pageNum, pageSize, total, list);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static PageListResult success(PageData pageData) {
            return new PageListResult(CommonResultCode.SUCCESS, pageData);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> success(PageData pageData, List<T> list) {
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
        public static PageListResult error(long pageNum, long pageSize) {
            return new PageListResult(CommonResultCode.FAIL, pageNum, pageSize);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> error(long pageNum, long pageSize, List<T> list) {
            return new PageListResult<T>(CommonResultCode.FAIL, pageNum, pageSize, list);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageListResult error(long pageNum, long pageSize, long total) {
            return new PageListResult(CommonResultCode.FAIL, pageNum, pageSize, total);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> error(long pageNum, long pageSize, long total, List<T> list) {
            return new PageListResult<T>(CommonResultCode.FAIL, pageNum, pageSize, total, list);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageListResult error(PageData pageData) {
            return new PageListResult(CommonResultCode.FAIL, pageData);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> error(PageData pageData, List<T> list) {
            return new PageListResult<T>(CommonResultCode.FAIL, pageData, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageListResult result(IPageResult IPageResult) {
            return new PageListResult(IPageResult);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> result(IPageResult IPageResult, List<T> list) {
            return new PageListResult<T>(IPageResult, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> result(IResult IResult, long pageNum, long pageSize, List<T> list) {
            return new PageListResult<T>(IResult, pageNum, pageSize, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageListResult result(IResult IResult, long pageNum, long pageSize, long total) {
            return new PageListResult(IResult, pageNum, pageSize, total);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> result(IResult IResult, long pageNum, long pageSize, long total, List<T> list) {
            return new PageListResult<T>(IResult, pageNum, pageSize, total, list);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageListResult result(IResult IResult, PageData pageData) {
            return new PageListResult(IResult, pageData);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageListResult<T> result(IResult IResult, PageData pageData, List<T> list) {
            return new PageListResult<T>(IResult, pageData, list);
        }

    }

}
