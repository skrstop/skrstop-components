package com.zoe.framework.components.core.common.response;

import com.zoe.framework.components.core.common.response.abstracts.AbstractPageResult;
import com.zoe.framework.components.core.common.response.common.CommonResultCode;
import com.zoe.framework.components.core.common.response.core.IDataPageSetResult;
import com.zoe.framework.components.core.common.response.core.IPageResult;
import com.zoe.framework.components.core.common.response.core.IResult;
import com.zoe.framework.components.core.common.response.page.PageInfo;
import com.zoe.framework.components.core.common.response.page.SimplePageInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
public class PageSetResult<T> extends AbstractPageResult implements IDataPageSetResult<T> {

    Set<T> data;

    public PageSetResult() {
        this.data = new HashSet<>();
    }

    public PageSetResult(IResult IResult) {
        super(IResult);
    }

    public PageSetResult(IPageResult IPageResult) {
        super(IPageResult);
    }

    public PageSetResult(IPageResult IPageResult, Set<T> set) {
        super(IPageResult);
        this.data = set;
    }

    public PageSetResult(IResult IResult, PageInfo pageInfo) {
        super(IResult, pageInfo);
    }

    public PageSetResult(IResult IResult, PageInfo pageInfo, Set<T> set) {
        super(IResult, pageInfo);
        this.data = set;
    }

    public PageSetResult(IResult IResult, long pageNum, long pageSize) {
        super(IResult, new SimplePageInfo(pageNum, pageSize));
    }

    public PageSetResult(IResult IResult, long pageNum, long pageSize, Set<T> set) {
        super(IResult, new SimplePageInfo(pageNum, pageSize));
        this.data = set;
    }

    public PageSetResult(IResult IResult, long pageNum, long pageSize, long total) {
        super(IResult, new SimplePageInfo(pageNum, pageSize, total));
    }

    public PageSetResult(IResult IResult, long pageNum, long pageSize, long total, Set<T> set) {
        super(IResult, new SimplePageInfo(pageNum, pageSize, total));
        this.data = set;
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
        public static PageSetResult success(long pageNum, long pageSize) {
            return new PageSetResult(CommonResultCode.SUCCESS, pageNum, pageSize);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> success(long pageNum, long pageSize, Set<T> set) {
            return new PageSetResult<T>(CommonResultCode.SUCCESS, pageNum, pageSize, set);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> success(long pageNum, long pageSize, long total) {
            return new PageSetResult<T>(CommonResultCode.SUCCESS, pageNum, pageSize, total);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> success(long pageNum, long pageSize, long total, Set<T> set) {
            return new PageSetResult<T>(CommonResultCode.SUCCESS, pageNum, pageSize, total, set);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static PageSetResult success(PageInfo pageInfo) {
            return new PageSetResult(CommonResultCode.SUCCESS, pageInfo);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> success(PageInfo pageInfo, Set<T> set) {
            return new PageSetResult<T>(CommonResultCode.SUCCESS, pageInfo, set);
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
        public static PageSetResult error(long pageNum, long pageSize) {
            return new PageSetResult(CommonResultCode.FAIL, pageNum, pageSize);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> error(long pageNum, long pageSize, Set<T> set) {
            return new PageSetResult<T>(CommonResultCode.FAIL, pageNum, pageSize, set);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageSetResult error(long pageNum, long pageSize, long total) {
            return new PageSetResult(CommonResultCode.FAIL, pageNum, pageSize, total);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> error(long pageNum, long pageSize, long total, Set<T> set) {
            return new PageSetResult<T>(CommonResultCode.FAIL, pageNum, pageSize, total, set);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static PageSetResult error(PageInfo pageInfo) {
            return new PageSetResult(CommonResultCode.FAIL, pageInfo);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> error(PageInfo pageInfo, Set<T> set) {
            return new PageSetResult<T>(CommonResultCode.FAIL, pageInfo, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageSetResult result(IPageResult IPageResult) {
            return new PageSetResult(IPageResult);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> result(IPageResult IPageResult, Set<T> set) {
            return new PageSetResult<T>(IPageResult, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> result(IResult IResult, long pageNum, long pageSize, Set<T> set) {
            return new PageSetResult<T>(IResult, pageNum, pageSize, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageSetResult result(IResult IResult, long pageNum, long pageSize, long total) {
            return new PageSetResult(IResult, pageNum, pageSize, total);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> result(IResult IResult, long pageNum, long pageSize, long total, Set<T> set) {
            return new PageSetResult<T>(IResult, pageNum, pageSize, total, set);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static PageSetResult result(IResult IResult, PageInfo pageInfo) {
            return new PageSetResult(IResult, pageInfo);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageSetResult<T> result(IResult IResult, PageInfo pageInfo, Set<T> set) {
            return new PageSetResult<T>(IResult, pageInfo, set);
        }

    }

}
