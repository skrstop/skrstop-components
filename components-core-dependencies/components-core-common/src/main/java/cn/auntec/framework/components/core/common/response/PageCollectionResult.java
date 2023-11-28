package cn.auntec.framework.components.core.common.response;

import cn.auntec.framework.components.core.common.response.abstracts.AbstractPageResult;
import cn.auntec.framework.components.core.common.response.common.CommonResultCode;
import cn.auntec.framework.components.core.common.response.core.IDataPageCollectionResult;
import cn.auntec.framework.components.core.common.response.core.IPageResult;
import cn.auntec.framework.components.core.common.response.core.IResult;
import cn.auntec.framework.components.core.common.response.page.PageInfo;
import cn.auntec.framework.components.core.common.response.page.SimplePageInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
public class PageCollectionResult<T> extends AbstractPageResult implements IDataPageCollectionResult<T> {

    Collection<T> data;

    public PageCollectionResult() {
        this.data = new ArrayList<>();
    }

    public PageCollectionResult(IResult IResult) {
        super(IResult);
    }

    public PageCollectionResult(IPageResult IPageResult) {
        super(IPageResult);
    }

    public PageCollectionResult(IPageResult IPageResult, Collection<T> list) {
        super(IPageResult);
        this.data = list;
    }

    public PageCollectionResult(IResult IResult, PageInfo pageInfo) {
        super(IResult, pageInfo);
    }

    public PageCollectionResult(IResult IResult, PageInfo pageInfo, Collection<T> list) {
        super(IResult, pageInfo);
        this.data = list;
    }

    public PageCollectionResult(IResult IResult, long pageNum, long pageSize) {
        super(IResult, new SimplePageInfo(pageNum, pageSize));
    }

    public PageCollectionResult(IResult IResult, long pageNum, long pageSize, Collection<T> list) {
        super(IResult, new SimplePageInfo(pageNum, pageSize));
        this.data = list;
    }

    public PageCollectionResult(IResult IResult, long pageNum, long pageSize, long total) {
        super(IResult, new SimplePageInfo(pageNum, pageSize, total));
    }

    public PageCollectionResult(IResult IResult, long pageNum, long pageSize, long total, Collection<T> list) {
        super(IResult, new SimplePageInfo(pageNum, pageSize, total));
        this.data = list;
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
        public static PageCollectionResult success(PageInfo pageInfo) {
            return new PageCollectionResult(CommonResultCode.SUCCESS, pageInfo);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> success(PageInfo pageInfo, Collection<T> list) {
            return new PageCollectionResult<T>(CommonResultCode.SUCCESS, pageInfo, list);
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
        public static PageCollectionResult error(PageInfo pageInfo) {
            return new PageCollectionResult(CommonResultCode.FAIL, pageInfo);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> error(PageInfo pageInfo, Collection<T> list) {
            return new PageCollectionResult<T>(CommonResultCode.FAIL, pageInfo, list);
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
        public static PageCollectionResult result(IResult IResult, PageInfo pageInfo) {
            return new PageCollectionResult(IResult, pageInfo);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static <T> PageCollectionResult<T> result(IResult IResult, PageInfo pageInfo, Collection<T> list) {
            return new PageCollectionResult<T>(IResult, pageInfo, list);
        }

    }

}
