package com.zoe.framework.components.core.common.response;

import com.zoe.framework.components.core.common.response.abstracts.AbstractPageResult;
import com.zoe.framework.components.core.common.response.common.CommonResultCode;
import com.zoe.framework.components.core.common.response.core.IDataPageLinkedSetResult;
import com.zoe.framework.components.core.common.response.core.IPageResult;
import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.core.common.response.page.PageInfo;
import com.zoe.framework.components.core.common.response.page.SimplePageInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
public class PageLinkedSetResult<T> extends AbstractPageResult implements IDataPageLinkedSetResult<T> {

    LinkedHashSet<T> data;

    public PageLinkedSetResult() {
        this.data = new LinkedHashSet<>();
    }

    public PageLinkedSetResult(IResult IResult) {
        super(IResult);
    }

    public PageLinkedSetResult(IPageResult IPageResult) {
        super(IPageResult);
    }

    public PageLinkedSetResult(IPageResult IPageResult, LinkedHashSet<T> set) {
        super(IPageResult);
        this.data = set;
    }

    public PageLinkedSetResult(IResult IResult, PageInfo pageInfo) {
        super(IResult, pageInfo);
    }

    public PageLinkedSetResult(IResult IResult, PageInfo pageInfo, LinkedHashSet<T> set) {
        super(IResult, pageInfo);
        this.data = set;
    }

    public PageLinkedSetResult(IResult IResult, long pageNum, long pageSize) {
        super(IResult, new SimplePageInfo(pageNum, pageSize));
    }

    public PageLinkedSetResult(IResult IResult, long pageNum, long pageSize, LinkedHashSet<T> set) {
        super(IResult, new SimplePageInfo(pageNum, pageSize));
        this.data = set;
    }

    public PageLinkedSetResult(IResult IResult, long pageNum, long pageSize, long total) {
        super(IResult, new SimplePageInfo(pageNum, pageSize, total));
    }

    public PageLinkedSetResult(IResult IResult, long pageNum, long pageSize, long total, LinkedHashSet<T> set) {
        super(IResult, new SimplePageInfo(pageNum, pageSize, total));
        this.data = set;
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
        public static PageLinkedSetResult success(PageInfo pageInfo) {
            return new PageLinkedSetResult(CommonResultCode.SUCCESS, pageInfo);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> success(PageInfo pageInfo, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(CommonResultCode.SUCCESS, pageInfo, set);
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
        public static PageLinkedSetResult error(PageInfo pageInfo) {
            return new PageLinkedSetResult(CommonResultCode.FAIL, pageInfo);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> error(PageInfo pageInfo, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(CommonResultCode.FAIL, pageInfo, set);
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
        public static PageLinkedSetResult result(IResult IResult, PageInfo pageInfo) {
            return new PageLinkedSetResult(IResult, pageInfo);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageLinkedSetResult<T> result(IResult IResult, PageInfo pageInfo, LinkedHashSet<T> set) {
            return new PageLinkedSetResult<T>(IResult, pageInfo, set);
        }

    }

}
