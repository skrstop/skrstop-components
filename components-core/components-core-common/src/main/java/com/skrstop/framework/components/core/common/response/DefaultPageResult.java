package com.skrstop.framework.components.core.common.response;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractPageResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.PageInfo;
import com.skrstop.framework.components.core.common.response.page.SimplePageInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
public class DefaultPageResult extends AbstractPageResult implements IPageResult {

    private static final long serialVersionUID = 9019006933206591705L;

    public DefaultPageResult() {

    }

    public DefaultPageResult(IResult IResult) {
        super(IResult);
    }

    public DefaultPageResult(IPageResult IPageResult) {
        super(IPageResult);
    }

    public DefaultPageResult(IResult IResult, PageInfo pageInfo) {
        super(IResult, pageInfo);
    }

    public DefaultPageResult(IResult IResult, long pageNum, long pageSize) {
        super(IResult, new SimplePageInfo(pageNum, pageSize));
    }

    public DefaultPageResult(IResult IResult, long pageNum, long pageSize, long total) {
        super(IResult, new SimplePageInfo(pageNum, pageSize, total));
    }

    public static class Builder {

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult success() {
            return new DefaultPageResult(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult success(long pageNum, long pageSize) {
            return new DefaultPageResult(CommonResultCode.SUCCESS, pageNum, pageSize);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult success(long pageNum, long pageSize, long total) {
            return new DefaultPageResult(CommonResultCode.SUCCESS, pageNum, pageSize, total);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult success(PageInfo pageInfo) {
            return new DefaultPageResult(CommonResultCode.SUCCESS, pageInfo);
        }


        /**
         * 默认请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult error() {
            return new DefaultPageResult(CommonResultCode.FAIL);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult error(long pageNum, long pageSize) {
            return new DefaultPageResult(CommonResultCode.FAIL, pageNum, pageSize);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult error(long pageNum, long pageSize, long total) {
            return new DefaultPageResult(CommonResultCode.FAIL, pageNum, pageSize, total);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult error(PageInfo pageInfo) {
            return new DefaultPageResult(CommonResultCode.FAIL, pageInfo);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult result(IResult IResult) {
            return new DefaultPageResult(IResult);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult result(IResult IResult, long pageNum, long pageSize) {
            return new DefaultPageResult(IResult, pageNum, pageSize);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult result(IResult IResult, long pageNum, long pageSize, long total) {
            return new DefaultPageResult(IResult, pageNum, pageSize, total);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult result(IResult IResult, PageInfo pageInfo) {
            return new DefaultPageResult(IResult, pageInfo);
        }

    }

}
